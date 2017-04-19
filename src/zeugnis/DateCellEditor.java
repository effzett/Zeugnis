/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
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
public class DateCellEditor extends AbstractCellEditor implements TableCellEditor {

    private final static Logger logger = Logger.getLogger(DateCellEditor.class.getName());
    private Object value = null;
    private JDialog dialog = null;
    JDatePickerImpl datePicker = null;

    public DateCellEditor() {

    }

    //Implement the one method defined by TableCellEditor.
    public Component getTableCellEditorComponent(JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {

        this.value = value;
        Point mposition = MouseInfo.getPointerInfo().getLocation();
        dialog = new JDialog();
        UtilDateModel model = new UtilDateModel();
        String date  = (String)value;
        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5));
        int year = Integer.parseInt(date.substring(6));
        logger.fine("Date: " + day + "." + month + "." + year);
        model.setDate(year, month, day);
        model.setSelected(true);

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);

        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        dialog.getContentPane().add(datePicker, java.awt.BorderLayout.CENTER);
        JButton button = new JButton("OK");
        
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeDialog();
            }
        });
        
        dialog.getContentPane().add(button, java.awt.BorderLayout.EAST);
        dialog.pack();
        dialog.setLocation(mposition);
        dialog.setVisible(true);

        return new JLabel((String) value);
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            int clickCountToStart = 2;
            return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }

    @Override
    public Object getCellEditorValue() {
        return value;
    }
  
    private void closeDialog() {
        Date date = (Date)datePicker.getModel().getValue();
        String datePattern = "dd.MM.yyyy";
        SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
        value = dateFormatter.format(date);
        logger.fine(dateFormatter.format(date));
        dialog.dispose();
    }
    
    public class DateLabelFormatter extends AbstractFormatter {

        private String datePattern = "dd.MM.yyyy";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }

    }

}
