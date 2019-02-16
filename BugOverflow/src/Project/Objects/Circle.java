package Project.Objects;

import javafx.scene.paint.Color;

public class Circle extends javafx.scene.shape.Circle {

    private double vx;
    private double vy;

    public Circle(double radius, double x, double y, double vx, double vy, Color color) {

        this.setRadius(radius);
        this.setCenterX(x);
        this.setCenterY(y);
        this.vx = vx;
        this.vy = vy;
        this.setFill(color);
    }

    public void update(double dt) {

        this.setCenterX(this.getCenterX() + vx * dt);
        this.setCenterY(this.getCenterY() + vy * dt);
    }

    public boolean intersects(Circle otherCircle) {

        if (Math.sqrt(Math.pow(otherCircle.getCenterX() - this.getCenterX(), 2)
                + Math.pow(otherCircle.getCenterY() - this.getCenterY(), 2)) < otherCircle.getRadius() + this.getRadius())
            return true;
        else
            return false;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }
}
