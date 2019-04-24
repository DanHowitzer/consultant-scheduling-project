/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project.controller;

import c195project.Appointment;
import c195project.AppointmentDAO;
import c195project.FormValidation;
import c195project.TimeHandler;
import c195project.User;
import c195project.WeekOfMonth;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;


/**
 * FXML Controller class
 *
 * @author dhoward
 */
public class CalendarViewController implements Initializable {
    
    User loggedInUser;
    
    // holds list of appointments for appointment table
    private ObservableList<Appointment> appointmentList;
    
    @FXML
    private ComboBox<Integer> yearCombo;
    
    @FXML
    private ComboBox<Month> monthCombo;
    
    @FXML
    private ComboBox<WeekOfMonth> weekCombo;
    
    @FXML
    private Label errorPrompt;
    
    @FXML
    private Label weekLabel;
    
    @FXML
    private RadioButton weeklyRadio;
    
    @FXML
    private RadioButton monthlyRadio;
    
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
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // set default value in table when no appointments are found
        appointmentTable.setPlaceholder(new Label("No appointments in selected time period. Try selecting a different year / month / week."));
        
        // get the local datetime of the user
        LocalDateTime now = LocalDateTime.now();
        
        // populate list of years (5 years into the past, 10 years into the future)
        Integer year = now.getYear();
        for(int x = year - 5; x <= year + 10; x++) {
            yearCombo.getItems().add(x);
        }
        
        // add this year to the year combobox and select it
        yearCombo.getSelectionModel().select(year);
        
        // add the months to the combobox
        ArrayList<Month> monthList = TimeHandler.getMonths();
        
        // lambda expression makes it easy and succinct to add the months to the combobox
        monthList.forEach(m -> monthCombo.getItems().add(m));
        
        // select this month by default
        monthCombo.getSelectionModel().select(now.getMonth());
        
        // populate weeks this month
        ArrayList<WeekOfMonth> weeks = WeekOfMonth.getWeeksOfMonth(now.toLocalDate());
        
        // using forEach() lambda expression makes the code more succinct and easier to write
        weeks.forEach(w -> weekCombo.getItems().add(w));
        
        // make week dropdown invisible by default
        weekCombo.setVisible(false);
        weekLabel.setVisible(false);

        // load appointments from this month by default
        appointmentList = FXCollections.observableArrayList(AppointmentDAO.getAppointmentsByMonth(now.toLocalDate()));

