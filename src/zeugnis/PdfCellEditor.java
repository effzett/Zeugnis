/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import com.itextpdf.text.DocumentException;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.EventObject;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
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

        JLabel pdfIcon = new JLabel(new ImageIcon(getClass().getResource("/zeugnis/pics/pdf.png")));
        pdfIcon.setToolTipText("Zeugnis als PDF erzeugen");
        pdfIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                createTestimony(table, row);
            }
        });
        return new JLabel("ätsch");

    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return false;
    }

    // Da der Wert nicht editierbar ist, einfach null zurueckgeben
    @Override
    public Object getCellEditorValue() {
        return null;
    }

    // Ueber Tabelle und Zeile kann auf den Namen des Schuelers zugegriffen werden.
    // Funktioniert noch nicht. Cell Editor ist der falsche Platz.
    // Der EventHandler muß direkt an das JLabel gehängt werden das beim Füllen der Tabelle
    // erzeugt wird.
    private void createTestimony(JTable table, int row) {

        try {
            ZeugnisPDF zeugnisPDF = new ZeugnisPDF();
        } catch (IOException | DocumentException ex) {
            logger.severe(ex.getLocalizedMessage());
        }

    }

}
