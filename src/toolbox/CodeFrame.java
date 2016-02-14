package toolbox;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class CodeFrame extends JFrame implements ActionListener, MouseListener {

    private final JTextPane editor = new JTextPane();
    private final JPopupMenu rightClickMenu = new JPopupMenu();

    public CodeFrame() {
        setTitle("SPS Code Snippet");
        setMinimumSize(new Dimension(800, 400));

        editor.setContentType("text/plain");
        editor.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        editor.setFont(new Font("Courier New", 0, 12));
        editor.setEditable(true);
        editor.addMouseListener(this);

        rightClickMenu.add("Cut").addActionListener(this);
        rightClickMenu.add("Copy").addActionListener(this);
        rightClickMenu.add("Paste").addActionListener(this);
        rightClickMenu.addSeparator();
        rightClickMenu.add("Select All").addActionListener(this);

        JScrollPane scrollPane = new JScrollPane(editor);
        add(scrollPane);
    }

    public static void main(String args[]) {
        new CodeFrame().setVisible(true);
    }

    private String splitPath(String path) {
        if (path.length() > 70) {
            StringBuilder sb = new StringBuilder();
            int j = 65;
            for (int i = 0; i < path.length() - 1; i++) {
                if ((j % 65 == 0) && i >= j)
                    if ((path.charAt(i) == '[') && (path.charAt(i - 2) == ',') && (path.length() - i > 15)) {
                        sb.append("\n           ");
                        j += 65;
                    }
                sb.append(path.charAt(i));
            }
            sb.append(']');
            path = sb.toString();
        }
        return path;
    }

    private void append(String msg, Color c, boolean bold)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        if (bold)
            aset = sc.addAttribute(aset, StyleConstants.Bold, Boolean.TRUE);
        else
            aset = sc.addAttribute(aset, StyleConstants.Bold, Boolean.FALSE);

        int len = editor.getDocument().getLength();
        editor.setCaretPosition(len);
        editor.setCharacterAttributes(aset, false);
        editor.replaceSelection(msg);
    }

    public void setPath(String path, Boolean isPathActive, String mapName, String mapPath) {
        path = splitPath(path);
        if (mapName.contains("."))
            mapName = mapName.substring(0, mapName.lastIndexOf('.')); // get rid of extension
        String folder;
        if (mapPath.toLowerCase().contains("runescape_surface")) {
            folder = "RUNESCAPE_SURFACE";
        } else if (mapPath.toLowerCase().contains("runescape_other")) {
            folder = "RUNESCAPE_OTHER";
        } else {
            folder = "Put your map in the SPS directory";
        }

        if (isPathActive) {
            append("program ", Color.BLACK, true);
            append("new;\n", Color.BLACK, false);
            append("{$DEFINE SMART}\n", Color.RED, false);
            append("{$i srl-6/srl.simba}\n", Color.RED, false);
            append("{$i sps/lib/sps-rs3.simba}\n\n", Color.RED, false);
            append("procedure ", Color.BLACK, true);
            append("walk();\n", Color.BLACK, false);
            append("var\n", Color.BLACK, false);
            append("  path: TPointArray;\n", Color.BLACK, false);
            append("begin\n", Color.BLACK, true);
            append("  path := " + path + ";\n\n", Color.BLACK, false);
            append("  if not ", Color.BLACK, true);
            append("sps.walkPath(path) ", Color.BLACK, false);
            append("then\n", Color.BLACK, true);
            append("  begin\n", Color.BLACK, true);
            append("    writeLn(", Color.BLACK, false);
            append("'walkPath() failed, trying blindWalk()'", Color.BLUE, false);
            append(");\n", Color.BLACK, false);
            append("    sps.blindWalk(path[high(path)]);\n", Color.BLACK, false);
            append("  end;\n\n", Color.BLACK, true);
            append("end;\n\n", Color.BLACK, true);
            append("begin\n", Color.BLACK, true);
            append("  setupSRL();\n", Color.BLACK, false);
            append("  sps.setup(", Color.BLACK, false);
            append("'" + mapName + "'", Color.BLUE, false);
            append(", ", Color.BLACK, false);
            append("'" + folder + "'", Color.BLUE, false);
            append(");\n", Color.BLACK, false);
            append("  walk();\n", Color.BLACK, false);
            append("end.", Color.BLACK, true);

        } else {

            append("program ", Color.BLACK, true);
            append("new;\n", Color.BLACK, false);
            append("{$DEFINE SMART}\n", Color.RED, false);
            append("{$i srl-6/srl.simba}\n", Color.RED, false);
            append("{$i sps/lib/sps-rs3.simba}\n\n", Color.RED, false);
            append("procedure ", Color.BLACK, true);
            append("areaCheck();\n", Color.BLACK, false);
            append("var\n", Color.BLACK, false);
            append("  myArea: TPointArray;\n", Color.BLACK, false);
            append("begin\n", Color.BLACK, true);
            append("  myArea := " + path + ";\n\n", Color.BLACK, false);
            append("  repeat\n", Color.BLACK, true);
            append("    writeLn(", Color.BLACK, false);
            append("'Inside Polygon: '", Color.BLUE, false);
            append(", sps.isInPolygon(myArea));\n", Color.BLACK, false);
            append("    wait(1000);\n", Color.BLACK, false);
            append("  until ", Color.BLACK, true);
            append("false;\n\n", Color.BLACK, false);
            append("end;\n\n", Color.BLACK, true);
            append("begin\n", Color.BLACK, true);
            append("  setupSRL();\n", Color.BLACK, false);
            append("  sps.setup(", Color.BLACK, false);
            append("'" + mapName + "'", Color.BLUE, false);
            append(", ", Color.BLACK, false);
            append("'" + folder + "'", Color.BLUE, false);
            append(");\n", Color.BLACK, false);
            append("  areaCheck();\n", Color.BLACK, false);
            append("end.", Color.BLACK, true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()){

            case "Cut": {
                editor.cut();
                break;
            }
            case "Copy": {
                editor.copy();
                break;
            }
            case "Paste": {
                editor.paste();
                break;
            }
            case "Select All":
                editor.selectAll();
                editor.requestFocus();
                break;

        }
        rightClickMenu.setVisible(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if  (e.getButton() == MouseEvent.BUTTON3) {
            rightClickMenu.setInvoker(this);
            rightClickMenu.setLocation(e.getXOnScreen(), e.getYOnScreen());
            rightClickMenu.setVisible(true);
            rightClickMenu.requestFocusInWindow();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}