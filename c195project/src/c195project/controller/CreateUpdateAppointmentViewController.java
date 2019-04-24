/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project.controller;

import c195project.Appointment;
import c195project.AppointmentDAO;
import c195project.City;
import c195project.CityDAO;
import c195project.Country;
import c195project.CountryDAO;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dhoward
 */
public class CreateUpdateAppointmentViewController implements Initializable {

    @FXML
    ComboBox<Customer> customerList;
    
    @FXML
    ComboBox<String> appointmentTypeList;
    
    @FXML
    ComboBox<LocalTime> startTimeList;
    
    @FXML
    ComboBox<LocalTime> endTimeList;
    
    @FXML
    DatePicker datePicker = new DatePicker();
    
    @FXML
    VBox mainVBox;
    
    @FXML
    Label paneTitle;
    
    @FXML
    Label errorPrompt;
    
    @FXML
    Button submitButton;
    
    User loggedInUser;
    Boolean isAdd;
    Appointment appointment;
    
    // initialize logged in user object passed by another controller
    @FXML
    public void setLoggedInUser(User user) {
        this.loggedInUser = new User(user.getUserId(), user.getName());
    }
    
        // sets up controller to add or to update user
    @FXML
    public void createAppointment() {
        
        // set button to correct function
        submitButton.setText("Create Appointment");
        
        // set pane title to correct function
        paneTitle.setText("Create New Appointment");

        // set boolean indicating we're adding a customer
        this.isAdd = true;
    }
    
    // sets up controller to update customer
    @FXML
    public void updateAppointment(Appointment appointment) {
        
        // set isAdd to false indicating we are udpating an appointment
        this.isAdd = false;
        
        // instantiate copy of passed appointment object
        this.appointment = appointment;
        
        // set button text to update appointment
        submitButton.setText("Update Appointment");
        
        // set pane title to correct function
        paneTitle.setText("Update Appointment");
        
        // set customer field
        customerList.getSelectionModel().select(appointment.getCustomer());
        
        // set appointment type field
        appointmentTypeList.getSelectionModel().select(appointment.getType());
        
        // set datepicker to localdate found in the appointment start localdatetime
        datePicker.setValue(appointment.getStart().toLocalDate());
        
        // set start localtime
        startTimeList.setValue(appointment.getStart().toLocalTime());
        
        // set end localtime
        endTimeList.setValue(appointment.getEnd().toLocalTime());
        
        
    }
    
