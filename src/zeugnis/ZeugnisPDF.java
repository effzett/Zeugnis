/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import com.itextpdf.text.BadElementException;
import java.io.IOException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Image;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.Font;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 *
 * @author internet
 */
public class ZeugnisPDF  {
    private static final Font NORMAL_FONT=new Font(Font.FontFamily.HELVETICA,12,Font.NORMAL);
    private static final Font BIGGER_FONT=new Font(Font.FontFamily.HELVETICA,14,Font.NORMAL);
    private static final Font NAME_FONT=new Font(Font.FontFamily.HELVETICA,26,Font.NORMAL);
    private static final Font SMALL_FONT=new Font(Font.FontFamily.HELVETICA,10,Font.NORMAL);
    private static final Font SMALL2_FONT=new Font(Font.FontFamily.HELVETICA,11,Font.NORMAL);
    private static final Font SMALLITALIC_FONT=new Font(Font.FontFamily.HELVETICA,10,Font.ITALIC);
    private static final Font SMALLITALIC2_FONT=new Font(Font.FontFamily.HELVETICA,11,Font.ITALIC);
    private static final Font SMALLBOLDITALIC_FONT=new Font(Font.FontFamily.HELVETICA,10,Font.BOLDITALIC);    
    private static final Font SMALLBOLDITALIC2_FONT=new Font(Font.FontFamily.HELVETICA,11,Font.BOLDITALIC);    
    private static final Font TINY_FONT=new Font(Font.FontFamily.HELVETICA,9,Font.NORMAL);
    private static final Font TINYITALIC_FONT=new Font(Font.FontFamily.HELVETICA,9,Font.ITALIC);
    private static final Font MICRO_FONT=new Font(Font.FontFamily.HELVETICA,7,Font.NORMAL);    
    private static final Font BIG_FONT=new Font(Font.FontFamily.HELVETICA,36,Font.BOLD);
    private static final Font NORMAL_BOLD_FONT=new Font(Font.FontFamily.HELVETICA,12,Font.BOLD);
    private static final Font SMALL_BOLD_FONT=new Font(Font.FontFamily.HELVETICA,10,Font.BOLD);
    private static final Font NORMAL_FONT_RED=new Font(Font.FontFamily.HELVETICA,12,Font.NORMAL,BaseColor.RED);
    private static Config config = null;
    
    private final static Logger logger = Logger.getLogger(ZeugnisPDF.class.getName());
    
    private static int symbol1;   // das Symbol 0,1,2..  für die x in den Tabellen A+S
    private static int symbol2;   // das Symbol 0,1,2..  für die x in den Tabellen Rest
    private File file;
    private int id;
    private int zid;
    private int schuljahr;
    private int halbjahr;
    private String klasse;
    private String name;
    private String vorname;
    private String gebdatum;
    private String gebort;
    private int fehltage;
    private int fehltageohne;
    private String lernentwicklung;
    private String lernentwicklungTitle;
    private String bemerkungenTitle;
    private String bemerkungen;
    private String currDate;
    private int noteArbeit;
    private int noteSozial;
    private String noteArbeitString;
    private String noteSozialString;
    private String textArbeit;
    private String textSozial;
    
    private String[] asBewertungen = {  "entspricht nicht den Erwartungen",
                                        "entspricht den Erwartungen mit Einschränkungen",
                                        "entspricht den Erwartungen",
                                        "entspricht den Erwartungen in vollem Umfang",
                                        "verdient besondere Anerkennung"
                                        }; 
        
    Hashtable<Integer,Integer> zeugnis;
    
    private ArrayList aVerhalten        = new ArrayList<TableItem>();
    private ArrayList sVerhalten        = new ArrayList<TableItem>();
    private ArrayList sprechenZL        = new ArrayList<TableItem>();
    private ArrayList schreibenZL       = new ArrayList<TableItem>();
    private ArrayList lesenZL           = new ArrayList<TableItem>();
    private ArrayList sprachZL          = new ArrayList<TableItem>();
    private ArrayList zahlenZL          = new ArrayList<TableItem>();
    private ArrayList groessenZL        = new ArrayList<TableItem>();
    private ArrayList raumZL            = new ArrayList<TableItem>();
    private ArrayList sachunterrichtZL  = new ArrayList<TableItem>();  // 1
    private ArrayList musikZL           = new ArrayList<TableItem>();  // 2
    private ArrayList religionZL        = new ArrayList<TableItem>();  // 3
    private ArrayList kunstZL           = new ArrayList<TableItem>();  // 4
    private ArrayList sportZL           = new ArrayList<TableItem>();  // 5
    private ArrayList werkenZL          = new ArrayList<TableItem>();  // 6
    private ArrayList textilZL          = new ArrayList<TableItem>();  // 7
    private ArrayList englischZL        = new ArrayList<TableItem>();  // 8
    
