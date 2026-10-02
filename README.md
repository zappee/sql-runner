## 🐧 SQL-Runner

![GitHub top language](https://img.shields.io/github/languages/top/zappee/sql-runner)
![GitHub Issues](https://img.shields.io/github/issues/zappee/sql-runner)
![GitHub Release](https://img.shields.io/github/v/release/zappee/sql-runner)

#### ⭐⭐ Like this project? Support my work by giving it a star on [GitHub](https://github.com/zappee/sql-runner/) ⭐⭐

![GitHub Repo stars](https://img.shields.io/github/stars/zappee/sql-runner?style=flat)

### 1) Overview
**SQL-Runner** is a lightweight, cross-platform command-line (CLI) utility written in Java.
It is designed to execute SQL queries directly from the terminal and stream the results to the standard output.
Because it is built for fast database interactions via the command line, it serves as an excellent utility for Linux shell scripts and containerized deployment workflows (such as Docker initialization blocks).


### 2) Key features
* **Cross-platform compatibility:** Runs anywhere Java is installed.
* **Script integration:** Designed explicitly to pass parameters and run queries inside Bash scripts.
* **Flexible input:** Supports raw SQL text strings directly in the terminal or path to a complex `.sql` file.
* **Automation ready:** Stops execution and throws distinct exit codes on failures to cleanly halt deployment pipelines if an error occurs.
* **Database driver support:** Connects via standard JDBC drivers, currently optimized for Oracle Database servers.

### 3) Use cases
* **Docker container orchestration:** Delay container startups inside a Docker environment until the database container is fully initialized and listening for incoming connections. It can be used to block the startup of application server containers (e.g., Spring Boot, Oracle WebLogic Managed Server) and wait for the database container to be ready, preventing uninitialized connection pools and datasource failures in the application containers.
* **Automated application deployment:** Run automated initialization routines, create or patch database schemas, and insert mandatory default data directly during CI/CD deployment pipelines.
* **Automated administrative tasks:** Schedule and run background database maintenance scripts, data cleanups, or daily reporting queries using the native Linux cron scheduler.
* **Shell script data ingestion:** Safely pass variables from shell scripts or CI/CD environments directly into SQL commands to insert, update, or manipulate database records dynamically during automated deployment workflows.

### 4) Quick start
Before executing the tool, ensure your environment meets the following requirements:
* The Java Runtime Environment (JRE) must be installed and globally configured in your system path.
* Stable network access to your target database instance.

The application is executed as a standalone JAR file using specific command-line arguments.

**Using a JDBC connection string:**
```console
$ java -jar sql-runner.jar \
    -U=<user> \
    -P=<password> \
    j=<jdbcUrl> \
    -s=<sqlStatements>
```

**Using explicit host, port, and database parameters:**
```console
$ java -jar sql-runner.jar \
    -U=<user> \
    -P=<password> \
    -h=<host> \
    -p=<port> \
    -d=<database> \
    -s=<sqlStatements>
```

### 5) Usage examples

#### 5.1) Database server status checker
In a Docker environment, sometimes the application server startup must be blocked until the database server is fully ready to receive incoming connections.
The following Bash script can be used as a _wait-for-database_ loop:

```bash
# wait-for-database.sh

until java -jar sql-runner.jar \
    -j jdbc:oracle:thin:@oracle-db:1521/ORCLPDB1.localdomain \
    -U "SYS as SYSDBA" \
    -P Oradoc_db1 \
    -s "SELECT 1 FROM DUAL"
do
  echo "The database server is not running yet. Waiting..."
  sleep 0.5
done

echo "Database server is up and running!"
```

#### 5.2) Execute a local SQL script file
To run a complex, pre-written script file against your target database, pass the file path using the `-f` flag:

```console
$ java -jar sql-runner.jar \
    -j jdbc:oracle:thin:@$DB_HOST:$DB_PORT/$DB_NAME \
    -U username \
    -P password \
    -f "create-schema.sql"
```

#### 5.3) Insert multiple records
You can execute sequential SQL statements and transactions in a single command string by splitting them with your statement separator:

```console
$ java -jar sql-runner.jar \
    -j jdbc:oracle:thin:@$DB_HOST:$DB_PORT/$DB_NAME \
    -U username \
    -P password \
    -s "INSERT INTO customer (id, name) VALUES (1, 'Arnold'); \
        INSERT INTO customer (id, name) VALUES (2, 'Jose'); \
        INSERT INTO customer (id, name) VALUES (3, 'Andrei'); \
        COMMIT;"
```

### 6) Summary of exit codes
Automated pipelines can track status using standard exit codes:
* **`0`:** The SQL statement or script executed completely without errors.
* **`1`:** The execution failed due to a database connection timeout, invalid credentials, or incorrect SQL syntax.
* **`2`:** The execution failed because required arguments were missing, misspelled, or formatted incorrectly.
* **`3`:** An unexpected runtime failure occurred within the application.

### 7) CLI Reference & Command syntax

SQL-Runner supports various configuration, authentication, and execution flags:

```console
Usage: SqlRunner [-?qS] [-c=<dialect>] [-e=<commandSeparator>] -U=<user> (-P=<password> | -I)
                 (-j=<jdbcUrl> | ([-h=<host>] [-p=<port>] -d=<database>)) (-s=<sqlStatements> |
                 -f=<sqlScriptFile>)
SQL command line tool. It executes the given SQL and shows the result on the standard output.

  -?, --help         Display this help and exit.
  -q, --quiet        In this mode nothing will be printed to the output.
  -c, --dialect      SQL dialect used during the execution of the SQL statement. Supported SQL
                       dialects: ORACLE.
                       Default: ORACLE
  -e, --cmdsep       SQL separator is a non-alphanumeric character used to separate multiple SQL
                       statements. Multiply statements is only recommended for SQL INSERT and
                       UPDATE. The result of the queries will not be displayed.
                       Default: ;
  -S, --showHeader   Shows the name of the fields from the SQL result set.
  -U, --user         Name for the login.

Specify a password for the connecting user:
  -P, --password     Password for the connecting user.
  -I, --iPassword    Interactive way to get the password for the connecting user.

Custom configuration:
  -h, --host         Name of the database server.
                       Default: localhost
  -p, --port         Number of the port where the server listens for requests.
                       Default: 1521
  -d, --database     Name of the particular database on the server. Also known as the SID in Oracle
                       terminology.

Provide a JDBC URL:
  -j, --jdbcUrl      JDBC URL, example: jdbc:oracle:<drivertype>:@//<host>:<port>/<database>.

SQL statement(s) to be executed:
  -s, --sql          SQL statements to be executed. Multiply statements can be provided. Example:
                       'insert into...; insert into ...; commit'
  -f, --file         Path to the SQL script file.

Exit codes:
  0   Successful program execution.
  1   An unexpected error appeared while executing the SQL statement.
  2   Usage error. The user input for the command was incorrect.
  3   Internal program error.

Please report issues at arnold.somogyi@gmail.com.
Documentation, source code: https://github.com/zappee/sql-runner.git
```

### 8) Source code

[https://github.com/zappee/sql-runner](https://github.com/zappee/sql-runner)

### 🤝 Contributing

Contributions, feature requests, optimization, and bug reports are always welcome!
For more information, please visit my [homepage](https://zappee.github.io).
