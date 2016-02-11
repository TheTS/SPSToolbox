package toolbox;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

class SettingsFrame extends JFrame implements ActionListener, ChangeListener, ItemListener {

    private final Properties properties = new Properties();
    private final File propertiesFile = new File(System.getProperty("user.home") + "/.SPSToolBox/", "SPSToolBox.properties");
    private final JButton bLineColor = new JButton("Choose Color");
    private final JButton bFillColor = new JButton("Choose Color");
    private boolean safePolygon = true;
    private boolean gameMode = true;
    private Color lineColor = Color.BLUE;
    private Color warnLineColor = Color.RED;
    private int maxLineLength = 65;
    private final JTextField slideText = new JTextField(String.valueOf(maxLineLength), 3);
    private File lastLoadedFile = null;
    private Color fillColor = new Color(90, 200, 220, 80);

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

        setPreferredSize(new Dimension(350, 250));
        setLocationRelativeTo(null);

        //Game Mode
        JLabel gameMode = new JLabel("RS Game Mode");
        JRadioButton bOSR = new JRadioButton("OSR");
        JRadioButton bRS3 = new JRadioButton("RS3");
        ButtonGroup bgGameMode = new ButtonGroup();
        bOSR.addActionListener(this);
        bRS3.addActionListener(this);
        bOSR.setSelected(true);
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

        //Fill color
        JLabel fillColor = new JLabel("Area Fill Color");
        bFillColor.addActionListener(this);

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
        add(fillColor, c);
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 4;
        add(bFillColor, c);

        c.gridx = 0;
        c.gridy = 5;
        add(safePolygon, c);
        c.gridx = 2;
        c.gridy = 5;
        add(cSafePolygon, c);

        pack();
        loadSettings();
    }

    public static void main(String args[]) {
        new SettingsFrame().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "OSR":
                if (!getGameMode())
                    setGameMode(true);
                break;
            case "RS3":
                if (getGameMode())
                    setGameMode(false);
                break;
            case "Choose Color":
                if (e.getSource() == bLineColor) {
                    setLineColor(pickColor());
                } else if (e.getSource() == bFillColor) {
                    setFillColor(pickColor());
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
        JSlider s = (JSlider) e.getSource();
        setMaxLineLength(s.getValue());
        slideText.setText(String.valueOf(getMaxLineLength()));
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        setSafePolygon(e.getStateChange() == ItemEvent.DESELECTED);
    }

    // Setters and Getters
    public int getMaxLineLength() {
        return maxLineLength;
    }

    private void setMaxLineLength(int value) {
        maxLineLength = value;
        properties.setProperty("maxLineLength", "" + value);
    }

    public boolean getSafePolygon() {
        return safePolygon;
    }

    private void setSafePolygon(Boolean b) {
        safePolygon = b;
        properties.setProperty("safePolygon", "" + b);
    }

    public boolean getGameMode() {
        return gameMode;
    }

    private void setGameMode(Boolean OSR) {
        gameMode = OSR;
        properties.setProperty("gameMode", "" + OSR);
    }

    public Color getLineColor() {
        return lineColor;
    }

    private void setLineColor(Color col) {
        lineColor = col;
        properties.setProperty("lineColor", "" + col.getRGB());
    }

    public Color getWarnLineColor() {
        return warnLineColor;
    }

    private void setWarnLineColor(Color col) {
        warnLineColor = col;
        properties.setProperty("warnLineColor", "" + col.getRGB());
    }

    public Color getFillColor() {
        return fillColor;
    }

    private void setFillColor(Color col) {
        fillColor = col;
        properties.setProperty("fillColor", "" + col.getRGB());
    }

    public File getLastLoadedFile() {
        return lastLoadedFile;
    }

    public void setLastLoadedFile(File newFile) {
        if (newFile == null)
            return;
        lastLoadedFile = newFile;
        properties.setProperty("lastLoadedFile", lastLoadedFile.getAbsolutePath());
    }

    private Color pickColor() {
        return JColorChooser.showDialog(this, "Choose a new color", Color.RED);
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
                FileInputStream in = new FileInputStream(propertiesFile);
                properties.load(in);

                if (properties.containsKey("lastLoadedFile"))
                    setLastLoadedFile(new File(properties.getProperty("lastLoadedFile")));
                else
                    setLastLoadedFile(getLastLoadedFile());

                if (properties.containsKey("gameMode"))
                    setGameMode(Boolean.getBoolean(properties.getProperty("gameMode")));
                else
                    setGameMode(getGameMode());

                if (properties.containsKey("maxLineLength"))
                    setMaxLineLength(Integer.valueOf(properties.getProperty("maxLineLength")));
                else
                    setMaxLineLength(getMaxLineLength());

                if (properties.containsKey("lineColor"))
                    setLineColor(new Color(Integer.valueOf(properties.getProperty("lineColor")), true));
                else
                    setLineColor(getLineColor());

                if (properties.containsKey("warnLineColor"))
                    setWarnLineColor(new Color(Integer.valueOf(properties.getProperty("warnLineColor")), true));
                else
                    setWarnLineColor(getWarnLineColor());

                if (properties.containsKey("fillColor"))
                    setFillColor(new Color(Integer.valueOf(properties.getProperty("fillColor")), true));
                else
                    setFillColor(getFillColor());

                if (properties.containsKey("safePolygon"))
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

}
