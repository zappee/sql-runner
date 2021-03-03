package com.remal.sqlrunner.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Splits the given SQL commands into pieces.
 *
 * <p>Copyright 2021 Arnold Somogyi</p>
 *
 * @author arnold.somogyi@gmail.com
 */
public final class SqlCommandsParser {

    /**
     * Splits the given SQL commands into pieces.
     *
     * @param sqlStatements the given SQL statements
     * @param sqlCommandSeparator non-alphanumeric character used to separate multiple SQL commands
     * @return the parsed SQL statements
     */
    public static List<String> parse(String sqlStatements, String sqlCommandSeparator) {
        String[] statements = sqlStatements.split(sqlCommandSeparator);
        return Arrays.stream(statements).map(String::trim).collect(Collectors.toList());
    }

    /**
     * Utility classes should not have a public or default constructor.
     */
    private SqlCommandsParser() {
    }
}
