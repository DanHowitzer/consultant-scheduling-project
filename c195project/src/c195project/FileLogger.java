/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 *
 * @author dhoward
 */
public class FileLogger {

    // logs a message as a line in the log file and includes the username and timestamp
    public FileLogger(String fileName, boolean append) {
    }
    
    // stores the filename
    private final static String fileName = "log.txt";
    
    // writes a line out to log.txt
    public static void writeLine(String message, User user) throws IOException {
        
        // create an instance of filewriter for the log file with append
        // set to true
        FileWriter writer = new FileWriter(fileName, true);
        
        // try with resources will try creating a bufferedwriter
        // and autmoatically close it when done
        // create buffered writer for the filewriter so we can write out
        // the line as well as a newline
        try (BufferedWriter buffered = new BufferedWriter(writer);) {
            
            // writes out the current local time plus the username plus the message
            buffered.write(user.getName() + " " + message + " at " + LocalDateTime.now().toString());
            buffered.newLine();
        }
    }
    
    // opens the log file in the user's OS
    public static void openLogFile() throws IOException {
        Desktop.getDesktop().open(new File(fileName));
    }
}
