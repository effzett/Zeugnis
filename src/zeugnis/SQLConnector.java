package zeugnis;

import com.itextpdf.text.DocumentException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.derby.drda.NetworkServerControl;

public class SQLConnector {

    private final static Logger logger = Logger.getLogger(SQLConnector.class.getName());
    private Connection con = null;
    private NetworkServerControl server = null;
    private boolean foreignDerby = false;
    private Properties config = null;
    SimpleDateFormat sdf = null;

    public SQLConnector(Properties config) {
        this.config = config;
        sdf = new SimpleDateFormat("dd.MM.YYYY");
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
     * @param values Die Werte zum Anlegen eines Schuelers
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

            String sql = "select IDSCHUELER, NAME, VORNAME, GEBDATUM, GEBORT from SCHUELER where KALSSE = '" + sClass + "' and SCHULJAHR = " + sYear;
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
     * Fühlt die übergebene JTable direkt mit Daten aus der Datenbank.
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
