package sps.path.generator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author int
 */
public class SPSPathGenerator extends JFrame implements ActionListener,ComponentListener,WindowListener{

    private static File imageFile = new File("c:/simba/includes/sps/img/runescape_surface/air_altar_path.png");
    private RSMapView mapView;
    private SidePane sidePane;
    protected SettingsFrame settingsFrame = new SettingsFrame();
    protected JFileChooser dialog = new JFileChooser(settingsFrame.getLastLoadedFile());
    private TipsFrame tipsFrame = new TipsFrame();
    private JPanel statusBar = new JPanel();
    private JLabel MapCoordLabel = new JLabel("Shift (0,0)");
    private JLabel mouseLabel = new JLabel("Mouse (0,0)");

    // main method
    public static void main(String[] args) throws MalformedURLException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed to set Look and Feel: "
                            + ex.getLocalizedMessage() , "Look and Feel Error",
                    JOptionPane.INFORMATION_MESSAGE);
            Logger.getLogger(SPSPathGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        new SPSPathGenerator("SPS Path Generator");

    }

    //SPSPathGenerator Constructor
    public SPSPathGenerator(String title) throws HeadlessException {
        super(title);
        this.setLayout(new GridBagLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        dialog.setFileFilter(new FileNameExtensionFilter("Image Files", "png","bmp","jpg","jpeg","gif"));
       
        mapView = new RSMapView() {

            @Override
            public void clickedPoint(Point p, int button) {
                if (button == MouseEvent.BUTTON1){
                    addTablePoint(p);
                }
            }

            @Override
            public Point[] getPoints() {
                return getTablePoints();
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
            public int getMaxLineLength() {
                return settingsFrame.getMaxLineLength();
            }

            @Override
            public int getFogOpacity() {
                return settingsFrame.getFogOpacity();
            }

            @Override
            public Color getLineCol() {
                return settingsFrame.getLineCol();
            }

            @Override
            public Color getWarnLineCol() {
                return settingsFrame.getWarnLineCol();
            }

            @Override
            public Color getPointCol() {
                return settingsFrame.getPointCol();
            }

            @Override
            public Color getSelPointCol() {
                return settingsFrame.getSelPointCol();
            }

            @Override
            public int[] getSelectedPointIndexs() {
                return sidePane.getPointTable().getSelectedRows();
            }

            @Override
            public void setMapCoordLabelVal(int x, int y){
                MapCoordLabel.setText("Current Map Coordinates (" + x + "," + y + ")");
            }

            @Override
            public void setMouseLabelVal(int x, int y){
                mouseLabel.setText("Mouse Coordinates (" + x + "," + y + ")");
            }
        };
        mapView.loadMapImage(imageFile.getAbsolutePath());

        //Path menu
        JMenu menuPath = new JMenu("Path");
        menuPath.add("Save Path").addActionListener(this);
        menuPath.add("Load Path").addActionListener(this);
        menuPath.addSeparator();
        menuPath.add("Invert Path").addActionListener(this);
        menuPath.add("Clear Path").addActionListener(this);

        //Map menu
        JMenu menuImage = new JMenu("Map");
        menuImage.add("Reload map").addActionListener(this);
        menuImage.add("Load different map").addActionListener(this);
        menuImage.add("Repaint map").addActionListener(this);

        //Tools menu
        JMenu menuTools = new JMenu("Tools");
        menuTools.add("Settings").addActionListener(this);

        //Help Menu
        JMenu menuHelp = new JMenu("Help");
        menuHelp.add("How to use this").addActionListener(this);

        //Buttons
        JButton pathBtn = new JButton("Copy Path Array");
        pathBtn.addActionListener(this);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuImage);
        menuBar.add(menuPath);
        menuBar.add(menuTools);
        menuBar.add(menuHelp);
        menuBar.add(pathBtn);

        //Status bar is at the bottom on the JFrame
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        statusBar.setMaximumSize(new Dimension(20000,25));
        statusBar.setPreferredSize(new Dimension(600,25));
        statusBar.add(MapCoordLabel);
        statusBar.add(mouseLabel);
        
        sidePane = new SidePane(mapView);
        this.add(mapView,new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        this.add(sidePane,new GridBagConstraints(1, 0, 1, 1, 0, 1, GridBagConstraints.NORTHEAST, GridBagConstraints.VERTICAL, new Insets(0,0,0,0), 0, 0));
        this.add(statusBar, new GridBagConstraints(0, 1, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
        this.setJMenuBar(menuBar);
        this.addComponentListener(this);
        this.addWindowListener(this);

        this.setSize(600, 400);
        this.setVisible(true);
    }

    private void addTablePoint(Point p) {

        sidePane.addTablePoint(p);
    }
    
    private Point[] getTablePoints(){

        return sidePane.getPoints();
    }

    // Action Listener
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()){
            case "Reload map":
                mapView.reloadMapImage();
                break;
            case "Load different map":
                dialog.setMultiSelectionEnabled(false);
                dialog.showDialog(this, "Choose");
                if (dialog.getSelectedFile() == null)
                    return;
                settingsFrame.setLastLoadedFile(dialog.getCurrentDirectory());
                mapView.loadMapImage(dialog.getSelectedFile().getAbsolutePath());
                break;
            case "Repaint":
                mapView.repaint();
                break;
            case "Copy Path Array":
                CopyPathToClip();
                break;
            case "Settings":
                settingsFrame.setVisible(true);
                break;
            case "Invert Path":
                sidePane.getPointTable().setPointArr(flipArray(sidePane.getPointTable().getPointArr()));
                break;
            case "Save Path":
                sidePane.getPointTable().savePath();
                break;
            case "Load Path":
                sidePane.getPointTable().openPath();
                mapView.repaint();
                break;
            case "Clear Path":
                if (JOptionPane.showConfirmDialog(this , "This will remove all the points from your path. Are you sure?") == JOptionPane.YES_OPTION)
                    sidePane.getPointTable().setPointArr(new Point[0]);
                break;
            case "How to use this":
                tipsFrame.setVisible(true);
                break;
        }

    }
    
    public void CopyPathToClip(){
        StringSelection pathDec = new StringSelection(getPathDec());
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(pathDec, pathDec);
            JOptionPane.showMessageDialog(this, "Successfully copied path to clipboard","Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Failed to set clipboard contents.", "Error accessing clipboard", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getPathDec() {
        StringBuilder sb = new StringBuilder("[");

        Point[] points = getTablePoints();
        for (int i = 0 ; i < points.length ; i++){
            sb.append("Point(").append(points[i].x).append(", ").append(points[i].y).append(")");
            if (i < points.length - 1 && points.length > 0){
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }

    private <T> T[] flipArray(T[] theArr){
        java.util.List<T> reversedList = new ArrayList<>();
        for (T t : theArr){
            reversedList.add(0, t);
        }
        return reversedList.toArray(theArr);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        mapView.setMiniMapView();
        mapView.getMinimap().repaint();
    }

    @Override
    public void windowClosing(WindowEvent e) {

        settingsFrame.onExit();
    }

    //Overrides that are unused
    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
}
