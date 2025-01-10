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
        # Create the "CreditExposure" table
        cursor.execute("""
            CREATE TABLE CreditExposure (
                entityA integer,
                probability double precision
            );
        """)

        cursor.execute("""
            CREATE TABLE Loan (
                entityB integer,
                entityC integer,
                lgd double precision
            );
        """)

        cursor.execute("""
            CREATE TABLE Security (
                entityB integer,
                entityC integer,
                s double precision
            );
        """)

        cursor.execute("""
            CREATE TABLE dflt (
                entityB integer,
                entityA integer,
                dflt text,
                reasoningKey text
            );
        """)

        cursor.execute("""
                        CREATE INDEX LoanIndex 
                        ON Loan (entityB);
                        """)

        cursor.execute("""
                        CREATE INDEX SecurityIndex 
                        ON Loan (entityB);
                        """)

        cursor.execute("""
                        CREATE INDEX dfltIndex 
                        ON dflt (reasoningKey);
                        """)

        connection.commit()
        cursor.close()
        print("Tables and Indexes created successfully.")

    except psycopg2.Error as e:
        print("Error initiating tables and views:", e)


def print_output(connection):
    """
    To use to verify a termination condition
    """
    query = """
            SELECT *
            FROM dflt;
            """
    cursor = connection.cursor()
    cursor.execute(query)
    rows = cursor.fetchall()
    connection.commit()
    cursor.close()
    print(rows)


def import_csv(connection, csv_file_path, table_name: str):
    try:
        cursor = connection.cursor()
        cursor.execute("COPY " + table_name + " FROM '" + csv_file_path + "' DELIMITER ',' CSV HEADER;")
        connection.commit()
        cursor.close()
        print(f"Data imported frm {csv_file_path} to " + table_name + " Table successfully.")

    except psycopg2.Error as e:
        print("Error importing CSV data:", e)


def labelled_null_generation_function(connection):
    try:
        query = """
                CREATE EXTENSION IF NOT EXISTS pgcrypto;
                
                CREATE OR REPLACE FUNCTION calculate_md5_hash(input_string text)
                RETURNS text AS $$
                BEGIN
                  RETURN md5(input_string);
                END;
                $$ LANGUAGE plpgsql;
                
                """
        run_query_and_commit(connection, query)

        query = """
                CREATE OR REPLACE FUNCTION generate_null(seed text, index integer)
                RETURNS text AS $$
                BEGIN
                  RETURN 'z_' || CAST(index AS text) || '_' || calculate_md5_hash(seed);
                END;
                $$ LANGUAGE plpgsql;
                """
        run_query_and_commit(connection, query)

    except psycopg2.Error as e:
        print("Error while defining labelled null generation function.", e)


def reasoning_key_function(connection):
    try:
        query = """
                CREATE OR REPLACE FUNCTION reasoning_key(entityB integer, entityA integer, dflt text)
                RETURNS text AS $$
                BEGIN
                  RETURN calculate_md5_hash(CAST(entityB AS text) || CAST(entityA AS text) || 'z_1');
                END;
                $$ LANGUAGE plpgsql;
                """
        run_query_and_commit(connection, query)

    except psycopg2.Error as e:
        print("Error while defining reasoning key function.", e)


def run_exit_rule(connection):
    """
    dflt(A,A,D) :- creditExposure(A,P), P>0.5.
    """
    try:
        query = """
                CREATE MATERIALIZED VIEW DeltadfltApp AS 
                SELECT entityA AS entityB, entityA AS entityA,  
                generate_null((CAST(entityA AS text) || CAST(entityA AS text)), 2) AS dflt
                FROM CreditExposure 
                WHERE probability > 0.5; 
                """
        run_query_and_commit(connection, query)
        query = """
                 CREATE TABLE Deltadflt AS 
                 SELECT entityB AS entityB, entityA AS entityA,  dflt AS dflt,
                  reasoning_key(entityB, entityA, dflt) AS reasoningKey
                 FROM DeltadfltApp; 
                 """
        run_query_and_commit(connection, query)
        query = """
                INSERT INTO dflt (entityB, entityA, dflt, reasoningKey)
                SELECT *
                FROM Deltadflt;
                """
        run_query_and_commit(connection, query)

        query = """
                DROP MATERIALIZED VIEW IF EXISTS DeltadfltApp CASCADE;
                """
        run_query_and_commit(connection, query)

    except psycopg2.Error as e:
        print("Error running dflt(A,A,D) :- creditExposure(A,P), P>0.5.:", e)


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


