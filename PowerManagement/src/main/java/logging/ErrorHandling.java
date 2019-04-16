/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logging;

import java.io.IOException;
import java.util.logging.*;
import java.util.logging.FileHandler;

/**
 *
 * @author Ville
 */
public class ErrorHandling {
    private static final Logger ERRORLOGGER = Logger.getLogger(ErrorHandling.class.getName());
    public static FileHandler fileHandler;
    
    public static void main(String[] args) {
        try {
            fileHandler = new FileHandler("./errorLog.log");
            ERRORLOGGER.addHandler(fileHandler);
             
            //Setting levels to handlers and LOGGER
            fileHandler.setLevel(Level.ALL);
            ERRORLOGGER.setLevel(Level.ALL);    
            ERRORLOGGER.config("Configuration done.");
             
        } catch (IOException e) {
            ERRORLOGGER.log(Level.SEVERE, "Error occurred in FileHandler.", e);
        }
    }
}
