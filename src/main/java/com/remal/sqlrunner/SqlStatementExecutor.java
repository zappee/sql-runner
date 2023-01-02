package com.remal.sqlrunner;

import com.remal.sqlrunner.domain.ExitCode;
import com.remal.sqlrunner.util.AnsiColor;
import com.remal.sqlrunner.util.DevNullPrintStream;
import com.remal.sqlrunner.util.ResultSetConverter;
import com.remal.sqlrunner.util.SqlScriptParser;
import oracle.jdbc.OracleConnection;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

/**
 * This class connects to the database and executes the provided SQL statement.
 *
 * <p>Copyright 2021 Arnold Somogyi</p>
 *
 * @author arnold.somogyi@gmail.com
 */
public class SqlStatementExecutor {

    private static final String NEW_LINE = "\r|\n";
    private static final String NOTHING = "";

    private PrintStream logWriter;
    private boolean showHeader;
    private String user;
    private byte[] password;

    /**
     * Initialization method.
     *
     * @param quiet if true then no log message will be shown
     * @param showHeader controls whether the name of the fields from the SQL result set is displayed or not
     * @param user name for the login
     * @param password password for the connecting user
     */
    public SqlStatementExecutor(boolean quiet, boolean showHeader, String user, byte[] password) {
        this.logWriter = quiet ? DevNullPrintStream.getPrintStream() : System.out;
        this.showHeader = showHeader;
        this.user = user;
        this.password = password;
    }

    /**
     * Executes the given SQL statements and returns with the execution result.
     *
     * @param jdbcUrl the JDBC URL
     * @param sqlStatements the SQL statements to be executed
     * @return 0 if the SQL statement was executed properly
     */
    public ExitCode execute(String jdbcUrl, List<String> sqlStatements) {
        ExitCode exitCode = ExitCode.OK;

        try (Connection connection = getConnection(jdbcUrl);
             Statement statement = connection.createStatement()) {

            for (String sql : sqlStatements) {
                exitCode = executeQuery(statement, sql);
                if (exitCode != ExitCode.OK) {
                    break;
                }
            }

        } catch (SQLException e) {
            String sql = "";
            showSqlError(sql, e);
            exitCode = ExitCode.SQL_EXECUTION_ERROR;

        } catch (Exception e) {
            showInternalError(e);
            exitCode = ExitCode.INTERNAL_ERROR;

        } finally {
            showExitCode(exitCode);
        }

        return exitCode;
    }

    /**
     * Executes the given SQL Script file and returns with the execution result.
     *
     * @param jdbcUrl the JDBC URL
     * @param sqlScriptFile path to the SQL Script file
     * @param sqlCommandSeparator a non-alphanumeric character used to separate multiple SQL statements
     * @return 0 if the SQL statement was executed properly
     */
    public ExitCode execute(String jdbcUrl, Path sqlScriptFile, String sqlCommandSeparator) {
        ExitCode exitCode;
        try {
            SqlScriptParser sqlScriptParser = new SqlScriptParser();
            sqlScriptParser.setDelimiters(sqlCommandSeparator, false);

            logWriter.println(String.format("opening the %s SQL script file...", sqlScriptFile.toString()));
            List<String> sqlStatements = sqlScriptParser.parse(Files.newBufferedReader(sqlScriptFile));
            exitCode = execute(jdbcUrl, sqlStatements);

        } catch (Exception e) {
            showInternalError(e);
            exitCode = ExitCode.INTERNAL_ERROR;
        }

        return exitCode;
    }

    /**
     * Single SQL statement executor.
     *
     * @param statement JDBC statement
     * @param sql SQL statement to bo executed
     * @return result of the execution
     */
    private ExitCode executeQuery(Statement statement, String sql) {
        ExitCode exitCode = ExitCode.OK;
        logWriter.println("SQL statement: " + sql);

        try (ResultSet rs = statement.executeQuery(sql)) {
            logWriter.println(ResultSetConverter.toString(rs, showHeader));
        } catch (SQLException e) {
            if ("The SQL string is not a query".contains(e.getMessage())) {
                executeUpdate(statement, sql);
            } else {
                showSqlError(sql, e);
                exitCode = ExitCode.SQL_EXECUTION_ERROR;
            }
        }

        return exitCode;
    }

    /**
     * Single SQL statement executor.
     *
     * @param statement JDBC statement
     * @param sql SQL statement to bo executed
     * @return result of the execution
     */
    private ExitCode executeUpdate(Statement statement, String sql) {
        ExitCode exitCode = ExitCode.OK;
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            showSqlError(sql, e);
            exitCode = ExitCode.SQL_EXECUTION_ERROR;
        }

        return exitCode;
    }

    /**
     * Connects to the database.
     *
     * @param jdbcUrl the connection JDBC URL
     * @return java connection object
     * @throws SQLException in case of error
     */
    private Connection getConnection(String jdbcUrl) throws SQLException {
        logWriter.println("getting connection to " + jdbcUrl + "...");
        logWriter.println(String.format("using '%s' as the username", user));

        Properties properties = getConnectionArguments(user, new String(password));
        return DriverManager.getConnection(jdbcUrl, properties);
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

            case SQL_EXECUTION_ERROR:
            case CLI_ERROR:
                color = AnsiColor.RED_BOLD_BRIGHT;
                break;

            default: color = AnsiColor.DEFAULT;
        }

        logWriter.printf("%sReturn code: %d", color, exitCode.getExitCode());
        logWriter.printf(AnsiColor.DEFAULT);
        logWriter.printf("%n%n");
    }

    /**
     * Shows information about the error message appeared while executing the application.
     *
     * @param e the exception was thrown
     */
    private void showInternalError(Exception e) {
        String internalErrorMessage = AnsiColor.RED_BOLD_BRIGHT
                + "ERROR: An internal error occurred while executing the tool.%n"
                + "Message: %s"
                + AnsiColor.DEFAULT;

        String errorMessage = String.format(internalErrorMessage, e.toString().replaceAll(NEW_LINE, NOTHING));
        logWriter.println(errorMessage);
        e.printStackTrace();
    }

    /**
     * Shows information about the error message appeared while executing the SQL statement.
     * @param sql the SQL statement that was executed
     * @param e the exception was thrown
     */
    private void showSqlError(String sql, SQLException e) {
        String sqlErrorMessage = AnsiColor.RED_BOLD_BRIGHT
                + "ERROR: An error occurred while executing the sql statement.%n"
                + "Message: %s%n"
                + "SQL: %s"
                + AnsiColor.DEFAULT;

        String errorMessage = String.format(sqlErrorMessage, e.toString().replaceAll(NEW_LINE, NOTHING), sql);
        logWriter.println(errorMessage);
    }
}