        // set propertyvaluefactories for appointmentid and appointmentname of the appointment objects in the list
        idColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        
        // callback to show start localdatetime. Converts UTC time stored in database to local timezone
        startColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment,String>,ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> param) {
                return new SimpleStringProperty(param.getValue().getStart().toString());
            }
        });
        
        // callback to show end localdatetime. Converts UTC time stored in database to local timezone
        endColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment,String>,ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> param) {
                return new SimpleStringProperty(param.getValue().getEnd().toString());
            }
        });
        
        // use callback to get parameters from nested customer object
        customerColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Appointment,String>,ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Appointment, String> param) {
                return new SimpleStringProperty(param.getValue().getCustomer().getCustomerName());
            }
        });
        
        // set contents of table
        appointmentTable.setItems(appointmentList);
        
        // add a listener to the year combobox to get appointments based on what the user chooses
        // using lambda expression makes code shorter and more readable
        yearCombo.valueProperty().addListener((ObservableValue<? extends Integer> obj, Integer oldvalue, Integer newvalue) -> {
            
            // remove any error prompts
            FormValidation.setErrorMessage(errorPrompt, false);
            
            if(newvalue != null) {
                
                // if a new value was selected and there's a month selected, query the database for a new set of appointments
                if(newvalue != oldvalue && monthCombo.getValue() != null) {
                    
                    // remove any error prompts
                    FormValidation.setErrorMessage(errorPrompt, false);
                    
                    // set the observablelist for appointments to query results from database that match the year and month selected by user
                    appointmentList = FXCollections.observableArrayList(AppointmentDAO.getAppointmentsByMonth(LocalDate.of(newvalue, monthCombo.getValue(), 15)));
                    
                    appointmentTable.setItems(appointmentList);
                    
                    // clear and populate weeks of the selected month into weekCombo box
                    weekCombo.getItems().clear();
                    
                    // using forEach() lambda expression makes code shorter and easier to write
                    WeekOfMonth.getWeeksOfMonth(LocalDate.of(newvalue, monthCombo.getValue(), 15)).forEach(w -> weekCombo.getItems().add(w));
                    
                }
                
            } else {
                
                // show user error message if no year is selected
                FormValidation.setErrorMessage(errorPrompt, true, "You must select a year");
                
                // clear the weeks
                weekCombo.getSelectionModel().clearSelection();
                weekCombo.getItems().clear();
                
            }
        });
        
        // detect changes in the month dropdown
        // using lambda expression makes code more succinct and easier to read
        monthCombo.valueProperty().addListener((ObservableValue<? extends Month> obj, Month oldvalue, Month newvalue) -> {
        
            // remove any error prompts
            FormValidation.setErrorMessage(errorPrompt, false);
            
            if(newvalue != null) {
                
                // if the new value is actually new and the year is filled out
                if(newvalue != oldvalue && yearCombo.getValue() != null) {
                    
                    // if the user has week selection active, clear the week combobox and the appointment list
                    if(weeklyRadio.isSelected()) {
                        
                        // clear and populate weeks of the selected month into weekCombo box
                        weekCombo.getSelectionModel().clearSelection();
                        weekCombo.getItems().clear();
                        
                        // using forEach() lambda expression makes code easier to write and more compact
                        WeekOfMonth.getWeeksOfMonth(LocalDate.of(yearCombo.getValue(), newvalue, 15)).forEach(w -> weekCombo.getItems().add(w));
                        
                        // clear the list of appointments
                        appointmentList.clear();
                    } else {
                        // get appointments for the selected year and month and set table
                        appointmentList = FXCollections.observableArrayList(AppointmentDAO.getAppointmentsByMonth(LocalDate.of(yearCombo.getValue(), newvalue, 15)));
                        appointmentTable.setItems(appointmentList);
                    }
                   
                }
                
            } else {
                
                // remove any error prompts
                FormValidation.setErrorMessage(errorPrompt, true, "You must select a year and month");
                
            }
            
        });
        
        // detect whether user selects a week
        // using lambda expression makes code easier to read and write
        weekCombo.valueProperty().addListener((ObservableValue<? extends WeekOfMonth> obj, WeekOfMonth oldvalue, WeekOfMonth newvalue) -> {
       
            // remove any error prompts
            FormValidation.setErrorMessage(errorPrompt, false);
            
            if(newvalue != null && weeklyRadio.isSelected()) {
                // if the user selects a week then set the radio button to select appointments by week
                weeklyRadio.setSelected(true);

                // get appointments for the selected week
                appointmentList = FXCollections.observableArrayList(AppointmentDAO.getAppointmentsByWeek(newvalue));

                appointmentTable.setItems(appointmentList);                
                
            } else if(newvalue == null && weeklyRadio.isSelected()) {
                
                // show user error prompt
                FormValidation.setErrorMessage(errorPrompt, true, "You must select a week");
            }
        
        });
        
        // detect whether user selects calendar by month
        // using lambda expression makes code easier to read and write because it's shorter
        weeklyRadio.selectedProperty().addListener((ObservableValue<? extends Boolean> obj, Boolean oldvalue, Boolean newvalue) -> {
        
            // if week radio button is enabled, make the week combobox visible, otherwise make it invisible
            // also if the month and year are selected, display the appointments for the month and year
            if(newvalue) {
                
                // make the week comobobox and label visible so the user can select the week
                weekCombo.setVisible(true);
                weekLabel.setVisible(true);
                
                // if there is a month and year selected, populate the week list
                if(weekCombo.getValue() == null) {
                    
                    // show user error prompt
                    FormValidation.setErrorMessage(errorPrompt, true, "You must select a week");

                    // clear the appointment list since no week is selected
                    appointmentList.clear();
                    
                } else {
                    
                    // remove error messages
                    FormValidation.setErrorMessage(errorPrompt, false);
                }
                
                if(yearCombo.getValue() != null && monthCombo.getValue() != null) {
                    
                    // clear and populate weeks of the selected month into weekCombo box
                    weekCombo.getSelectionModel().clearSelection();
                    weekCombo.getItems().clear();
                    
                    // using .forEach() lambda expression makes code more succinct and easier to write
                    WeekOfMonth.getWeeksOfMonth(LocalDate.of(yearCombo.getValue(), monthCombo.getValue(), 15)).forEach(w -> weekCombo.getItems().add(w));
                    
                }
                
            } else {
                
                // make the week combobox and label invisible
                weekCombo.setVisible(false);
                weekLabel.setVisible(false);
                
                // clear out items in week combobox
                weekCombo.getItems().clear();
                
                // if a month and year is selected, display the appointments for that month and year
                if(monthCombo.getValue() != null && yearCombo.getValue() != null) {
                    
                    // set radio button to select by month
                    monthlyRadio.setSelected(true);
                    
                    // get appointments for the selected year and month
                    appointmentList = FXCollections.observableArrayList(AppointmentDAO.getAppointmentsByMonth(LocalDate.of(yearCombo.getValue(), monthCombo.getValue(), 15)));
                    
                    appointmentTable.setItems(appointmentList);
                    
                } else {
                    // if there's no month or year selected, clear the list
                    appointmentList.clear();
                }
            }
            
        
        });
        
    }    
    
}
