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

    public void _insertClass() throws SQLException {

        String[][] testClass = new String[][]{
//            {"Schröder", "Gerhardt", "1944-04-07", "Massenberg-Wöhren", "1a", "2016"},
//            {"Ferkel", "Angela", "1954-06-17", "Hamburg", "1a", "2016"},
//            {"Manson", "Charles", "1934-11-12", "Cinncinnati", "1a", "2016"},
            {"Mick", "Jagger", "1943-01-04", "East Hill, Dartford", "1a", "2016"},};
        Integer idZeugnis1;
        Integer idZeugnis2;
        Integer idSchueler = 0;
        String[] zeugnis1;
        String[] zeugnis2;

        for (String[] schueler : testClass) {
            // Besser wenn idSchueler gekapselt wird und nicht zu Fuss erstellt wird

            // Schueler einfügen
            idSchueler = connector._insertSchueler();
            System.out.println(idSchueler.toString());
            connector._updateSchueler(schueler, idSchueler);
            if (idSchueler > 0) {
                // Zeugnis einfügen
                idZeugnis1 = connector._getIdZeugnis(idSchueler, 1);
                idZeugnis2 = connector._getIdZeugnis(idSchueler, 2);
                zeugnis1 = new String[]{String.valueOf(idZeugnis1), String.valueOf(idSchueler), "1", "2", "3", "4", "Keine", "Versetzt", "1", "2016"};
                zeugnis2 = new String[]{String.valueOf(idZeugnis2), String.valueOf(idSchueler), "1", "2", "3", "4", "Keine", "Versetzt", "2", "2016"};
                try {
                    connector.updateZeugnis(zeugnis1);
                    connector.updateZeugnis(zeugnis2);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
