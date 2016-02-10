package sps.path.generator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TipsFrame extends JFrame implements ActionListener {

    public TipsFrame() {
        JButton jButton1 = new JButton();
        JEditorPane jEditorPane1 = new JEditorPane();
        JScrollPane jScrollPane1  = new JScrollPane();
        jButton1.setText("Close");
        jButton1.addActionListener(this);

        setAlwaysOnTop(true);
        setMaximumSize(new Dimension(215, 300));
        setMinimumSize(new Dimension(215, 300));

        jEditorPane1.setContentType("text/html");
        jEditorPane1.setEditable(false);
        jEditorPane1.setText("<html>\n\t<head>\n\t\t<title></title>\n\t</head>\n\t<body>\n\t\t<p>\n\t\t\t<u><strong>Navigation</strong></u></p>\n\t\t<p dir=\"ltr\">\n\t\t\tYou can move the view around by clicking or<br />\n\t\t\tdragging the mouse on the minimap, in the top<br />\n\t\t\tright corner, or you can drag the map view<br />\n\t\t\taround with the right mouse button.</p>\n\t\t<p>\n\t\t\t<u><strong>Adding points to your path</strong></u></p>\n\t\t<p>\n\t\t\tTo add a point, click in the view area. You can<br />\n\t\t\tadjust the location of any point in your path,<br />\n\t\t\tby right clicking it in the list, and clicking the<br />\n\t\t\tnew point on the view. You can also reorder<br />\n\t\t\tyour points by right clicking a point in the list,<br />\n\t\t\tand entering its new index. You can also clear<br />\n\t\t\tall points from your path by choosing<br />\n\t\t\t<em><strong>Path -&gt; Clear Path</strong></em></p>\n\t\t<p>\n\t\t\t<u><strong>Loading your map</strong></u></p>\n\t\t<p>\n\t\t\tTo load a map, choose<br />\n\t\t\t<strong><em>Image -&gt; Load different map</em></strong>. If changes are<br />\n\t\t\tmade to the map externally, you can update it by<br />\n\t\t\tchoosing <strong><em>Image -&gt; Reload map</em></strong></p>\n\t\t<p>\n\t\t\t<u><strong>Saving your paths</strong></u></p>\n\t\t<p>\n\t\t\tYou can save your path by choosing<br />\n\t\t\t<strong>Path -&gt; Save Path</strong>, and load them later, with the<br />\n\t\t\t<strong> Path -&gt; Load Path</strong> menu item.</p>\n\t\t<p>\n\t\t\t<u><strong>Settings</strong></u></p>\n\t\t<p style=\"margin-left: 40px;\">\n\t\t\t<strong>Max Line Length: </strong>This controls how far apart<br />\n\t\t\ttwo points can be, before they are considered<br />\n\t\t\ttoo far apart and displayed as dotted lines</p>\n\t\t<p style=\"margin-left: 40px;\">\n\t\t\t<strong>Fog Opacity: </strong>The density of the fog,<br />\n\t\t\tsurrounding the selected point(s)</p>\n\t\t<p style=\"margin-left: 40px;\">\n\t\t\t<strong>Line Color:</strong> The color of lines between<br />\n\t\t\tpoints that are not too distant,as determined<br />\n\t\t\tby the <em>Max Line Length</em> value</p>\n\t\t<p style=\"margin-left: 40px;\">\n\t\t\t<strong>Warning Line color: </strong>The color of lines<br />\n\t\t\tbetween points that are too distant, as<br />\n\t\t\tdetermined by the <em>Max Line Length</em> value</p>\n\t\t<p style=\"margin-left: 40px;\">\n\t\t\t<strong>Point color: </strong>The color of all points, excluding<br />\n\t\t\tthe currently selected point</p>\n\t\t<p style=\"margin-left: 40px;\">\n\t\t\t<strong>Selected Point Color: </strong>The color of the selected point</p>\n\t\t<p style=\"margin-left: 40px;\">\n\t\t\t<strong>Include surrounding areas in generation: </strong><br />\n\t\t\tThis will force the generator to include all possible<br />\n\t\t\tareas, if a point lies on more than one map segment.</p>\n\t</body>\n</html>\n");
        jScrollPane1.setViewportView(jEditorPane1);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(341, 341, 341)
                .addComponent(jButton1))
            .addComponent(jScrollPane1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1))
        );
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Close")) {
            this.setVisible(false);
        }
    }

}
