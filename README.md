# Remal SQL-Runner command line tool

_keywords: java, sql, query, insert, select, update, oracle, database, bash script, shell script, command line,  tool, execute, automate, docker_

_[Release Note](release.md)_

## 1) Overview

The `SQL-Runner` is a small command-line tool written in Java and can be used on all platforms, for which JRE 8 is available.
The tool can be used to execute any SQL commands, especially it is suitable for executing SQL `select`, `update` and `delete`.
You can use the tool to create a new database schema during the application deployment and insert initial data into databases.
The tool uses a JDBC driver to connect to a specific database and relatively easy to configure.
The latest version of the tool only supports Oracle Database server, but other SQL dialects can easily be added.

The result of the executed SQL command is displayed on the standard output.
When there is an error the tool stops with exit code 1 or 2 and the error message is displayed.

SQL-Runner is created with a main purpose to be used in Linux shell-scripts or in Windows command line.

## 2) Examples
### 2.1) Check whether the database server is up
In Docker environment sometime the application server startup must be blocked untill the database server is ready to receive incoming connections.
~~~
#!/bin/bash

until java -jar sql-runner.jar \
                    -j jdbc:oracle:thin:@oracle-db:1521/ORCLPDB1.localdomain \
                    -U "SYS as SYSDBA" \
                    -P Oradoc_db1 \
                    "SELECT 1 FROM DUAL"
do
    echo "The database server in not up and running. Waiting..."
    sleep 0.5
done
echo "Database server is up and running"
~~~

### 2.2) Execute an SQL script file
The following example executes an Oracle Script file.
~~~
java -jar sql-runner.jar
               -j jdbc:oracle:thin:@$DB_HOST:$DB_PORT/$DB_NAME \
               -U username \
               -P password \
               -f "create-schema.sql"
~~~

### 2.3) Insert multiply records
~~~
java -jar sql-runner.jar
               -j jdbc:oracle:thin:@$DB_HOST:$DB_PORT/$DB_NAME \
               -U username \
               -P password \
               -s "INSERT INTO customer (id, name) VALUES (1, 'Arnold'); \
                   INSERT INTO customer (id, name) VALUES (2, 'Stefan'); \
                   INSERT INTO customer (id, name) VALUES (1, 'Andrei'); \
                   commit"
~~~

## 3) Usage
~~~~
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
~~~~

## 4) Licence
BSD (2-clause) licensed.
