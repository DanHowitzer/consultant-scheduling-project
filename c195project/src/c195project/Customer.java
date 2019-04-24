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
public class Customer {
    private final SimpleStringProperty CustomerID = new SimpleStringProperty("");
    private final SimpleStringProperty CustomerName = new SimpleStringProperty("");
    private Address address;
    
    @Override
    public String toString() {
        return CustomerName.get();
    }
    
    public Customer() {
        this.address = new Address();
    }
    
    public Customer(String customerName) {
        setCustomerName(customerName);
    }
    
    public Customer(String customerID, String customerName, Address address) {
        setCustomerID(customerID);
        setCustomerName(customerName);
        this.address = address;
    }
    
    public String getCustomerName() {
        return CustomerName.get();
    }
    
    public String getCustomerID() {
        return CustomerID.get();
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setCustomerID(String customerID) {
        CustomerID.set(customerID);
    }
    
    public void setCustomerName(String customerName) {
        CustomerName.set(customerName);
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
}
