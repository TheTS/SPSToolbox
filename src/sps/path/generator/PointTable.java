/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.path.generator;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author int
 */
public abstract class PointTable extends JTable implements FocusListener, ActionListener, MouseListener, ListSelectionListener, KeyListener {

    private JPopupMenu rightClickMenu = new JPopupMenu();
    private JFileChooser fileDialog = new JFileChooser();
    private FileFilter fileFilter = new FileFilter() {

        @Override
        public boolean accept(File f) {
            System.out.println();
            System.out.println(f.getName().toString());
            Matcher m = Pattern.compile(".SPSPath$").matcher(f.getName());
            return m.find();
        }

        @Override
        public String getDescription() {
            return "Path Files (*.SPSPath)";
        }
    };
    private static DefaultTableModel tm =
                                     new DefaultTableModel(new String[]{"#", "X", "Y"}, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            //return column != 0;
            //TODO restrict editing to numbers only
            return false;
        }
    };
    private boolean repositioning;
    private int repositionIndex;
    private String pointPattern = ".*?(\\d*):(\\d*)\r\n.*?";

    public PointTable() {
        super(tm);
        this.addMouseListener(this);
        this.addKeyListener(this);

        rightClickMenu.addFocusListener(this);
        JMenuItem item;
        item = new JMenuItem("Move point in list");
        item.addActionListener(this);
        rightClickMenu.add(item);
        item = new JMenuItem("Reposition point");
        item.addActionListener(this);
        rightClickMenu.add(item);
        item = new JMenuItem("Delete point(s)");
        item.addActionListener(this);
        rightClickMenu.add(item);
        rightClickMenu.add("Move view here").addActionListener(this);

        fileDialog.setFileFilter(fileFilter);
        //fileDialog.setAcceptAllFileFilterUsed(false);
    }

    public void setPointArr(Point[] points) {
        int sel = getSelectedRow();
        tm.setNumRows(points.length);
        if (points.length == 0) {
            return;
        }
        for (int i = 0; i < points.length; i++) {
            tm.setValueAt(String.valueOf(i), i, 0);
            tm.setValueAt(String.valueOf(points[i].x), i, 1);
            tm.setValueAt(String.valueOf(points[i].y), i, 2);
        }
        setRowSelectionInterval(points.length - sel - 1, points.length - sel - 1);
    }

    public Point[] getPointArr() {
        Point[] points = new Point[this.getRowCount()];

        for (int i = 0; i < points.length; i++) {
            points[i] =
            new Point(Integer.parseInt(String.valueOf(tm.getValueAt(i, 1))),
                      Integer.parseInt(String.valueOf(tm.getValueAt(i, 2))));
        }
        return points;
    }

    public void addPoint(Point p) {
        if (!isRepositioning()) {
            tm.addRow(new Integer[]{tm.getRowCount(), p.x, p.y});
            this.setRowSelectionInterval(Math.max(0, tm.getRowCount() - 1), Math.max(0, tm.getRowCount() - 1));
        } else {
            tm.removeRow(getRepositionIndex());
            tm.insertRow(getRepositionIndex(), new Integer[]{getRepositionIndex(), p.x, p.y});
            setRowSelectionInterval(getRepositionIndex(), getRepositionIndex());
            setRepositioning(false);
        }
        this.revalidate();
    }

    public void moveRow(int from, int to) {
        Object[] tempRow = {this.getValueAt(from, 0),
                            this.getValueAt(from, 1),
                            this.getValueAt(from, 2)};
        tm.removeRow(from);
        tm.insertRow(to, tempRow);
        renumberTable();
    }

    public void renumberTable() {
        for (int i = 0; i < tm.getRowCount(); i++) {
            tm.setValueAt(i, i, 0);
        }
    }

    @Override
    public Dimension getMaximumSize() {
        return super.getMaximumSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(60, super.getPreferredSize().height);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(60, super.getPreferredSize().height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point[] points = getPointArr();
        int index = this.rowAtPoint(e.getPoint());
        if (e.getButton() == MouseEvent.BUTTON3) {
            boolean selectedUnderMouse = false;
            for (int i = this.getSelectedRows().length - 1; i >= 0; i--) {
                if (getSelectedRows()[i] == index) {
                    selectedUnderMouse = true;
                }
            }

            if (!selectedUnderMouse) {
                this.setRowSelectionInterval(index, index);
            }
            rightClickMenu.setInvoker(this);
            rightClickMenu.setLocation(e.getXOnScreen(), e.getYOnScreen());
            rightClickMenu.setVisible(true);
            rightClickMenu.requestFocusInWindow();
        } else if (e.isControlDown()) {
//            if (index >= 0)
//                getMapView().moveViewToMapCoords(points[index].x, 
//                                                 points[index].y);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Delete point(s)")) {
            deleteSelectedPoints();
        } else if (e.getActionCommand().equals("Reposition point")) {
            if (this.getSelectedRow() >= 0) {
                setRepositioning(true);
                setRepositionIndex(this.getSelectedRow());
            }
        } else if (e.getActionCommand().equals("Move point in list")) {
            String result =
                   JOptionPane.showInputDialog(this, "Enter a new indice for this point.", "Change point indice", JOptionPane.QUESTION_MESSAGE);
            try {
                int i = Integer.parseInt(result);
                moveRow(this.getSelectedRow(), i);
            } catch (NumberFormatException ex) {
            }
        } else if (e.getActionCommand().equals("Move view here")) {
            int sel = getSelectedRow();
            Point[] points = getPointArr();
            getMapView().moveViewToMapCoords(points[sel].x, points[sel].y);
        }
        //rightClickMenu.setVisible(false);
        getMapView().repaint();
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
        rightClickMenu.setVisible(false);
    }

    /**
     * @return the repositioning
     */
    public boolean isRepositioning() {
        return repositioning;
    }

    /**
     * @param repositioning the repositioning to set
     */
    public void setRepositioning(boolean repositioning) {
        this.repositioning = repositioning;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        super.valueChanged(e);
        getMapView().repaint();
    }

    public abstract RSMapView getMapView();

    /**
     * @return the repositionIndex
     */
    public int getRepositionIndex() {
        return repositionIndex;
    }

    /**
     * @param repositionIndex the repositionIndex to set
     */
    public void setRepositionIndex(int repositionIndex) {
        this.repositionIndex = repositionIndex;
    }

    public void savePath() {
        fileDialog.showSaveDialog(this);
        File saveFile = fileDialog.getSelectedFile();
        if (!Pattern.compile(".SPSPath$").matcher(saveFile.getName()).find()) {
            saveFile = new File(saveFile.getAbsolutePath() + ".SPSPath");
        }
        if (true) {
            try {
                FileWriter fw = new FileWriter(saveFile);
                for (Point p : getPointArr()) {
                    fw.append("" + p.x + ":" + p.y + "\r\n");
                }
                fw.flush();
            } catch (IOException ex) {
                Logger.getLogger(PointTable.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void openPath() {
        fileDialog.showOpenDialog(this);
        List<Point> points = new ArrayList<Point>();
        try {
            System.out.println(fileDialog.getSelectedFile().exists());
            FileReader fr = new FileReader(fileDialog.getSelectedFile());
            String[] s = new String[0];
            Point p;
            Matcher m = null;
            while (true) {
                s = readUntilMatch(fr, Pattern.compile(pointPattern), 35);
                if (s.length > 1) {
                    p = new Point(Integer.valueOf(s[1]), Integer.valueOf(s[2]));
                    points.add(p);
                } else {
                    break;
                }
            }
            setPointArr(points.toArray(new Point[0]));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PointTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        getMapView().repaint();
    }

    private String[] readUntilMatch(FileReader fr, Pattern p, int limit) {
        StringBuilder sb = new StringBuilder();
        Matcher m = null;
        int b = 0;
        try {
            while (!(m = p.matcher(sb.toString())).matches()) {
                b = fr.read();
                if (b == -1) {
                    return new String[0];
                }
                sb.append((char) b);
            }
        } catch (IOException ex) {
            Logger.getLogger(PointTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new String[]{sb.toString(),
                            m == null ? "" : m.group(1),
                            m == null ? "" : m.group(2)};
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            deleteSelectedPoints();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void deleteSelectedPoints() {
        int firstSelection = getSelectedRow();
        if (this.getSelectedRow() >= 0) {
            for (int i = this.getSelectedRows().length - 1; i >= 0; i--) {
                tm.removeRow(this.getSelectedRows()[i]);
            }
            if (tm.getRowCount() > 0 && firstSelection >= 0 && tm.getColumnCount() > firstSelection) {
                this.setRowSelectionInterval(Math.max(0, Math.min(firstSelection, tm.getRowCount() - 1)), Math.max(0, Math.min(firstSelection, tm.getRowCount() - 1)));
            }
        }
        renumberTable();
    }
}
