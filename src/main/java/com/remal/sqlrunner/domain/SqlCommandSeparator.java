package com.remal.sqlrunner.domain;

/**
 * Default command line separator, used for separate multiply SQL commands.
 * E.g.: java -jar sql-runner.jar options "INSERT INTO user VALUES('katalin'); \
 *                                         INSERT INTO user VALUES('arnold'); \
 *                                         COMMIT"
 *
 * @author arnold.somogyi@gmail.com
 */
public enum SqlCommandSeparator {

    /**
     * default separator
     */
    SEMICOLON(SqlCommandSeparator.SEMICOLON_VALUE);

    /**
     * String representation of the enum item.
     */
    public static final String SEMICOLON_VALUE = ";";

    private String value;

    /**
     * Constructor.
     *
     * @param value representation of the enum item
     */
    SqlCommandSeparator(String value) {
        this.value = value;
    }

    /**
     * Getter.
     *
     * @return the string representation of the enum value
     */
    public String getValue() {
        return value;
    }
}
