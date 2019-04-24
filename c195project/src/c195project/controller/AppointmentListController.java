/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project.controller;

import c195project.Appointment;
import c195project.Appointment;
import c195project.AppointmentDAO;
import c195project.Customer;
import c195project.CustomerDAO;
import c195project.FileLogger;
import c195project.FormValidation;
import c195project.TimeHandler;
import c195project.User;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.ComboBox;
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
public class AppointmentListController implements Initializable {

    User loggedInUser;
    
    // holds list of appointments for appointment table
    private ObservableList<Appointment> appointmentList;
    
    @FXML
    private Button addAppointmentButton;
    
    @FXML
    private Button editCustomerButton;
    
    @FXML
    private Button editAppointmentButton;
    
    @FXML
    private Label errorPrompt;
    
    @FXML
    private TableView<Appointment> appointmentTable;
    
    @FXML
    private TableColumn<Appointment, String> idColumn;
    
    @FXML
    private TableColumn<Appointment, String> customerColumn;
    
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    
    @FXML
    private TableColumn<Appointment, String> startColumn;
    
    @FXML
    private TableColumn<Appointment, String> endColumn;
    
    // initialize logged in user object passed by another controller
    @FXML
    public void setLoggedInUser(User user) {
        this.loggedInUser = new User(user.getUserId(), user.getName());
    }
    
    @FXML
    public void handleEditCustomerButton(ActionEvent event) throws IOException {
        
        // create customer object using customer belonging to the appointment selected by user
        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
        
        // check to make sure an appointment is actually selected by customer
        if(appointment != null) {
            FormValidation.setErrorMessage(errorPrompt, false);

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
            customerController.updateCustomer(appointment.getCustomer());

            // pass logged in user's info to view
            customerController.setLoggedInUser(loggedInUser);

            // pass logged in user's info to main pane
            controller.setLoggedInUser(loggedInUser);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            FormValidation.setErrorMessage(errorPrompt, true, "You must select an appointment!");
        }        
    }
    
    @FXML
    public void handleAddAppointmentButton(ActionEvent event) throws IOException {
        // reload main screen and load create add appointment view
        Stage stage;
        Parent root;
        stage=(Stage) this.addAppointmentButton.getScene().getWindow();
        
        // reload main screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/c195project/view/MainScreen.fxml"));
        root = loader.load();
        MainScreenController controller = loader.getController();
        
        // load update / add customer view in center pane
        FXMLLoader appointmentLoader = controller.loadView("/c195project/view/CreateUpdateAppointmentView.fxml");
        CreateUpdateAppointmentViewController appointmentController = appointmentLoader.getController();
        
        // tell update / add customer view that we're adding a customer
        appointmentController.createAppointment();
        
        // pass logged in user's info to view
        appointmentController.setLoggedInUser(loggedInUser);
        
        // pass logged in user's info to main pane
        controller.setLoggedInUser(loggedInUser);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void handleEditAppointmentButton(ActionEvent event) throws IOException {
        
        // get selected appointment from table
        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
        
        // throw error if no appointment selected, otherwise send to update screen
        if(appointment != null) {
            // turn off any error messages
            FormValidation.setErrorMessage(errorPrompt, false);
            
            // reload main screen and load create update customer view
            Stage stage;
            Parent root;
            stage=(Stage) this.editAppointmentButton.getScene().getWindow();

            // reload main screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/c195project/view/MainScreen.fxml"));
            root = loader.load();
            MainScreenController controller = loader.getController();

            // load update / add customer view in center pane
            FXMLLoader appointmentLoader = controller.loadView("/c195project/view/CreateUpdateAppointmentView.fxml");
            CreateUpdateAppointmentViewController appointmentController = appointmentLoader.getController();

            // tell update / add customer view that we're adding a customer
            appointmentController.updateAppointment(appointment);

            // pass logged in user's info to view
            appointmentController.setLoggedInUser(loggedInUser);

            // pass logged in user's info to main pane
            controller.setLoggedInUser(loggedInUser);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            // tell user they have to select an appointment before editing
            FormValidation.setErrorMessage(errorPrompt, true, "You must select an appointment first before you can edit!");
        }
    }
    
    @FXML
    public void handleDeleteAppointmentButton(ActionEvent event) throws IOException {
        // instantiate copy of selected appointment
        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
        
        // warn the user about deleting the appointment and do it only if 
        // they confirm
        if(appointment != null) {
            
            // clear all error dialogs
            FormValidation.setErrorMessage(errorPrompt, false);
            
            // open up confirmation dialog for user to confirm deleting appointment
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Are you sure you want to delete this appointment?");
            
            //ask user to confirm whether they want to delete
            Optional<ButtonType> result = alert.showAndWait();
            
            // if user confirms, delete appointment, otherwise do nothing
            // if there's a sql error, let user know
            if (result.get() == ButtonType.OK){
                try {
                    AppointmentDAO.deleteAppointment(appointment);
                } catch (SQLException ex) {
                    Logger.getLogger(AppointmentListController.class.getName()).log(Level.SEVERE, null, ex);
                    FormValidation.setErrorMessage(errorPrompt, true, "SQL error, appointment not deleted!");
                } finally {
                    
                    // log to file
                    FileLogger.writeLine("deleted an appointment", loggedInUser);
                    
                    // show user message that they successfully deleted the appointment
                    FormValidation.setErrorMessage(errorPrompt, false, "Appointment deleted successfully!");
                    
                    // refresh appointments list
                    appointmentList = FXCollections.observableArrayList(AppointmentDAO.getAllAppointments());
                    appointmentTable.setItems(appointmentList);
                }
            } else {
                // don't do anything
            }

        }
        else {
            // display error if user did not choose a appointment before clicking "delete"
            FormValidation.setErrorMessage(errorPrompt, true,
                    "You must select an appointment before you can delete it!");
        }        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                
        // load appointments into appointment table from database
        appointmentList = FXCollections.observableArrayList(AppointmentDAO.getAllAppointments());

        // set propertyvaluefactories for appointmentid and appointmentname of the appointment objects in the list
        idColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        
        // callback to show start localdatetime. Converts UTC time stored in database to local timezone
        startColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment,String>,ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Appointment, String> param) {
                return new SimpleStringProperty(param.getValue().getStart().toString());
            }
        });
        
        // callback to show end localdatetime. Converts UTC time stored in database to local timezone
        endColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment,String>,ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Appointment, String> param) {
                return new SimpleStringProperty(param.getValue().getEnd().toString());
            }
        });
        
        // use callback to get parameters from nested customer object
        customerColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment,String>,ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Appointment, String> param) {
                return new SimpleStringProperty(param.getValue().getCustomer().getCustomerName());
            }
        });
        
        // set contents of table
        appointmentTable.setItems(appointmentList);
        
        
        
    }
        
    
}
