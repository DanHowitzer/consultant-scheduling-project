package c195project;


import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dhoward
 */
public class CustomerDAO {
    public static ArrayList<Customer> getAllCustomers() {
        
        // temporary array list to hold results of query
        ArrayList<Customer> customerList = new ArrayList();
        
        // try with resources to get full data for each customer in database
        // including name, address, city, and country
        try(Connection connection = ConnectionManager.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("select c.customerid,"
                    + "c.customername, a.addressid, a.address, a.address2, "
                    + "a.postalcode, a.phone, t.cityid, t.city, o.countryid, "
                    + "o.country from customer c inner join address a "
                    + "on c.addressid = a.addressid inner join city t "
                    + "on a.cityid = t.cityid inner join country o "
                    + "on t.countryid = o.countryid order by c.customername");
            
            // loop through resultset and add every country to country arraylist
            while(results.next()) {
                
                Country country = new Country(results.getString("o.countryid"), 
                    results.getString("o.country"));
                
                City city = new City(results.getString("t.cityid"),
                    results.getString("t.city"), country);
                
                Address address = new Address(results.getString("a.addressid"),
                    results.getString("a.address"), results.getString("a.address2"),
                    results.getString("a.postalcode"), results.getString("a.phone"), city);
                
                Customer customer = new Customer(results.getString("c.customerid"),
                    results.getString("c.customername"), address);
                
                // add customer to customer arraylist
                customerList.add(customer);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // return completed arraylist, could be empty if database is empty or connection errors out
        return customerList;
    }
    
    public static void createCustomer(Customer customer, User user) throws SQLException {
        
        PreparedStatement customerStatement = null;
        
        // insert query for inserting new customer after address is inserted
        String customerSQL = "insert into customer(customerName, addressID, "
                + "active, createdate, createdby, lastupdate, lastupdateby)"
                + "values(?, ?, 1, now(), ?, now(), ?)";
        
        // try catch block with resources
        try (Connection connection = ConnectionManager.getConnection();) {
            
            // instantiate prepared statement to insert customer into database
            customerStatement = connection.prepareStatement(customerSQL);
            
            // set attributes for prepared statement
            // create address in database and set addressID in the statement
            customerStatement.setString(1, customer.getCustomerName());
            customerStatement.setString(2, AddressDAO.createAddress(customer.getAddress(), user));
            customerStatement.setString(3, user.getName());
            customerStatement.setString(4, user.getName());
            
            // execute the insert
            customerStatement.executeUpdate();
            System.out.println("Customer inserted!");
        }
    }
    
    public static void updateCustomer(Customer customer, User user) throws SQLException {
        
        PreparedStatement customerStatement = null;
        
        // insert query for updating customer
        String customerSQL = "update customer set customername = ?,  "
                + "lastupdate = now(), lastupdateby = ? where customerid = ?";
        
        // try catch block with resources
        try (Connection connection = ConnectionManager.getConnection();) {
            
            // instantiate prepared statement to insert customer into database
            customerStatement = connection.prepareStatement(customerSQL);
            
            // set attributes for prepared statement
            // create address in database and set addressID in the statement
            customerStatement.setString(1, customer.getCustomerName());
            customerStatement.setString(2, user.getName());
            customerStatement.setString(3, customer.getCustomerID());
            
            AddressDAO.updateAddress(customer.getAddress(), user);
            
            // execute the insert
            customerStatement.executeUpdate();
            System.out.println("Customer updated!");
        }
    }
    
    public static void deleteCustomer(Customer customer, User user) throws SQLException {
        
        PreparedStatement customerStatement = null;
        PreparedStatement appointmentStatement = null;
        
        // sql statements to cascade delete every appointment associated with customer
        // customer is deleted as well as address for customer
        String appointmentSQL = "delete from appointment where customerid = ?";
        String customerSQL = "delete from customer where customerid = ?";
        
        // set parameters for prepared statements
        try (Connection connection = ConnectionManager.getConnection();) {
            
            customerStatement = connection.prepareStatement(customerSQL);
            
            // set parameters for sql statements
            customerStatement.setString(1, customer.getCustomerID());
            appointmentStatement.setString(1, customer.getCustomerID());
            
            customerStatement.execute();
            System.out.println("Customer deleted!");
            
        } finally {
            
            // delete address after customer is deleted
            AddressDAO.deleteAddress(customer.getAddress());
        }
        
        
        
        
    }
}
