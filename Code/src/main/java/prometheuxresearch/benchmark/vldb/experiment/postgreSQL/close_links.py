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

        cursor.execute("""
                CREATE TABLE MightCloseLink (
                    frm integer,
                    toN integer,
                    visited text,
                    ownership double precision
                );
              """)

        cursor.execute("""
                       CREATE INDEX MightCloseLinkIndex ON MightCloseLink (frm, toN);
                       """)

        cursor.execute("""
                CREATE TABLE MightCloseLinkContributor (
                    frm integer,
                    toN integer,
                    contributorNode integer,
                    visited text,
                    oldContributor double precision,
                    newContributor double precision
                );
              """)

        cursor.execute("""
                       CREATE INDEX MightCloseLinkContributorIndex ON MightCloseLinkContributor (frm, toN, contributorNode);
                       """)

        # Create the "CloseLink" table
        cursor.execute("""
            CREATE TABLE CloseLink (
                frm integer,
                toN integer
            );
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


def count_cl_rows(connection):
    cursor = None
    try:
        query = """
              SELECT COUNT(*)
              FROM CloseLink
              GROUP BY (frm, toN);
              """
        cursor = connection.cursor()
        cursor.execute(query)
        row = cursor.fetchone()
        connection.commit()
        return int(row[0])

    except psycopg2.Error as e:
        print("Error counting close link rows:", e)
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
        cursor.execute("""DROP TABLE IF EXISTS CloseLink CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS MightCloseLink CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS MClDelta CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS Own CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS MightCloseLinkContributor CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS MightCloseLinkDelta CASCADE;""")

        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkCreated CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkUpdated CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkContributorApp1 CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkContributorDelta CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkContributorUpdated CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkContributorCreated CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkApp CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkApp1 CASCADE;""")
        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkContributorApp CASCADE;""")

        connection.commit()
        cursor.close()
        print("Table and Views deleted successfully.")

    except psycopg2.Error as e:
        print("Error initiating tables and views:", e)


def run_exit_rule(connection):
    try:
        query = """
                CREATE TABLE MightCloseLinkDelta AS
                SELECT frm AS frm, toN AS toN, (',' || frm || ',' || toN || ',') AS visited, SUM(ownership) AS ownership
                FROM Own
                GROUP BY (frm, toN);
                """
        run_query_and_commit(connection, query)

        query = """
                CREATE MATERIALIZED VIEW MightCloseLinkContributorDelta AS
                SELECT frm AS frm, toN AS toN, toN AS contributorNode, (',' || frm || ',' || toN || ',') AS visited,
                0.0::double precision AS oldContributor, ownership AS newContributor
                FROM MightCloseLinkDelta;
                """
        run_query_and_commit(connection, query)

        query = """
                INSERT INTO MightCloseLink (frm, toN, visited, ownership)
                SELECT frm, toN, visited, ownership
                FROM MightCloseLinkDelta;
                """
        run_query_and_commit(connection, query)

        query = """
                INSERT INTO MightCloseLinkContributor (frm, toN, contributorNode, visited, oldContributor, newContributor)
                SELECT frm, toN, contributorNode, visited, oldContributor, newContributor
                FROM MightCloseLinkContributorDelta;
                """
        run_query_and_commit(connection, query)

        query = """
                DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkContributorDelta CASCADE;
                """
        run_query_and_commit(connection, query)


    except psycopg2.Error as e:
        print("Error running exit rule.", e)


def run_recursion(connection):
    try:
        update_planner_stats(connection, "MightCloseLink")
        update_planner_stats(connection, "MightCloseLinkContributor")

        terminating_query = """
                              SELECT COUNT (*)
                              FROM MightCloseLinkDelta;
                              """

        n_delta_mcl = run_terminating_query(connection, terminating_query)
        n_iteration = 1
        while n_delta_mcl != 0:
            print("Iteration number: " + str(n_iteration))
            print("Running recursive rules.")

            # app table to build views on it
            query = """DROP TABLE IF EXISTS MClDelta;"""
            run_query_and_commit(connection, query)

            query = """
                    CREATE TABLE MClDelta AS
                    SELECT frm AS frm, toN AS toN, visited AS visited, ownership AS ownership
                    FROM MightCloseLinkDelta;
                    """
            run_query_and_commit(connection, query)

            query = """
                    CREATE MATERIALIZED VIEW MightCloseLinkContributorApp1 AS 
                    SELECT t1.frm AS frm, t2.toN AS toN, t1.toN AS contributorNode, 
                    (t1.visited || ',' || t2.toN || ',') AS visited, 
                    (t1.ownership * t2.ownership) AS newContributor
                    FROM MClDelta AS t1 JOIN Own AS t2 ON t1.toN = t2.frm
                    WHERE NOT (t1.visited LIKE '%,' || t2.toN::text || ',%') AND 
                    t1.frm <> t2.toN;
                    """
            run_query_and_commit(connection, query)

            print("Running join.")

            query = """
                    CREATE MATERIALIZED VIEW MightCloseLinkContributorApp AS
                    SELECT t.frm, t.toN, t.contributorNode, ARRAY_TO_STRING(ARRAY_AGG(t.visited), '') AS visited, 
                    MAX(t.newContributor) AS newContributor
                    FROM MightCloseLinkContributorApp1 AS t
                    GROUP BY (t.frm, t.toN, t.contributorNode);
                    """
            run_query_and_commit(connection, query)

            print("Aggregating...")

            query = """
                    CREATE MATERIALIZED VIEW MightCloseLinkContributorUpdated AS
                    SELECT t1.frm AS frm, t2.toN AS toN, t1.contributorNode AS contributorNode, 
                    (t1.visited || t2.visited) AS visited, t1.newContributor AS newContributor, t2.newContributor as oldContributor
                    FROM MightCloseLinkContributorApp AS t1, MightCloseLinkContributor AS t2
                    WHERE t1.frm = t2.frm AND t1.toN = t2.toN AND t1.contributorNode = t2.contributorNode 
                    AND t1.newContributor > t2.newContributor;
                    """
            run_query_and_commit(connection, query)

            print("Aggregating...")

            query = """
                    CREATE MATERIALIZED VIEW MightCloseLinkContributorCreated AS
                    SELECT t1.frm AS frm, t1.toN AS toN, t1.contributorNode AS contributorNode, t1.visited AS visited,
                           t1.newContributor AS newContributor, 0 AS oldContributor
                    FROM MightCloseLinkContributorApp AS t1 LEFT JOIN MightCloseLinkContributor AS t2
                    ON t1.frm = t2.frm AND t1.toN = t2.toN AND t1.contributorNode = t2.contributorNode
                    WHERE t2.frm IS NULL AND t2.toN IS NULL AND t2.contributorNode IS NULL;
                    """
            run_query_and_commit(connection, query)

            print("Aggregating...")

            query = """
                    CREATE MATERIALIZED VIEW MightCloseLinkContributorDelta AS
                    SELECT t1.frm AS frm, t1.toN AS toN, t1.contributorNode AS contributorNode, t1.visited AS visited, 
                    t1.oldContributor AS oldContributor, t1.newContributor AS newContributor
                    FROM MightCloseLinkContributorCreated AS t1
                    UNION 
                    SELECT t2.frm AS frm, t2.toN AS toN, t2.contributorNode AS contributorNode, t2.visited AS visited,
                    t2.oldContributor AS oldContributor, t2.newContributor AS newContributor
                    FROM MightCloseLinkContributorUpdated AS t2;
                    """
            run_query_and_commit(connection, query)

            print("Running...")

            query = """
                    UPDATE MightCloseLinkContributor
                    SET visited = t.visited, oldContributor = t.oldContributor, newContributor = t.newContributor
                    FROM MightCloseLinkContributorUpdated AS t
                    WHERE MightCloseLinkContributor.frm = t.frm AND MightCloseLinkContributor.toN = t.toN AND MightCloseLinkContributor.contributorNode = t.contributorNode;
                    """
            run_query_and_commit(connection, query)

            query = """
                    INSERT INTO MightCloseLinkContributor (frm, toN, contributorNode, visited, oldContributor, newContributor)
                    SELECT frm, toN, contributorNode, visited, oldContributor, newContributor
                    FROM MightCloseLinkContributorCreated;
                    """
            run_query_and_commit(connection, query)

            print("Finished computing and updating contributors tables.")

            query = """
                    CREATE MATERIALIZED VIEW MightCloseLinkApp1 AS
                    SELECT t.frm AS frm, t.toN AS toN, ARRAY_TO_STRING(ARRAY_AGG(t.visited), '') AS visited, 
                    SUM(oldContributor) AS toSubtract, SUM(newContributor) AS toSum
                    FROM MightCloseLinkContributorDelta AS t
                    GROUP BY (t.frm, t.toN);
                    """
            run_query_and_commit(connection, query)

            query = """
                    CREATE MATERIALIZED VIEW MightCloseLinkApp AS
                    SELECT t.frm AS frm, t.toN AS toN, t.visited AS visited, (toSum - toSubtract) AS ownership
                    FROM MightCloseLinkApp1 AS t;
                    """
            run_query_and_commit(connection, query)

            query = """
                   CREATE MATERIALIZED VIEW MightCloseLinkUpdated AS
                   SELECT t1.frm AS frm, t1.toN AS toN, (t1.visited || t2.visited) AS visited,
                    (t1.ownership + t2.ownership) AS ownership
                   FROM MightCloseLinkApp AS t1, MightCloseLink AS t2
                   WHERE t1.frm = t2.frm AND t1.toN = t2.toN;
                   """
            run_query_and_commit(connection, query)

            query = """
                   CREATE MATERIALIZED VIEW MightCloseLinkCreated AS
                   SELECT t1.frm AS frm, t1.toN AS toN, t1.visited AS visited, t1.ownership AS ownership
                   FROM MightCloseLinkApp AS t1 LEFT JOIN MightCloseLink AS t2
                   ON t1.frm = t2.frm AND t1.toN = t2.toN
                   WHERE t2.frm IS NULL AND t2.toN IS NULL;
                   """
            run_query_and_commit(connection, query)

            query = """DROP TABLE IF EXISTS MightCloseLinkDelta;"""
            run_query_and_commit(connection, query)

            query = """
                    CREATE TABLE MightCloseLinkDelta AS
                    SELECT t1.frm AS frm, t1.toN AS toN, t1.visited AS visited, t1.ownership AS ownership
                    FROM MightCloseLinkCreated AS t1
                    UNION 
                    SELECT t2.frm AS frm, t2.toN AS toN, t2.visited AS visited, t2.ownership AS ownership
                    FROM MightCloseLinkUpdated AS t2;
                    """
            run_query_and_commit(connection, query)

            query = """
                    UPDATE MightCloseLink
                    SET visited = t.visited, ownership = t.ownership
                    FROM MightCloseLinkUpdated AS t
                    WHERE MightCloseLink.frm = t.frm AND MightCloseLink.toN = t.toN;
                    """
            run_query_and_commit(connection, query)

            query = """
                    INSERT INTO MightCloseLink (frm, toN, visited, ownership)
                    SELECT frm, toN, visited, ownership
                    FROM MightCloseLinkCreated;
                    """
            run_query_and_commit(connection, query)

            query = """
               DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkCreated CASCADE; 
               DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkUpdated CASCADE; 
               DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkContributorApp1 CASCADE;
               DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkContributorDelta CASCADE;
               DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkContributorUpdated CASCADE;
               DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkContributorCreated CASCADE;
               DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkApp CASCADE;
               DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkApp1 CASCADE;
               DROP MATERIALIZED VIEW IF EXISTS MightCloseLinkContributorApp CASCADE;
               """
            run_query_and_commit(connection, query)

            update_planner_stats(connection, "MightCloseLink")
            update_planner_stats(connection, "MightCloseLinkDelta")
            update_planner_stats(connection, "MightCloseLinkContributor")
            update_planner_stats(connection, "MightCloseLinkContributorDelta")

            n_delta_mcl = run_terminating_query(connection, terminating_query)
            n_iteration = n_iteration + 1
            print("New close link relationship found: " + str(n_delta_mcl))

    except psycopg2.Error as e:
        print("Error running recursion", e)


def run_final_rule(connection):
    query = """
            INSERT INTO CloseLink (frm, toN)
            SELECT t.frm, t.toN
            FROM MightCloseLink AS t
            WHERE ownership > 0.2;
            """
    run_query_and_commit(connection, query)


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

        print("Running exit rule.")
        run_exit_rule(connection)

        print("Running recursive rules...")
        run_recursion(connection)
        print("Recursion finished.")

        print("Running final rule to extract close links.")
        run_final_rule(connection)

        n_cl = count_cl_rows(connection)

        # Record the end time
        end_time = time.time()

        print("Dropping tables and views if present in the database.")
        drop_all_tables_and_views(connection)

        print("Number of close link relationship found: " + str(n_cl))
        # Calculate the elapsed time in seconds
        elapsed_time = end_time - start_time

        # Print the elapsed time
        print(f"Execution time: {elapsed_time:.4f} seconds")

    except psycopg2.Error as e:
        print("Error connecting to the db:", e)
    finally:
        connection.close()
