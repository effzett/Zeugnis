/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
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
import javax.swing.table.TableModel;

/**
 *
 * @author juergen
 */
public class DeleteCellEditor extends AbstractCellEditor implements TableCellEditor {

    private final static Logger logger = Logger.getLogger(DeleteCellEditor.class.getName());
    private Object value = null;

    public Component getTableCellEditorComponent(JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {

        this.value = value;
        int result = JOptionPane.showConfirmDialog(null,
                "Soll die Zeile gelöscht werden?",
                "",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        
        if(result == JOptionPane.OK_OPTION) {
            TableModel model = table.getModel();
            
            for (int i = 0; i < model.getColumnCount(); i++) {

                if(model.getColumnName(i).equals("Id")) {
                    
                    try {
                        SingletonSQLConnector.getInstance().deletePuple((String)model.getValueAt(row, i));
                        break;
                    } catch (SQLException ex) {
                        logger.severe(ex.getLocalizedMessage());
                    }
                    
                }
            
            }
            
            ((DefaultTableModel)model).removeRow(row);
        }
               
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

}
