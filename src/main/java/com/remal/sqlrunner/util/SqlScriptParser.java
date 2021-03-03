package com.remal.sqlrunner.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses the Oracle SQL Script and cut it to SQL statements.
 *
 * <p>Copyright 2021 Arnold Somogyi</p>
 *
 * @author arnold.somogyi@gmail.com
 */
public class SqlScriptParser {

    private static final String DEFAULT_DELIMITER = ";";

    private String delimiter;
    private boolean fullLineDelimiter = false;

    /**
     * Default constructor.
     */
    public SqlScriptParser() {
        this.delimiter = DEFAULT_DELIMITER;
    }

    /**
     * Runs an SQL script (read in using the Reader parameter) using the
     * connection passed in
     *
     * @param reader the source of the script
     * @return the SQL statements
     * @throws IOException if there is an error reading from the Reader
     */
    public List<String> parse(BufferedReader reader) throws IOException {
        List<String> sqlStatements = new ArrayList<>();
        String originalDelimiter = delimiter;
        StringBuilder sqlStatement = new StringBuilder();

        while (reader.ready()) {
            String line = reader.readLine();
            String trimmedLine = line.trim();

            if (isCommentLine(trimmedLine)) {
                // do nothing

            } else if (trimmedLine.toLowerCase().startsWith("set define off")) {
                // stored procedure definition starts from here
                delimiter = "/";

            } else if (!fullLineDelimiter && trimmedLine.endsWith(delimiter)
                    || fullLineDelimiter && trimmedLine.equals(delimiter)) {

                sqlStatement.append(line.substring(0, line.lastIndexOf(delimiter)));
                sqlStatement.append(" ");
                sqlStatements.add(sqlStatement.toString());
                sqlStatement = new StringBuilder();

                if (trimmedLine.equals(delimiter)) {
                    // end of the definition of the stored procedure
                    delimiter = originalDelimiter;
                }

            } else {
                sqlStatement.append(line);
                sqlStatement.append(" ");
            }
        }

        sqlStatements.add("commit");

        return sqlStatements;
    }

    /**
     * Setter method.
     *
     * @param delimiter a non-alphanumeric character used to separate multiple SQL statements
     * @param fullLineDelimiter set it true if a line contains only delimiter cn be skipped
     */
    public void setDelimiters(String delimiter, boolean fullLineDelimiter) {
        this.delimiter = delimiter;
        this.fullLineDelimiter = fullLineDelimiter;
    }

    /**
     * Checks whether the line read from the SQL Script file is a comment or not.
     *
     * @param line line from the SQL Script file
     * @return true if the line is  comment
     */
    private boolean isCommentLine(String line) {
        boolean commentLine = false;
        if (line.startsWith("--") || line.startsWith("//") || (line.length() < 1)) {
            commentLine = true;
        }

        return commentLine;
    }
}
