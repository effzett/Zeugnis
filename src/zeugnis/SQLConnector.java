package zeugnis;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.apache.derby.drda.NetworkServerControl;

public class SQLConnector {

    private static boolean log = true;
    private Connection con = null;
    private NetworkServerControl server = null;
    private boolean foreignDerby = false;
    private Properties config = null;

    public SQLConnector(Properties config) {
        this.config = config;
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
        con = DriverManager.getConnection("jdbc:derby://localhost:1527/zeugnis",
                config.getProperty("derbyUser"),
                config.getProperty("derbyPassword"));
    }

    /*        
     public boolean dataExists(String kennzeichen) throws SQLException {

     try (Statement statement = con.createStatement()) {

     String sql = "select KENNZEICHEN from DATENSATZ where KENNZEICHEN = '" + kennzeichen + "'";

     if (log) {
     System.out.println(sql);
     }

     ResultSet set = statement.executeQuery(sql);

     if (set.next()) {
     return true;
     } else {
     return false;
     }

     }

     }
     */
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
