/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
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

    private String getNewKlasse(String klasse) {
        String newKlasse = klasse;

        if (klasse.matches("[1-3].")) {
            // Einfache Methode
            newKlasse = newKlasse.replace('3', '4');
            newKlasse = newKlasse.replace('2', '3');
            newKlasse = newKlasse.replace('1', '2');
        } else {
            if(!klasse.matches("[4].")){
                logger.severe("Die Klassenbezeichnung entspricht nicht 1a,1b,1c,2a,2b,etc.");
            }
        }
        return newKlasse;
    }

    private static class SingeltonSQLConnectorHolder {

        private static final SingletonSQLConnector INSTANCE = new SingletonSQLConnector();
    }

    /**
     * startderby == Zugriff auf Embedded Server startderby == 0 Es wird ein
     * laufender externer Derby Server auf Port 1527 erwartet
     *
     * @throws Exception
     */
    public void connect() throws Exception {
        StringWriter sw = new StringWriter();

        if (config.getProperty("startDerby").equals("1")) {
            con = DriverManager.getConnection("jdbc:derby:Zeugnis;create=true",
                    config.getProperty("derbyUser"),
                    config.getProperty("derbyPassword"));

            // Datenbanktabellen erstellen und default Werte laden.
            // Dazu werden die Statements aus der Zeugnis.sql ausgeführt.
            CreateDatabase.create(con);
        } else {
            // Datenbank in der Entwicklungsumbung benutzen
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/Zeugnis",
                    config.getProperty("derbyUser"),
                    config.getProperty("derbyPassword"));
            server = new NetworkServerControl();
            server.start(new PrintWriter(sw));
            System.out.println(sw.toString());
        }

        // Connect to the server
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();

    }

    /**
     * *
     * Dies ist eine Alternative zum manuellen Einlesen des .sql Scripts Später
     * kann dies mal aktiviert werden...
     *
     * @param file
     * @return
     * @throws SQLException
     */
    protected boolean runScript(String file) throws SQLException {
        InputStream inputStream = null;
        Boolean retVal = true;
        // Test, ob Datenbank gefüllt
        // ...
        if (!existTBZeugnis()) {
            try {
                inputStream = this.getClass().getResourceAsStream(file);;
                int result = ij.runScript(this.con, inputStream, "UTF-8", System.out, "UTF-8");
                // logger.fine("Intialized DB Zeugnis: " + result);
                retVal = (result == 0);
            } catch (UnsupportedEncodingException e) {
                // logger.fine("SQL file not found.");
                retVal = false;
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

//    /**
//     * *
//     * Erzeugt ID aus NAME+VORNAME+GEBDATUM+SCHULJAHR
//     *
//     * @return
//     */
//    public Integer createIdSchueler(String name, String vorname, String gebdatum, String schuljahr) {
//        Integer retVal = 0;
//
//        retVal = (name + vorname + gebdatum + schuljahr).hashCode();
//        return retVal;
//    }

    /**
     * *
     * liefert das größte Schuljahr zurück, dass in SCHUELER gefunden wurde
     * falls kein Zeugnis vorhanden wird das aktuelle Jahr zurückgegeben
     */
    public Integer getMaxSchuljahrFromZeugnis() throws SQLException {
        Integer retVal = 0;
        Boolean found=false;

        try (Statement statement = con.createStatement()) {

            String sql = "select distinct SCHULJAHR from SCHUELER order by SCHULJAHR desc";
            // // logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                retVal = set.getInt(1);
                found=true;
                break;
            }
            // logger.fine(retVal.toString());
        }
        if(!found){
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date()); //heute
            retVal = cal.get(Calendar.YEAR);
        }
        return retVal;
    }

    /**
     * *
     * liefert das größte Schuljahr zurück, dass in KRITERIUM gefunden wurde
     */
    public Integer getMaxSchuljahrFromKriterium() throws SQLException {
        Integer retVal = 0;

        try (Statement statement = con.createStatement()) {

            String sql = "select distinct SCHULJAHR from KRITERIUM order by SCHULJAHR desc";
            // logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                retVal = set.getInt(1);
                break;
            }
            // logger.fine(retVal.toString());
        }
        return retVal;
    }

    /**
     * *
     * liefert das größte Schuljahr zurück, dass in LERNBEREICH gefunden wurde
     */
    public Integer getMaxSchuljahrFromLernbereich() throws SQLException {
        Integer retVal = 0;

        try (Statement statement = con.createStatement()) {

            String sql = "select distinct SCHULJAHR from LERNBEREICH order by SCHULJAHR desc";
            // logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                retVal = set.getInt(1);
                break;
            }
            // logger.fine(retVal.toString());
        }
        return retVal;
    }