    /**
     * Erzeugt eine Zeugnis für die Statistik
     * @param liste
     * @throws IOException
     * @throws DocumentException
     * @throws SQLException 
     */
    public ZeugnisPDF(ArrayList<Integer> liste) throws IOException, DocumentException, SQLException, ParseException{
        
        SingletonSQLConnector connector = SingletonSQLConnector.getInstance();
        config = Config.getInstance();

        // Liest die Daten zur Wahl der Ankreuzsymbole aus der config.properties
        try{
            symbol1 = Integer.parseInt(config.getProperty("symbol1"));
            symbol2 = Integer.parseInt(config.getProperty("symbol2"));
        }
        catch(NumberFormatException e){
            symbol1=9;
            symbol2=6;
        }
        this.zeugnis = new Hashtable<>();

        for(Integer id: liste){
            
            zid = connector._getIdZeugnis(id,Gui.getHYear());           
        }

        
        //logger.fine("ZeugnisPDF-idSCHUELER->" + String.valueOf(idK));
        //logger.fine("ZeugnisPDF-idZEUGNIS ->" + String.valueOf(zid));

        //Alle nötigen Felder werden ohne Datenbank gefüllt
        currDate = (new SimpleDateFormat("dd.MM.yyyy")).format(new Date());
        name      = config.getProperty("sName", "Mustermann");
        vorname   = config.getProperty("sVorname", "Maxi");
        gebdatum  = currDate;
        gebort    = connector.getSchuelerGebOrt(liste); // häufigster Ort
        schuljahr = Gui.getSYear();
        halbjahr  = Gui.getHYear();
        klasse    = Gui.getSClass();
        fehltage  = connector.getFehltage(liste);
        fehltageohne= connector.getFehltageOhne(liste);
        lernentwicklung = "Dies ist ein Musterzeugnis mit durchschnittlichen Werten!\n\n"+
            "Name: "+name+"\n"+
            "Vorname: "+vorname+"\n"+
            "Geburtsdatum: aktuelles Datum\n"+
            "Geburtsort: der häufigste Geburtsort\n"+
            "Fehltage: arithmetisches Mittel aller Fehltage\n"+
            "Fehltage ohne Entsch.: arithmetisches Mittel aller Fehltage ohne Entschuldigung\n"+
            "Lernentwicklung: Diesen Erklärungstext\n"+
            "Note Arbeitsverhalten: arithmetisches Mittel aller Noten\n"+
            "Note Sozialverhalten: arithmetisches Mittel aller Noten\n"+
            "Bewertungen Lernbereiche: arithmetische Mittel der Bewertungen";
        bemerkungen = "";
        lernentwicklungTitle = "Lernentwicklungsbericht\n\n";
        bemerkungenTitle = "Bemerkungen\n\n";
        noteArbeit = connector.getNoteArbeit(liste);
        noteSozial = connector.getNoteSozial(liste);
        noteArbeitString = connector.asBewertungen(noteArbeit);
        noteSozialString = connector.asBewertungen(noteSozial);

        // Ab hier sollte das Statistikzeugnis stehen...
        if(fehltageohne>fehltage){
            fehltageohne=fehltage;
        }
        
        // Erzeugung des Namens und des Ordners
        String fileName = name+vorname+gebdatum+".pdf"; 
        String dirName = String.valueOf(Gui.getSYear())+String.valueOf(Gui.getHYear())+Gui.getSClass(); 
        File dir = new File("./" + dirName); 
        if(!dir.exists()){
            dir.mkdir();
        }
        file = new File("./" + dirName + "/" + fileName); 
        
        Integer count=0;
   
        
        for (Integer idS : liste) {
                ArrayList aVerhaltenX        = new ArrayList<TableItem>();
                ArrayList sVerhaltenX        = new ArrayList<TableItem>();
                ArrayList sprechenZLX        = new ArrayList<TableItem>();
                ArrayList schreibenZLX       = new ArrayList<TableItem>();
                ArrayList lesenZLX           = new ArrayList<TableItem>();
                ArrayList sprachZLX          = new ArrayList<TableItem>();
                ArrayList zahlenZLX          = new ArrayList<TableItem>();
                ArrayList groessenZLX        = new ArrayList<TableItem>();
                ArrayList raumZLX            = new ArrayList<TableItem>();
                ArrayList sachunterrichtZLX  = new ArrayList<TableItem>();  // 1
                ArrayList musikZLX           = new ArrayList<TableItem>();  // 2
                ArrayList religionZLX        = new ArrayList<TableItem>();  // 3
                ArrayList kunstZLX           = new ArrayList<TableItem>();  // 4
                ArrayList sportZLX           = new ArrayList<TableItem>();  // 5
                ArrayList werkenZLX          = new ArrayList<TableItem>();  // 6
                ArrayList textilZLX          = new ArrayList<TableItem>();  // 7
                ArrayList englischZLX        = new ArrayList<TableItem>();  // 8

            zid = connector._getIdZeugnis(idS,Gui.getHYear());
            // liefert alle im zeugnis abgelegten Kriterien mit Bewertungen
            zeugnis.clear();
            zeugnis = connector.getID_KriterienZeugnis(zid);


            for (Integer idK : connector.getID_KriterienFromLernbereich("Arbeitsverhalten")) {  // holt Reihenfolge
                //logger.fine("ID_KRITERIUM= "+Integer.toString(idK));
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
                aVerhaltenX.add(ti);
                if(count==0){
                    aVerhalten.add(ti);                    
                }
            }
            for (Integer idK : connector.getID_KriterienFromLernbereich("Sozialverhalten")) {  // holt Reihenfolge
                //logger.fine("ID_KRITERIUM= "+Integer.toString(idK));
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
                sVerhaltenX.add(ti);
                if(count==0){
                    sVerhalten.add(ti);                    
                }

            }
            for (Integer idK : connector.getID_KriterienFromLernbereich("Sprechen und Zuhören")) {  // holt Reihenfolge
                //logger.fine("ID_KRITERIUM= "+Integer.toString(idK));
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung((zeugnis.get(idK)==0)?3:zeugnis.get(idK));           // holt Bewertung aus Hashtable
                sprechenZLX.add(ti);
                if(count==0){
                    sprechenZL.add(ti);                    
                }

            }
            for (Integer idK : connector.getID_KriterienFromLernbereich("Schreiben")) {  // holt Reihenfolge
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung((zeugnis.get(idK)==0)?3:zeugnis.get(idK));           // holt Bewertung aus Hashtable
                schreibenZLX.add(ti);
                if(count==0){
                    schreibenZL.add(ti);                    
                }

            }
            for (Integer idK : connector.getID_KriterienFromLernbereich("Lesen")) {  // holt Reihenfolge
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung((zeugnis.get(idK)==0)?3:zeugnis.get(idK));           // holt Bewertung aus Hashtable
                lesenZLX.add(ti);
                if(count==0){
                    lesenZL.add(ti);                    
                }

            }
            for (Integer idK : connector.getID_KriterienFromLernbereich("Sprache")) {  // holt Reihenfolge
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung((zeugnis.get(idK)==0)?3:zeugnis.get(idK));           // holt Bewertung aus Hashtable
                sprachZLX.add(ti);
                if(count==0){
                    sprachZL.add(ti);                    
                }

            }
            for (Integer idK : connector.getID_KriterienFromLernbereich("Zahlen")) {  // holt Reihenfolge
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung((zeugnis.get(idK)==0)?3:zeugnis.get(idK));           // holt Bewertung aus Hashtable
                zahlenZLX.add(ti);
                if(count==0){
                    zahlenZL.add(ti);                    
                }
            }
            for (Integer idK : connector.getID_KriterienFromLernbereich("Größen")) {  // holt Reihenfolge
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung((zeugnis.get(idK)==0)?3:zeugnis.get(idK));           // holt Bewertung aus Hashtable
                groessenZLX.add(ti);
                if(count==0){
                    groessenZL.add(ti);                    
                }
            }
            for (Integer idK : connector.getID_KriterienFromLernbereich("Raum")) {  // holt Reihenfolge
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung((zeugnis.get(idK)==0)?3:zeugnis.get(idK));           // holt Bewertung aus Hashtable
                raumZLX.add(ti);
                if(count==0){
                    raumZL.add(ti);                    
                }
            }
            for (Integer idK : connector.getID_KriterienFromLernbereich("Sachunterricht")) {  // holt Reihenfolge
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung((zeugnis.get(idK)==0)?3:zeugnis.get(idK));           // holt Bewertung aus Hashtable
                sachunterrichtZLX.add(ti);
                if(count==0){
                    sachunterrichtZL.add(ti);                    
                }
            }
            for (Integer idK : connector.getID_KriterienFromLernbereich("Musik")) {  // holt Reihenfolge
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung((zeugnis.get(idK)==0)?3:zeugnis.get(idK));           // holt Bewertung aus Hashtable
                musikZLX.add(ti);
                if(count==0){
                    musikZL.add(ti);                    
                }
            }
            for (Integer idK : connector.getID_KriterienFromLernbereich("Religion")) {  // holt Reihenfolge
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung((zeugnis.get(idK)==0)?3:zeugnis.get(idK));           // holt Bewertung aus Hashtable
                religionZLX.add(ti);
                if(count==0){
                    religionZL.add(ti);                    
                }
            }
            for (Integer idK : connector.getID_KriterienFromLernbereich("Kunst")) {  // holt Reihenfolge
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung((zeugnis.get(idK)==0)?3:zeugnis.get(idK));           // holt Bewertung aus Hashtable
                kunstZLX.add(ti);
                if(count==0){
                    kunstZL.add(ti);                    
                }
            }
            for (Integer idK : connector.getID_KriterienFromLernbereich("Sport")) {  // holt Reihenfolge
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung((zeugnis.get(idK)==0)?3:zeugnis.get(idK));           // holt Bewertung aus Hashtable
                sportZLX.add(ti);
                if(count==0){
                    sportZL.add(ti);                    
                }
            }
            for (Integer idK : connector.getID_KriterienFromLernbereich("Werken")) {  // holt Reihenfolge
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung((zeugnis.get(idK)==0)?3:zeugnis.get(idK));           // holt Bewertung aus Hashtable
                werkenZLX.add(ti);
                if(count==0){
                    werkenZL.add(ti);                    
                }
            }
            for (Integer idK : connector.getID_KriterienFromLernbereich("Textil")) {  // holt Reihenfolge
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung((zeugnis.get(idK)==0)?3:zeugnis.get(idK));           // holt Bewertung aus Hashtable
                textilZLX.add(ti);
                if(count==0){
                    textilZL.add(ti);                    
                }
            }
            for (Integer idK : connector.getID_KriterienFromLernbereich("Englisch")) {  // holt Reihenfolge
                TableItem ti = new TableItem();
                ti.setText(connector.getKriteriumText(idK)); // holt Text
                ti.setBewertung((zeugnis.get(idK)==0)?3:zeugnis.get(idK));           // holt Bewertung aus Hashtable
                englischZLX.add(ti);
                if(count==0){
                    englischZL.add(ti);                    
                }
            }
            // Werte addieren
            // ...
            if (count != 0) {
                aVerhalten = sumArrayList(aVerhalten, aVerhaltenX);
                sVerhalten = sumArrayList(sVerhalten, sVerhaltenX);
                sprechenZL = sumArrayList(sprechenZL, sprechenZLX);
                schreibenZL = sumArrayList(schreibenZL, schreibenZLX);
                lesenZL = sumArrayList(lesenZL, lesenZLX);
                sprachZL = sumArrayList(sprachZL, sprachZLX);
                zahlenZL = sumArrayList(zahlenZL, zahlenZLX);
                groessenZL = sumArrayList(groessenZL, groessenZLX);
                raumZL = sumArrayList(raumZL, raumZLX);
                sachunterrichtZL = sumArrayList(sachunterrichtZL, sachunterrichtZLX);
                musikZL = sumArrayList(musikZL, musikZLX);
                religionZL = sumArrayList(religionZL, religionZLX);
                kunstZL = sumArrayList(kunstZL, kunstZLX);
                sportZL = sumArrayList(sportZL, sportZLX);
                werkenZL = sumArrayList(werkenZL, werkenZLX);
                textilZL = sumArrayList(textilZL, textilZLX);
                englischZL = sumArrayList(englischZL, englischZLX);
            }
            count++;    // Anzahl der Schueler
        } // Schuelerschleife
        // Dividieren
        // ...
        aVerhalten = divArrayList(aVerhalten, count);
        sVerhalten = divArrayList(sVerhalten, count);
        sprechenZL = divArrayList(sprechenZL, count);
        schreibenZL = divArrayList(schreibenZL, count);
        lesenZL = divArrayList(lesenZL, count);
        sprachZL = divArrayList(sprachZL, count);
        zahlenZL = divArrayList(zahlenZL, count);
        groessenZL = divArrayList(groessenZL, count);
        raumZL = divArrayList(raumZL, count);
        sachunterrichtZL = divArrayList(sachunterrichtZL, count);
        musikZL = divArrayList(musikZL, count);
        religionZL = divArrayList(religionZL, count);
        kunstZL = divArrayList(kunstZL, count);
        sportZL = divArrayList(sportZL, count);
        werkenZL = divArrayList(werkenZL, count);
        textilZL = divArrayList(textilZL, count);
        englischZL = divArrayList(englischZL, count);
    }
    
        /**
     * Erzeugt eine Zeugnisklasse und füllt Variablen aus der DB
     * @param idSCHUELER
     * @throws IOException
     * @throws DocumentException
     * @throws SQLException 
     */
    public ZeugnisPDF(int idSCHUELER) throws IOException, DocumentException, SQLException, ParseException{
        
        SingletonSQLConnector connector = SingletonSQLConnector.getInstance();
        config = Config.getInstance();

        // Liest die Daten zur Wahl der Ankreuzsymbole aus der config.properties
        try{
            symbol1 = Integer.parseInt(config.getProperty("symbol1"));
            symbol2 = Integer.parseInt(config.getProperty("symbol2"));
        }
        catch(NumberFormatException e){
            symbol1=9;
            symbol2=6;
        }
        this.zeugnis = new Hashtable<>();

        // Daten aus der DB holen...
        id = idSCHUELER;
        zid = connector._getIdZeugnis(id,Gui.getHYear());
        
        //logger.fine("ZeugnisPDF-idSCHUELER->" + String.valueOf(idK));
        //logger.fine("ZeugnisPDF-idZEUGNIS ->" + String.valueOf(zid));

        //Alle nötigen Felder werden aus der Datenbank gefüllt
        name      = connector.getSchuelerName(id);
        vorname   = connector.getSchuelerVorname(id);
        gebdatum  = convertDate(connector.getSchuelerGebDatum(id));
        gebort    = connector.getSchuelerGebOrt(id);
        currDate = (new SimpleDateFormat("dd.MM.yyyy")).format(new Date());
        schuljahr = Gui.getSYear();
        halbjahr  = Gui.getHYear();
        klasse    = Gui.getSClass();
        fehltage  = connector.getFehltage(zid);
        fehltageohne= connector.getFehltageOhne(zid);
        lernentwicklungTitle = "Lernentwicklungsbericht\n\n";
        bemerkungenTitle = "Bemerkungen\n\n";
        lernentwicklung = connector.getLernentwicklung(zid); 
        bemerkungen = connector.getBemerkung(zid);
        noteArbeit = connector.getNoteArbeit(zid);
        noteSozial = connector.getNoteSozial(zid);
        noteArbeitString = connector.asBewertungen(noteArbeit);
        noteSozialString = connector.asBewertungen(noteSozial);
        textArbeit = connector.getTextArbeit(zid);
        textSozial = connector.getTextSozial(zid);
        
        // liefert alle im zeugnis abgelegten Kriterien mit Bewertungen
        zeugnis = connector.getID_KriterienZeugnis(zid);
        
        if(fehltageohne>fehltage){
            fehltageohne=fehltage;
        }
        
        // Erzeugung des Namens und des Ordners
        String fileName = name+vorname+gebdatum+".pdf"; 
        String dirName = String.valueOf(Gui.getSYear())+String.valueOf(Gui.getHYear())+Gui.getSClass(); 
        File dir = new File("./" + dirName); 
        if(!dir.exists()){
            dir.mkdir();
        }
        file = new File("./" + dirName + "/" + fileName); 
        
        for(Integer idK : connector.getID_KriterienFromLernbereich("Arbeitsverhalten")){  // holt Reihenfolge
            //logger.fine("ID_KRITERIUM= "+Integer.toString(idK));
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            aVerhalten.add(ti);
        }

        for(Integer idK : connector.getID_KriterienFromLernbereich("Sozialverhalten")){  // holt Reihenfolge
            //logger.fine("ID_KRITERIUM= "+Integer.toString(idK));
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            sVerhalten.add(ti);
        }
        
        
        for(Integer idK : connector.getID_KriterienFromLernbereich("Sprechen und Zuhören")){  // holt Reihenfolge
            //logger.fine("ID_KRITERIUM= "+Integer.toString(idK));
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            sprechenZL.add(ti);
        }

        for(Integer idK : connector.getID_KriterienFromLernbereich("Schreiben")){  // holt Reihenfolge
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            schreibenZL.add(ti);
        }

        for(Integer idK : connector.getID_KriterienFromLernbereich("Lesen")){  // holt Reihenfolge
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            lesenZL.add(ti);
        }

        for(Integer idK : connector.getID_KriterienFromLernbereich("Sprache")){  // holt Reihenfolge
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            sprachZL.add(ti);
        }

        for(Integer idK : connector.getID_KriterienFromLernbereich("Zahlen")){  // holt Reihenfolge
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            zahlenZL.add(ti);
        }

        for(Integer idK : connector.getID_KriterienFromLernbereich("Größen")){  // holt Reihenfolge
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            groessenZL.add(ti);
        }

        for(Integer idK : connector.getID_KriterienFromLernbereich("Raum")){  // holt Reihenfolge
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            raumZL.add(ti);
        }

        for(Integer idK : connector.getID_KriterienFromLernbereich("Sachunterricht")){  // holt Reihenfolge
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            sachunterrichtZL.add(ti);
        }

        for(Integer idK : connector.getID_KriterienFromLernbereich("Musik")){  // holt Reihenfolge
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            musikZL.add(ti);
        }

        for(Integer idK : connector.getID_KriterienFromLernbereich("Religion")){  // holt Reihenfolge
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            religionZL.add(ti);
        }

        for(Integer idK : connector.getID_KriterienFromLernbereich("Kunst")){  // holt Reihenfolge
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            kunstZL.add(ti);
        }

        for(Integer idK : connector.getID_KriterienFromLernbereich("Sport")){  // holt Reihenfolge
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            sportZL.add(ti);
        }

        for(Integer idK : connector.getID_KriterienFromLernbereich("Werken")){  // holt Reihenfolge
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            werkenZL.add(ti);
        }

        for(Integer idK : connector.getID_KriterienFromLernbereich("Textil")){  // holt Reihenfolge
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            textilZL.add(ti);
        }

        for(Integer idK : connector.getID_KriterienFromLernbereich("Englisch")){  // holt Reihenfolge
            TableItem ti = new TableItem();
            ti.setText(connector.getKriteriumText(idK)); // holt Text
            ti.setBewertung(zeugnis.get(idK));           // holt Bewertung aus Hashtable
            englischZL.add(ti);
        }
    }

    
    /**
     * Konvertiert das Datum von "yyyy-MM-dd" nach "dd.MM.yyyy"
     * @param oldDate
     * @return
     * @throws ParseException 
     */
    private String convertDate(String oldDate) throws ParseException{
        SimpleDateFormat oldSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = oldSdf.parse(oldDate);
        SimpleDateFormat newSdf = new SimpleDateFormat("dd.MM.yyyy");
        return newSdf.format(date);
    }
    
