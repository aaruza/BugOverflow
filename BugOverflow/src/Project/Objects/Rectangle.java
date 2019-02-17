package Project.Objects;

import javafx.scene.paint.Color;

/**
 * Defines a rectangle entity.
 */
public class Rectangle extends javafx.scene.shape.Rectangle {

    // x-component of velocity
    private double vx;

    // y-component of velocity
    private double vy;

    /**
     * Instantiates a rectangle entity.
     *
     * @param x     x-position of the circle's center
     * @param y     y-position of the circle's center
     * @param vx    x-component of the circle's velocity
     * @param vy    y-component of the circle's velocity
     * @param color
     */
    public Rectangle(double width, double height, double x, double y, double vx, double vy, Color color) {

        this.setWidth(width);
        this.setHeight(height);
        this.setX(x);
        this.setY(y);
        this.vx = vx;
        this.vy = vy;
        this.setFill(color);
    }

    /**
     * Update's the rectangle's position based on its velocity.
     *
     * @param dt    time interval
     */
    public void update(double dt) {

        this.setX(this.getX() + vx * dt);
        this.setY(this.getY() + vy * dt);
    }

    /**
     * Checks whether or not this rectangle intersects with another rectangle.
     *
     * @param otherRectangle
     * @return true if intersects, false otherwise
     */
    public boolean intersects(Rectangle otherRectangle) {

        // check horizontal direction
        if (this.getX() + this.getWidth()/2 <= otherRectangle.getX() - otherRectangle.getWidth()/2 ||
                this.getX() - this.getWidth()/2 >= otherRectangle.getX() + otherRectangle.getWidth()/2)
            return false;

        // check vertical direction
        if (this.getY() + this.getHeight()/2 <= otherRectangle.getY() - otherRectangle.getHeight()/2 ||
                this.getY() - this.getHeight()/2 >= otherRectangle.getY() + otherRectangle.getHeight()/2)
            return false;

        return true;
    }

    /**
     * Checks whether or not this rectangle intersects with another circle.
     *
     * @param circle
     * @return true if intersects, false otherwise
     */
    public boolean intersects(Circle circle) {

        // check horizontal direction
        if (this.getX() + this.getWidth()/2 <= circle.getCenterX() - circle.getRadius() ||
                this.getX() - this.getWidth()/2 >= circle.getCenterX() + circle.getRadius())
            return false;

        // check vertical direction
        if (this.getY() + this.getHeight()/2 <= circle.getCenterY() - circle.getRadius() ||
                this.getY() - this.getHeight()/2 >= circle.getCenterY() + circle.getRadius())
            return false;


        // check corners:

        // clamp closest point to the rectangle's extents
        double x = clamp(circle.getCenterX(), this.getX() - this.getWidth()/2, this.getX() + this.getWidth()/2);
        double y = clamp(circle.getCenterY(), this.getY() - this.getHeight()/2, this.getY() + this.getHeight()/2);


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
     * Returns the x-component of the rectangle's velocity.
     * @return vx
     */
    public double getVx() {
        return vx;
    }

    /**
     * Returns the y-component of the rectangle's velocity.
     * @return vy
     */
    public double getVy() {
        return vy;
    }

    /**
     * Sets the x-component of the rectangle's velocity.
     */
    public void setVx(double vx) {
        this.vx = vx;
    }

    /**
     * Sets the y-component of the rectangle's velocity.
     */
    public void setVy(double vy) {
        this.vy = vy;
    }
}
