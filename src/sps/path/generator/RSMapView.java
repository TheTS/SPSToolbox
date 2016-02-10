package sps.path.generator;

import java.awt.*;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

public abstract class RSMapView extends JPanel implements MouseListener , MouseMotionListener {

    private File mapLoc;
    private double shiftX, shiftY;
    private Point mouseLoc, lastDragLoc;
    BufferedImage mapImage;
    private Point[] pathPoints;

    public RSMapView() {
        setBackground(Color.lightGray);
        addMouseMotionListener(this);
        addMouseListener(this);
        pathPoints = new Point[0];
    }

    public void reloadMapImage() {
        if (mapImage != null) {
            mapImage.flush();
            mapImage = null;
        }
        System.gc();
        try {
            if (mapLoc == null || !mapLoc.canRead())
                return;

            mapImage = ImageIO.read(mapLoc);
        } catch (IOException ex) {
            Logger.getLogger(RSMapView.class.getName()).log(Level.SEVERE , null , ex);
        }
    }

    public void loadMapImage(String filePath) {
        mapLoc = new File(filePath);
        reloadMapImage();
        repaint();
    }

    public void moveViewToMapCoords(int x , int y) {
        if (mapImage == null)
            return;

        moveViewTo((double) x / mapImage.getWidth() ,
                   (double) y / mapImage.getHeight());
    }

