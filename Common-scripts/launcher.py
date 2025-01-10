import subprocess
import configparser
import os
import sys
import logging
import argparse

# whether to actually run the command or not
RUN_COMMANDS = False


class PrintAndLog:
    def __init__(self, log_file):
        self.log_file = log_file
        self.stdout = sys.stdout
        self.log = logging.getLogger(__name__)
        self.log.setLevel(logging.INFO)
        self.log_handler = logging.FileHandler(log_file)
        self.log_formatter = logging.Formatter('%(asctime)s - %(message)s')
        self.log_handler.setFormatter(self.log_formatter)
        self.log.addHandler(self.log_handler)

    def write(self, text):
        self.stdout.write(text)
        self.log.info(text)

    def flush(self):
        self.stdout.flush()
        self.log_handler.flush()


class PlatformRunner:
    def __init__(self, name, conf_file):
        self.name = name
        if file_exists(conf_file):
            platform_to_props, _ = get_configurations_by_section(conf_file)
            if name in platform_to_props.keys():
                self.platform_properties = platform_to_props[name]
            else:
                print(f"The configurations for platform '{name}' does not exist.")
        else:
            print(f"The configuration file '{conf_file}' does not exist.")

    def compile_and_run(self, source_file: str, project_name: str, project_path: str, job_properties: list,
                        timeout=1000):
        pass

    def run(self, source_file: str, executable_path: str, job_properties: list, timeout=1000):
        pass


class PostgreSQLRunner(PlatformRunner):

    def compile_and_run(self, source_file: str, project_name: str, project_path: str, job_properties: list,
                        timeout=1000):
        command = "python " + source_file + " "
        self.platform_properties = list(map(lambda x: (x[0].split(".")[-1], x[1]), self.platform_properties))
        for (key, value) in self.platform_properties:
            command = command + "--" + key + " " + value + " "
        for (key, value) in job_properties:
            command = command + "--" + key + " " + value + " "

        is_python_installed = run_command_with_timeout("python --version")
        if "python" in str(is_python_installed).lower() or not RUN_COMMANDS:
            run_command_with_timeout(command, timeout)
        else:
            print("python is not installed in your working environment. It is not possible to run the job")

    def run(self, source_file: str, executable_path: str, job_properties: list, timeout=1000):
        self.compile_and_run(source_file, "", "", job_properties, timeout)


class JavaStreamRunner(PlatformRunner):

    def compile_and_run(self, source_file: str, project_name: str, project_path: str, job_properties: list,
                        timeout=1000):
        self.run(source_file, project_path, job_properties, timeout)

    def run(self, source_file: str, executable_path: str, job_properties: list, timeout=1000):
        self.platform_properties = list(map(lambda x: (x[0].split(".")[-1], x[1]), self.platform_properties))
        xmx_value = list(filter(lambda x: x[0] == "Xmx", self.platform_properties))
        if len(xmx_value) != 1:
            # set as default
            xmx_value = "8g"
        else:
            xmx_value = xmx_value[0][1]

        source_file = source_file.replace("/", ".")

        command = "java -Xmx" + xmx_value
        command = command + " -cp " + executable_path + " " + source_file + " "
        for (key, value) in self.platform_properties:
            if key != "Xmx" and key != "xmx":
                command = command + "--" + key + " " + value + " "
        for (key, value) in job_properties:
            command = command + "--" + key + " " + value + " "
        run_command_with_timeout(command, timeout)