    /***
     * Erzeugt eine leere Zeile als Abstandshalter
     * @param colspan
     * @param fixedHeight
     * @return 
     */
    private PdfPCell emptyLine(int colspan, float fixedHeight){
        PdfPCell cell = new PdfPCell();
        cell.setColspan(colspan);
        cell.setFixedHeight(fixedHeight);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
        
    /***
     * Wählt ein Symbol aus für die Kreuze in den Tabellen und liefert cell
     * @param symbol
     * @param pad
     * @param hAlign
     * @param vAlign
     * @param border
     * @return
     * @throws IOException
     * @throws BadElementException 
     */
    private PdfPCell checkCross(int symbol,float pad, int hAlign,int vAlign,int border) throws IOException, BadElementException{
        PdfPCell cell;

        URL url1 = this.getClass().getResource("pics/img01.png");
        Image img1 = Image.getInstance(url1);
        img1.scalePercent(4.5f);
        URL url2 = this.getClass().getResource("pics/img02.png");
        Image img2 = Image.getInstance(url2);
        img2.scalePercent(4.5f);
        URL url3 = this.getClass().getResource("pics/img03.png");
        Image img3 = Image.getInstance(url3);
        img3.scalePercent(4.5f);
        URL url4 = this.getClass().getResource("pics/img04.png");
        Image img4 = Image.getInstance(url4);
        img4.scalePercent(4.5f);
        URL url5 = this.getClass().getResource("pics/img05.png");
        Image img5 = Image.getInstance(url5);
        img5.scalePercent(4.5f);
        URL url6 = this.getClass().getResource("pics/img06.png");
        Image img6 = Image.getInstance(url6);
        img6.scalePercent(4.5f);
        URL url7 = this.getClass().getResource("pics/img07.png");
        Image img7 = Image.getInstance(url7);
        img7.scalePercent(4.5f);
        URL url8 = this.getClass().getResource("pics/img08.png");
        Image img8 = Image.getInstance(url8);
        img8.scalePercent(4.5f);
        URL url9 = this.getClass().getResource("pics/img09.png");
        Image img9 = Image.getInstance(url9);
        img9.scalePercent(4.5f);
        URL url10 = this.getClass().getResource("pics/img10.png");
        Image img10 = Image.getInstance(url10);
        img10.scalePercent(4.5f);
        URL url11 = this.getClass().getResource("pics/img11.png");
        Image img11 = Image.getInstance(url11);
        img11.scalePercent(4.5f);
        URL url12 = this.getClass().getResource("pics/img12.png");
        Image img12 = Image.getInstance(url12);
        img12.scalePercent(4.5f);
        URL url13 = this.getClass().getResource("pics/img13.png");
        Image img13 = Image.getInstance(url13);
        img13.scalePercent(4.5f);
        URL url14 = this.getClass().getResource("pics/img14.png");
        Image img14 = Image.getInstance(url14);
        img14.scalePercent(4.5f);
    
        switch (symbol){
            case 1: cell = new PdfPCell(img1);
            break;
            case 2: cell = new PdfPCell(img2);
            break;
            case 3: cell = new PdfPCell(img3);
            break;
            case 4: cell = new PdfPCell(img4);
            break;
            case 5: cell = new PdfPCell(img5);
            break;
            case 6: cell = new PdfPCell(img6);
            break;
            case 7: cell = new PdfPCell(img7);
            break;
            case 8: cell = new PdfPCell(img8);
            break;
            case 9: cell = new PdfPCell(img9);
            break;
            case 10: cell = new PdfPCell(img10);
            break;
            case 11: cell = new PdfPCell(img11);
            break;
            case 12: cell = new PdfPCell(img12);
            break;
            case 13: cell = new PdfPCell(img13);
            break;
            case 14: cell = new PdfPCell(img14);
            break;
            default: cell= new PdfPCell(img1);
            break;
        }
        cell.setPadding(pad);
        cell.setHorizontalAlignment(hAlign);
        cell.setVerticalAlignment(vAlign);
        cell.setBorder(border);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
    
    /***
     * Liefert ein cell mit einem Kreissymbol für den angegebenen Index
     * @param fraction
     * @param pad
     * @param hAlign
     * @param vAlign
     * @param border
     * @return
     * @throws IOException
     * @throws BadElementException 
     */
    private PdfPCell kreisViertel(int fraction,float pad, int hAlign,int vAlign,int border) throws IOException, BadElementException{
        PdfPCell cell;

        URL url0 = this.getClass().getResource("pics/strich0.png");
        Image img0 = Image.getInstance(url0);
        img0.scalePercent(8f);
        URL url1 = this.getClass().getResource("pics/viertel0.png");
        Image img1 = Image.getInstance(url1);
        img1.scalePercent(8f);
        URL url2 = this.getClass().getResource("pics/halb0.png");
        Image img2 = Image.getInstance(url2);
        img2.scalePercent(8f);
        URL url3 = this.getClass().getResource("pics/dreiviertel0.png");
        Image img3 = Image.getInstance(url3);
        img3.scalePercent(8f);
        URL url4 = this.getClass().getResource("pics/voll0.png");
        Image img4 = Image.getInstance(url4);
        img4.scalePercent(8f);

        switch (fraction){
            case 0: cell = new PdfPCell(img0);
            break;
            case 1: cell = new PdfPCell(img1);
            break;
            case 2: cell = new PdfPCell(img2);
            break;
            case 3: cell = new PdfPCell(img3);
            break;
            case 4: cell = new PdfPCell(img4);
            break;
            default: cell= new PdfPCell();
            break;
        }
        cell.setPadding(pad);
        cell.setHorizontalAlignment(hAlign);
        cell.setVerticalAlignment(vAlign);
        cell.setBorder(border);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
    
//    /***
//     * Erzeugt cell für die Header Zeile
//     * @param page
//     * @param vorname
//     * @param name
//     * @param geb
//     * @param currdate
//     * @param colspan
//     * @return 
//     */
//    private PdfPCell header(int page,String vorname,String name,String geb, String currdate, int colspan){
////        PdfPCell retCell = new PdfPCell(new Phrase("Seite "+ String.valueOf(page) +
////                " des Grundschulzeugnisses von " + vorname + " "+ name + " (" + gebdatum + ") " + 
////                " vom " +currDate,SMALL_FONT));
//        PdfPCell retCell = new PdfPCell(new Phrase("Seite "+ String.valueOf(page) +
//                " des Grundschulzeugnisses von " + vorname + " "+ name + ", geboren am " + gebdatum,SMALL_FONT));
//        retCell.setColspan(colspan);
//        retCell.setHorizontalAlignment(Element.ALIGN_LEFT);
//        retCell.setBorder(Rectangle.NO_BORDER);
//        return retCell;
//    }
    
    private PdfPCell title1(String s,int colspan,float minheight){
        PdfPCell retCell = new PdfPCell(new Phrase(s,NORMAL_BOLD_FONT));
        retCell.setColspan(colspan);
        retCell.setFixedHeight(minheight);
        retCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        retCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        retCell.setBorder(Rectangle.NO_BORDER);
        return retCell;
    }
    
    private PdfPCell title2(String s,int colspan,float minheight,float pad){
        PdfPCell retCell = new PdfPCell(new Phrase(s,NORMAL_FONT));        
        retCell.setColspan(colspan);
        retCell.setFixedHeight(50f);
        retCell.setVerticalAlignment(Element.ALIGN_TOP);
        retCell.setPadding(pad);
        retCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return retCell;
    }
    
    /***
     * Erzeugt cell mit Selten/Wechselnd/Überwiegend
     * @param s
     * @param pad
     * @return 
     */
    private PdfPCell seweue(String s, float pad){
        PdfPCell retCell = new PdfPCell(new Phrase(s,TINY_FONT));
        retCell.setVerticalAlignment(Element.ALIGN_TOP);
        retCell.setPadding(pad);
        retCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return retCell;
    }
    
    /***
     * Erzeugt ein table Objekt das Arbeits- und Sozialverhalten
     * @param table
     * @param verhalten
     * @param pad
     * @return
     * @throws IOException
     * @throws BadElementException 
     */
    private PdfPTable asVerhalten(PdfPTable table,ArrayList verhalten,float pad) throws IOException, BadElementException{
        Integer mH = 15;
        for(Integer i=0; i<verhalten.size(); i++){
            PdfPCell cell2;
            cell2 = new PdfPCell(new Phrase( ((TableItem)verhalten.get(i)).getText() ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setMinimumHeight(mH);
            cell2.setPadding(pad);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            PdfPCell selection = new PdfPCell(checkCross(getSymbol1(),pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.BOX));
            PdfPCell cell2bewertungX;
//            cell2bewertungX = new PdfPCell(new Phrase("x" ,TINY_FONT));
            cell2bewertungX = selection;
            //cell2ATitle.setColspan(4);
            cell2bewertungX.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2bewertungX.setMinimumHeight(mH);
            cell2bewertungX.setPadding(pad);
            cell2bewertungX.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            PdfPCell cell2bewertung;
            cell2bewertung = new PdfPCell(new Phrase("" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell2bewertung.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2bewertung.setMinimumHeight(mH);
            cell2bewertung.setPadding(pad);
            cell2bewertung.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
 
            table.addCell(cell2);
            switch ( ( (TableItem) verhalten.get(i)).getBewertung()) {
            case 0: table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    break;                
            case 1: table.addCell(cell2bewertungX);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    break;
            case 2: table.addCell(cell2bewertung);
                    table.addCell(cell2bewertungX);
                    table.addCell(cell2bewertung);;
                    break;
            case 3: table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertungX);
                    break;
            default:table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    break;
            }
         }
        return table;
    }
    
    /***
     * Erzeugt eine Liste mit Kriterien und Bewertungsspalten
     * @param table
     * @param lernbereich
     * @param pad
     * @return
     * @throws IOException
     * @throws BadElementException 
     */
    private PdfPTable lernbereiche(PdfPTable table,ArrayList lernbereich,float pad) throws IOException, BadElementException{
        for(Integer i=0; i<lernbereich.size(); i++){
            PdfPCell cell2;
            cell2 = new PdfPCell(new Phrase( ((TableItem)lernbereich.get(i)).getText() ,TINY_FONT));
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setPadding(pad);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            
            PdfPCell cell2bewertungX;
            PdfPCell selection = new PdfPCell(checkCross(getSymbol2(),pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.BOX));
//            cell2bewertungX = new PdfPCell(new Phrase("x" ,TINY_FONT));
            cell2bewertungX = selection;
            cell2bewertungX.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2bewertungX.setPadding(pad);
            cell2bewertungX.setHorizontalAlignment(Element.ALIGN_CENTER);
            
            PdfPCell cell2bewertung;
            cell2bewertung = new PdfPCell(new Phrase("" ,TINY_FONT));
            cell2bewertung.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2bewertung.setPadding(pad);
            cell2bewertung.setHorizontalAlignment(Element.ALIGN_CENTER);
 
            table.addCell(cell2);
            switch ( ( (TableItem) lernbereich.get(i)).getBewertung()) {
            case 0: table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    break;
            case 4: table.addCell(cell2bewertungX);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    break;
            case 5: table.addCell(cell2bewertung);
                    table.addCell(cell2bewertungX);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);;
                    break;
            case 6: table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertungX);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    break;
            case 7: table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertungX);
                    table.addCell(cell2bewertung);
                    break;
            case 8: table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertungX);
                    break;
            default:table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    table.addCell(cell2bewertung);
                    break;
            }
         }
        return table;
    }


    /***
     * Zeigt die generierte PDF Datei im Standard PDF-Viewer an
     * @throws IOException 
     */
    public void display() throws IOException {
        Desktop desktop = Desktop.getDesktop();
        if (desktop != null && desktop.isSupported(Desktop.Action.OPEN)) {
            desktop.open(file);
        } else {
            System.err.println("PDF-Datei kann nicht angezeigt werden: " + file.getPath());
        }
    }
    
    /**
    * Erzeugt das Zeugnis PDF in einem definierten Ordner
    * @throws IOException
    * @throws DocumentException
    * @throws SQLException 
    */
    public void CreatePDF() throws IOException, DocumentException, SQLException{
        float pad= 6.0f;
        
        // Einige Strings für die Ausgabe...
        String Schuljahr = "Schuljahr " + Integer.toString(schuljahr) + "/" + Integer.toString(schuljahr+1);
        String Halbjahr;
        if(halbjahr==1){
            Halbjahr  = "1. Halbjahr";            
        }
        else{
            Halbjahr  = "1. und 2. Halbjahr";
        }
        String Klasse    = "Klasse "+ klasse;
        

        String Tage      = "Versäumte Unterrichtstage im "+Halbjahr+": "+ String.valueOf(fehltage) +  " davon unentschuldigt: " + String.valueOf(fehltageohne);
        String Unterschriften1 = "________________________________________\n\n Klassenlehrerin / Klassenlehrer";
        String Unterschriften2 = "________________________________________\n\n Schulleiterin / Schulleiter";
        String Unterschriften3 = "________________________________________\n\n Unterschrift einer/eines Erziehungsberechtigten";
        String DatumLine       = "________________________________________\n\n Ausstellungsort und Datum";
        
    //    String Datum = "Datum: " + currDate;
        String Datum = currDate;
        

        String AundS        = "Arbeits- und Sozialverhalten";
        String ATitle       = "Arbeitsverhalten\n\n" + vorname + "...";
        String STitle       = "Sozialverhalten\n\n"  + vorname + "...";
        String Selten       = "selten";
        String Wechselnd    = "wechselnd";
        String Ueberwiegend = "überwiegend";

        String Erklaerungen         = "Erklärungen";
        String BewertungsstufenAS   = "Bewertungsstufen für das Arbeits- und Sozialverhalten:";
        String Symbole              = "Symbolerläuterungen für die Unterrichtsfächer:";
        
        String Sym0 = "Das Thema wurde noch nicht bearbeitet";
        String Sym1 = "Die Kompetenz ist in Ansätzen vorhanden";
        String Sym2 = "Die Kompetenz ist grundlegend gesichert";
        String Sym3 = "Die Kompetenz ist weitgehend gesichert";
        String Sym4 = "Die Kompetenz ist gesichert";
        
        String deutschS         = "Deutsch ";
        String matheS           = "Mathematik ";
        String sachunterrichtS  = "Sachunterricht ";
        String musikS           = "Musik ";
        String religionS        = "Religion ";
        String kunstS           = "Kunst ";
        String sportS           = "Sport ";
        String textilS          = "Textiles Gestalten ";
        String werkenS          = "Werken ";
        String englischS        = "Englisch ";
        
//        String jStufe   = "Jahrgangsstufe "+ Gui.getSClass().substring(0, 1);
        String jStufe   = "";
        String sz       = "Sprechen und Zuhören\n\n" + vorname + "...";
        String sch      = "Schreiben\n\n" + vorname + "...";
        String les      = "Lesen - mit Texten und Medien umgehen\n\n" + vorname + "...";
        String sp       = "Sprache und Sprachgebrauch untersuchen\n\n" + vorname + "...";
        String zo       = "Zahlen und Operationen\n\n" + vorname + "...";
        String gm       = "Größen und Messen\n\n" + vorname + "...";
        String rf       = "Raum und Form\n\n" + vorname + "...";
        String su       = vorname + "...";
        
        // Ausgabedokument erzeugen
        Document doc=new Document(PageSize.A4,50,50,20,30);
        PdfWriter writer = PdfWriter.getInstance(doc,new FileOutputStream(file));

        
        // Für header und footer
        TableHeader event = new TableHeader();
        event.setHeader("");
        writer.setPageEvent(event);
        
        doc.open();
        // Logo
//        URL url = this.getClass().getResource("pics/GSBrelingen.jpg");
        URL url = this.getClass().getResource("pics/GSKopfLogo300.png");
        Image img = Image.getInstance(url);
        double moremargin=doc.getPageSize().getWidth()*0.0;
        float scaler = ((doc.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin()- (float)moremargin) / img.getWidth()) * 100;
        img.scalePercent(scaler*0.7f);
        
        // Seite 1 *************************************************************
        // Tablestruktur aufbauen...
        PdfPTable table1 = new PdfPTable(new float[] { 36, 28, 36 });
        table1.setWidthPercentage(100);
        
        PdfPCell cell1Logo;
        cell1Logo = new PdfPCell(img);
        cell1Logo.setColspan(3);
        cell1Logo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1Logo.setBorder(Rectangle.NO_BORDER);
        
        
        // Adresse
        PdfPCell cell1Adresse;
        cell1Adresse = new PdfPCell(new Phrase("Grundschule Brelingen • Schulstraße 10 • 30900 Wedemark • GS.Brelingen@Wedemark.de",SMALL_FONT));
        cell1Adresse.setColspan(3);
        cell1Adresse.setFixedHeight(30f);
        cell1Adresse.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1Adresse.setBorder(Rectangle.NO_BORDER);
                
        //Zeugnis
        PdfPCell cell1Zeugnis;
        cell1Zeugnis = new PdfPCell(new Phrase("Zeugnis",BIG_FONT));
        cell1Zeugnis.setColspan(3);
        cell1Zeugnis.setFixedHeight(40f);
        cell1Zeugnis.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell1Zeugnis.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1Zeugnis.setBorder(Rectangle.NO_BORDER);
       
        //Schuljahr
        PdfPCell cell1Schuljahr;
        cell1Schuljahr = new PdfPCell(new Phrase(Schuljahr,BIGGER_FONT));
        cell1Schuljahr.setFixedHeight(30f);
        cell1Schuljahr.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell1Schuljahr.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1Schuljahr.setBorder(Rectangle.NO_BORDER);

        //Halbjahr
        PdfPCell cell1Halbjahr;
        cell1Halbjahr = new PdfPCell(new Phrase(Halbjahr,BIGGER_FONT));
        cell1Halbjahr.setFixedHeight(30f);
        cell1Halbjahr.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell1Halbjahr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1Halbjahr.setBorder(Rectangle.NO_BORDER);
        
        //Klasse
        PdfPCell cell1Klasse;
        cell1Klasse = new PdfPCell(new Phrase(Klasse,BIGGER_FONT));
        cell1Klasse.setFixedHeight(30f);
        cell1Klasse.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell1Klasse.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell1Klasse.setBorder(Rectangle.NO_BORDER);

        //Name
        PdfPCell cell1Name;
        cell1Name = new PdfPCell(new Phrase(vorname + " " + name,NAME_FONT));
        cell1Name.setColspan(3);
        cell1Name.setFixedHeight(40f);
        cell1Name.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell1Name.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1Name.setBorder(Rectangle.NO_BORDER);
        
        //Geboren
        PdfPCell cell1Geboren;
        cell1Geboren = new PdfPCell(new Phrase("geboren am "+gebdatum+" in "+gebort,NORMAL_FONT));
        cell1Geboren.setColspan(3);
        cell1Geboren.setFixedHeight(20f);
        cell1Geboren.setVerticalAlignment(Element.ALIGN_TOP);
        cell1Geboren.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1Geboren.setBorder(Rectangle.NO_BORDER);

        //Tage
        PdfPCell cell1Tage;
        cell1Tage = new PdfPCell(new Phrase(Tage,SMALL_FONT));
        cell1Tage.setColspan(3);
        cell1Tage.setFixedHeight(50f);
        cell1Tage.setVerticalAlignment(Element.ALIGN_TOP);
        cell1Tage.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1Tage.setBorder(Rectangle.NO_BORDER);

        //Lernentwicklung
        Paragraph lern = new Paragraph();
        Paragraph bem = new Paragraph();
        PdfPTable table = new PdfPTable(1);
        Phrase lernTitle = new Phrase(lernentwicklungTitle,SMALLBOLDITALIC2_FONT);
        Phrase bemTitle = new Phrase(bemerkungenTitle,SMALLBOLDITALIC2_FONT);
        Phrase lernText = new Phrase(lernentwicklung,SMALL2_FONT);
        Phrase bemText = new Phrase(bemerkungen,SMALL2_FONT); 
        if(lernentwicklung.isEmpty()){
            ;
        }else{
            lern.add(lernTitle);
        }
        if(bemerkungen.isEmpty()){
            ;
        }else{
            bem.add(bemTitle);
        }
        lern.add(lernText);
        bem.add(bemText);
        PdfPCell le = new PdfPCell(lern);
        PdfPCell be = new PdfPCell(bem);
        
        le.setVerticalAlignment(Element.ALIGN_TOP);
        be.setVerticalAlignment(Element.ALIGN_BOTTOM);
        le.setBorder(Rectangle.NO_BORDER);
        be.setBorder(Rectangle.NO_BORDER);
        table.addCell(le);
        table.addCell(be);
        PdfPCell cell1Lernentwicklung;
        cell1Lernentwicklung = new PdfPCell(table);
        cell1Lernentwicklung.setColspan(3);
        cell1Lernentwicklung.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1Lernentwicklung.setFixedHeight(350f);
        cell1Lernentwicklung.setPadding(pad);
//        cell1Lernentwicklung.setBorder(Rectangle.NO_BORDER);
        
        // Unterschriften
        PdfPCell cell1Unterschriften1;
        cell1Unterschriften1 = new PdfPCell(new Phrase(Unterschriften1,MICRO_FONT));
        //cell1Unterschriften.setColspan(3);
        cell1Unterschriften1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1Unterschriften1.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell1Unterschriften1.setFixedHeight(70f);
        cell1Unterschriften1.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell1Empty;
        cell1Empty = new PdfPCell(new Phrase("",SMALL_FONT));
        //cell1Unterschriften.setColspan(3);
        cell1Empty.setFixedHeight(70f);
        cell1Empty.setBorder(Rectangle.NO_BORDER);

        
        PdfPCell cell1Unterschriften2;
        cell1Unterschriften2 = new PdfPCell(new Phrase(Unterschriften2,MICRO_FONT));
        //cell1Unterschriften2.setColspan(2);
        cell1Unterschriften2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1Unterschriften2.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell1Unterschriften2.setFixedHeight(70f);
        cell1Unterschriften2.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell1Datum;
        if(Gui.isOverwriteDateSelected()){
            if(Gui.getOverwriteDate().isEmpty()){
                Datum = "     ......................";
            }
            else{
                Datum = Gui.getOverwriteDate();
            }
        }
        Phrase p1 = new Phrase("Brelingen, den "+Datum+"\n",SMALL_FONT);
        Phrase p2 = new Phrase(DatumLine,MICRO_FONT);
        Paragraph par1 = new Paragraph(p1);
        par1.add(p2);
        cell1Datum = new PdfPCell(par1);
        //cell1Datum.addElement(p1);
        //cell1Datum.addElement(p2);
        //cell1Unterschriften.setColspan(3);
        cell1Datum.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1Datum.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell1Datum.setFixedHeight(60f);
        cell1Datum.setBorder(Rectangle.NO_BORDER);

        
        PdfPCell cell1Gesehen;
        cell1Gesehen = new PdfPCell(new Phrase("",NORMAL_FONT));
        cell1Gesehen.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell1Gesehen.setVerticalAlignment(Element.ALIGN_CENTER);        
        //cell1Unterschriften.setColspan(3);
        cell1Gesehen.setFixedHeight(60f);
        cell1Gesehen.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell1Unterschriften3;
        Phrase p3 = new Phrase("gesehen:                                             \u00a0\n",TINY_FONT);
        Phrase p4 = new Phrase(Unterschriften3,MICRO_FONT);
        Paragraph par2 = new Paragraph(p3);
        par2.add(p4);
        cell1Unterschriften3 = new PdfPCell(par2);
        //cell1Unterschriften3.setColspan(2);
        cell1Unterschriften3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1Unterschriften3.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell1Unterschriften3.setFixedHeight(60f);
        cell1Unterschriften3.setBorder(Rectangle.NO_BORDER);

        
        table1.addCell(cell1Logo);
        table1.addCell(cell1Adresse);
        table1.addCell(cell1Zeugnis);
        table1.addCell(cell1Schuljahr);
        table1.addCell(cell1Halbjahr);
        table1.addCell(cell1Klasse);
        table1.addCell(cell1Name);
        table1.addCell(cell1Geboren);
        table1.addCell(cell1Tage);
        table1.addCell(cell1Lernentwicklung);
        table1.addCell(cell1Unterschriften1);
        table1.addCell(cell1Empty);
        table1.addCell(cell1Unterschriften2);
        table1.addCell(cell1Datum);
        table1.addCell(cell1Gesehen);
        table1.addCell(cell1Unterschriften3);
        
        
        doc.add(table1);
      
        doc.newPage();
        
        // Seite 2 *************************************************************
        String head = vorname + " " + name + ", geboren am " + gebdatum;
        event.setHeader(head);
        // Tablestruktur aufbauen...
        pad=3.0f;
        PdfPTable table2 = new PdfPTable(4);
        table2.setWidths(new float[] { 58, 14,14,14 });
        table2.setWidthPercentage(100);
        
        PdfPCell cell2Header = emptyLine(4,10f);
        PdfPCell cell2AundS  = new PdfPCell(title1(AundS,4,35f));
        PdfPCell cell2ATitle = new PdfPCell(title2(ATitle,1,50f,pad));

        PdfPCell cell2Selten = new PdfPCell(seweue(Selten,pad));
        PdfPCell cell2Wechselnd = new PdfPCell(seweue(Wechselnd,pad));
        PdfPCell cell2Ueberwiegend = new PdfPCell(seweue(Ueberwiegend,pad));
        

        table2.addCell(cell2Header);
        table2.addCell(cell2AundS);
        table2.addCell(cell2ATitle);
        table2.addCell(cell2Selten);
        table2.addCell(cell2Wechselnd);
        table2.addCell(cell2Ueberwiegend);
        
        table2 = asVerhalten(table2,aVerhalten,pad);
        
        PdfPCell cell2KommentarA;
        cell2KommentarA = new PdfPCell(new Paragraph(textArbeit,TINYITALIC_FONT));
        cell2KommentarA.setColspan(4);
        cell2KommentarA.setBorder(Rectangle.LEFT | Rectangle.RIGHT );
        cell2KommentarA.setPadding(pad);
        cell2KommentarA.setHorizontalAlignment(Element.ALIGN_LEFT);
        table2.addCell(cell2KommentarA);  
        
        PdfPCell cell2BewertungA;
        if(vorname.endsWith("s") || vorname.endsWith("x") || vorname.endsWith("z")){
            cell2BewertungA = new PdfPCell(new Phrase(vorname + "' Arbeitsverhalten " + noteArbeitString,NORMAL_FONT));            
        }
        else{
            cell2BewertungA = new PdfPCell(new Phrase(vorname + "s Arbeitsverhalten " + noteArbeitString,NORMAL_FONT));                        
        }
        cell2BewertungA.setColspan(4);
        cell2BewertungA.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //cell2BewertungA.setFixedHeight(45f);
        cell2BewertungA.setPadding(pad);
        cell2BewertungA.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2BewertungA.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
        table2.addCell(cell2BewertungA);
        
        table2.addCell(emptyLine(4,15f));
              
        PdfPCell cell2STitle = new PdfPCell(title2(STitle,1,50f,pad));

        table2.addCell(cell2STitle);
        table2.addCell(cell2Selten);
        table2.addCell(cell2Wechselnd);
        table2.addCell(cell2Ueberwiegend);
        
        table2 = asVerhalten(table2,sVerhalten,pad);
      
        PdfPCell cell2KommentarS;
        cell2KommentarS = new PdfPCell(new Paragraph(textSozial,TINYITALIC_FONT));
        cell2KommentarS.setColspan(4);
        cell2KommentarS.setBorder(Rectangle.LEFT | Rectangle.RIGHT );
        cell2KommentarS.setPadding(pad);
        cell2KommentarS.setHorizontalAlignment(Element.ALIGN_LEFT);
        table2.addCell(cell2KommentarS);  
        
        PdfPCell cell2BewertungS;
        if(vorname.endsWith("s") || vorname.endsWith("x") || vorname.endsWith("z")){
            cell2BewertungS = new PdfPCell(new Phrase(vorname + "' Sozialverhalten " + noteSozialString,NORMAL_FONT));            
        }
        else{
            cell2BewertungS = new PdfPCell(new Phrase(vorname + "s Sozialverhalten " + noteSozialString,NORMAL_FONT));                        
        }
        cell2BewertungS.setColspan(4);
        cell2BewertungS.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //cell2BewertungA.setFixedHeight(45f);
        cell2BewertungS.setPadding(pad);
        cell2BewertungS.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2BewertungS.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
        table2.addCell(cell2BewertungS);

        table2.addCell(emptyLine(4,15f));

        // Tablestruktur aufbauen...
        // Legende...
        PdfPTable table2a = new PdfPTable(4);
        table2a.setWidths(new float[] { 3, 50,8,44 });
        table2a.setWidthPercentage(100);

        
        PdfPCell cell2Erklaerungen;
        Chunk chunk1 = new Chunk(Erklaerungen,NORMAL_FONT);
        chunk1.setUnderline(1.5f, -1);
        cell2Erklaerungen = new PdfPCell((new Phrase(chunk1)));
        cell2Erklaerungen.setColspan(4);
        cell2Erklaerungen.setFixedHeight(20f);
        cell2Erklaerungen.setVerticalAlignment(Element.ALIGN_TOP);
        cell2Erklaerungen.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2Erklaerungen.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Erklaerungen);
     
        PdfPCell cell2BewertungsstufenAS;
        cell2BewertungsstufenAS = new PdfPCell(new Phrase(BewertungsstufenAS,SMALL_BOLD_FONT));
        cell2BewertungsstufenAS.setColspan(2);
        cell2BewertungsstufenAS.setVerticalAlignment(Element.ALIGN_TOP);
        cell2BewertungsstufenAS.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2BewertungsstufenAS.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2BewertungsstufenAS);

        PdfPCell cell2Symbole;
        cell2Symbole = new PdfPCell(new Phrase(Symbole,SMALL_BOLD_FONT));
        cell2Symbole.setColspan(2);
        cell2Symbole.setVerticalAlignment(Element.ALIGN_TOP);
        cell2Symbole.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2Symbole.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Symbole);

