/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author juergen
 */
public class GuiTest {

    // Fill the class table
    public void fillClassTable(JTable classTable) {
        //ImageIcon delete = new ImageIcon(getClass().getResource("delete-button.icon.png"));
        Object[][] classTableContent = new Object[][]{
            {"Müller", "Kevin", "10.01.2010", "Hannover", new ImageIcon(getClass().getResource("/zeugnis/pics/delete.png")), new ImageIcon(getClass().getResource("/zeugnis/pics/pdf.png"))},
            {"Meier", "Leonie-Sophie", "02.02.2011", "Berlin", new ImageIcon(getClass().getResource("/zeugnis/pics/delete.png")), new ImageIcon(getClass().getResource("/zeugnis/pics/pdf.png"))},
            {"Trump", "Florian-Claude", "03.03.2012", "New York", new ImageIcon(getClass().getResource("/zeugnis/pics/delete.png")), new ImageIcon(getClass().getResource("/zeugnis/pics/pdf.png"))}
        };

        DefaultTableModel model = (DefaultTableModel) classTable.getModel();
        model.setRowCount(0);

        for (Object[] row : classTableContent) {
            model.addRow(row);
        }

    }

    // fill the testimonyTable
    public void fillTestimonyTable(JTable testimonyTable) {

        // Objects for the ComboBox
        
        Object[] comboBoxContent = new Object[]{
            "Zeile1",
            "Zeile2",
            "Zeile3",
            new ImageIcon(getClass().getResource("/zeugnis/pics/leer.png")),
            new ImageIcon(getClass().getResource("/zeugnis/pics/viertel.png")),
            new ImageIcon(getClass().getResource("/zeugnis/pics/halb.png")),
            new ImageIcon(getClass().getResource("/zeugnis/pics/dreiviertel.png")),
            new ImageIcon(getClass().getResource("/zeugnis/pics/voll.png"))
        };
        
        
        Object[][] testimonyTableContent = new Object[][]{
            {"Fach1 blabla", "Zeile1"},
            {"Fach2 blabla", new ImageIcon(getClass().getResource("/zeugnis/pics/halb.png"))}
        };

        DefaultTableModel model = (DefaultTableModel) testimonyTable.getModel();
        model.setRowCount(0);

        for (Object[] row : testimonyTableContent) {
            model.addRow(row);
        }

    }

}
