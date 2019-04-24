/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project.controller;

import c195project.Appointment;
import c195project.AppointmentDAO;
import c195project.C195project;
import c195project.FileLogger;
import c195project.User;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dhoward
 */
public class MainScreenController implements Initializable {

    ResourceBundle systemPrompts;
    User loggedInUser;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private BorderPane mainPane;
    
    // open the log text file for the user
    @FXML
    protected void handleOpenLogFileButton(ActionEvent event) throws IOException {
        FileLogger.openLogFile();
    }
    
    @FXML
    protected void handleScheduleReport(ActionEvent event) {
        // use loadView to load appointment list
        FXMLLoader loader = loadView("/c195project/view/ReportConsultantSchedule.fxml");
        
        // pass logged in user to next controller
        ReportConsultantScheduleController controller = loader.getController();
        controller.setLoggedInUser(loggedInUser);         
    }
    
    // open appointment type by month report
    @FXML
    protected void handleAppointmentTypeReport(ActionEvent event) {
        // use loadView to load appointment list
        FXMLLoader loader = loadView("/c195project/view/ReportAppointmentsPerMonth.fxml");
        
        // pass logged in user to next controller
        ReportAppointmentsPerMonthController controller = loader.getController();
        controller.setLoggedInUser(loggedInUser);           
    }
    
    
    @FXML
    protected void handleCalendarButton(ActionEvent event) {
        // use loadView to load appointment list
        FXMLLoader loader = loadView("/c195project/view/CalendarView.fxml");
        
        // pass logged in user to next controller
        CalendarViewController controller = loader.getController();
        controller.setLoggedInUser(loggedInUser);      
    }
    
    @FXML
    protected void handleAppointmentsButton(ActionEvent event) {
        // use loadView to load appointment list
        FXMLLoader loader = loadView("/c195project/view/AppointmentListView.fxml");
        
        // pass logged in user to next controller
        AppointmentListController controller = loader.getController();
        controller.setLoggedInUser(loggedInUser);        
    }
    
    @FXML
    protected void handleCustomersButton(ActionEvent event) {
        // use loadView to load createUpdateCustomerView
        FXMLLoader loader = loadView("/c195project/view/CustomerListView.fxml");
        
        // pass logged in user to next controller
        CustomerListViewController controller = loader.getController();
        controller.setLoggedInUser(loggedInUser);
    }
    
    @FXML
    protected void handleConsultantListing(ActionEvent event) {
        // use loadView to load createUpdateCustomerView
        FXMLLoader loader = loadView("/c195project/view/ReportConsultantListing.fxml");
        
        // pass logged in user to next controller
        ReportConsultantListingController controller = loader.getController();
        controller.setLoggedInUser(loggedInUser);        
    }
    
    /**
    * Initializes the controller class.
    */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // sets up language files bundle to pass back to the login screen later if user navigates to it
        this.systemPrompts = rb;
        
        
        ArrayList<Appointment> appointments;
        
        // grab current time and date
        LocalDateTime now = LocalDateTime.now();
        
        // grab list of all appointments booked for today
        appointments = AppointmentDAO.getAppointmentsByLocalDate(now.toLocalDate());
        
        // remove all appointments not within the next 15 minutes
        // lambda expression is more succinct than traditional loops
        appointments.removeIf(a -> !(a.getStart().isAfter(now) && a.getStart().isBefore(now.plusMinutes(15))));
        
        // if there are appointments within the next 15 minutes, alert the user
        if(!appointments.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("15 Minute Appointment Warning");
            alert.setHeaderText("Appointment in the next 15 minutes");
            alert.setContentText("There is an appointment scheduled in the next 15 minutes!");
            
            //ask user to confirm whether they want to delete
            Optional<ButtonType> result = alert.showAndWait();
        }    
    }    
    
    
    // initialize logged in user object passed by another controller
    @FXML
    public void setLoggedInUser(User user) {
        this.loggedInUser = new User(user.getUserId(), user.getName());
    }
    
    // loads FXML view into center of border pane on main screen
    public FXMLLoader loadView(String viewFile) {
        URL url = getClass().getResource(viewFile);
        FXMLLoader loader = new FXMLLoader(url);
        
        try {
            mainPane.setCenter(loader.load());
            return loader;
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
