java -jar target/sql-runner-1.0-with-dependencies.jar -j jdbc:oracle:thin:@//localhost:1521/ORCLPDB1.localdomain -U "SYS as SYSDBA" -P "Oradoc_db1" "select * from SLM.APPLICATION"

java -jar target/sql-runner-1.0-with-dependencies.jar -h localhost -p 1521 -d ORCLPDB1.localdomain -U "SYS as SYSDBA" -P "Oradoc_db1" "select * from SLM.APPLICATION"

