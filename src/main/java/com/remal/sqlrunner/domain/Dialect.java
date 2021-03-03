package com.remal.sqlrunner.domain;

import picocli.CommandLine.Command;

/**
 * Supported SQL dialects.
 *
 * <p>Copyright 2021 Arnold Somogyi</p>
 *
 * @author arnold.somogyi@gmail.com
 */
@Command
public enum Dialect {

    ORACLE(Dialect.ORACLE_VALUE, "jdbc:oracle:thin:@//host:port/database");

    /**
     * String representation of the enum item.
     */
    public static final String ORACLE_VALUE = "ORACLE";

    private String value;
    private String jdbcUrlTemplate;

    /**
     * Constructor.
     *
     * @param value representation of the enum item
     * @param jdbcUrlTemplate JDBC URL template
     */
    Dialect(String value, String jdbcUrlTemplate) {
        this.value = value;
        this.jdbcUrlTemplate = jdbcUrlTemplate;
    }

    /**
     * Getter method, builds the JDBC URL based on the given values.
     *
     * @param host name of the database server
     * @param port number of the port where the server listens for requests
     * @param database name of the particular database on the server, also known as the SID in Oracle
     * @return the JDBC URL
     */
    public String getJdbcUrl(String host, int port, String database) {
        return jdbcUrlTemplate
                .replace("host", host)
                .replace("port", String.valueOf(port))
                .replace("database", database);
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
