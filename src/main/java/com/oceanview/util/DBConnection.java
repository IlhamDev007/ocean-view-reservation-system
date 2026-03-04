package com.oceanview.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/OceanView";
    private static final String USER = "root";  // Replace with your MySQL username
    private static final String PASSWORD = "pass123";  // Replace with your MySQL password

    // Method to establish and return a connection to the database
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Step 1: Load and register MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Step 2: Establish the connection
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connection to the database established successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
        return conn;
    }
}