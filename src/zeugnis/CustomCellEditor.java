/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zeugnis;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author u033334
 */
public class CustomCellEditor extends AbstractCellEditor implements TableCellEditor{
    private Object value = null;

    @Override
    public Object getCellEditorValue() {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return value;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String label = (String)value;
        this.value = label;
        System.out.println(label);
        
        return new JLabel(label);
    }
    
    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            int clickCountToStart = 2;
            return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }
    
}
