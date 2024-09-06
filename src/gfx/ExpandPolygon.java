package gfx;

import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

import world.World;

public class ExpandPolygon {
    public static Polygon expandPolygon(Polygon originalPolygon, double scale) {
        // Convert Polygon to GeneralPath for transformation
        GeneralPath path = new GeneralPath();
        path.moveTo(originalPolygon.xpoints[0], originalPolygon.ypoints[0]);
        
        for (int i = 1; i < originalPolygon.npoints; i++) {
            path.lineTo(originalPolygon.xpoints[i], originalPolygon.ypoints[i]);
        }
        path.closePath();

        // Create an AffineTransform for scaling
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        at.translate(-World.camera.getXOffset()*scale, -World.camera.getYOffset()*scale);

        // Apply transformation
        GeneralPath expandedPath = (GeneralPath) path.createTransformedShape(at);

        // Convert back to Polygon
        Polygon expandedPolygon = new Polygon();
        PathIterator iterator = expandedPath.getPathIterator(null);
        double[] coords = new double[6];

        while (!iterator.isDone()) {
            int type = iterator.currentSegment(coords);
            if (type != PathIterator.SEG_CLOSE) {
                expandedPolygon.addPoint((int) coords[0], (int) coords[1]);
            }
            iterator.next();
        }

        return expandedPolygon;
    }
}
