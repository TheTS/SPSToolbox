package sps.path.generator;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author int
 */
public abstract class RSMapView extends JPanel implements MouseListener , MouseMotionListener {

    private File mapLoc;
    private double shiftX, shiftY;
    private Point mouseLoc, lastDragLoc = new Point(0 , 0);
    BufferedImage mapImage;
    private RSMiniMap minimap;
    private boolean controlDown = false;
    private Point[] pathPoints = new Point[0];
    private boolean rightMouseDown;
    private Point applyingPoint;

    public RSMapView() {
        super();
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        minimap = new RSMiniMap(this , mapImage);
    }

    public void reloadMapImage() {
        if (mapImage != null) {
            mapImage.flush();
            mapImage = null;
        }
        minimap.setMapImage(null);
        System.gc();
        try {
            if (mapLoc == null) {
                return;
            }
            if (!mapLoc.canRead()) {
                return;
            }
            mapImage = ImageIO.read(mapLoc);
            minimap.setMapImage(mapImage);
        } catch (IOException ex) {
            Logger.getLogger(RSMapView.class.getName()).log(Level.SEVERE , null , ex);
        }
    }

    public void loadMapImage(String filePath) {
        mapLoc = new File(filePath);
        reloadMapImage();
        repaint();
    }

    public void setMiniMapView() {
        if (mapImage == null || minimap == null) {
            return;
        }
        getMinimap().setView(shiftX / mapImage.getWidth() , shiftY / mapImage.getHeight() , (double) this.getWidth() / mapImage.getWidth() , (double) this.getHeight() / mapImage.getHeight());
    }

    public void moveViewToMapCoords(int x , int y) {
        if (mapImage == null) {
            return;
        }
        moveViewTo((double) x / mapImage.getWidth() ,
                   (double) y / mapImage.getHeight());
    }

    public void moveViewTo(double x , double y) {
        if (mapImage == null) {
            return;
        }
        //set the shifts, then correct them if they are out of range
        shiftX = (x * mapImage.getWidth()) - this.getWidth() / 2;
        shiftX =
        Math.max(0 , Math.min(shiftX , mapImage.getWidth() - this.getWidth()));
        shiftY = (y * mapImage.getHeight()) - this.getHeight() / 2;
        shiftY =
        Math.max(0 , Math.min(shiftY , mapImage.getHeight() - this.getHeight()));
        repaint();
        setMiniMapView();
    }

    @Override
    public void paint(Graphics g) {
        pathPoints = getPoints();
        super.paint(g);
        if (mapImage != null) {
            g.drawImage(mapImage , 0 , 0 , this.getWidth() , this.getHeight() , (int) shiftX , (int) shiftY , (int) shiftX + this.getWidth() , (int) shiftY + this.getHeight() , this);
        }
        drawPath(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            lastDragLoc.setLocation(e.getX() , e.getY());
            rightMouseDown = true;
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            applyingPoint = e.getPoint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightMouseDown = false;
        } else if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.getPoint().distance(applyingPoint) < 20) {
                clickedPoint(new Point((int) (e.getX() + shiftX) , (int) (e.getY() + shiftY)) , e.getButton());
                pathPoints = getPoints();
                repaint();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mapImage == null) {
            return;
        }
        if (!rightMouseDown) {
            setMapCoordLabelVal((int)shiftX + e.getX(), (int)shiftY + e.getY());
            setMouseLabelVal(e.getX(), e.getY());
            return;
        }
        int speed = (e.isControlDown() ? 5 : 1);

        shiftX += (lastDragLoc.getX() - e.getX()) * speed;
        shiftX = Math.max(0 , Math.min(shiftX , mapImage.getWidth() - this.getWidth()));

        shiftY += (lastDragLoc.getY() - e.getY()) * speed;
        shiftY = Math.max(0 , Math.min(shiftY , mapImage.getHeight() - this.getHeight()));

        lastDragLoc.setLocation(e.getPoint());
        repaint();
        setMiniMapView();
        
        setMapCoordLabelVal((int)shiftX + e.getX(), (int)shiftY + e.getY());
        setMouseLabelVal(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseLoc = e.getPoint();
        if (isRepositioning()) {
            repaint();
        }
        setMapCoordLabelVal((int)shiftX + e.getX(), (int)shiftY + e.getY());
        setMouseLabelVal(e.getX(), e.getY());
    }


