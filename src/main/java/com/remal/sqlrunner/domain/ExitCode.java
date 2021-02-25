package com.remal.sqlrunner.domain;

/**
 * List of the exit codes of the application.
 *
 * @author arnold.somogyi@gmail.com
 */
public enum ExitCode {

    /**
     * Used in case of the successful program execution.
     */
    OK(ExitCode.OK_EXIT_CODE),

    /**
     * Command Line Interface error.
     * This exit code is used in case of missing command line parameter(s).
     */
    CLI_ERROR(ExitCode.CLI_ERROR_EXIT_CODE),

    /**
     * Used if an unexpected error appeared while executing the SQL statement.
     */
    SQL_EXECUTION_ERROR(ExitCode.SQL_EXECUTION_ERROR_EXIT_CODE);

    /**
     * Exit codes constants.
     */
    public static final int OK_EXIT_CODE = 0;
    public static final int CLI_ERROR_EXIT_CODE = 1;
    public static final int SQL_EXECUTION_ERROR_EXIT_CODE = 2;


    private int exitCodeRepresentation;

    /**
     * Constructor.
     *
     * @param exitCode exit code
     */
    ExitCode(int exitCode) {
        this.exitCodeRepresentation = exitCode;
    }

    /**
     * Getter method.
     *
     * @return exit code
     */
    public int getExitCode() {
        return exitCodeRepresentation;
    }
}