        float height=20.0f;
        PdfPCell cell2Bewertungsstufen1;
        PdfPCell cell2Item;
        
        cell2Item = new PdfPCell(new Phrase("—"));
        cell2Bewertungsstufen1 = new PdfPCell(new Phrase("\""+asBewertungen[0]+"\"",SMALL_FONT));
        //cell2Bewertungsstufen1.setColspan(2);
        cell2Bewertungsstufen1.setMinimumHeight(height);
        cell2Bewertungsstufen1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Bewertungsstufen1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Bewertungsstufen1.setBorder(Rectangle.NO_BORDER);
        cell2Item.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Item);
        table2a.addCell(cell2Bewertungsstufen1);

//        PdfPCell cell2Sym0img;
//        cell2Sym0img = new PdfPCell(new Phrase("--",SMALL_FONT));
//        //cell2Bewertungsstufen1.setColspan(1);
//        cell2Sym0img.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        cell2Sym0img.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        cell2Sym0img.setBorder(Rectangle.NO_BORDER);
//        table2a.addCell(cell2Sym0img);

        table2a.addCell(kreisViertel(0,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.NO_BORDER));

        PdfPCell cell2Sym0;
        cell2Sym0 = new PdfPCell(new Phrase(Sym0,SMALL_FONT));
        //cell2Bewertungsstufen1.setColspan(1);
        cell2Sym0.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Sym0.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Sym0.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Sym0);

        PdfPCell cell2Bewertungsstufen2;
        cell2Bewertungsstufen2 = new PdfPCell(new Phrase("\""+asBewertungen[1]+"\"",SMALL_FONT));
        //cell2Bewertungsstufen2.setColspan(2);
        cell2Bewertungsstufen2.setMinimumHeight(height);
        cell2Bewertungsstufen2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Bewertungsstufen2.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Bewertungsstufen2.setBorder(Rectangle.NO_BORDER);
        cell2Item.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Item);
        table2a.addCell(cell2Bewertungsstufen2);

        table2a.addCell(kreisViertel(1,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.NO_BORDER));

        PdfPCell cell2Sym1;
        cell2Sym1 = new PdfPCell(new Phrase(Sym1,SMALL_FONT));
        //cell2Bewertungsstufen1.setColspan(1);
        cell2Sym1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Sym1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Sym1.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Sym1);

        PdfPCell cell2Bewertungsstufen3;
        cell2Bewertungsstufen3 = new PdfPCell(new Phrase("\""+asBewertungen[2]+"\"",SMALL_FONT));
        //cell2Bewertungsstufen3.setColspan(2);
        cell2Bewertungsstufen3.setMinimumHeight(height);
        cell2Bewertungsstufen3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Bewertungsstufen3.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Bewertungsstufen3.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Item);
        table2a.addCell(cell2Bewertungsstufen3);

        table2a.addCell(kreisViertel(2,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP, Rectangle.NO_BORDER));

        PdfPCell cell2Sym2;
        cell2Sym2 = new PdfPCell(new Phrase(Sym2,SMALL_FONT));
        //cell2Bewertungsstufen1.setColspan(1);
        cell2Sym2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Sym2.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Sym2.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Sym2);

        PdfPCell cell2Bewertungsstufen4;
        cell2Bewertungsstufen4 = new PdfPCell(new Phrase("\""+asBewertungen[3]+"\"",SMALL_FONT));
        //cell2Bewertungsstufen4.setColspan(2);
        cell2Bewertungsstufen4.setMinimumHeight(height);
        cell2Bewertungsstufen4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Bewertungsstufen4.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Bewertungsstufen4.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Item);
        table2a.addCell(cell2Bewertungsstufen4);

        table2a.addCell(kreisViertel(3,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.NO_BORDER));

        PdfPCell cell2Sym3;
        cell2Sym3 = new PdfPCell(new Phrase(Sym3,SMALL_FONT));
        //cell2Bewertungsstufen1.setColspan(1);
        cell2Sym3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Sym3.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Sym3.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Sym3);

        PdfPCell cell2Bewertungsstufen5;
        cell2Bewertungsstufen5 = new PdfPCell(new Phrase("\""+asBewertungen[4]+"\"",SMALL_FONT));
        //cell2Bewertungsstufen5.setColspan(2);
        cell2Bewertungsstufen5.setMinimumHeight(height);
        cell2Bewertungsstufen5.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Bewertungsstufen5.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Bewertungsstufen5.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Item);
        table2a.addCell(cell2Bewertungsstufen5);

        table2a.addCell(kreisViertel(4,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP, Rectangle.NO_BORDER));

        PdfPCell cell2Sym4;
        cell2Sym4 = new PdfPCell(new Phrase(Sym4,SMALL_FONT));
        //cell2Bewertungsstufen1.setColspan(1);
        cell2Sym4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Sym4.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Sym4.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Sym4);

        doc.add(table2);
        doc.add(table2a);

        doc.newPage();
        
        // Seite 3 *************************************************************
        // Tablestruktur aufbauen...
        pad=3f;
        PdfPTable table3 = new PdfPTable(6);
        table3.setWidths(new float[] { 60,8,8,8,8,8 });
        table3.setWidthPercentage(100);
        
        PdfPCell cell3Header = emptyLine(6,10f);
        PdfPCell cell3Deutsch;
        cell3Deutsch = new PdfPCell(new Phrase(deutschS + jStufe,NORMAL_BOLD_FONT));
        cell3Deutsch.setColspan(6);
        cell3Deutsch.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell3Deutsch.setFixedHeight(35f);
        cell3Deutsch.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell3Deutsch.setBorder(Rectangle.NO_BORDER);
 
        // Sprechen und Zuhören 
        PdfPCell cell3sz;
        cell3sz = new PdfPCell(new Phrase(sz,NORMAL_FONT));
        //cell2ATitle.setColspan(4);
        cell3sz.setVerticalAlignment(Element.ALIGN_TOP);
        cell3sz.setFixedHeight(50f);
        cell3sz.setPadding(pad);
        cell3sz.setHorizontalAlignment(Element.ALIGN_LEFT);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell3Leer;
