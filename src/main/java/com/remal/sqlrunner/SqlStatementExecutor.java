package com.remal.sqlrunner;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.Executors;

import com.remal.sqlrunner.domain.Dialect;
import com.remal.sqlrunner.domain.ExitCode;
import com.remal.sqlrunner.util.AnsiColor;
import oracle.jdbc.driver.OracleConnection;

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

    private static final int ONE_SECOND_IN_MILLI = 1000;

    /**
     * Error message template.
     */
    private static final String ERROR_MESSAGE = AnsiColor.RED_BOLD_BRIGHT + "%nERROR: %s" + AnsiColor.DEFAULT;

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
        ExitCode exitCode = ExitCode.OK;
        try (Connection connection = getConnection(jdbcUrl);
             Statement statement = connection.createStatement()) {

            if (verbose) {
                out.println("executing SQL statement: " + sql + "...");
            }

            ResultSet resultSet = statement.executeQuery(sql);
            out.println(ResultSetConverter.toString(resultSet, showHeader));
        } catch (SQLException e) {
            String errorMessage = String.format(ERROR_MESSAGE, e.toString());
            out.println(errorMessage);
            exitCode = ExitCode.SQL_ERROR;
        } finally {
            showExitCode(exitCode);
        }

        return exitCode;
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

        DriverManager.setLoginTimeout(ONE_SECOND_IN_MILLI / 1000);

        Properties properties = getConnectionArguments(user, password);
        Connection connection = DriverManager.getConnection(jdbcUrl, properties);
        connection.setNetworkTimeout(Executors.newSingleThreadExecutor(), ONE_SECOND_IN_MILLI);

        return connection;
    }

    /**
     * Build a list of arbitrary string tag/value pairs as connection arguments.
     * Normally at least a "user" and "password" property should be included.
     *
     * @param user username
     * @param password password
     * @return connection argument list
     */
    private Properties getConnectionArguments(String user, String password) {
        Properties properties = new Properties();
        properties.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, user);
        properties.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, password);
        return properties;
    }


    /**
     * Show the exit code of the application.
     *
     * @param exitCode the exit code
     */
    private void showExitCode(ExitCode exitCode) {
        String color;
        switch (exitCode) {
            case OK:
                color = AnsiColor.GREEN_BOLD_BRIGHT;
                break;

            case SQL_ERROR:
            case CLI_ERROR:
                color = AnsiColor.RED_BOLD_BRIGHT;
                break;

            default: color = AnsiColor.DEFAULT;
        }

        out.printf("%sReturn code: %d", color, exitCode.getExitCode());
        out.printf(AnsiColor.DEFAULT);
        out.printf("%n%n");
    }
}