//    /**
//     * *
//     * Generiert Daten für ein neues Schuljahr legt kein neues Schuljahr an,
//     * sondern füllt nur die Daten in das neue Schuljahr Sollte nur einmal
//     * aufgerufen werden durch die GUI wenn ein neues Schuljahr generiert wird
//     *
//     * @param schuljahr
//     */
//    public void generateNewYear(Integer schuljahr) throws SQLException {
//        Integer newYear = schuljahr;
//        Integer oldYear = schuljahr - 1;
//
//        // TODO Lernbereich für neues Schuljahr erstellen
//        // if(newYear not exist in LERNBEREICH)
//        if (newYear != this.getMaxSchuljahrFromLernbereich()) {
//            // Alle Daten von oldyear nehmen, aber mit ID_LERNBEREICH+1000 und newYear
//            appendNewYearLernbereich(newYear);
//        }
//
//        // TODO Kriterium Tabelle für neues Schuljahr erstellen
//        // if(newYear not exist in KRITERIUM)
//        if (newYear != this.getMaxSchuljahrFromKriterium()) {
//            // Alle Daten von oldyear nehmen, aber mit ID_KRITERIUM+1000 und newYear
//            // Achtung: ID_LERNBEREICH muss sich auf die richtigen Jahre in LERNBEREICH
//            // beziehen! Da immer +1000 sollte das so funktionieren....
//            appendNewYearKriterium(newYear);
//        }
//
//        for (Integer idSchueler : getIDSchueler(oldYear)) {
//            // logger.fine(idSchueler.toString());
//            // Für jeden Schuler durchführen...
//            String name = this.getSchuelerName(idSchueler);
//            String vorname = this.getSchuelerVorname(idSchueler);
//            String gebdatum = this.getSchuelerGebDatum(idSchueler);
//            String gebort = this.getSchuelerGebOrt(idSchueler);
//            String klasse = this.getSchuelerKlasse(idSchueler);
//
//            // Neue Klasse bestimmen
//            String newKlasse = getNewKlasse(klasse);
//
//            if (newKlasse.contains("2") || newKlasse.contains("3") || newKlasse.contains("4")) {   // 5. Klasse gibt's nicht
//                // Schueler einfügen
//                String[] schueler = new String[]{
//                    Integer.toString(this.createIdSchueler(name, vorname, gebdatum, Integer.toString(newYear))),
//                    name,
//                    vorname,
//                    gebdatum,
//                    gebort,
//                    newKlasse,
//                    Integer.toString(newYear)};
////                if(!this.insertPuple(schueler)){
//                if(_insertSchueler() !=0 ){
//                    this._updateSchueler(schueler, idSchueler);
//                }
//                else{
//                    // mindestens Fehlermeldung ausgeben...
//                    logger.fine("Konnte den Schüler nicht in die DB einfügen");                    
//                }
//                // logger.fine(schueler.toString());
//            }
//
//        }
//    }
    
    /**
     * *
     * Generiert Daten für ein neues Schuljahr legt kein neues Schuljahr an,
     * sondern füllt nur die Daten in das neue Schuljahr Sollte nur einmal
     * aufgerufen werden durch die GUI wenn ein neues Schuljahr generiert wird
     *
     * @param schuljahr
     */
    public void _generateNewYear(Integer schuljahr) throws SQLException {
        Integer newYear = schuljahr;
        Integer oldYear = schuljahr - 1;

        // TODO Lernbereich für neues Schuljahr erstellen
        // if(newYear not exist in LERNBEREICH)
        if (newYear != this.getMaxSchuljahrFromLernbereich()) {
            // Alle Daten von oldyear nehmen, aber mit ID_LERNBEREICH+1000 und newYear
            appendNewYearLernbereich(newYear);
        }

        // TODO Kriterium Tabelle für neues Schuljahr erstellen
        // if(newYear not exist in KRITERIUM)
        if (newYear != this.getMaxSchuljahrFromKriterium()) {
            // Alle Daten von oldyear nehmen, aber mit ID_KRITERIUM+1000 und newYear
            // Achtung: ID_LERNBEREICH muss sich auf die richtigen Jahre in LERNBEREICH
            // beziehen! Da immer +1000 sollte das so funktionieren....
            appendNewYearKriterium(newYear);
        }

        for (Integer idSchueler : getIDSchueler(oldYear)) {
            // logger.fine(idSchueler.toString());
            // Für jeden Schuler durchführen...
            String name = this.getSchuelerName(idSchueler);
            String vorname = this.getSchuelerVorname(idSchueler);
            String gebdatum = this.getSchuelerGebDatum(idSchueler);
            String gebort = this.getSchuelerGebOrt(idSchueler);
            String klasse = this.getSchuelerKlasse(idSchueler);

            // Neue Klasse bestimmen
            String newKlasse = getNewKlasse(klasse);

            if (newKlasse.contains("2") || newKlasse.contains("3") || newKlasse.contains("4")) {   // 5. Klasse gibt's nicht
                // Schueler einfügen
                String[] schueler = new String[]{
                    name,vorname,gebdatum,gebort,newKlasse,Integer.toString(newYear)};
                idSchueler = this._insertSchueler(newKlasse,newYear);
                if( idSchueler !=0 ){
                    this._updateSchueler(schueler, idSchueler);
                }
                else{
                    // mindestens Fehlermeldung ausgeben...
                    logger.fine("Konnte den Schüler nicht in die DB einfügen");                    
                }
            }
        }
    }

    private void appendNewYearLernbereich(Integer newYear) throws SQLException {
        Integer oldYear = newYear - 1;
        ArrayList<String> setInput = new ArrayList<String>();

        try (Statement statement = con.createStatement()) {
            String sql = "select * from LERNBEREICH where SCHULJAHR=" + oldYear;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                Integer idLernbereich = set.getInt(1);
                String lernbereich = set.getString(2);
                Integer klassenstufe = set.getInt(3);
                Integer notenbereich = set.getInt(4);
                Integer schuljahr = set.getInt(5);
                String sql2 = "insert into LERNBEREICH values("
                        + Integer.toString(idLernbereich + 10000) + ",'"
                        + lernbereich + "',"
                        + Integer.toString(klassenstufe) + ","
                        + Integer.toString(notenbereich) + ","
                        + Integer.toString(newYear) + ")";
                setInput.add(sql2);
            }
            for (String insertCmd : setInput) {
                // logger.fine(insertCmd);
                statement.executeUpdate(insertCmd);
            }
        }
    }

    private void appendNewYearKriterium(Integer newYear) throws SQLException {
        Integer oldYear = newYear - 1;
        ArrayList<String> setInput = new ArrayList<String>();

        try (Statement statement = con.createStatement()) {
            String sql = "select * from KRITERIUM where SCHULJAHR=" + oldYear;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                Integer idKriterium = set.getInt(1);
                Integer idLernbereich = set.getInt(2);
                String kriteriumtext = set.getString(3);
                Integer schuljahr = set.getInt(4);
                String sql2 = "insert into KRITERIUM values("
                        + Integer.toString(idKriterium + 10000) + ","
                        + Integer.toString(idLernbereich + 10000) + ",'"
                        + kriteriumtext + "',"
                        + Integer.toString(newYear) + ")";
                setInput.add(sql2);
            }
            for (String insertCmd : setInput) {
                // logger.fine(insertCmd);
                statement.executeUpdate(insertCmd);
            }
        }

    }

    /**
     * *
     * liefert eine Liste aller SchuelerID, die im angegebenen Schuljahr sind
     *
     * @return
     */
    public ArrayList<Integer> getIDSchueler(Integer schuljahr) throws SQLException {
        ArrayList<Integer> result = new ArrayList<Integer>();

        try (Statement statement = con.createStatement()) {
            String sql = "select ID_SCHUELER from SCHUELER where SCHULJAHR=" + schuljahr;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                result.add(set.getInt(1));
            }
        }
        return result;
    }

