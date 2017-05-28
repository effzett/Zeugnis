/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis.testdata;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import zeugnis.SingletonSQLConnector;

/**
 *
 * @author juergen
 */
public class FillDatabase {

    private SingletonSQLConnector connector = null;

    public FillDatabase(SingletonSQLConnector connector) {
        this.connector = connector;
    }

    public void insertClass() throws SQLException {

        String[][] testClass = new String[][]{
            {Integer.toString("SchröderGerhardt1944-04-072016".hashCode()), "Schröder", "Gerhardt", "1944-04-07", "Massenberg-Wöhren", "1a", "2016"},
            {Integer.toString("FerkelAngela1954-06-172016".hashCode()), "Ferkel", "Angela", "1954-06-17", "Hamburg", "1a", "2016"},
            {Integer.toString("MansonCharles1934-11-122016".hashCode()), "Manson", "Charles", "1934-11-12", "Cinncinnati", "1a", "2016"},
            {Integer.toString("MickJagger1943-01-042016".hashCode()), "Mick", "Jagger", "1943-01-04", "East Hill, Dartford", "1a", "2016"},};
        Integer idZeugnis1;
        Integer idZeugnis2;
        Integer idSchueler;
        String[] zeugnis1;
        String[] zeugnis2;
        
        for (String[] puple : testClass) {
            // Besser wenn idSchueler gekapselt wird und nicht zu Fuss erstellt wird
            idSchueler = connector.createIdSchueler(puple[1], puple[2], puple[3], puple[6]);
            puple[0] = String.valueOf(idSchueler);

            try {
                // Schueler einfügen
                connector.insertPuple(puple);
                // Zeugnis einfügen
                idZeugnis1 = connector.getIdZeugnis(idSchueler,1);
                idZeugnis2 = connector.getIdZeugnis(idSchueler,2);
                System.out.println(String.valueOf(idZeugnis1));
                zeugnis1 = new String[]{String.valueOf(idZeugnis1),String.valueOf(idSchueler),"1","2","3","4","Keine","Versetzt","1","2016"};               
                zeugnis2 = new String[]{String.valueOf(idZeugnis2),String.valueOf(idSchueler),"1","2","3","4","Keine","Versetzt","2","2016"};               
                try {
                    connector.updateZeugnis(zeugnis1);
                    connector.updateZeugnis(zeugnis2);                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // jetzt noch KRITERIUMSLISTE füllen...
                try {
                    if (!connector.existKriteriumsliste()) { // prüfen ob leer 
                        ArrayList<Integer> lbid = new ArrayList<Integer>(); // Lernbereiche
                        lbid = connector.getID_Lernbereiche(2016,1);
                        for (Integer l : lbid) {
                            ArrayList<Integer> kid = new ArrayList<Integer>();  // Kriterium
                            kid = connector.getID_Kriterien(l);
                            for (Integer k : kid) {
                                Integer bew = ThreadLocalRandom.current().nextInt(1, 2);
                                String[] s1 = {zeugnis1[0], Integer.toString(k), Integer.toString(bew)};
                                bew = ThreadLocalRandom.current().nextInt(1, 2);
                                String[] s2 = {zeugnis2[0], Integer.toString(k), Integer.toString(bew)};
                                connector.insertKriteriumsliste(s1);
                                connector.insertKriteriumsliste(s2);                                
                            }
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println("FEEEEEEEHLER " + ex.getMessage());
                }
            } catch (SQLException ex) {
                System.out.println("Schüler schon vorhanden.");
                continue;
            }

        }        
    }

}
