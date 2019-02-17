package sample;
import java.util.ArrayList;
public class Room {
    /**
     * "opening"stores the index of Rooms around it
     * in the array, 0 is north, 1 is east, 2 is south, 3 is west
     * if there is no room connected by a door, the array value is -1,
     * else the value is the index of the room
     */

     int[] opening= new int[4];

    //doors is the number of rooms this room is connected to
    //index is the sequence in which this room is instantiated
    int doors,index;

    //initialization method
    public Room(int door,int index_,int[] openings){
        doors=door;
        opening=openings;
        index=index_;
    }

}
