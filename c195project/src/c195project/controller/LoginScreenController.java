/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project.controller;

import c195project.FileLogger;
import c195project.User;
import c195project.UserDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * @author dhoward
 */
public class LoginScreenController implements Initializable {
    
    ResourceBundle systemPrompts;
    
    User loggedInUser;
    
    @FXML
    private Label passwordLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorPrompt;
    @FXML
    private Button submitButton;
    @FXML
    private Button exitButton;
    
    // exits the program
    @FXML
    protected void handleExitButton(ActionEvent event) {
        System.exit(0);
    }
    
    // sends user to main screen if their username and password match what's in the database
    @FXML
    protected void login(ActionEvent event) throws IOException, SQLException {
        
        // get userid from database if userid and password match what's in the database
        // if not, userID will be null
        String userID = UserDAO.login(usernameField.getText(), passwordField.getText());
        
        if(userID != null) {
            
            // set logedInUser user object to match the user that's logged in
            this.loggedInUser = new User(userID, usernameField.getText());
            
            FileLogger.writeLine("logged in successfully", loggedInUser);
            
            // make sure error message does not show
            errorPrompt.setVisible(false);
            
            // creates new stage and root to load the main screen
            Stage stage;
            Parent root;
            stage=(Stage) submitButton.getScene().getWindow();
            
            // load main screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/c195project/view/MainScreen.fxml"));
            

            
            // load bilingual language resources
            loader.setResources(systemPrompts);
            
            root = loader.load();
            
            // pass logged in user's info to next screen
            MainScreenController mainScreen = loader.getController();
            mainScreen.setLoggedInUser(this.loggedInUser);
            
            // sets and shows the scene to the user
            Scene scene = new Scene(root);
            
            scene.getStylesheets().add(getClass().getResource("/c195project/stylesheet/c195project.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Scheduler");
            stage.show(); 

        } else {
            errorPrompt.setVisible(true);
        }
    }
    
    // initialize logged in user object passed by another controller
    @FXML
    public void setLoggedInUser(User user) {
        this.loggedInUser = new User(user.getUserId(),user.getName());
    }
    
    // initialize text to correct language for user's region
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.systemPrompts = rb;
        
        // sets localized text for username and password labels, button
        usernameLabel.setText(systemPrompts.getString("usernameLabel"));
        passwordLabel.setText(systemPrompts.getString("passwordLabel"));
        submitButton.setText(systemPrompts.getString("submitButton"));
        errorPrompt.setText(systemPrompts.getString("errorPrompt"));
        exitButton.setText(systemPrompts.getString("exitButton"));
        errorPrompt.setVisible(false);
        errorPrompt.setId("error-prompt");
    }    
    
}