//        cell3Leer = new PdfPCell(new Phrase("--",TINY_FONT));
        cell3Leer = new PdfPCell(kreisViertel(0,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.BOX));
        //cell2ATitle.setColspan(4);
        cell3Leer.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //cell2ATitle.setFixedHeight(30f);
        cell3Leer.setPadding(pad);
        cell3Leer.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        table3.addCell(cell3Header);
        table3.addCell(cell3Deutsch);
        table3.addCell(cell3sz);
        table3.addCell(cell3Leer);
        table3.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table3.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table3.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table3.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
        table3 = lernbereiche(table3,sprechenZL,pad);

        // Schreiben 
        PdfPCell cell3EmptyLine;
        cell3EmptyLine = new PdfPCell(new Phrase("",NORMAL_BOLD_FONT));
        cell3EmptyLine.setColspan(6);
        cell3EmptyLine.setFixedHeight(15f);
        cell3EmptyLine.setBorder(Rectangle.NO_BORDER);
        
        cell3sz = new PdfPCell(new Phrase(sch,NORMAL_FONT));
        cell3sz.setVerticalAlignment(Element.ALIGN_TOP);
        cell3sz.setFixedHeight(50f);
        cell3sz.setPadding(pad);
        cell3sz.setHorizontalAlignment(Element.ALIGN_LEFT);

