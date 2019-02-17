package Project.Entities;

import Project.Objects.Circle;
import javafx.scene.paint.Color;
public class Bullet extends Circle {

    //int x,y,v=150;

    public double radius;
    public double x;
    public double y;
    public double vx;
    public double vy;
    public boolean isEnemy=false;
    Color color;

    //should only put positive speeds
    public Bullet(double radius, double x, double y, double vx, double vy, Color color) {
        super( radius,  x,  y,  vx,  vy,  color);
        this.setCenterX(x);
        this.setCenterY(y);
        this.x = x;
        this.vx = vx;
        this.vy = vy;
        this.setFill(color);
    }
}
