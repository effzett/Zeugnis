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

        for (String[] puple : testClass) {

            try {
                connector.insertPuple(puple);
            } catch (SQLException ex) {
                System.out.println("Schüler schon vorhanden.");
                continue;
            }

        }
        
        String[] testZeugnis = new String[8];
        String[] puple = new String[10];
        puple[0] = Integer.toString("SchröderGerhardt1944-04-0720161".hashCode());
        testZeugnis[0] = puple[0];
        puple[2] = "1";
        puple[3] = "2";
        puple[4] = "0";
        puple[5] = "0";
        puple[6] = "Keine";
        puple[7] = "Versetzt";

        try {
            connector.updateZeugnis(puple);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        puple = new String[10];
        puple[0] = Integer.toString("SchröderGerhardt1944-04-0720162".hashCode());
        testZeugnis[1] = puple[0];
        puple[2] = "1";
        puple[3] = "2";
        puple[4] = "0";
        puple[5] = "0";
        puple[6] = "Keine";
        puple[7] = "Versetzt";

        try {
            connector.updateZeugnis(puple);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        puple = new String[10];
        puple[0] = Integer.toString("FerkelAngela1954-06-1720161".hashCode());
        testZeugnis[2] = puple[0];
        puple[2] = "1";
        puple[3] = "2";
        puple[4] = "0";
        puple[5] = "0";
        puple[6] = "Keine";
        puple[7] = "Versetzt";

        try {
            connector.updateZeugnis(puple);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        puple = new String[10];
        puple[0] = Integer.toString("FerkelAngela1954-06-1720162".hashCode());
        testZeugnis[3] = puple[0];
        puple[2] = "1";
        puple[3] = "2";
        puple[4] = "0";
        puple[5] = "0";
        puple[6] = "Keine";
        puple[7] = "Versetzt";

        try {
            connector.updateZeugnis(puple);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        puple = new String[10];
        puple[0] = Integer.toString("MansonCharles1934-11-1220161".hashCode());
        testZeugnis[4] = puple[0];
        puple[2] = "1";
        puple[3] = "2";
        puple[4] = "0";
        puple[5] = "0";
        puple[6] = "Keine";
        puple[7] = "Versetzt";

        try {
            connector.updateZeugnis(puple);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        puple = new String[10];
        puple[0] = Integer.toString("MansonCharles1934-11-1220162".hashCode());
        testZeugnis[5] = puple[5];
        puple[2] = "1";
        puple[3] = "2";
        puple[4] = "0";
        puple[5] = "0";
        puple[6] = "Keine";
        puple[7] = "Versetzt";

        try {
            connector.updateZeugnis(puple);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        puple = new String[10];
        puple[0] = Integer.toString("MickJagger1943-01-0420161".hashCode());
        testZeugnis[6] = puple[0];
        puple[2] = "1";
        puple[3] = "2";
        puple[4] = "0";
        puple[5] = "0";
        puple[6] = "Keine";
        puple[7] = "Versetzt";

        try {
            connector.updateZeugnis(puple);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        puple = new String[10];
        puple[0] = Integer.toString("MickJagger1943-01-0420162".hashCode());
        testZeugnis[7] = puple[0];
        puple[2] = "1";
        puple[3] = "2";
        puple[4] = "0";
        puple[5] = "0";
        puple[6] = "Keine";
        puple[7] = "Versetzt";

        try {
            connector.updateZeugnis(puple);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
         

        // jetzt noch KRITERIUMSLISTE füllen...
        try {
            if (!connector.existKriteriumsliste()) { // prüfen ob leer 
                ArrayList<Integer> lbid = new ArrayList<Integer>(); // Lernbereiche
                lbid = connector.getID_Lernbereiche();
                for (Integer l : lbid) {
                    ArrayList<Integer> kid = new ArrayList<Integer>();  // Kriterium
                    kid = connector.getID_Kriterien(l);
                    for (int j = 1; j < 9; j++) { // sind nur 8 Beispielzeugnisse
                        for (Integer k : kid) {
                            Integer bew = ThreadLocalRandom.current().nextInt(1, 4);
                            String[] s = {testZeugnis[j - 1], Integer.toString(k), Integer.toString(bew)};
                            System.out.println("KRITERIUMSLISTE:" + s[0] + " KRITERIUM:" + s[1] + "BEWERTUNG:" + s[2]);
                            connector.insertKriteriumsliste(s);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("FEEEEEEEHLER " + ex.getMessage());
        }

    }

}
