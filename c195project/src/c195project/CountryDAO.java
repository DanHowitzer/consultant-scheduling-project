/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project;

import java.util.ArrayList;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dhoward
 */
public class CountryDAO {
    public static ArrayList<Country> getAllCountries() {
        ArrayList<Country> countryList = new ArrayList();
        
        // try with 
        try(Connection connection = ConnectionManager.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("select country, countryid from country order by country");
            
            // loop through resultset and add every country to country arraylist
            while(results.next()) {
                
                // initialize country object with country info for this row of the resultset
                Country country = new Country(results.getString("countryid"), 
                    results.getString("country"));
                
                // add country to country arraylist
                countryList.add(country);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // return completed arraylist, could be empty if database is empty or connection errors out
        return countryList;
        
    }
    
}
