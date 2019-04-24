/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project.controller;

import c195project.User;
import c195project.Address;
import c195project.Country;
import c195project.CountryDAO;
import c195project.City;
import c195project.CityDAO;
import c195project.Customer;
import c195project.CustomerDAO;
import c195project.FileLogger;
import c195project.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dhoward
 */
public class CreateUpdateCustomerViewController implements Initializable {

    @FXML
    VBox mainVBox;
    
    @FXML
    ComboBox<Country> countryList;
    
    @FXML
    ComboBox<City> cityList;
    
    @FXML
    TextField customerNameField;
    
    @FXML
    TextField address1Field;
    
    @FXML
    TextField address2Field;
    
    @FXML
    TextField postalCodeField;
    
    @FXML
    TextField phoneNumberField;
    
    @FXML
    Label errorPrompt;
    
    @FXML
    Button submitButton;
    
    private Boolean isAdd;
    
    private User loggedInUser;
    
    private Customer customer;
    
    // initialize logged in user object passed by another controller
    @FXML
    public void setLoggedInUser(User user) {
        this.loggedInUser = new User(user.getUserId(), user.getName());
    }
   
    // sets up controller to add or to update user
    @FXML
    public void createCustomer() {
        
        // set title to correct function
        submitButton.setText("Create User");

        // set boolean indicating we're adding a customer
        this.isAdd = true;
        
        submitButton.setText("Add Customer");
    }
    
    // sets up controller to update customer
    @FXML
    public void updateCustomer(Customer customer) {
        
        // set isAdd to false indicating we are udpating a customer, not adding
        this.isAdd = false;
        
        submitButton.setText("Update Customer");
        
        // set fields to match customer being edited
        this.customer = customer;
        this.customerNameField.setText(customer.getCustomerName());
        this.address1Field.setText(customer.getAddress().getAddress1());
        this.address2Field.setText(customer.getAddress().getAddress2());
        this.postalCodeField.setText(customer.getAddress().getPostalCode());
        this.phoneNumberField.setText(customer.getAddress().getPhone());
        this.countryList.getSelectionModel().select(customer.getAddress().getCity().getCountry());
        this.cityList.getSelectionModel().select(customer.getAddress().getCity());
        
        
    }
    
    @FXML
    protected void handleSubmitButton(ActionEvent event) throws SQLException, IOException {
        
        // create arraylist of required text fields for validation
        ArrayList<TextField> requiredFields = new ArrayList();
        requiredFields.add(customerNameField);
        requiredFields.add(address1Field);
        requiredFields.add(postalCodeField);
        
        // create arraylist of required comboboxes for validation
        ArrayList<ComboBox> requiredComboBoxes = new ArrayList();
        requiredComboBoxes.add(cityList);
        requiredComboBoxes.add(countryList);
        
        // validate zip code field, store result
        Boolean zipCodeValid = FormValidation.validateZipCode(postalCodeField);
        
        // valid phone number field, store result
        Boolean phoneValid = FormValidation.validatePhoneNumber(phoneNumberField);
        
        Boolean comboValid = FormValidation.validateComboBoxes(requiredComboBoxes);
        
        // if required fields have been validated, create the customer in the database
        // otherwise display an error message to the user
        if(!FormValidation.validateTextFields(requiredFields)
                || !comboValid 
                || !zipCodeValid || !phoneValid) {
            
            // prompt user with error message
            FormValidation.setErrorMessage(errorPrompt, true,
                    "Some equired field(s) are blank or invalid!");
            
        } else {
            
            if(isAdd) {
                // create address to be added or updated
                Address address = new Address(address1Field.getText(),
                        address2Field.getText(), postalCodeField.getText(),
                        phoneNumberField.getText(), cityList.getValue());

                // create customer to be added or updated
                Customer customer = new Customer(customerNameField.getText());
                customer.setAddress(address);
            
                // pass new customer and address to database to add or update
                try {
                    CustomerDAO.createCustomer(customer, loggedInUser);
                    FormValidation.setErrorMessage(errorPrompt, false, "Customer successfully added!");
                    FormValidation.clearAllFormFields(mainVBox);
                    FileLogger.writeLine("added customer " + customer.getCustomerName(), loggedInUser);
                } catch (SQLException ex) {
                    FormValidation.setErrorMessage(errorPrompt, true,
                        "Error while trying to save customer to the database!");
                    Logger.getLogger(CreateUpdateCustomerViewController.class.getName()).log(Level.SEVERE, null, ex);
                }  
            } else {
                
                // update values on customer object and nested address
                customer.setCustomerName(customerNameField.getText());
                customer.getAddress().setAddress1(address1Field.getText());
                customer.getAddress().setAddress2(address2Field.getText());
                customer.getAddress().setPostalCode(postalCodeField.getText());
                customer.getAddress().setPhone(phoneNumberField.getText());
                customer.getAddress().setCity(cityList.getValue());
                
                // run update in database
                CustomerDAO.updateCustomer(customer, loggedInUser);
                
                // log to file
                FileLogger.writeLine("updated customer " + customer.getCustomerName(), loggedInUser);
                
                // show success message to user
                FormValidation.setErrorMessage(errorPrompt, false, "Customer successfully updated!");
            }
        }
    }

    
    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {
        
        // get araylist of countries from database
        ArrayList<Country> countryArrayList = CountryDAO.getAllCountries();
        
        // use lambda expression to add each country to the combobox menu item
        // makes code much shorter
        countryArrayList.forEach(c -> countryList.getItems().add(c));
        
        // listener to dynamically load correct cities for country selected by user
        // lambda expression makes writing listener much more compact
        countryList.valueProperty().addListener((obj, oldValue, newValue) -> {
            
            // if a new country is picked, set city list based on that country
            if (newValue != null) {
                
                // clear existing list of cities
                cityList.getItems().clear();
                
                // clear current city selected
                cityList.setValue(null);
                
                // get the country object matching the country name selected by user
                ArrayList<City> cityArrayList = CityDAO.getCitiesByCountry(newValue);

                // add list of cities to city combo box
                // lambda expression makes code more succinct
                cityArrayList.forEach(c -> cityList.getItems().add(c));
                
                
            } else {
                
                // clear city combo box if no country is selected
                cityList.getItems().clear();
                cityList.setValue(null);
                
            }
            
        });
    }    
    
}
