package Project.Utilities;

import Project.Objects.Circle;
import Project.Objects.Rectangle;


public class Collision {

    /**
     * Checks and resolves player collision with walls.
     *
     * @param left     wall on left
     * @param right    wall on right
     * @param top      wall on top
     * @param bottom   wall on bottom
     * @param player
     */
    public static void collisionWithWalls(Rectangle left,Rectangle right, Rectangle top, Rectangle bottom,
                                          Rectangle player) {

        // check collision with left walls
        if (intersects(player, left))
            player.setX(left.getX() + left.getWidth());

        // check collision with right walls
        else if (intersects(player, right))
            player.setX(right.getX() - player.getWidth());

        // check collision with top walls
        if (intersects(player, top))
            player.setY(top.getY() + top.getHeight());

        // check collision with bottom walls
        else if (intersects(player, bottom))
            player.setY(bottom.getY() - player.getHeight());
    }

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
        if (rectangle.getX() + rectangle.getWidth() <= otherRectangle.getX() ||
                rectangle.getX() >= otherRectangle.getX()  + otherRectangle.getWidth())
            return false;

        // check vertical direction
        if (rectangle.getY() >= otherRectangle.getY() + otherRectangle.getHeight() ||
                rectangle.getY() + rectangle.getHeight() <= otherRectangle.getY())
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
        if (rectangle.getX() + rectangle.getWidth() <= circle.getCenterX() - circle.getRadius() ||
                rectangle.getX() >= circle.getCenterX() + circle.getRadius())
            return false;

        // check vertical direction
        if (rectangle.getY() >= circle.getCenterY() + circle.getRadius() ||
                rectangle.getY() + rectangle.getHeight() <= circle.getCenterY() - circle.getRadius())
            return false;


        // check corners:

        // clamp closest point to the rectangle's extents
        double x = clamp(circle.getCenterX(), rectangle.getX(), rectangle.getX() + rectangle.getWidth());
        double y = clamp(circle.getCenterX(), rectangle.getY(), rectangle.getY() + rectangle.getHeight());


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
