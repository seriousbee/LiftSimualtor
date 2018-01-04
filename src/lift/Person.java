
package lift;

/**
 * Simulates a person using a lift.
 * @author K. Bryson
 */
public class Person extends Thread {

    private final int person_id;
    private final LiftController controller;

    public Person(int person_id, LiftController controller) {
        this.person_id = person_id;
        this.controller = controller;
    }

    public void run() {

        System.out.println("Started Person " + person_id);

        try {
            while (true) {

                // Take 5 seconds to run to random floor!
                sleep(5000);
                int from_floor = (int) (java.lang.Math.random() * Main.NUMBER_FLOORS);
                //int from_floor = 3;
                // Allow person to go to the same floor ...
                // people sometimes mess about ... and good boundary case!
                int to_floor = (int) (java.lang.Math.random() * Main.NUMBER_FLOORS);
                //int to_floor = 6;

                // Ok ... let's do the journey ... and hope the lift doesn't get stuck!

                System.out.println("Person " + person_id + " wants to go from floor " +
                        from_floor + " to floor " + to_floor);

                // Define the direction - note that people will select 'DOWN' if
                // they are just messing about (going to the same floor) except
                // if they are actually on floor 0 ... since only 'UP' button then.
                if (to_floor - from_floor > 0 || from_floor == 0) {
                    System.out.println("Person " + person_id + " pressing UP button.");
                    controller.pushUpButton(from_floor);
                } else {
                    System.out.println("Person " + person_id + " pressing DOWN button.");
                    controller.pushDownButton(from_floor);                	
                }
                

                System.out.println("Person " + person_id + " has entered lift.");
                System.out.println("Person " + person_id + " selecting floor " + to_floor);

                controller.selectFloor(to_floor);

                System.out.println("Person " + person_id + " getting out of lift.");

            }
        } catch (InterruptedException e) {
            System.out.println("Person " + person_id + " was interrupted.");
        }
    }
}
