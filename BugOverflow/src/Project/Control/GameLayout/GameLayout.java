package Project.Control.GameLayout;

import Project.Entities.Bullet;
import Project.Entities.RangeEnemy;
import Project.Objects.Rectangle;
import Project.Utilities.Collision;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameLayout {

    @FXML
    GridPane mainLayoutGrid;

    AnchorPane wallLayout;
    FXMLLoader loader;

    // max dimensions of scene
    private final int WIDTH = 600;
    private final int HEIGHT = 400;

    private int radius = 25;
    private Color playerColor = Color.BLACK;
    private Color enemyColor = Color.RED;


    private Color bulletColor = Color.PURPLE;
    private Color enemyBulletColor = Color.AQUA;

    private double playerSpeed = 200;
    private double bulletSpeed = 900;
    private double enemyBulletSpeed=200;

    private int numMonst=3;
    private int numLevels =0;

    private double dx = 200;
    private double dy = 200;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private Label hpLabel;

    @FXML
    private Label monstersLabel;

    @FXML
    private Label levelsCleared;

    // position of mouse
    private double mousex;
    private double mousey;

    // difference between position of mouse and position of player
    private double mousedx;
    private double mousedy;

    private int step=0;
    private Rectangle player;
    private ArrayList<Bullet> bullets;
   // private ArrayList<Bullet> enemyBullets;
    private ArrayList<RangeEnemy> ranges;
    private ArrayList<Double> rangedMoveX;
    private ArrayList<Double> rangedMoveY;
    private ArrayList<Integer> rangedTimer;

    private Rectangle topWall;
    private Rectangle leftWall;
    private Rectangle bottomWall;
    private Rectangle rightWall;

    private double wallWidth = 40;

    private int hp;


    private double timeBetweenShots;
    private double minTimeBetweenShots = 0.2;

    private double timeBetweenEnemyShots;
    private double minTimeEnemyBullet=1;

    // player movement direction
    private int directionX;
    private int directionY;

    private int numRange=3;

    private boolean shoot;

    private long startNanoTime;
    Image image = new Image("Images/OctocatFixed.png");
    ImagePattern imagePattern = new ImagePattern(image);

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

                hp = 10;
                monstersLabel.setText("Monsters left: " + numRange);
                levelsCleared.setText("Levels cleared: " + numLevels);
                mainLayoutGrid.add(wallLayout, 0, 1);


                player = new Rectangle(50, 50, wallLayout.getScene().getWidth()/2,
                        wallLayout.getScene().getHeight()/2, 0, 0, playerColor);

                bullets = new ArrayList<>();
              //  enemyBullets = new ArrayList<>();
                ranges = new ArrayList<>();
                rangedTimer = new ArrayList<>();
                rangedMoveX = new ArrayList<>();
                rangedMoveY = new ArrayList<>();


                player.setFill(imagePattern);


                directionX = 0;
                directionY = 0;
                timeBetweenShots = 0.0;
                timeBetweenEnemyShots=0.0;
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
                for(int i=0;i<numRange;i++)
                {
                    RangeEnemy range1 = new RangeEnemy(20, (int)Math.round((Math.random()*(wallLayout.getScene().getWidth()-wallWidth-100)+wallWidth)), (int)Math.round((Math.random()*(wallLayout.getScene().getHeight()-wallWidth-100)+wallWidth)), playerSpeed/2, playerSpeed/2, enemyColor);
                    ranges.add(range1);
                    wallLayout.getChildren().add(range1);
                    rangedMoveX.add(0.0);
                    rangedMoveY.add(0.0);
                    rangedTimer.add(0);

                    image = new Image("Images/MergeConflicts.png");
                    imagePattern = new ImagePattern(image);
                    range1.setFill(imagePattern);
                }

                wallLayout.getChildren().addAll(player, leftWall, topWall, bottomWall, rightWall);

                wallLayout.setOnMouseDragged(e -> {
                    mousex = e.getX();
                    mousey = e.getY();

                    mousedx = mousex - (player.getX() + player.getWidth()/2);
                    mousedy = mousey - (player.getY() + player.getHeight()/2);
                });

                wallLayout.setOnMousePressed(e -> {
                    shoot = true;

                    mousex = e.getX();
                    mousey = e.getY();

                    mousedx = mousex - (player.getX() + player.getWidth()/2);
                    mousedy = mousey - (player.getY() + player.getHeight()/2);
                });

                wallLayout.setOnMouseReleased(e -> shoot = false);

                mainLayoutGrid.getScene().setOnKeyPressed(e -> {

                    mousedx = mousex - (player.getX() + player.getWidth()/2);
                    mousedy = mousey - (player.getY() + player.getHeight()/2);

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
                        if(timeBetweenEnemyShots<minTimeEnemyBullet)
                                timeBetweenEnemyShots += dt;

                        // move
                        double temp = Math.sqrt(Math.pow(directionX, 2) + Math.pow(directionY, 2));
                        if (temp != 0)
                            temp = 1/temp;
                        player.setX(player.getX() + temp*playerSpeed*directionX*dt);
                        player.setY(player.getY() + temp*playerSpeed*directionY*dt);

                        // check collision with walls
                        Collision.collisionWithWalls(leftWall, rightWall, topWall, bottomWall, player);

                        for (RangeEnemy enemy : ranges) {
                            Collision.collisionWithWalls(leftWall, rightWall, topWall, bottomWall, enemy);
                        }

                        //Range enemy move
                        for(int i=0;i<ranges.size();i++)
                        {
//                            if(rangedTimer.get(i)  0) {
//                                rangedTimer.set(i, (int) (Math.random() * 100 + 100));
//                                rangedMoveX.set(i, (ranges.get(i).getCenterX() + player.getCenterX() * Math.random() * dt * 2.0 ));
//                                rangedMoveY.set(i, (ranges.get(i).getCenterY() + (600 * Math.random() - 300.0)) * dt);
//                            }
//                            else {
//                                System.out.println(rangedTimer);
//                                rangedTimer.set(i, rangedTimer.get(i)-1);
//                            }
//
//                            ranges.get(i).setCenterX(rangedMoveX.get(i));
//                            ranges.get(i).setCenterY(rangedMoveY.get(i));

                            //entity is to the left of player
//                            if( ranges.get(i).getCenterX()+ransges.get(i).rand-player.getCenterX()<0){
//                                ranges.get(i).setCenterX(ranges.get(i).getCenterX() + ranges.get(i).vx * dt);
//                                //entity is above player
//                                if(ranges.get(i).getCenterY()+ranges.get(i).rand-player.getCenterY()<0) {
//                                    ranges.get(i).setCenterY(ranges.get(i).getCenterY() + ranges.get(i).vy * dt);
//                                }else{
//                                    //entity below player
//                                    ranges.get(i).setCenterY(ranges.get(i).getCenterY() + ranges.get(i).vy *-1* dt);
//                                }
//                            }else{
//                                //entity is to the right of player
//                                ranges.get(i).setCenterX(ranges.get(i).getCenterX() + ranges.get(i).vx *-1* dt);
//                            }
//                                if(rangedTimer.get(i) == 0) {
//                                    rangedTimer.set(i, (int) (Math.random() * 100 + 100));
//                                    rangedMoveX.set(i, (ranges.get(i).getCenterX() + 10 * Math.pow(-1, Math.round((Math.random() * (2 - 1) + 1))) * dt*100));
//                                    rangedMoveY.set(i, (ranges.get(i).getCenterY() + 100 * Math.pow(-1, Math.round((Math.random() * (2 - 1) + 1))) * dt*100));
//                                }
//                                else {
//                                    System.out.println(rangedTimer);
//                                    rangedTimer.set(i, rangedTimer.get(i)-1);
//
//                                }
                            if(step%10 == 0) {
                                ranges.get(i).setCenterX(ranges.get(i).getCenterX() + 30 * Math.pow(-1, Math.round((Math.random() * (2 - 1) + 1))) * dt);
                                ranges.get(i).setCenterY(ranges.get(i).getCenterY() + 30 * Math.pow(-1, Math.round((Math.random() * (2 - 1) + 1))) * dt);
                            }

                        }
                       step++;

                        // shoot
                        if (timeBetweenShots >= minTimeBetweenShots) {
                            timeBetweenShots = 0.0;
                            if (shoot)
                                shoot();
                        }
                        if(timeBetweenEnemyShots>=minTimeEnemyBullet){
                            timeBetweenEnemyShots = 0.0;
                            for(int i=0;i<ranges.size();i++)
                            {
                                enemyShoot(ranges.get(i));
                            }
                        }

                        // update bullets
                        for (int i = 0; i < bullets.size(); i++) {

                            Bullet bullet = bullets.get(i);

                            bullet.update(dt);

                            // check bullet leaving screen
                            if (bullet.getCenterX() < -bullet.getRadius() ||
                                    bullet.getCenterX() > wallLayout.getWidth() + bullet.getRadius() ||
                                    bullet.getCenterY() < -bullet.getRadius() ||
                                    bullet.getCenterY() > wallLayout.getHeight() + bullet.getRadius()) {

                                bullets.remove(bullet);
                                wallLayout.getChildren().remove(bullet);
                                i--;
                                break;
                            }

                            // check collision with dummy
                            for (int j = 0; j < ranges.size(); j++) {

                                RangeEnemy dummy = ranges.get(j);

                                if (!bullet.isEnemy&&Collision.intersects(bullet, dummy)) {

                                    bullets.remove(bullet);
                                    wallLayout.getChildren().remove(bullet);

                                    ranges.remove(dummy);
                                    wallLayout.getChildren().remove(dummy);
                                    i--;
                                    j--;

                                    numRange--;
                                    monstersLabel.setText("Monsters left: " + numRange);

                                    if (numRange==0)
                                        System.out.println("YOU'VE WON");
                                }
                            }

                            // check collision with player
                            if (bullet.isEnemy && Collision.intersects(bullet, player)) {

                                bullets.remove(bullet);
                                wallLayout.getChildren().remove(bullet);
                                i--;

                                hp--;

                                if(hp == 0) {
                                    try {
                                        Parent root = FXMLLoader.load(getClass().getResource("../../FXML/Deathscreen.fxml"));
                                        Scene scene = new Scene(root, mainAnchor.getWidth(), mainAnchor.getHeight());

                                        Stage stage = (Stage)mainAnchor.getScene().getWindow();

                                        stage.setScene(scene);
                                        stage.show();
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                                hpLabel.setText("HP: " + hp + "/10");
                            }

                        }
                        if(numRange==0)
                            respawn(numMonst+=2);
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

    Bullet bullet = new Bullet(5, player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2, x, y, bulletColor);
    bullets.add(bullet);

    wallLayout.getChildren().add(bullet);
}
    private void enemyShoot(RangeEnemy enemy) {

        double d = Math.sqrt(Math.pow(enemy.getCenterX()-(player.getX() + player.getWidth()/2), 2) + Math.pow(enemy.getCenterY()-(player.getY() + player.getHeight()/2), 2));

        double vx = enemyBulletSpeed * (enemy.getCenterX()-(player.getX() + player.getWidth()/2)) / -d;
        double vy = enemyBulletSpeed *( enemy.getCenterY()-(player.getY() + player.getHeight()/2) )/ -d;

        Bullet bullet = new Bullet(5, enemy.getCenterX(), enemy.getCenterY(),vx, vy, enemyBulletColor);
        bullet.isEnemy=true;
        bullets.add(bullet);

        wallLayout.getChildren().add(bullet);
    }

    private void respawn( int num){
        /*
         * Make dummy targets.
         */
        numRange=num;
        for(int i=0;i<numRange;i++)
        {
            RangeEnemy range1 = new RangeEnemy(20, (int)Math.round((Math.random()*(wallLayout.getScene().getWidth()-wallWidth-100)+wallWidth)), (int)Math.round((Math.random()*(wallLayout.getScene().getHeight()-wallWidth-100)+wallWidth)), playerSpeed/2, playerSpeed/2, enemyColor);
            ranges.add(range1);
            wallLayout.getChildren().add(range1);
            rangedMoveX.add(0.0);
            rangedMoveY.add(0.0);
            rangedTimer.add(0);

            image = new Image("Images/MergeConflicts.png");
            imagePattern = new ImagePattern(image);
            range1.setFill(imagePattern);


        }

        numLevels++;
        levelsCleared.setText("Levels cleared: " + numLevels);
    }



}


