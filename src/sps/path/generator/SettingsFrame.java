package sps.path.generator;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SettingsFrame extends JFrame implements ActionListener, ChangeListener, ItemListener {

    private Properties properties = new Properties();
    private File propertiesFile =  new File(System.getProperty("user.home") + "/.SPSToolBox/","SPSToolBox.properties");
    private boolean safePolygon = true;
    private boolean gameMode = false;
    private Color lineColor = Color.BLUE;
    private Color warnLineColor = Color.RED;
    private int maxLineLength = 65;
    private File lastLoadedFile = null;

    JButton bLineColor = new JButton("Choose Color");
    JTextField slideText = new JTextField(String.valueOf(maxLineLength), 3);

    // Constructor
    public SettingsFrame() {
        super("Settings");
        setResizable(false);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                onClose();
            }
        });

        setPreferredSize(new Dimension(350, 200));
        setLocationRelativeTo(null);

        //Game Mode
        JLabel gameMode = new JLabel("RS Game Mode");
        JRadioButton bOSR = new JRadioButton("OSR");
        JRadioButton bRS3 = new JRadioButton("RS3");
        ButtonGroup bgGameMode = new ButtonGroup();
        bOSR.addActionListener(this);
        bRS3.addActionListener(this);
        bRS3.setSelected(true);
        bgGameMode.add(bOSR);
        bgGameMode.add(bRS3);

        //Max line length
        JLabel maxLineLength = new JLabel("Max Line Length");
        JSlider s_MaxLineLength = new JSlider(SwingConstants.HORIZONTAL, 10, 100, this.maxLineLength);
        s_MaxLineLength.addChangeListener(this);

        //Line color
        JLabel lineColor = new JLabel("Line Color");
        bLineColor.addActionListener(this);

        //Warning line color
        JLabel warnLineColor = new JLabel("Warning Line Color");
        JButton bWarnLineColor = new JButton("Choose Color");
        bWarnLineColor.addActionListener(this);

        //To enable that hackery polygon drawing
        JLabel safePolygon = new JLabel("Enable self-intersecting polygons");
        JCheckBox cSafePolygon = new JCheckBox();
        cSafePolygon.setSelected(false);
        cSafePolygon.addItemListener(this);

        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(3, 5, 3, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        add(gameMode, c);
        c.gridx = 1;
        c.gridy = 0;
        add(bOSR, c);
        c.gridx = 2;
        c.gridy = 0;
        add(bRS3, c);
        c.gridx = 0;
        c.gridy = 1;
        add(maxLineLength, c);
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 1;
        add(s_MaxLineLength, c);
        c.gridwidth = 2;
        c.gridx = 3;
        c.gridy = 1;
        add(slideText, c);
        c.gridx = 0;
        c.gridy = 2;
        add(lineColor, c);
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 2;
        add(bLineColor, c);
        c.gridx = 0;
        c.gridy = 3;
        add(warnLineColor, c);
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 3;
        add(bWarnLineColor, c);
        c.gridx = 0;
        c.gridy = 4;
        add(safePolygon, c);
        c.gridx = 2;
        c.gridy = 4;
        add(cSafePolygon, c);

        pack();
        loadSettings();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()){
            case "OSR":
                if (!getGameMode())
                    setGameMode(true);
                break;
            case "RS3":
                if (getGameMode())
                    setGameMode(false);
                break;
            case "Choose Color":
                if (e.getSource() == bLineColor){
                    setLineColor(pickColor());
                } else {
                    setWarnLineColor(pickColor());
                }
                break;
            default:
                System.out.println(e.getActionCommand());
        }

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider s = (JSlider)e.getSource();
        setMaxLineLength(s.getValue());
        slideText.setText(String.valueOf(getMaxLineLength()));
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        setSafePolygon(e.getStateChange() == ItemEvent.DESELECTED);
    }

    // Setters and Getters
    public int getMaxLineLength(){
        return maxLineLength;
    }

    public void setMaxLineLength(int value){
        maxLineLength = value;
        properties.setProperty("maxLineLength", "" + value);
    }

    public boolean getSafePolygon(){
        return safePolygon;
    }

    public void setSafePolygon(Boolean b){
        safePolygon = b;
        properties.setProperty("safePolygon" , "" + b);
    }

    public boolean getGameMode(){
        return gameMode;
    }

    public void setGameMode(Boolean OSR){
        gameMode = OSR;
        properties.setProperty("gameMode" , "" + OSR);
    }

    public Color getLineColor(){
        return lineColor;
    }

    public void setLineColor(Color col){
        lineColor = col;
        properties.setProperty("lineColor" , "" + col.getRGB());
    }

    public Color getWarnLineColor(){
        return warnLineColor;
    }

    public void setWarnLineColor(Color col){
        warnLineColor = col;
        properties.setProperty("warnLineColor" , "" + col.getRGB());
    }

    public void setLastLoadedFile(File newFile){
        if (newFile == null)
            return;
        lastLoadedFile = newFile;
        properties.setProperty("lastLoadedFile", lastLoadedFile.getAbsolutePath());
    }

    public File getLastLoadedFile() {
        return lastLoadedFile;
    }

    private Color pickColor() {
        return JColorChooser.showDialog(this, "Choose a new color" , Color.RED);
    }

    private void loadSettings() {
        // Load the settings from file
        if (!propertiesFile.exists()) {
            try {
                propertiesFile.getParentFile().mkdirs();
                propertiesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                properties.load(new FileInputStream(propertiesFile));

                if (properties.contains("lastLoadedFile"))
                    setLastLoadedFile(new File(properties.getProperty("lastLoadedFile")));
                else
                    setLastLoadedFile(lastLoadedFile);

                if (properties.contains("gameMode"))
                    setGameMode(Boolean.getBoolean(properties.getProperty("gameMode")));
                else
                    setGameMode(getGameMode());

                if (properties.contains("maxLineLength"))
                    setMaxLineLength(Integer.getInteger(properties.getProperty("maxLineLength")));
                else
                    setMaxLineLength(getMaxLineLength());

                if (properties.contains("lineColor"))
                    setLineColor(new Color(Integer.getInteger(properties.getProperty("lineColor"))));
                else
                    setLineColor(getLineColor());

                if (properties.contains("warnLineColor"))
                    setWarnLineColor(new Color(Integer.getInteger(properties.getProperty("warnLineColor"))));
                else
                    setWarnLineColor(getWarnLineColor());

                if (properties.contains("safePolygon"))
                    setSafePolygon(Boolean.getBoolean(properties.getProperty("safePolygon")));
                else
                    setSafePolygon(getSafePolygon());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onClose() {
        //Save settings to a file
        if (!propertiesFile.exists())
            try {
                propertiesFile.createNewFile();
            } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream out = new FileOutputStream(propertiesFile);
            properties.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        new SettingsFrame().setVisible(true);
    }

}
