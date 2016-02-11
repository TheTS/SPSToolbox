package toolbox;

import javax.swing.*;
import java.awt.*;

class CodeFrame extends JFrame {

    private final JEditorPane editor = new JEditorPane();

    public CodeFrame() {
        setTitle("SPS Code Snippet");
        setMinimumSize(new Dimension(800, 400));

        editor.setContentType("text/plain");
        editor.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        editor.setFont(new Font("Courier New", 0, 12));
        editor.setEditable(true);

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

    public void setPath(String path, Boolean isPathActive, String mapName, String mapPath) {
        path = splitPath(path);
        String folder;
        if (mapPath.toLowerCase().contains("runescape_surface")) {
            folder = "RUNESCAPE_SURFACE";
        } else if (mapPath.toLowerCase().contains("runescape_other")) {
            folder = "RUNESCAPE_OTHER";
        } else {
            folder = "{Put your map in the SPS directory}";
        }

        if (isPathActive) {
            editor.setText(
                    "program new\n" +
                            "{$DEFINE SMART}\n" +
                            "{$i srl-6/srl.simba}\n" +
                            "{$i sps/lib/sps-rs3.simba}\n\n" +
                            "procedure walk();\n" +
                            "var\n" +
                            "  path: TPointArray;\n" +
                            "begin\n" +
                            "  path := " + path + ";\n\n" +
                            "  if not sps.walkPath(path) then\n" +
                            "    sps.blindWalk(path[high(path)]);\n" +
                            "end;\n\n" +
                            "begin\n" +
                            "  setupSRL();\n" +
                            "  sps.setup(" + mapName + ", " + folder + ");\n\n" +
                            "  walk();\n" +
                            "end.");

        } else {
            editor.setText(
                    "program new\n" +
                            "{$DEFINE SMART}\n" +
                            "{$i srl-6/srl.simba}\n" +
                            "{$i sps/lib/sps-rs3.simba}\n\n" +
                            "var\n" +
                            "  myArea: TPointArray;\n\n" +
                            "begin\n" +
                            "  setupSRL();\n" +
                            "  sps.setup(" + mapName + ", " + folder + ");\n\n" +
                            "  myArea := " + path + ";\n\n" +
                            "  repeat;\n" +
                            "    writeLn(sps.isInPolygon(myArea));\n" +
                            "    wait(100);\n" +
                            "  until false;\n" +
                            "end.");
        }
    }
}
