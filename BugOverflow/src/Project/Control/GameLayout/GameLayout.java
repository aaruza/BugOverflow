package Project.Control.GameLayout;

import Project.Objects.Circle;
import Project.Objects.Rectangle;
import Project.Utilities.Collision;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.util.ArrayList;

public class GameLayout {

    @FXML
    GridPane mainLayoutGrid;

    AnchorPane wallLayout;
    FXMLLoader loader;

    @FXML
    Label hpLabel;

//    AnchorPane aLeft1, aLeft2, aRight1, aRight2, aTop1, aTop2, aBottom1, aBottom2;
//    javafx.scene.shape.Rectangle left1, left2, right1, right2, top1, top2, bottom1, bottom2;

    GridPane playPane;

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

    private Rectangle player;
    private ArrayList<Circle> bullets;
    private ArrayList<Circle> dummies;

    private Rectangle topWall;
    private Rectangle leftWall;
    private Rectangle bottomWall;
    private Rectangle rightWall;

    private double wallWidth = 40;

    private double timeBetweenShots;
    private double minTimeBetweenShots = 0.2;

    // player movement direction
    private int directionX;
    private int directionY;

    private boolean shoot;

    private long startNanoTime;

    public GameLayout() {

    }

    @FXML
    public void initialize(){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loader = new FXMLLoader(getClass().getResource( "../../FXML/GameLayout/WallLayoutPerm.fxml"));
                loader.setController(this);
                try {
                    wallLayout = loader.load();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                mainLayoutGrid.add(wallLayout, 0, 1);

                Image image = new Image("Images/Octocat.png");
                player = new Rectangle(50, 50, wallLayout.getScene().getWidth()/2,
                        wallLayout.getScene().getHeight()/2, 0, 0, playerColor);

                bullets = new ArrayList<>();
                dummies = new ArrayList<>();

                ImagePattern imagePattern = new ImagePattern(image);
                player.setFill(imagePattern);

                //ImageView imageView = new ImageView(image);

                directionX = 0;
                directionY = 0;
                timeBetweenShots = 0.0;
                shoot = false;


                /*
                 * Make walls.
                 */
                topWall = new Rectangle(wallLayout.getScene().getWidth(), wallWidth, 0,
                        0, 0, 0, Color.BLACK);
                bottomWall = new Rectangle(wallLayout.getScene().getWidth(), wallWidth, 0,
                        wallLayout.getScene().getHeight() - wallWidth, 0, 0, Color.BLACK);
                leftWall = new Rectangle(wallWidth, wallLayout.getScene().getHeight() - 2*wallWidth, 0,
                        wallWidth, 0, 0, Color.BLACK);
                rightWall = new Rectangle(wallWidth, wallLayout.getScene().getHeight() - 2*wallWidth,
                        wallLayout.getScene().getWidth() - wallWidth, wallWidth, 0, 0, Color.BLACK);

                /*
                 * Make dummy targets.
                 */
                Circle dummy1 = new Circle(20, 300, 200, 0, 0, Color.RED);
                Circle dummy2 = new Circle(20, 400, 100, 0, 0, Color.RED);
                Circle dummy3 = new Circle(20, 100, 200, 0, 0, Color.RED);
                dummies.add(dummy1);
                dummies.add(dummy2);
                dummies.add(dummy3);

                wallLayout.getChildren().addAll(player, dummy1, dummy2, dummy3, leftWall, topWall, bottomWall, rightWall);

                wallLayout.setOnMouseDragged(e -> {
                    mousex = e.getX();
                    mousey = e.getY();

                    mousedx = mousex - player.getX();
                    mousedy = mousey - player.getY();
                });

                wallLayout.setOnMousePressed(e -> {
                    shoot = true;

                    mousex = e.getX();
                    mousey = e.getY();

                    mousedx = mousex - player.getX();
                    mousedy = mousey - player.getY();
                });

                wallLayout.setOnMouseReleased(e -> shoot = false);

                mainLayoutGrid.getScene().setOnKeyPressed(e -> {

                    mousedx = mousex - player.getX();
                    mousedy = mousey - player.getY();

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

                mainLayoutGrid.getScene().setOnKeyReleased(e -> {

                    // move
                    if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.S)
                        directionY = 0;
                    if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.D)
                        directionX = 0;
                });

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
                        player.setX(player.getX() + temp*playerSpeed*directionX*dt);
                        player.setY(player.getY() + temp*playerSpeed*directionY*dt);

                        // check collision with walls
                        Collision.collisionWithWalls(leftWall, rightWall, topWall, bottomWall, player);

                        // shoot
                        if (shoot && timeBetweenShots >= minTimeBetweenShots) {
                            timeBetweenShots = 0.0;
                            shoot();
                        }

                        // update bullets
                        for (int i = 0; i < bullets.size(); i++) {

                            Circle bullet = bullets.get(i);

                            bullet.update(dt);

                            boolean checkDummyCollision = true;

                            // check bullet leaving screen
                            if (bullet.getCenterX() < wallWidth + bullet.getRadius() ||
                                    bullet.getCenterX() > wallLayout.getScene().getWidth() - wallWidth - bullet.getRadius() ||
                                    bullet.getCenterY() < wallWidth + bullet.getRadius() ||
                                    bullet.getCenterY() > wallLayout.getScene().getHeight() - wallWidth - bullet.getRadius()) {

                                bullets.remove(bullet);
                                wallLayout.getChildren().remove(bullet);
                                i--;
                                checkDummyCollision = false;
                            }

                            if (checkDummyCollision) {

                                // check collision with dummy
                                for (int j = 0; j < dummies.size(); j++) {

                                    Circle dummy = dummies.get(j);

                                    if (Collision.intersects(bullet, dummy)) {

                                        bullets.remove(bullet);
                                        wallLayout.getChildren().remove(bullet);

                                        dummies.remove(dummy);
                                        wallLayout.getChildren().remove(dummy);
                                        i--;
                                        j--;
                                    }
                                }
                            }


                        }

                    }
                };
                animationTimer.start();

            }
        });

    }
    private void shoot() {

        double d = Math.sqrt(Math.pow(mousedx, 2) + Math.pow(mousedy, 2));

        double x = bulletSpeed * mousedx / d;
        double y = bulletSpeed * mousedy / d;

        Circle bullet = new Circle(5, player.getX(), player.getY(), x, y, bulletColor);
        bullets.add(bullet);

        wallLayout.getChildren().add(bullet);
    }

}


