# README

## Spark Data Processing Program

This Spark program is designed to process and transform data based on a configuration file. It utilizes Apache Spark to perform data transformations efficiently. The program reads input data from a CSV file, applies specified transformations, and writes the results to an output location.

### Table of Contents
1. [Prerequisites](#prerequisites)
2. [Usage](#usage)
3. [Program Overview](#program-overview)
4. [Configuration](#configuration)
5. [Output](#output)
6. [Building and Running](#building-and-running)
7. [Dependencies](#dependencies)

### Prerequisites
Before running this program, ensure you have the following prerequisites in place:
- Apache Spark is installed and properly configured.
- Hadoop Distributed File System (HDFS) is set up and accessible.
- Scala and sbt (Scala Build Tool) are installed.
- Input data in CSV format.
- Configuration file in JSON format.

### Usage
To use this program, follow these steps:

1. **Compile the Program:**
   - Use `sbt` or any preferred Scala build tool to compile the program.
   - Create a JAR file containing the compiled code and its dependencies.

2. **Prepare Input Data:**
   - Ensure that your input data is available in a CSV file. You will specify this file as an argument when running the program.

3. **Create a Configuration File:**
   - Create a JSON configuration file specifying the transformation rules for your data. The configuration file should contain an array of objects, each representing a transformation rule.
   - Each rule should have two properties: `cei_code` (an integer) and `primary_key` (a string).

4. **Run the Program:**
   - Execute the program by running the JAR file with the following arguments:
     - Argument 1: Path to the input CSV file.
     - Argument 2: Output directory where transformed data will be stored.
     - Argument 3: Path to the JSON configuration file.
     - Argument 4: Epoch timestamp (as a long) for filtering data.

5. **Review the Output:**
   - The program will process the data according to the rules specified in the configuration file and store the transformed data in the output directory.

### Program Overview
The Spark program performs the following main steps:

1. **Initialization:**
   - Initializes a Spark session with the name "Spark-final" and sets Spark configurations.

2. **Reading Configuration:**
   - Reads the JSON configuration file that contains transformation rules.

3. **Loading Input Data:**
   - Reads the input data from a CSV file using Spark.

4. **Data Transformation:**
   - Applies transformations to the input data based on the configuration rules.
   - Partitions the data by "updated_at" and "cei_code."
   - Writes the transformed data to the specified output directory.

5. **Shutdown:**
   - Stops the Spark session.

### Configuration
The JSON configuration file should contain an array of objects, where each object represents a transformation rule. Each rule object has the following properties:
- `cei_code`: An integer representing the CEI code.
- `primary_key`: A string representing the primary key used for partitioning and filtering the data.

Example configuration JSON:
```json
[
    {
        "cei_code": 1,
        "primary_key": "customer_id"
    },
    {
        "cei_code": 2,
        "primary_key": "product_id"
    }
]
```

### Output
The program outputs the transformed data as CSV files partitioned by "updated_at" and "cei_code" in the specified output directory. Each CSV file contains data for a specific CEI code and "updated_at" value.

### Building and Running
To build and run the program, follow these steps:

1. Build the program using Scala build tools (e.g., `sbt`):
   ```
   sbt compile
   sbt assembly
   ```

2. Run the program with the following arguments:
   ```
   spark-submit --class Main target/scala-2.12/spark-data-processing-program-assembly-1.0.jar <input-csv> <output-directory> <config-json> <epoch-timestamp>
   ```
   - `<input-csv>`: Path to the input CSV file.
   - `<output-directory>`: Output directory for storing transformed data.
   - `<config-json>`: Path to the JSON configuration file.
   - `<epoch-timestamp>`: Epoch timestamp for filtering data.

### Dependencies
The program relies on the following dependencies:
- Apache Spark for distributed data processing.
- JSON4S for parsing JSON configuration.
- Hadoop libraries for working with the Hadoop File System.
- Scala and sbt for building and running the program.

Make sure to include these dependencies when building the program.

---

Please feel free to reach out if you have any questions or need further assistance with this Spark data processing program.
