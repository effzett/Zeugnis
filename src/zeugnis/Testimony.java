/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juergen
 */
public class Testimony {
    
    private final static Logger parent = Logger.getLogger("zeugnis");
    private final static Logger logger = Logger.getLogger(Testimony.class.getName());
    private ConsoleHandler consoleHandler = null;
    private DialogHandler dialogHandler = null;
    
    public Testimony() {
        
        try {
            // Default Logging

            // Avoid double Console logging output
            Logger parent = logger.getParent();
            for (Handler handler : Logger.getLogger("").getHandlers()) {
                Logger.getLogger("").removeHandler(handler);
            }

            parent.setLevel(Level.ALL);
            consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            parent.addHandler(consoleHandler);
            dialogHandler = new DialogHandler();
            dialogHandler.setLevel(Level.INFO);
            parent.addHandler(dialogHandler);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Gui().setVisible(true);
            }
        });
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Testimony();
    }
    
}
