package Project.Entities;

import javafx.scene.paint.Color;
import Project.Objects.Circle;

public class MeleeEnemy extends Circle{
    //int x,y,v=150;

    public double radius;
    public double x;
    public double y;
    public double vx;
    public double vy;
    public boolean alive = true;
    public boolean isTouching=false;
    public int rand=(int)Math.round((Math.random()*(-20-20)+20));
    Color color;

    //should only put positive speeds
    public MeleeEnemy(double radius, double x, double y, double vx, double vy, Color color) {
        super( radius,  x,  y,  vx,  vy,  color);
        this.setCenterX(x);
        this.setCenterY(y);
        this.x = x;
        this.vx = vx;
        this.vy = vy;
        this.setFill(color);
    }
    public void update(int pX, int pY, int t){
        //entity is to the left of player
        if( x-pX<0){
            this.setCenterX(this.getCenterX() + vx * t);
            //entity is above player
            if(y-pY<0) {
                this.setCenterY(this.getCenterY() + vy * t);
            }else{
                //entity below player
                this.setCenterY(this.getCenterY() + vy *-1* t);
            }
        }else{
            //entity is to the right of player
            this.setCenterX(this.getCenterX() + vx *-1* t);
        }
    }
    public boolean intersects(Circle otherCircle) {

        if (Math.sqrt(Math.pow(otherCircle.getCenterX() - this.getCenterX(), 2)
                + Math.pow(otherCircle.getCenterY() - this.getCenterY(), 2)) < otherCircle.getRadius() + this.getRadius())
            return true;
        else
            return false;
    }
}