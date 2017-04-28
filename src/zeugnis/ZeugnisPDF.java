/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import java.io.IOException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Image;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.Font;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import static com.itextpdf.text.pdf.PdfName.URL;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author internet
 */
public class ZeugnisPDF  {
    private static final Font NORMAL_FONT=new Font(Font.FontFamily.HELVETICA,12,Font.NORMAL);
    private static final Font BIGGER_FONT=new Font(Font.FontFamily.HELVETICA,14,Font.NORMAL);
    private static final Font NAME_FONT=new Font(Font.FontFamily.HELVETICA,26,Font.NORMAL);
    private static final Font SMALL_FONT=new Font(Font.FontFamily.HELVETICA,10,Font.NORMAL);
    private static final Font TINY_FONT=new Font(Font.FontFamily.HELVETICA,9,Font.NORMAL);
    private static final Font BIG_FONT=new Font(Font.FontFamily.HELVETICA,36,Font.BOLD);
    private static final Font NORMAL_BOLD_FONT=new Font(Font.FontFamily.HELVETICA,12,Font.BOLD);
    private static final Font SMALL_BOLD_FONT=new Font(Font.FontFamily.HELVETICA,10,Font.BOLD);
    private static final Font NORMAL_FONT_RED=new Font(Font.FontFamily.HELVETICA,12,Font.NORMAL,BaseColor.RED);
 
    private final static Logger logger = Logger.getLogger(ZeugnisPDF.class.getName());
    
    private int id;
    private String name;
    private String vorname;
    private String gebdatum;
    private String gebort;
    private String currDate;
    
    public class TableItem{
        private String text;
        private int    bewertung;

        /**
         * @return the text
         */
        public String getText() {
            return text;
        }

        /**
         * @param text the text to set
         */
        public void setText(String text) {
            this.text = text;
        }

        /**
         * @return the bewertung
         */
        public int getBewertung() {
            return bewertung;
        }

        /**
         * @param bewertung the bewertung to set
         */
        public void setBewertung(int bewertung) {
            this.bewertung = bewertung;
        }

    }
    
    private ArrayList aVerhalten = new ArrayList<TableItem>();
    private ArrayList sVerhalten = new ArrayList<TableItem>();
    
