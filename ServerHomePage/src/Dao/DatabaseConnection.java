package Dao;
import serverhomepage.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
        static {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static final String URL = "jdbc:derby://localhost:1527/TicTacDB";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static Connection theConnection;

    public static void openConnection(){
        try {
            if (theConnection == null || theConnection.isClosed()) {
                theConnection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connection opened");
            }
        } catch (SQLException e) {
            System.err.println("Error opening database connection");
            e.printStackTrace();
        }
    }

    public static boolean isConnectionOpen() {
        try {
            return theConnection != null && !theConnection.isClosed();
        } catch (SQLException e) {
            System.err.println("Error checking connection status");
            e.printStackTrace();
            return false;
        }
    }

    public static void closeConnection() {
        if (theConnection != null) {
            try {
                theConnection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing database connection");
                e.printStackTrace();
            }
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}