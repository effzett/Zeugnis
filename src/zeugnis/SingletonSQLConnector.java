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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.derby.drda.NetworkServerControl;

/**
 *
 * @author u033334
 */
public class SingletonSQLConnector {

    private final static Logger logger = Logger.getLogger(SQLConnector.class.getName());
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
     * NameVornameGebDatumSchuljahr. Das Datum im Format yyyy.MM-dd.
     *
     * @param values Die Werte zum Anlegen eines Schuelers in der Reihen folge
     * sder Spalten der Tabelle SCHUELER
     * @throws SQLException
     */
    public void insertPuple(String[] values) throws SQLException, ParseException {

        try (Statement statement = con.createStatement()) {
            // Datumstring zu SQLDatum konvertieren
            Date date = sdf.parse(values[3]);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            String sql = "insert into SCHUELER values(" + values[0]
                    + ",'" + values[1]
                    + "', '" + values[2]
                    + "', '" + sqlDate.toString()
                    + "', '" + values[4]
                    + "', '" + values[5]
                    + "', " + values[6] + ")";

            logger.fine(sql);
            statement.executeUpdate(sql);
        }

    }

    /**
     * Update eines Schülers. Es werden alle Werte einer Zeile upgedatet (außer
     * dem Key)
     *
     * @param values Eine String[] Array mit der Reihenfolge den Werten aller
     * Spalten der Tabelle SCHUELER
     * @throws SQLException
     */
    public void updatePuple(String[] values) throws SQLException, ParseException {
        // Datumstring zu SQLDatum konvertieren
        Date date = sdf.parse(values[3]);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        try (Statement statement = con.createStatement()) {
            String sql = "update SCHUELER set NAME = '" + values[1]
                    + "', VORNAME = '" + values[2]
                    + "', GEBDATUM = '" + sqlDate.toString()
                    + "', GEBORT = '" + values[4]
                    + "', KLASSE = '" + values[5]
                    + "', SCHULJAHR = " + values[6]
                    + " where ID_SCHUELER = " + values[0];

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

            String sql = "select IDSCHUELER, NAME, VORNAME, GEBDATUM, GEBORT from SCHUELER where KLASSE = '" + sClass + "' and SCHULJAHR = " + sYear;
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
            String sql = "select ID_SCHUELER, NAME, VORNAME, GEBDATUM, GEBORT from SCHUELER where KLASSE = '" + sClass + "' and SCHULJAHR = " + sYear;
            logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            while (set.next()) {
                Object[] row = new Object[6];
                row[0] = set.getString(2);
                row[1] = set.getString(3);
                row[2] = sdf.format(set.getDate(4));
                row[3] = set.getString(5);
                model.addRow(row);
            }

        }

    }

    /**
     * Speichert den Inhalt der uebergebenen JTable in der Tabelle SCHUELER.
     * Vorhandene Datensaetze werden unabhaengig ob eine Aenderung stattgefunden
     * hat aktualisiert, Neue Datensaetze eingefuegt.
     *
     * @param table Die Tabelle mit der Schulklasse.
     * @param sYear Das Schuljahr
     * @param sClass Die Klasse
     * @throws SQLException
     */
    public void insertUpdateClassTable(JTable table, int sYear, String sClass) throws SQLException, ParseException {
        TableModel model = table.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            String[] values = new String[7];
            values[5] = sClass;
            values[6] = Integer.toString(sYear);

            for (int ii = 0; ii < model.getColumnCount(); ii++) {
                String cName = model.getColumnName(ii);
                logger.fine(cName);

                switch (cName) {
                    case "Name":
                        values[1] = (String) model.getValueAt(i, ii);
                        break;
                    case "Vorname":
                        values[2] = (String) model.getValueAt(i, ii);
                        break;
                    case "Geburtsdatum":
                        values[3] = (String) model.getValueAt(i, ii);
                        break;
                    case "Geburtsort":
                        values[4] = (String) model.getValueAt(i, ii);
                        break;
                }

            }

            values[0] = Integer.toString((values[1] + values[2] + values[3] + values[6]).hashCode());

            if (pupleExist(Integer.parseInt(values[0]))) {
                updatePuple(values);
            } else {
                insertPuple(values);
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
            logger.fine(sql);
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
            logger.fine(sql);
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
            logger.fine(sql);
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
            logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                gebort = set.getString(1);
            }
        }
        return gebort;
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
