/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;
 import com.itextpdf.text.BaseColor;
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
public class ZeugnisPDF {
    private static final Font NORMAL_FONT=new Font(Font.FontFamily.TIMES_ROMAN,12,Font.NORMAL);
    private static final Font NORMAL_BOLD_FONT=new Font(Font.FontFamily.TIMES_ROMAN,12,Font.BOLD);
    private static final Font NORMAL_FONT_RED=new Font(Font.FontFamily.TIMES_ROMAN,12,Font.NORMAL,BaseColor.RED);
 
    
    public ZeugnisPDF(){
        // Schueler ist Max Mustermann, nur zum Layout testen
        // Der richtige Ctor ist mit Parameter CreatePDF(int idSchueler)
        // und Daten werden aus Datenbanken geholt...
        
        PdfWriter writer = null;
        Document doc=new Document(PageSize.A4,50,30,50,30);
        try {
            writer=PdfWriter.getInstance(doc,new FileOutputStream(new File("MustermannMax.pdf")));
        } catch (FileNotFoundException | DocumentException ex) {
            Logger.getLogger(PdfCellEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        doc.open();
        
       Paragraph para = new Paragraph("Logo und Schuladresse",NORMAL_FONT);
       para.setLeading(0, 1);
       PdfPTable table = new PdfPTable(1);
       table.setWidthPercentage(100);
       PdfPCell cell = new PdfPCell();
       cell.setMinimumHeight(50);
       cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
       cell.setHorizontalAlignment(Element.ALIGN_CENTER);
       cell.addElement(para);
       table.addCell(cell);
        try {
            doc.add(table);
        } catch (DocumentException ex) {
            Logger.getLogger(PdfCellEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        doc.addTitle("iText Test");
        doc.addAuthor("wer auch immer");
        doc.addCreationDate();
        doc.addSubject("Hello World");
        doc.close();
        writer.close();
    }
    

}
