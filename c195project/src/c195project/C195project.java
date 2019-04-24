/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project;

import c195project.controller.LoginScreenController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author dhoward
 */
public class C195project extends Application {
    
    static Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception {
        // get user's locale
        Locale myLocale = Locale.getDefault();
        
        // uncomment code below to set locale to Taiwan and language to Traditional Chinese
//        myLocale = new Locale("zh", "TW");
        
        System.out.println(myLocale.toString());

        // load resource bundle for prompts in multiple languages for the login screen
        ResourceBundle systemPrompts = ResourceBundle.getBundle("lang/lang",myLocale);
        
        // load javaFX GUI
        try {
            // load FXML for login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/LoginScreen.fxml"));
            
            // load bilingual language resources
            loader.setResources(systemPrompts);
            
            // load view into root
            Parent root = loader.load();

            // create new scene
            Scene scene = new Scene(root, 300, 275);

            // set scene title
            stage.setTitle("Scheduler Login");
            
            // set scene for login screen on stage
            stage.setScene(scene);
            
            // load stylesheet for scene
            scene.getStylesheets().add
                (C195project.class.getResource("stylesheet/c195project.css").toExternalForm());
            
            // show the stage to the user
            stage.show();
            
        } catch (IOException e) {
            
            // exception handling if javaFX does not load correctly
            throw new RuntimeException("Error loading javaFX GUI", e);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