class FlinkRunner(PlatformRunner):

    def compile_and_run(self, source_file: str, project_name: str, project_path: str, job_properties: list,
                        timeout=1000):
        compile_with_maven(project_path, project_name)
        plat_props = dict()
        for key, value in self.platform_properties:
            plat_props[key] = value

        source_file = source_file.replace("/", ".")

        command = "" + plat_props["flink.bin.dir"] + "/flink run -c " + source_file + " " \
                  + project_path + "/target/" + project_name + ".jar "+" --executor.start-pipeline "

        for key, value in self.platform_properties:
            if key != "flink.bin.dir":
                command = command + "--" + key + " " + value + " "

        for (key, value) in job_properties:
            command = command + "--" + key + " " + value + " "

        run_command_with_timeout(command, timeout)

    def run(self, source_file: str, executable_path: str, job_properties: list, timeout=1000):
        plat_props = dict()
        for key, value in self.platform_properties:
            plat_props[key] = value

        source_file = source_file.replace("/", ".")

        command = "" + plat_props["flink.bin.dir"] + "/flink run -c " + source_file + " " \
                  + executable_path + " --executor.start-pipeline "

        for key, value in self.platform_properties:
            if key != "flink.bin.dir":
                command = command + "--" + key + " " + value + " "

        for (key, value) in job_properties:
            command = command + "--" + key + " " + value + " "

        run_command_with_timeout(command, timeout)


class SparkSQLRunner(PlatformRunner):

    def compile_and_run(self, source_file: str, project_name: str, project_path: str, job_properties: list,
                        timeout=1000):
        compile_with_maven(project_path, project_name)
        plat_props = dict()
        for key, value in self.platform_properties:
            plat_props[key] = value
            
        source_file = source_file.replace("/", ".")

        command = "" + plat_props["spark.bin.dir"] + "/spark-submit " + "--class " + source_file

        sparkHome=""

        for key, value in self.platform_properties:
            if key != "spark.bin.dir" and key != "spark.homeDir":
                command = command + " --conf " + key + "=" + value + " "
            if key =="spark.homeDir":
                sparkHome+="--spark.homeDir "+value

        command += " " \
                  + executable_path + " "

        for (key, value) in job_properties:
            command = command + "--" + key + " " + value + " "
        if(sparkHome != ""):
            command = command + " "+sparkHome

        run_command_with_timeout(command, timeout)

    def run(self, source_file: str, executable_path: str, job_properties: list, timeout=1000):
        plat_props = dict()
        for key, value in self.platform_properties:
            plat_props[key] = value

        source_file = source_file.replace("/", ".")

        command = "" + plat_props["spark.bin.dir"] + "/spark-submit " + "--class " + source_file

        sparkHome=""

        for key, value in self.platform_properties:
            if key != "spark.bin.dir" and key != "spark.homeDir":
                command = command + " --conf " + key + "=" + value + " "
            if key =="spark.homeDir":
                sparkHome+="--spark.homeDir "+value

        command += " " \
                  + executable_path + " "

        for (key, value) in job_properties:
            command = command + "--" + key + " " + value + " "
        if(sparkHome != ""):
            command = command + " "+sparkHome

        run_command_with_timeout(command, timeout)


def file_exists(file_path):
    output = False
    if os.path.isfile(file_path):
        return True
    if output:
        print(f"The file '{file_path}' is a regular file and exists.")
    else:
        print(f"The file '{file_path}' either does not exist or is not a regular file.")
    return output


def get_configurations_by_section(conf_file: str):
    # Load the .conf configuration
    config = configparser.ConfigParser()
    config.optionxform=str

    # Read the configuration file
    with open(conf_file, 'r') as file:
        lines = [line.strip() for line in file]
        config.read_string('\n'.join(lines))

    # Get a list of all sections (categories)
    sections = config.sections()
    sections_to_properties = dict()

    # Iterate over the sections
    for section in sections:
        if section not in sections_to_properties.keys():
            sections_to_properties[section] = list()

        # Access and print the settings within each section
        for key, value in config.items(section):
            sections_to_properties[section].append((key, value))

    return sections_to_properties, config


def run_command_with_timeout(command, timeout_sec=1000):
    output = None
    try:
        print(f"[RUNNING COMMAND] {command}")
        if RUN_COMMANDS:
            # Run the command with a timeout
            completed_process = subprocess.run(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                                               timeout=timeout_sec, text=True)

            output = completed_process.stdout
            # Print the output
            print("Job output:", output)
            print()

    except subprocess.TimeoutExpired:
        print("Job timed out after {} seconds.".format(timeout_sec))
    except subprocess.CalledProcessError as e:
        print("Job failed with error:", e)
    except Exception as e:
        print("An error occurred:", e)
    return output


