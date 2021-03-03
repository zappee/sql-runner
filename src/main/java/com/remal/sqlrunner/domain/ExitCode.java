package com.remal.sqlrunner.domain;

/**
 * List of the exit codes of the application.
 *
 * <p>Copyright 2021 Arnold Somogyi</p>
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
     * This exit code is used in case of a missing command line parameter.
     */
    CLI_ERROR(ExitCode.CLI_ERROR_EXIT_CODE),

    /**
     * Used if an error appeared while executing an SQL statement.
     */
    SQL_EXECUTION_ERROR(ExitCode.SQL_EXECUTION_ERROR_EXIT_CODE),

    /**
     * Used if an unexpected program error appeared.
     */
    INTERNAL_ERROR(ExitCode.INTERNAL_ERROR_EXIT_CODE);

    /**
     * Representation of ExitCode.OK
     */
    public static final int OK_EXIT_CODE = 0;

    /**
     * Representation of ExitCode.CLI_ERROR
     */
    public static final int CLI_ERROR_EXIT_CODE = 1;

    /**
     * Representation of ExitCode.SQL_EXECUTION
     */
    public static final int SQL_EXECUTION_ERROR_EXIT_CODE = 2;

    /**
     * Representation of ExitCode.INTERNAL_ERROR
     */
    public static final int INTERNAL_ERROR_EXIT_CODE = 3;

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
