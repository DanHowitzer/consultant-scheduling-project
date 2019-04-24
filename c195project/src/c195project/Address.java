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
public class Address {
    private final SimpleStringProperty AddressID = new SimpleStringProperty("");
    private final SimpleStringProperty Address1 = new SimpleStringProperty("");
    private final SimpleStringProperty Address2 = new SimpleStringProperty("");
    private final SimpleStringProperty PostalCode = new SimpleStringProperty("");
    private final SimpleStringProperty Phone = new SimpleStringProperty("");
    private City city;
    
    public Address() {
        
    }
    
    public Address(String address, String address2, String postalCode, String phone, City city) {
        setAddress1(address);
        setAddress2(address2);
        setPostalCode(postalCode);
        setPhone(phone);
        setCity(city);
    }
    
    public Address(String addressID, String address, String address2,
            String postalCode, String phone, City city) {
        setAddressID(addressID);
        setAddress1(address);
        setAddress2(address2);
        setPostalCode(postalCode);
        setPhone(phone);
        setCity(city);
    }
    
    public String getAddress1() {
        return Address1.get();
    }
    
    public String getAddress2() {
        return Address2.get();
    }
    
    public String getPostalCode() {
        return PostalCode.get();
    }
    
    public String getPhone() {
        return Phone.get();
    }
    
    public String getAddressID() {
        return AddressID.get();
    }
    
    public City getCity() {
        return city;
    }
    
    public void setAddressID(String addressID) {
        AddressID.set(addressID);
    }
    
    public void setAddress1(String address) {
        Address1.set(address);
    }
    
    public void setAddress2(String address2) {
        Address2.set(address2);
    }
    
    public void setPostalCode(String postalCode) {
        PostalCode.set(postalCode);
    }
    
    public void setPhone(String phone) {
        Phone.set(phone);
    }
    
    public void setCity(City city) {
        this.city = city;
    }
}
