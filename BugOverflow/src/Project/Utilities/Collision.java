package Project.Utilities;

import Project.Objects.Circle;
import Project.Objects.Rectangle;

import java.util.ArrayList;


public class Collision {
/*
    public static void checkForCollisions(ArrayList<Circle> circles, int width, int height) {

        for (int i = 0; i < circles.size(); i++) {

            // check collision with other circles
            for (int j = i + 1; j < circles.size(); j++) {

                // collision
                if (circles.get(i).intersects(circles.get(j))) {

                    // update
                    update(circles.get(i), circles.get(j));
                }

            }

            // check collision with walls

            double x = circles.get(i).getCenterX();
            double y = circles.get(i).getCenterY();
            double r = circles.get(i).getRadius();
            Circle c = circles.get(i);

            if (x + r >= width) {
                c.setCenterX(width - r);
                c.setVx(-c.getVx());
            }

            else if (x - r <= 0) {
                c.setCenterX(r);
                c.setVx(-c.getVx());
            }

            if (y + r >= height) {
                c.setCenterY(height - r);
                c.setVy(-c.getVy());
            }

            else if (y - r <= 0) {
                c.setCenterY(r);
                c.setVy(-c.getVy());
            }
        }

    }

    private static void update(Circle c1, Circle c2) {

        // update positions

        double dx = c1.getCenterX() - c2.getCenterX();
        double dy = c1.getCenterY() - c2.getCenterY();

        double l = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        double d = (c1.getRadius() + c2.getRadius() - l) / 2;

        double x = d * dx / l;
        double y = d * dy / l;

        c1.setCenterX(c1.getCenterX() + x);
        c1.setCenterY(c1.getCenterY() + y);

        c2.setCenterX(c2.getCenterX() - x);
        c2.setCenterY(c2.getCenterY() - y);


        // update velocities

        double c1x = c1.getVx();
        double c1y = c1.getVy();
        double c2x = c2.getVx();
        double c2y = c2.getVy();

        c1.setVx(c2x);
        c1.setVy(c2y);

        c2.setVx(c1x);
        c2.setVy(c1y);
    }
*/
    /**
     * Checks whether or not this rectangle intersects with another rectangle.
     *
     *
     * @param rectangle
     * @param otherRectangle
     * @return true if intersects, false otherwise
     */
    public static boolean intersects(Rectangle rectangle, Rectangle otherRectangle) {

        // check horizontal direction
        if (rectangle.getX() + rectangle.getWidth()/2 <= otherRectangle.getX() - otherRectangle.getWidth()/2 ||
                rectangle.getX() - rectangle.getWidth()/2 >= otherRectangle.getX() + otherRectangle.getWidth()/2)
            return false;

        // check vertical direction
        if (rectangle.getY() + rectangle.getHeight()/2 <= otherRectangle.getY() - otherRectangle.getHeight()/2 ||
                rectangle.getY() - rectangle.getHeight()/2 >= otherRectangle.getY() + otherRectangle.getHeight()/2)
            return false;

        return true;
    }

    /**
     * Checks whether or not this rectangle intersects with another circle.
     *
     * @param rectangle
     * @param circle
     * @return true if intersects, false otherwise
     */
    public static boolean intersects(Rectangle rectangle, Circle circle) {

        // check horizontal direction
        if (rectangle.getX() + rectangle.getWidth()/2 <= circle.getCenterX() - circle.getRadius() ||
                rectangle.getX() - rectangle.getWidth()/2 >= circle.getCenterX() + circle.getRadius())
            return false;

        // check vertical direction
        if (rectangle.getY() + rectangle.getHeight()/2 <= circle.getCenterY() - circle.getRadius() ||
                rectangle.getY() - rectangle.getHeight()/2 >= circle.getCenterY() + circle.getRadius())
            return false;


        // check corners:

        // clamp closest point to the rectangle's extents
        double x = clamp(circle.getCenterX(), rectangle.getX() - rectangle.getWidth()/2, rectangle.getX() + rectangle.getWidth()/2);
        double y = clamp(circle.getCenterY(), rectangle.getY() - rectangle.getHeight()/2, rectangle.getY() + rectangle.getHeight()/2);


        // get minimum distance between circle and rectangle (uses Pythag)
        double dx = x - circle.getCenterX();
        double dy = y - circle.getCenterY();

        double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));


        // check for intersection
        if (distance >= circle.getRadius())
            return false;

        return true;
    }

    /**
     * Clamps a value to a specified range.
     *
     * @param x     the value
     * @param min   minimum value to clamp to
     * @param max   maximum value to clamp to
     * @return
     */
    public static double clamp(double x, double min, double max) {

        if (x < min)
            x = min;
        else if ( x > max)
            x = max;

        return x;
    }
    /**
     * Checks whether or not this circle intersects with another circle.
     * @param circle
     * @param otherCircle
     * @return true if intersects, false otherwise
     */
    public static boolean intersects(Circle circle, Circle otherCircle) {

        if (Math.sqrt(Math.pow(otherCircle.getCenterX() - circle.getCenterX(), 2)
                + Math.pow(otherCircle.getCenterY() - circle.getCenterY(), 2)) < otherCircle.getRadius() + circle.getRadius())
            return true;
        else
            return false;
    }

    /**
     * Checks if this circle intersects another rectangle.
     *
     * @param circle
     * @param rectangle
     * @return true if intersects, false otherwise
     */
    public static boolean intersects(Circle circle, Rectangle rectangle) {

        return intersects(rectangle, circle);
    }

}
