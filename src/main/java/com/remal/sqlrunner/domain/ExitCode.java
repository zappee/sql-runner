package com.remal.sqlrunner.domain;

/**
 * List of the exit codes of the application.
 *
 * <p>
 * created on 10/07/2020
 * </p>
 *
 * @author arnold.somogyi@gmail.com
 */
public enum ExitCode {
    /**
     * Used in case of the successful program execution.
     */
    OK(1),

    /**
     * Used if an unexpected error appeared while executing the SQL statement.
     */
    SQL_ERROR(2);

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
