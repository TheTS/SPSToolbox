package sps.path.generator;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.UIManager;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Int
 */
public class SettingsFrame extends javax.swing.JFrame  {

    private Color pointCol = Color.RED;
    private Color selPointCol = Color.WHITE;
    private Color warnLineCol = Color.RED;
    private Color lineCol = Color.RED;
    private Properties properties = new Properties();
    private File propertiesFile =
                 new File(System.getProperty("user.home") + "/.SPSPathGen/","SPSPathGen.properties");
    private int fogOpacity = 50;
    private int maxLineLength = 70;
    private boolean extraAreas = true;
    private boolean validateAreas = true;
    private File lastLoadedFile = null;

    /**
     * Creates new form SettingsFrame
     */
    public SettingsFrame() {
        initComponents();
        pack();
        loadSettings();
    }

    @Override
    public void setVisible(boolean b) {
        if (!isVisible()) {
            pack();
            validate();
        }
        super.setVisible(b);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lbl_MaxLineLength = new javax.swing.JLabel();
        spnr_MaxLineLength = new javax.swing.JSpinner();
        lbl_FogOpacity = new javax.swing.JLabel();
        spnr_FogOpacity = new javax.swing.JSpinner();
        lbl_Linecol = new javax.swing.JLabel();
        btn_Linecol = new javax.swing.JButton();
        lbl_Linecol1 = new javax.swing.JLabel();
        btn_WarningLineCol = new javax.swing.JButton();
        lbl_Linecol2 = new javax.swing.JLabel();
        btn_SelPointCol = new javax.swing.JButton();
        lbl_PointCol = new javax.swing.JLabel();
        btn_PointCol = new javax.swing.JButton();
        lbl_ExtraAreas = new javax.swing.JLabel();
        btn_ExtraAreas = new javax.swing.JToggleButton();
        lvl_ValidateAreas = new javax.swing.JLabel();
        btn_ValidateAreas = new javax.swing.JToggleButton();

        setTitle("Settings");
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        lbl_MaxLineLength.setText("Max Line Length");
        lbl_MaxLineLength.setToolTipText("<html>\nSpecifies how long a line can be before it will be considered too long to be succesfully walked to.\n<br />\nLines longer than this limit will be shown as bold, and dashed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(lbl_MaxLineLength, gridBagConstraints);

        spnr_MaxLineLength.setModel(new javax.swing.SpinnerNumberModel(15, 15, 76, 1));
        spnr_MaxLineLength.setMinimumSize(new java.awt.Dimension(60, 20));
        spnr_MaxLineLength.setPreferredSize(new java.awt.Dimension(60, 20));
        spnr_MaxLineLength.setValue(70);
        spnr_MaxLineLength.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnr_MaxLineLengthStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(spnr_MaxLineLength, gridBagConstraints);

        lbl_FogOpacity.setText("Fog Opacity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(lbl_FogOpacity, gridBagConstraints);

        spnr_FogOpacity.setModel(new javax.swing.SpinnerNumberModel(50, 0, 100, 1));
        spnr_FogOpacity.setMinimumSize(new java.awt.Dimension(60, 20));
        spnr_FogOpacity.setPreferredSize(new java.awt.Dimension(60, 20));
        spnr_FogOpacity.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnr_FogOpacityStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(spnr_FogOpacity, gridBagConstraints);

        lbl_Linecol.setText("Line Color");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(lbl_Linecol, gridBagConstraints);

        btn_Linecol.setText("Choose Color...");
        btn_Linecol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChooseColor(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(btn_Linecol, gridBagConstraints);

        lbl_Linecol1.setText("Warning Line Color");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(lbl_Linecol1, gridBagConstraints);

        btn_WarningLineCol.setText("Choose Color...");
        btn_WarningLineCol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_WarningLineColActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(btn_WarningLineCol, gridBagConstraints);

        lbl_Linecol2.setText("Selected Point Color");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(lbl_Linecol2, gridBagConstraints);

        btn_SelPointCol.setText("Choose Color...");
        btn_SelPointCol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChooseColor(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(btn_SelPointCol, gridBagConstraints);

        lbl_PointCol.setText("Point Color");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(lbl_PointCol, gridBagConstraints);

        btn_PointCol.setText("Choose Color...");
        btn_PointCol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PointColActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(btn_PointCol, gridBagConstraints);

        lbl_ExtraAreas.setText("Include surrounding areas in generation:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(lbl_ExtraAreas, gridBagConstraints);

        btn_ExtraAreas.setSelected(true);
        btn_ExtraAreas.setText("Enabled");
        btn_ExtraAreas.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                btn_ExtraAreasStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(btn_ExtraAreas, gridBagConstraints);

        lvl_ValidateAreas.setText("Validate Areas(Disable if your area generation does not work)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(lvl_ValidateAreas, gridBagConstraints);

        btn_ValidateAreas.setText("Enabled");
        btn_ValidateAreas.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                btn_ValidateAreasStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(btn_ValidateAreas, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ChooseColor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChooseColor
        if (btn_Linecol.equals(evt.getSource())) {
            setLineCol(JColorChooser.showDialog(this , "Choose a new line color" , Color.RED));
        } else if (btn_SelPointCol.equals(evt.getSource())) {
            setSelPointCol(JColorChooser.showDialog(this , "Choose a new selected point color" , Color.RED));
        } else if (btn_PointCol.equals(evt.getSource())) {
            setPointCol(JColorChooser.showDialog(this , "Choose a new point color" , Color.RED));
        } else if (btn_WarningLineCol.equals(evt.getSource())) {
            setWarnLineCol(JColorChooser.showDialog(this , "Choose a new warning color" , Color.RED));
        }

        System.out.println(lineCol);
        System.out.println(warnLineCol);
        System.out.println(selPointCol);
        System.out.println(pointCol);
    }//GEN-LAST:event_ChooseColor

    private void btn_WarningLineColActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_WarningLineColActionPerformed
        ChooseColor(evt);
    }//GEN-LAST:event_btn_WarningLineColActionPerformed

    private void btn_PointColActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PointColActionPerformed
        ChooseColor(evt);
    }//GEN-LAST:event_btn_PointColActionPerformed

    private void spnr_FogOpacityStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnr_FogOpacityStateChanged
        setFogOpacity((Integer)spnr_FogOpacity.getValue());
    }//GEN-LAST:event_spnr_FogOpacityStateChanged

    private void spnr_MaxLineLengthStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnr_MaxLineLengthStateChanged
        setMaxLineLength((Integer)spnr_MaxLineLength.getValue());
    }//GEN-LAST:event_spnr_MaxLineLengthStateChanged

    private void btn_ExtraAreasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_btn_ExtraAreasStateChanged
        if (btn_ExtraAreas.isSelected()){
            btn_ExtraAreas.setText("Enabled");
        } else {
            btn_ExtraAreas.setText("Disabled");
        }
        setExtraAreas(btn_ExtraAreas.isSelected());
    }//GEN-LAST:event_btn_ExtraAreasStateChanged

    private void btn_ValidateAreasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_btn_ValidateAreasStateChanged
        if (btn_ValidateAreas.isSelected()){
            btn_ValidateAreas.setText("Enabled");
        } else {
            btn_ValidateAreas.setText("Disabled");
        }
        setValidateAreas(btn_ValidateAreas.isSelected());
    }//GEN-LAST:event_btn_ValidateAreasStateChanged

    public boolean getValidateAreas(){
        return validateAreas;
    }
    
    public void setValidateAreas(boolean newVal){
        validateAreas = newVal;
        btn_ValidateAreas.setSelected(validateAreas);
        properties.setProperty("Validate Areas", "" + validateAreas);
    }
    
    public boolean getExtraAreas(){
        return extraAreas;
    }
    
    public void setExtraAreas(boolean newVal){
        extraAreas = newVal;
        btn_ExtraAreas.setSelected(extraAreas);
        properties.setProperty("Extra Areas", "" + extraAreas);
    }
    
    public int getMaxLineLength() {
        return maxLineLength;
    }
    
    public void setMaxLineLength(int newVal){
        if (newVal < 0)
            return;
        maxLineLength = newVal;
        spnr_MaxLineLength.setValue(maxLineLength);
        properties.setProperty("Max Line Length", "" + maxLineLength);
    }

    public int getFogOpacity() {
        return fogOpacity;
    }
    
    public void setFogOpacity(int newVal){
        if (newVal > 100 || newVal < 0)
            return;
        fogOpacity = newVal;
        spnr_FogOpacity.setValue(fogOpacity);
        properties.setProperty("Fog Opacity", "" + fogOpacity);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SettingsFrame.class.getName()).log(java.util.logging.Level.SEVERE , null , ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SettingsFrame.class.getName()).log(java.util.logging.Level.SEVERE , null , ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SettingsFrame.class.getName()).log(java.util.logging.Level.SEVERE , null , ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SettingsFrame.class.getName()).log(java.util.logging.Level.SEVERE , null , ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new SettingsFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btn_ExtraAreas;
    private javax.swing.JButton btn_Linecol;
    private javax.swing.JButton btn_PointCol;
    private javax.swing.JButton btn_SelPointCol;
    private javax.swing.JToggleButton btn_ValidateAreas;
    private javax.swing.JButton btn_WarningLineCol;
    private javax.swing.JLabel lbl_ExtraAreas;
    private javax.swing.JLabel lbl_FogOpacity;
    private javax.swing.JLabel lbl_Linecol;
    private javax.swing.JLabel lbl_Linecol1;
    private javax.swing.JLabel lbl_Linecol2;
    private javax.swing.JLabel lbl_MaxLineLength;
    private javax.swing.JLabel lbl_PointCol;
    private javax.swing.JLabel lvl_ValidateAreas;
    private javax.swing.JSpinner spnr_FogOpacity;
    private javax.swing.JSpinner spnr_MaxLineLength;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the selPointCollineCol
     */
    public Color getSelPointCol() {
        return selPointCol;
    }

    /**
     * @param selPointCollineCol the selPointCollineCol to set
     */
    public void setSelPointCol(Color selPointCol) {
        if (selPointCol == null)
            return;
        this.selPointCol = selPointCol;
        String selPointColString = "" + selPointCol.getRGB();
        properties.setProperty("Selected Point Color" , selPointColString);
    }

    /**
     * @return the warnLineCol
     */
    public Color getWarnLineCol() {
        return warnLineCol;
    }

    /**
     * @param warnLineCol the warnLineCol to set
     */
    public void setWarnLineCol(Color warnLineCol) {
        if (warnLineCol == null)
            return;
        this.warnLineCol = warnLineCol;
        String warnLineColString = "" + warnLineCol.getRGB();
        properties.setProperty("Warning Line Color" , warnLineColString);
    }

    /**
     * @return the lineCol
     */
    public Color getLineCol() {
        return lineCol;
    }

    /**
     * @param lineCol the lineCol to set
     */
    public void setLineCol(Color lineCol) {
        if (lineCol == null) 
            return;
        this.lineCol = lineCol;
        String lineColString = "" + lineCol.getRGB();
        properties.setProperty("Line Color" , lineColString);
    }

    /**
     * @return the pointCol
     */
    public Color getPointCol() {
        return pointCol;
    }

    /** 
     * @param pointCol the pointCol to set
     */
    public void setPointCol(Color pointCol) {
        if (pointCol == null)
            return;
        this.pointCol = pointCol;
        String pointColString = "" + pointCol.getRGB();
        properties.setProperty("Point Color" , pointColString);
    }
    
    public void setLastLoadedFile(File newFile){
        if (newFile == null)
            return;
        lastLoadedFile = newFile;
        properties.setProperty("Last Loaded File", lastLoadedFile.getAbsolutePath());
    }
    
    public File getLastLoadedFile() {
        return lastLoadedFile;
    }

    /**
     * Loads Settings from the properties file, stored in the users home
     * directory
     */
    private void loadSettings() {
        if (!propertiesFile.exists()) {
            try {
                propertiesFile.getParentFile().mkdirs();
                propertiesFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(SettingsFrame.class.getName()).log(Level.SEVERE , null , ex);
            }
            return;
        }
        try {
//          The template for these are as follows:
//          if (properties contains this key) then
//              set some value to to value of this key
//          else
//              call this values setter method, so it writes this value to the properties file
            properties.load(new FileInputStream(propertiesFile));
            if (properties.containsKey("Point Color")){
                setPointCol(new Color(Integer.valueOf(
                        properties.getProperty("Point Color")) , true));
            } else {
                setPointCol(pointCol);
            }
            if (properties.containsKey("Line Color")){
                setLineCol(new Color(Integer.valueOf(
                        properties.getProperty("Line Color")).intValue() , true));
            } else {
                setLineCol(lineCol);
            }
            if (properties.containsKey("Selected Point Color")){
                setSelPointCol(new Color(Integer.valueOf(
                        properties.getProperty("Selected Point Color")).intValue() , true));
            } else {
                setSelPointCol(selPointCol);
            }
            if (properties.containsKey("Warning Line Color")){
                setWarnLineCol(new Color(Integer.valueOf(
                        properties.getProperty("Warning Line Color")).intValue() , true));
            }else {
                setWarnLineCol(warnLineCol);
            }
            if (properties.containsKey("Fog Opacity")){
                setFogOpacity(Integer.valueOf(
                        properties.getProperty("Fog Opacity")));
            } else {
                setFogOpacity(fogOpacity);
            }
            if (properties.containsKey("Max Line Length")){
                setMaxLineLength(Integer.valueOf(
                        properties.getProperty("Max Line Length")));
            } else {
                setMaxLineLength(maxLineLength);
            }
            if (properties.containsKey("Extra Areas")){
                setExtraAreas(Boolean.valueOf(properties.getProperty("Extra Areas")));
            } else {
                setExtraAreas(extraAreas);
            }
            if (properties.containsKey("Validate Areas")){
                setValidateAreas(Boolean.valueOf(properties.getProperty("Validate Areas")));
            } else {
                setValidateAreas(extraAreas);
            }
            if (properties.containsKey("Last Loaded File")){
                setLastLoadedFile(new File(properties.getProperty("Last Loaded File")));
            } else {
                setLastLoadedFile(lastLoadedFile);
            }
        } catch (Exception ex) {
            Logger.getLogger(SettingsFrame.class.getName()).log(Level.SEVERE , null , ex);
        }
    }
    
    public void onExit() {
        propertiesFile.delete();
        try {
            propertiesFile.createNewFile();
            if (!propertiesFile.exists())
                return;
            FileOutputStream fio = new FileOutputStream(propertiesFile);
            properties.store(fio, "");
            fio.close();
        } catch (Exception ex) {
            Logger.getLogger(SettingsFrame.class.getName()).log(Level.SEVERE , null , ex);
        }
    }
}