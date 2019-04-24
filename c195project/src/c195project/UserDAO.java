/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dhoward
 */

public class UserDAO {
    
    
    
    // return true if username and password matches a user in the database
    public static String login(String username, String password) throws SQLException {
        PreparedStatement statement = null;
        
        String loginSQL = "select userid, username from user where username = ? and "
                + "password = ?";
        
        // try getting connection with resources
        // will close connection regardless of whether it is successful or not
        try (Connection connection = ConnectionManager.getConnection();) {
            
            // instantiate prepared statement to get user matching username + password
            // from database
            statement = connection.prepareStatement(loginSQL);
            
            // insert variables into prepared statement
            statement.setString(1, username);
            statement.setString(2, password);
            
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            
            // get resultset
            ResultSet results = statement.executeQuery();
            
            // if a result is returned, return a User object from the database results
            // if not return null to signify that the user was not found with that
            // username and password combination
            if(results.next()) {
                System.out.println("Succesfully logged in!");
                return results.getString("userid");
            } else {
                System.out.println("User not found with that username / password");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            statement.close();
        }
    }
    
    // get report of every user in the database
    public static String getUserReport() {
        
        // stringbuilder for building the report string to pass back
        StringBuilder message = new StringBuilder();
        
        // create parepared statement
        PreparedStatement statement = null;
        
        // create sql query string
        // we are assuming all appointments in database start and end on the same date
        String sqlQuery = "select username as 'consultant' " +
                                "from user " +
                                "order by username";
        
                // try with resources to get appointments on the specified date
        try(Connection connection = ConnectionManager.getConnection();) {
            
            // fully instantiate statement
            statement = connection.prepareStatement(sqlQuery);
            
            // query database and get results
            ResultSet results = statement.executeQuery();
            
            message.append("Report: List of all consultants (users) \n");
            message.append("---------------------------\n\n");
            
            // turn resultset into a report
            while(results.next()) {
                
                message.append(results.getString("consultant") + "\n");
                     
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return message.toString();
        
    }
}
