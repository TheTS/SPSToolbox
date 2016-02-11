package toolbox;

import javax.swing.*;
import java.awt.*;

class SidePane extends JPanel {

    private final PointTable pointTable;
    private final RSMapView map;

    public SidePane(RSMapView map) {
        this.map = map;
        JScrollPane tablePane;

        pointTable = new PointTable() {
            @Override
            public RSMapView getMapView() {
                return map;
            }
        };

        tablePane = new JScrollPane(getPointTable());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setMaximumSize(new Dimension(125, 2000));
        setMinimumSize(new Dimension(125, super.getHeight()));
        setPreferredSize(new Dimension(125, super.getHeight()));
        add(tablePane);
    }

    public PointTable getPointTable() { return pointTable; }

    public void addTablePoint(Point p) {
        getPointTable().addPoint(p);
        map.repaint();
    }

    public Point[] getPoints() {
        return getPointTable().getPointArr();
    }
}
