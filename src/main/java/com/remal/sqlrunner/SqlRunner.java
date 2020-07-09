package com.remal.sqlrunner;

import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * SQL command line tool.
 * It executes the given SQL and show the result on the standard output.
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
        description = "SQL command line tool. It executes the given SQL and show the result on the standard output.\n",
        parameterListHeading = "General options:\n",
        footerHeading = "\nPlease report issues at arnold.somogyi@gmail.com.",
        footer = "\nDocumentation, source code: https://github.com/zappee/sql-runner.git")
public class SqlRunner implements Runnable {

    /**
     * Definition of the general command line options.
     */
    @Option(names = {"-?", "--help"}, usageHelp = true, description = "Display this help and exit.")
    private boolean help;

    @Option(names = {"-d", "--dialect"}, defaultValue = "oracle", showDefaultValue = CommandLine.Help.Visibility.ALWAYS, description = "Supported SQL dialects: oracle.")
    private static String dialect;

    @ArgGroup(exclusive = true, multiplicity = "1", heading = "\nProvide a JDBC URL:\n")
    MainArgGroup mainArgGroup;

    /**
     * Two exclusive parameter groups:
     *    (1) JDBC URL parameter
     *    (2) Custom connection parameters
     */
    static class MainArgGroup {
        /**
         * JDBC URL option (only one parameter).
         */
        @Option(names = {"-j", "--jdbcUrl"}, arity = "1", description = "JDBC URL, example: jdbc:oracle:<drivertype>:@//<host>:<port>/<database>.")
        private static String jdbcUrl;

        /**
         * Custom connection parameter group.
         */
        @ArgGroup(exclusive = false, multiplicity = "1", heading = "\nCustom configuration:\n")
        CustomConfigurationGroup customConfigurationGroup;
    }

    /**
     * Definition of the SQL which will be executed.
     */
    @Parameters(index = "0", arity = "1", description = "SQL to be executed. Example: 'select 1 from dual'")
    String sql;

    /**
     * Custom connection parameters.
     */
    static class CustomConfigurationGroup {
        @Option(names = {"-h", "--host"}, required = true, description = "Name of the database server.")
        private static String host;

        @Option(names = {"-p", "--port"}, required = true, description = "Number of the port where the server listens for requests.")
        private static String port;

        @Option(names = {"-s", "--sid"}, required = true, description = "Name of the particular database on the server. Also known as the SID in Oracle terminology.")
        private static String sid;

        @Option(names = {"-U", "--user"}, required = true, description = "Name for the login.")
        private static String user;

        @Option(names = {"-P", "--password"}, required = true, description = "Password for the connecting user.")
        private static String password;
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
     */
    @Override
    public void run() {
        int exitCode = 0; //executeMyStaff();
        System.exit(exitCode);
    }
}
