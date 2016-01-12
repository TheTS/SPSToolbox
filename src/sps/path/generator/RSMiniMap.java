package sps.path.generator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author int
 */
public class RSMiniMap extends JPanel implements MouseListener,MouseMotionListener{
    
    RSMapView map;
    private BufferedImage mapImage;
    private Rectangle2D viewRect;
    
    public RSMiniMap(RSMapView map, BufferedImage mapImage) {
        this.map = map;
        this.mapImage = mapImage;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (getMapImage() == null) {
            return;
        }
        g.setColor((Color.CYAN));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (!(getMapImage() != null))
                return;
        g.drawImage(getMapImage(), 0, 0, this.getWidth(), this.getHeight(), this);
        
        if (viewRect == null){
            return;
        }
        g.setColor(Color.red);
        g.drawRect((int)viewRect.getX(), (int)viewRect.getY(), (int)viewRect.getWidth(), (int)viewRect.getHeight());
        g.setColor(new Color(255,0,0,255/4));
        g.fillRect((int)viewRect.getX(), (int)viewRect.getY(), (int)viewRect.getWidth(), (int)viewRect.getHeight());
    }

    void setView(double x1, double y1, double x2, double y2) {
        if (viewRect == null){
            viewRect = new Rectangle2D.Double(x1, y1, x2, y2);
        } else {
            viewRect.setRect(x1*this.getWidth(), y1*this.getHeight(), x2*this.getWidth(), y2*this.getHeight());
        }
        repaint();
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(100,100);
    }
    @Override
    public Dimension getMaximumSize() {
        return new Dimension(100,100);
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100,100);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        map.moveViewTo((double)e.getX()/this.getWidth(), (double)e.getY()/this.getHeight());
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
    public void mouseDragged(MouseEvent e) {
        map.moveViewTo((double)e.getX()/this.getWidth(), (double)e.getY()/this.getHeight());
    }

    @Override
    public void mouseMoved(MouseEvent e) { 
    }

    /**
     * @return the mapImage
     */
    public BufferedImage getMapImage() {
        return mapImage;
    }

    /**
     * @param mapImage the mapImage to set
     */
    public void setMapImage(BufferedImage mapImage) {
        this.mapImage = mapImage;
        repaint();
    } 
}