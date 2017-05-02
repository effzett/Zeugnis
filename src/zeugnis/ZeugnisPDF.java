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
import java.awt.Desktop;
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
    private int schuljahr;
    private int halbjahr;
    private String klasse;
    private String name;
    private String vorname;
    private String gebdatum;
    private String gebort;
    private String currDate;
    private String[] asBewertungen = {  "verdient besondere Anerkennung",
                                        "entspricht den Erwartungen in vollem Umfang",
                                        "entspricht den Erwartungen",
                                        "entspricht den Erwartungen mit Einschränkungen",
                                        "entspricht nicht den Erwartungen"}; 
    private int noteArbeit;
    private int noteSozial;
    
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
    private ArrayList sprechenZL = new ArrayList<TableItem>();
    private ArrayList schreibenZL = new ArrayList<TableItem>();
    private ArrayList lesenZL = new ArrayList<TableItem>();
    private ArrayList sprachZL = new ArrayList<TableItem>();
    private ArrayList zahlenZL = new ArrayList<TableItem>();
    private ArrayList groessenZL = new ArrayList<TableItem>();
    private ArrayList raumZL = new ArrayList<TableItem>();
    
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

        //Alle nötigen Felder werden aus der Datenbank gefüllt
        name      = connector.getSchuelerName(id);
        vorname   = connector.getSchuelerVorname(id);
        gebdatum  = convertDate(connector.getSchuelerGebDatum(id));
        gebort    = connector.getSchuelerGebOrt(id);
        currDate = (new SimpleDateFormat("dd.MM.yyyy")).format(new Date());
        schuljahr = Gui.getSYear();
        halbjahr  = Gui.getHYear();
        klasse    = Gui.getSClass();
        
        // Hier müssen später andere Aufrufe stehen , da aus Zeugnissen geholt werden muss...
        ArrayList<String> avListe= new ArrayList();
        avListe = connector.getAVerhalten(idSCHUELER);
        ArrayList<String> svListe= new ArrayList();
        svListe = connector.getSVerhalten(idSCHUELER);
        ArrayList<String> szListe = new ArrayList();
        szListe=connector.getKriterien(idSCHUELER,"Sprechen und Zuhören");
        ArrayList<String> schListe = new ArrayList();
        schListe=connector.getKriterien(idSCHUELER,"Schreiben");
        ArrayList<String> les = new ArrayList();
        les=connector.getKriterien(idSCHUELER,"Lesen - mit Texten und Medien umgehen");
        ArrayList<String> sp = new ArrayList();
        sp=connector.getKriterien(idSCHUELER,"Sprache und Sprachgebrauch untersuchen");
        ArrayList<String> zo = new ArrayList();
        zo=connector.getKriterien(idSCHUELER,"Zahlen und Operationen");
        ArrayList<String> gm = new ArrayList();
        gm=connector.getKriterien(idSCHUELER,"Größen und Messen");
        ArrayList<String> rf = new ArrayList();
        rf=connector.getKriterien(idSCHUELER,"Raum und Form");
        
        
        for(int i=0;i<avListe.size();i++){
            TableItem ti = new TableItem();
            ti.setText(avListe.get(i));
            //ti.setText("ist ein ganz, ganz netter und befolgt \nalle Anweisungen " + String.valueOf(i));
            ti.setBewertung(ThreadLocalRandom.current().nextInt(1, 4));
            aVerhalten.add(ti);
        }
      for(int i=0;i<svListe.size();i++){
            TableItem ti = new TableItem();
            ti.setText(svListe.get(i));
            //ti.setText("ist ein asozialer Sack, der andere Kinder \nquält und ärgert " + String.valueOf(i));
            ti.setBewertung(ThreadLocalRandom.current().nextInt(1, 4));
            sVerhalten.add(ti);
        }
        noteArbeit = 2;
        noteSozial = 0;
        
        for(int i=0;i<szListe.size();i++){
            TableItem ti = new TableItem();
            ti.setText(szListe.get(i));
            ti.setBewertung(ThreadLocalRandom.current().nextInt(1, 6));
            sprechenZL.add(ti);
        }

        for(int i=0;i<schListe.size();i++){
            TableItem ti = new TableItem();
            ti.setText(schListe.get(i));
            ti.setBewertung(ThreadLocalRandom.current().nextInt(1, 6));
            schreibenZL.add(ti);
        }

        for(int i=0;i<les.size();i++){
            TableItem ti = new TableItem();
            ti.setText(les.get(i));
            ti.setBewertung(ThreadLocalRandom.current().nextInt(1, 6));
            lesenZL.add(ti);
        }

        for(int i=0;i<sp.size();i++){
            TableItem ti = new TableItem();
            ti.setText(sp.get(i));
            ti.setBewertung(ThreadLocalRandom.current().nextInt(1, 6));
            sprachZL.add(ti);
        }

        for(int i=0;i<zo.size();i++){
            TableItem ti = new TableItem();
            ti.setText(zo.get(i));
            ti.setBewertung(ThreadLocalRandom.current().nextInt(1, 6));
            zahlenZL.add(ti);
        }
        for(int i=0;i<gm.size();i++){
            TableItem ti = new TableItem();
            ti.setText(gm.get(i));
            ti.setBewertung(ThreadLocalRandom.current().nextInt(1, 6));
            groessenZL.add(ti);
        }
        for(int i=0;i<rf.size();i++){
            TableItem ti = new TableItem();
            ti.setText(rf.get(i));
            ti.setBewertung(ThreadLocalRandom.current().nextInt(1, 6));
            raumZL.add(ti);
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
    
    private PdfPCell header(int page,String vorname,String name,String geb, String currdate, int colspan){
        PdfPCell retCell = new PdfPCell(new Phrase("Seite "+ String.valueOf(page) +
                " des Grundschulzeugnisses von " + vorname + " "+ name + " (" + gebdatum + ") " + 
                " vom " +currDate,SMALL_FONT));
        retCell.setColspan(colspan);
        retCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        retCell.setBorder(Rectangle.NO_BORDER);
        return retCell;
    }
    
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
    
    private PdfPCell seweue(String s, float pad){
        PdfPCell retCell = new PdfPCell(new Phrase(s,TINY_FONT));
        retCell.setVerticalAlignment(Element.ALIGN_TOP);
        retCell.setPadding(pad);
        retCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return retCell;
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
        float pad= 6.0f;

        // wird später alles aus der DB geholt
        
        String Schuljahr = "Schuljahr " + Integer.toString(schuljahr) + "/" + Integer.toString(schuljahr+1);
        String Halbjahr;
        if(halbjahr==1){
            Halbjahr  = "1. Halbjahr";            
        }
        else{
            Halbjahr  = "1. und 2. Halbjahr";
        }
        String Klasse    = "Klasse "+ klasse;
        

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
        
        String Symbole = "Symbolerläuterungen für die Unterrichtsfächer:";
        String Sym0 = "das Thema wurde noch nicht bearbeitet";
        String Sym1 = "Die Kompetenz ist in Ansätzen vorhanden";
        String Sym2 = "Die Kompetenz ist grundlegend gesichert";
        String Sym3 = "Die Kompetenz ist weitgehend gesichert";
        String Sym4 = "Die Kompetenz ist gesichert";
        
        String deutsch = "Deutsch ";
        String mathe   = "Mathematik ";
        String jStufe  = "Jahrgangsstufe "+ Gui.getSClass().substring(0, 1);
        String sz = "Sprechen und Zuhören\n\n" + vorname + "...";
        String sch = "Schreiben\n\n" + vorname + "...";
        String les = "Lesen - mit Texten und Medien umgehen\n\n" + vorname + "...";
        String sp = "Sprache und Sprachgebrauch untersuchen\n\n" + vorname + "...";
        String zo = "Zahlen und Operationen\n\n" + vorname + "...";
        String gm = "Größen und Messen\n\n" + vorname + "...";
        String rf = "Raum und Form\n\n" + vorname + "...";

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
        //pad=2.5f;
        PdfPTable table2 = new PdfPTable(4);
        table2.setWidths(new float[] { 58, 14,14,14 });
        table2.setWidthPercentage(100);
        
        PdfPCell cell2Header = new PdfPCell(header(2,vorname,name,gebdatum,currDate,4));
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
        
        PdfPCell cell2BewertungA;
        if(vorname.endsWith("s") || vorname.endsWith("x") || vorname.endsWith("z")){
            cell2BewertungA = new PdfPCell(new Phrase(vorname + "' Verhalten " + asBewertungen[noteArbeit],NORMAL_FONT));            
        }
        else{
            cell2BewertungA = new PdfPCell(new Phrase(vorname + "s Verhalten " + asBewertungen[noteArbeit],NORMAL_FONT));                        
        }
        cell2BewertungA.setColspan(4);
        cell2BewertungA.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //cell2BewertungA.setFixedHeight(45f);
        cell2BewertungA.setPadding(pad);
        cell2BewertungA.setHorizontalAlignment(Element.ALIGN_LEFT);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);
        table2.addCell(cell2BewertungA);
        
        PdfPCell cell2EmptyLine;
        cell2EmptyLine = new PdfPCell(new Phrase("",NORMAL_BOLD_FONT));
        cell2EmptyLine.setColspan(4);
        cell2EmptyLine.setFixedHeight(30f);
        cell2EmptyLine.setBorder(Rectangle.NO_BORDER);

        table2.addCell(cell2EmptyLine);
        
        
        PdfPCell cell2STitle = new PdfPCell(title2(STitle,1,50f,pad));

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

   PdfPCell cell2BewertungS;
        if(vorname.endsWith("s") || vorname.endsWith("x") || vorname.endsWith("z")){
            cell2BewertungS = new PdfPCell(new Phrase(vorname + "' Verhalten " + asBewertungen[noteSozial],NORMAL_FONT));            
        }
        else{
            cell2BewertungS = new PdfPCell(new Phrase(vorname + "s Verhalten " + asBewertungen[noteSozial],NORMAL_FONT));                        
        }
        cell2BewertungS.setColspan(4);
        cell2BewertungS.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //cell2BewertungA.setFixedHeight(45f);
        cell2BewertungS.setPadding(pad);
        cell2BewertungS.setHorizontalAlignment(Element.ALIGN_LEFT);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);
        table2.addCell(cell2BewertungS);
             
        
        cell2EmptyLine = new PdfPCell(new Phrase("",NORMAL_BOLD_FONT));
        cell2EmptyLine.setColspan(4);
        cell2EmptyLine.setFixedHeight(15f);
        cell2EmptyLine.setBorder(Rectangle.NO_BORDER);

        table2.addCell(cell2EmptyLine);

        // Tablestruktur aufbauen...
        PdfPTable table2a = new PdfPTable(4);
        table2a.setWidths(new float[] { 10, 45,10,45 });
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
        cell2Bewertungsstufen1 = new PdfPCell(new Phrase("\""+asBewertungen[0]+"\"",SMALL_FONT));
        cell2Bewertungsstufen1.setColspan(2);
        cell2Bewertungsstufen1.setMinimumHeight(height);
        cell2Bewertungsstufen1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Bewertungsstufen1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Bewertungsstufen1.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Bewertungsstufen1);

        PdfPCell cell2Sym0img;
        cell2Sym0img = new PdfPCell(new Phrase("--",SMALL_FONT));
        //cell2Bewertungsstufen1.setColspan(1);
        cell2Sym0img.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Sym0img.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2Sym0img.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Sym0img);

        PdfPCell cell2Sym0;
        cell2Sym0 = new PdfPCell(new Phrase(Sym0,SMALL_FONT));
        //cell2Bewertungsstufen1.setColspan(1);
        cell2Sym0.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Sym0.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Sym0.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Sym0);

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

        PdfPCell cell2Bewertungsstufen2;
        cell2Bewertungsstufen2 = new PdfPCell(new Phrase("\""+asBewertungen[1]+"\"",SMALL_FONT));
        cell2Bewertungsstufen2.setColspan(2);
        cell2Bewertungsstufen2.setMinimumHeight(height);
        cell2Bewertungsstufen2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Bewertungsstufen2.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Bewertungsstufen2.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Bewertungsstufen2);

        PdfPCell cell2Sym1img;
        cell2Sym1img = new PdfPCell(img1);
        //cell2Bewertungsstufen1.setColspan(1);
        cell2Sym1img.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Sym1img.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2Sym1img.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Sym1img);

        PdfPCell cell2Sym1;
        cell2Sym1 = new PdfPCell(new Phrase(Sym1,SMALL_FONT));
        //cell2Bewertungsstufen1.setColspan(1);
        cell2Sym1.setVerticalAlignment(Element.ALIGN_TOP);
        cell2Sym1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Sym1.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Sym1);

        PdfPCell cell2Bewertungsstufen3;
        cell2Bewertungsstufen3 = new PdfPCell(new Phrase("\""+asBewertungen[2]+"\"",SMALL_FONT));
        cell2Bewertungsstufen3.setColspan(2);
        cell2Bewertungsstufen3.setMinimumHeight(height);
        cell2Bewertungsstufen3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Bewertungsstufen3.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Bewertungsstufen3.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Bewertungsstufen3);

        PdfPCell cell2Sym2img;
        cell2Sym2img = new PdfPCell(img2);
        //cell2Bewertungsstufen1.setColspan(1);
        cell2Sym2img.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Sym2img.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2Sym2img.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Sym2img);

        PdfPCell cell2Sym2;
        cell2Sym2 = new PdfPCell(new Phrase(Sym2,SMALL_FONT));
        //cell2Bewertungsstufen1.setColspan(1);
        cell2Sym2.setVerticalAlignment(Element.ALIGN_TOP);
        cell2Sym2.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Sym2.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Sym2);

        PdfPCell cell2Bewertungsstufen4;
        cell2Bewertungsstufen4 = new PdfPCell(new Phrase("\""+asBewertungen[3]+"\"",SMALL_FONT));
        cell2Bewertungsstufen4.setColspan(2);
        cell2Bewertungsstufen4.setMinimumHeight(height);
        cell2Bewertungsstufen4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Bewertungsstufen4.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Bewertungsstufen4.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Bewertungsstufen4);

        PdfPCell cell2Sym3img;
        cell2Sym3img = new PdfPCell(img3);
        //cell2Bewertungsstufen1.setColspan(1);
        cell2Sym3img.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Sym3img.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2Sym3img.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Sym3img);

        PdfPCell cell2Sym3;
        cell2Sym3 = new PdfPCell(new Phrase(Sym3,SMALL_FONT));
        //cell2Bewertungsstufen1.setColspan(1);
        cell2Sym3.setVerticalAlignment(Element.ALIGN_TOP);
        cell2Sym3.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Sym3.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Sym3);

        PdfPCell cell2Bewertungsstufen5;
        cell2Bewertungsstufen5 = new PdfPCell(new Phrase("\""+asBewertungen[4]+"\"",SMALL_FONT));
        cell2Bewertungsstufen5.setColspan(2);
        cell2Bewertungsstufen5.setMinimumHeight(height);
        cell2Bewertungsstufen5.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Bewertungsstufen5.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2Bewertungsstufen5.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Bewertungsstufen5);

        PdfPCell cell2Sym4img;
        cell2Sym4img = new PdfPCell(img4);
        //cell2Bewertungsstufen1.setColspan(1);
        cell2Sym4img.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2Sym4img.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2Sym4img.setBorder(Rectangle.NO_BORDER);
        table2a.addCell(cell2Sym4img);

        PdfPCell cell2Sym4;
        cell2Sym4 = new PdfPCell(new Phrase(Sym4,SMALL_FONT));
        //cell2Bewertungsstufen1.setColspan(1);
        cell2Sym4.setVerticalAlignment(Element.ALIGN_TOP);
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
        
        PdfPCell cell3Header = header(3,vorname,name,gebdatum,currDate,6);
        
        PdfPCell cell3Deutsch;
        cell3Deutsch = new PdfPCell(new Phrase(deutsch + jStufe,NORMAL_BOLD_FONT));
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
        cell3Leer = new PdfPCell(new Phrase("--",TINY_FONT));
        //cell2ATitle.setColspan(4);
        cell3Leer.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell3Leer.setPadding(pad);
        cell3Leer.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell3Viertel;
        cell3Viertel = new PdfPCell(img1);
        //cell2ATitle.setColspan(4);
        cell3Viertel.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell3Viertel.setPadding(pad);
        cell3Viertel.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell3Halb;
        cell3Halb = new PdfPCell(img2);
        //cell2ATitle.setColspan(4);
        cell3Halb.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell3Halb.setPadding(pad);
        cell3Halb.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell3Dreiviertel;
        cell3Dreiviertel = new PdfPCell(img3);
        //cell2ATitle.setColspan(4);
        cell3Dreiviertel.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell3Dreiviertel.setPadding(pad);
        cell3Dreiviertel.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell3Voll;
        cell3Voll = new PdfPCell(img4);
        //cell2ATitle.setColspan(4);
        cell3Voll.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell3Voll.setPadding(pad);
        cell3Voll.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        table3.addCell(cell3Header);
        table3.addCell(cell3Deutsch);
        table3.addCell(cell3sz);
        table3.addCell(cell3Leer);
        table3.addCell(cell3Viertel);
        table3.addCell(cell3Halb);
        table3.addCell(cell3Dreiviertel);
        table3.addCell(cell3Voll);
        
 
        for(Integer i=0; i<sprechenZL.size(); i++){
            PdfPCell cell3;
            cell3 = new PdfPCell(new Phrase( ((TableItem)sprechenZL.get(i)).getText() ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell3.setMinimumHeight(15);
            cell3.setPadding(pad);
            cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            
            PdfPCell cell3bewertungX;
            cell3bewertungX = new PdfPCell(new Phrase("x" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell3bewertungX.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell3bewertungX.setMinimumHeight(15);
            cell3bewertungX.setPadding(pad);
            cell3bewertungX.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            PdfPCell cell3bewertung;
            cell3bewertung = new PdfPCell(new Phrase("" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell3bewertung.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell3bewertung.setMinimumHeight(15);
            cell3bewertung.setPadding(pad);
            cell3bewertung.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
 
            table3.addCell(cell3);
            switch ( ( (TableItem) sprechenZL.get(i)).getBewertung()) {
            case 1: table3.addCell(cell3bewertungX);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    break;
            case 2: table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertungX);
                    table3.addCell(cell3bewertung);;
                    table3.addCell(cell3bewertung);;
                    table3.addCell(cell3bewertung);;
                    break;
            case 3: table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertungX);
                    table3.addCell(cell3bewertung);;
                    table3.addCell(cell3bewertung);;
                    break;
            case 4: table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertungX);;
                    table3.addCell(cell3bewertung);;
                    break;
            case 5: table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);;
                    table3.addCell(cell3bewertungX);;
                    break;
            default: ;
                    break;
            }
         }


        // Schreiben 
        PdfPCell cell3EmptyLine;
        cell3EmptyLine = new PdfPCell(new Phrase("",NORMAL_BOLD_FONT));
        cell3EmptyLine.setColspan(6);
        cell3EmptyLine.setFixedHeight(15f);
        cell3EmptyLine.setBorder(Rectangle.NO_BORDER);
        
        cell3sz = new PdfPCell(new Phrase(sch,NORMAL_FONT));
        //cell2ATitle.setColspan(4);
        cell3sz.setVerticalAlignment(Element.ALIGN_TOP);
        cell3sz.setFixedHeight(50f);
        cell3sz.setPadding(pad);
        cell3sz.setHorizontalAlignment(Element.ALIGN_LEFT);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell3Leer = new PdfPCell(new Phrase("--",TINY_FONT));
        //cell2ATitle.setColspan(4);
        cell3Leer.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell3Leer.setPadding(pad);
        cell3Leer.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell3Viertel = new PdfPCell(img1);
        //cell2ATitle.setColspan(4);
        cell3Viertel.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell3Viertel.setPadding(pad);
        cell3Viertel.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell3Halb = new PdfPCell(img2);
        //cell2ATitle.setColspan(4);
        cell3Halb.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell3Halb.setPadding(pad);
        cell3Halb.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell3Dreiviertel = new PdfPCell(img3);
        //cell2ATitle.setColspan(4);
        cell3Dreiviertel.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell3Dreiviertel.setPadding(pad);
        cell3Dreiviertel.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell3Voll = new PdfPCell(img4);
        //cell2ATitle.setColspan(4);
        cell3Voll.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell3Voll.setPadding(pad);
        cell3Voll.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        table3.addCell(cell3EmptyLine);
        table3.addCell(cell3sz);
        table3.addCell(cell3Leer);
        table3.addCell(cell3Viertel);
        table3.addCell(cell3Halb);
        table3.addCell(cell3Dreiviertel);
        table3.addCell(cell3Voll);
        
 
        for(Integer i=0; i<schreibenZL.size(); i++){
            PdfPCell cell3;
            cell3 = new PdfPCell(new Phrase( ((TableItem)schreibenZL.get(i)).getText() ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell3.setMinimumHeight(15);
            cell3.setPadding(pad);
            cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            
            PdfPCell cell3bewertungX;
            cell3bewertungX = new PdfPCell(new Phrase("x" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell3bewertungX.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell3bewertungX.setMinimumHeight(15);
            cell3bewertungX.setPadding(pad);
            cell3bewertungX.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            PdfPCell cell3bewertung;
            cell3bewertung = new PdfPCell(new Phrase("" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell3bewertung.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell3bewertung.setMinimumHeight(15);
            cell3bewertung.setPadding(pad);
            cell3bewertung.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
 
            table3.addCell(cell3);
            switch ( ( (TableItem) schreibenZL.get(i)).getBewertung()) {
            case 1: table3.addCell(cell3bewertungX);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    break;
            case 2: table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertungX);
                    table3.addCell(cell3bewertung);;
                    table3.addCell(cell3bewertung);;
                    table3.addCell(cell3bewertung);;
                    break;
            case 3: table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertungX);
                    table3.addCell(cell3bewertung);;
                    table3.addCell(cell3bewertung);;
                    break;
            case 4: table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertungX);;
                    table3.addCell(cell3bewertung);;
                    break;
            case 5: table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);;
                    table3.addCell(cell3bewertungX);;
                    break;
            default: ;
                    break;
            }
         }

        // Lesen - mit Texten und Medien umgehen
        cell3EmptyLine = new PdfPCell(new Phrase("",NORMAL_BOLD_FONT));
        cell3EmptyLine.setColspan(6);
        cell3EmptyLine.setFixedHeight(15f);
        cell3EmptyLine.setBorder(Rectangle.NO_BORDER);
        
        cell3sz = new PdfPCell(new Phrase(les,NORMAL_FONT));
        //cell2ATitle.setColspan(4);
        cell3sz.setVerticalAlignment(Element.ALIGN_TOP);
        cell3sz.setFixedHeight(50f);
        cell3sz.setPadding(pad);
        cell3sz.setHorizontalAlignment(Element.ALIGN_LEFT);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell3Leer = new PdfPCell(new Phrase("--",TINY_FONT));
        //cell2ATitle.setColspan(4);
        cell3Leer.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell3Leer.setPadding(pad);
        cell3Leer.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell3Viertel = new PdfPCell(img1);
        //cell2ATitle.setColspan(4);
        cell3Viertel.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell3Viertel.setPadding(pad);
        cell3Viertel.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell3Halb = new PdfPCell(img2);
        //cell2ATitle.setColspan(4);
        cell3Halb.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell3Halb.setPadding(pad);
        cell3Halb.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell3Dreiviertel = new PdfPCell(img3);
        //cell2ATitle.setColspan(4);
        cell3Dreiviertel.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell3Dreiviertel.setPadding(pad);
        cell3Dreiviertel.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell3Voll = new PdfPCell(img4);
        //cell2ATitle.setColspan(4);
        cell3Voll.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell3Voll.setPadding(pad);
        cell3Voll.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        table3.addCell(cell3EmptyLine);
        table3.addCell(cell3sz);
        table3.addCell(cell3Leer);
        table3.addCell(cell3Viertel);
        table3.addCell(cell3Halb);
        table3.addCell(cell3Dreiviertel);
        table3.addCell(cell3Voll);
        
 
        for(Integer i=0; i<lesenZL.size(); i++){
            PdfPCell cell3;
            cell3 = new PdfPCell(new Phrase( ((TableItem)lesenZL.get(i)).getText() ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell3.setMinimumHeight(15);
            cell3.setPadding(pad);
            cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            
            PdfPCell cell3bewertungX;
            cell3bewertungX = new PdfPCell(new Phrase("x" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell3bewertungX.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell3bewertungX.setMinimumHeight(15);
            cell3bewertungX.setPadding(pad);
            cell3bewertungX.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            PdfPCell cell3bewertung;
            cell3bewertung = new PdfPCell(new Phrase("" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell3bewertung.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell3bewertung.setMinimumHeight(15);
            cell3bewertung.setPadding(pad);
            cell3bewertung.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
 
            table3.addCell(cell3);
            switch ( ( (TableItem) schreibenZL.get(i)).getBewertung()) {
            case 1: table3.addCell(cell3bewertungX);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    break;
            case 2: table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertungX);
                    table3.addCell(cell3bewertung);;
                    table3.addCell(cell3bewertung);;
                    table3.addCell(cell3bewertung);;
                    break;
            case 3: table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertungX);
                    table3.addCell(cell3bewertung);;
                    table3.addCell(cell3bewertung);;
                    break;
            case 4: table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertungX);;
                    table3.addCell(cell3bewertung);;
                    break;
            case 5: table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);
                    table3.addCell(cell3bewertung);;
                    table3.addCell(cell3bewertungX);;
                    break;
            default: ;
                    break;
            }
         }

        
        doc.add(table3);
        doc.newPage();

        // Seite 4 *************************************************************
        // Tablestruktur aufbauen...
        pad=3f;
        PdfPTable table4 = new PdfPTable(6);
        table4.setWidths(new float[] { 60,8,8,8,8,8 });
        table4.setWidthPercentage(100);
        
        PdfPCell cell4Header = header(4,vorname,name,gebdatum,currDate,6);
        
        PdfPCell cell4Mathe;
        cell4Mathe = new PdfPCell(new Phrase(mathe + jStufe,NORMAL_BOLD_FONT));
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
        cell4Leer = new PdfPCell(new Phrase("--",TINY_FONT));
        //cell2ATitle.setColspan(4);
        cell4Leer.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell4Leer.setPadding(pad);
        cell4Leer.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell4Viertel;
        cell4Viertel = new PdfPCell(img1);
        //cell2ATitle.setColspan(4);
        cell4Viertel.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell4Viertel.setPadding(pad);
        cell4Viertel.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell4Halb;
        cell4Halb = new PdfPCell(img2);
        //cell2ATitle.setColspan(4);
        cell4Halb.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell4Halb.setPadding(pad);
        cell4Halb.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell4Dreiviertel;
        cell4Dreiviertel = new PdfPCell(img3);
        //cell2ATitle.setColspan(4);
        cell4Dreiviertel.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell4Dreiviertel.setPadding(pad);
        cell4Dreiviertel.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell4Voll;
        cell4Voll = new PdfPCell(img4);
        //cell2ATitle.setColspan(4);
        cell4Voll.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell4Voll.setPadding(pad);
        cell4Voll.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        table4.addCell(cell4Header);
        table4.addCell(cell4Mathe);
        table4.addCell(cell4zo);
        table4.addCell(cell4Leer);
        table4.addCell(cell4Viertel);
        table4.addCell(cell4Halb);
        table4.addCell(cell4Dreiviertel);
        table4.addCell(cell4Voll);
        
 
        for(Integer i=0; i<zahlenZL.size(); i++){
            PdfPCell cell4;
            cell4 = new PdfPCell(new Phrase( ((TableItem)zahlenZL.get(i)).getText() ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell4.setMinimumHeight(15);
            cell4.setPadding(pad);
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            
            PdfPCell cell4bewertungX;
            cell4bewertungX = new PdfPCell(new Phrase("x" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell4bewertungX.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell4bewertungX.setMinimumHeight(15);
            cell4bewertungX.setPadding(pad);
            cell4bewertungX.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            PdfPCell cell4bewertung;
            cell4bewertung = new PdfPCell(new Phrase("" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell4bewertung.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell4bewertung.setMinimumHeight(15);
            cell4bewertung.setPadding(pad);
            cell4bewertung.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
 
            table4.addCell(cell4);
            switch ( ( (TableItem) zahlenZL.get(i)).getBewertung()) {
            case 1: table4.addCell(cell4bewertungX);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    break;
            case 2: table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertungX);
                    table4.addCell(cell4bewertung);;
                    table4.addCell(cell4bewertung);;
                    table4.addCell(cell4bewertung);;
                    break;
            case 3: table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertungX);
                    table4.addCell(cell4bewertung);;
                    table4.addCell(cell4bewertung);;
                    break;
            case 4: table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertungX);;
                    table4.addCell(cell4bewertung);;
                    break;
            case 5: table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);;
                    table4.addCell(cell4bewertungX);;
                    break;
            default: ;
                    break;
            }
         }
 
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

        cell4Leer = new PdfPCell(new Phrase("--",TINY_FONT));
        //cell2ATitle.setColspan(4);
        cell4Leer.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell4Leer.setPadding(pad);
        cell4Leer.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell4Viertel = new PdfPCell(img1);
        //cell2ATitle.setColspan(4);
        cell4Viertel.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell4Viertel.setPadding(pad);
        cell4Viertel.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell4Halb = new PdfPCell(img2);
        //cell2ATitle.setColspan(4);
        cell4Halb.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell4Halb.setPadding(pad);
        cell4Halb.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell4Dreiviertel = new PdfPCell(img3);
        //cell2ATitle.setColspan(4);
        cell4Dreiviertel.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell4Dreiviertel.setPadding(pad);
        cell4Dreiviertel.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell4Voll = new PdfPCell(img4);
        //cell2ATitle.setColspan(4);
        cell4Voll.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell4Voll.setPadding(pad);
        cell4Voll.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        table4.addCell(cell4EmptyLine);
        table4.addCell(cell4gm);
        table4.addCell(cell4Leer);
        table4.addCell(cell4Viertel);
        table4.addCell(cell4Halb);
        table4.addCell(cell4Dreiviertel);
        table4.addCell(cell4Voll);
        
 
        for(Integer i=0; i<groessenZL.size(); i++){
            PdfPCell cell4;
            cell4 = new PdfPCell(new Phrase( ((TableItem)groessenZL.get(i)).getText() ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell4.setMinimumHeight(15);
            cell4.setPadding(pad);
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            
            PdfPCell cell4bewertungX;
            cell4bewertungX = new PdfPCell(new Phrase("x" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell4bewertungX.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell4bewertungX.setMinimumHeight(15);
            cell4bewertungX.setPadding(pad);
            cell4bewertungX.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            PdfPCell cell4bewertung;
            cell4bewertung = new PdfPCell(new Phrase("" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell4bewertung.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell4bewertung.setMinimumHeight(15);
            cell4bewertung.setPadding(pad);
            cell4bewertung.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
 
            table4.addCell(cell4);
            switch ( ( (TableItem) groessenZL.get(i)).getBewertung()) {
            case 1: table4.addCell(cell4bewertungX);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    break;
            case 2: table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertungX);
                    table4.addCell(cell4bewertung);;
                    table4.addCell(cell4bewertung);;
                    table4.addCell(cell4bewertung);;
                    break;
            case 3: table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertungX);
                    table4.addCell(cell4bewertung);;
                    table4.addCell(cell4bewertung);;
                    break;
            case 4: table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertungX);;
                    table4.addCell(cell4bewertung);;
                    break;
            case 5: table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);;
                    table4.addCell(cell4bewertungX);;
                    break;
            default: ;
                    break;
            }
         }

        // Raum und Form
        cell4EmptyLine = new PdfPCell(new Phrase("",NORMAL_BOLD_FONT));
        cell4EmptyLine.setColspan(6);
        cell4EmptyLine.setFixedHeight(15f);
        cell4EmptyLine.setBorder(Rectangle.NO_BORDER);
        
        PdfPCell cell4rf;
        cell4rf = new PdfPCell(new Phrase(rf,NORMAL_FONT));
        //cell2ATitle.setColspan(4);
        cell4rf.setVerticalAlignment(Element.ALIGN_TOP);
        cell4rf.setFixedHeight(50f);
        cell4rf.setPadding(pad);
        cell4rf.setHorizontalAlignment(Element.ALIGN_LEFT);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell4Leer = new PdfPCell(new Phrase("--",TINY_FONT));
        //cell2ATitle.setColspan(4);
        cell4Leer.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell4Leer.setPadding(pad);
        cell4Leer.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell4Viertel = new PdfPCell(img1);
        //cell2ATitle.setColspan(4);
        cell4Viertel.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell4Viertel.setPadding(pad);
        cell4Viertel.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell4Halb = new PdfPCell(img2);
        //cell2ATitle.setColspan(4);
        cell4Halb.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell4Halb.setPadding(pad);
        cell4Halb.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell4Dreiviertel = new PdfPCell(img3);
        //cell2ATitle.setColspan(4);
        cell4Dreiviertel.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell4Dreiviertel.setPadding(pad);
        cell4Dreiviertel.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        cell4Voll = new PdfPCell(img4);
        //cell2ATitle.setColspan(4);
        cell4Voll.setVerticalAlignment(Element.ALIGN_TOP);
        //cell2ATitle.setFixedHeight(30f);
        cell4Voll.setPadding(pad);
        cell4Voll.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell2ATitle.setBorder(Rectangle.NO_BORDER);

        table4.addCell(cell4EmptyLine);
        table4.addCell(cell4rf);
        table4.addCell(cell4Leer);
        table4.addCell(cell4Viertel);
        table4.addCell(cell4Halb);
        table4.addCell(cell4Dreiviertel);
        table4.addCell(cell4Voll);
        
 
        for(Integer i=0; i<raumZL.size(); i++){
            PdfPCell cell4;
            cell4 = new PdfPCell(new Phrase( ((TableItem)raumZL.get(i)).getText() ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell4.setMinimumHeight(15);
            cell4.setPadding(pad);
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            
            PdfPCell cell4bewertungX;
            cell4bewertungX = new PdfPCell(new Phrase("x" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell4bewertungX.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell4bewertungX.setMinimumHeight(15);
            cell4bewertungX.setPadding(pad);
            cell4bewertungX.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
            
            PdfPCell cell4bewertung;
            cell4bewertung = new PdfPCell(new Phrase("" ,TINY_FONT));
            //cell2ATitle.setColspan(4);
            cell4bewertung.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell4bewertung.setMinimumHeight(15);
            cell4bewertung.setPadding(pad);
            cell4bewertung.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell2ATitle.setBorder(Rectangle.NO_BORDER);
 
            table4.addCell(cell4);
            switch ( ( (TableItem) raumZL.get(i)).getBewertung()) {
            case 1: table4.addCell(cell4bewertungX);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    break;
            case 2: table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertungX);
                    table4.addCell(cell4bewertung);;
                    table4.addCell(cell4bewertung);;
                    table4.addCell(cell4bewertung);;
                    break;
            case 3: table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertungX);
                    table4.addCell(cell4bewertung);;
                    table4.addCell(cell4bewertung);;
                    break;
            case 4: table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertungX);;
                    table4.addCell(cell4bewertung);;
                    break;
            case 5: table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);
                    table4.addCell(cell4bewertung);;
                    table4.addCell(cell4bewertungX);;
                    break;
            default: ;
                    break;
            }
         }

        
        doc.add(table4);

        
        
        doc.addTitle("Zeugnis");
        doc.addAuthor("Grundschule Brelingen");
        doc.addCreationDate();
        doc.addSubject("Zeugnis");
        doc.close();
        writer.close();
              
        // TODO nur zum Testen, muss später besser gelöst werden
        Desktop desktop = Desktop.getDesktop();
        if (desktop != null && desktop.isSupported(Desktop.Action.OPEN)) {
            desktop.open(new File(System.getProperty("user.home") +"/NetBeansProjects/Zeugnis"+ "/"+name+vorname+".pdf"));
            } else {
                System.err.println("PDF-Datei kann nicht angezeigt werden!");
            }

    }
}
