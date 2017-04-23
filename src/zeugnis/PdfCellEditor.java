/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import com.itextpdf.text.DocumentException;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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


    public Component getTableCellEditorComponent(JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {

        try {
            ZeugnisPDF zeugnisPDF = new ZeugnisPDF();
        } catch (IOException ex) {
            Logger.getLogger(PdfCellEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(PdfCellEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        /* JOptionPane.showMessageDialog(null,
            "Methode zum PDF erzeugen noch nicht implementiert.",
            "",
            JOptionPane.INFORMATION_MESSAGE);
         */
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
