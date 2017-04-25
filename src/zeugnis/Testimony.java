/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private SQLConnector connector = null;

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

        // Create a default config.properties in the directory where the jarfile is placed
        // if the config.properties does not already exist.
        try {
            File jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
            String jarPath = jarFile.getParent();
            File configFile = new File(jarPath + File.separator + "config.properties");
            config = new Properties();

            if (configFile.exists()) {
                config.load(new FileReader(configFile));

            } else {
                config.setProperty("installDir", jarPath);
                config.setProperty("startDerby", "0");
                config.setProperty("derbyUser", "zeugnis");
                config.setProperty("derbyPassword", "zeugnis");
                config.store(new FileWriter(configFile),
                        "Default Config created at " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date()));
            }
            
            connector = new SQLConnector(config);
            connector.connect();
            
            // fill Database with Testdata
            FillDatabase fd = new FillDatabase(connector);
            fd.insertClass();
            

        } catch (IOException ex) {
            logger.severe(ex.getLocalizedMessage());
        } catch (Exception ex) {
            logger.severe(ex.getLocalizedMessage());
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Gui(connector).setVisible(true);
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
