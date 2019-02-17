package Project.Control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import Project.Objects.Circle;

import java.util.ArrayList;

public class ShootingTest {

    // max dimensions of scene
    private final int WIDTH = 600;
    private final int HEIGHT = 400;

    private int radius = 25;
    private Color playerColor = Color.BLACK;
    private Color bulletColor = Color.PURPLE;

    private double playerSpeed = 200;
    private double bulletSpeed = 900;

    private double dx = 200;
    private double dy = 200;

    // position of mouse
    private double mousex;
    private double mousey;

    // difference between position of mouse and position of player
    private double mousedx;
    private double mousedy;

    private Circle player;
    private ArrayList<Circle> bullets;
    private ArrayList<Circle> dummies;

    private double timeBetweenShots;
    private double minTimeBetweenShots = 0.2;

    // player movement direction
    private int directionX;
    private int directionY;

    private boolean shoot;

    private long startNanoTime;

    @FXML
    AnchorPane mainAnchor;

    public ShootingTest() {
    }

    @FXML
    public void initialize() {

        player = new Circle(radius, 40, 50, 0, 0, playerColor);

        bullets = new ArrayList<>();
        dummies = new ArrayList<>();

        directionX = 0;
        directionY = 0;
        timeBetweenShots = 0.0;
        shoot = false;


        /*
         * Make dummy targets.
         */
        Circle dummy1 = new Circle(20, 300, 200, 0, 0, Color.RED);
        Circle dummy2 = new Circle(20, 400, 100, 0, 0, Color.RED);
        Circle dummy3 = new Circle(20, 100, 200, 0, 0, Color.RED);
        dummies.add(dummy1);
        dummies.add(dummy2);
        dummies.add(dummy3);

        mainAnchor.getChildren().addAll(player, dummy1, dummy2, dummy3);

        mainAnchor.setOnMouseDragged(e -> {
            mousex = e.getX();
            mousey = e.getY();

            mousedx = mousex - player.getCenterX();
            mousedy = mousey - player.getCenterY();
        });

        mainAnchor.setOnMousePressed(e -> {
            shoot = true;

            mousex = e.getX();
            mousey = e.getY();

            mousedx = mousex - player.getCenterX();
            mousedy = mousey - player.getCenterY();
        });

        mainAnchor.setOnMouseReleased(e -> shoot = false);

        startNanoTime = System.nanoTime();

        AnimationTimer animationTimer = new AnimationTimer() {

            @Override
            public void handle(long currentNanoTime) {

                // get time
                double dt = (currentNanoTime - startNanoTime) / 1000000000.0;
                startNanoTime = currentNanoTime;

                if (timeBetweenShots < minTimeBetweenShots)
                    timeBetweenShots += dt;

                // move
                double temp = Math.sqrt(Math.pow(directionX, 2) + Math.pow(directionY, 2));
                if (temp != 0)
                    temp = 1/temp;
                player.setCenterX(player.getCenterX() + temp*playerSpeed*directionX*dt);
                player.setCenterY(player.getCenterY() + temp*playerSpeed*directionY*dt);

                // shoot
                if (shoot && timeBetweenShots >= minTimeBetweenShots) {
                    timeBetweenShots = 0.0;
                    shoot();
                }

                // update bullets
                for (int i = 0; i < bullets.size(); i++) {

                    Circle bullet = bullets.get(i);

                    bullet.update(dt);

                    // check bullet leaving screen
                    if (bullet.getCenterX() < -bullet.getRadius() ||
                            bullet.getCenterX() > WIDTH + bullet.getRadius() ||
                            bullet.getCenterY() < -bullet.getRadius() ||
                            bullet.getCenterY() > HEIGHT + bullet.getRadius()) {

                        bullets.remove(bullet);
                        mainAnchor.getChildren().remove(bullet);
                        i--;
                        break;
                    }

                    // check collision with dummy
                    for (int j = 0; j < dummies.size(); j++) {

                        Circle dummy = dummies.get(j);

                        if (bullet.intersects(dummy)) {

                            bullets.remove(bullet);
                            mainAnchor.getChildren().remove(bullet);

                            dummies.remove(dummy);
                            mainAnchor.getChildren().remove(dummy);
                            i--;
                            j--;
                        }
                    }

                }

            }
        };

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainAnchor.getScene().setOnKeyPressed(e -> {

                    mousedx = mousex - player.getCenterX();
                    mousedy = mousey - player.getCenterY();

                    // move up down
                    switch (e.getCode()) {
                        case W:
                            directionY = -1;
                            break;
                        case S:
                            directionY = 1;
                            break;
                    }

                    // move left right
                    switch (e.getCode()) {
                        case A:
                            directionX = -1;
                            break;
                        case D:
                            directionX = 1;
                            break;
                    }

                });

                mainAnchor.getScene().setOnKeyReleased(e -> {

                    // move
                    if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.S)
                        directionY = 0;
                    if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.D)
                        directionX = 0;
                });
            }
        });
        animationTimer.start();
    }

    private void shoot() {

        double d = Math.sqrt(Math.pow(mousedx, 2) + Math.pow(mousedy, 2));

        double x = bulletSpeed * mousedx / d;
        double y = bulletSpeed * mousedy / d;

        Circle bullet = new Circle(5, player.getCenterX(), player.getCenterY(), x, y, bulletColor);
        bullets.add(bullet);

        mainAnchor.getChildren().add(bullet);
    }

}
