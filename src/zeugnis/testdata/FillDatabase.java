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

        String[][] testZeugnis = new String[][]{
            {Integer.toString("SchröderGerhardt1944-04-0720161".hashCode()),Integer.toString("SchröderGerhardt1944-04-072016".hashCode())   ,"1","1","2","0","0","Keine","Versetzt","1","2016"},
            {Integer.toString("SchröderGerhardt1944-04-0720162".hashCode()),Integer.toString("SchröderGerhardt1944-04-072016".hashCode())   ,"2","1","2","0","0","Keine","Versetzt","2","2016"},
            {Integer.toString("FerkelAngela1954-06-1720161".hashCode()),Integer.toString("FerkelAngela1954-06-172016".hashCode())           ,"3","1","2","0","0","Keine","Versetzt","1","2016"},
            {Integer.toString("FerkelAngela1954-06-1720162".hashCode()),Integer.toString("FerkelAngela1954-06-172016".hashCode())           ,"4","1","2","0","0","Keine","Versetzt","2","2016"},
            {Integer.toString("MansonCharles1934-11-1220161".hashCode()),Integer.toString("MansonCharles1934-11-122016".hashCode())         ,"5","1","2","0","0","Keine","Versetzt","1","2016"},
            {Integer.toString("MansonCharles1934-11-1220162".hashCode()),Integer.toString("MansonCharles1934-11-122016".hashCode())         ,"6","1","2","0","0","Keine","Versetzt","2","2016"},
            {Integer.toString("MickJagger1943-01-0420161".hashCode()),Integer.toString("MickJagger1943-01-042016".hashCode())               ,"7","1","2","0","0","Keine","Versetzt","1","2016"},
            {Integer.toString("MickJagger1943-01-0420162".hashCode()),Integer.toString("MickJagger1943-01-042016".hashCode())               ,"8","1","2","0","0","Keine","Versetzt","2","2016"}
        };
    
        
        int i=0;
        for (String[] puple : testClass) {
            try {
                connector.insertPuple(puple);
                connector.insertZeugnis(testZeugnis[i]); // 1. Halbjahr
                connector.insertZeugnis(testZeugnis[i+1]); // 2. Halbjahr
                i+=2;
            } catch (SQLException ex) {
                System.out.println("Testdaten schon vorhanden.");
                continue;
            }

        }

    }

}
