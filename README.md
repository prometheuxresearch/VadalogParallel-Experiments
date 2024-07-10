# Vadalog Parallel Experiments

This repository contains Vadalog Parallel codebase and compared systems implementations, material and instructions to reproduce the experiments presented in the VLDB paper on the Vadalog Parallel System: Distributed Reasoning with Datalog+/-.

# Vadalog Parallel App

## Overview
Vadalog Parallel App is a Spring Boot application designed to handle parallel processing tasks for Vadalog programs.

## Requirements
- Java 17

## Build and Start

**Navigate to Vadalog Parallel project directory**
   ```sh
   cd vadalog-parallel
   ```
To build and start the application, use the provided scripts:

1. **Build the application:**
   ```sh
   ./build.sh
   ```
2. **Start the application:**
   ```sh
   ./start.sh
   ```

## Running Programs
Scripts to run each program are located in the `run` directory. Below is an example of how to execute the transitive closure program:

1. Navigate to the run directory:
   ```sh
   cd run
   ```

2. Execute the transitive closure program:
   ```sh
   ./run_transitive_closure.sh
   ```

## Datasets and Programs
- Datasets are located in the `disk/input` directory.
- Programs are located in their respective directories within `repository/programName/programName.vada`.

## Modifying Input and Output Datasets
To modify the input dataset or the directory to write the output for a program, follow these steps:

1. Open the program file in the respective directory. For example, to modify the input for the transitive closure program, open repository/transitiveClosure/transitiveClosure.vada
2. Modify the bind annotation for the input predicate arc to read a different file. This will change the dataset that the program processes.
3. Modify the bind annotation for the output predicate tc to write the output into a different file.

## Example

To modify the transitive closure input:

1. Open the file:
   ```sh
    vi repository/transitiveClosure/transitiveClosure.vada
   ```
2. Locate the bind annotation for the input predicate arc and update the file path and the file name to the desired dataset.
    ```sh
    @bind("arc","csv","disk/input/new_file_path","new_file_name.csv").
    ```
    
## Run on a Yarn Cluster
By default, Vadalog Parallel run as a standalone application. To enable parallel and distributed processing on a cluster, you can modify the properties in the `spark-defaults.conf` file located in `src/main/resources`.

Set the property spark.master to yarn.
Configure other available driver, executor, Yarn, and HDFS properties.
For detailed configuration options, please refer to the [Apache Spark documentation on running on Yarn](https://spark.apache.org/docs/latest/running-on-yarn.html). 


## Compared System Requirements

To replicate the experiments, ensure your system meets the following requirements:
- Ubuntu machine v20
- Java 11
- Python 3
- Spark 3.4.0
- Flink 1.17.1
- Maven
- PostgreSQL 16 (with parallelism enabled)

## Compilation and Running Experiments

#### 1. Compilation
```bash
cd VadalogParallel-Experiments/Code
mvn -f pom.xml clean package -DskipTests
mv target/Code-1.0.0-jar-with-dependencies.jar target/Code.jar
```

#### 2. Display available commands
Navigate to the project base directory:
```bash
cd ../
```
Display available launcher scripts for running experiments with different systems:
```bash
cat Common-scripts/command.txt
```
Execute the first launcher script to get available commands for experiments:

```bash
python3 Common-scripts/launcher.py --platforms_file Common-scripts/platforms.conf --jobs_file Common-scripts/jobs.conf --log_file Common-scripts/log.txt --to_run False
```
This command will display available commands for running experiments with different systems.

#### 3. Execution

For example, to run the "Close Links" program with the system JAVA STREAM PARALLEL on the company graph with a size of 50k, use the following command:
```bash
java -Xmx500g -cp Code/target/Code.jar prometheuxresearch.benchmark.vldb.experiment.javastream.closeLink.CloseLinkJavaStream --parallelism 96 --input Code/src/test/resources/programs_data/companygraph/company_graph_50k.csv
```