//    /**
//     * der Primary Key des Schuelers wird erzeugt aus dem Hashcode aus
//     * NameVornameGebDatumSchuljahr. Das Datum im Format yyyy-MM-dd.
//     * Gleichzeitig werden zwei Zeugnisse erzeugt. Für jedes Halbjahr eines
//     *
//     * @param values Die Werte zum Anlegen eines Schuelers in der Reihen folge
//     * sder Spalten der Tabelle SCHUELER
//     * @return 
//     * @throws SQLException
//     */
//    public Boolean insertPuple(String[] values) throws SQLException {
//        Boolean retVal=true;
//        
//        try (Statement statement = con.createStatement()) {
//
//            String sql = "insert into SCHUELER values(" + values[0]
//                    + ",'" + values[1]
//                    + "', '" + values[2]
//                    + "', '" + values[3]
//                    + "', '" + values[4]
//                    + "', '" + values[5]
//                    + "', " + values[6] + ")";
//
//            logger.fine(sql);
//            statement.executeUpdate(sql);
//            int idZeugnis = getIdZeugnis(Integer.parseInt(values[0]), 1);
//            sql = "insert into ZEUGNIS (ID_ZEUGNIS, ID_SCHUELER, NOTE_ARBEIT, NOTE_SOZIAL, FEHLTAGE, FEHLTAGEOHNE, ENTWICKLUNG, BEMERKUNG, HALBJAHR, SCHULJAHR) values(" + idZeugnis
//                    + ", " + values[0]
//                    + ", " + 0
//                    + ", " + 0
//                    + ", " + 0
//                    + ", " + 0
//                    + ", " + "' '"
//                    + ", " + "' '"
//                    + ", " + 1
//                    + ", " + values[6]
//                    + ")";
//
//            logger.fine(sql);
//            statement.executeUpdate(sql);
//            insertKriteriumslisteAll(idZeugnis);
//            // logger.fine("Kriterien eingefügt");
//
//            idZeugnis = getIdZeugnis(Integer.parseInt(values[0]), 2);
//            sql = "insert into ZEUGNIS (ID_ZEUGNIS, ID_SCHUELER, NOTE_ARBEIT, NOTE_SOZIAL, FEHLTAGE, FEHLTAGEOHNE, ENTWICKLUNG, BEMERKUNG, HALBJAHR, SCHULJAHR) values(" + idZeugnis
//                    + ", " + values[0]
//                    + ", " + 0
//                    + ", " + 0
//                    + ", " + 0
//                    + ", " + 0
//                    + ", " + "' '"
//                    + ", " + "' '"
//                    + ", " + 2
//                    + ", " + values[6]
//                    + ")";
//
//            // logger.fine(sql);
//            statement.executeUpdate(sql);
//            // Kriteriumsliste aufbauen...
//            insertKriteriumslisteAll(idZeugnis);
//            // logger.fine("Kriterien eingefügt");
//        }
//        catch(SQLException e){
//            if(e.getSQLState().equals(23505)){
//                retVal=false;
//            }
//        }
//        return retVal;
//    }

    /**
     * Zum Update eines Zeugnisses. Es wird ein String[11] erwartet. Die Felder
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
                sql += "ENTWICKLUNG = '" + values[6].substring(0,Math.min(values[6].length(),3000)) + "', ";
            }
            if (values[7] != null) {
                sql += "BEMERKUNG = '" + values[7].substring(0,Math.min(values[7].length(),1500)) + "', ";
            }
            if (values[8] != null) {
                sql += "HALBJAHR = " + values[8] + ", ";
            }
            if (values[9] != null) {
                sql += "SCHULJAHR = " + values[9] + ", ";
            }
            if (values[10] != null) {
                sql += "TEXT_ARBEIT = '" + values[10].substring(0,Math.min(values[10].length(),500)) + "', ";
            }
            if (values[11] != null) {
                sql += "TEXT_SOZIAL = '" + values[11].substring(0,Math.min(values[11].length(),500)) + "', ";
            }

            sql = (sql.substring(0, sql.length() - 2)) + " where ID_ZEUGNIS = " + values[0];
            // logger.fine(sql);
            statement.executeUpdate(sql);
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex, "SQL-Fehler", JOptionPane.ERROR_MESSAGE);
        }

    }

//    /**
//     * Gibt den Inhalt einer Zeile der Tabelle Zeugnis als String[] zurueck. Die
//     * Reihenfolge im Array ist die gleich wie die der Spalten in der Tabelle.
//     *
//     * @param idZeugnis IDZeugnis
//     * @return die Daten der abgefragten Zeile oder NULL wenn kein Ergebnis
//     * gefunden wird.
//     * @throws java.sql.SQLException
//     */
//    public String[] fetchZeugnis(int idZeugnis) throws SQLException {
//        String[] result = new String[9];
//
//        try (Statement statement = con.createStatement()) {
//            String sql = "select * from ZEUGNIS where ID_ZEUGNIS = " + idZeugnis;
//            // logger.fine(sql);
//            ResultSet set = statement.executeQuery(sql);
//
//            if (set.next()) {
//
//                for (int i = 0; i < result.length; i++) {
//                    result[i] = set.getNString(i + 1);
//                }
//
//                return result;
//            } else {
//                return null;
//            }
//
//        }
//
//    }

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
            // logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            if (set.next()) {

                for (int i = 0; i < result.length; i++) {
                    result[i] = set.getString(i + 1);
                }

                return result;
            } else {
                return null;
            }

        }

    }

    /**
     * *
     * prüft, ob Datenbanken existieren Exception bei Zugriff auf DB ergibt
     * false
     *
     * @return
     * @throws SQLException
     */
    public Boolean existTBZeugnis() throws SQLException {
        try (Statement statement = con.createStatement()) {
            String sql = "SELECT * FROM ZEUGNIS.ZEUGNIS";
            // logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private Integer getKlassenstufe(Integer zid) throws SQLException {
        Integer retVal = 0;
        String klasse = "";

        try (Statement statement = con.createStatement()) {
            String sql = "select KLASSE from SCHUELER,ZEUGNIS where ZEUGNIS.ID_ZEUGNIS = " + zid + " AND ZEUGNIS.ID_SCHUELER=SCHUELER.ID_SCHUELER";
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                klasse = set.getString(1);
            }
        }
        return Integer.parseInt(klasse.substring(0, 1));
    }

    /**
     * *
     * Fügt für ein übergebenes Zeugnis alle zugehörigen Kriterien_IDs in die
     * Datenbank mit der Bewertung 0 ein
     *
     * @param zid
     */
    public void insertKriteriumslisteAll(Integer zid) throws SQLException {
        Integer bewertung = 0;
        // Klassenstufe ermitteln
        Integer ks = this.getKlassenstufe(zid);

        try {
            ArrayList<Integer> lbid = new ArrayList<Integer>(); // ID_Lernbereiche
            lbid = this.getID_Lernbereiche(this.getZeugnisSchuljahr(zid), ks);

            for (Integer l : lbid) {
                ArrayList<Integer> kid = new ArrayList<Integer>();  // ID_Kriterium
                kid = this.getID_Kriterien(l);
                for (Integer k : kid) {
                    String[] s = {Integer.toString(zid), Integer.toString(k), Integer.toString(bewertung)};
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

    /***
     * liefert eine Liste der Zeugnis-Ids für das aktuelle Klasse/Halbjahr/Schuljahr
     * @return 
     */
    public ArrayList<Integer> listIdSchueler() throws SQLException{
        ArrayList<Integer> result = new ArrayList<>();

        try (Statement statement = con.createStatement()) {
            String sql = "select ZEUGNIS.ID_SCHUELER from SCHUELER,ZEUGNIS where SCHUELER.SCHULJAHR=" + Gui.getSYear() + " AND ZEUGNIS.HALBJAHR="+Gui.getHYear()+" AND KLASSE like '"+Gui.getSClass() + "' AND SCHUELER.ID_SCHUELER=ZEUGNIS.ID_SCHUELER";
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                result.add(set.getInt(1));
            }
        }
        return result;
     
    }
    
//    /**
//     * Update eines Schülers. Ausserdem muessen in der Zeugnistabelle gggf die
//     * ZeugnisIs und die SchuelerId geaendert werden.
//     *
//     * @param values Eine String[] Array mit der Reihenfolge den Werten aller
//     * Spalten der Tabelle SCHUELER
//     * @param idSchueler Die Id des zu loeschenden Datensatzes.
//     * @throws SQLException
//     */
//    public void updatePuple(String[] values, String idSchueler) throws SQLException {
//        String[] puple = fetchPupil(Integer.parseInt(idSchueler));
//        String sqlUpdateSchueler = "update SCHUELER set ";
//        try (Statement statement = con.createStatement()) {
//
//            if (!puple[0].equals(values[0])) {
//                sqlUpdateSchueler += "ID_SCHUELER=" + values[0] + ", ";
//            }
//
//            if (!puple[0].equals(values[1])) {
//                sqlUpdateSchueler += "NAME='" + values[1] + "', ";
//            }
//
//            if (!puple[0].equals(values[2])) {
//                sqlUpdateSchueler += "VORNAME='" + values[2] + "', ";
//            }
//
//            if (!puple[0].equals(values[3])) {
//                sqlUpdateSchueler += "GEBDATUM='" + values[3] + "', ";
//            }
//
//            if (!puple[0].equals(values[4])) {
//                sqlUpdateSchueler += "GEBORT='" + values[4] + "', ";
//            }
//
//            if (!puple[0].equals(values[5])) {
//                sqlUpdateSchueler += "KLASSE='" + values[5] + "', ";
//            }
//
//            if (!puple[0].equals(values[6])) {
//                sqlUpdateSchueler += "SCHULJAHR=" + values[6] + ", ";
//            }
//
//            sqlUpdateSchueler = sqlUpdateSchueler.substring(0, sqlUpdateSchueler.length() - 2);
//            sqlUpdateSchueler += " where ID_SCHUELER=" + idSchueler;
//          
//           // Aenderungen in Zeugnis und Kriteriumsliste durchführen wenn sich die ID_SCHUELER geaendertr hat
//            if (!idSchueler.equals(values[0])) {
//                 
// //               int idZeugnis = getIdZeugnis(Integer.parseInt(values[0]), 1);
//                // getIdZeugnisMemory holt die Daten aus den Parametern, nicht aus der DB 
//                int idZeugnis = getIdZeugnisMemory(values[1],values[2],values[3],values[6],"1");
//                int idKriteriumsliste = getIdZeugnis(Integer.parseInt(idSchueler), 1);
//                String sql = "update KRITERIUMSLISTE set ID_KRITERIUMSLISTE=" + idZeugnis
//                        + " where ID_KRITERIUMSLISTE=" + idKriteriumsliste;
//                logger.fine(sql);
//                statement.executeUpdate(sql);
//                
//                sql = "update ZEUGNIS set ID_ZEUGNIS=" + idZeugnis
//                        + ", ID_SCHUELER=" + values[0]
//                        + " where Id_SCHUELER=" + idSchueler
//                        + " and HALBJAHR=" + 1;
//                logger.fine(sql);
//                statement.executeUpdate(sql);
//                
//                
// //               idZeugnis = getIdZeugnis(Integer.parseInt(values[0]), 2);
//                idZeugnis = getIdZeugnisMemory(values[1],values[2],values[3],values[6],"2");
//                idKriteriumsliste = getIdZeugnis(Integer.parseInt(idSchueler), 2);
//                sql = "update KRITERIUMSLISTE set ID_KRITERIUMSLISTE=" + idZeugnis
//                        + " where ID_KRITERIUMSLISTE=" + idKriteriumsliste;
//                logger.fine(sql);
//                statement.executeUpdate(sql);
//
//                sql = "update ZEUGNIS set ID_ZEUGNIS=" + idZeugnis
//                        + ", ID_SCHUELER=" + values[0]
//                        + " where Id_SCHUELER=" + idSchueler
//                        + " and HALBJAHR=" + 2;
//                logger.fine(sql);
//                statement.executeUpdate(sql);
//
//            }
//
//            logger.fine(sqlUpdateSchueler);
//            statement.executeUpdate(sqlUpdateSchueler);
//        }
//
//    }

//    /**
//     * Löschen eines Schülers mit den dazugehoerigen Zeugnissen.
//     *
//     * @param idschueler Die Id des zu loeschenden Datensatzes.
//     * @throws SQLException
//     */
//    public void deletePuple(String idSchueler) throws SQLException {
//        Integer idZeugnis1;
//        Integer idZeugnis2;
//
//        idZeugnis1 = this._getIdZeugnis(Integer.parseInt(idSchueler), 1);
//        idZeugnis2 = this._getIdZeugnis(Integer.parseInt(idSchueler), 2);
//
//        try (Statement statement = con.createStatement()) {
//            // Erst Kriterienlisten löschen
//            String sql = "delete from KRITERIUMSLISTE where ID_KRITERIUMSLISTE = " + idZeugnis1 + " OR ID_KRITERIUMSLISTE = " + idZeugnis2;
//            // logger.fine(sql);
//            statement.executeUpdate(sql);
//
//            // Dann Zeugnisse löschen
//            sql = "delete from ZEUGNIS where ID_ZEUGNIS = " + idZeugnis1 + " OR ID_ZEUGNIS = " + idZeugnis2;
//            // logger.fine(sql);
//            statement.executeUpdate(sql);
//
//            // Dann Schueler löschen
//            sql = "delete from SCHUELER where ID_Schueler = " + idSchueler;
//            // logger.fine(sql);
//            statement.executeUpdate(sql);
//        }
//
//    }

    /**
     * Gibt die Daten aller Schueler eines Jahrgangs und einer Klasse zurueck.
     *
     * @param sYear Das Schuljahr
     * @param sClass Die Klasse
     * @return Array mit den Daten der Schueler
     * @throws SQLException
     */
    public ArrayList[] fetchPupils(int sYear, String sClass) throws SQLException {
        ArrayList<ArrayList> result = new ArrayList<>();

        try (Statement statement = con.createStatement()) {

            String sql = "select ID_SCHUELER, NAME, VORNAME, GEBDATUM, GEBORT from SCHUELER where KLASSE = '" + sClass + "' and SCHULJAHR = " + sYear;
            // logger.fine(sql);
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

//    /**
//     * Gibt die Daten eines Schuelers zurueck. Oder null wenn kein Schueler
//     * unter der uebergebenen ID zu finden ist.
//     *
//     * @param idSchueler Die Schueler Id
//     * @return Die Daten des Schuelers oder null wenn der Schueler nicht
//     * gefunden wird.
//     * @throws SQLException
//     */
//    public String[] fetchPupil(int idSchueler) throws SQLException {
//        String[] result = new String[7];
//
//        try (Statement statement = con.createStatement()) {
//            String sql = "select * from SCHUELER where ID_SCHUELER = " + idSchueler;
//            logger.fine(sql);
//            ResultSet set = statement.executeQuery(sql);
//
//            if (set.next()) {
//                result[0] = set.getString(1);
//                result[1] = set.getString(2);
//                result[2] = set.getString(3);
//                result[3] = set.getString(4);
//                result[4] = set.getString(5);
//                result[5] = set.getString(6);
//                result[6] = set.getString(7);
//                return result;
//            } else {
//                return null;
//            }
//
//        }
//
//    }

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
            // logger.fine(sql);
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
    
    public void fillKriteriumTable(JTable table, String sYear, String sClass) throws SQLException{
        
         try (Statement statement = con.createStatement()) {
            String sql = "select KRITERIUM.SCHULJAHR, KRITERIUM.ID_LERNBEREICH, KRITERIUM.ID_KRITERIUM, KRITERIUM.KRITERIUMTEXT from KRITERIUM,LERNBEREICH where KRITERIUM.SCHULJAHR = " 
                    + sYear +" AND KRITERIUM.ID_LERNBEREICH=LERNBEREICH.ID_LERNBEREICH AND (LERNBEREICH.KLASSENSTUFE=0 OR LERNBEREICH.KLASSENSTUFE="+ sClass.substring(0, 1)+") order by KRITERIUM.ID_KRITERIUM asc";
            // logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            while (set.next()) {
                Object[] row = new Object[4];
                row[0] = set.getString(1);
                row[1] = set.getString(2);
                row[2] = set.getString(3);
                row[3] = set.getString(4);
                model.addRow(row);
            }
        }
    }

    public void fillLernbereichTable(JTable table, String sYear, String sClass) throws SQLException{
        
         try (Statement statement = con.createStatement()) {
            String sql = "select SCHULJAHR, KLASSENSTUFE, ID_LERNBEREICH, LERNBEREICH from LERNBEREICH where SCHULJAHR = " 
                    + sYear +" AND (KLASSENSTUFE=0 OR KLASSENSTUFE="+ sClass.substring(0, 1)+") order by ID_LERNBEREICH asc";
            // logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            while (set.next()) {
                Object[] row = new Object[4];
                row[0] = set.getString(1);
                row[1] = set.getString(2);
                row[2] = set.getString(3);
                row[3] = set.getString(4);
                model.addRow(row);
            }
        }
    }
    
    
//    /**
//     * Prueft auf die Existenz eines Schuelers ueber den PrimaryKey.
//     *
//     * @param idSchueler
//     * @return
//     * @throws SQLException
//     */
//    public boolean pupleExist(int idSchueler) throws SQLException {
//
//        try (Statement statement = con.createStatement()) {
//            String sql = "select * from SCHUELER where ID_SCHUELER = " + idSchueler;
//            // logger.fine(sql);
//            ResultSet set = statement.executeQuery(sql);
//
//            return set.next();
//        }
//    }

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

//    /**
//     * liefert das Schuljahr aus der Schuelertabelle für die idSchueler
//     *
//     * @param idSCHUELER
//     * @return
//     * @throws SQLException
//     */
//    public String getSchuelerSchuljahr(int idSCHUELER) throws SQLException {
//        String name = "";
//        try (Statement statement = con.createStatement()) {
//            String sql = "select SCHULJAHR from SCHUELER where ID_SCHUELER = " + idSCHUELER;
//            //logger.fine(sql);
//            ResultSet set = statement.executeQuery(sql);
//            while (set.next()) {
//                name = set.getString(1);
//            }
//        }
//        return name;
//    }

    /**
     * liefert das Schuljahr aus der Zeugnistabelle für die idZeugnis
     *
     * @param idZEUGNIS
     * @return
     * @throws SQLException
     */
    public Integer getZeugnisSchuljahr(int idZEUGNIS) throws SQLException {
        Integer schuljahr = 0;
        try (Statement statement = con.createStatement()) {
            String sql = "select SCHULJAHR from ZEUGNIS where ID_ZEUGNIS = " + idZEUGNIS;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                schuljahr = set.getInt(1);
            }
        }
        return schuljahr;
    }

    /**
     * liefert den Nachnamen aus der Schuelertabelle für die idSchueler
     *
     * @param idSCHUELER
     * @return
     * @throws SQLException
     */
    public String getSchuelerName(int idSCHUELER) throws SQLException {
        String name = "Name";
        // logger.fine(String.valueOf(idSCHUELER));

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
     *
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
     *
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
     *
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
     * liefert den häufigsten Geburtsort für die Liste der idSchueler
     *
     * @param liste
     * @return
     * @throws SQLException
     */
    public String getSchuelerGebOrt(ArrayList<Integer> liste) throws SQLException {
        String gebort = "";
        Map<String, Integer> locations = new HashMap<>();
        int max=-1;
        
        // Bei gleicher Häufigkeit zählt der letzte Eintrag
        for (Integer idSchueler : liste) {
            String ort="";
            try (Statement statement = con.createStatement()) {
                String sql = "select GEBORT from SCHUELER where ID_SCHUELER = " + idSchueler;
                //logger.fine(sql);
                ResultSet set = statement.executeQuery(sql);
                while (set.next()) {
                    ort = set.getString(1);
                }
            }
            if( !locations.containsKey(ort)){    
                locations.put(ort, 0);       // Ort eintragen mit zaehler=0
                if(max<0){
                    max=0;
                    gebort = ort;           
                }
            }
            else {
                int count = locations.get(ort) + 1;
                locations.put(ort, count);    // Zähler für Ort inkrementieren
                if(count>max){
                    max=count;
                    gebort = ort;
                }
            }
        }
        return gebort;
    }

    public String getSchuelerKlasse(int idSCHUELER) throws SQLException {
        String klasse = "";
        try (Statement statement = con.createStatement()) {
            String sql = "select KLASSE from SCHUELER where ID_SCHUELER = " + idSCHUELER;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                klasse = set.getString(1);
            }
        }
        return klasse;
    }

    /**
     * liefert die Fehltage aus dem Zeugnis
     *
     * @param zid
     * @return
     * @throws SQLException
     */
    public Integer getFehltage(Integer zid) throws SQLException {
        Integer retVal = 0;
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
     * liefert die mittleren Fehltage für Statistik
     *
     * @param liste
     * @return
     * @throws SQLException
     */
    public Integer getFehltage(ArrayList<Integer> liste) throws SQLException {
        Integer retVal = 0;
        Integer count =0;

        for (Integer id : liste) {
            Integer zid= this._getIdZeugnis(id, Gui.getHYear());
            Integer fehl=0;
            try (Statement statement = con.createStatement()) {
                String sql = "select FEHLTAGE from ZEUGNIS where ID_ZEUGNIS=" + zid;
                //logger.fine(sql);
                ResultSet set = statement.executeQuery(sql);
                while (set.next()) {
                    fehl = set.getInt(1);
                    count++;
                    retVal+=fehl;
                }
            }
        }
        retVal = (Integer)Math.round((float)retVal/count);
        return retVal;
    }

    /**
     * liefert die Fehltage ohne Entsch aus dem Zeugnis
     *
     * @param zid
     * @return
     * @throws SQLException
     */
    public Integer getFehltageOhne(Integer zid) throws SQLException {
        Integer retVal = 0;
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
     * liefert die mittleren Fehltage ohne Entsch für Statistik
     *
     * @param liste
     * @return
     * @throws SQLException
     */
    public Integer getFehltageOhne(ArrayList<Integer> liste) throws SQLException {
        Integer retVal = 0;
        Integer count = 0;

        for (Integer id : liste) {
            Integer zid= this._getIdZeugnis(id, Gui.getHYear());
            Integer fehl=0;
            try (Statement statement = con.createStatement()) {
                String sql = "select FEHLTAGEOHNE from ZEUGNIS where ID_ZEUGNIS=" + zid;
                //logger.fine(sql);
                ResultSet set = statement.executeQuery(sql);

                while (set.next()) {
                    fehl = set.getInt(1);
                    count++;
                    retVal+=fehl;
                }
            }
        }
        retVal = (Integer) Math.round((float) retVal / count);
        return retVal;
    }

    /**
     * *
     * Liefert die Lernentwicklung zurück
     *
     * @param zid
     * @return
     * @throws SQLException
     */
    public String getLernentwicklung(Integer zid) throws SQLException {
        String retVal = "";
        try (Statement statement = con.createStatement()) {
            String sql = "select ENTWICKLUNG from ZEUGNIS where ID_ZEUGNIS=" + zid;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                retVal = set.getString(1);
            }
        }
        return retVal;
    }

    /**
     * *
     * Liefert die Bemerkung zurück
     *
     * @param zid
     * @return
     * @throws SQLException
     */
    public String getBemerkung(Integer zid) throws SQLException {
        String retVal = "";
        try (Statement statement = con.createStatement()) {
            String sql = "select BEMERKUNG from ZEUGNIS where ID_ZEUGNIS=" + zid;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                retVal = set.getString(1);
            }
        }
        return retVal;
    }

    
//    /**
//     * liefert eine Liste der ID_KRITERIUM des Arbeitsverhaltens für aktuelles
//     * Schuljahr
//     *
//     * @return
//     * @throws SQLException
//     */
//    public ArrayList<Integer> getAVerhaltenID() throws SQLException {
//        ArrayList<Integer> result = new ArrayList<Integer>();
//
//        try (Statement statement = con.createStatement()) {
//            String sql = "select ID_KRITERIUM from KRITERIUM,LERNBEREICH where LERNBEREICH.ID_LERNBEREICH=1 AND KLASSENSTUFE=0 AND LERNBEREICH.SCHULJAHR =" + Gui.getSYear()
//                    + " AND LERNBEREICH.SCHULJAHR=KRITERIUM.SCHULJAHR AND LERNBEREICH.ID_LERNBEREICH=KRITERIUM.ID_LERNBEREICH";
//            //logger.fine(sql);
//            ResultSet set = statement.executeQuery(sql);
//
//            while (set.next()) {
//                result.add(set.getInt(1));
//            }
//        }
//        return result;
//    }

//    /**
//     * liefert eine Liste der ID_KRITERIUM des Sozialverhaltens für aktuelles
//     * Schuljahr
//     *
//     * @return
//     * @throws SQLException
//     */
//    public ArrayList<Integer> getSVerhaltenID() throws SQLException {
//        ArrayList<Integer> result = new ArrayList<Integer>();
//
//        try (Statement statement = con.createStatement()) {
//            String sql = "select ID_KRITERIUM from KRITERIUM,LERNBEREICH where LERNBEREICH.ID_LERNBEREICH=2 AND KLASSENSTUFE=0 AND LERNBEREICH.SCHULJAHR =" + Gui.getSYear()
//                    + " AND LERNBEREICH.SCHULJAHR=KRITERIUM.SCHULJAHR AND LERNBEREICH.ID_LERNBEREICH=KRITERIUM.ID_LERNBEREICH";
//            //logger.fine(sql);
//            ResultSet set = statement.executeQuery(sql);
//
//            while (set.next()) {
//                result.add(set.getInt(1));
//            }
//        }
//        return result;
//    }

    /**
     * liefert die Note (0-5) für das Arbeitsverhalten aus dem Zeugnis
     *
     * @param zid
     * @return
     * @throws SQLException
     */
    public Integer getNoteArbeit(Integer zid) throws SQLException {
        Integer retVal = 0;

        try (Statement statement = con.createStatement()) {
            String sql = "select NOTE_ARBEIT from ZEUGNIS where ID_ZEUGNIS=" + zid;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                retVal = set.getInt(1);
            }
        }
        return retVal;
    }

    /**
     * liefert die mittlere Note (0-5) für das Arbeitsverhalten für Statistik
     *
     * @param liste
     * @return
     * @throws SQLException
     */
    public Integer getNoteArbeit(ArrayList<Integer> liste) throws SQLException {
        Integer retVal = 0;
        Integer count = 0;

        for (Integer id : liste) {
            Integer zid= this._getIdZeugnis(id, Gui.getHYear());
            Integer note=0;

            try (Statement statement = con.createStatement()) {
                String sql = "select NOTE_ARBEIT from ZEUGNIS where ID_ZEUGNIS=" + zid;
                //logger.fine(sql);
                ResultSet set = statement.executeQuery(sql);

                while (set.next()) {
                    note = set.getInt(1);
                    count++;
                    retVal+=note;
                }
            }
        }
        logger.fine(String.valueOf(retVal) + " und count="+String.valueOf(count));
        retVal = (Integer)Math.round((float)retVal/count);
        logger.fine(String.valueOf(retVal) + " und count="+String.valueOf(count));
        return retVal;
    }

    /**
     * liefert den Bemerkungstext für das Arbeitsverhalten aus dem Zeugnis
     *
     * @param zid
     * @return
     * @throws SQLException
     */
    public String getTextArbeit(Integer zid) throws SQLException {
        String retVal = "";

        try (Statement statement = con.createStatement()) {
            String sql = "select TEXT_ARBEIT from ZEUGNIS where ID_ZEUGNIS=" + zid;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                retVal = set.getString(1);
            }
        }
        return retVal;
    }

    /**
     * liefert die Note (0-5) für das Sozialverhalten aus dem Zeugnis
     *
     * @param zid
     * @return
     * @throws SQLException
     */
    public Integer getNoteSozial(Integer zid) throws SQLException {
        Integer retVal = 0;

        try (Statement statement = con.createStatement()) {
            String sql = "select NOTE_SOZIAL from ZEUGNIS where ID_ZEUGNIS=" + zid;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                retVal = set.getInt(1);
            }
        }
        return retVal;
    }

    /**
     * liefert die mittlere Note (0-5) für das Sozialverhalten für Statistik
     *
     * @param liste
     * @return
     * @throws SQLException
     */
    public Integer getNoteSozial(ArrayList<Integer> liste) throws SQLException {
        Integer retVal = 0;
        Integer count = 0;

        for (Integer id : liste) {
            Integer zid= this._getIdZeugnis(id, Gui.getHYear());
            Integer note=0;

            try (Statement statement = con.createStatement()) {
                String sql = "select NOTE_SOZIAL from ZEUGNIS where ID_ZEUGNIS=" + zid;
                //logger.fine(sql);
                ResultSet set = statement.executeQuery(sql);

                while (set.next()) {
                    note = set.getInt(1);
                    count++;
                    retVal+=note;
                }
            }
        }
        logger.fine(String.valueOf(retVal) + " und count="+String.valueOf(count));
        retVal = (Integer)Math.round((float)retVal/count);
        logger.fine(String.valueOf(retVal) + " und count="+String.valueOf(count));
        return retVal;
    }

    /**
     * liefert den Bemerkungstext für das Sozialverhalten aus dem Zeugnis
     *
     * @param zid
     * @return
     * @throws SQLException
     */
    public String getTextSozial(Integer zid) throws SQLException {
        String retVal = "";

        try (Statement statement = con.createStatement()) {
            String sql = "select TEXT_SOZIAL from ZEUGNIS where ID_ZEUGNIS=" + zid;
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                retVal = set.getString(1);
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
    public String getKriteriumText(Integer idKriterium) throws SQLException {
        String retVal = "";
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
     * liefert die Kriterien mit dazugehöriger ID_Kriterium für den übergebenen Lernbereich für die aktuelle
     * Klassenstufe und das aktuelle Schuljahr
     *
     * @param idSCHULER
     * @param lernbereich
     * @return
     * @throws SQLException
     */
    public ArrayList<String[]> getKriterien(int idSCHULER, String lernbereich) throws SQLException {
        ArrayList<String[]> result = new ArrayList<>();

        try (Statement statement = con.createStatement()) {
            String k = Gui.getSClass().substring(0, 1);
            String sql = "select ID_Kriterium, KRITERIUMTEXT from KRITERIUM,LERNBEREICH where LERNBEREICH.LERNBEREICH='" + lernbereich + "' AND KRITERIUM.ID_LERNBEREICH=LERNBEREICH.ID_LERNBEREICH AND (LERNBEREICH.KLASSENSTUFE=" + k + " OR LERNBEREICH.KLASSENSTUFE=0) AND LERNBEREICH.SCHULJAHR =" + Gui.getSYear() + " AND KRITERIUM.SCHULJAHR =" + Gui.getSYear();
            //logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                String[] array = {set.getString(1), set.getString(2)};
                result.add(array);
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
            String sql = "select ID_KRITERIUM from KRITERIUM where KRITERIUM.ID_LERNBEREICH=" + idLernbereich;
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

//    /**
//     * liefert eine Liste der Lernbereiche für das aktuelle Schuljahr und die
//     * aktuelle Klassenstufe
//     *
//     * @param schuljahr
//     * @param klassenstufe
//     * @return result
//     * @throws java.sql.SQLException
//     */
//    public ArrayList<String> getLernbereiche() throws SQLException {
//        ArrayList<String> result = new ArrayList<>();
//        try (Statement statement = con.createStatement()) {
//
//            String sql = "select LERNBEREICH from LERNBEREICH where KLASSENSTUFE="
//                    + Gui.getSClass().substring(0, 1) + " AND SCHULJAHR ="
//                    + Gui.getSYear();
//            ResultSet set = statement.executeQuery(sql);
//
//            while (set.next()) {
//                result.add(set.getString(1));
//            }
//        }
//        return result;
//    }

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
    public ArrayList<Integer> getID_Lernbereiche(Integer schuljahr, Integer klassenstufe) throws SQLException {
        ArrayList<Integer> result = new ArrayList<>();
        String k = Integer.toString(klassenstufe);;
        int y = schuljahr;;

        try (Statement statement = con.createStatement()) {
            String sql = "select ID_LERNBEREICH from LERNBEREICH where (KLASSENSTUFE=" + k + " OR KLASSENSTUFE=0) AND SCHULJAHR =" + y;
            logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                result.add(set.getInt(1));
            }

        }

        return result;
    }

//    /**
//     * Liefert die ID_ZEUGNIS zurück, wenn man die ID_SCHUELER übergibt
//     * Achtung: benutzt die Daten aus der Datenbanktabelle!
//     * @param idSchueler
//     * @param halbjahr
//     * @return
//     * @throws SQLException
//     */
//    public Integer getIdZeugnis(Integer idSchueler, Integer halbjahr) throws SQLException {
//        Integer retVal;
//
//        retVal = (this.getSchuelerName(idSchueler) + this.getSchuelerVorname(idSchueler)
//                + this.getSchuelerGebDatum(idSchueler) + this.getSchuelerSchuljahr(idSchueler) + halbjahr.toString()).hashCode();
//        return retVal;
//    }

//    /***
//     * Liefert die ID_ZEUGNIS zurück, wenn man nicht die DB Tabelle benutzen kann
//     * @param name
//     * @param vorname
//     * @param date
//     * @param jahr
//     * @param halbjahr
//     * @return 
//     */
//    public Integer getIdZeugnisMemory(String name, String vorname, String date, String jahr, String halbjahr){
//        return (name+vorname+date+jahr+halbjahr).hashCode();
//    }
        
    /**
     * liefert ID_Kritierien bei Eingabe eines Lernbereichs zu aktuellem
     * Schuljahr und Klassenstufe
     *
     * @param lernbereich
     * @return
     * @throws SQLException
     */
    public ArrayList<Integer> getID_KriterienFromLernbereich(String lernbereich) throws SQLException {
        ArrayList<Integer> result = new ArrayList<Integer>();

        try (Statement statement = con.createStatement()) {
            String klassenstufe;
            int schuljahr;

            klassenstufe = Gui.getSClass().substring(0, 1);
            schuljahr = Gui.getSYear();

            String sql = "select ID_KRITERIUM from KRITERIUM,LERNBEREICH where LERNBEREICH.LERNBEREICH like '" + lernbereich + "%'"
                    + " AND LERNBEREICH.SCHULJAHR=" + schuljahr
                    + " AND (LERNBEREICH.KLASSENSTUFE=0 OR LERNBEREICH.KLASSENSTUFE =" + klassenstufe + ")"
                    + " AND LERNBEREICH.ID_LERNBEREICH=KRITERIUM.ID_LERNBEREICH "
                    + " AND LERNBEREICH.SCHULJAHR=KRITERIUM.SCHULJAHR";
            logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                result.add(set.getInt(1));
            }
        }
        return result;
    }

    /**
     * Speichert eine Bewertung
     * 
     * @param idKriteriumsliste
     * @param idKriterium
     * @param bewertung
     * @throws SQLException 
     */
    public void updateBewertung(int idKriteriumsliste, int idKriterium, int bewertung) throws SQLException {
         String sql = "update KRITERIUMSLISTE set BEWERTUNG = " + bewertung +
                 " where ID_KRITERIUMSLISTE = " + idKriteriumsliste +
                 " and ID_KRITERIUM = " + idKriterium;
         logger.fine(sql);
         
         try (Statement statement = con.createStatement()) {
             statement.executeUpdate(sql);
         }
    }
    
    public void updateKriteriumDB(JTable table, int row, int col) throws SQLException{
        String year = String.valueOf(table.getValueAt(row, 0));
        String idLernbereich = String.valueOf(table.getValueAt(row, 1));
        String idKriterium = String.valueOf(table.getValueAt(row, 2));
        String value = String.valueOf(table.getValueAt(row, col));
        
        String sql ="update KRITERIUM set KRITERIUMTEXT='"+value+"' WHERE SCHULJAHR="+year+
                " AND ID_LERNBEREICH="+idLernbereich+" AND ID_KRITERIUM="+idKriterium;
        logger.fine(sql+"->"+String.valueOf(row)+" "+String.valueOf(col));
        try (Statement statement = con.createStatement()) {
             statement.executeUpdate(sql);
        }

    }

    /***
     * Löscht Kriterienliste, Zeugnisse und dann den Schüler
     * @param idSchueler
     * @return
     * @throws SQLException 
     */
    public boolean _deleteSchueler(Integer idSchueler) throws SQLException{
        boolean retVal=true;
        Integer idZeugnis1;
        Integer idZeugnis2;

        idZeugnis1 = _getIdZeugnis(idSchueler, 1);
        idZeugnis2 = _getIdZeugnis(idSchueler, 2);

        try (Statement statement = con.createStatement()) {
            // Erst Kriterienlisten löschen
            String sql = "delete from KRITERIUMSLISTE where ID_KRITERIUMSLISTE = " + idZeugnis1 + " OR ID_KRITERIUMSLISTE = " + idZeugnis2;
            logger.fine(sql);
            statement.executeUpdate(sql);

            // Dann Zeugnisse löschen
            sql = "delete from ZEUGNIS where ID_ZEUGNIS = " + idZeugnis1 + " OR ID_ZEUGNIS = " + idZeugnis2;
            logger.fine(sql);
            statement.executeUpdate(sql);

            // Dann Schueler löschen
            sql = "delete from SCHUELER where ID_Schueler = " + idSchueler;
            logger.fine(sql);
            statement.executeUpdate(sql);
        }
        return retVal;
    }

     /**
     * Gibt eine Bewertung in Abhaengigkeit der idKriteriumsliste und der IdKriterium zurueck.
     * 
     * @param idKriteriumsliste
     * @param idKriterium
     * @return
     * @throws SQLException 
     */
    public int getBewertung(int idKriteriumsliste, int idKriterium) throws SQLException {
        String sql = "select BEWERTUNG from KRITERIUMSLISTE where ID_KRITERIUMSLISTE = " + 
                idKriteriumsliste + " and ID_KRITERIUM = " + idKriterium;
        logger.fine(sql);
        int bewertung = 0;
        
        try (Statement statement = con.createStatement()) {
             ResultSet set = statement.executeQuery(sql);
                          
             while(set.next()) {
                 bewertung = set.getInt(1);
             }
        }
        
        return bewertung;
    }
   


    /***
     * Aktualisiert einen Schueler
     * @param values
     * @param idSchueler
     * @return
     * @throws SQLException 
     */
    public boolean _updateSchueler(String[] values, Integer idSchueler) throws SQLException{
        boolean retVal=true;
        String name     = values[0];
        String vorname  = values[1];
        String gebdatum = values[2];
        String gebort   = values[3];
        String klasse   = values[4];
        String schuljahr= values[5];
        String sql = "update SCHUELER set ";
        sql += "NAME='" + name + "', ";
        sql += "VORNAME='" + vorname + "', ";
        sql += "GEBDATUM='" + gebdatum + "', ";
        sql += "GEBORT='" + gebort + "', ";
        sql += "KLASSE='" + klasse + "', ";
        sql += "SCHULJAHR=" + schuljahr;
        sql += " where ID_SCHUELER=" + idSchueler;
        try (Statement statement = con.createStatement()) {
            logger.fine(sql);
            statement.executeUpdate(sql);
        } 
        return retVal;
    }
    
    
    /***
     * _Routine für die nächste Version
     * Fügt einen neuen Schüler in die Schülertabelle ein zu einem angegebenen Schuljahr
     * Speziell zum Anlegen eines neuen Schuljahres
     * @return Rückgabe ist die nächste  zu vergebene SchuelerId im Erfolgsfall,
     *          ansonsten -1;
     */
    public Integer _insertSchueler(String klasse, Integer schuljahr) throws SQLException{
        Integer idSchueler=0;   // Rueckgabewert
        if(schuljahr==0){
            schuljahr=2017;
        }
        if(klasse==null){
            klasse="1a";
        }
        // Neue Klasse bestimmen
        String newKlasse = getNewKlasse(klasse);    // Neue Klasse für Versetzung:
        
        try (Statement statement = con.createStatement()) {

            // Leeren Schueler anlegen...
            String sql = "insert into SCHUELER(NAME,VORNAME,GEBDATUM,GEBORT,KLASSE,SCHULJAHR) values('','', '1999-01-01', '', '"+ klasse+ "', "+schuljahr+")";
            logger.fine(sql);
            
            int rows = statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
            try (ResultSet rs = statement.getGeneratedKeys()) {
                while (rs.next()) {
                    idSchueler = rs.getInt(1);
                }
            }
            logger.fine(idSchueler.toString());
            
            if (idSchueler != 0) {
                int idZeugnis = _getIdZeugnis(idSchueler, 1);
                sql = "insert into ZEUGNIS (ID_ZEUGNIS, ID_SCHUELER, NOTE_ARBEIT, NOTE_SOZIAL, FEHLTAGE, FEHLTAGEOHNE, ENTWICKLUNG, BEMERKUNG, HALBJAHR, SCHULJAHR) values(" 
                        + idZeugnis+ ", " + idSchueler + ", "+0+", "+0+", "+0+", "+0+", "+"''"+", "+"''"+", "+1+", "+schuljahr+")";
                statement.executeUpdate(sql);
                logger.fine("Leeres Zeugnis für Halbjahr 1 eingefügt: "+sql);
                
                insertKriteriumslisteAll(idZeugnis);
                logger.fine("Kriterien für Zeugnis Halbjahr 1 eingefügt");

                idZeugnis = _getIdZeugnis(idSchueler, 2);
                sql = "insert into ZEUGNIS (ID_ZEUGNIS, ID_SCHUELER, NOTE_ARBEIT, NOTE_SOZIAL, FEHLTAGE, FEHLTAGEOHNE, ENTWICKLUNG, BEMERKUNG, HALBJAHR, SCHULJAHR) values(" + idZeugnis
                        + ", " + idSchueler + ", "+0+", "+0+", "+0+", "+0+", "+"''"+", "+"'Versetzt nach Klasse "+ newKlasse +".', "+2+", "+schuljahr+")";
                logger.fine("Leeres Zeugnis für Halbjahr 2 eingefügt: "+sql);
                statement.executeUpdate(sql);

                insertKriteriumslisteAll(idZeugnis);
                logger.fine("Kriterien für Zeugnis Halbjahr 2 eingefügt");
            }
        }
        return idSchueler;
    }

        /***
     * _Routine für die nächste Version
     * Fügt einen neuen Schüler in die Schülertabelle ein
     * @return Rückgabe ist die nächste  zu vergebene SchuelerId im Erfolgsfall,
     *          ansonsten -1;
     */
    public Integer _insertSchueler() throws SQLException{
        Integer idSchueler=0;   // Rueckgabewert
        Integer schuljahr = Gui.getSYear();
        String klasse = Gui.getSClass();
        if(schuljahr==0){
            schuljahr=2017;
        }
        if(klasse==null){
            klasse="1a";
        }
        
        try (Statement statement = con.createStatement()) {

            // Leeren Schueler anlegen...
            String sql = "insert into SCHUELER(NAME,VORNAME,GEBDATUM,GEBORT,KLASSE,SCHULJAHR) values('','', '1999-01-01', '', '"+ klasse+ "', "+schuljahr+")";
            logger.fine(sql);
            
            int rows = statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
            try (ResultSet rs = statement.getGeneratedKeys()) {
                while (rs.next()) {
                    idSchueler = rs.getInt(1);
                }
            }
            logger.fine(idSchueler.toString());
            
            if (idSchueler != 0) {
                int idZeugnis = _getIdZeugnis(idSchueler, 1);
                sql = "insert into ZEUGNIS (ID_ZEUGNIS, ID_SCHUELER, NOTE_ARBEIT, NOTE_SOZIAL, FEHLTAGE, FEHLTAGEOHNE, ENTWICKLUNG, BEMERKUNG, HALBJAHR, SCHULJAHR) values(" 
                        + idZeugnis+ ", " + idSchueler + ", "+0+", "+0+", "+0+", "+0+", "+"''"+", "+"''"+", "+1+", "+schuljahr+")";
                statement.executeUpdate(sql);
                logger.fine("Leeres Zeugnis für Halbjahr 1 eingefügt: "+sql);
                
                insertKriteriumslisteAll(idZeugnis);
                logger.fine("Kriterien für Zeugnis Halbjahr 1 eingefügt");

                idZeugnis = _getIdZeugnis(idSchueler, 2);
                sql = "insert into ZEUGNIS (ID_ZEUGNIS, ID_SCHUELER, NOTE_ARBEIT, NOTE_SOZIAL, FEHLTAGE, FEHLTAGEOHNE, ENTWICKLUNG, BEMERKUNG, HALBJAHR, SCHULJAHR) values(" + idZeugnis
                        + ", " + idSchueler + ", "+0+", "+0+", "+0+", "+0+", "+"''"+", "+"''"+", "+2+", "+schuljahr+")";
                logger.fine("Leeres Zeugnis für Halbjahr 2 eingefügt: "+sql);
                statement.executeUpdate(sql);

                insertKriteriumslisteAll(idZeugnis);
                logger.fine("Kriterien für Zeugnis Halbjahr 2 eingefügt");
            }
        }
        return idSchueler;
    }

    
    /***
     * _Routine für die nächste Version
     * Liefert die idZeugnis für das angegebene Halbjahr zurück
     * @param idSchueler
     * @param halbjahr
     * @return 
     */
    public Integer _getIdZeugnis(Integer idSchueler, Integer halbjahr){
        Integer retVal = 0;
        Integer offset=100000000;
        
        switch (halbjahr) {
            case 1:
                retVal = idSchueler + offset;
                break;
            case 2:
                retVal = idSchueler + offset + offset;
                break;
            default:
                retVal=0;
        }
        return retVal;
    }
    
    /***
     * Ermittelt, ob in der Kriterienliste Bewertungen mit 0 existieren
     * Nur Leere Kriteriumtextzeilen dürfen 0-Werte enthalten
     * Damit kann eine Lämpchen gesteuert werden, das anzeigt, ob das Zeugnis
     * komplett ausgefüllt wurde.
     * @param idZeugnis
     * @return 
     */
    public boolean isZeugnisComplete(Integer idZeugnis) throws SQLException{
        boolean retVal=true;

        try (Statement statement = con.createStatement()) {

            String sql = "select KRITERIUMSLISTE.ID_KRITERIUM, KRITERIUM.KRITERIUMTEXT, KRITERIUMSLISTE.BEWERTUNG "+
                    "from KRITERIUM,KRITERIUMSLISTE where "+
                    "KRITERIUMSLISTE.ID_KRITERIUM=KRITERIUM.ID_KRITERIUM AND KRITERIUMSLISTE.ID_KRITERIUMSLISTE=" + idZeugnis;
            logger.fine(sql);
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                if((!set.getString(2).isEmpty()) && (set.getInt(3)==0)){ // zählen nur Einträge wo Kriteriumtext nicht leer
                    retVal=false;
                    break;
                }
            }
        }
        
        // Hier noch weitere Tests auf Vollständigkeit in der Zeugnistabelle...
        if(this.getNoteArbeit(idZeugnis)==0 || this.getNoteSozial(idZeugnis)==0){
            retVal=false;
        }
        return retVal;
    }
    
    public String asBewertungen(Integer note){
        String retVal="";
        switch(note){
            case 0: retVal="";
            break;
            case 1: retVal="entspricht nicht den Erwartungen.";
            break;
            case 2: retVal="entspricht den Erwartungen mit Einschränkungen.";
            break;
            case 3: retVal="entspricht den Erwartungen.";
            break;
            case 4: retVal="entspricht den Erwartungen in vollem Umfang.";
            break;
            case 5: retVal="verdient besondere Anerkennung.";
            break;
            default: retVal="";
            break;
        }
        return retVal;
    }
}
