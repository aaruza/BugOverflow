package sample;
import java.util.LinkedList;
import java.util.Queue;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

//testing
public class Main  {

    private static int numRoom,index=0;
    private static ArrayList<Room> allRooms=new ArrayList<Room>();
    private static Queue<Room> queue=new LinkedList<Room>();
    private static Group root = new Group();
    private static final int WIDTH=10,HEIGHT=10;

    public static void main(String[] args) {
        numRoom=10;
        Room seed = randRoom(-1);
        allRooms.add(seed);
        queue.add(seed);
        roomGen();
    }


    public static void roomGen() {

        int oppositeD;
        while(!queue.isEmpty())
        {
            Room r=queue.poll();
            if(r.doors!=0){
                //direction
                for (int i = 0; i < 4; i++) {
                    //check if direction is already taken
                    if (r.opening[i] == 0) {
                        if(numRoom==0){
                            for(int j=0;j<4;j++)
                              r.opening[j] = -1;
                            r.doors= 0;
                        }else {
                            oppositeD=oppositeDirection(i);
                            Room room = randRoom(oppositeD);
                            room.opening[oppositeD]=r.index;
                            System.out.print(" Room root: " + r.index + " Direction from root: " + i);
                            allRooms.add(room);
                            queue.add(room);
                            //set this direction to the newly generated room
                            r.opening[i] = room.index;
                        }
                    }
                }
            }
        }
    }
    public static int oppositeDirection(int d)
    {
        switch(d)
        {
            case 0:
                return 2;
            case 1:
                return 3;
            case 2:
                return 0;
            case 3:
                return 1;
        }
        return -1;
    }

    public static Room randRoom(int taken){
        index++;
        numRoom--;
        System.out.println("\nRoom number: "+index);
        //num of doors from 1-3
        int door=(int)Math.round((Math.random()*(3-1)+1));

        //if door num is bigger than total rooms needed, re-roll
        while(door>numRoom){
            door=(int)Math.round((Math.random()*(numRoom)));
        }
        System.out.print(" Room doors: "+door);
        //which room each door points to
        int[] openings= new int[4];
        for(int i=0;i<4;i++)
            openings[i]=-1;
        int direction;

        //decide which direction each door points to
        for(int i=0;i<door;i++)
        {
            //random int from 0-3
            //0 is north, 1 is east, 2 is south, 3 is west
            direction=(int)Math.round((Math.random()*(3)));

            while( openings[direction]==0||taken==direction){
                direction=(int)Math.round((Math.random()*(3)));
            }

            //0 means there's a room in this direction
            openings[direction]=0;


        }
        //initialize room
        Room r = new Room(door, index,openings);
        return r;
    }

    //
    public void setRec(int prev, int direction){
        Rectangle r = new Rectangle();
        switch(direction){
            case 0:  r.setY(root.getChildren().get(prev).getLayoutY()-3);
                r.setX(root.getChildren().get(prev).getLayoutX());
        }

        r.setWidth(WIDTH);
        r.setHeight(HEIGHT);
        root.getChildren().add(r);

    }


  /* public void start(Stage primaryStage) {
        primaryStage.setTitle( "Canvas Example" );

        circles = new ArrayList<>();


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
    }*/

}



