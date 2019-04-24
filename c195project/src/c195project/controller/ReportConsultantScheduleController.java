/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project.controller;

import c195project.AppointmentDAO;
import c195project.FileLogger;
import c195project.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author dhoward
 */
public class ReportConsultantScheduleController implements Initializable {
    
    User loggedInUser;
    
    @FXML
    TextArea reportText = new TextArea();
    
    
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
        reportText.setText(AppointmentDAO.getSchedules());
        reportText.setEditable(false);
        
        try {
            FileLogger.writeLine("ran consultant schedule report", loggedInUser);
        } catch (IOException ex) {
            Logger.getLogger(ReportConsultantScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