//        cell3Leer = new PdfPCell(new Phrase("--",TINY_FONT));
        cell3Leer = new PdfPCell(kreisViertel(0,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.BOX));
        cell3Leer.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell3Leer.setPadding(pad);
        cell3Leer.setHorizontalAlignment(Element.ALIGN_CENTER);

        table3.addCell(cell3EmptyLine);
        table3.addCell(cell3sz);
        table3.addCell(cell3Leer);
        table3.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table3.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table3.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table3.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
 
        table3 = lernbereiche(table3,schreibenZL,pad);
        
        // Lesen - mit Texten und Medien umgehen
        cell3EmptyLine = new PdfPCell(new Phrase("",NORMAL_BOLD_FONT));
        cell3EmptyLine.setColspan(6);
        cell3EmptyLine.setFixedHeight(15f);
        cell3EmptyLine.setBorder(Rectangle.NO_BORDER);
        
        cell3sz = new PdfPCell(new Phrase(les,NORMAL_FONT));
        cell3sz.setVerticalAlignment(Element.ALIGN_TOP);
        cell3sz.setFixedHeight(50f);
        cell3sz.setPadding(pad);
        cell3sz.setHorizontalAlignment(Element.ALIGN_LEFT);

//        cell3Leer = new PdfPCell(new Phrase("--",TINY_FONT));
        cell3Leer = new PdfPCell(kreisViertel(0,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.BOX));
        cell3Leer.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell3Leer.setPadding(pad);
        cell3Leer.setHorizontalAlignment(Element.ALIGN_CENTER);

        table3.addCell(cell3EmptyLine);
        table3.addCell(cell3sz);
        table3.addCell(cell3Leer);
        table3.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table3.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table3.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table3.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
        table3 = lernbereiche(table3,lesenZL,pad);

        // Sprache und Sprachgebrauch untersuchen
        cell3EmptyLine = new PdfPCell(new Phrase("",NORMAL_BOLD_FONT));
        cell3EmptyLine.setColspan(6);
        cell3EmptyLine.setFixedHeight(15f);
        cell3EmptyLine.setBorder(Rectangle.NO_BORDER);
        
        cell3sz = new PdfPCell(new Phrase(sp,NORMAL_FONT));
        cell3sz.setVerticalAlignment(Element.ALIGN_TOP);
        cell3sz.setFixedHeight(50f);
        cell3sz.setPadding(pad);
        cell3sz.setHorizontalAlignment(Element.ALIGN_LEFT);

//        cell3Leer = new PdfPCell(new Phrase("--",TINY_FONT));
        cell3Leer = new PdfPCell(kreisViertel(0,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.BOX));
        cell3Leer.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell3Leer.setPadding(pad);
        cell3Leer.setHorizontalAlignment(Element.ALIGN_CENTER);

        table3.addCell(cell3EmptyLine);
        table3.addCell(cell3sz);
        table3.addCell(cell3Leer);
        table3.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table3.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table3.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table3.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
        table3 = lernbereiche(table3,sprachZL,pad);
       
        doc.add(table3);
        doc.newPage();

        // Seite 4 *************************************************************
        // Tablestruktur aufbauen...
        pad=3f;
        PdfPTable table4 = new PdfPTable(6);
        table4.setWidths(new float[] { 60,8,8,8,8,8 });
        table4.setWidthPercentage(100);
        
        PdfPCell cell4Header = emptyLine(6,10f);
        
        PdfPCell cell4Mathe;
        cell4Mathe = new PdfPCell(new Phrase(matheS + jStufe,NORMAL_BOLD_FONT));
        cell4Mathe.setColspan(6);
        cell4Mathe.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell4Mathe.setFixedHeight(35f);
        cell4Mathe.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell4Mathe.setBorder(Rectangle.NO_BORDER);
 
        // Zahlen und Operationen 
        PdfPCell cell4zo;
        cell4zo = new PdfPCell(new Phrase(zo,NORMAL_FONT));
        //cell2ATitle.setColspan(4);
        cell4zo.setVerticalAlignment(Element.ALIGN_TOP);
        cell4zo.setFixedHeight(50f);
        cell4zo.setPadding(pad);
        cell4zo.setHorizontalAlignment(Element.ALIGN_LEFT);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell4Leer;