def run_recursion(connection):
    try:
        update_planner_stats(connection, "dflt")

        terminating_query = """
                              SELECT COUNT(*)
                              FROM Deltadflt;
                              """

        n_delta_dflt = run_terminating_query(connection, terminating_query)
        n_iteration = 1
        while n_delta_dflt != 0:
            print("Iteration number: " + str(n_iteration))

            print("Running first recursive rule...")

            query = """
                    DROP TABLE IF EXISTS DD CASCADE;
                    """
            run_query_and_commit(connection, query)

            query = """
                     CREATE TABLE DD AS 
                     SELECT entityB AS entityB, entityA AS entityA,  dflt AS dflt,
                      reasoningKey AS reasoningKey
                     FROM Deltadflt; 
                     """
            run_query_and_commit(connection, query)

            query = """
                    CREATE MATERIALIZED VIEW DeltadfltApp1 AS 
                    SELECT t2.entityC AS entityC, t1.entityA AS entityA, 
                    generate_null((CAST(t2.entityC AS text) || CAST(t1.entityA AS text)), 2) AS dflt
                    FROM DD AS t1, Loan AS t2
                    WHERE t1.entityB = t2.entityB AND t2.lgd >= 0.5;
                    """
            run_query_and_commit(connection, query)

            query = """
                    CREATE MATERIALIZED VIEW Deltadflt1 AS 
                    SELECT t.entityC AS entityC, t.entityA AS entityA, t.dflt AS dflt, 
                    reasoning_key(entityC, entityA, dflt) AS reasoningKey
                    FROM DeltadfltApp1 AS t;
                    """
            run_query_and_commit(connection, query)

            print("Running second recursive rule...")

            query = """
                    CREATE MATERIALIZED VIEW DeltadfltApp2 AS 
                    SELECT t2.entityC AS entityC, t1.entityA AS entityA, 
                    generate_null((CAST(t2.entityC AS text) || CAST(t1.entityA AS text)), 2) AS dflt
                    FROM DD AS t1, Security AS t2
                    WHERE t1.entityB = t2.entityB AND t2.s >= 0.3;
                    """
            run_query_and_commit(connection, query)

            query = """
                    CREATE MATERIALIZED VIEW Deltadflt2 AS 
                    SELECT t.entityC AS entityC, t.entityA AS entityA, t.dflt AS dflt, 
                    reasoning_key(entityC, entityA, dflt) AS reasoningKey
                    FROM DeltadfltApp2 AS t;
                    """
            run_query_and_commit(connection, query)

            print("Computing delta...")

            query = """
                    CREATE MATERIALIZED VIEW DeltadfltApp AS
                    SELECT * FROM Deltadflt1
                    UNION
                    SELECT * FROM Deltadflt2;
                    """
            run_query_and_commit(connection, query)

            query = """
                    DROP TABLE IF EXISTS Deltadflt;
                    """
            run_query_and_commit(connection, query)

            query = """
                    CREATE TABLE Deltadflt AS
                    SELECT t1.entityC AS entityB, t1.entityA AS entityA, 
                    t1.dflt AS dflt, t1.reasoningKey AS reasoningKey
                    FROM DeltadfltApp AS t1 LEFT JOIN dflt AS t2 on t1.reasoningKey = t2.reasoningKey
                    WHERE t2.reasoningKey IS NULL;
                    
                    """
            run_query_and_commit(connection, query)

            query = """
                    INSERT INTO dflt (entityB, entityA, dflt, reasoningKey)
                    SELECT entityB, entityA, dflt, reasoningKey
                    FROM Deltadflt;
                    """
            run_query_and_commit(connection, query)

            query = """
                    DROP MATERIALIZED VIEW IF EXISTS DeltadfltApp CASCADE;
                    DROP MATERIALIZED VIEW IF EXISTS Deltadflt2 CASCADE;
                    DROP MATERIALIZED VIEW IF EXISTS DeltadfltApp2 CASCADE;
                    DROP MATERIALIZED VIEW IF EXISTS DeltadfltApp1 CASCADE;
                    DROP MATERIALIZED VIEW IF EXISTS Deltadflt1 CASCADE;
                    """
            run_query_and_commit(connection, query)

            update_planner_stats(connection, "dflt")
            update_planner_stats(connection, "Deltadflt")

            n_delta_dflt = run_terminating_query(connection, terminating_query)
            n_iteration = n_iteration + 1
            print("New dflt relationships found: " + str(n_delta_dflt))

    except psycopg2.Error as e:
        print("Error running recursion", e)


def count_dflt_rows(connection):
    cursor = None
    try:
        query = """
              SELECT COUNT(*)
              FROM dflt;
              """
        cursor = connection.cursor()
        cursor.execute(query)
        row = cursor.fetchone()
        connection.commit()
        return int(row[0])

    except psycopg2.Error as e:
        print("Error counting dflt rows:", e)
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
        cursor.execute("""DROP TABLE IF EXISTS CreditExposure CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS Loan CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS Security CASCADE;""")

        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS DeltadfltApp CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS DeltadfltApp1 CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS Deltadflt1 CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS DeltadfltApp2 CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS Deltadflt2 CASCADE;""")

        cursor.execute("""DROP TABLE IF EXISTS dflt CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS DD CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS Deltadflt CASCADE;""")

        connection.commit()
        cursor.close()
        print("Table and Views deleted successfully.")

    except psycopg2.Error as e:
        print("Error deleting tables and views:", e)


# read args frm command line

parser = argparse.ArgumentParser(description='Connect to PostgreSQL and execute a query.')
parser.add_argument('--input_credit_exposure', required=True, help='Complete CSV file path to read from')
parser.add_argument('--input_loan', required=True, help='Complete CSV file path to read frm')
parser.add_argument('--input_security', required=True, help='Complete CSV file path to read frm')
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
        print("Initializing custom functions for nulls.")
        labelled_null_generation_function(connection)
        reasoning_key_function(connection)

        print("Reading input files and importing them.")
        import_csv(connection, args.input_credit_exposure, "CreditExposure")
        import_csv(connection, args.input_loan, "Loan")
        import_csv(connection, args.input_security, "Security")

        # Record the start time
        start_time = time.time()

        print("Running exit rule: dflt(A,A,D) :- creditExposure(A,P), P>0.5.")
        run_exit_rule(connection)

        print("Running recursive rules...")
        run_recursion(connection)
        print("Recursion finished.")

        n_control = count_dflt_rows(connection)
        # print_output(connection)

        # Record the end time
        end_time = time.time()

        print("Dropping tables and views if present in the database.")
        drop_all_tables_and_views(connection)

        print("Number of dflts found: " + str(n_control))
        # Calculate the elapsed time in seconds
        elapsed_time = end_time - start_time

        # Print the elapsed time
        print(f"Execution time: {elapsed_time:.4f} seconds")

    except psycopg2.Error as e:
        print("Error connecting to the db:", e)
    finally:
        connection.close()
