# Vadalog Parallel Experiments

This repository contains the material and instructions to reproduce the experiments presented in the VLDB paper on the **Vadalog Parallel System: Distributed Reasoning with Datalog+/-**. If you require a production license for the Vadalog Parallel system, please contact [davben@prometheux.co.uk](mailto:davben@prometheux.co.uk).

---

## Table of Contents
1. [Requirements](#requirements)
2. [Compilation and Running Experiments](#compilation-and-running-experiments)
   1. [Compilation](#1-compilation)
   2. [Display available commands](#2-display-available-commands)
   3. [Execution](#3-execution)

---

## Requirements
- **Java 11 or higher**

---

## Compilation and Running Experiments

### 1. Compilation

1. Navigate to the code directory:
    ```bash
    cd VadalogParallel-Experiments/Code
    ```
2. Compile the project:
    ```bash
    mvn -f pom.xml clean package -DskipTests
    ```
3. Rename the output JAR (optional step for convenience):
    ```bash
    mv target/Code-1.0.0-jar-with-dependencies.jar target/Code.jar
    ```

### 2. Display available commands

1. Return to the project base directory:
    ```bash
    cd ../
    ```
2. View the list of launcher scripts and experiment commands:
    ```bash
    cat Common-scripts/command.txt
    ```
3. Display available commands for experiments:
    ```python
    python3 Common-scripts/launcher.py \
    --platforms_file Common-scripts/platforms.conf \
    --jobs_file Common-scripts/jobs.conf \
    --log_file Common-scripts/log.txt \
    --to_run False
    ```

### 3. Execution
As an example, to run the **Close Links** program using the **JAVA STREAM PARALLEL** system on the company graph with 50k nodes, you can use the following command:

```java
java -Xmx5g \
    -cp Code/target/Code.jar \
    prometheuxresearch.benchmark.vldb.experiment.javastream.closeLink.CloseLinkJavaStream \
    --parallelism 96 \
    --input Code/src/test/resources/programs_data/companygraph/company_graph_50k.csv
```
Adjust the `-Xmx` and `--parallelism` parameters based on your available memory and desired parallelism settings.

For any inquiries regarding production licensing and for distributed evaluation on SAAS or Premise cluster, please contact [davben@prometheux.co.uk](mailto:davben@prometheux.co.uk).