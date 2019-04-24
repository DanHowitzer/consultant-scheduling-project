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
public class City {
    
    private final SimpleStringProperty City = new SimpleStringProperty("");
    private final SimpleStringProperty CityID = new SimpleStringProperty("");
    private Country country;
    
    // override native toString method with a method that returns the city name
    // used to display the name of the city in a combobox
    @Override
    public String toString() {
        return City.get();
    }
    
    public City () {
        
    }
    
    public City(String city, Country country) {
        setCity(city);
        setCountry(country);
    }
    
    public City(String cityID, String city, Country country) {
        setCityID(cityID);
        setCity(city);
        setCountry(country);
    }
    
    public void setCity(String city) {
        City.set(city);
    }
    
    public void setCityID(String cityID) {
        CityID.set(cityID);
    }
    
    public void setCountry(Country country) {
        this.country = country;
    }
    
    public String getCity() {
        return City.get();
    }
    
    public String getCityID() {
        return CityID.get();
    }
    
    public Country getCountry() {
        return country;
    }
    
}
