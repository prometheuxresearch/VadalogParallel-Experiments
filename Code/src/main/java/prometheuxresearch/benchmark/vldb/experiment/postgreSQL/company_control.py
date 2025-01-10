import psycopg2
import argparse
import time


def get_database_connection(database_name: str, user: str, password: str, host: str, port: str):
    """
    To open a connection to the db
    """
    try:
        conn = psycopg2.connect(
            dbname=database_name,
            user=user,
            password=password,
            host=host,
            port=port
        )
        return conn
    except psycopg2.Error as e:
        print("Error connecting to PostgreSQL:", e)
        return None


def run_query_and_commit(connection, query: str):
    """
    run a single query and commit it
    """
    cursor = connection.cursor()
    cursor.execute(query)
    connection.commit()
    cursor.close()


def init_tables(connection):
    """
    create initial tables
    """
    try:
        cursor = connection.cursor()
        # Create the "Own" table
        cursor.execute("""
            CREATE TABLE Own (
                frm integer,
                toN integer,
                ownership double precision
            );
        """)

        # Create index on "Own" table
        cursor.execute("""
                        CREATE INDEX OwnIndex ON Own (frm);
                        """)

        # Create the "control" view
        cursor.execute("""
                CREATE TABLE Control (
                    frm integer,
                    toN integer,
                    ownership double precision
                );
              """)

        # Create the "controlledShares" table
        cursor.execute("""
            CREATE TABLE ControlledShares (
                frm integer,
                toN integer,
                through integer,
                ownership double precision
            );
        """)

        # Create the "TotalControlledShares" table
        cursor.execute("""
            CREATE TABLE TotalControlledShares (
                frm integer,
                toN integer,
                ownership double precision
            );
        """)

        # Create index on "Own" table
        cursor.execute("""
                        CREATE INDEX TotalControlledSharesIndex 
                        ON TotalControlledShares (frm, toN);
                        """)

        connection.commit()
        cursor.close()
        print("Tables and Indexes created successfully.")

    except psycopg2.Error as e:
        print("Error initiating tables and views:", e)


def import_csv_to_own(connection, csv_file_path):
    try:
        cursor = connection.cursor()
        # Copy data frm CSV to the table Own
        cursor.execute("COPY own FROM '" + csv_file_path + "' DELIMITER ',' CSV HEADER;")
        connection.commit()
        cursor.close()
        print(f"Data imported from {csv_file_path} to Own Table successfully.")

    except psycopg2.Error as e:
        print("Error importing CSV data (own):", e)


def run_exit_rule(connection):
    """
    controlled_shares(X,Y,Y,W) :- own(X,Y,W), X<>Y.
    """
    try:
        query = """
                CREATE TABLE DeltaControlledShares AS
                SELECT frm AS frm, toN AS toN, toN as through, ownership AS ownership
                FROM Own
                WHERE frm <> toN;
                """
        run_query_and_commit(connection, query)

        query = """
                INSERT INTO ControlledShares (frm, toN, through, ownership)
                SELECT frm, toN, through, ownership
                FROM DeltaControlledShares;
                """
        run_query_and_commit(connection, query)


    except psycopg2.Error as e:
        print("Error running controlled_shares(X,Y,Y,W) :- own(X,Y,W), X<>Y.:", e)


def run_terminating_query(connection, query: str):
    """
    To use to verify a termination condition
    """
    cursor = connection.cursor()
    cursor.execute(query)
    row = cursor.fetchone()
    connection.commit()
    cursor.close()
    return int(row[0])


def run_query(connection, query: str):
    """
    To use to verify a termination condition
    """
    cursor = connection.cursor()
    cursor.execute(query)
    rows = cursor.fetchall()
    connection.commit()
    cursor.close()
    return rows


