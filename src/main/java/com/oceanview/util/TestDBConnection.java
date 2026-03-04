package com.oceanview.util;

import java.sql.Connection;

public class TestDBConnection {
    public static void main(String[] args) {
        // Get the database connection
        Connection connection = DBConnection.getConnection();

        if (connection != null) {
            System.out.println("Connection is successful!");
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }
}