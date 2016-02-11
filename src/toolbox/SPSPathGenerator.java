package toolbox;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

class SPSPathGenerator extends JFrame implements ActionListener {

    private static boolean IsPathActive = true;
    private static File imageFile;
    private final RSMapView mapView;
    private final TipsFrame tipsFrame = new TipsFrame();
    private final CodeFrame codeFrame = new CodeFrame();
    private final SettingsFrame settingsFrame = new SettingsFrame();
    private final JFileChooser dialog = new JFileChooser(settingsFrame.getLastLoadedFile());
    private final JPanel statusBar = new JPanel();
    private final JLabel MapCoordLabel = new JLabel();
    private final JLabel mouseLabel = new JLabel();
    private SidePane sidePane;

    private SPSPathGenerator() throws HeadlessException {
        super("SPS Toolbox");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        if (settingsFrame.getLastLoadedFile() != null) {
            imageFile = settingsFrame.getLastLoadedFile();
        } else {
            imageFile = new File("c:/simba/includes/sps/img/runescape_surface/air_altar_path.png");
        }

        mapView = new RSMapView() { //Override RSMapView to call methods through SidePane instance

            @Override
            public void clickedPoint(Point p, int button) {
                if (button == MouseEvent.BUTTON1) {
                    sidePane.addTablePoint(p);
                }
            }

            @Override
            public void deleteSelectedPoint() {
                sidePane.getPointTable().deleteSelectedPoints();
            }

            @Override
            public Point[] getPoints() {
                return sidePane.getPoints();
            }

            @Override
            public boolean isPathActive() {
                return getIsPathActive();
            }

            @Override
            public boolean isRepositioning() {
                return sidePane.getPointTable().isRepositioning();
            }

            @Override
            public int getRepositionedPointIndex() {
                return sidePane.getPointTable().getRepositionIndex();
            }

            @Override
            public int getSelectedPointIndex() {
                return sidePane.getPointTable().getSelectedRow();
            }

            @Override
            public int[] getSelectedPointIndexs() {
                return sidePane.getPointTable().getSelectedRows();
            }

            @Override
            public int getMaxLineLength() {
                return settingsFrame.getMaxLineLength();
            }

            @Override
            public Color getLineColor() {
                return settingsFrame.getLineColor();
            }

            @Override
            public Color getWarnLineColor() {
                return settingsFrame.getWarnLineColor();
            }

            @Override
            public Color getFillColor() {
                return settingsFrame.getFillColor();
            }

            @Override
            public boolean getGameMode() {
                return settingsFrame.getGameMode();
            }

            @Override
            public boolean getSafePolygon() {
                return settingsFrame.getSafePolygon();
            }

            @Override
            public void setMapCoordLabelVal(int x, int y) {
                MapCoordLabel.setText("Current Map Coordinates (" + x + "," + y + ")");
            }

            @Override
            public void setMouseLabelVal(Point p) {
                mouseLabel.setText("Mouse Coordinates (" + p.x + "," + p.y + ")");
            }
        };

        mapView.loadMapImage(imageFile.getAbsolutePath());
        dialog.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "bmp", "jpg", "jpeg", "gif"));

        //Map menu
        JMenu menuMap = new JMenu("Map");
        menuMap.add("Reload Map").addActionListener(this);
        menuMap.add("Load New Map").addActionListener(this);
        menuMap.addSeparator();
        menuMap.add("Repaint Map").addActionListener(this);

        //Points menu
        JMenu menuPath = new JMenu("Points");
        menuPath.add("Save Points").addActionListener(this);
        menuPath.add("Load Points").addActionListener(this);
        menuPath.addSeparator();
        menuPath.add("Invert Points").addActionListener(this);
        menuPath.add("Clear Points").addActionListener(this);

        //Tools menu
        JMenu menuTools = new JMenu("Tools");
        menuTools.add("Settings").addActionListener(this);

        //Help Menu
        JMenu menuHelp = new JMenu("Help");
        menuHelp.add("How to use this").addActionListener(this);

        //Buttons
        JButton codeBtn = new JButton("Make Code Snippet");
        JButton copyBtn = new JButton("Copy To Clipboard");
        JRadioButton pathBtn = new JRadioButton("Path");
        JRadioButton areaBtn = new JRadioButton("Area");
        codeBtn.addActionListener(this);
        copyBtn.addActionListener(this);
        pathBtn.addActionListener(this);
        areaBtn.addActionListener(this);

        ButtonGroup radioBtns = new ButtonGroup();
        radioBtns.add(pathBtn);
        radioBtns.add(areaBtn);
        pathBtn.setSelected(true);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuMap);
        menuBar.add(menuPath);
        menuBar.add(menuTools);
        menuBar.add(menuHelp);
        menuBar.add(codeBtn);
        menuBar.add(copyBtn);
        menuBar.add(new JSeparator(JSeparator.VERTICAL));
        menuBar.add(pathBtn);
        menuBar.add(areaBtn);

        //Status bar is at the bottom on the JFrame
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        statusBar.setMaximumSize(new Dimension(20000, 25));
        statusBar.setPreferredSize(new Dimension(600, 25));
        statusBar.add(MapCoordLabel);
        JLabel placeholder = new JLabel(" | ");
        statusBar.add(placeholder);
        statusBar.add(mouseLabel);

        sidePane = new SidePane(mapView);

        setLayout(new GridBagLayout());
        setMinimumSize(new Dimension(550, 400));
        add(mapView, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        add(sidePane, new GridBagConstraints(1, 0, 1, 1, 0, 1, GridBagConstraints.NORTHEAST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
        add(statusBar, new GridBagConstraints(0, 1, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        setJMenuBar(menuBar);

        getContentPane().setBackground(Color.white);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new SPSPathGenerator();
    }

    private static <T> T[] reverse(T[] array) {
        Collections.reverse(Arrays.asList(array));
        return array;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "Reload Map":
                mapView.reloadMapImage();
                break;
            case "Load New Map":
                dialog.setMultiSelectionEnabled(false);
                dialog.showDialog(this, "Choose");
                if (dialog.getSelectedFile() == null)
                    return;
                imageFile = dialog.getSelectedFile();
                settingsFrame.setLastLoadedFile(dialog.getSelectedFile().getAbsoluteFile());
                settingsFrame.onClose();
                mapView.loadMapImage(dialog.getSelectedFile().getAbsolutePath());
                break;
            case "Repaint Map":
                mapView.repaint();
                break;
            case "Save Points":
                sidePane.getPointTable().savePath();
                break;
            case "Load Points":
                sidePane.getPointTable().openPath();
                mapView.repaint();
                break;
            case "Invert Points":
                sidePane.getPointTable().setPointArr(reverse(sidePane.getPointTable().getPointArr()));
                break;
            case "Clear Points":
                if (JOptionPane.showConfirmDialog(this, "This will remove all the points from your " + (getIsPathActive() ? "path" : "area"), "Are you sure?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    sidePane.getPointTable().setPointArr(new Point[0]);
                break;
            case "Settings":
                settingsFrame.setVisible(true);
                break;
            case "How to use this":
                tipsFrame.setVisible(true);
                break;
            case "Make Code Snippet":
                codeFrame.setPath(getPathDec(), getIsPathActive(), imageFile.getName(), imageFile.getAbsolutePath());
                codeFrame.setVisible(true);
                break;
            case "Copy To Clipboard":
                CopyPathToClip();
                break;
            case "Path":
                if (!getIsPathActive() && sidePane.getPoints().length > 0)
                    if (JOptionPane.showConfirmDialog(this, "This will remove all the points from your area", "Are you sure?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                        sidePane.getPointTable().setPointArr(new Point[0]);
                setIsPathActive(true);
                break;
            case "Area":
                if (getIsPathActive() && sidePane.getPoints().length > 0)
                    if (JOptionPane.showConfirmDialog(this, "This will remove all the points from your path", "Are you sure?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                        sidePane.getPointTable().setPointArr(new Point[0]);
                setIsPathActive(false);
                break;
        }
    }

    private boolean getIsPathActive() {
        return IsPathActive;
    }

    private void setIsPathActive(boolean active) {
        IsPathActive = active;
    }

    private void CopyPathToClip() {
        StringSelection pathDec = new StringSelection(getPathDec());
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(pathDec, pathDec);
            JOptionPane.showMessageDialog(this, "Successfully copied " + (getIsPathActive() ? "path" : "area") + " to clipboard", "Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to set clipboard contents.", "Error accessing clipboard", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getPathDec() {
        StringBuilder sb = new StringBuilder("[");

        Point[] points = sidePane.getPoints();
        for (int i = 0; i < points.length; i++) {
            sb.append("[").append(points[i].x).append(", ").append(points[i].y).append("]");
            if (i < points.length - 1 && points.length > 0)
                sb.append(", ");

        }
        return sb.append("]").toString();
    }
}
