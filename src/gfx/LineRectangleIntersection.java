package gfx;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class LineRectangleIntersection {
    // Method to calculate intersection points
    public static List<Point2D> getIntersectionPoints(Line2D.Double line, Rectangle rect) {
        List<Point2D> intersectionPoints = new ArrayList<>();

        // Define rectangle edges as lines
        Line2D.Double topEdge = new Line2D.Double(rect.x, rect.y, rect.x + rect.width, rect.y);
        Line2D.Double bottomEdge = new Line2D.Double(rect.x, rect.y + rect.height, rect.x + rect.width, rect.y + rect.height);
        Line2D.Double leftEdge = new Line2D.Double(rect.x, rect.y, rect.x, rect.y + rect.height);
        Line2D.Double rightEdge = new Line2D.Double(rect.x + rect.width, rect.y, rect.x + rect.width, rect.y + rect.height);

        // Check intersection with each edge
        checkIntersection(line, topEdge, intersectionPoints);
        checkIntersection(line, bottomEdge, intersectionPoints);
        checkIntersection(line, leftEdge, intersectionPoints);
        checkIntersection(line, rightEdge, intersectionPoints);

        return intersectionPoints;
    }

    // Helper method to check intersection between two lines and add the point to the list
    public static void checkIntersection(Line2D.Double line1, Line2D.Double line2, List<Point2D> intersectionPoints) {
        if (line1.intersectsLine(line2)) {
            Point2D intersection = getIntersectionPoint(line1, line2);
            if (intersection != null) {
                intersectionPoints.add(intersection);
            }
        }
    }

    // Method to calculate the intersection point of two lines
    public static Point2D getIntersectionPoint(Line2D.Double line1, Line2D.Double line2) {
        double x1 = line1.x1, y1 = line1.y1, x2 = line1.x2, y2 = line1.y2;
        double x3 = line2.x1, y3 = line2.y1, x4 = line2.x2, y4 = line2.y2;

        double denominator = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (denominator == 0) {
            return null; // Lines are parallel or coincident
        }

        double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denominator;
        double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denominator;

        // Check if intersection is within the line segments
        if (ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1) {
            double x = x1 + ua * (x2 - x1);
            double y = y1 + ua * (y2 - y1);
            return new Point2D.Double(x, y);
        }

        return null; // Intersection is outside of the line segments
    }
}
