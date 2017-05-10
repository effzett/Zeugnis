/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.derby.drda.NetworkServerControl;

/**
 *
 * @author u033334
 */
public class SingletonSQLConnector {

    private final static Logger logger = Logger.getLogger(SingletonSQLConnector.class.getName());
    private static Config config = null;
    private Connection con = null;
    private NetworkServerControl server = null;
    private boolean foreignDerby = false;
    SimpleDateFormat sdf = null;

    private SingletonSQLConnector() {
        config = Config.getInstance();
        sdf = new SimpleDateFormat("dd.MM.yyyy");
    }

    public static SingletonSQLConnector getInstance() {
        return SingeltonSQLConnectorHolder.INSTANCE;
    }

    private static class SingeltonSQLConnectorHolder {
        private static final SingletonSQLConnector INSTANCE = new SingletonSQLConnector();
    }

    /**
     * If startderby == 1 start an own derby instance if port 1527 is not in
     * use. If startderby == 0 Use an runnin dery server at port 1527.
     *
     * @throws Exception
     */
    public void connect() throws Exception {
        StringWriter sw = new StringWriter();

        if (config.getProperty("startDerby").equals("1")) {
		// Start Derby at localhost on port 1527

            // Check if port 1527 is already in use. (In case of a crash the Derby Server will still running on another process)
            Socket socket = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("localhost", 1527);

            try {
                socket.connect(sockaddr);
            } catch (IOException ex) {
                foreignDerby = false;
                server = new NetworkServerControl();
                server.start(new PrintWriter(sw));
                System.out.println(sw.toString());
            }

            socket.close();
        }

        // Connect to the server
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        con = DriverManager.getConnection("jdbc:derby://localhost:1527/Zeugnis",
                config.getProperty("derbyUser"),
                config.getProperty("derbyPassword"));
    }

    /**
     * der Primary Key des Schuelers wird erzeugt aus dem Hashcode aus
     * NameVornameGebDatumSchuljahr. Das Datum im Format yyyy-MM-dd.
     *
     * @param values Die Werte zum Anlegen eines Schuelers in der Reihen folge
     * sder Spalten der Tabelle SCHUELER
     * @throws SQLException
     */
    public void insertPuple(String[] values) throws SQLException {

        try (Statement statement = con.createStatement()) {
            
            String sql = "insert into SCHUELER values(" + values[0]
                    + ",'" + values[1]
                    + "', '" + values[2]
                    + "', '" + values[3]
                    + "', '" + values[4]
                    + "', '" + values[5]
                    + "', " + values[6] + ")";

            logger.fine(sql);
            statement.executeUpdate(sql);
        }

    }
    
    public void insertZeugnis(String[] values) throws SQLException{
        
        try (Statement statement = con.createStatement()) {
            
            String sql = "insert into ZEUGNIS values(" + values[0]
                + "," + values[1]
                + "," + values[2]
                + "," + values[3]
                + "," + values[4]
                + "," + values[5]
                + ",'" + values[6]
                + "','" + values[7]
                + "'," + values[8]
                + "," + values[9] + ")";
            logger.fine(sql);
            statement.executeUpdate(sql);
        }
    }

    public Boolean existKriteriumsliste() throws SQLException{
        try (Statement statement = con.createStatement()) {
            String sql = "select * from KRITERIUMSLISTE";
            logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            return set.next();
        }
    }
    
    public void insertKriteriumsliste(String[] values) throws SQLException{
        try (Statement statement = con.createStatement()) {
            
            String sql = "insert into KRITERIUMSLISTE values(" + 
                            values[0]
                    + "," + values[1]
                    + "," + values[2] 
                    + ")";
            logger.fine(sql);
            statement.executeUpdate(sql);
        }
 
    }
    
    
    /**
     * Update eines Schülers. Es werden alle Werte einer Zeile upgedatet.
     *
     * @param values Eine String[] Array mit der Reihenfolge den Werten aller
     * Spalten der Tabelle SCHUELER
     * @param idSchueler Die Id des zu loeschenden Datensatzes.
     * @throws SQLException
     */
    public void updatePuple(String[] values, String idSchueler) throws SQLException {
        

        try (Statement statement = con.createStatement()) {
            String sql = "delete from SCHUELER where ID_Schueler = " + idSchueler;
            logger.fine(sql);
            statement.executeUpdate(sql);
            insertPuple(values);
        }

    }
    
