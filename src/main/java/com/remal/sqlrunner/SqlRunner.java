package com.remal.sqlrunner;

import java.util.Objects;
import java.util.concurrent.Callable;

import com.remal.sqlrunner.domain.Dialect;
import com.remal.sqlrunner.domain.ExitCode;
import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * SQL command-line tool.
 * The tool executes the given SQL and shows the result on the standard output.
 *
 * <p>
 * created on 09/07/2020
 * </p>
 *
 * @author arnold.somogyi@gmail.com
 */
@Command(name = "SqlRunner",
        sortOptions = false,
        usageHelpWidth = 100,
        description = "SQL command line tool. It executes the given SQL and show the result on the standard output.%n",
        parameterListHeading = "General options:%n",
        exitCodeListHeading = "%nExit codes:%n",
        exitCodeOnUsageHelp = ExitCode.CLI_ERROR_EXIT_CODE,
        exitCodeList = {
                "0:Successful program execution.",
                "1:An unexpected error appeared while executing the SQL statement.",
                "2:Usage error. User input for the command was incorrect." },
        footerHeading = "%nPlease report issues at arnold.somogyi@gmail.com.",
        footer = "%nDocumentation, source code: https://github.com/zappee/sql-runner.git")
public class SqlRunner implements Callable<Integer> {

    /**
     * Definition of the general command line options.
     */
    @Option(names = {"-?", "--help"},
            usageHelp = true,
            description = "Display this help and exit.")
    private boolean help;

    @Option(names = {"-v", "--verbose"},
            description = "It provides additional details as to what the tool is doing.")
    private boolean verbose;

    @Option(names = {"-c", "--dialect"},
            defaultValue = Dialect.ORACLE_VALUE,
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            description = "SQL dialect used during the execution of the SQL statement. "
                    + "Supported SQL dialects: ${COMPLETION-CANDIDATES}.")
    private Dialect dialect;

    @Option(names = {"-s", "--showHeader"},
            description = "Shows the name of the fields from the SQL result set.")
    private boolean showHeader;

    @Option(names = {"-U", "--user"},
            required = true,
            description = "Name for the login.")
    private String user;

    @ArgGroup(multiplicity = "1",
            heading = "%nSpecify a password for the connecting user:%n")
    PasswordArgGroup passwordArgGroup;

    @ArgGroup(multiplicity = "1",
            heading = "%nProvide a JDBC URL:%n")
    MainArgGroup mainArgGroup;

    /**
     * A parameter group for password.
     * Password can be provided on two different ways:
     *    (1) via a parameter
     *    (1) use the interactive mode where user needs to type the password
     */
    static class PasswordArgGroup {
        @Option(names = {"-P", "--password"},
                required = true,
                description = "Password for the connecting user.")
        private String password;

        @Option(names = {"-I", "--iPassword"},
                required = true,
                interactive = true,
                description = "Interactive way to get the password for the connecting user.")
        private String interactivePassword;
    }

    /**
     * Two exclusive parameter groups:
     *    (1) JDBC URL parameter
     *    (2) Custom connection parameters
     */
    static class MainArgGroup {
        /**
         * JDBC URL option (only one parameter).
         */
        @Option(names = {"-j", "--jdbcUrl"},
                arity = "1",
                description = "JDBC URL, example: jdbc:oracle:<drivertype>:@//<host>:<port>/<database>.")
        private String jdbcUrl;

        /**
         * Custom connection parameters group.
         */
        @ArgGroup(exclusive = false,
                multiplicity = "1",
                heading = "%nCustom configuration:%n")
        private CustomConfigurationGroup customConfigurationGroup;
    }

    /**
     * Definition of the SQL which will be executed.
     */
    @Parameters(index = "0",
            arity = "1",
            description = "SQL to be executed. Example: 'select 1 from dual'")
    private String sql;

    /**
     * Custom connection parameters.
     */
    static class CustomConfigurationGroup {
        @Option(names = {"-h", "--host"},
                required = true,
                description = "Name of the database server.")
        private String host;

        @Option(names = {"-p", "--port"},
                required = true,
                description = "Number of the port where the server listens for requests.")
        private int port;

        @Option(names = {"-d", "--database"},
                required = true,
                description = "Name of the particular database on the server. Also known as the SID in Oracle "
                        + "terminology.")
        private String database;
    }

    /**
     * The entry point of the executable JAR.
     *
     * @param args command line parameters
     */
    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new SqlRunner());
        int exitCode = cmd.execute(args);
        System.exit(exitCode);
    }

    /**
     * It is used to create a thread.
     *
     * @return exit code
     */
    @Override
    public Integer call() {
        // initialization of the SQL statement executor
        SqlStatementExecutor executor;
        if (Objects.nonNull(passwordArgGroup.password)) {
            executor = new SqlStatementExecutor(user, passwordArgGroup.password);
        } else {
            executor = new SqlStatementExecutor(user, passwordArgGroup.interactivePassword);
        }

        executor.setVerbose(verbose);
        executor.setShowHeader(showHeader);

        // execute the SQL statement and print the resut to the standard output
        if (Objects.nonNull(mainArgGroup.jdbcUrl)) {
            return executor.execute(mainArgGroup.jdbcUrl, sql).getExitCode();
        } else {
            return executor
                    .execute(
                            dialect,
                            mainArgGroup.customConfigurationGroup.host,
                            mainArgGroup.customConfigurationGroup.port,
                            mainArgGroup.customConfigurationGroup.database,
                            sql)
                    .getExitCode();
        }
    }
}
