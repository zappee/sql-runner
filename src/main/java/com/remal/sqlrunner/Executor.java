package com.remal.sqlrunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQL executor.
 * This class connects to the database and executes the given SQL.
 *
 * <p>
 * created on 09/07/2020
 * </p>
 *
 * @author arnold.somogyi@gmail.com
 */
public class Executor {

    /**
     * Executes the given SQL and returns with the result as a string.
     *
     * @param jdbcUrl database to connect
     * @param sql SQL which will be executed
     * @return the result of the SQL
     */
    public static String execute(String jdbcUrl, String sql) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl)) {
            Statement stmt = connection.createStatement();

            ResultSet resSet = null;
            if (stmt.execute(sql)) {
                resSet = stmt.getResultSet();
            }

            System.exit(0);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        return null;
    }
}