     /**
     * Löschen eines Schülers.
     *
     * @param idschueler Die Id des zu loeschenden Datensatzes.
     * @throws SQLException
     */
    public void deletePuple(String idSchueler) throws SQLException {
        

        try (Statement statement = con.createStatement()) {
            String sql = "delete from SCHUELER where ID_Schueler = " + idSchueler;
            logger.fine(sql);
            statement.executeUpdate(sql);
        }

    }

    /**
     * Gibt die Daten aller Schueler eines Jahrgangs und einer Klasse zurueck.
     *
     * @param sYear Das Schuljahr
     * @param sClass Die Klasse
     * @return Array mit den Daten der Schueler
     * @throws SQLException
     */
    public ArrayList[] fetchPuples(int sYear, String sClass) throws SQLException {
        ArrayList<ArrayList> result = new ArrayList<>();

        try (Statement statement = con.createStatement()) {

            String sql = "select ID_SCHUELER, NAME, VORNAME, GEBDATUM, GEBORT from SCHUELER where KLASSE = '" + sClass + "' and SCHULJAHR = " + sYear;
            logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                ArrayList puple = new ArrayList();
                puple.add(set.getString(1));
                puple.add(set.getString(2));
                puple.add(set.getString(3));
                puple.add(sdf.format(set.getDate(4)));
                puple.add(set.getString(5));
                result.add(puple);
            }

        }

