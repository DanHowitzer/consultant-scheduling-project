/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author dhoward
 */
public class ConnectionManager {
    
    public static final String url = "jdbc:mysql://52.206.157.109:3306/U043lf";
//    public static final String url = "jdbc:mysql://localhost:3306/U043lf";
    public static final String username = "U043lf";
    public static final String password = "53688154197";
    public static final String driver = "com.mysql.cj.jdbc.Driver";
    
    
    // creates and passes back database connection
    public static Connection getConnection() {
        // establish connection to database
        System.out.println("Connecting to database...");
        
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Database driver not found", e);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to database", e);
        }
    }
    
}
