/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project.controller;
import c195project.Customer;
import c195project.CustomerDAO;
import c195project.FileLogger;
import c195project.FormValidation;
import c195project.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author dhoward
 */
public class CustomerListViewController implements Initializable {
    
    // holds list of customers for customer table
    private ObservableList<Customer> customerList;
    
    User loggedInUser;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    private TableView<Customer> customerTable;
    
    @FXML
    private TableColumn<Customer, String> customerIDColumn;
    
    @FXML
    private TableColumn<Customer, String> nameColumn;
    
    @FXML
    private TableColumn<Customer, String> address1Column;
    
    @FXML
    private TableColumn<Customer, String> address2Column;
    
    @FXML
    private TableColumn<Customer, String> cityColumn;
    
    @FXML
    private TableColumn<Customer, String> countryColumn;
    
    @FXML
    private TableColumn<Customer, String> postalCodeColumn;
    
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    
    @FXML
    private Button createCustomerButton;
    
    @FXML
    private Button editCustomerButton;
    
    // initialize logged in user object passed by another controller
    @FXML
    public void setLoggedInUser(User user) {
        this.loggedInUser = new User(user.getUserId(), user.getName());
    }
    
    @FXML
    protected void handleUpdateCustomerButton(ActionEvent event) throws IOException {
        
        // create customer object using customer selected by user
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        
        // check to make sure a user is actually selected by customer
        if(customer != null) {
            FormValidation.setErrorMessage(errorLabel, false);

            // reload main screen and load create update customer view
            Stage stage;
            Parent root;
            stage=(Stage) this.editCustomerButton.getScene().getWindow();

            // reload main screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/c195project/view/MainScreen.fxml"));
            root = loader.load();
            MainScreenController controller = loader.getController();

            // load update / add customer view in center pane
            FXMLLoader customerLoader = controller.loadView("/c195project/view/CreateUpdateCustomerView.fxml");
            CreateUpdateCustomerViewController customerController = customerLoader.getController();

            // tell update / add customer view that we're adding a customer
            customerController.updateCustomer(customer);

            // pass logged in user's info to view
            customerController.setLoggedInUser(loggedInUser);

            // pass logged in user's info to main pane
            controller.setLoggedInUser(loggedInUser);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            FormValidation.setErrorMessage(errorLabel, true, "You must select a customer!");
        }
        
    }
    
    @FXML
    protected void handleDeleteCustomerButton(ActionEvent event) throws IOException {
        
        // instantiate copy of selected customer
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        
        // warn the user about deleting the customer and do it only if 
        // they confirm
        if(customer != null) {
            
            // clear all error dialogs
            FormValidation.setErrorMessage(errorLabel, false);
            
            // open up confirmation dialog for user to confirm deleting customer
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Are you sure you want to delete this customer? "
                    + "All of their appointments will also be deleted!");
            
            //ask user to confirm whether they want to delete
            Optional<ButtonType> result = alert.showAndWait();
            
            // if user confirms, delete customer, otherwise do nothing
            // if there's a sql error, let user know
            if (result.get() == ButtonType.OK){
                try {
                    CustomerDAO.deleteCustomer(customer, loggedInUser);
                    FileLogger.writeLine("deleted customer " + customer.getCustomerName(), loggedInUser);
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerListViewController.class.getName()).log(Level.SEVERE, null, ex);
                    FormValidation.setErrorMessage(errorLabel, true, "SQL error, customer not deleted!");
                } finally {
                    // show user message that they successfully deleted the customer
                    FormValidation.setErrorMessage(errorLabel, false, "Customer deleted successfully!");
                    
                    // refresh customers list
                    customerList = FXCollections.observableArrayList(CustomerDAO.getAllCustomers());
                    customerTable.setItems(customerList);
                }
            } else {
                // don't do anything
            }

        }
        else {
            // display error if user did not choose a customer before clicking "delete"
            FormValidation.setErrorMessage(errorLabel, true,
                    "You must select a customer before you can delete them!");
        }
    }

    @FXML
    protected void handleCreateCustomerButton(ActionEvent event) throws IOException {
        
        // reload main screen and load create update customer view
        Stage stage;
        Parent root;
        stage=(Stage) this.createCustomerButton.getScene().getWindow();
        
        // reload main screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/c195project/view/MainScreen.fxml"));
        root = loader.load();
        MainScreenController controller = loader.getController();
        
        // load update / add customer view in center pane
        FXMLLoader customerLoader = controller.loadView("/c195project/view/CreateUpdateCustomerView.fxml");
        CreateUpdateCustomerViewController customerController = customerLoader.getController();
        
        // tell update / add customer view that we're adding a customer
        customerController.createCustomer();
        
        // pass logged in user's info to view
        customerController.setLoggedInUser(loggedInUser);
        
        // pass logged in user's info to main pane
        controller.setLoggedInUser(loggedInUser);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // load customers into customer table from database
        customerList = FXCollections.observableArrayList(CustomerDAO.getAllCustomers());
        
        // set propertyvaluefactories for customerid and customername of the customer objects in the list
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        
        // use callbacks to get parameters from nested objects such as address, city, and country
        // from each customer object
        address1Column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer,String>,ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Customer, String> param) {
                return new SimpleStringProperty(param.getValue().getAddress().getAddress1());
            }
        });
        address2Column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer,String>,ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Customer, String> param) {
                return new SimpleStringProperty(param.getValue().getAddress().getAddress2());
            }
        });
        cityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer,String>,ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Customer, String> param) {
                return new SimpleStringProperty(param.getValue().getAddress().getCity().getCity());
            }
        });
        countryColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer,String>,ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Customer, String> param) {
                return new SimpleStringProperty(param.getValue().getAddress().getCity().getCountry().getCountry());
            }
        });
        postalCodeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer,String>,ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Customer, String> param) {
                return new SimpleStringProperty(param.getValue().getAddress().getPostalCode());
            }
        });
        phoneColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer,String>,ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Customer, String> param) {
                return new SimpleStringProperty(param.getValue().getAddress().getPhone());
            }
        });
        
        // set contents of table
        customerTable.setItems(customerList);
    }    
    

    
}
