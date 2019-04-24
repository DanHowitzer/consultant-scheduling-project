/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.layout.Pane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author dhoward
 */
public class FormValidation {
    
    // returns true if none of the date pickers are blank
    public static boolean validateDatePickers(ArrayList<DatePicker> datePickers) {
        
        // marks any invalid datepickers with error class id
        // lambda expression makes loop easy to read and write
        datePickers.forEach(d -> {
            if(d.getValue() == null) {
                d.setId("error");
            } else {
                d.setId("");
            }
        });
        
        // using the stream().anyMatch() lambda function saves time and makes the statement more succinct
        return !datePickers.stream().anyMatch(d -> d.getValue() == null);
    }
    
    // returns true if none of the text fields in the arraylist are blank
    public static boolean validateTextFields(ArrayList<TextField> fields) {
        
        // sets all blank fields with error class id
        // lambda expression succinctly loops through all fields and makes code easier to write and read
        fields.forEach(f -> {
            if(f.getText().equals("")) {
                f.setId("error");
            } else {
                f.setId("");
            }
        });
        
        // using stream().anyMatch() lambda function makes the code more succinct and easier to read and write
        return !fields.stream().anyMatch(f -> f.getText().equals(""));
        
    }
    
    // returns true if the field is a five digit number or a five digit number followed by
    // a hyphen and a four digit number
    public static boolean validateZipCode(TextField field) {
        
        // regular expression pattern for US zip code (5 digit number or 5 digit number - 4 digit number) 
        Pattern zipPattern = Pattern.compile("^[0-9]{5}(?:-[0-9]{4})?$");
        
        // match passed string to zip code pattern
        Matcher zipMatcher = zipPattern.matcher(field.getText());
        
        // put error css class onto field if invalid, otherwise remove error style
        if(zipMatcher.matches()) {
            field.setId("");
        } else {
            field.setId("error");            
        }

        
        // return true if matches or false if it doesn't match
        return zipMatcher.matches();
        
    }
    
  
    // returns true if the field is a valid north american phone number
    // can be in (000) 000 0000, 0000000000, 000-000-0000, 000.000.0000, 000 000 0000 formats
    public static boolean validatePhoneNumber(TextField field) {
        
        // regular expression pattern for North American 10 digit phone number
        // can be in (000) 000 0000, 0000000000, 000-000-0000, 000.000.0000, 000 000 0000 formats
        Pattern phonePattern = Pattern.compile("^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$");
        
        // match passed string to zip code pattern
        Matcher phoneMatcher = phonePattern.matcher(field.getText());
        
        // put error css class onto field if invalid, otherwise remove error style
        if(phoneMatcher.matches()) {
            field.setId("");
        } else {
            field.setId("error");            
        }

        
        // return true if matches or false if it doesn't match
        return phoneMatcher.matches();
        
    }
    
    
    // returns true if none of the combobox selections are empty
    public static boolean validateComboBoxes(ArrayList<ComboBox> comboboxes) {
        
        // put error css class onto field if invalid, otherwise remove error style
        // lambda expression makes code succinct and easier to write
        comboboxes.forEach(c -> {
            if(c.getSelectionModel().isEmpty()) {
                c.setId("error");
            } else {
                c.setId("");
            }
        });
        
        // using stream().anyMatch() lambda function makes the code more succinct and easier to read and write
        return !comboboxes.stream().anyMatch(c -> c.getSelectionModel().isEmpty());
        
    }
    
    
    
    // makes error label visible to customer and sets a default "Error!" message
    // if errorStatus is false, makes the error message invisible
    public static void setErrorMessage(Label errorLabel, Boolean errorStatus) {
        
        // sets CSS id to error-prompt for proper styling
        errorLabel.setId("error-prompt");
        
        // if the errorStatus is true, make the error message visible with the text "Error!"
        // otherwise make the error message invisible
        if(errorStatus) {
            errorLabel.setVisible(true);
            errorLabel.setText("Error!");
        } else {
            errorLabel.setVisible(false);
            errorLabel.setText("");
        }
    }
    
    // makes error label visible to customer and accepts an errormessage string to set
    public static void setErrorMessage(Label errorLabel, Boolean errorStatus, String userMessage) {
        
        // set to prompt provided and make the errormessage visible
        errorLabel.setText(userMessage);
        errorLabel.setVisible(true);
        
        // if the errorStatus is true, set css ID to error-prompt so it's red
        // otherwise make it success-prompt green
        if(errorStatus) {
            // sets CSS id to error-prompt for proper styling
            errorLabel.setId("error-prompt");
        } else {
            // sets CSS id to success-prompt for proper styling
            errorLabel.setId("success-prompt");
        }
    }
    
    
    // clear all form fields
    public static void clearAllFormFields(Pane pane) {
        
        // clear each type of form field
        // using .forEach() lambda function makes code more succinct and easier to write
        pane.getChildren().forEach(c -> {
            
            if(c instanceof TextField) {
                // clear text fields
                ((TextField)c).setText("");
            } else if (c instanceof ComboBox) {
                // clear combobox selection
                ((ComboBox)c).valueProperty().set(null);
            } else if (c instanceof DatePicker) {
                // clear datepicker
                ((DatePicker)c).valueProperty().set(null);
            }
            
        });
    }
}
