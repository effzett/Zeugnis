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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author u033334
 */
public class Config extends Properties {

    private final static Logger logger = Logger.getLogger(Config.class.getName());

    private Config() {
        File jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        String jarPath = jarFile.getParent();
        File configFile = new File(jarPath + File.separator + "config.properties");

        try {
            if (configFile.exists()) {
                load(new FileReader(configFile));

            } else {
                setProperty("installDir", jarPath);
                setProperty("startDerby", "1");
                setProperty("derbyUser", "zeugnis");
                setProperty("derbyPassword", "zeugnis");
                setProperty("classes", "1a,1b,2a,2b,3a,3b,4a,4b");
                setProperty("symbol1", "9");
                setProperty("symbol2", "6");
                setProperty("sName", "Mustermann");
                setProperty("sVorname", "Maxi");

                store(new FileWriter(configFile),
                        "Default Config created at " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date()));
            }

        } catch (IOException ex) {
            logger.severe(ex.getLocalizedMessage());
        }
    }

    public static Config getInstance() {
        return ConfigHolder.INSTANCE;
    }

    private static class ConfigHolder {

        private static final Config INSTANCE = new Config();
    }
    
    public void storeProperties(){
        File jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        String jarPath = jarFile.getParent();
        File configFile = new File(jarPath + File.separator + "config.properties");
        setProperty("installDir", jarPath);
        setProperty("startDerby", "1");
        setProperty("derbyUser", "zeugnis");
        setProperty("derbyPassword", "zeugnis");
        setProperty("classes", "1a,1b,2a,2b,3a,3b,4a,4b");
        setProperty("symbol1", String.valueOf(ZeugnisPDF.getSymbol1()));
        setProperty("symbol2", String.valueOf(ZeugnisPDF.getSymbol2()));
        setProperty("sName", this.getProperty("sName", "Mustermann"));
        setProperty("sVorname", this.getProperty("sVorname", "Maxi"));
        try {
            store(new FileWriter(configFile),
                    "Modificated Config created at " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date()));
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