//        cell4Leer = new PdfPCell(new Phrase("--",TINY_FONT));
        cell4Leer = new PdfPCell(kreisViertel(0,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.BOX));
        //cell2ATitle.setColspan(4);
        cell4Leer.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //cell2ATitle.setFixedHeight(30f);
        cell4Leer.setPadding(pad);
        cell4Leer.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        table4.addCell(cell4Header);
        table4.addCell(cell4Mathe);
        table4.addCell(cell4zo);
        table4.addCell(cell4Leer);
        table4.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table4.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table4.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table4.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
        table4 = lernbereiche(table4,zahlenZL,pad);
         
        PdfPCell cell4EmptyLine;
        cell4EmptyLine = new PdfPCell(new Phrase("",NORMAL_BOLD_FONT));
        cell4EmptyLine.setColspan(6);
        cell4EmptyLine.setFixedHeight(15f);
        cell4EmptyLine.setBorder(Rectangle.NO_BORDER);
        
        // Größen und Messen
        PdfPCell cell4gm;
        cell4gm = new PdfPCell(new Phrase(gm,NORMAL_FONT));
        //cell2ATitle.setColspan(4);
        cell4gm.setVerticalAlignment(Element.ALIGN_TOP);
        cell4gm.setFixedHeight(50f);
        cell4gm.setPadding(pad);
        cell4gm.setHorizontalAlignment(Element.ALIGN_LEFT);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

//        cell4Leer = new PdfPCell(new Phrase("--",TINY_FONT));
        cell4Leer = new PdfPCell(kreisViertel(0,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.BOX));
        cell4Leer.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell4Leer.setPadding(pad);
        cell4Leer.setHorizontalAlignment(Element.ALIGN_CENTER);

        table4.addCell(cell4EmptyLine);
        table4.addCell(cell4gm);
        table4.addCell(cell4Leer);
        table4.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table4.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table4.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table4.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
       
        table4 = lernbereiche(table4,groessenZL,pad);

        // Raum und Form
        
        PdfPCell cell4rf;
        cell4rf = new PdfPCell(new Phrase(rf,NORMAL_FONT));
        cell4rf.setVerticalAlignment(Element.ALIGN_TOP);
        cell4rf.setFixedHeight(50f);
        cell4rf.setPadding(pad);
        cell4rf.setHorizontalAlignment(Element.ALIGN_LEFT);

