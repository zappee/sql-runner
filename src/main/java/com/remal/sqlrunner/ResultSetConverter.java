package com.remal.sqlrunner;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Prints the content of the JDBC ResultSet.
 *
 * @author arnold.somogyi@gmail.com
 */
public class ResultSetConverter {

    private static final String LAST_CHAR_REGEX = ".$";
    private static final String EMPTY_STRING = "";

    /**
     * JDBC ResultSet to string converter.
     *
     * @param resultSet JDBC ResultSet
     * @param showHeader flag to control whether the name of the fields from the SQL result set is displayed or not
     * @return the string representation of the JDBC ResultSet
     * @throws SQLException in case of any error appears during the conversation
     */
    public static String toString(ResultSet resultSet, boolean showHeader) throws SQLException {
        ResultSetMetaData metadata = resultSet.getMetaData();
        StringBuilder result = new StringBuilder();

        if (showHeader) {
            result.append(getFieldNames(metadata));
        }

        int fieldNumber = metadata.getColumnCount();

        StringBuilder row = new StringBuilder();
        while (resultSet.next()) {
            for (int i = 1; i <= fieldNumber; i++) {
                row.append(resultSet.getString(i)).append(";");
            }
            String s = row.toString().replaceAll(LAST_CHAR_REGEX, EMPTY_STRING) + System.lineSeparator();
            result.append(s);
        }

        return result.toString().trim();
    }

    /**
     * Converts the field names appears in the SQL result set to string.
     *
     * @param metadata result set metadata
     * @return the name of the fields as a string
     * @throws SQLException in case of any error appears during the conversation
     */
    private static String getFieldNames(ResultSetMetaData metadata) throws SQLException {
        StringBuilder sb = new StringBuilder();
        int fieldNumbers = metadata.getColumnCount();
        for (int i = 1; i <= fieldNumbers; i++) {
            sb.append(metadata.getColumnName(i)).append(";");
        }
        // remove the last character and return the value
        return sb.toString().replaceAll(LAST_CHAR_REGEX, "") + System.lineSeparator();
    }

    /**
     * Hidden constructor.
     * Utility classes should not have public constructors.
     */
    private ResultSetConverter() {
    }
}
