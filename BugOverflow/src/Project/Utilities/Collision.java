package Project.Utilities;

import Project.Objects.Circle;

import java.util.ArrayList;

public class Collision {

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
}
