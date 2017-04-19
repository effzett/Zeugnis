/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.JOptionPane;

/**
 *
 * @author juergen
 */
public class DialogHandler extends Handler {

    @Override
    public void publish(LogRecord record) {
        int messageType = JOptionPane.INFORMATION_MESSAGE;

        if (record.getLevel() == Level.SEVERE) {
            messageType = JOptionPane.ERROR_MESSAGE;
        } else if (record.getLevel() == Level.WARNING) {
            messageType = JOptionPane.WARNING_MESSAGE;
        }

        if (isLoggable(record)) {
            JOptionPane.showMessageDialog(null, record.getMessage(), null, messageType);
        }

    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() throws SecurityException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
