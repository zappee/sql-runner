package com.remal.sqlrunner;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.remal.sqlrunner.domain.Dialect;
import com.remal.sqlrunner.domain.ExitCode;

/**
 * This class connects to the database and executes the provided SQL statement.
 *
 * <p>
 * created on 09/07/2020
 * </p>
 *
 * @author arnold.somogyi@gmail.com
 */
public class SqlStatementExecutor {

    private PrintStream out = System.out;
    private boolean verbose = false;
    private boolean showHeader = false;
    private String user;
    private String password;

    /**
     * Initialization method.
     *
     * @param user name for the login
     * @param password password for the connecting user
     */
    public SqlStatementExecutor(String user, String password) {
        this.user = user;
        this.password = password;
    }

    /**
     * Executes the given SQL statement and returns with the result as a string.
     *
     * @param jdbcUrl the JDBC URL
     * @param sql sql statement to be executed
     * @return 0 if the SQL statement was executed properly, otherwise 1
     */
    public ExitCode execute(String jdbcUrl, String sql) {
        try (Connection connection = getConnection(jdbcUrl);
             Statement statement = connection.createStatement()) {

            if (verbose) {
                out.println("executing SQL statement: " + sql + "...");
            }

            ResultSet resultSet = statement.executeQuery(sql);
            out.println(ResultSetConverter.toString(resultSet, showHeader));
            return ExitCode.OK;

        } catch (SQLException e) {
            out.println(e.getMessage());
            return ExitCode.SQL_ERROR;
        }
    }

    /**
     * Executes the given SQL statement and returns with the result as a string.
     *
     * @param dialect SQL dialect, used during the execution of the SQL statement
     * @param host name of the database server
     * @param port number of the port where the server listens for requests
     * @param database name of the particular database on the server, lso known as the SID in Oracle terminology
     * @param sql sql statement to be executed
     * @return 0 if the SQL statement was executed properly, otherwise 1
     */
    public ExitCode execute(Dialect dialect, String host, int port, String database, String sql) {
        String jdbcUrl = dialect.getJdbcUrl(host, port, database);
        return execute(jdbcUrl, sql);
    }

    /**
     * Flag to control how detailed log is provided by this class.
     * The default value is false.
     *
     * @param verbose if true then detailed log will be shown
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Flag to control whether the name of the fields from the SQL result set is displayed or not.
     *
     * @param showHeader value of the flag
     */
    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

    /**
     * Set the standard output where the application writes logs.
     *
     * @param out the output writer
     */
    public SqlStatementExecutor(PrintStream out) {
        this.out = out;
    }

    /**
     * Connects to the database.
     *
     * @param jdbcUrl the connection JDBC URL
     * @return java connection object
     * @throws SQLException in case of error
     */
    private Connection getConnection(String jdbcUrl) throws SQLException {
        if (verbose) {
            out.println("getting connection to " + jdbcUrl + "...");
        }
        return DriverManager.getConnection(jdbcUrl, user, password);
    }
}