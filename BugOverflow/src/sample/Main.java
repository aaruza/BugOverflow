package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    private final int WIDTH = 600;
    private final int HEIGHT = 400;

    private int radius = 25;
    private Color playerColor = Color.BLACK;
    private Color bulletColor = Color.PURPLE;

    private double dx = 200;
    private double dy = 200;

    private double mousedx;
    private double mousedy;

    private Group root;

    private Circle player;
    private ArrayList<Circle> bullets;
    private ArrayList<Circle> dummies;

    private double bulletSpeed = 900;

    private double timeBetweenShots;
    private double minTimeBetweenShots = 0.5;

    private char directionX;
    private char directionY;

    private boolean shoot;

    private long startNanoTime;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle( "Canvas Example" );

        player = new sample.Circle(radius, 40, 50, 0, 0, playerColor);

        bullets = new ArrayList<>();
        dummies = new ArrayList<>();

        directionX = 'x';
        directionY = 'x';
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

        root = new Group();
        root.getChildren().addAll(player, dummy1, dummy2, dummy3);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(e -> {

            // move up down
            switch (e.getCode()) {

                // move
                case UP:
                    directionY = 'u';
                    break;
                case DOWN:
                    directionY = 'd';
                    break;
            }

            // move left right
            switch (e.getCode()) {
                case LEFT:
                    directionX = 'l';
                    break;
                case RIGHT:
                    directionX = 'r';
                    break;
            }

        });

        scene.setOnKeyReleased(e -> {

            // move
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN)
                directionY = 'x';
            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT)
                directionX = 'x';
        });

        scene.setOnMouseDragged(e -> {
            mousedx = e.getX() - player.getCenterX();
            mousedy = e.getY() - player.getCenterY();
        });

        scene.setOnMousePressed(e -> {
            shoot = true;
        });

        scene.setOnMouseReleased(e -> shoot = false);

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
                switch (directionX) {

                    case 'l':
                        player.setCenterX(player.getCenterX() - dx * dt);
                        break;
                    case 'r':
                        player.setCenterX(player.getCenterX() + dx * dt);
                        break;
                }

                switch (directionY) {

                    case 'u':
                        player.setCenterY(player.getCenterY() - dy * dt);
                        break;
                    case 'd':
                        player.setCenterY(player.getCenterY() + dy * dt);
                        break;
                }

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
                        root.getChildren().remove(bullet);
                        i--;
                        break;
                    }

                    // check collision with dummy
                    for (int j = 0; j < dummies.size(); j++) {

                        Circle dummy = dummies.get(j);

                        if (bullet.intersects(dummy)) {

                            bullets.remove(bullet);
                            root.getChildren().remove(bullet);

                            dummies.remove(dummy);
                            root.getChildren().remove(dummy);
                            i--;
                            j--;
                        }
                    }

                }

            }
        };

        animationTimer.start();
    }

    private void shoot() {

        double d = Math.sqrt(Math.pow(mousedx, 2) + Math.pow(mousedy, 2));

        double x = bulletSpeed * mousedx / d;
        double y = bulletSpeed * mousedy / d;

        Circle bullet = new Circle(5, player.getCenterX(), player.getCenterY(), x, y, bulletColor);
        bullets.add(bullet);

        root.getChildren().add(bullet);
    }

}
