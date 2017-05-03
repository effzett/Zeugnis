/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis.testdata;

import java.sql.SQLException;
import java.text.ParseException;
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

    public void insertClass() {

        String[][] testClass = new String[][]{
            {Integer.toString("SchröderGerhardt1944-04-072016".hashCode()), "Schröder", "Gerhardt", "07.04.1944", "Massenberg-Wöhren", "1a", "2016"},
            {Integer.toString("FerkelAngela1954-06-172016".hashCode()), "Ferkel", "Angela", "17.06.1954", "Hamburg", "1a", "2016"},
            {Integer.toString("MansonCharles1934-11-122016".hashCode()), "Manson", "Charles", "12.11.1934", "Cinncinnati", "1a", "2016"},
            {Integer.toString("MickJagger1943-01-042016".hashCode()), "Mick", "Jagger", "04.01.1943", "East Hill, Dartford", "1a", "2016"},};

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
