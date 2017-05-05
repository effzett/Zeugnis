/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeugnis;

import java.awt.Color;
import java.awt.Component;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author juergen
 */
public class Gui extends javax.swing.JFrame implements TableModelListener {

    private final static Logger logger = Logger.getLogger(Gui.class.getName());
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private static int sYear = 0;
    private static int hYear = 0;
    private static String sClass = null;
    private SingletonSQLConnector connector = null;
    private Config config = null;
    private boolean tableModelEventEnabled = true;

    /**
     * Creates new form Gui
     */
    public Gui() {
        config = Config.getInstance();
        connector = SingletonSQLConnector.getInstance();
        initComponents();
        sYear = Integer.parseInt(((String) jComboBox1.getSelectedItem()).substring(0, 4));
        hYear = Integer.parseInt((String) jComboBox2.getSelectedItem());
        sClass = (String) jComboBox3.getSelectedItem();
        fillClassTable();
        fillSubjectComboBox();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                changeTab(evt);
            }
        });

        // Da sich die Schueler id (Primary Key) aus den Spalten Name,
        // Vorname oder Geburtdatum zusammensetzt, kann sie nicht mehr ermittelt
        // werden wenn eines der Felder editiert wird. Daher muß die Id in der
        // ersten verdeckten Spalte mitgeführt und ggf nach dem Insert oder Update
        // eines Schuelers auf Stand gehalten werden.
        DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(
            new String [] {
                "Id", "Name", "Vorname", "Geburtsdatum", "Geburtsort", "", ""
            }, 7);
            tableModel.addTableModelListener(this);
            jTable1.setModel(tableModel);
            jTable1.setCellSelectionEnabled(true);
            jTable1.setRowHeight(20);
            jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            TableColumn column = jTable1.getColumnModel().getColumn(0);
            column.setWidth(0);
            column.setMaxWidth(0);
            column = jTable1.getColumnModel().getColumn(3);
            column.setCellEditor(new zeugnis.DateCellEditor());
            column = jTable1.getColumnModel().getColumn(5);
            column.setCellEditor(new zeugnis.DeleteCellEditor());
            column.setCellRenderer(new IconCellRenderer());
            column = jTable1.getColumnModel().getColumn(6);
            column.setCellEditor(new PdfCellEditor());
            column.setCellRenderer(new IconCellRenderer());
            jScrollPane1.setViewportView(jTable1);

            jButton1.setText("Neue Zeile");
            jButton1.setActionCommand("Neu");
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    addRow(evt);
                }
            });

            jButton3.setText("Pdf's erzeugen");
            jButton3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    createPdfForClass(evt);
                }
            });

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jButton1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton3)
                    .addContainerGap(505, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 707, Short.MAX_VALUE))
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addGap(0, 374, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton3)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 40, Short.MAX_VALUE)))
            );

            jTabbedPane1.addTab("Schulklassen", jPanel2);

            jLabel4.setText("Schüler");

            jComboBox4.setRenderer(new DefaultListCellRenderer(){

                public Component getListCellRendererComponent(JList list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                    if(value instanceof String[]){
                        String[] puple = (String[])value;
                        setText(puple[0]);
                    }
                    /*
                    Color background;
                    Color foreground;

                    // check if this cell represents the current DnD drop location
                    JList.DropLocation dropLocation = list.getDropLocation();
                    if (dropLocation != null
                        && !dropLocation.isInsert()
                        && dropLocation.getIndex() == index) {

                        background = Color.BLUE;
                        foreground = Color.WHITE;

                        // check if this cell is selected
                    } else if (isSelected) {
                        background = Color.RED;
                        foreground = Color.WHITE;

                        // unselected, and not the DnD drop location
                    } else {
                        background = Color.WHITE;
                        foreground = Color.BLACK;
                    };

                    setBackground(background);
                    setForeground(foreground);
                    */
                    return this;
                }

            });
            jComboBox4.setModel(new javax.swing.DefaultComboBoxModel());

            jLabel5.setText("Lernentwicklungsbericht");

            jTextArea1.setColumns(20);
            jTextArea1.setRows(5);
            jScrollPane2.setViewportView(jTextArea1);

            jLabel6.setText("Bemerkungen");

            jTextField1.setColumns(20);
            jTextField1.setText("Wird versetzt nach Klasse...");

            jLabel7.setText("Fehltage");

            jSpinner1.setModel(new javax.swing.SpinnerNumberModel());

            jLabel8.setText("entschuldigte Fehltage");

            jSpinner2.setModel(new javax.swing.SpinnerNumberModel());

            jTable2.setModel(new javax.swing.table.DefaultTableModel(
                new String [] {
                    "Kriterien", "Bewertung"
                }, 2)
            );
            jTable2.setRowHeight(20);
            jTable2.setRowSelectionAllowed(false);
            jTable2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            column = jTable2.getColumnModel().getColumn(1);
            //column.setCellRenderer(new zeugnis.ComboBoxCellRenderer());
            column.setCellEditor(new DefaultCellEditor(getMarkBox()));
            jScrollPane3.setViewportView(jTable2);

            jLabel9.setText("Fächer");

            jComboBox5.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    changeSubject(evt);
                }
            });

            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
            jPanel3.setLayout(jPanel3Layout);
            jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel5)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9)
                                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addContainerGap(13, Short.MAX_VALUE))
            );

            jTabbedPane1.addTab("Zeugnisse", jPanel3);

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTabbedPane1)
                    .addContainerGap())
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1)
            );

            jLabel1.setText("Schuljahr");

            try{
                jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(connector.fetchSYears()));
            } catch(SQLException ex) {
                logger.severe(ex.getLocalizedMessage());
            }
            jComboBox1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    changeSYear(evt);
                }
            });

            jLabel2.setText("Halbjahr");

            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2" }));
            jComboBox2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    changeHYear(evt);
                }
            });
            jComboBox2.setEnabled((jTabbedPane1.getSelectedIndex() == 0) ? false : true);

            jLabel3.setText("Klasse");

            jComboBox3.setModel(new javax.swing.DefaultComboBoxModel( config.getProperty("classes").split(",")));
            jComboBox3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    changeSClass(evt);
                }
            });

            jMenu1.setText("Datei");

            jMenuItem1.setText("Beenden");
            jMenu1.add(jMenuItem1);

            jMenuBar1.add(jMenu1);

            jMenu2.setText("Bearbeiten");

            jMenuItem2.setText("Neues Schuljahr anlegen");
            jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    addSchoolYear(evt);
                }
            });
            jMenu2.add(jMenuItem2);

            jMenuBar1.add(jMenu2);

            setJMenuBar(jMenuBar1);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel3)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void fillClassTable() {
        String sYear = (String) jComboBox1.getSelectedItem();
        String sClass = (String) jComboBox3.getSelectedItem();

        try {
            connector.fillClassTable(jTable1, Integer.parseInt(sYear.substring(0, 4)), sClass);
        } catch (SQLException ex) {
            logger.severe(ex.getLocalizedMessage());
        }

    }

    private void addRow(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRow
        String datePattern = "dd.MM.yyyy";
        SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
        Object[] row = {"", "", "",
            dateFormatter.format(Calendar.getInstance().getTime()), "",};
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.addRow(row);
    }//GEN-LAST:event_addRow


    private void changeSYear(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeSYear
        sYear = Integer.parseInt(((String) jComboBox1.getSelectedItem()).substring(0, 4));
        fillPupleComboBox();
        fillSubjectComboBox();    
    }//GEN-LAST:event_changeSYear

    private void changeHYear(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeHYear
        hYear = Integer.parseInt((String) jComboBox2.getSelectedItem());

    }//GEN-LAST:event_changeHYear

    private void changeSClass(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeSClass
        sClass = (String) jComboBox3.getSelectedItem();
        fillPupleComboBox();
        fillSubjectComboBox();        
    }//GEN-LAST:event_changeSClass

    private void createPdfForClass(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createPdfForClass
        // TODO add your handling code here:
    }//GEN-LAST:event_createPdfForClass

    private void addSchoolYear(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSchoolYear
        String result = (String) JOptionPane.showInputDialog(
                this,
                "Tragen Sie das Schuljahr im Format yyyy/yy (z.B 2015/16) ein",
                "Neues Schuljahr anlegen",
                JOptionPane.PLAIN_MESSAGE);

        if ((result != null) && (result.length() > 0)) {

            if (result.matches("[0-9]{4}/[0-9]{2}")) {

            } else {
                logger.warning("Der eingegebene Wert entspricht nicht dem Format yyyy/yy (z.B 2015/16)");
            }

        }
        
    }//GEN-LAST:event_addSchoolYear

    private void changeTab(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_changeTab

        if (jTabbedPane1.getSelectedIndex() == 1) {
            fillPupleComboBox();
        }

    }//GEN-LAST:event_changeTab

    private void changeSubject(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeSubject
        fillTestimonyTable((String)jComboBox5.getSelectedItem());
    }//GEN-LAST:event_changeSubject

    /**
     * Die ComboBox wird neu befuellt wenn: Auf den Reiter Zeugnis gewechselt
     * wird um den aktuellen Stand zu haben falls im Reiter Schulklassen Schüler
     * ediert, hinzugefuegt oder geloescht wurden. Ein anderes Schuljahr
     * ausgewählt wird. ComboBox1 Schuljahr Eine andere Klasse ausgewählt wird.
     * ComboBox3 Klasse.
     * Auf die ID des Schuelers kann uber (String[])jComboBox4getSelectedItem zugegrigffen
     * werden. Das zweite Feld im String ist die ID_SCHUELER.
     */
    private void fillPupleComboBox() {
        jComboBox4.removeAllItems();

        try {
            ArrayList[] fetchedPuples = connector.fetchPuples(sYear, sClass);

            for (int i = 0; i < fetchedPuples.length; i++) {
                ArrayList<String> puple = fetchedPuples[i];
                jComboBox4.insertItemAt(new String[]{(puple.get(1) + ", " + puple.get(2)), puple.get(0)}, i);
            }

            jComboBox4.setSelectedIndex(0);
        } catch (SQLException ex) {
            logger.severe(ex.getLocalizedMessage());
        }

    }
    
    /**
     * Fuellt die jComboBox5 mit den entsprechenden Fächern. Abhaengig
     * von Klasse und Schuljahr.
     * In Abhaengigkeit des Faches wird die Tabelle zur politisch
     * korrrekten Benotung gefuellt.
     */
    private void fillSubjectComboBox() {
        
        try {
            jComboBox5.setModel(new DefaultComboBoxModel(connector.getLernbereiche().toArray(new String[0])));
        } catch (SQLException ex) {
            logger.severe(ex.getLocalizedMessage());
        }
      
        fillTestimonyTable((String)jComboBox5.getItemAt(0));
    }
    
    private void fillTestimonyTable(String subject) {
        
        try {
            ArrayList<String> criteria = connector.getKriterien(0, subject);
            DefaultTableModel model = (DefaultTableModel)jTable2.getModel();
            model.setRowCount(0);
            Object[] row = new Object[2];
            Iterator<String> it = criteria.iterator();
                        
            while(it.hasNext()) {
                row[0] = it.next();
                row[1]= getMarkBox();
                model.addRow(row);
            }
            
        } catch (SQLException ex) {
            logger.severe(ex.getLocalizedMessage());
        }
        
    }

    // Gibt eine neue Instanz eines Noten PulldownMenues zurueck.
    private JComboBox getMarkBox() {
        JComboBox markComboBox = new JComboBox();
        markComboBox.setRenderer(new DefaultListCellRenderer() {

            public Component getListCellRendererComponent(JList list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof java.lang.String) {
                    setText((String) value);
                    setIcon(null);
                } else if (value instanceof javax.swing.ImageIcon) {
                    setText("");
                    setIcon((ImageIcon) value);
                } else {
                    logger.severe("Unerwartetes Object beim Rendern der ComboBox");
                }

                return this;
            }

        });

        markComboBox.setModel(new javax.swing.DefaultComboBoxModel(new Object[]{
            "Zeile1",
            "Zeile2",
            "Zeile3",
            new ImageIcon(getClass().getResource("/zeugnis/pics/leer.png")),
            new ImageIcon(getClass().getResource("/zeugnis/pics/viertel.png")),
            new ImageIcon(getClass().getResource("/zeugnis/pics/halb.png")),
            new ImageIcon(getClass().getResource("/zeugnis/pics/dreiviertel.png")),
            new ImageIcon(getClass().getResource("/zeugnis/pics/voll.png"))
        }));

        return markComboBox;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void tableChanged(TableModelEvent e) {

        if (e.getType() == TableModelEvent.UPDATE && tableModelEventEnabled) {
            int row = e.getFirstRow();
            TableModel model = (TableModel) e.getSource();
            int idColumn = 0;

            // Prüfen ob die Spalten Name, Vorname, Geburtsdatun Werte enthalten um eine idSchueler zu generieren
            // Wenn ja, über rows ermitteln ob es sich um ein update oder ein insert handelt.
            Object data0 = null, data1 = null, data2 = null, data3 = null, data4 = null;
            String string1, string2, string3;

            for (int i = 0; i < model.getColumnCount(); i++) {

                switch (model.getColumnName(i)) {
                    case "Id":
                        data0 = model.getValueAt(row, i);
                        idColumn = i;
                        break;
                    case "Name":
                        data1 = model.getValueAt(row, i);
                        break;
                    case "Vorname":
                        data2 = model.getValueAt(row, i);
                        break;
                    case "Geburtsdatum":
                        data3 = model.getValueAt(row, i);
                        break;
                    case "Geburtsort":
                        data4 = model.getValueAt(row, i);
                        break;
                }

            }

            if (data1 != null && !(string1 = (String) data1).isEmpty()
                    && data2 != null && !(string2 = (String) data2).isEmpty()
                    && data3 != null && !(string3 = (String) data3).isEmpty()) {
                logger.fine("column0: " + string1 + " column1: " + string2 + " column2: " + string3);

                // Datumstring zu SQLDatum konvertieren
                Date date = null;
                java.sql.Date sqlDate = null;

                try {
                    date = sdf.parse(string3);
                    sqlDate = new java.sql.Date(date.getTime());
                } catch (ParseException ex) {
                    logger.severe(ex.getLocalizedMessage());
                }

                String[] values = new String[7];
                values[0] = Integer.toString((string1 + string2 + sqlDate.toString() + sYear).hashCode());
                values[1] = string1;
                values[2] = string2;
                values[3] = string3;
                values[4] = (String) data4;
                values[5] = (String) jComboBox3.getSelectedItem();
                values[6] = ((String) jComboBox1.getSelectedItem()).substring(0, 4);

                try {
                    tableModelEventEnabled = false;
                    // insert
                    if (data0 == null || ((String) data0).isEmpty()) {

                        connector.insertPuple(values);

                        // Die neue Id in die erste Spalte der Tabelle schreiben.
                        model.setValueAt(values[0], row, idColumn);

                        // update    
                    } else {
                        connector.updatePuple(values, (String) data0);

                        // Die neue Id in die erste Spalte der Tabelle schreiben.
                        jTable1.getModel().setValueAt(values[0], row, idColumn);
                    }

                    tableModelEventEnabled = true;
                } catch (SQLException ex) {
                    logger.severe(ex.getLocalizedMessage());
                }

            }

        }

    }

    /*
     class ComboBoxRenderer extends JLabel
     implements ListCellRenderer {

     public ComboBoxRenderer() {
     setOpaque(true);
     setHorizontalAlignment(CENTER);
     setVerticalAlignment(CENTER);
     }

     @Override
     public Component getListCellRendererComponent(
     JList list,
     Object value,
     int index,
     boolean isSelected,
     boolean cellHasFocus) {

     if (isSelected) {
     setBackground(list.getSelectionBackground());
     setForeground(list.getSelectionForeground());
     } else {
     setBackground(list.getBackground());
     setForeground(list.getForeground());
     }

     if (value instanceof java.lang.String) {
     setText((String) value);
     setIcon(null);
     } else if (value instanceof javax.swing.ImageIcon) {
     setText("");
     setIcon((ImageIcon) value);
     } else {
     logger.severe("Unerwartetes Object beim rendern der ComboBox");
     }

     return this;
     }

     }

     */
    public static int getSYear() {
        //return (String)jComboBox1.getSelectedItem();
        return sYear;
    }

    public static int getHYear() {
        //return (String)jComboBox2.getSelectedItem();
        return hYear;
    }

    public static String getSClass() {
        return sClass;
    }

}
