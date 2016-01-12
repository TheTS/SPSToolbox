package sps.path.generator;

import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author int
 */
public class SidePane extends JPanel {

    private RSMapView map;
    private GridBagLayout gbLayout = new GridBagLayout();
    private PointTable pointTable;
    private JScrollPane tablePane;

    //Constructor
    public SidePane(RSMapView map) {
        this.map = map;

        this.pointTable = new PointTable() {

            @Override
            public RSMapView getMapView() {

                return getMap();
            }
        };

        this.tablePane = new JScrollPane(getPointTable());
        this.setLayout(gbLayout);
        
        this.add(map.getMinimap(),new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        this.add(tablePane,new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        
    }

    void addTablePoint(Point p) {
        getPointTable().addPoint(p);
        getMap().repaint();
    }

    public Point[] getPoints() {
        return getPointTable().getPointArr();
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(150,(int)super.getMinimumSize().getHeight());
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(150,(int)super.getMinimumSize().getHeight());
    }

    /**
     * @return the tablePane
     */
    public JScrollPane getTablePane() {
        return tablePane;
    }

    /**
     * @param tablePane the tablePane to set
     */
    public void setTablePane(JScrollPane tablePane) {
        this.tablePane = tablePane;
    }

    /**
     * @return the pointTable
     */
    public PointTable getPointTable() {
        return pointTable;
    }

    /**
     * @param pointTable the pointTable to set
     */
    public void setPointTable(PointTable pointTable) {
        this.pointTable = pointTable;
    }

    /**
     * @return the map
     */
    public RSMapView getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(RSMapView map) {
        this.map = map;
    }
    
    
}
