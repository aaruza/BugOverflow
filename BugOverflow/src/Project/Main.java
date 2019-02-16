package Project;

import Project.Objects.Circle;
import Project.Utilities.Collision;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    private final int WIDTH = 600;
    private final int HEIGHT = 400;

    private int radius = 25;
    private Color color = Color.RED;

    private  ArrayList<Circle> circles;

    private long startNanoTime;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle( "Canvas Example" );

        Group root = new Group();

        circles = new ArrayList<>();

        Circle c1 = new Circle(radius, 40, 50, 400, 100, color);
        circles.add(c1);

        Circle c2 = new Circle(radius, 80, 100, 100, -300, color);
        circles.add(c2);

        Circle c3 = new Circle(radius, 200, 300, -150, -200, color);
        circles.add(c3);

        Circle c4 = new Circle(radius, 300, 100, -100, -340, color);
        circles.add(c4);

        Circle c5 = new Circle(radius, 30, 200, 70, 150, color);
        circles.add(c5);

        Circle c6 = new Circle(radius, 60, 200, -130, 280, color);
        circles.add(c6);

        Circle c7 = new Circle(radius, 90, 30, -200, 200, color);
        circles.add(c7);

        Circle c8 = new Circle(radius, 30, 350, 260, -100, color);
        circles.add(c8);

        Circle c9 = new Circle(radius, 200, 200, -350, 150, color);
        circles.add(c9);

        Circle c10 = new Circle(radius, 150, 300, 190, 140, color);
        circles.add(c10);

        Circle c11 = new Circle(radius, 150, 200, 170, -200, color);
        circles.add(c11);

        Circle c12 = new Circle(radius, 100, 30, 250, -200, color);
        circles.add(c12);

        Circle c13 = new Circle(radius, 260, 30, 260, -230, color);
        circles.add(c13);

        Circle c14 = new Circle(radius, 70, 90, -190, 170, color);
        circles.add(c14);

        root.getChildren().addAll(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();


        startNanoTime = System.nanoTime();

        AnimationTimer animationTimer = new AnimationTimer() {

            @Override
            public void handle(long currentNanoTime) {

                // get time
                double dt = (currentNanoTime - startNanoTime) / 1000000000.0;
                startNanoTime = currentNanoTime;

                // update
                for (Circle c : circles) {
                    c.update(dt);
                }
                Collision.checkForCollisions(circles, WIDTH, HEIGHT);
            }
        };

        animationTimer.start();
    }

}