    public File getMapLoc() {
        return mapLoc;
    }

    public RSMiniMap getMinimap() {
        return minimap;
    }

    public abstract void clickedPoint(Point p , int button);

    public abstract Point[] getPoints();

    public abstract boolean isRepositioning();

    public abstract int getRepositionedPointIndex();

    public abstract int getSelectedPointIndex();

    public abstract int[] getSelectedPointIndexs();

    public abstract int getMaxLineLength();

    public abstract int getFogOpacity();

    public abstract Color getLineCol();

    public abstract Color getWarnLineCol();

    public abstract Color getPointCol();

    public abstract Color getSelPointCol();
    
    public abstract void setMapCoordLabelVal(int x, int y);
    
    public abstract void setMouseLabelVal(int x, int y);

    private void drawPath(Graphics gg) {
        Graphics2D g = (Graphics2D) gg;
        Point startP, endP;
        int radius = 3;
        int miniMapRad = 76;
        int WarnDist = 70;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
        if (getSelectedPointIndex() >= 0 && getSelectedPointIndex() < pathPoints.length) {
            g.setColor(new Color(0 , 0 , 0 , getFogOpacity() / 100f));
            Area[] exclusions = new Area[getSelectedPointIndexs().length];
            for (int i = 0; i < getSelectedPointIndexs().length; i++) {
                exclusions[i] =
                getMiniMapExclusion(pathPoints[getSelectedPointIndexs()[i]] , miniMapRad);
            }
            Area Intersection = exclusions[0];
            for (int i = 1; i < getSelectedPointIndexs().length; i++) {
                Intersection.intersect(exclusions[i]);
            }
            g.fill(Intersection);
        }
        g.setColor(getPointCol());
        boolean thisPointIsSelected;
        for (int i = 0; i < pathPoints.length; i++) {
            thisPointIsSelected = false;
            for (int ii : getSelectedPointIndexs())
                if (i == ii)
                    thisPointIsSelected = true;
            if (!thisPointIsSelected)
                g.drawOval(pathPoints[i].x - radius - (int) shiftX , pathPoints[i].y - radius - (int) shiftY , radius * 2 , radius * 2);
        }
        if (pathPoints.length > 1) {
            for (int i = 1; i < pathPoints.length; i++) {
                if (isRepositioning() && getRepositionedPointIndex() == i - 1) {
                    startP = new Point(mouseLoc.x , mouseLoc.y);
                    endP =
                    new Point(pathPoints[i].x - (int) shiftX , pathPoints[i].y - (int) shiftY);
                } else if (isRepositioning() && getRepositionedPointIndex() == i) {
                    startP =
                    new Point(pathPoints[i - 1].x - (int) shiftX , pathPoints[i - 1].y - (int) shiftY);
                    endP = new Point(mouseLoc);
                } else {
                    startP =
                    new Point(pathPoints[i - 1].x - (int) shiftX , pathPoints[i - 1].y - (int) shiftY);;
                    endP =
                    new Point(pathPoints[i].x - (int) shiftX , pathPoints[i].y - (int) shiftY);
                }
                if (startP.distance(endP) >= getMaxLineLength()) {
                    g.setColor(getWarnLineCol());
                    g.setStroke(new BasicStroke(2 , BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND , 1 , new float[]{5 , 4} , 0));
                } else {
                    g.setColor(getLineCol());
                    g.setStroke(new BasicStroke(1));
                }
                g.drawLine(startP.x , startP.y , endP.x , endP.y);
                g.setStroke(new BasicStroke(1));
            }

            //draw current points circle last so its on top
            if (getSelectedPointIndex() >= 0 && getSelectedPointIndex() < pathPoints.length) {
                g.setColor(getSelPointCol());
                for (int i : getSelectedPointIndexs())
                  g.drawOval(pathPoints[i].x - radius - (int) shiftX , pathPoints[i].y - radius - (int) shiftY , radius * 2 , radius * 2);
            }

        }
    }

    private Area getMiniMapExclusion(Point p , int radius) {
        Area a =
             new Area(new Rectangle2D.Double(0 , 0 , getWidth() , getHeight()));
        a.subtract(new Area(new Rectangle2D.Double(p.x - radius - shiftX , p.y - radius - shiftY , radius * 2 , radius * 2)));
        return a;
    }
}
