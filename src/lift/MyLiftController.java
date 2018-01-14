package lift;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Null implementation of a Lift Controller.
 * @author K. Bryson
 */
public class MyLiftController implements LiftController, LiftWakerCallback {

    class Request{
        Request(int floor, Direction direction) {
            this.floor = floor;
            this.direction = direction;
        }
        int floor;
        Direction direction;
    }

    class LiftWaker extends Thread {

        LiftWakerCallback callback;
        LiftWaker(LiftWakerCallback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            callback.liftWaitedEnough();
        }
    }

    private boolean flag = false;
    private int currentFloor = 0;
    private Direction currentDirection;
    private List<Request> requests3 = Collections.synchronizedList(new ArrayList<Request>());

    /* Interface for People */
    public void pushUpButton(int floor) {
        Request myRequest3 = new Request(floor, Direction.UP);
        requests3.add(myRequest3);
        while(true){
            synchronized (this){
                try {
                    this.wait();
                    if(currentFloor == floor && currentDirection == Direction.UP){
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        requests3.remove(myRequest3);
    }

    public void pushDownButton(int floor) {
        Request myRequest3 = new Request(floor, Direction.DOWN);
        requests3.add(myRequest3);
        while(true){
            synchronized (this){
                try {
                    this.wait();
                    if(currentFloor == floor && currentDirection == Direction.DOWN){
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        requests3.remove(myRequest3);
    }
    
    public void selectFloor(int floor) {
        Request myRequest3 = new Request(floor, Direction.UNSET);
        requests3.add(myRequest3);
        while(true){
            synchronized (this){
                try {
                    this.wait();
                    if(currentFloor == floor)
                        break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        requests3.remove(myRequest3);
    }

    /* Interface for Lifts */
    public synchronized boolean liftAtFloor(int floor, Direction direction) {
        if(containsFloor(floor, direction)){
            currentDirection = direction;
            return true;
        }
        return false;
    }

    public synchronized void doorsOpen(int floor) {
        currentFloor = floor;
        while(true){
            synchronized (this){
                try {
                    this.notifyAll();
                    LiftWaker waker = new LiftWaker(this);
                    waker.start();
                    this.wait();
                    if(flag){
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized void doorsClosed(int floor) {
        currentFloor = -1;
        currentDirection = null;
    }

    private boolean containsFloor(int floor, Direction direction){
        for (Request request : requests3) {
            if(request != null && request.floor==floor && (request.direction.equals(direction) || request.direction.equals(Direction.UNSET)))
                return true;
        }
        return false;
    }

    @Override
    public synchronized void liftWaitedEnough() {
        flag = true;
        notifyAll();
    }

}

interface LiftWakerCallback {
    void liftWaitedEnough();
}