        return result.toArray(new ArrayList[0]);
    }

    /**
     * Fuellt die übergebene JTable direkt mit Daten aus der Datenbank.
     *
     * @param table Die Tabelle mit der Schulklasse.
     * @param sYear Das Schuljahr
     * @param sClass Die Klasse
     * @throws SQLException
     */
    public void fillClassTable(JTable table, int sYear, String sClass) throws SQLException {

        try (Statement statement = con.createStatement()) {
            String sql = "select ID_SCHUELER, NAME, VORNAME, GEBDATUM, GEBORT from SCHUELER where KLASSE = '" + sClass + "' and SCHULJAHR = " + sYear + " order by NAME asc";
            logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            while (set.next()) {
                Object[] row = new Object[7];
                row[0] = set.getString(1);
                row[1] = set.getString(2);
                row[2] = set.getString(3);
                row[3] = sdf.format(set.getDate(4));
                row[4] = set.getString(5);
                model.addRow(row);
            }

        }

    }
 
    /**
     * Prueft auf die Existenz eines Schuelers ueber den PrimaryKey.
     *
     * @param idSchueler
     * @return
     * @throws SQLException
     */
    public boolean pupleExist(int idSchueler) throws SQLException {

        try (Statement statement = con.createStatement()) {
            String sql = "select * from SCHUELER where ID_SCHUELER = " + idSchueler;
            logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            return set.next();
        }
    }

    /**
     * Gibt alle bisher verwendeten Schuljahre aufsteigend sortiert in einem
     * Array zurueck. Die Schuljahre werden gleich in das Format yyyy/yy
     * formatiert.
     *
     * @return Array mit den verfuegbaren Schuljahren. Aufsteigen sortiert.
     * @throws SQLException
     */
    public String[] fetchSYears() throws SQLException {
        ArrayList<String> result = new ArrayList<>();

        try (Statement statement = con.createStatement()) {

            String sql = "select distinct SCHULJAHR from SCHUELER order by SCHULJAHR asc";
            logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                int year = set.getInt(1);
                result.add(Integer.toString(year) + "/" + Integer.toString(year += 1).substring(2));
            }

        }

        return result.toArray(new String[0]);
    }

    
    public String getSchuelerName(int idSCHUELER) throws SQLException{
        String name="Name";
        try (Statement statement = con.createStatement()) {
            String sql = "select NAME from SCHUELER where ID_SCHUELER = " + idSCHUELER ;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                name = set.getString(1);
            }
        }
        return name;
    }
    
    public String getSchuelerVorname(int idSCHUELER) throws SQLException{
        String vorname="Vorname";
        try (Statement statement = con.createStatement()) {
            String sql = "select VORNAME from SCHUELER where ID_SCHUELER = " + idSCHUELER ;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                vorname = set.getString(1);
            }
        }
        return vorname;
    }

    public String getSchuelerGebDatum(int idSCHUELER) throws SQLException{
        String gebdatum="";
        try (Statement statement = con.createStatement()) {
            String sql = "select GEBDATUM from SCHUELER where ID_SCHUELER = " + idSCHUELER ;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                gebdatum = set.getString(1);
            }
        }
        return gebdatum;
    }
    
    public String getSchuelerGebOrt(int idSCHUELER) throws SQLException{
        String gebort="";
        try (Statement statement = con.createStatement()) {
            String sql = "select GEBORT from SCHUELER where ID_SCHUELER = " + idSCHUELER ;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                gebort = set.getString(1);
            }
        }
        return gebort;
    }
 
    public ArrayList<String> getAVerhalten(int idSCHULER) throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        
        // dies ist nur ein Provisorium
        // zieht Daten nicht aus Zeugnis sondern aus KRITERIUM
        //kann erst gemacht werden, wenn Zeugnis befüllt....
        try (Statement statement = con.createStatement()) {

            String sql = "select KRITERIUMTEXT from KRITERIUM where ID_LERNBEREICH=1 AND SCHULJAHR ="+Gui.getSYear();
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                result.add(set.getString(1));
            }

        }
        return result;
    }

    public ArrayList<String> getSVerhalten(int idSCHULER) throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        
        // dies ist nur ein Provisorium
        // zieht Daten nicht aus Zeugnis sondern aus KRITERIUM
        //kann erst gemacht werden, wenn Zeugnis befüllt....
        try (Statement statement = con.createStatement()) {

            String sql = "select KRITERIUMTEXT from KRITERIUM where ID_LERNBEREICH=2 AND SCHULJAHR ="+Gui.getSYear();
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                result.add(set.getString(1));
            }

        }
        return result;
    }
    
    /**
     * liefert die Kriterien für den übergebenen Lernbereich für die aktuelle 
     * Klassenstufe und das aktuelle Schuljahr
     * @param idSCHULER
     * @param lernbereich
     * @return
     * @throws SQLException 
     */
    public ArrayList<String> getKriterien(int idSCHULER,String lernbereich) throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        
        // dies ist nur ein Provisorium
        // zieht Daten nicht aus Zeugnis sondern aus KRITERIUM
        //kann erst gemacht werden, wenn Zeugnis befüllt....
        try (Statement statement = con.createStatement()) {
            String k=Gui.getSClass().substring(0, 1);
            String sql = "select KRITERIUMTEXT from KRITERIUM,LERNBEREICH where LERNBEREICH.LERNBEREICH='"+ lernbereich+"' AND KRITERIUM.ID_LERNBEREICH=LERNBEREICH.ID_LERNBEREICH AND LERNBEREICH.KLASSENSTUFE=" + k +" AND LERNBEREICH.SCHULJAHR ="+Gui.getSYear() + " AND KRITERIUM.SCHULJAHR ="+Gui.getSYear();
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                result.add(set.getString(1));
            }
        }
        return result;
    }
     
       /**
     * liefert die Liste der ID_Kriterien für den übergebenen ID_Lernbereich für die aktuelle 
     * Klassenstufe und das aktuelle Schuljahr
     * @param idSCHULER
     * @param lernbereich
     * @return
     * @throws SQLException 
     */
    public ArrayList<Integer> getID_Kriterien(int idLernbereich) throws SQLException {
        ArrayList<Integer> result = new ArrayList<>();

        try (Statement statement = con.createStatement()) {
            String k;
            int y;
//            try {
//                k = Gui.getSClass().substring(0, 1);
//                y = Gui.getSYear();
//            } catch (Exception ex) {
//                k="1";
//                y=2016;
//            }
                k="1";
                y=2016;
            
//            String sql = "select ID_KRITERIUM from KRITERIUM where KRITERIUM.ID_LERNBEREICH=" + idLernbereich +" AND KRITERIUM.SCHULJAHR =2016";
            String sql = "select ID_KRITERIUM from KRITERIUM where KRITERIUM.ID_LERNBEREICH=" + idLernbereich +" AND KRITERIUM.SCHULJAHR ="+y;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                result.add(set.getInt(1));
            }
        }
        return result;
    }

     /**
     * liefert ID_Kriterien, die zur Zeugnis.ID_KRITERIUMSLISTE passen
     * in einer Hashtable. Dadurch kann man mit der ID_KRITERIUM leicht die 
     * Bewertung erhalten
     * immer für die aktuelle Klassenstufe/Schuljahr/Halbjahr
     * Diese Liste wird dann in einer Hashtabelle (ID_KRITERIUM,BEWERTUNG) abgelegt
     * @param idSCHULER
     * @return
     * @throws SQLException 
     */
    public Hashtable<Integer,Integer> getID_KriterienZeugnis(int idSchueler) throws SQLException {
        Hashtable<Integer,Integer> resultHT = new Hashtable<Integer,Integer>();

        try (Statement statement = con.createStatement()) {
            String sql = "select ID_KRITERIUM,BEWERTUNG from KRITERIUMSLISTE,ZEUGNIS where KRITERIUMSLISTE.ID_KRITERIUMSLISTE=ZEUGNIS.ID_ZEUGNIS AND ZEUGNIS.ID_SCHUELER=" + idSchueler + " AND ZEUGNIS.HALBJAHR="+ Gui.getHYear()+" AND ZEUGNIS.SCHULJAHR ="+Gui.getSYear();
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                resultHT.put(set.getInt(1), set.getInt(2));
            }
        }
        return resultHT;
    }

    /**
     * liefert eine Liste der Lernbereiche für das aktuelle Schuljahr und 
     * die aktuelle Klassenstufe
     * @param schuljahr
     * @param klassenstufe
     * @return result
     * @throws java.sql.SQLException
     */
    public ArrayList<String> getLernbereiche() throws SQLException{ 
        ArrayList<String> result = new ArrayList<>();
        try (Statement statement = con.createStatement()) {

            String sql = "select LERNBEREICH from LERNBEREICH where KLASSENSTUFE="+ 
                    Gui.getSClass().substring(0,1) + " AND SCHULJAHR =" +
                    Gui.getSYear();
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                result.add(set.getString(1));
            }
        }
        return result;
    }
    
     /**
     * liefert eine Liste der Lernbereiche, für das aktuelle Schuljahr und 
     * die aktuelle Klassenstufe incl der Kalssenstuf 0
     * @param schuljahr
     * @param klassenstufe
     * @return result
     * @throws java.sql.SQLException
     */
    public ArrayList<String> getLernbereicheIncl0() throws SQLException{ 
        ArrayList<String> result = new ArrayList<>();
        try (Statement statement = con.createStatement()) {

            String sql = "select LERNBEREICH from LERNBEREICH where (KLASSENSTUFE="+ 
                    Gui.getSClass().substring(0,1) + " OR KLASSENSTUFE = 0 )AND  SCHULJAHR =" +
                    Gui.getSYear();
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                result.add(set.getString(1));
            }
        }
        return result;
    }

        /**
     * liefert eine Liste der ID_Lernbereiche, die für das aktuelle Schuljahr und 
     * die aktuelle Klassenstufe
     * @param schuljahr
     * @param klassenstufe
     * @return result
     * @throws java.sql.SQLException
     */
    public ArrayList<Integer> getID_Lernbereiche() throws SQLException{ 
        ArrayList<Integer> result = new ArrayList<>();
        String k;
        int y;
        try {
            k = Gui.getSClass().substring(0, 1);
            y = Gui.getSYear();
        } catch (Exception ex) {
            k="1";
            y=2016;
        }
//            k="1";
//            y=2016;

        try (Statement statement = con.createStatement()) {
            String sql = "select ID_LERNBEREICH from LERNBEREICH where (KLASSENSTUFE="+ k +" OR KLASSENSTUFE=0) AND SCHULJAHR =" + y;
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                result.add(set.getInt(1));
            }
        }
        return result;
    }

    
    
    /**
     * Shuts down the Derby Server in case of starting by this class.
     *
     * @throws Exception
     */
    public void shutdown() throws Exception {

        if (!foreignDerby) {
            server.shutdown();
        }

    }

}
