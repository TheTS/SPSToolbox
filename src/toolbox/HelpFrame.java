package toolbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class HelpFrame extends JFrame implements ActionListener {

    public HelpFrame() {
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


        jEditorPane1.setText( "<html> <head> <title></title> </head> <body> <p style=\"color:003366;\" align=\"center\"><strong>Navigation</strong></p> <p>Pan the map by left-clicking and dragging. Hold down <em>control</em> to increase panning speed.</p> <p style=\"color:003366;\" align=\"center\"><strong>Adding Points</strong></p> <p>Choose <em>area</em> or <em>path</em> in the top-right corner. Left-click the map to add a point. You can edit points by right-clicking the table.</p> <p style=\"color:003366;\" align=\"center\"><strong>Saving Paths and Areas</strong></p> <p>You can save points to file by clicking <strong><em>Points -&gt; Save Points</em></strong>. Likewise, you can load a file by clicking <strong><em>Points -&gt; Load Points</em></strong>.</p> <p style=\"color:003366;\" align=\"center\"><strong>Settings</strong></p><p style=\"margin-left: 10px;\"><strong><span style=\"color: #8b8b8b;\">Minimap Guide:</span></strong> Changes the minimap guide shape when drawing paths.</p> <p style=\"margin-left: 10px;\"><strong><span style=\"color: #8b8b8b;\">Max Line Length:</span></strong> How far two points can be apart before they are considered too far apart (see warning line below).</p> <p style=\"margin-left: 10px;\"><strong><span style=\"color: #8b8b8b;\">Line Color:</span></strong> The color of path and area lines.</p><p style=\"margin-left: 10px;\"><strong><span style=\"color: #8b8b8b;\">Warning Line color:</span></strong> The color of path lines longer than the <em>Max Line Length</em>.</p> <p style=\"margin-left: 10px;\"><strong><span style=\"color: #8b8b8b;\">Area Fill Color:</span></strong> The color used to fill areas with 3+ points.</p> <p style=\"margin-left: 10px;\"><strong><span style=\"color: #8b8b8b;\">Enable self-intersecting polgons:</span></strong> Not recommended. This enables you to draw an area which overlaps itself.</p></body></html>");

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