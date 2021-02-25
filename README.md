# Remal SQL-Runner command line tool

_keywords: java, sql, query, insert, select, update, oracle, database, bash script, shell script, command line,  tool, execute, automate, docker_

_[Release Note](release.md)_

## 1) Overview

The `SQL-Runner` is a small command-line tool written in Java and can be used on all platforms, for which JRE 8 is available.
The tool can be used to execute any SQL commands, especially it is suitable for executing SQL `select`, `update` and `delete`.
You can use it to `insert` data into databases as well, but I think this is not the most effective way to do this.
The tool uses a JDBC driver to connect to a specific database and relatively easy to configure.
The latest version of the tool only supports Oracle Database server, but other SQL dialects can easily be added.

The result of the executed SQL command is displayed on the standard output.
When there is an error the tool stops with exit code 1 or 2 and the error message is displayed.

SQL-Runner is created with a main purpose to be used in Linux shell-scripts or in Windows command line. 

## 2) Use cases
#### 2.1) Database status check
A possible use case in Docker environment is to check whether the database server is ready to receive incoming connections or not:
~~~
#!/bin/bash

until java -jar sql-runner-0.2.0-with-dependencies.jar -j jdbc:oracle:thin:@//oracle-db:1521/ORCLPDB1.localdomain -U "SYS as SYSDBA" -P Oradoc_db1 "select 1 from dual"
do
    echo "The database server in not up and running. Waiting..."
    sleep 0.5
done
echo "Database server is up and running"
~~~

## 3) Usage
~~~~
Usage: SqlRunner [-?qsv] [-c=<dialect>] [-e=<sqlCommandSeparator>] -U=<user> (-P=<password> | -I)
                 (-j=<jdbcUrl> | (-h=<host> -p=<port> -d=<database>)) <sqlStatements>
SQL command line tool. It executes the given SQL and shows the result on the standard output.

General options:
      <sqlStatements>   SQL statements to be executed. Multiply statements can be provided.
                          Example: 'insert into...; insert into ...; commit'
  -?, --help            Display this help and exit.
  -v, --verbose         It provides additional details as to what the tool is doing.
  -q, --quiet           In this mode nothing will be printed to the output.
  -c, --dialect         SQL dialect used during the execution of the SQL statement. Supported SQL
                          dialects: ORACLE.
                         Default: ORACLE
  -e, --cmdsep          SQL separator is a non-alphanumeric character used to separate multiple SQL
                          statements. Multiply statements is only recommended for SQL INSERT and
                          UPDATE because in this case the result of the queries will not be
                          displayed.
                         Default: ;
  -s, --showHeader      Shows the name of the fields from the SQL result set.
  -U, --user            Name for the login.

Specify a password for the connecting user:
  -P, --password        Password for the connecting user.
  -I, --iPassword       Interactive way to get the password for the connecting user.

Custom configuration:
  -h, --host            Name of the database server.
  -p, --port            Number of the port where the server listens for requests.
  -d, --database        Name of the particular database on the server. Also known as the SID in
                          Oracle terminology.

Provide a JDBC URL:
  -j, --jdbcUrl         JDBC URL, example: jdbc:oracle:<drivertype>:@//<host>:<port>/<database>.

Exit codes:
  0   Successful program execution.
  1   An unexpected error appeared while executing the SQL statement.
  2   Usage error. The user input for the command was incorrect.

Please report issues at arnold.somogyi@gmail.com.
Documentation, source code: https://github.com/zappee/sql-runner.git
~~~~

## 4) Licence
BSD (2-clause) licensed.