    public void moveViewTo(double x , double y) {
        if (mapImage == null)
            return;

        //set the shifts, then correct them if they are out of range
        shiftX = (x * mapImage.getWidth()) - this.getWidth() / 2;
        shiftX = Math.max(0 , Math.min(shiftX , mapImage.getWidth() - this.getWidth()));
        shiftY = (y * mapImage.getHeight()) - this.getHeight() / 2;
        shiftY = Math.max(0 , Math.min(shiftY , mapImage.getHeight() - this.getHeight()));
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseLoc = e.getPoint();
        if (isRepositioning())
            repaint();

        setMapCoordLabelVal((int)shiftX + e.getX(), (int)shiftY + e.getY());
        setMouseLabelVal(e.getPoint());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
            if (e.getClickCount() == 1) {
                Point p = e.getPoint();
                clickedPoint(new Point((int) (p.x + shiftX) , (int) (p.y + shiftY)) , e.getButton());
                pathPoints = getPoints();
                repaint();
            }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e))
            lastDragLoc = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mapImage == null)
            return;

        if (SwingUtilities.isLeftMouseButton(e)) {
            Point p = e.getPoint();
            int speed = (e.isControlDown() ? 5 : 1);

            shiftX += (lastDragLoc.x - p.x) * speed;
            shiftX = Math.max(0, Math.min(shiftX, mapImage.getWidth() - this.getWidth()));
            shiftY += (lastDragLoc.y - p.y) * speed;
            shiftY = Math.max(0, Math.min(shiftY, mapImage.getHeight() - this.getHeight()));

            lastDragLoc = p;
            setMapCoordLabelVal((int) shiftX + p.x, (int) shiftY + p.y);
            setMouseLabelVal(p);
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);

        // Drawing the map image first
        if (mapImage != null)
            g.drawImage(mapImage , 0 , 0 , this.getWidth() , this.getHeight() , (int) shiftX , (int) shiftY , (int) shiftX + this.getWidth() , (int) shiftY + this.getHeight() , this);

        pathPoints = getPoints();

        if (pathPoints.length > 0){

            // If drawing a path, draw the fog on the map (but not in the minimap guide area)
            if (getSelectedPointIndex() >= 0 && getSelectedPointIndex() < pathPoints.length && isPathActive()) {
                int miniMapRadius = (getGameMode() ? 90: 75);
                g.setColor(new Color(0 , 0 , 0 , 50 / 100f));
                Area exclusions = getMiniMapExclusion(pathPoints[getSelectedPointIndex()], miniMapRadius);
                exclusions.intersect(exclusions);
                g.fill(exclusions);
            }

            // Create a shape if drawing an area (so we can fill), else the draw lines
            if (!isPathActive()) {
                Path2D currentShape = new Path2D.Double();
                currentShape.moveTo(pathPoints[0].x, pathPoints[0].y);

                for (int i = 1; i < pathPoints.length; i++)
                    currentShape.lineTo(pathPoints[i].x, pathPoints[i].y);

                currentShape.closePath();

                AffineTransform af = AffineTransform.getTranslateInstance(-shiftX, -shiftY);
                Shape printShape = af.createTransformedShape(currentShape);
                g.setColor(new Color(255, 147, 216, 54));
                g.fill(printShape);
                g.setColor(getLineColor());
                g.draw(printShape);

                // Check is polygon is self-intersecting, and if so, delete the last point
                if (getSafePolygon())
                    if (Path2Ds.isSelfIntersecting(currentShape.getPathIterator(null))){
                        deleteSelectedPoint();
                        repaint();
                    }
            }
            if (pathPoints.length > 1) {
                Point startP, endP;

                for (int i = 1; i < pathPoints.length; i++) {
                    if (isRepositioning() && getRepositionedPointIndex() == i - 1) {
                        startP = new Point(mouseLoc.x , mouseLoc.y);
                        endP = new Point(pathPoints[i].x - (int) shiftX , pathPoints[i].y - (int) shiftY);
                    } else if (isRepositioning() && getRepositionedPointIndex() == i) {
                        startP = new Point(pathPoints[i - 1].x - (int) shiftX , pathPoints[i - 1].y - (int) shiftY);
                        endP = new Point(mouseLoc);
                    } else {
                        startP = new Point(pathPoints[i - 1].x - (int) shiftX , pathPoints[i - 1].y - (int) shiftY);
                        endP = new Point(pathPoints[i].x - (int) shiftX , pathPoints[i].y - (int) shiftY);
                    }

                    if (startP.distance(endP) >= getMaxLineLength() && isPathActive()) {
                        g.setColor(getWarnLineColor());
                        g.setStroke(new BasicStroke(2 , BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND , 1 , new float[]{5 , 4} , 0));
                    } else {
                        g.setColor(getLineColor());
                        g.setStroke(new BasicStroke(1));
                    }
                    g.drawLine(startP.x , startP.y , endP.x , endP.y);
                }
            }

            // Draw the points (colour dependant if currently selected)
            int radius = 3;

            for (int i = 0; i < pathPoints.length; i++)
                for (int j : getSelectedPointIndexs()) {
                    if (i != j) {
                        g.setColor(Color.RED);
                        g.fillOval(pathPoints[i].x - radius - (int) shiftX, pathPoints[i].y - radius - (int) shiftY, radius * 2, radius * 2);
                    } else {
                        g.setColor(Color.WHITE);
                        g.fillOval(pathPoints[i].x - radius - (int) shiftX, pathPoints[i].y - radius - (int) shiftY, radius * 2, radius * 2);
                    }
                }
        }
    }

    private Area getMiniMapExclusion(Point p , int radius) {
        Area a = new Area(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        Area b;
        if (getGameMode()) {
            b = new Area(new Ellipse2D.Double(p.x - radius - shiftX , p.y - radius - shiftY , radius * 2 , radius * 2));
        } else {
            b = new Area(new Rectangle2D.Double(p.x - radius - shiftX , p.y - radius - shiftY , radius * 2 , radius * 2));
        }
        a.subtract(b);
        return a;
    }

    // Abstract methods overriden in main class
    public abstract void clickedPoint(Point p , int button);

    public abstract void deleteSelectedPoint();

    public abstract Point[] getPoints();

    public abstract boolean isRepositioning();

    public abstract boolean isPathActive();

    public abstract int getRepositionedPointIndex();

    public abstract int getSelectedPointIndex();

    public abstract int[] getSelectedPointIndexs();

    public abstract int getMaxLineLength();

    public abstract Color getLineColor();

    public abstract Color getWarnLineColor();

    public abstract boolean getGameMode();

    public abstract boolean getSafePolygon();

    public abstract void setMapCoordLabelVal(int x, int y);

    public abstract void setMouseLabelVal(Point p);

    // Unused overrides
    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
