package sample;
import java.util.LinkedList;
import java.util.Queue;
import javafx.scene.Group;
import javafx.scene.shape.*;
import java.util.ArrayList;


public class Main  {

    /**
     * numRoom is the total number of rooms to generate
     * index is a variable to keep track of the current room number being created
     * allRooms is an arraylist containing all rooms generated
     * Queue is used in the breadth first search to generate the map
     * all other variables are not used and were being developed for minimap
     */
    private static int numRoom,index=-1;
    private static ArrayList<Room> allRooms=new ArrayList<Room>();
    private static Queue<Room> queue=new LinkedList<Room>();
    private static Group root = new Group();
    private static final int WIDTH=10,HEIGHT=10;

    public static void main(String[] args) {
        //state number of rooms you want to generate
        numRoom=10;

        //first initialize a room to use as the seed for bfs
        Room seed = randRoom(-1);

        //add the first room to the arraylist
        allRooms.add(seed);
        //add the first room to the queue
        queue.add(seed);
        //generate rooms
        roomGen();
    }


    public static void roomGen() {

        int oppositeD;
        while(!queue.isEmpty())
        {
            Room r=queue.poll();
            if(r.doors!=0){
                //check all four directions of room (north, south, east, west)
                for (int i = 0; i < 4; i++) {
                    //check if direction has already generated room
                    if (r.opening[i] == -1) {
                        //if no rooms are needed,make this a end room, and break out of for loop
                        if(numRoom==0){
                            for(int j=0;j<4;j++)
                              r.opening[j] = -2;
                            r.doors= 0;
                            break;
                        }else {
                            //add root room to array
                            oppositeD=oppositeDirection(i);
                            Room room = randRoom(oppositeD);
                            room.opening[oppositeD]=r.index;

                            //console printing for debugging
                            System.out.print(" Room root: " + r.index + " Direction from root: " + i);

                            //add newly generated room to arraylist, queue
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

    //switch room direction
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

    //generate random variable room
    public static Room randRoom(int taken){
        //increment index, minus numRoom
        index++;
        numRoom--;

        //debug console printing
        System.out.println("\nRoom number: "+index);

        //num of doors from 1-3
        int door=(int)Math.round((Math.random()*(3-1)+1));

        //if door num is bigger than total rooms needed, re-roll
        while(door>numRoom){
            door=(int)Math.round((Math.random()*(numRoom)));
        }

        //debug
        System.out.print(" Room doors: "+door);

        //instantiate array that stores what room each door points to
        int[] openings= new int[4];

        //initiate to -2
        for(int i=0;i<4;i++)
            openings[i]=-2;
        int direction;

        //decide which direction each door points to
        for(int i=0;i<door;i++)
        {
            //random int from 0-3
            //0 is north, 1 is east, 2 is south, 3 is west
            direction=(int)Math.round((Math.random()*(3)));

            while( openings[direction]==-1||taken==direction){
                direction=(int)Math.round((Math.random()*(3)));
            }

            //-1 means there's a room in this direction
            openings[direction]=-1;


        }
        //initialize room
        Room r = new Room(door, index,openings);
        return r;
    }

    //unfinished minimap method that is not used anywhere else
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

    //cindy's old circle methods

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