def run_recursion(connection):
    try:
        update_planner_stats(connection, "Control")
        update_planner_stats(connection, "ControlledShares")
        update_planner_stats(connection, "TotalControlledShares")

        terminating_query_1 = """
                              SELECT COUNT(*)
                              FROM DeltaControlledShares;
                              """

        n_delta_controlled_shares = run_terminating_query(connection, terminating_query_1)

        n_iteration = 1
        while n_delta_controlled_shares != 0:
            print("Iteration number: " + str(n_iteration))
            print("Running: TotalControlledShares(X,Y,P) :- ControlledShares(X,Y,W), P = msum(W).")

            # app table to build views on it
            query = """DROP TABLE IF EXISTS DCShares;"""
            run_query_and_commit(connection, query)

            query = """
                    CREATE TABLE DCShares AS
                    SELECT frm AS frm, toN AS toN, toN as through, ownership AS ownership
                    FROM DeltaControlledShares;
                    """
            run_query_and_commit(connection, query)

            # perform aggregation
            query = """
                    CREATE MATERIALIZED VIEW TotalControlledSharesApp AS
                    SELECT frm AS frm, toN AS toN, SUM (ownership) AS ownership
                    FROM DCShares
                    GROUP BY (frm,toN);
                    """
            run_query_and_commit(connection, query)

            # here we create two views: (1) update the controlled shares already existing; (2) create the ones
            # non-existing

            query = """
                    CREATE MATERIALIZED VIEW TotalControlledSharesUpdated AS
                    SELECT t1.frm AS frm, t2.frm AS toN, (t1.ownership + t2.ownership) AS ownership
                    FROM TotalControlledSharesApp AS t1, TotalControlledShares AS t2
                    WHERE t1.frm = t2.frm AND t1.toN = t2.toN;
                    """
            run_query_and_commit(connection, query)

            query = """
                    CREATE MATERIALIZED VIEW TotalControlledSharesCreated AS
                    SELECT t1.frm AS frm, t1.toN AS toN, t1.ownership AS ownership
                    FROM TotalControlledSharesApp AS t1 LEFT JOIN TotalControlledShares AS t2
                    ON t1.frm = t2.frm AND t1.toN=t2.toN
                    WHERE t2.frm IS NULL AND t2.toN IS NULL;
                    """
            run_query_and_commit(connection, query)

            query = """
                    CREATE MATERIALIZED VIEW TotalControlledSharesDelta AS
                    SELECT t1.frm AS frm, t1.toN AS toN, t1.ownership AS ownership
                    FROM TotalControlledSharesUpdated AS t1
                    UNION 
                    SELECT t2.frm AS frm, t2.toN AS toN, t2.ownership AS ownership
                    FROM TotalControlledSharesCreated AS t2;
                    """
            run_query_and_commit(connection, query)

            query = """
                    UPDATE TotalControlledShares 
                    SET ownership = t.ownership
                    FROM TotalControlledSharesUpdated AS t
                    WHERE TotalControlledShares.frm = t.frm AND TotalControlledShares.toN = t.toN;
                    """
            run_query_and_commit(connection, query)

            query = """
                    INSERT INTO TotalControlledShares (frm, toN, ownership)
                    SELECT frm, toN, ownership
                    FROM TotalControlledSharesCreated;
                    """
            run_query_and_commit(connection, query)

            update_planner_stats(connection, "TotalControlledShares")

            print("Running: Control(X,Y,P) :- TotalControlledShares(X,Y,P), P>0.5.")

            query = """
                    CREATE MATERIALIZED VIEW DeltaControl AS
                    SELECT *
                    FROM TotalControlledSharesDelta
                    WHERE ownership>=0.5;
                    """
            run_query_and_commit(connection, query)

            query = """
                    INSERT INTO Control (frm, toN, ownership)
                    SELECT frm, toN, ownership
                    FROM DeltaControl;
                    """
            run_query_and_commit(connection, query)

            update_planner_stats(connection, "Control")

            print("Running: ControlledShares(X,Y,Z,W) :- Control(X,Z,_), Own(Z,Y,W), X<>Z, Z<>Y.")

            query = """
                    CREATE MATERIALIZED VIEW DeltaControlledSharesApp AS
                    SELECT DISTINCT t1.frm AS frm, t2.toN AS toN, t1.toN AS through, t2.ownership AS ownership
                    FROM DeltaControl AS t1, Own AS t2 
                    WHERE t1.toN = t2.frm AND t1.frm <> t2.frm AND t1.frm<>t2.toN;
                    """
            run_query_and_commit(connection, query)

            query = """DROP TABLE IF EXISTS DeltaControlledShares;"""
            run_query_and_commit(connection, query)

            # extract controlled shares delta
            query = """
                    CREATE TABLE DeltaControlledShares AS
                    SELECT * FROM DeltaControlledSharesApp
                    EXCEPT
                    SELECT * FROM ControlledShares;
                    """
            run_query_and_commit(connection, query)

            n_delta_controlled_shares = run_terminating_query(connection, terminating_query_1)

            query = """
                    INSERT INTO ControlledShares (frm, toN, through, ownership)
                    SELECT frm, toN, through, ownership
                    FROM DeltaControlledShares;
                    """
            run_query_and_commit(connection, query)

            query = """
                    DROP MATERIALIZED VIEW IF EXISTS TotalControlledSharesApp CASCADE;
                    DROP MATERIALIZED VIEW IF EXISTS TotalControlledSharesUpdated CASCADE;
                    DROP MATERIALIZED VIEW IF EXISTS TotalControlledSharesCreated CASCADE;
                    DROP MATERIALIZED VIEW IF EXISTS TotalControlledSharesDelta CASCADE;
                    DROP MATERIALIZED VIEW IF EXISTS DeltaControl CASCADE;
                    DROP MATERIALIZED VIEW IF EXISTS DeltaControlledSharesApp CASCADE;
            """
            run_query_and_commit(connection, query)
            update_planner_stats(connection, "ControlledShares")

            n_iteration = n_iteration + 1
            print("New control relationship found: " + str(n_delta_controlled_shares))

    except psycopg2.Error as e:
        print("Error running recursion", e)


