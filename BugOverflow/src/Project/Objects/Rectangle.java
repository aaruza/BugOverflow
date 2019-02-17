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
