/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import com.itextpdf.text.DocumentException;
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author juergen
 */
public class PdfCellEditor extends AbstractCellEditor implements TableCellEditor {

    private final static Logger logger = Logger.getLogger(PdfCellEditor.class.getName());
    private Object value = null;
    private JDialog dialog = null;
    JDatePickerImpl datePicker = null;

    public PdfCellEditor() {
        
    }

    //Implement the one method defined by TableCellEditor.
    public Component getTableCellEditorComponent(JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {
        createTestimony(table, row);
        return (JLabel)value;
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
     
    private void createTestimony(JTable table, int row) {
        
        try {
            new ZeugnisPDF();
        } catch (IOException | DocumentException ex) {
            logger.severe(ex.getLocalizedMessage());
        }
        
    }
    
}
