package com.ulluna;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyLiftController implements LiftController {

    //instance data used to pass the information on the current state of the Lift to Persons
    private int currentFloor = 0;
    private Direction currentDirection;

    //list of Persons' Requests for the Lift to stop. Class Request is implemented at the bottom of the file
    private List<Request> requests = Collections.synchronizedList(new ArrayList<Request>());


    /*
    Push UP/DOWN and selectFloor are very similar in which they add a new Request to requests and wait for the Lift to stop at the correct floor in the right direction
    Once the Lift has reached the desired destination, they remove their Request from requests and notify the Lift that they have entered / left
     */

    public synchronized void pushUpButton(int floor) throws InterruptedException {
        Request myRequest3 = new Request(floor, Direction.UP);
        requests.add(myRequest3);
        while(true){
            wait();
            if (currentFloor == floor && currentDirection == Direction.UP) {
                break;
            }
        }
        requests.remove(myRequest3);
        notifyAll();
    }

    public synchronized void pushDownButton(int floor) throws InterruptedException {
        Request myRequest3 = new Request(floor, Direction.DOWN);
        requests.add(myRequest3);
        while(true){
            wait();
            if (currentFloor == floor && currentDirection == Direction.DOWN) {
                break;
            }
        }
        requests.remove(myRequest3);
        notifyAll();
    }

    public synchronized void selectFloor(int floor) throws InterruptedException {
        Request myRequest3 = new Request(floor, Direction.UNSET);
        requests.add(myRequest3);
        while(true){
            wait();
            if (currentFloor == floor)
                break;
        }
        requests.remove(myRequest3);
        notifyAll();
    }

    //liftAtFloor simply checks whether there is a Request for the Lift given the current floor and direction. If so, it informs the Persons about the current direction
    public synchronized boolean liftAtFloor(int floor, Direction direction) {
        if(containsFloor(floor, direction)){
            currentDirection = direction;
            return true;
        }
        return false;
    }

    //doorsOpen notifies all Persons that they may now enter the lift at the floor and waits for all requests for this floor to disappear
    public synchronized void doorsOpen(int floor) throws InterruptedException {
        currentFloor = floor;
        while(true){
            notifyAll();
            wait();
            if (!containsFloor(floor, Direction.UNSET))
                break;
        }
    }

    //this method could easily stay empty but for cleanness sake, I'm resetting the info sent to Persons
    public synchronized void doorsClosed(int floor) {
        currentFloor = -1;
        currentDirection = null;
    }

    //checks if any Person requested the lift to stop given the floor number and Direction
    private boolean containsFloor(int floor, Direction direction){
        for (Request request : requests) {
            if (request.floor == floor)
                if (request.direction.equals(direction) || request.direction.equals(Direction.UNSET))
                    return true;
        }
        return false;
    }

    //a Tuple of desired floor and Direction. These objects are stored in requests
    private class Request {
        int floor;
        Direction direction;

        Request(int floor, Direction direction) {
            this.floor = floor;
            this.direction = direction;
        }
    }
}
