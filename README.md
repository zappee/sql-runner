# Remal SQL-Runner command line tool

_keywords: java, sql, query, insert, select, update, oracle, database, bash script, shell script, command line,  tool, execute, automate, docker_

_[Release Note](release.md)_

## 1) Overview
The SQL-Runner is a flexible command-line Java tool that can be used to run any SQL commands using bash script and command line. It can be used very effectively from Windows/Linux script files.

The latest version of the tool only supports Oracle Database server, but it can support different SQL dialects as well.

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
Usage: SqlRunner [-?sv] [-c=<dialect>] -U=<user> (-P=<password> | -I) (-j=<jdbcUrl> | (-h=<host>
                 -p=<port> -d=<database>)) <sql>
SQL command line tool. It executes the given SQL and shows the result on the standard output.

General options:
      <sql>                 SQL to be executed. Example: 'select 1 from dual'
  -?, --help                Display this help and exit.
  -v, --verbose             It provides additional details as to what the tool is doing.
  -q, --quiet               In this mode nothing will be printed to the output.
  -c, --dialect=<dialect>   SQL dialect used during the execution of the SQL statement. Supported
                              SQL dialects: ORACLE.
                              Default: ORACLE
  -s, --showHeader          Shows the name of the fields from the SQL result set.
  -U, --user=<user>         Name for the login.

Specify a password for the connecting user:
  -P, --password=<password> Password for the connecting user.
  -I, --iPassword           Interactive way to get the password for the connecting user.

Custom configuration:
  -h, --host=<host>         Name of the database server.
  -p, --port=<port>         Number of the port where the server listens for requests.
  -d, --database=<database> Name of the particular database on the server. Also known as the SID in
                              Oracle terminology.

Provide a JDBC URL:
  -j, --jdbcUrl=<jdbcUrl>   JDBC URL, example: jdbc:oracle:<drivertype>:@//<host>:<port>/<database>.

Exit codes:
  0   Successful program execution.
  1   An unexpected error appeared while executing the SQL statement.
  2   Usage error. The user input for the command was incorrect.

Please report issues at arnold.somogyi@gmail.com.
Documentation, source code: https://github.com/zappee/sql-runner.git
~~~~

## 4) Licence
BSD (2-clause) licensed.