    /**
     * Erzeugt eine Zeugnisklasse und füllt Variablen aus der DB
     * @param idSCHUELER
     * @throws IOException
     * @throws DocumentException
     * @throws SQLException 
     */
    public ZeugnisPDF(int idSCHUELER) throws IOException, DocumentException, SQLException, ParseException{
        // Hier können schon alle Werte aus der Datenbank geholt werden...
        id = idSCHUELER;
        
        SingletonSQLConnector connector = SingletonSQLConnector.getInstance();

        name      = connector.getSchuelerName(id);
        vorname   = connector.getSchuelerVorname(id);
        gebdatum  = convertDate(connector.getSchuelerGebDatum(id));
        gebort    = connector.getSchuelerGebOrt(id);
        currDate = (new SimpleDateFormat("dd.MM.yyyy")).format(new Date());
        
        // Später werden die Werte hier aus der Datenbank geholt...
        for(int i=0;i<11;i++){
            TableItem ti = new TableItem();
            ti.setText("ist ein ganz, ganz netter und befolgt \nalle Anweisungen " + String.valueOf(i));
            ti.setBewertung(ThreadLocalRandom.current().nextInt(1, 4));
            aVerhalten.add(ti);
        }
      for(int i=0;i<7;i++){
            TableItem ti = new TableItem();
            ti.setText("ist ein asozialer Sack, der andere Kinder \nquält und ärgert " + String.valueOf(i));
            ti.setBewertung(ThreadLocalRandom.current().nextInt(1, 4));
            sVerhalten.add(ti);
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
    
    private void makeCrossOnRightPosition(){
        
    }
    /**
    * 
    * @throws IOException
    * @throws DocumentException
    * @throws SQLException 
    */
    public void CreatePDF() throws IOException, DocumentException, SQLException{
        float pad= 3.0f;

        // wird später alles aus der DB geholt
        
        // Woher kann ich diese globalen Variablen bekommen????
        String Schuljahr = "Schuljahr 2017/2018";
        String Halbjahr  = "1. und 2. Halbjahr";
        String Klasse    = "Klasse 1a";
        

        String Tage      = "versäumte Unterrichtstage im 1. und 2. Halbjahr: 2 davon unentschuldigt: 0";
        String Lernentwicklung = "Lernentwicklung (kurz!)\nInteressen\nLernstand Deutsch\nLernstand Mathe\nVeränderungsprozesse\nKnackpunkte";
        String Unterschriften1 = "___________________\nUnterschrift\nSchulleiter/in";
        String Unterschriften2 = "___________________\nUnterschrift\nKlassenlehrer/in";
        String Datum = "Datum: " + currDate;
        String Unterschriften3 = "___________________\nUnterschrift\nErziehungsberechtigte/r";
        

        String AundS = "Arbeits- und Sozialverhalten";
        String ATitle = "Arbeitsverhalten\n\n" + vorname + "...";
        String STitle = "Sozialverhalten\n\n" + vorname + "...";
        String Selten ="selten";
        String Wechselnd = "wechselnd";
        String Ueberwiegend = "überwiegend";

        String Erklaerungen = "Erklärungen";
        String BewertungsstufenAS = "Bewertungsstufen für das Arbeits- und Sozialverhalten:";
        String BewertungsstufenListe = "'verdient besondere Anerkennung'\n'entspricht den Erwartungen in vollem Umfang'\n'entspricht den Erwartungen'\n'entspricht den Erwartungen mit Einschränkungen'\n'entspricht nicht den Erwartungen'";
        String Symbole = "Symbolerläuterungen für die Unterrichtsfächer:";



        PdfWriter writer = null;
        Document doc=new Document(PageSize.A4,50,50,20,30);
        writer=PdfWriter.getInstance(doc,new FileOutputStream(new File(name+vorname+".pdf")));
        doc.open();

        // Logo
        URL url = this.getClass().getResource("pics/GSBrelingen.jpg");
        Image img = Image.getInstance(url);
        double moremargin=doc.getPageSize().getWidth()*0.0;
        float scaler = ((doc.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin()- (float)moremargin) / img.getWidth()) * 100;
        img.scalePercent(scaler);
        
        // Seite 1 *************************************************************
        // Tablestruktur aufbauen...
        PdfPTable table1 = new PdfPTable(3);
        table1.setWidthPercentage(100);
        
        PdfPCell cell1Logo;
        cell1Logo = new PdfPCell(img);
        cell1Logo.setColspan(3);
        cell1Logo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1Logo.setBorder(Rectangle.NO_BORDER);
        
        
        // Adresse
        PdfPCell cell1Adresse;
        cell1Adresse = new PdfPCell(new Phrase("Grundschule Brelingen, Schulstraße 10, 30900 Wedemark",NORMAL_FONT));
        cell1Adresse.setColspan(3);
        cell1Adresse.setFixedHeight(60f);
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
        cell1Geboren.setVerticalAlignment(Element.ALIGN_BOTTOM);
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
        PdfPCell cell1Lernentwicklung;
        cell1Lernentwicklung = new PdfPCell(new Phrase(Lernentwicklung,NORMAL_FONT));
        cell1Lernentwicklung.setColspan(3);
        cell1Lernentwicklung.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1Lernentwicklung.setFixedHeight(300f);
        cell1Lernentwicklung.setPadding(pad);
        //cell1Lernentwicklung.setBorder(Rectangle.NO_BORDER);
        
        // Unterschriften
        PdfPCell cell1Unterschriften1;
        cell1Unterschriften1 = new PdfPCell(new Phrase(Unterschriften1,SMALL_FONT));
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
        cell1Unterschriften2 = new PdfPCell(new Phrase(Unterschriften2,SMALL_FONT));
        //cell1Unterschriften2.setColspan(2);
        cell1Unterschriften2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1Unterschriften2.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell1Unterschriften2.setFixedHeight(70f);
        cell1Unterschriften2.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell1Datum;
        cell1Datum = new PdfPCell(new Phrase(Datum,SMALL_FONT));
        //cell1Unterschriften.setColspan(3);
        cell1Datum.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1Datum.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell1Datum.setFixedHeight(60f);
        cell1Datum.setBorder(Rectangle.NO_BORDER);
        
        cell1Empty = new PdfPCell(new Phrase("",SMALL_FONT));
        //cell1Unterschriften.setColspan(3);
        cell1Empty.setFixedHeight(60f);
        cell1Empty.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell1Unterschriften3;
        cell1Unterschriften3 = new PdfPCell(new Phrase(Unterschriften3,SMALL_FONT));
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
        table1.addCell(cell1Empty);
        table1.addCell(cell1Unterschriften3);
        
        
        doc.add(table1);
      
        doc.newPage();
        
        // Seite 2 *************************************************************
        // Tablestruktur aufbauen...
        PdfPTable table2 = new PdfPTable(4);
        table2.setWidths(new float[] { 64, 12,12,12 });
        table2.setWidthPercentage(100);
        PdfPCell cell2Header;
        cell2Header = new PdfPCell(new Phrase("Seite 2 des Grundschulzeugnisses von " + vorname + " "+ name + " (" + gebdatum + ") " + " vom " +currDate,SMALL_FONT));
        cell2Header.setColspan(4);
        cell2Header.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Header.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell2AundS;
        cell2AundS = new PdfPCell(new Phrase(AundS,NORMAL_BOLD_FONT));
        cell2AundS.setColspan(4);
        cell2AundS.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2AundS.setFixedHeight(40f);
        cell2AundS.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2AundS.setBorder(Rectangle.NO_BORDER);
 
        PdfPCell cell2ATitle;
        cell2ATitle = new PdfPCell(new Phrase(ATitle,NORMAL_FONT));
        //cell2ATitle.setColspan(4);
        cell2ATitle.setVerticalAlignment(Element.ALIGN_TOP);
        cell2ATitle.setFixedHeight(45f);
        cell2ATitle.setPadding(pad);
        cell2ATitle.setHorizontalAlignment(Element.ALIGN_LEFT);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell2Selten;
        cell2Selten = new PdfPCell(new Phrase(Selten,TINY_FONT));
        //cell2ATitle.setColspan(4);
        cell2Selten.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell2Selten.setPadding(pad);
        cell2Selten.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell2Wechselnd;
        cell2Wechselnd = new PdfPCell(new Phrase(Wechselnd,TINY_FONT));
        //cell2ATitle.setColspan(4);
        cell2Wechselnd.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell2Wechselnd.setPadding(pad);
        cell2Wechselnd.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell2Ueberwiegend;
        cell2Ueberwiegend = new PdfPCell(new Phrase(Ueberwiegend,TINY_FONT));
        //cell2ATitle.setColspan(4);
        cell2Ueberwiegend.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell2Ueberwiegend.setPadding(pad);
        cell2Ueberwiegend.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        table2.addCell(cell2Header);
        table2.addCell(cell2AundS);
        table2.addCell(cell2ATitle);
        table2.addCell(cell2Selten);
        table2.addCell(cell2Wechselnd);
        table2.addCell(cell2Ueberwiegend);
        
        for(Integer i=0; i<aVerhalten.size(); i++){
            PdfPCell cell2;
            cell2 = new PdfPCell(new Phrase( ((TableItem)aVerhalten.get(i)).getText() ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            //cell2.setMinimumHeight(15);
            cell2.setPadding(pad);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            
            PdfPCell cell2bewertungX;
            cell2bewertungX = new PdfPCell(new Phrase("x" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell2bewertungX.setVerticalAlignment(Element.ALIGN_MIDDLE);
            //cell2bewertungX.setMinimumHeight(15);
            cell2bewertungX.setPadding(pad);
            cell2bewertungX.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            PdfPCell cell2bewertung;
            cell2bewertung = new PdfPCell(new Phrase("" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell2bewertung.setVerticalAlignment(Element.ALIGN_MIDDLE);
            //cell2bewertung.setMinimumHeight(15);
            cell2bewertung.setPadding(pad);
            cell2bewertung.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
 
            table2.addCell(cell2);
            switch ( ( (TableItem) aVerhalten.get(i)).getBewertung()) {
            case 1: table2.addCell(cell2bewertungX);
                    table2.addCell(cell2bewertung);
                    table2.addCell(cell2bewertung);
                    break;
            case 2: table2.addCell(cell2bewertung);
                    table2.addCell(cell2bewertungX);
                    table2.addCell(cell2bewertung);;
                    break;
            case 3: table2.addCell(cell2bewertung);
                    table2.addCell(cell2bewertung);
                    table2.addCell(cell2bewertungX);
                    break;
            default: ;
                    break;
            }
         }
        
        PdfPCell cell2EmptyLine;
        cell2EmptyLine = new PdfPCell(new Phrase("",NORMAL_BOLD_FONT));
        cell2EmptyLine.setColspan(4);
        cell2EmptyLine.setFixedHeight(30f);
        cell2EmptyLine.setBorder(Rectangle.NO_BORDER);

        table2.addCell(cell2EmptyLine);
        
        PdfPCell cell2STitle;
        cell2STitle = new PdfPCell(new Phrase(STitle,NORMAL_FONT));
        //cell2ATitle.setColspan(4);
        cell2STitle.setVerticalAlignment(Element.ALIGN_TOP);
        cell2STitle.setFixedHeight(45f);
        cell2STitle.setPadding(pad);
        cell2STitle.setHorizontalAlignment(Element.ALIGN_LEFT);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        table2.addCell(cell2STitle);
        table2.addCell(cell2Selten);
        table2.addCell(cell2Wechselnd);
        table2.addCell(cell2Ueberwiegend);
        
        for(Integer i=0; i<sVerhalten.size(); i++){
            PdfPCell cell2;
            cell2 = new PdfPCell(new Phrase( ((TableItem)sVerhalten.get(i)).getText() ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setMinimumHeight(15);
            cell2.setPadding(pad);
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            
            PdfPCell cell2bewertungX;
            cell2bewertungX = new PdfPCell(new Phrase("x" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell2bewertungX.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2bewertungX.setMinimumHeight(15);
            cell2bewertungX.setPadding(pad);
            cell2bewertungX.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            PdfPCell cell2bewertung;
            cell2bewertung = new PdfPCell(new Phrase("" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell2bewertung.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2bewertung.setMinimumHeight(15);
            cell2bewertung.setPadding(pad);
            cell2bewertung.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
 
            table2.addCell(cell2);
            switch ( ( (TableItem) sVerhalten.get(i)).getBewertung()) {
            case 1: table2.addCell(cell2bewertungX);
                    table2.addCell(cell2bewertung);
                    table2.addCell(cell2bewertung);
                    break;
            case 2: table2.addCell(cell2bewertung);
                    table2.addCell(cell2bewertungX);
                    table2.addCell(cell2bewertung);;
                    break;
            case 3: table2.addCell(cell2bewertung);
                    table2.addCell(cell2bewertung);
                    table2.addCell(cell2bewertungX);
                    break;
            default: ;
                    break;
            }
         }

        cell2EmptyLine = new PdfPCell(new Phrase("",NORMAL_BOLD_FONT));
        cell2EmptyLine.setColspan(4);
        cell2EmptyLine.setFixedHeight(20f);
        cell2EmptyLine.setBorder(Rectangle.NO_BORDER);

        table2.addCell(cell2EmptyLine);

        // Tablestruktur aufbauen...
        PdfPTable table2a = new PdfPTable(4);
        table2a.setWidthPercentage(100);

        
        PdfPCell cell2Erklaerungen;
        Chunk chunk1 = new Chunk(Erklaerungen,SMALL_FONT);
        chunk1.setUnderline(1.5f, -1);
        cell2Erklaerungen = new PdfPCell((new Phrase(chunk1)));
        cell2Erklaerungen.setColspan(4);
        cell2Erklaerungen.setVerticalAlignment(Element.ALIGN_TOP);
        cell2Erklaerungen.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2Erklaerungen.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Erklaerungen);
     
        PdfPCell cell2BewertungsstufenAS;
        cell2BewertungsstufenAS = new PdfPCell(new Phrase(BewertungsstufenAS,SMALL_FONT));
        cell2BewertungsstufenAS.setColspan(4);
        cell2BewertungsstufenAS.setVerticalAlignment(Element.ALIGN_TOP);
        cell2BewertungsstufenAS.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2BewertungsstufenAS.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2BewertungsstufenAS);

        PdfPCell cell2BewertungsstufenListe;
        cell2BewertungsstufenListe = new PdfPCell(new Phrase(BewertungsstufenListe,SMALL_FONT));
        cell2BewertungsstufenListe.setColspan(4);
        cell2BewertungsstufenListe.setVerticalAlignment(Element.ALIGN_TOP);
        cell2BewertungsstufenListe.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2BewertungsstufenListe.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2BewertungsstufenListe);

        PdfPCell cell2Symbole;
        cell2Symbole = new PdfPCell(new Phrase(Symbole,SMALL_FONT));
        cell2Symbole.setColspan(4);
        cell2Symbole.setVerticalAlignment(Element.ALIGN_TOP);
        cell2Symbole.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Symbole.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Symbole);

        /*
        PdfPCell cell2BewertungsstufenListe;
        cell2BewertungsstufenListe = new PdfPCell(new Phrase(BewertungsstufenListe,SMALL_FONT));
        cell2BewertungsstufenListe.setColspan(4);
        cell2BewertungsstufenListe.setVerticalAlignment(Element.ALIGN_TOP);
        cell2BewertungsstufenListe.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2BewertungsstufenListe.setBorder(Rectangle.NO_BORDER);
        table2.addCell(cell2BewertungsstufenListe);
        */
        doc.add(table2);
        doc.add(table2a);

        doc.addTitle("Zeugnis");
        doc.addAuthor("Grundschule Brelingen");
        doc.addCreationDate();
        doc.addSubject("Zeugnis");
        doc.close();
        writer.close();
    }
}
