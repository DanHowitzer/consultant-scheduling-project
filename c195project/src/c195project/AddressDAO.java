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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dhoward
 */
public class AddressDAO {
    
    // inserts a new address into the database
    // returns addressID of newly added address
    public static String createAddress(Address address, User user) throws SQLException {
        PreparedStatement addressStatement = null;
        
        // insert query for inserting new address
        String addressSQL = "insert into address(address, address2, cityID,"
                + "postalCode, phone, createdate, createdby, lastupdate, lastupdateby) "
                + "values(?, ?, ?, ?, ?, now(), ?, now(), ?)";
        
        // try with resources to connect to the database
        try ( Connection connection = ConnectionManager.getConnection();) {
            // instantiate preparedStatement objects to run insert statements
            addressStatement = connection.prepareStatement(addressSQL);
            Statement lastInserted = connection.createStatement();
            
            // insert attributes for address into statement
            addressStatement.setString(1, address.getAddress1());
            addressStatement.setString(2, address.getAddress2());
            addressStatement.setString(3, address.getCity().getCityID());
            addressStatement.setString(4, address.getPostalCode());
            addressStatement.setString(5, address.getPhone());
            addressStatement.setString(6, user.getName());
            addressStatement.setString(7, user.getName());
            
            // execute update to insert address
            addressStatement.executeUpdate();
            
            // return addressID of inserted address by running last_insert_id() query
            ResultSet lastInsertedResult = lastInserted.executeQuery("select last_insert_id();");
            lastInsertedResult.next();
            return lastInsertedResult.getString("last_insert_id()");
            
        } catch (SQLException ex) {
            Logger.getLogger(AddressDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            
            // close prepared statement
            if(addressStatement != null) {
                addressStatement.close();
            }
            
        }
    }
    
    // gets addressID of address in database that matches attributes of passed address
    // returns the newest row if multiple rows are found
    public static String getAddressID(Address address) throws SQLException {
        PreparedStatement addressStatement = null;
        
        // create string for prepared statement
        String addressSQL = "select addressid from address where address = ? and "
                + "address2 = ? and postalcode = ? and phone = ? "
                + "order by addressid desc";
        
        // try with resources to connect to the database
        try ( Connection connection = ConnectionManager.getConnection();) {
            // instantiate preparedStatement objects to run insert statements
            addressStatement = connection.prepareStatement(addressSQL);
            
            // insert attributes for address into statement
            addressStatement.setString(1, address.getAddress1());
            addressStatement.setString(2, address.getAddress2());
            addressStatement.setString(3, address.getPostalCode());
            addressStatement.setString(4, address.getPhone());
            
            // execute update to insert address
            ResultSet results = addressStatement.executeQuery();
            
            // if a result is found, return the address id, otherwise return null
            if(results.next()) {
                return results.getString("addressid");
            } else {
                return null;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AddressDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            
            // close prepared statement
            if(addressStatement != null) {
                addressStatement.close();
            }
            
        }        
    }
    
    public static void updateAddress(Address address, User user) {
        PreparedStatement addressStatement = null;
        
        // query for updating address
        String addressSQL = "update address set address = ?, address2 = ?, "
                + "cityid = ?, postalcode = ?, phone = ?, lastupdate = now(),"
                + "lastupdateby = ? where addressid = ?";
        
        // try catch block with resources
        try (Connection connection = ConnectionManager.getConnection();) {
            
            // instantiate prepared statement to insert customer into database
            addressStatement = connection.prepareStatement(addressSQL);
            
            // set attributes for prepared statement
            // update address in database
            addressStatement.setString(1, address.getAddress1());
            addressStatement.setString(2, address.getAddress2());
            addressStatement.setString(3, address.getCity().getCityID());
            addressStatement.setString(4, address.getPostalCode());
            addressStatement.setString(5, address.getPhone());
            addressStatement.setString(6, user.getName());
            addressStatement.setString(7, address.getAddressID());
            
            // execute the update
            addressStatement.executeUpdate();
            System.out.println("Address updated!");
        } catch (SQLException ex) {
            Logger.getLogger(AddressDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // deletes an address from the database based on the addressid
    // remember to delete the customer first!
    public static void deleteAddress(Address address) {
        PreparedStatement addressStatement = null;
        
        // query to delete address
        String addressSQL = "delete from address where addressid = ?";
        
        // try catch block with resources
        try (Connection connection = ConnectionManager.getConnection();) {
            
            // instantiate prepared statement
            addressStatement = connection.prepareStatement(addressSQL);
            
            // add parameter for sql statement
            addressStatement.setString(1, address.getAddressID());
            
            // execute the update
            addressStatement.executeUpdate();
            System.out.println("Address deleted!");
        } catch (SQLException ex) {
            Logger.getLogger(AddressDAO.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}