def count_control_rows(connection):
    cursor = None
    try:
        query = """
              SELECT COUNT(*)
              FROM TotalControlledShares
              WHERE ownership >= 0.5;
              """
        cursor = connection.cursor()
        cursor.execute(query)
        row = cursor.fetchone()
        connection.commit()
        return int(row[0])

    except psycopg2.Error as e:
        print("Error counting control rows:", e)
    finally:
        if cursor is not None:
            cursor.close()


def update_planner_stats(connection, table_name: str):
    # Create a cursor
    cursor = connection.cursor()

    try:
        # Execute the ANALYZE command for a specific table
        cursor.execute(f"ANALYZE {table_name};")

        # Commit the transaction
        connection.commit()
    except psycopg2.Error as e:
        print("Error when updating the table stats:", e)
        connection.rollback()  # Rollback in case of an error
    finally:
        cursor.close()


def drop_all_tables_and_views(connection):
    try:
        cursor = connection.cursor()
        cursor.execute("""DROP TABLE IF EXISTS Own CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS Control CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS DeltaControlledShares CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS ControlledShares CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS TotalControlledShares CASCADE;""")

        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS DeltaControl CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS DeltaControlledShares CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS TotalControlledSharesApp CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS TotalControlledSharesDelta CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS TotalControlledSharesUpdated CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS TotalControlledSharesCreated CASCADE;""")
        connection.commit()
        cursor.close()
        print("Tables and Views deleted successfully.")

    except psycopg2.Error as e:
        print("Error initiating tables and views:", e)


# BEGINNING OF THE PROGRAM

# read args frm command line

parser = argparse.ArgumentParser(description='Connect to PostgreSQL and execute a query.')
parser.add_argument('--input_csv', required=True, help='Complete CSV file path to read frm')
parser.add_argument('--database', required=True, help='Database name')
parser.add_argument('--user', required=True, help='Database user')
parser.add_argument('--password', required=True, help='Database password')
parser.add_argument('--host', required=True, help='Database host')
parser.add_argument('--port', required=True, help='Database port')
args = parser.parse_args()

# connect to the db
print("Connecting to the database PostgreSQL.")
connection = get_database_connection(args.database, args.user, args.password, args.host, args.port)

if connection:
    print("Connection OK.")
    try:
        print("Dropping tables and views if present in the database.")
        drop_all_tables_and_views(connection)

        print("Initializing tables and indexes.")
        init_tables(connection)

        print("Reading input files and importing them.")
        import_csv_to_own(connection, args.input_csv)

        # Record the start time
        start_time = time.time()

        print("Running exit rule: controlled_shares(X,Y,Y,W) :- own(X,Y,W), X<>Y.")
        run_exit_rule(connection)

        print("Running recursive rules...")
        run_recursion(connection)
        print("Recursion finished.")

        n_control = count_control_rows(connection)

        # Record the end time
        end_time = time.time()

        print("Dropping tables and views if present in the database.")
        drop_all_tables_and_views(connection)

        print("Number of control relationship found: " + str(n_control))
        # Calculate the elapsed time in seconds
        elapsed_time = end_time - start_time

        # Print the elapsed time
        print(f"Execution time: {elapsed_time:.4f} seconds")

    except psycopg2.Error as e:
        print("Error connecting to the db:", e)
    finally:
        connection.close()
