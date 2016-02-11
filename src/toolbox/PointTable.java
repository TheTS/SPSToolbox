package toolbox;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PointTable extends JTable implements ActionListener, MouseListener, ListSelectionListener {

    private static final DefaultTableModel tm = new DefaultTableModel(new String[]{"#", "X", "Y"}, 0) {
    };
    private final JPopupMenu rightClickMenu = new JPopupMenu();
    private final JFileChooser fileDialog = new JFileChooser();
    private boolean repositioning;
    private int repositionIndex;

    public PointTable() {
        super(tm);
        addMouseListener(this);
        rightClickMenu.add("Change point index").addActionListener(this);
        rightClickMenu.add("Delete point").addActionListener(this);
        rightClickMenu.add("Focus view here").addActionListener(this);
        rightClickMenu.add("Reposition point").addActionListener(this);
    }

    public Point[] getPointArr() {
        Point[] points = new Point[this.getRowCount()];

        for (int i = 0; i < points.length; i++)
            points[i] = new Point(Integer.parseInt(String.valueOf(tm.getValueAt(i, 1))), Integer.parseInt(String.valueOf(tm.getValueAt(i, 2))));

        return points;
    }

    public void setPointArr(Point[] points) {
        tm.setNumRows(points.length);
        if (points.length == 0)
            return;

        for (int i = 0; i < points.length; i++) {
            tm.setValueAt(String.valueOf(i), i, 0);
            tm.setValueAt(String.valueOf(points[i].x), i, 1);
            tm.setValueAt(String.valueOf(points[i].y), i, 2);
        }
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

    private void moveRow(int from, int to) {
        Object[] tempRow = {this.getValueAt(from, 0), this.getValueAt(from, 1), this.getValueAt(from, 2)};
        tm.removeRow(from);
        tm.insertRow(to, tempRow);
        renumberTable();
    }

    private void renumberTable() {
        for (int i = 0; i < tm.getRowCount(); i++)
            tm.setValueAt(i, i, 0);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            int index = this.rowAtPoint(e.getPoint());
            this.setRowSelectionInterval(index, index);
            rightClickMenu.setInvoker(this);
            rightClickMenu.setLocation(e.getXOnScreen(), e.getYOnScreen());
            rightClickMenu.setVisible(true);
            rightClickMenu.requestFocusInWindow();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "Change point index":
                String i = JOptionPane.showInputDialog(this, "Enter a new indice for this point.", "Change point indice", JOptionPane.QUESTION_MESSAGE);
                try {
                    moveRow(this.getSelectedRow(), Integer.parseInt(i));
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
                break;
            case "Delete point":
                deleteSelectedPoints();
                break;
            case "Focus view here":
                int index = getSelectedRow();
                Point[] points = getPointArr();
                getMapView().moveViewToMapCoords(points[index].x, points[index].y);
                break;
            case "Reposition point":
                if (this.getSelectedRow() >= 0) {
                    setRepositioning(true);
                    setRepositionIndex(this.getSelectedRow());
                }
        }

        rightClickMenu.setVisible(false);
        getMapView().repaint();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        super.valueChanged(e);
        getMapView().repaint();
    }

    public abstract RSMapView getMapView();

    public boolean isRepositioning() {
        return repositioning;
    }

    private void setRepositioning(boolean repositioning) {
        this.repositioning = repositioning;
    }

    public int getRepositionIndex() {
        return repositionIndex;
    }

    private void setRepositionIndex(int repositionIndex) {
        this.repositionIndex = repositionIndex;
    }

    public void savePath() {
        fileDialog.showSaveDialog(this);
        File saveFile = fileDialog.getSelectedFile();
        if (!Pattern.compile(".SPSPath$").matcher(saveFile.getName()).find())
            saveFile = new File(saveFile.getAbsolutePath() + ".SPSPath");

        try {
            FileWriter fw = new FileWriter(saveFile);
            for (Point p : getPointArr()) {
                fw.append("").append(String.valueOf(p.x)).append(":").append(String.valueOf(p.y)).append("\r\n");
            }
            fw.flush();
        } catch (IOException ex) {
            Logger.getLogger(PointTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void openPath() {
        String pointPattern = ".*?(\\d*):(\\d*)\r\n.*?";
        fileDialog.showOpenDialog(this);
        List<Point> points = new ArrayList<>();
        try {
            System.out.println(fileDialog.getSelectedFile().exists());
            FileReader fr = new FileReader(fileDialog.getSelectedFile());
            String[] s;
            Point p;
            while (true) {
                s = readUntilMatch(fr, Pattern.compile(pointPattern));
                if (s.length > 1) {
                    p = new Point(Integer.valueOf(s[1]), Integer.valueOf(s[2]));
                    points.add(p);
                } else {
                    break;
                }
            }
            setPointArr(points.toArray(new Point[points.size()]));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PointTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        getMapView().repaint();
    }

    private String[] readUntilMatch(FileReader fr, Pattern p) {
        StringBuilder sb = new StringBuilder();
        Matcher m = null;
        int b;
        try {
            while (!(m = p.matcher(sb.toString())).matches()) {
                b = fr.read();
                if (b == -1)
                    return new String[0];

                sb.append((char) b);
            }
        } catch (IOException ex) {
            Logger.getLogger(PointTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new String[]{sb.toString(), m.group(1), m.group(2)};
    }

    public void deleteSelectedPoints() {
        int firstSelection = getSelectedRow();
        if (getSelectedRow() >= 0) {
            for (int i = getSelectedRows().length - 1; i >= 0; i--)
                tm.removeRow(getSelectedRows()[i]);


            if (tm.getRowCount() > 0 && firstSelection == tm.getRowCount()) {
                this.setRowSelectionInterval(tm.getRowCount() - 1, tm.getRowCount() - 1);
            } else if (tm.getRowCount() > 0 && firstSelection >= 0) {
                this.setRowSelectionInterval(Math.max(0, Math.min(firstSelection, tm.getRowCount() - 1)), Math.max(0, Math.min(firstSelection, tm.getRowCount() - 1)));
            }
        }
        renumberTable();
    }

    // Unused overrides
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
}
