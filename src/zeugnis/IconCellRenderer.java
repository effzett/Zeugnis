/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author u033334
 */
public class IconCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (column == 5) {
            setIcon(new ImageIcon(getClass().getResource("/zeugnis/pics/delete.png")));
            this.setHorizontalAlignment(JLabel.CENTER);
        }

        if (column == 6) {
            setIcon(new ImageIcon(getClass().getResource("/zeugnis/pics/pdf.png")));
            this.setHorizontalAlignment(JLabel.CENTER);
        }

        return this;
    }

}
