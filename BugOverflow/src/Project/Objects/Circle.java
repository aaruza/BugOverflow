package Project.Objects;

import javafx.scene.paint.Color;

/**
 * Defines a circle entity.
 */
public class Circle extends javafx.scene.shape.Circle {

    // x-component of velocity
    private double vx;

    // y-component of velocity
    private double vy;

    /**
     * Instantiates a circle entity.
     *
     * @param radius
     * @param x     x-position of the circle's center
     * @param y     y-position of the circle's center
     * @param vx    x-component of the circle's velocity
     * @param vy    y-component of the circle's velocity
     * @param color
     */
    public Circle(double radius, double x, double y, double vx, double vy, Color color) {

        this.setRadius(radius);
        this.setCenterX(x);
        this.setCenterY(y);
        this.vx = vx;
        this.vy = vy;
        this.setFill(color);
    }

    /**
     * Update's the circle's position based on its velocity.
     *
     * @param dt    time interval
     */
    public void update(double dt) {

        this.setCenterX(this.getCenterX() + vx * dt);
        this.setCenterY(this.getCenterY() + vy * dt);
    }

    /**
     * Checks whether or not this circle intersects with another circle.
     *
     * @param otherCircle
     * @return true if intersects, false otherwise
     */
    public boolean intersects(Circle otherCircle) {

        if (Math.sqrt(Math.pow(otherCircle.getCenterX() - this.getCenterX(), 2)
                + Math.pow(otherCircle.getCenterY() - this.getCenterY(), 2)) < otherCircle.getRadius() + this.getRadius())
            return true;
        else
            return false;
    }

    /**
     * Checks if this circle intersects another rectangle.
     *
     * @param rectangle
     * @return true if intersects, false otherwise
     */
    public boolean intersects(Rectangle rectangle) {

        return rectangle.intersects(this);
    }

    /**
     * Returns the x-component of the circle's velocity.
     * @return vx
     */
    public double getVx() {
        return vx;
    }

    /**
     * Returns the y-component of the circle's velocity.
     * @return vy
     */
    public double getVy() {
        return vy;
    }

    /**
     * Sets the x-component of the circle's velocity.
     */
    public void setVx(double vx) {
        this.vx = vx;
    }

    /**
     * Sets the y-component of the circle's velocity.
     */
    public void setVy(double vy) {
        this.vy = vy;
    }
}
