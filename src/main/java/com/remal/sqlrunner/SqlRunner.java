package com.remal.sqlrunner;

import com.remal.sqlrunner.domain.Dialect;
import com.remal.sqlrunner.domain.ExitCode;
import com.remal.sqlrunner.domain.SqlCommandSeparator;
import com.remal.sqlrunner.picocli.CustomOptionRenderer;
import com.remal.sqlrunner.util.SqlCommandsParser;
import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * SQL command-line tool.
 * The tool executes the given SQL and shows the result on the standard output.
 *
 * <p>Copyright 2021 Arnold Somogyi</p>
 *
 * @author arnold.somogyi@gmail.com
 */
@Command(name = "SqlRunner",
        sortOptions = false,
        usageHelpWidth = 100,
        description = "SQL command line tool. It executes the given SQL and shows the result on the standard output.%n",
        parameterListHeading = "General options:%n",
        exitCodeListHeading = "%nExit codes:%n",
        exitCodeOnUsageHelp = ExitCode.CLI_ERROR_EXIT_CODE,
        exitCodeList = {
                "0:Successful program execution.",
                "1:An unexpected error appeared while executing the SQL statement.",
                "2:Usage error. The user input for the command was incorrect.",
                "3:Internal program error." },
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

    @Option(names = {"-q", "--quiet"},
            description = "In this mode nothing will be printed to the output.")
    private boolean quiet;

    @Option(names = {"-c", "--dialect"},
            defaultValue = Dialect.ORACLE_VALUE,
            description = "SQL dialect used during the execution of the SQL statement. "
                    + "Supported SQL dialects: ${COMPLETION-CANDIDATES}.%n"
                    + "  Default: " + Dialect.ORACLE_VALUE)
    private Dialect dialect;

    @Option(names = {"-e", "--cmdsep"},
            defaultValue = SqlCommandSeparator.SEMICOLON_VALUE,
            paramLabel = "<commandSeparator>",
            description = "SQL separator is a non-alphanumeric character used to separate multiple SQL statements. "
                    + "Multiply statements is only recommended for SQL INSERT and UPDATE. The result of the queries "
                    + "will not be displayed.%n"
                    + "  Default: " + SqlCommandSeparator.SEMICOLON_VALUE)
    private String sqlCommandSeparator;

    @Option(names = {"-S", "--showHeader"},
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

    @ArgGroup(multiplicity = "1",
            heading = "%nSQL statement(s) to be executed:%n")
    SqlStatementGroup sqlStatementGroup;

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
     * Two exclusive command line parameters:
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
     * Two exclusive command line parameters:
     *    (1) SQL statements provided via the command line interface
     *    (2) SQL statements from an external file
     */
    static class SqlStatementGroup {
        @Option(names = {"-s", "--sql"},
                required = true,
                description = "SQL statements to be executed. Multiply statements can be provided. "
                        + "Example: 'insert into...; insert into ...; commit'")
        private String sqlStatements;

        @Option(names = {"-f", "--file"},
                required = true,
                description = "Path to the SQL script file.")
        private String sqlScriptFile;
    }

    /**
     * Custom connection parameters.
     */
    static class CustomConfigurationGroup {
        private static final String DEFAULT_ORACLE_PORT = "1521";
        private static final String DEFAULT_HOST = "localhost";

        @Option(names = {"-h", "--host"},
                required = true,
                defaultValue = DEFAULT_HOST,
                description = "Name of the database server.%n"
                + "  Default: " + DEFAULT_HOST)
        private String host;

        @Option(names = {"-p", "--port"},
                required = true,
                defaultValue = DEFAULT_ORACLE_PORT,
                description = "Number of the port where the server listens for requests.%n"
                        + "  Default: " + DEFAULT_ORACLE_PORT)
        private int port;

        @Option(names = {"-d", "--database"},
                required = true,
                description = "Name of the particular database on the server. Also known as the SID in Oracle "
                        + "terminology.")
        private String database;
    }

    /**
     * It is used to create a thread.
     *
     * @return exit code
     */
    @Override
    public Integer call() {
        String password = passwordArgGroup.password;
        if (Objects.isNull(password)) {
            password = passwordArgGroup.interactivePassword;
        }

        String jdbcUrl = mainArgGroup.jdbcUrl;
        if (Objects.isNull(jdbcUrl)) {
            jdbcUrl = dialect.getJdbcUrl(
                    mainArgGroup.customConfigurationGroup.host,
                    mainArgGroup.customConfigurationGroup.port,
                    mainArgGroup.customConfigurationGroup.database);
        }

        SqlStatementExecutor executor = new SqlStatementExecutor(quiet, showHeader, user, password.getBytes());

        if (Objects.nonNull(sqlStatementGroup.sqlStatements)) {
            List<String> sqlStatements = SqlCommandsParser.parse(sqlStatementGroup.sqlStatements, sqlCommandSeparator);
            return executor.execute(jdbcUrl, sqlStatements).getExitCode();
        } else {
            Path path = Paths.get(sqlStatementGroup.sqlScriptFile);
            return executor.execute(jdbcUrl, path, sqlCommandSeparator).getExitCode();
        }
    }

    /**
     * The entry point of the executable JAR.
     *
     * @param args command line parameters
     */
    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new SqlRunner());
        cmd.setHelpFactory(new CustomOptionRenderer());
        int exitCode = cmd.execute(args);
        System.exit(exitCode);
    }
}