    @FXML
    public void handleSubmitButton(ActionEvent event) throws IOException {
        
        // create arraylist of comboboxes for required fields that must be validated
        ArrayList<ComboBox> requiredFields = new ArrayList();
        requiredFields.add(customerList);
        requiredFields.add(appointmentTypeList);
        requiredFields.add(startTimeList);
        requiredFields.add(endTimeList);
        
        ArrayList<DatePicker> requiredDatePickers = new ArrayList();
        requiredDatePickers.add(datePicker);
        
        System.out.println(FormValidation.validateDatePickers(requiredDatePickers));
        System.out.println(FormValidation.validateComboBoxes(requiredFields));
        
        
        // if fields are valid, go ahead and add or update appointment
        if(FormValidation.validateComboBoxes(requiredFields)
                && FormValidation.validateDatePickers(requiredDatePickers)) {
            
            // clear error messages
            FormValidation.setErrorMessage(errorPrompt, false);
            
            // set up appointment object to be inserted into database by grabbing
            // data from form
            
            // create localdatetime for starting and ending date and time picked by user
            LocalDateTime startTime = LocalDateTime.of(datePicker.getValue(), startTimeList.getValue());
            LocalDateTime endTime = LocalDateTime.of(datePicker.getValue(), endTimeList.getValue());
            
            // create appointment with data provided by user
            Appointment appointment = new Appointment();
            appointment.setCustomer(customerList.getValue());
            appointment.setStart(startTime);
            appointment.setEnd(endTime);
            appointment.setType(appointmentTypeList.getValue());
            
            // if isAdd is true, adds the appointment, otherwise updates it
            if(isAdd) {
                
                try {
                    AppointmentDAO.addAppointment(appointment, loggedInUser);
                    
                    // log to logger file
                    FileLogger.writeLine("added a new appointment", loggedInUser);
                    
                    FormValidation.setErrorMessage(errorPrompt, false, "Appointment added successfully!");
                } catch (SQLException ex) {
                    Logger.getLogger(CreateUpdateAppointmentViewController.class.getName()).log(Level.SEVERE, null, ex);
                    FormValidation.setErrorMessage(errorPrompt, true, "Database error!");
                }

                // clear all form fields
                FormValidation.clearAllFormFields(mainVBox);
                
            } else {
                
                // set appointmentid to appointmentid passed from list screen
                appointment.setAppointmentID(this.appointment.getAppointmentID());
                
                try {
                    AppointmentDAO.updateAppointment(appointment, loggedInUser);
                    
                    // log to file
                    FileLogger.writeLine("updated an appointment", loggedInUser);
                    
                    FormValidation.setErrorMessage(errorPrompt, false, "Appointment updated!");
                } catch (SQLException ex) {
                    Logger.getLogger(CreateUpdateAppointmentViewController.class.getName()).log(Level.SEVERE, null, ex);
                    FormValidation.setErrorMessage(errorPrompt, true, "Database error!");
                }
               
            }

        } else {
            FormValidation.setErrorMessage(errorPrompt, true, "Please fill out the required fields!");
        }
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // get araylist of customers from database
        ArrayList<Customer> customerArrayList = CustomerDAO.getAllCustomers();
        
        ArrayList<LocalTime> businessHours = TimeHandler.getBusinessHours();

        // use lambda expression to add each customer to the combobox menu item
        // it's more succinct and readable to do it via a lambda expression
        customerArrayList.forEach(c -> customerList.getItems().add(c));
        
        // load appointment type list
        appointmentTypeList.getItems().add("New Customer Consultation");
        appointmentTypeList.getItems().add("Online Advertising Consultation");
        appointmentTypeList.getItems().add("Public Relations Consultation");
        appointmentTypeList.getItems().add("SEO Consultation");
        appointmentTypeList.getItems().add("Social Media Consultation");
        appointmentTypeList.getItems().add("Website Creation Consultation");

        // listener to store picked date when user selects one
        // using lambda expression makes code more succinct and easier to read and write
        datePicker.valueProperty().addListener((obj, oldValue, newValue) -> {
            
            // if a new date is not blank, populate start and end times if appropriate
            if (newValue != null) {
                
                // if the user chooses a date in the past, throw up a warning and clear out the start and end time lists
                // otherwise continue
                if(newValue.isBefore(LocalDate.now())) {
                    FormValidation.setErrorMessage(errorPrompt, true, "You cannot book an appointment before today's date! Please choose a new date.");
                    startTimeList.getItems().clear();
                    endTimeList.getItems().clear();
                } else {
                    
                    // clear out any error messages
                    FormValidation.setErrorMessage(errorPrompt, false);
                                 
                    // get fresh list of business hours
                    ArrayList<LocalTime> filteredBusinessHours = TimeHandler.getBusinessHours();

                    // if the date picked is today, clear out any times before the current time
                    // this stops the user from booking appointments in the past
                    if(newValue.equals(LocalDate.now())) {
                        LocalTime now = LocalTime.now();

                        // lambda expression to remove any business hours before this moment
                        // the lambda expression is much more succinct than writing a traditional loop
                        filteredBusinessHours.removeIf(t -> t.isBefore(now));
                    }

                    // get list of appointments already made on the date picked
                    ArrayList<Appointment> appointmentsOnDate = AppointmentDAO.getAppointmentsByLocalDate(newValue);

                    // remove any business hours that are between the start and end times of any existing appointments
                    // this helps ensure the user can't double book appointments
                    // forEach() lambda expression makes code more succinct
                    appointmentsOnDate.forEach(a -> {
                        // remove if time is inbetween the start and end times
                        // removeIf() lambda expression is much quicker to code than traditional functions
                        filteredBusinessHours.removeIf(i -> 
                                i.isAfter(a.getStart().toLocalTime()) && i.isBefore(a.getEnd().toLocalTime())
                        );
                    });

                    // clear start and end times lists
                    startTimeList.getItems().clear();
                    endTimeList.getItems().clear();

                    // add filtered business hours
                    // using forEach() lambda expression makes code more compact, easier to write
                    filteredBusinessHours.forEach(t -> {
                        startTimeList.getItems().add(t);
                        endTimeList.getItems().add(t);
                            });

                    if(!startTimeList.getItems().isEmpty()) {
                        // remove times that don't have a free block of time afterwards
                        // because those times will not be valid for booking appointments
                        // lambda statement is used because it's more succienct than traditional loops
                        startTimeList.getItems().removeIf(t -> !startTimeList.getItems().contains(TimeHandler.nextTimeSlot(t)));
    //                    
    //                    // remove last 15 minute block of the day from the start time
    //                    startTimeList.getItems().remove(startTimeList.getItems().size() - 1);
                    }

                    if(!endTimeList.getItems().isEmpty()) {
                        // remove end times that don't have at least one block of time before it
                        // because those times will not be valid for booking appointments
                        // lambda expression is used because it's more succinct than traditional loops
                        endTimeList.getItems().removeIf(t -> !startTimeList.getItems().contains(TimeHandler.previousTimeSlot(t)));
                    }

                    // if the starttimelist is empty warn the user that the date is completely booked
                    if(startTimeList.getItems().isEmpty()) {
                        FormValidation.setErrorMessage(errorPrompt, true, "Date is fully booked! Please choose a different date.");
                    } else {
                        FormValidation.setErrorMessage(errorPrompt, false);
                    }
                }

            } else {
                // clear out the start and end time lists if no date is picked
                startTimeList.getItems().clear();
                endTimeList.getItems().clear();
            }
            
        });
        
        // lambda statement succinctly adds listener to change endtime list 
        // according to what's selected for the starttime
        startTimeList.valueProperty().addListener((ObservableValue<? extends LocalTime> obj, LocalTime oldValue, LocalTime newValue) -> {
            // if the a start time is selected, load end times that are after the start time
            if (newValue != null) {
                // clear out previous end time list
                endTimeList.getItems().clear();
                
                // add all valid business hours to endtimelist
                // forEach() lambda expression makes code much more succinct
                businessHours.forEach(t -> endTimeList.getItems().add(t));
                
                // remove any blocks of time equal to or before startime chosen by user
                // forEach() lambda expression makes code more succinct
                endTimeList.getItems().removeIf(t -> t.equals(newValue) || t.isBefore(newValue));
                
                // get list of appointments already made on the date picked
                ArrayList<Appointment> appointmentsOnDate = AppointmentDAO.getAppointmentsByLocalDate(datePicker.getValue());
                
                // if there are any existing appointments after the start time chosen by the user
                // remove all time that is after that appointment
                // this prevents the user from booking appointments that overlap with other appointments
                // lambda expression is more succinct that traditional loops
                appointmentsOnDate.forEach(a -> {
                    if (a.getStart().toLocalTime().isAfter(newValue)) {
                        endTimeList.getItems().removeIf(t -> t.isAfter(a.getStart().toLocalTime()));
                    }
                });
                
                System.out.println("Set endTimeList to times after start time chosen");
            } else {
                // clear end times if no start time is set
                endTimeList.getItems().clear();
                endTimeList.setValue(null);
            }
        });

    }    
    
}
