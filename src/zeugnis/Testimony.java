/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import zeugnis.testdata.FillDatabase;

/**
 *
 * @author juergen
 */
public class Testimony {

    private final static Logger parent = Logger.getLogger("zeugnis");
    private final static Logger logger = Logger.getLogger(Testimony.class.getName());
    private ConsoleHandler consoleHandler = null;
    private DialogHandler dialogHandler = null;
    private Properties config = null;
  
    public Testimony() {

        try {
            // Default Logging

            // Avoid double Console logging output
            //Logger parent = logger.getParent();
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

        try {
            Config config = Config.getInstance();
            SingletonSQLConnector connector = SingletonSQLConnector.getInstance();
            connector.connect();

            // fill DB with initialization data
                // Logo
            //connector.runScript("/Zeugnis.sql");
            // fill Database with Testdata
            //FillDatabase fd = new FillDatabase(connector);
            //fd._insertClass();

        } catch (IOException ex) {
            logger.severe(ex.getLocalizedMessage());
        } catch (Exception ex) {
            logger.severe(ex.getLocalizedMessage());
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
