/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
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
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.derby.drda.NetworkServerControl;
import org.apache.derby.tools.ij;

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
        con = DriverManager.getConnection("jdbc:derby:Zeugnis;create=true",
//        con = DriverManager.getConnection("jdbc:derby://localhost:1527/Zeugnis;create=true",
                config.getProperty("derbyUser"),
                config.getProperty("derbyPassword"));
        
        // Datenbanktabellen erstellen und default Werte laden.
        // Dazu werden die Statements aus der Zeugnis.sql ausgeführt.
        CreateDatabase.create(con);

    }
    
            
    // Wenn DB empty, Script ausführen
    protected boolean runScript(String file) throws SQLException {
        InputStream inputStream=null;
        Boolean retVal=true;
        // Test, ob Datenbank gefüllt
        // ...
        if (!existTBZeugnis()) {
            try {
                inputStream = this.getClass().getResourceAsStream(file);;
                int result = ij.runScript(this.con, inputStream, "UTF-8", System.out, "UTF-8");
                logger.fine("Intialized DB Zeugnis: " + result);
                retVal= (result == 0);
            } catch (UnsupportedEncodingException e) {
                logger.fine("SQL file not found.");
                retVal= false;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return retVal;

    }

    /**
     * der Primary Key des Schuelers wird erzeugt aus dem Hashcode aus
     * NameVornameGebDatumSchuljahr. Das Datum im Format yyyy-MM-dd.
     * Gleichzeitig werden zwei Zeugnisse erzeugt. Für jedes Halbjahr eines
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
//            int idZeugnis = (values[1] + values[2] + values[3] + values[6] + "1").hashCode();
            int idZeugnis =  (getIdZeugnis((Integer)Integer.parseInt(values[0]),1).hashCode());
            sql = "insert into ZEUGNIS (ID_ZEUGNIS, ID_SCHUELER, NOTE_ARBEIT, NOTE_SOZIAL, FEHLTAGE, FEHLTAGEOHNE, ENTWICKLUNG, BEMERKUNG, HALBJAHR, SCHULJAHR) values(" + idZeugnis
                    + ", " + values[0]
                    + ", " + 0
                    + ", " + 0
                    + ", " + 0
                    + ", " + 0
                    + ", " + "' '"
                    + ", " + "' '"                    
                    + ", " + 1
                    + ", " + values[6]
                    + ")";

            logger.fine(sql);
            statement.executeUpdate(sql);
            insertKriteriumslisteAll(idZeugnis);
            logger.fine("Kriterien eingefügt");

//            idZeugnis = (values[1] + values[2] + values[3] + values[6] + "2").hashCode();
            idZeugnis =  (getIdZeugnis((Integer)Integer.parseInt(values[0]),2).hashCode());
            sql = "insert into ZEUGNIS (ID_ZEUGNIS, ID_SCHUELER, NOTE_ARBEIT, NOTE_SOZIAL, FEHLTAGE, FEHLTAGEOHNE, ENTWICKLUNG, BEMERKUNG, HALBJAHR, SCHULJAHR) values(" + idZeugnis
//            sql = "insert into ZEUGNIS (ID_ZEUGNIS, ID_SCHUELER, HALBJAHR, SCHULJAHR) values(" + idZeugnis
                    + ", " + values[0]
                    + ", " + 0
                    + ", " + 0
                    + ", " + 0
                    + ", " + 0
                    + ", " + "' '"
                    + ", " + "' '"                    
                    + ", " + 2
                    + ", " + values[6]
                    + ")";

            logger.fine(sql);
            statement.executeUpdate(sql);
            insertKriteriumslisteAll(idZeugnis);
            logger.fine("Kriterien eingefügt");
        }

    }

    /**
     * Zum Update eines Zeugnisses. Es wird ein String[9] erwartet. Die Felder
     * die NULL sind werden ignoriert. Das Feld ID_SCHUELER muß gefuellt sein.
     *
     * @param values Die Werte in der Reihenfolge der Tabellenspalten.
     * @throws SQLException
     */
    public void updateZeugnis(String[] values) throws SQLException {

        try (Statement statement = con.createStatement()) {
            String sql = "update ZEUGNIS set ";
            if (values[1] != null) {
                sql += "ID_SCHUELER = " + values[1] + ", ";
            }
            if (values[2] != null) {
                sql += "NOTE_ARBEIT = " + values[2] + ", ";
            }
            if (values[3] != null) {
                sql += "NOTE_SOZIAL = " + values[3] + ", ";
            }
            if (values[4] != null) {
                sql += "FEHLTAGE = " + values[4] + ", ";
            }
            if (values[5] != null) {
                sql += "FEHLTAGEOHNE = " + values[5] + ", ";
            }
            if (values[6] != null) {
                sql += "ENTWICKLUNG = '" + values[6] + "', ";
            }
            if (values[7] != null) {
                sql += "BEMERKUNG = '" + values[7] + "', ";
            }
            if (values[8] != null) {
                sql += "HALBJAHR = " + values[8] + ", ";
            }
            if (values[9] != null) {
                sql += "SCHULJAHR = " + values[9] + ", ";
            }
            sql = (sql.substring(0, sql.length() - 2)) + " where ID_ZEUGNIS = " + values[0];
            logger.fine(sql);
            statement.executeUpdate(sql);
        }

    }

    /**
     * Gibt den Inhalt einer Zeile der Tabelle Zeugnis als String[] zurueck. Die
     * Reihenfolge im Array ist die gleich wie die der Spalten in der Tabelle.
     *
     * @param idZeugnis IDZeugnis
     * @return die Daten der abgefragten Zeile oder NULL wenn kein Ergebnis
     * gefunden wird.
     * @throws java.sql.SQLException
     */
    public String[] fetchZeugnis(int idZeugnis) throws SQLException {
        String[] result = new String[9];

        try (Statement statement = con.createStatement()) {
            String sql = "select * from ZEUGNIS where ID_ZEUGNIS = " + idZeugnis;
            logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            if (set.next()) {
                
                for(int i = 0; i < result.length; i ++) {
                    result[i] = set.getNString(i + 1);
                }
                
                return result;
            } else {
                return null;
            }

        }

    }
    
    /**
     * Gibt den Inhalt einer Zeile der Tabelle Zeugnis als String[] zurueck. Die
     * Reihenfolge im Array ist die gleich wie die der Spalten in der Tabelle.
     *
     * @param idZeugnis IDSchueler
     * @param halbjahr Das Schulhalbjahr
     * @return die Daten der abgefragten Zeile oder NULL wenn kein Ergebnis
     * gefunden wird.
     * @throws java.sql.SQLException
     */
    public String[] fetchZeugnis(int idSchueler, int halbjahr) throws SQLException {
        String[] result = new String[9];

        try (Statement statement = con.createStatement()) {
            String sql = "select * from ZEUGNIS where ID_SCHUELER = " + idSchueler + " AND HALBJAHR = " + halbjahr;
            logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            if (set.next()) {
                
                for(int i = 0; i < result.length; i ++) {
                    result[i] = set.getString(i + 1);
                }
                
                return result;
            } else {
                return null;
            }

        }

    }

    public Boolean existKriteriumsliste() throws SQLException {
        try (Statement statement = con.createStatement()) {
            String sql = "select * from KRITERIUMSLISTE";
            logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            return set.next();
        }
    }

    /***
     * prüft, ob Datenbanken existieren
     * Exception bei Zugriff auf DB ergibt false
     * @return
     * @throws SQLException 
     */
    public Boolean existTBZeugnis() throws SQLException {
        try (Statement statement = con.createStatement()) {
            String sql = "SELECT * FROM ZEUGNIS.ZEUGNIS";
            logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            return true;
        } catch(SQLException e){
            return false;
        }
    }

    /***
     * Fügt für ein übergebenes Zeugnis 
     * alle zugehörigen Kriterien_IDs in die Datenbank mit der Bewertung 0 ein
     * 
     * @param zid 
     */
    public void insertKriteriumslisteAll(Integer zid){
        Integer bewertung = 1;
        try {
            ArrayList<Integer> lbid = new ArrayList<Integer>(); // ID_Lernbereiche
            lbid = this.getID_Lernbereiche();
            for (Integer l : lbid) {
                ArrayList<Integer> kid = new ArrayList<Integer>();  // ID_Kriterium
                kid = this.getID_Kriterien(l);
                for (Integer k : kid) {
                    String[] s = {Integer.toString(zid), Integer.toString(k), Integer.toString(bewertung)};
                    //logger.fine("----------------------->>>>>>>>>>>>>" + Integer.toString(zid));
                    this.insertKriteriumsliste(s);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SingletonSQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public void insertKriteriumsliste(String[] values) throws SQLException {
        try (Statement statement = con.createStatement()) {
            String sql = "insert into KRITERIUMSLISTE values("
                    + values[0]
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
     * Löschen eines Schülers mit den dzugehoerigen Zeugnissen.
     *
     * @param idschueler Die Id des zu loeschenden Datensatzes.
     * @throws SQLException
     */
    public void deletePuple(String idSchueler) throws SQLException {

        try (Statement statement = con.createStatement()) {
            String sql = "delete from ZEUGNIS where ID_SCHUELER = " + idSchueler;
            logger.fine(sql);
            statement.executeUpdate(sql);
            
            sql = "delete from SCHUELER where ID_Schueler = " + idSchueler;
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
    public void fillClassTable(JTable table, String sYear, String sClass) throws SQLException {

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

    /**
     * liefert das Schuljahr aus der Schuelertabelle für die idSchueler
     * @param idSCHUELER
     * @return
     * @throws SQLException 
     */
    public String getSchuelerSchuljahr(int idSCHUELER) throws SQLException {
        String name = "";
        try (Statement statement = con.createStatement()) {
            String sql = "select SCHULJAHR from SCHUELER where ID_SCHUELER = " + idSCHUELER;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                name = set.getString(1);
            }
        }
        return name;
    }

    /**
     * liefert den Nachnamen aus der Schuelertabelle für die idSchueler
     * @param idSCHUELER
     * @return
     * @throws SQLException 
     */
    public String getSchuelerName(int idSCHUELER) throws SQLException {
        String name = "Name";
        try (Statement statement = con.createStatement()) {
            String sql = "select NAME from SCHUELER where ID_SCHUELER = " + idSCHUELER;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                name = set.getString(1);
            }
        }
        return name;
    }

    /**
     * liefert den Vornamen aus der Schuelertabelle für die idSchueler
     * @param idSCHUELER
     * @return
     * @throws SQLException 
     */
    public String getSchuelerVorname(int idSCHUELER) throws SQLException {
        String vorname = "Vorname";
        try (Statement statement = con.createStatement()) {
            String sql = "select VORNAME from SCHUELER where ID_SCHUELER = " + idSCHUELER;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                vorname = set.getString(1);
            }
        }
        return vorname;
    }

    /**
     * liefert das Geburtsdatum aus der Schuelertabelle für die idSchueler
     * @param idSCHUELER
     * @return
     * @throws SQLException 
     */
    public String getSchuelerGebDatum(int idSCHUELER) throws SQLException {
        String gebdatum = "";
        try (Statement statement = con.createStatement()) {
            String sql = "select GEBDATUM from SCHUELER where ID_SCHUELER = " + idSCHUELER;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                gebdatum = set.getString(1);
            }
        }
        return gebdatum;
    }

    /**
     * liefert den Geburtsort aus der Schuelertabelle für die idSchueler
     * @param idSCHUELER
     * @return
     * @throws SQLException 
     */
    public String getSchuelerGebOrt(int idSCHUELER) throws SQLException {
        String gebort = "";
        try (Statement statement = con.createStatement()) {
            String sql = "select GEBORT from SCHUELER where ID_SCHUELER = " + idSCHUELER;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                gebort = set.getString(1);
            }
        }
        return gebort;
    }

    /**
     * liefert die Fehltage aus dem Zeugnis
     * @param zid
     * @return
     * @throws SQLException 
     */
    public Integer getFehltage(Integer zid) throws SQLException{
        Integer retVal=0;
        try (Statement statement = con.createStatement()) {
            String sql = "select FEHLTAGE from ZEUGNIS where ID_ZEUGNIS=" + zid;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                retVal = set.getInt(1);
            }
        }
        return retVal;
    }
    
    /**
     * liefert die Fehltage ohne Entsch aus dem Zeugnis
     * @param zid
     * @return
     * @throws SQLException 
     */
    public Integer getFehltageOhne(Integer zid) throws SQLException{
        Integer retVal=0;
        try (Statement statement = con.createStatement()) {
            String sql = "select FEHLTAGEOHNE from ZEUGNIS where ID_ZEUGNIS=" + zid;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                retVal = set.getInt(1);
            }
        }
        return retVal;
    }
    
    /**
     * liefert eine Liste der ID_KRITERIUM des Arbeitsverhaltens für aktuelles Schuljahr
     * @return
     * @throws SQLException 
     */
    public ArrayList<Integer> getAVerhaltenID() throws SQLException {
        ArrayList<Integer> result = new ArrayList<Integer>();
        
        try (Statement statement = con.createStatement()) {
            String sql = "select ID_KRITERIUM from KRITERIUM,LERNBEREICH where LERNBEREICH.ID_LERNBEREICH=1 AND KLASSENSTUFE=0 AND LERNBEREICH.SCHULJAHR =" + Gui.getSYear() +
                    " AND LERNBEREICH.SCHULJAHR=KRITERIUM.SCHULJAHR AND LERNBEREICH.ID_LERNBEREICH=KRITERIUM.ID_LERNBEREICH";
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                result.add(set.getInt(1));
            }
        }
        return result;
    }

    /**
     * liefert eine Liste der ID_KRITERIUM des Sozialverhaltens für aktuelles Schuljahr
     * @return
     * @throws SQLException 
     */
    public ArrayList<Integer> getSVerhaltenID() throws SQLException {
        ArrayList<Integer> result = new ArrayList<Integer>();
        
        try (Statement statement = con.createStatement()) {
            String sql = "select ID_KRITERIUM from KRITERIUM,LERNBEREICH where LERNBEREICH.ID_LERNBEREICH=2 AND KLASSENSTUFE=0 AND LERNBEREICH.SCHULJAHR =" + Gui.getSYear() +
                    " AND LERNBEREICH.SCHULJAHR=KRITERIUM.SCHULJAHR AND LERNBEREICH.ID_LERNBEREICH=KRITERIUM.ID_LERNBEREICH";
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                result.add(set.getInt(1));
            }
        }
        return result;
    }

    /**
     * liefert die Note (0-4) für das Arbeitsverhalten aus dem Zeugnis
     * @param zid
     * @return
     * @throws SQLException 
     */
    public Integer getNoteArbeit(Integer zid) throws SQLException{
        Integer retVal=0;
        
        try (Statement statement = con.createStatement()) {
            String sql = "select NOTE_ARBEIT from ZEUGNIS where ID_ZEUGNIS=" + zid;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                retVal= set.getInt(1);
            }
        }
        return retVal; 
    }
    
    /**
     * liefert die Note (0-4) für das Sozialverhalten aus dem Zeugnis
     * @param zid
     * @return
     * @throws SQLException 
     */
    public Integer getNoteSozial(Integer zid) throws SQLException{
        Integer retVal=0;
        
        try (Statement statement = con.createStatement()) {
            String sql = "select NOTE_SOZIAL from ZEUGNIS where ID_ZEUGNIS=" + zid;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                retVal= set.getInt(1);
            }
        }
        return retVal; 
    }
    

    /**
     * liefert den Kriteriumtext zur übergebenen idKriterium
     * 
     * @param idKriterium
     * @return
     * @throws SQLException 
     */
    public String getKriteriumText(Integer idKriterium) throws SQLException{
        String retVal="";
        try (Statement statement = con.createStatement()) {
            String sql = "select KRITERIUMTEXT from KRITERIUM where ID_KRITERIUM=" + idKriterium;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                retVal = set.getString(1);
            }
        }
        return retVal;
    }
    
    /**
     * liefert die Kriterien für den übergebenen Lernbereich für die aktuelle
     * Klassenstufe und das aktuelle Schuljahr
     *
     * @param idSCHULER
     * @param lernbereich
     * @return
     * @throws SQLException
     */
    public ArrayList<String> getKriterien(int idSCHULER, String lernbereich) throws SQLException {
        ArrayList<String> result = new ArrayList<>();

        // dies ist nur ein Provisorium
        // zieht Daten nicht aus Zeugnis sondern aus KRITERIUM
        //kann erst gemacht werden, wenn Zeugnis befüllt....
        try (Statement statement = con.createStatement()) {
            String k = Gui.getSClass().substring(0, 1);
            String sql = "select KRITERIUMTEXT from KRITERIUM,LERNBEREICH where LERNBEREICH.LERNBEREICH='" + lernbereich + "' AND KRITERIUM.ID_LERNBEREICH=LERNBEREICH.ID_LERNBEREICH AND LERNBEREICH.KLASSENSTUFE=" + k + " AND LERNBEREICH.SCHULJAHR =" + Gui.getSYear() + " AND KRITERIUM.SCHULJAHR =" + Gui.getSYear();
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                result.add(set.getString(1));
            }
        }
        return result;
    }

    /**
     * liefert die Liste der ID_Kriterien für den übergebenen ID_Lernbereich für
     * die aktuelle Klassenstufe und das aktuelle Schuljahr
     *
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
            k = "1";
            y = 2016;

//            String sql = "select ID_KRITERIUM from KRITERIUM where KRITERIUM.ID_LERNBEREICH=" + idLernbereich +" AND KRITERIUM.SCHULJAHR =2016";
            String sql = "select ID_KRITERIUM from KRITERIUM where KRITERIUM.ID_LERNBEREICH=" + idLernbereich + " AND KRITERIUM.SCHULJAHR =" + y;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                result.add(set.getInt(1));
            }
        }
        return result;
    }

    /**
     * liefert ID_Kriterien, die zur Zeugnis.ID_KRITERIUMSLISTE passen in einer
     * Hashtable. Dadurch kann man mit der ID_KRITERIUM leicht die Bewertung
     * erhalten immer für die aktuelle Klassenstufe/Schuljahr/Halbjahr Diese
     * Liste wird dann in einer Hashtabelle (ID_KRITERIUM,BEWERTUNG) abgelegt
     *
     * @param idZeugnis
     * @return
     * @throws SQLException
     */
    public Hashtable<Integer, Integer> getID_KriterienZeugnis(int idZeugnis) throws SQLException {
        Hashtable<Integer, Integer> resultHT = new Hashtable<Integer, Integer>();

        try (Statement statement = con.createStatement()) {
            String sql = "select ID_KRITERIUM,BEWERTUNG from KRITERIUMSLISTE,ZEUGNIS where KRITERIUMSLISTE.ID_KRITERIUMSLISTE=ZEUGNIS.ID_ZEUGNIS AND ZEUGNIS.ID_ZEUGNIS=" + idZeugnis;
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                resultHT.put(set.getInt(1), set.getInt(2));
            }
        }
        return resultHT;
    }

    /**
     * liefert eine Liste der Lernbereiche für das aktuelle Schuljahr und die
     * aktuelle Klassenstufe
     *
     * @param schuljahr
     * @param klassenstufe
     * @return result
     * @throws java.sql.SQLException
     */
    public ArrayList<String> getLernbereiche() throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        try (Statement statement = con.createStatement()) {

            String sql = "select LERNBEREICH from LERNBEREICH where KLASSENSTUFE="
                    + Gui.getSClass().substring(0, 1) + " AND SCHULJAHR ="
                    + Gui.getSYear();
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                result.add(set.getString(1));
            }
        }
        return result;
    }

    /**
     * liefert eine Liste der Lernbereiche, für das aktuelle Schuljahr und die
     * aktuelle Klassenstufe incl der Klassenstufe 0
     *
     * @param schuljahr
     * @param klassenstufe
     * @return result
     * @throws java.sql.SQLException
     */
    public ArrayList<String> getLernbereicheIncl0() throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        try (Statement statement = con.createStatement()) {

            String sql = "select LERNBEREICH from LERNBEREICH where (KLASSENSTUFE="
                    + Gui.getSClass().substring(0, 1) + " OR KLASSENSTUFE = 0 )AND  SCHULJAHR ="
                    + Gui.getSYear();
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                result.add(set.getString(1));
            }
        }
        return result;
    }

    /**
     * liefert eine Liste der ID_Lernbereiche, die für das aktuelle Schuljahr
     * und die aktuelle Klassenstufe
     *
     * @param schuljahr
     * @param klassenstufe
     * @return result
     * @throws java.sql.SQLException
     */
    public ArrayList<Integer> getID_Lernbereiche() throws SQLException {
        ArrayList<Integer> result = new ArrayList<>();
        String k;
        int y;
        try {
            k = Gui.getSClass().substring(0, 1);
            y = Gui.getSYear();
        } catch (Exception ex) { // wenn GUI noch nicht initialisiert...(Testcode)
            k = "1";
            y = 2016;
        }
//            k="1";
//            y=2016;

        try (Statement statement = con.createStatement()) {
            String sql = "select ID_LERNBEREICH from LERNBEREICH where (KLASSENSTUFE=" + k + " OR KLASSENSTUFE=0) AND SCHULJAHR =" + y;
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                result.add(set.getInt(1));
            }
        }
        return result;
    }

    /**
     * Liefert die ID_ZEUGNIS zurück, wenn man die ID_SCHUELER übergibt
     * @param idSchueler
     * @return
     * @throws SQLException 
     */
    public Integer getIdZeugnis(Integer idSchueler, Integer halbjahr) throws SQLException{
        Integer retVal;
        retVal = (this.getSchuelerName(idSchueler) + this.getSchuelerVorname(idSchueler) +
                this.getSchuelerGebDatum(idSchueler) + this.getSchuelerSchuljahr(idSchueler) + halbjahr.toString()).hashCode();
        return retVal;
    }
    
    /**
     * liefert ID_Kritierien bei Eingabe eines Lernbereichs zu passendem Schuljahr und Klassenstufe
     * @param lernbereich
     * @return
     * @throws SQLException 
     */
    public ArrayList<Integer> getID_KriterienFromLernbereich(String lernbereich) throws SQLException{
        ArrayList<Integer> result = new ArrayList<Integer>();

        try (Statement statement = con.createStatement()) {
            String klassenstufe;
            int schuljahr;

            klassenstufe = Gui.getSClass().substring(0, 1);
            schuljahr = Gui.getSYear();

            String sql = "select ID_KRITERIUM from KRITERIUM,LERNBEREICH where LERNBEREICH.LERNBEREICH like '" + lernbereich + "%'"+
                    " AND LERNBEREICH.SCHULJAHR=" + schuljahr +
                    " AND (LERNBEREICH.KLASSENSTUFE=0 OR LERNBEREICH.KLASSENSTUFE =" + klassenstufe + ")" +
                    " AND LERNBEREICH.ID_LERNBEREICH=KRITERIUM.ID_LERNBEREICH " + 
                    " AND LERNBEREICH.SCHULJAHR=KRITERIUM.SCHULJAHR";
            //logger.fine(sql);
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
