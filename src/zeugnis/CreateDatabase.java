/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTransactionRollbackException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 *
 * @author u033334
 */
public class CreateDatabase {

    private final static Logger logger = Logger.getLogger(CreateDatabase.class.getName());

    public static void create(Connection connect) throws SQLException {
        String s = new String();
        StringBuffer sb = new StringBuffer();

        try {

            try (BufferedReader br = new BufferedReader(new InputStreamReader(CreateDatabase.class.getResource("/Zeugnis.sql").openStream(),"UTF-8"))) {
                while ((s = br.readLine()) != null) {
                    
                    // Kommentarzeilen ueberspringen
                    if (!s.matches("^[-/]{2,}.*")) {
                        sb.append(s);
                    }
                    
                }
            }

            // Die einzelnen Statements ueber ; separieren
            String[] inst = sb.toString().split(";");

            Statement st = connect.createStatement();

            for (int i = 0; i < inst.length; i++) {
                // Leerzeichen loeschen
                if (!inst[i].trim().equals("")) {
                    st.executeUpdate(inst[i]);
                    logger.fine(">>" + inst[i]);
                }
            }

        } catch (SQLTransactionRollbackException ex) {
            logger.fine("Datenbank existiert. Zeugnis.sql ist gestoppt.");
        } catch (Exception ex) {
            logger.fine("Exception: Zeugnis.sql ist gestoppt.");            
            ex.printStackTrace();
        }

    }

}
