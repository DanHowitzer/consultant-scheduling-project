/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dhoward
 */
public class CityDAO {
    public static ArrayList<City> getCitiesByCountry(Country country) {
        ArrayList<City> cityList = new ArrayList();
        
        // try with resources to open connection to database
        try(Connection connection = ConnectionManager.getConnection();) {
            
            // sql statement to get list of cities matching country code
            String citySQL = "select city, cityid, countryid from city where"
                    + " countryid = ? order by city";
            
            // instantiate prepared statement for query
            PreparedStatement statement = connection.prepareStatement(citySQL);
            
            // fill in countryid in query, convert string to int to match
            // datatype in database
            statement.setInt(1, Integer.parseInt(country.getCountryID()));
            
            // execute statement and store results in resultset
            ResultSet results = statement.executeQuery();
            
            // loop through resultset and add every country to country arraylist
            while(results.next()) {
                
                // initialize country object with country info for this row of the resultset
                City city = new City(results.getString("cityid"), 
                    results.getString("city"), country);
                
                // add country to country arraylist
                cityList.add(city);
                
            }
            
        } catch (SQLException ex) {
            
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        // return completed arraylist, could be empty if database is empty or connection errors out
        return cityList;
        
    }    
}
