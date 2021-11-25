package com.mshlz.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static Connection connection = null;

    private static String host = "jdbc:postgresql://localhost:5432/blackjack";
    private static String user = "postgres";
    private static String password = "postgres";

    private DatabaseConnection() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(host, user, password);
                connection.setAutoCommit(false);

                System.out.println("DB Connection Succeeded!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return connection;
    }
}
