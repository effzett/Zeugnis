/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author juergen
 */

public class PdfCellEditor extends AbstractCellEditor implements TableCellEditor {

    private final static Logger logger = Logger.getLogger(PdfCellEditor.class.getName());
    private Object value = null;

private static final Font NORMAL_FONT=new Font(Font.FontFamily.TIMES_ROMAN,12,Font.NORMAL);
private static final Font NORMAL_BOLD_FONT=new Font(Font.FontFamily.TIMES_ROMAN,12,Font.BOLD);
private static final Font NORMAL_FONT_RED=new Font(Font.FontFamily.TIMES_ROMAN,12,Font.NORMAL,BaseColor.RED);

    public Component getTableCellEditorComponent(JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {

        PdfWriter writer = null;
        Document doc=new Document(PageSize.A4,50,30,50,30);
        try {
            writer=PdfWriter.getInstance(doc,new FileOutputStream(new File("g:\\test.pdf")));
        } catch (FileNotFoundException | DocumentException ex) {
            Logger.getLogger(PdfCellEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        doc.open();
             Phrase textblock=new Phrase();
 
    Chunk chunk=new Chunk("Das ist ein erster Text.",NORMAL_FONT);
    textblock.add(chunk);
 
    chunk=new Chunk("Das ist ein zweiter Text.",NORMAL_BOLD_FONT);
    textblock.add(chunk);
 
    chunk=new Chunk("Das ist ein dritter Text.",NORMAL_FONT_RED);
    chunk.setBackground(BaseColor.GREEN);
    textblock.add(chunk);
 
    textblock.add(Chunk.NEWLINE);
    textblock.add(Chunk.NEWLINE);
 
    chunk=new Chunk("Das ist ein vierter Text.",NORMAL_FONT);
    textblock.add(chunk);
        try {
            doc.add(new Paragraph(textblock));
        } catch (DocumentException ex) {
            Logger.getLogger(PdfCellEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
          doc.addTitle("iText Test");
    doc.addAuthor("wer auch immer");
    doc.addCreationDate();
    doc.addSubject("Hello World");
          doc.close();
    writer.close();

        JOptionPane.showMessageDialog(null,
                "Methode zum PDF erzeugen noch nicht implementiert.",
                "",
                JOptionPane.INFORMATION_MESSAGE);
               
        return new JLabel(new ImageIcon(getClass().getResource("pdf.png")));
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {

        if (anEvent instanceof MouseEvent) {
            int clickCountToStart = 1;
            return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
        }

        return true;
    }

    @Override
    public Object getCellEditorValue() {
        return value;
    }

}
