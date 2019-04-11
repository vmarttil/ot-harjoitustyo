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
public class errorHandling {
    private static final Logger errorLogger = Logger.getLogger(errorHandling.class.getName()) ;
    public static FileHandler fileHandler;
    
    public static void main(String[] args) {
        try{
            fileHandler = new FileHandler("./errorLog.log");
            errorLogger.addHandler(fileHandler);
             
            //Setting levels to handlers and LOGGER
            fileHandler.setLevel(Level.ALL);
            errorLogger.setLevel(Level.ALL);    
            errorLogger.config("Configuration done.");
             
        }catch(IOException e){
            errorLogger.log(Level.SEVERE, "Error occurred in FileHandler.", e);
        }
    }
}
