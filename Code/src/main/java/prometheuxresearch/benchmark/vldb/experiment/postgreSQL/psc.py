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
        # Create the "KeyPerson" table
        cursor.execute("""
            CREATE TABLE KeyPerson (
                company text,
                person text
            );
        """)

        # Create index on "KeyPerson" table
        cursor.execute("""
                CREATE INDEX KeyPersonIndex ON KeyPerson (person);
                """)

        cursor.execute("""
                CREATE TABLE Person (
                    person text
                );
              """)

        cursor.execute("""
               CREATE INDEX PersonIndex ON Person (person);
               """)

        cursor.execute("""
                CREATE TABLE Company (
                    company text
                );
              """)

        cursor.execute("""
                CREATE TABLE Control (
                    company1 text,
                    company2 text
                );
              """)

        cursor.execute("""
               CREATE INDEX ControlIndex ON Control (company1);
               """)

        cursor.execute("""
               CREATE TABLE Psc (
                   company1 text,
                   company2 text,
                   person text,
                   reasoningKey text
               );
             """)

        cursor.execute("""
               CREATE INDEX PscIndex ON Psc (reasoningKey);
               """)

        connection.commit()
        cursor.close()
        print("Tables and Indexes created successfully.")

    except psycopg2.Error as e:
        print("Error initiating tables and views:", e)


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
                CREATE OR REPLACE FUNCTION reasoning_key(company1 text, company2 text, person text)
                RETURNS text AS $$
                BEGIN
                    RETURN CASE
                        WHEN person LIKE 'z_' THEN calculate_md5_hash(company1 || company2 || 'z_1')
                        ELSE calculate_md5_hash(company1 || company2 || person)
                    END;
                END;
                $$ LANGUAGE plpgsql;
                """
        run_query_and_commit(connection, query)

    except psycopg2.Error as e:
        print("Error while defining labelled null generation function.", e)


def run_exit_rules(connection):
    try:
        query = """
                CREATE MATERIALIZED VIEW DeltaPsc1 AS 
                SELECT t1.company AS company1, t1.company AS company2, t1.person AS person 
                FROM KeyPerson AS t1, Person AS t2
                WHERE t1.person = t2.person;
                """
        run_query_and_commit(connection, query)

        query = """
                CREATE MATERIALIZED VIEW DeltaPsc2 AS 
                SELECT company AS company1, company AS company2, generate_null(CAST(company AS text), 2) AS person
                FROM Company;
                """
        run_query_and_commit(connection, query)

        query = """
                 CREATE MATERIALIZED VIEW DeltaPscApp AS 
                 SELECT *
                 FROM DeltaPsc1
                 UNION
                 SELECT *
                 FROM DeltaPsc2;
                 """
        run_query_and_commit(connection, query)

        query = """
                 CREATE TABLE DeltaPsc AS 
                 SELECT company1, company2, person, 
                 reasoning_key(CAST(company1 AS text), CAST(company2 AS text), person) AS reasoningKey
                 FROM DeltaPscApp;
                 """
        run_query_and_commit(connection, query)

        query = """
                DROP MATERIALIZED VIEW IF EXISTS DeltaPscApp CASCADE;
                DROP MATERIALIZED VIEW IF EXISTS DeltaPsc2 CASCADE;
                DROP MATERIALIZED VIEW IF EXISTS DeltaPsc1 CASCADE;
                """
        run_query_and_commit(connection, query)

    except psycopg2.Error as e:
        print("Error running exit TGDs.", e)


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


def count_psc(connection):
    cursor = None
    try:
        query = """
              SELECT COUNT(*)
              FROM Psc;
              """
        cursor = connection.cursor()
        cursor.execute(query)
        row = cursor.fetchone()
        connection.commit()
        return int(row[0])

    except psycopg2.Error as e:
        print("Error counting psc rows:", e)
    finally:
        if cursor is not None:
            cursor.close()


def run_recursion(connection):
    try:
        terminating_query = """
                              SELECT COUNT(*)
                              FROM DeltaPsc;
                              """

        n_delta_psc = run_terminating_query(connection, terminating_query)
        n_iteration = 1
        while n_delta_psc != 0:
            print("Iteration number: " + str(n_iteration))

            print("Running recursive rule...")

            query = """
                    DROP TABLE IF EXISTS DeltaPscX CASCADE;
                    """
            run_query_and_commit(connection, query)

            query = """
                     CREATE TABLE DeltaPscX AS 
                     SELECT company1, company2, person, reasoningKey
                     FROM DeltaPsc;
                     """
            run_query_and_commit(connection, query)

            query = """
                    CREATE MATERIALIZED VIEW DeltaPscApp AS 
                    SELECT DISTINCT t1.company2 AS company1, t2.company2 AS company2, t2.person AS person, 
                    reasoning_key(t1.company2, t2.company2, t2.person) AS reasoningKey
                    FROM Control AS t1, DeltaPscX AS t2
                    WHERE t1.company1 = t2.company1;
                    """
            run_query_and_commit(connection, query)

            query = """
                    DROP TABLE IF EXISTS DeltaPsc CASCADE;
                    """
            run_query_and_commit(connection, query)

            query = """
                    CREATE TABLE DeltaPsc AS
                    SELECT t1.company1 AS company1, t1.company2 AS company2, 
                    t1.person AS person, t1.reasoningKey AS reasoningKey
                    FROM DeltaPscApp AS t1 LEFT JOIN Psc AS t2 on t1.reasoningKey = t2.reasoningKey
                    WHERE t2.reasoningKey IS NULL;

                    """
            run_query_and_commit(connection, query)

            query = """
                    INSERT INTO Psc (company1, company2, person, reasoningKey)
                    SELECT company1, company2, person, reasoningKey
                    FROM DeltaPsc;
                    """
            run_query_and_commit(connection, query)

            query = """
                    DROP MATERIALIZED VIEW IF EXISTS DeltaPscApp CASCADE;
                    """
            run_query_and_commit(connection, query)

            update_planner_stats(connection, "Psc")

            n_delta_psc = run_terminating_query(connection, terminating_query)
            n_iteration = n_iteration + 1
            print("New psc relationships found: " + str(n_delta_psc))

    except psycopg2.Error as e:
        print("Error running recursion", e)


def drop_all_tables_and_views(connection):
    try:
        cursor = connection.cursor()
        cursor.execute("""DROP TABLE IF EXISTS Control CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS Person CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS KeyPerson CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS Company CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS Psc CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS DeltaPsc CASCADE;""")
        cursor.execute("""DROP TABLE IF EXISTS DeltaPscX CASCADE;""")

        cursor.execute("""DROP MATERIALIZED VIEW IF EXISTS DeltaPscApp CASCADE;""")

        connection.commit()
        cursor.close()
        print("Table and Views deleted successfully.")

    except psycopg2.Error as e:
        print("Error initiating tables and views:", e)


# read args frm command line

parser = argparse.ArgumentParser(description='Connect to PostgreSQL and execute a query.')
parser.add_argument('--input_person', required=True, help='Complete CSV file path to read from')
parser.add_argument('--input_keyperson', required=True, help='Complete CSV file path to read frm')
parser.add_argument('--input_company', required=True, help='Complete CSV file path to read frm')
parser.add_argument('--input_control', required=True, help='Complete CSV file path to read frm')
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
        print("Initializing custom functions for labelled nulls.")
        labelled_null_generation_function(connection)
        reasoning_key_function(connection)

        print("Reading input files and importing them.")
        import_csv(connection, args.input_person, "Person")
        import_csv(connection, args.input_keyperson, "KeyPerson")
        import_csv(connection, args.input_company, "Company")
        import_csv(connection, args.input_control, "Control")

        # Record the start time
        start_time = time.time()

        print("Running exit rules.")
        run_exit_rules(connection)

        print("Running recursive rule...")
        run_recursion(connection)
        print("Recursion finished.")

        n_psc = count_psc(connection)

        # Record the end time
        end_time = time.time()

        print("Dropping tables and views if present in the database.")
        drop_all_tables_and_views(connection)

        print("Number of psc found: " + str(n_psc))
        # Calculate the elapsed time in seconds
        elapsed_time = end_time - start_time

        # Print the elapsed time
        print(f"Execution time: {elapsed_time:.4f} seconds")

    except psycopg2.Error as e:
        print("Error connecting to the db:", e)
    finally:
        connection.close()