def compile_with_maven(input_project_path: str, project_name: str):
    jar_file = input_project_path + "/target/" + project_name + ".jar"
    current_dir_cmd = "pwd"
    current_dir = run_command_with_timeout(current_dir_cmd)

    if not file_exists(jar_file):
        change_dir_cmd = "cd " + input_project_path
        run_command_with_timeout(change_dir_cmd)
        compile_mvn = "mvn package"
        run_command_with_timeout(compile_mvn)
        run_command_with_timeout("cd " + current_dir)
    else:
        print("Executable jar already exists: " + jar_file)


def modify_global(value: bool):
    global RUN_COMMANDS
    if value == "True":
        RUN_COMMANDS = True


# Command example: sudo python3 launcher.py --platforms_file platforms.conf --jobs_file jobs.conf --log_file log.txt
# --to_run False

parser = argparse.ArgumentParser(
    description="A script to automatically run a sequence of jobs.")

parser.add_argument("--platforms_file", required=True, type=str, help="Path to the platforms configuration file")
parser.add_argument("--jobs_file", required=True, type=str, help="Path to the jobs configuration file")
parser.add_argument("--log_file", required=True, type=str, help="Path to the log file to write")
parser.add_argument("--to_run", required=True, type=str, help="Path to the log file to write")
args = parser.parse_args()

# Check file extensions
if not args.platforms_file.endswith(".conf") or not args.jobs_file.endswith(".conf"):
    print("Error: The first two files must have a .conf extension.")
    exit(1)

if not args.log_file.endswith(".txt"):
    print("Error: The log file must have a .txt extension.")
    exit(1)

log_file_path = args.log_file
platform_file_path = args.platforms_file
jobs_file_path = args.jobs_file
modify_global(args.to_run)

# Create an instance of the PrintAndLog class
print_and_log_instance = PrintAndLog(log_file_path)

# Redirect standard output to the PrintAndLog instance
sys.stdout = print_and_log_instance

print("Initializing a new job execution. \n")
print("The platforms configuration file is " + platform_file_path)
print("The job configuration file is " + jobs_file_path + "\n")

jobs_to_properties, _ = get_configurations_by_section(jobs_file_path)

for (job, properties) in jobs_to_properties.items():
    # access the properties of the section by their positions
    job_name, platform = tuple(job.split("-"))
    code_file = properties[0][1]
    project_path_or_exe = properties[1][1]
    # split the string into a list and remove empty properties
    others = [s for s in list(properties[2][1].split(",")) if s]
    # split each string into a key,value pair
    others = list(map(lambda x: [str(x).split(":")[0], str(x).split(":")[1]], others))
    # remove spaces
    others = list(map(lambda pair: tuple(map(lambda s: s.replace(" ", ""), pair)), others))

    print(f"[{job}]: Attempting to run job {job_name} in platform {platform}.")
    try:
        if platform == "PostgreSQL":
            pg_job = PostgreSQLRunner(platform, platform_file_path)
            pg_job.run(code_file, project_path_or_exe, others)
        elif platform == "SparkSQL":
            spark_job = SparkSQLRunner(platform, platform_file_path)
            spark_job.run(code_file, project_path_or_exe, others)
        elif platform == "Flink":
            flink_job = FlinkRunner(platform, platform_file_path)
            flink_job.run(code_file, project_path_or_exe, others)
        elif platform == "JavaStream":
            java_stream_job = JavaStreamRunner(platform, platform_file_path)
            java_stream_job.run(code_file, project_path_or_exe, others)
        else:
            print("Unrecognized or not supported platform.")
        print("Finished running job. \n")

    except Exception as e:
        print(f"An exception occured: {e}" + " when running command.")