//        cell4Leer = new PdfPCell(new Phrase("--",TINY_FONT));
        cell4Leer = new PdfPCell(kreisViertel(0,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.BOX));
        cell4Leer.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell4Leer.setPadding(pad);
        cell4Leer.setHorizontalAlignment(Element.ALIGN_CENTER);

        table4.addCell(emptyLine(6,15f));
        table4.addCell(cell4rf);
        table4.addCell(cell4Leer);
        table4.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table4.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table4.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        table4.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
        table4 = lernbereiche(table4,raumZL,pad);
        
        doc.add(table4);
        doc.newPage();
              
        // *****************************************************************************************************
        if(Gui.getSClass().substring(0, 1).endsWith("1" ) || Gui.getSClass().substring(0, 1).endsWith("2")){

            // Seite 5 *************************************************************
            // Tablestruktur aufbauen...
            pad=3f;
            PdfPTable table5 = new PdfPTable(6);
            table5.setWidths(new float[] { 60,8,8,8,8,8 });
            table5.setWidthPercentage(100);
        
            PdfPCell cell5Header = emptyLine(6,10f);

            // *********************************************************************
            // Sachunterricht
            // *********************************************************************
            PdfPCell cell5Sachunterricht;
            cell5Sachunterricht = new PdfPCell(new Phrase(sachunterrichtS + jStufe,NORMAL_BOLD_FONT));
            cell5Sachunterricht.setColspan(6);
            cell5Sachunterricht.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5Sachunterricht.setFixedHeight(35f);
            cell5Sachunterricht.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell5Sachunterricht.setBorder(Rectangle.NO_BORDER);
 
            PdfPCell cell5su;
            cell5su = new PdfPCell(new Phrase(su,NORMAL_FONT));
            cell5su.setVerticalAlignment(Element.ALIGN_TOP);
            cell5su.setFixedHeight(30f);
            cell5su.setPadding(pad);
            cell5su.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cell5Leer;
            cell5Leer = new PdfPCell(kreisViertel(0,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.BOX));
            cell5Leer.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5Leer.setPadding(pad);
            cell5Leer.setHorizontalAlignment(Element.ALIGN_CENTER);

            table5.addCell(cell5Header);
            table5.addCell(cell5Sachunterricht);
            table5.addCell(cell5su);
            table5.addCell(cell5Leer);
            table5.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
            table5 = lernbereiche(table5,sachunterrichtZL,pad);
        
            table5.addCell(emptyLine(6,15f));
        
            // *********************************************************************
            // Sport
            // *********************************************************************
            PdfPCell cell5Sport;
            cell5Sport = new PdfPCell(new Phrase(sportS + jStufe,NORMAL_BOLD_FONT));
            cell5Sport.setColspan(6);
            cell5Sport.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5Sport.setFixedHeight(35f);
            cell5Sport.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell5Sport.setBorder(Rectangle.NO_BORDER);
 
            table5.addCell(cell5Sport);
            table5.addCell(cell5su);
            table5.addCell(cell5Leer);
            table5.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
            table5 = lernbereiche(table5,sportZL,pad);
       
            table5.addCell(emptyLine(6,15f)); 
            
            // *********************************************************************
            // Religion
            // *********************************************************************
            PdfPCell cell5Religion;
            cell5Religion = new PdfPCell(new Phrase(religionS + jStufe,NORMAL_BOLD_FONT));
            cell5Religion.setColspan(6);
            cell5Religion.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5Religion.setFixedHeight(35f);
            cell5Religion.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell5Religion.setBorder(Rectangle.NO_BORDER);
 
            cell5su = new PdfPCell(new Phrase(su,NORMAL_FONT));
            cell5su.setVerticalAlignment(Element.ALIGN_TOP);
            cell5su.setFixedHeight(30f);
            cell5su.setPadding(pad);
            cell5su.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell5Leer = new PdfPCell(kreisViertel(0,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.BOX));
            cell5Leer.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5Leer.setPadding(pad);
            cell5Leer.setHorizontalAlignment(Element.ALIGN_CENTER);

            table5.addCell(cell5Religion);
            table5.addCell(cell5su);
            table5.addCell(cell5Leer);
            table5.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
            table5 = lernbereiche(table5,religionZL,pad);

            table5.addCell(emptyLine(6,15f)); 

            // *********************************************************************
            // Musik
            // *********************************************************************
            PdfPCell cell5Musik;
            cell5Musik = new PdfPCell(new Phrase(musikS + jStufe,NORMAL_BOLD_FONT));
            cell5Musik.setColspan(6);
            cell5Musik.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5Musik.setFixedHeight(35f);
            cell5Musik.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell5Musik.setBorder(Rectangle.NO_BORDER);
 
            cell5su = new PdfPCell(new Phrase(su,NORMAL_FONT));
            cell5su.setVerticalAlignment(Element.ALIGN_TOP);
            cell5su.setFixedHeight(30f);
            cell5su.setPadding(pad);
            cell5su.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell5Leer = new PdfPCell(kreisViertel(0,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.BOX));
            cell5Leer.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5Leer.setPadding(pad);
            cell5Leer.setHorizontalAlignment(Element.ALIGN_CENTER);

            table5.addCell(cell5Musik);
            table5.addCell(cell5su);
            table5.addCell(cell5Leer);
            table5.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
            table5 = lernbereiche(table5,musikZL,pad);
        
            table5.addCell(emptyLine(6,15f));
            
            // *********************************************************************
            // Kunst
            // *********************************************************************
            PdfPCell cell5Kunst;
            cell5Kunst = new PdfPCell(new Phrase(kunstS + jStufe,NORMAL_BOLD_FONT));
            cell5Kunst.setColspan(6);
            cell5Kunst.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5Kunst.setFixedHeight(35f);
            cell5Kunst.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell5Kunst.setBorder(Rectangle.NO_BORDER);
 
            table5.addCell(cell5Kunst);
            table5.addCell(cell5su);
            table5.addCell(cell5Leer);
            table5.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
            table5 = lernbereiche(table5,kunstZL,pad);
        
            table5.addCell(emptyLine(6,15f));

            doc.add(table5);    // Ende Seite 5 für 1. und 2. Klasse
        }
        else{   // Anfang Seite 5 für 3. und 4. Klasse
            // Seite 5a *************************************************************
            doc.newPage();            // Tablestruktur aufbauen...
            pad=3f;
            PdfPTable table5a = new PdfPTable(6);
            table5a.setWidths(new float[] { 60,8,8,8,8,8 });
            table5a.setWidthPercentage(100);
        
            PdfPCell cell5Header = emptyLine(6,10f);

            // *********************************************************************
            // Sachunterricht
            // *********************************************************************
            PdfPCell cell5Sachunterricht;
            cell5Sachunterricht = new PdfPCell(new Phrase(sachunterrichtS + jStufe,NORMAL_BOLD_FONT));
            cell5Sachunterricht.setColspan(6);
            cell5Sachunterricht.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5Sachunterricht.setFixedHeight(35f);
            cell5Sachunterricht.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell5Sachunterricht.setBorder(Rectangle.NO_BORDER);
 
            PdfPCell cell5su;
            cell5su = new PdfPCell(new Phrase(su,NORMAL_FONT));
            cell5su.setVerticalAlignment(Element.ALIGN_TOP);
            cell5su.setFixedHeight(30f);
            cell5su.setPadding(pad);
            cell5su.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cell5Leer;
            cell5Leer = new PdfPCell(kreisViertel(0,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.BOX));
            cell5Leer.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5Leer.setPadding(pad);
            cell5Leer.setHorizontalAlignment(Element.ALIGN_CENTER);

            table5a.addCell(cell5Header);
            table5a.addCell(cell5Sachunterricht);
            table5a.addCell(cell5su);
            table5a.addCell(cell5Leer);
            table5a.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5a.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5a.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5a.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
            table5a = lernbereiche(table5a,sachunterrichtZL,pad);
        
            table5a.addCell(emptyLine(6,15f));
        
            // *********************************************************************
            // Englisch
            // *********************************************************************
            PdfPCell cell5Englisch;
            cell5Englisch = new PdfPCell(new Phrase(englischS + jStufe,NORMAL_BOLD_FONT));
            cell5Englisch.setColspan(6);
            cell5Englisch.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5Englisch.setFixedHeight(35f);
            cell5Englisch.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell5Englisch.setBorder(Rectangle.NO_BORDER);

            table5a.addCell(cell5Englisch);
            table5a.addCell(cell5su);
            table5a.addCell(cell5Leer);
            table5a.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5a.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5a.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5a.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
            table5a = lernbereiche(table5a,englischZL,pad);
        
            table5a.addCell(emptyLine(6,15f));

            // *********************************************************************
            // Sport
            // *********************************************************************
            PdfPCell cell5Sport;
            cell5Sport = new PdfPCell(new Phrase(sportS + jStufe,NORMAL_BOLD_FONT));
            cell5Sport.setColspan(6);
            cell5Sport.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5Sport.setFixedHeight(35f);
            cell5Sport.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell5Sport.setBorder(Rectangle.NO_BORDER);
 
            table5a.addCell(cell5Sport);
            table5a.addCell(cell5su);
            table5a.addCell(cell5Leer);
            table5a.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5a.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5a.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table5a.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
            table5a = lernbereiche(table5a,sportZL,pad);
       
            table5a.addCell(emptyLine(6,15f)); 
            
            doc.add(table5a);
            
            // Seite 6 *************************************************************
            doc.newPage();
            // Tablestruktur aufbauen...
            PdfPTable table6 = new PdfPTable(6);
            pad=3f;
            table6.setWidths(new float[] { 60,8,8,8,8,8 });
            table6.setWidthPercentage(100);
        
            PdfPCell cell6Header = emptyLine(6,10f);
           
            // *********************************************************************
            // Religion
            // *********************************************************************
            PdfPCell cell6Religion;
            cell6Religion = new PdfPCell(new Phrase(religionS + jStufe,NORMAL_BOLD_FONT));
            cell6Religion.setColspan(6);
            cell6Religion.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell6Religion.setFixedHeight(35f);
            cell6Religion.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell6Religion.setBorder(Rectangle.NO_BORDER);
 
            PdfPCell cell6su;
            cell6su = new PdfPCell(new Phrase(su,NORMAL_FONT));
            cell6su.setVerticalAlignment(Element.ALIGN_TOP);
            cell6su.setFixedHeight(30f);
            cell6su.setPadding(pad);
            cell6su.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cell6Leer;
            cell6Leer = new PdfPCell(kreisViertel(0,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.BOX));
            cell6Leer.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell6Leer.setPadding(pad);
            cell6Leer.setHorizontalAlignment(Element.ALIGN_CENTER);

            table6.addCell(cell6Religion);
            table6.addCell(cell6su);
            table6.addCell(cell6Leer);
            table6.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table6.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table6.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table6.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
            table6 = lernbereiche(table6,religionZL,pad);

            table6.addCell(emptyLine(6,15f)); 

            // *********************************************************************
            // Musik
            // *********************************************************************
            PdfPCell cell6Musik;
            cell6Musik = new PdfPCell(new Phrase(musikS + jStufe,NORMAL_BOLD_FONT));
            cell6Musik.setColspan(6);
            cell6Musik.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell6Musik.setFixedHeight(35f);
            cell6Musik.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell6Musik.setBorder(Rectangle.NO_BORDER);
 
            cell6su = new PdfPCell(new Phrase(su,NORMAL_FONT));
            cell6su.setVerticalAlignment(Element.ALIGN_TOP);
            cell6su.setFixedHeight(30f);
            cell6su.setPadding(pad);
            cell6su.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell6Leer = new PdfPCell(kreisViertel(0,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.BOX));
            cell6Leer.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell6Leer.setPadding(pad);
            cell6Leer.setHorizontalAlignment(Element.ALIGN_CENTER);

            table6.addCell(cell6Musik);
            table6.addCell(cell6su);
            table6.addCell(cell6Leer);
            table6.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table6.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table6.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table6.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
            table6 = lernbereiche(table6,musikZL,pad);
        
            table6.addCell(emptyLine(6,15f));
            
            // *********************************************************************
            // Kunst
            // *********************************************************************
            PdfPCell cell6Kunst;
            cell6Kunst = new PdfPCell(new Phrase(kunstS + jStufe,NORMAL_BOLD_FONT));
            cell6Kunst.setColspan(6);
            cell6Kunst.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell6Kunst.setFixedHeight(35f);
            cell6Kunst.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell6Kunst.setBorder(Rectangle.NO_BORDER);
 
            table6.addCell(cell6Kunst);
            table6.addCell(cell6su);
            table6.addCell(cell6Leer);
            table6.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table6.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table6.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table6.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
            table6 = lernbereiche(table6,kunstZL,pad);
        
            table6.addCell(emptyLine(6,15f));
            
            doc.add(table6);
            // Seite 7 *************************************************************
            doc.newPage();
            // Tablestruktur aufbauen...
            PdfPTable table7 = new PdfPTable(6);
            pad=3f;
            table7.setWidths(new float[] { 60,8,8,8,8,8 });
            table7.setWidthPercentage(100);
        

            PdfPCell cell7Header = emptyLine(6,10f);            
            
            table7.addCell(cell7Header);
            
            PdfPCell cell7su;
            cell7su = new PdfPCell(new Phrase(su,NORMAL_FONT));
            cell7su.setVerticalAlignment(Element.ALIGN_TOP);
            cell7su.setFixedHeight(30f);
            cell7su.setPadding(pad);
            cell7su.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cell7Leer;
            cell7Leer = new PdfPCell(kreisViertel(0,pad,Element.ALIGN_RIGHT,Element.ALIGN_TOP,Rectangle.BOX));
            cell7Leer.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell7Leer.setPadding(pad);
            cell7Leer.setHorizontalAlignment(Element.ALIGN_CENTER);

            // *********************************************************************
            // Textil
            // *********************************************************************
            PdfPCell cell7Textil;
            cell7Textil = new PdfPCell(new Phrase(textilS + jStufe,NORMAL_BOLD_FONT));
            cell7Textil.setColspan(6);
            cell7Textil.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell7Textil.setFixedHeight(35f);
            cell7Textil.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell7Textil.setBorder(Rectangle.NO_BORDER);
 
            table7.addCell(cell7Textil);
            table7.addCell(cell7su);
            table7.addCell(cell7Leer);
            table7.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table7.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table7.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table7.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
            table7 = lernbereiche(table7,textilZL,pad);
        
            table7.addCell(emptyLine(6,15f));
            
            // *********************************************************************
            // Werken
            // *********************************************************************
            PdfPCell cell7Werken;
            cell7Werken = new PdfPCell(new Phrase(werkenS + jStufe,NORMAL_BOLD_FONT));
            cell7Werken.setColspan(6);
            cell7Werken.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell7Werken.setFixedHeight(35f);
            cell7Werken.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell7Werken.setBorder(Rectangle.NO_BORDER);
 
            table7.addCell(cell7Werken);
            table7.addCell(cell7su);
            table7.addCell(cell7Leer);
            table7.addCell(kreisViertel(1,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table7.addCell(kreisViertel(2,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table7.addCell(kreisViertel(3,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
            table7.addCell(kreisViertel(4,pad,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,Rectangle.BOX));
        
            table7 = lernbereiche(table7,werkenZL,pad);
        
            table7.addCell(emptyLine(6,15f));

            doc.add(table7);
        }
        doc.addTitle("Zeugnis");
        doc.addAuthor("Grundschule Brelingen");
        doc.addCreationDate();
        doc.addSubject("Zeugnis");
        doc.close();
        writer.close();
    }

    
    ArrayList<TableItem> sumArrayList(ArrayList<TableItem> dest, ArrayList<TableItem> add){
        for(int i=0; i<add.size(); i++){
            TableItem tiDest = dest.get(i);
            TableItem tiAdd = add.get(i);
            Integer value = tiDest.getBewertung()+tiAdd.getBewertung();
            tiDest.setBewertung(value);
            dest.set(i, tiDest);
        }
        return dest;
    }
    
    ArrayList<TableItem> divArrayList(ArrayList<TableItem> dest, Integer div){
        for(int i=0; i<dest.size(); i++){
            TableItem ti = dest.get(i);
            Integer value = Math.round((float)ti.getBewertung()/div);
            ti.setBewertung(value);
            dest.set(i, ti);
        }
        return dest;
    }
    /**
     * @return the symbol1
     */
    public static int getSymbol1() {
        return symbol1;
    }

    /**
     * @param symbol the symbol1 to set
     */
    public static void setSymbol1(int symbol) {
        symbol1 = symbol;
    }

    /**
     * @return the symbol2
     */
    public static int getSymbol2() {
        return symbol2;
    }

    /**
     * @param symbol the symbol2 to set
     */
    public static void setSymbol2(int symbol) {
        symbol2 = symbol;
    }
    
    
    
    
    class TableHeader extends PdfPageEventHelper {
        /** The header text. */
        String header;
        /** The template with the total number of pages. */
        PdfTemplate total;
 
        /**
         * Allows us to change the content of the header.
         * @param header The new header String
         */
        public void setHeader(String header) {
            this.header = header;
        }
 
        /**
         * Creates the PdfTemplate that will hold the total number of pages.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onOpenDocument(PdfWriter writer, Document document) {
            total = writer.getDirectContent().createTemplate(30, 16);
        }
 
        /**
         * Adds a header to every page
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable tableFooter = new PdfPTable(3);
            PdfPTable tableHeader = new PdfPTable(1);
            
            if (!this.header.isEmpty()) {
                try {
                    tableFooter.setWidths(new int[]{24, 24, 2});
                    tableFooter.setTotalWidth(527);
                    tableFooter.setLockedWidth(true);
                    tableHeader.setTotalWidth(527);
                    tableHeader.setLockedWidth(true);
                    

                    // Column 1
                    tableFooter.getDefaultCell().setFixedHeight(20);                    
                    tableHeader.getDefaultCell().setFixedHeight(20);
                    tableFooter.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                    tableHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                    tableFooter.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);

                    PdfPCell cellHeader = new PdfPCell(new Phrase(header,SMALL_FONT));
                    cellHeader.setBorder(Rectangle.NO_BORDER);
                    cellHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tableFooter.addCell("");
                    
                    // Column 2
                    tableFooter.addCell( new Phrase(String.format("Seite %d von ", writer.getPageNumber()) , SMALL_FONT) );
                    
                    // Column 3
                    PdfPCell cell = new PdfPCell(Image.getInstance(total));
                    cell.setBorder(Rectangle.NO_BORDER);
                    tableFooter.addCell(cell);
                    tableHeader.addCell(cellHeader);
                    
                    tableFooter.writeSelectedRows(0, -1, 54, 30, writer.getDirectContent());
                    tableHeader.writeSelectedRows(0, -1, 40, 825, writer.getDirectContent());
                } catch (DocumentException de) {
                    throw new ExceptionConverter(de);
                }                
            }
        }
 
        /**
         * Fills out the total number of pages before the document is closed.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber()),SMALL_FONT),2, 4, 0);
        }
    }

    
    
}
