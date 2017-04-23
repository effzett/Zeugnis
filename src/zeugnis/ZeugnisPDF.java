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
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author internet
 */
public class ZeugnisPDF  {
    private static final Font NORMAL_FONT=new Font(Font.FontFamily.HELVETICA,12,Font.NORMAL);
    private static final Font BIG_FONT=new Font(Font.FontFamily.HELVETICA,36,Font.BOLD);
    private static final Font NORMAL_BOLD_FONT=new Font(Font.FontFamily.HELVETICA,12,Font.BOLD);
    private static final Font NORMAL_FONT_RED=new Font(Font.FontFamily.HELVETICA,12,Font.NORMAL,BaseColor.RED);
 
    
    public ZeugnisPDF() throws IOException, DocumentException, DocumentException{
        // Schueler ist Max Mustermann, nur zum Layout testen
        // Der richtige Ctor ist mit Parameter CreatePDF(int idSchueler)
        // und Daten werden aus Datenbanken geholt...
        
        // wird später alles aus der DB geholt
        String Logozeile = "Logo und Schuladresse";
        String Schuljahr = "Schuljahr 2017/2018";
        String Halbjahr  = "1. und 2. Halbjahr";
        String Klasse    = "Klasse 1a";
        String Name      = "Max Mustermann";
        String Geboren   = "geboren am 01.01.2000 in Brelingen";
        String Tage      = "versäumte Unterrichtstage im 1. und 2. Halbjahr: 2 davon unentschuldigt: 0";
        String Lernentwicklung;  // aus der DB ?????
        
        PdfWriter writer = null;
        Document doc=new Document(PageSize.A4,50,30,20,30);
        writer=PdfWriter.getInstance(doc,new FileOutputStream(new File("MustermannMax.pdf")));
        doc.open();
        
        // Logo
        double moremargin=doc.getPageSize().getWidth()*0.05;
        Image img = Image.getInstance("OtherFiles/GSBrelingen.jpg");
        float scaler = ((doc.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin()- (float)moremargin) / img.getWidth()) * 100;
        img.scalePercent(scaler);
        doc.add(img);
      
        // Logo
        Paragraph paraLogo = new Paragraph("Grundschule Brelingen, Schulstraße 10, 30900 Wedemark",NORMAL_FONT);
        paraLogo.setLeading(0, 1);
        paraLogo.setAlignment(Element.ALIGN_CENTER);
        
        //Zeugnis
        Paragraph paraZeugnis = new Paragraph("Zeugnis",BIG_FONT);
        paraZeugnis.setLeading(0, 2);
        paraZeugnis.setAlignment(Element.ALIGN_CENTER);
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        cell.setMinimumHeight(50);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(paraLogo);
        cell.addElement(paraZeugnis);
        
        table.addCell(cell);
        
        doc.add(table);
        doc.addTitle("Zeugnis");
        doc.addAuthor("Grundschule Brelingen");
        doc.addCreationDate();
        doc.addSubject("Zeugnis");
        doc.close();
        writer.close();
    }
}
