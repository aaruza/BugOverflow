package Project.Control.GameLayout;

import Project.Entities.Bullet;
import Project.Entities.MeleeEnemy;
import Project.Entities.RangeEnemy;
import Project.Objects.Circle;
import Project.Utilities.Collision;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    private double dx = 200;
    private double dy = 200;

    // position of mouse
    private double mousex;
    private double mousey;

    // difference between position of mouse and position of player
    private double mousedx;
    private double mousedy;

    private int step=0;
    private Circle player;
    private ArrayList<Bullet> bullets;
   // private ArrayList<Bullet> enemyBullets;
    private ArrayList<RangeEnemy> ranges;
    private ArrayList<MeleeEnemy> melees;
    private ArrayList<Double> rangedMoveX;
    private ArrayList<Double> meleeMoveX;
    private ArrayList<Double> rangedMoveY;
    private ArrayList<Double> meleeMoveY;
    private ArrayList<Integer> rangedTimer;
    private ArrayList<Integer> meleetimer;

    private double timeBetweenShots;
    private double minTimeBetweenShots = 0.2;

    private double timeBetweenEnemyShots;
    private double minTimeEnemyBullet=1;

    // player movement direction
    private int directionX;
    private int directionY;

    private int numRange=3;
    private int numMelee=3;

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
                player = new Circle(radius, 40, 50, 0, 0, playerColor);

                bullets = new ArrayList<>();
              //  enemyBullets = new ArrayList<>();
                ranges = new ArrayList<>();
                melees = new ArrayList<>();
                meleetimer = new ArrayList<>();
                meleeMoveX = new ArrayList<>();
                meleeMoveY = new ArrayList<>();
                rangedTimer = new ArrayList<>();
                rangedMoveX = new ArrayList<>();
                rangedMoveY = new ArrayList<>();

                ImagePattern imagePattern = new ImagePattern(image);
                player.setFill(imagePattern);

                //ImageView imageView = new ImageView(image);

                directionX = 0;
                directionY = 0;
                timeBetweenShots = 0.0;
                timeBetweenEnemyShots=0.0;
                shoot = false;


                /*
                 * Make dummy targets.
                 */
                for(int i=0;i<numRange;i++)
                {
                    RangeEnemy range1 = new RangeEnemy(20, (int)Math.round((Math.random()*(550-30)+30)), (int)Math.round((Math.random()*(30-300)+300)), playerSpeed/2, playerSpeed/2, enemyColor);
                    ranges.add(range1);
                    wallLayout.getChildren().add(range1);
                    rangedMoveX.add(0.0);
                    rangedMoveY.add(0.0);
                    rangedTimer.add(0);
                }

                for(int i=0;i<numMelee;i++)
                {
                    MeleeEnemy melee1 = new MeleeEnemy(20, (int)Math.round((Math.random()*(550-30)+30)), (int)Math.round((Math.random()*(30-300)+300)), playerSpeed/2, playerSpeed/2, enemyColor);
                    melees.add(melee1);
                    meleeMoveX.add(0.0);
                    meleeMoveY.add(0.0);
                    meleetimer.add(0);
                    wallLayout.getChildren().add(melee1);
                }

                wallLayout.getChildren().addAll(player);

                wallLayout.setOnMouseDragged(e -> {
                    mousex = e.getX();
                    mousey = e.getY();

                    mousedx = mousex - player.getCenterX();
                    mousedy = mousey - player.getCenterY();
                });

                wallLayout.setOnMousePressed(e -> {
                    shoot = true;

                    mousex = e.getX();
                    mousey = e.getY();

                    mousedx = mousex - player.getCenterX();
                    mousedy = mousey - player.getCenterY();
                });

                wallLayout.setOnMouseReleased(e -> shoot = false);

                mainLayoutGrid.getScene().setOnKeyPressed(e -> {

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
                        player.setCenterX(player.getCenterX() + temp*playerSpeed*directionX*dt);
                        player.setCenterY(player.getCenterY() + temp*playerSpeed*directionY*dt);

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

                        //Melee move
                        for(int i=0;i<melees.size();i++)
                        {
                            if(step%(numMelee-i)==0) {
                                //entity is to the left of player
                                if( melees.get(i).getCenterX()+melees.get(i).rand-player.getCenterX()<0){
                                    melees.get(i).setCenterX(melees.get(i).getCenterX() +melees.get(i). vx * dt);
                                    //entity is above player
                                    if(melees.get(i).getCenterY()+melees.get(i).rand-player.getCenterY()<0) {
                                        melees.get(i).setCenterY(melees.get(i).getCenterY() +melees.get(i). vy * dt);
                                    }else{
                                        //entity below player
                                        melees.get(i).setCenterY(melees.get(i).getCenterY() +melees.get(i). vy *-1* dt);
                                    }
                                }else{
                                    //entity is to the right of player
                                    melees.get(i).setCenterX(melees.get(i).getCenterX() + melees.get(i).vx *-1* dt);
                                }
                            }
                        }

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
                                }
                            }
                            for (int j = 0; j < melees.size(); j++) {

                                MeleeEnemy dummy = melees.get(j);

                                if (!bullet.isEnemy&&Collision.intersects(bullet, dummy)) {

                                    bullets.remove(bullet);
                                    wallLayout.getChildren().remove(bullet);

                                    melees.remove(dummy);
                                    wallLayout.getChildren().remove(dummy);
                                    i--;
                                    j--;
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

    Bullet bullet = new Bullet(5, player.getCenterX(), player.getCenterY(), x, y, bulletColor);
    bullets.add(bullet);

    wallLayout.getChildren().add(bullet);
}
    private void enemyShoot(RangeEnemy enemy) {

        double d = Math.sqrt(Math.pow(enemy.getCenterX()-player.getCenterX(), 2) + Math.pow(enemy.getCenterY()-player.getCenterY(), 2));

        double vx = enemyBulletSpeed * (enemy.getCenterX()-player.getCenterX()) / -d;
        double vy = enemyBulletSpeed *( enemy.getCenterY()-player.getCenterY() )/ -d;

        Bullet bullet = new Bullet(5, enemy.getCenterX(), enemy.getCenterY(),vx, vy, enemyBulletColor);
        bullet.isEnemy=true;
        bullets.add(bullet);

        wallLayout.getChildren().add(bullet);
    }


}


