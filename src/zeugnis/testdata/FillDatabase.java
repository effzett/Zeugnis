/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis.testdata;

import java.sql.SQLException;
import zeugnis.SQLConnector;

/**
 *
 * @author juergen
 */
public class FillDatabase {

    private SQLConnector connector = null;

    public FillDatabase(SQLConnector connector) {
        this.connector = connector;
    }

    public void insertClass() {

        String[][] testClass = new String[][]{
            {Integer.toString("SchröderGerhardt1944-04-071a".hashCode()), "Schröder", "Gerhardt", "1944-04-07", "Massenberg-Wöhren", "1a", "2016"},
            {Integer.toString("FerkelAngela1954-06-171a".hashCode()), "Ferkel", "Angela", "1954-06-17", "Hamburg", "1a", "2016"},
            {Integer.toString("MansonCharles1934-11-121a".hashCode()), "Manson", "Charles", "1934-11-12", "Cinncinnati", "1a", "2016"},
            {Integer.toString("MickJagger194308261a".hashCode()), "Mick", "Jagger", "1943-08-26", "East Hill, Dartford", "1a", "2016"},};

        for (String[] puple : testClass) {

            try {
                connector.insertPuple(puple);
            } catch (SQLException ex) {
                System.out.println("Testdaten schon vorhanden.");
                continue;
            }

        }

    }

}
