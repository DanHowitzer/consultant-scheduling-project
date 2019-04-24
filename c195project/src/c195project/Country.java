/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author dhoward
 */
public class Country {
    private final SimpleStringProperty Country = new SimpleStringProperty("");
    private final SimpleStringProperty CountryID = new SimpleStringProperty("");
    
    // override default toString method so the country name is displayed when
    // countries are added to combobox
    @Override
    public String toString() {
        return Country.get();
    }
    
    public Country() {
        
    }
    
    public Country(String country) {
        setCountry(country);
    }
    
    public Country(String countryID, String country) {
        setCountryID(countryID);
        setCountry(country);
    }
    
    public void setCountry(String country) {
        Country.set(country);
    }
    
    public void setCountryID(String countryID) {
        CountryID.set(countryID);
    }
    
    public String getCountry() {
        return Country.get();
    }
    
    public String getCountryID() {
        return CountryID.get();
    }
    
}
