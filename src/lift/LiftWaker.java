package lift;

/**
 * Created by tomaszczernuszenko on 04/01/2018.
 */
public class LiftWaker extends Thread {

    LiftWakerCallback callback;

    public LiftWaker(LiftWakerCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        callback.liftWaitedEnough();
    }
}
