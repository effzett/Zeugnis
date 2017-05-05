/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import java.awt.Component;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author juergen
 */
public class ComboBoxCellRenderer implements TableCellRenderer {

    private final static Logger logger = Logger.getLogger(ComboBoxCellRenderer.class.getName());

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        JLabel label = new JLabel();
        
        if (value != null) {
            JComboBox comboBox = (JComboBox)value;
            Object item = comboBox.getSelectedItem();

            if (item instanceof java.lang.String) {
                label.setText((String) item);
                label.setIcon(null);
            } else if (item instanceof javax.swing.ImageIcon) {
                label.setText("");
                label.setIcon((ImageIcon) item);
            } else {
                logger.severe("Unerwartetes Objects beim Rendern der ComboBox: " + value.getClass().getName());
            }

            return comboBox;
        } else {
            return label;
        }

    }

}
