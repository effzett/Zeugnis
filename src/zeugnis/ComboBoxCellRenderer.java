/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author juergen
 */
public class ComboBoxCellRenderer implements TableCellRenderer {

    private final static Logger logger = Logger.getLogger(ComboBoxCellRenderer.class.getName());

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
       
        if (value instanceof java.lang.String) {
            label.setText((String) value);
        } else if (value instanceof javax.swing.ImageIcon) {
            label.setIcon((ImageIcon) value);
        } else {
            logger.severe("Unerwartetes Objects beim Rendern des Tabellenfeldes: " + value.getClass().getName());
        }

        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

}
