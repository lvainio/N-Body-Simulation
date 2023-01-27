
/*
 * Sequential implementation of the n-body problem.
 * 
 * Usage:
 *      compile: javac *.java
 *      execute: java Sequential <num_bodies> <num_steps> 
 *      graphic: java Sequential <num_bodies> <num_steps> -gui
 */

public class Sequential {
    // TODO: final max variables? I.e. max bodies, max steps etc.

    private static int numBodies = 100;
    private static int numSteps = 100_000;
    private static boolean guiToggled = false;

    private final double G = 6.67e-11;
    private final double DT = 100_000;

    private GUI gui;
    private Timer timer;

    private Data bodies;

    /*
     * Main.
     */
    public static void main(String[] args) {
        // Command line args
        try {
            if (args.length >= 1) {
                numBodies = Integer.parseInt(args[0]);
            }
            if (args.length >= 2) {
                numSteps = Integer.parseInt(args[1]);
            }
            if (args.length >= 3) {
                if (args[2].equals("-gui")) {
                    guiToggled = true;
                }
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            System.exit(1);
        }

        System.out.println("\n> Simulating the gravitational n-body problem with:");
        System.out.println("\t - " + numBodies + " bodies");
        System.out.println("\t - " + numSteps + " steps\n");

        new Sequential();
    }

     /*
     * Constructor for sequential.
     */
    public Sequential() {
        // generate bodies.
        bodies = new Data(numBodies);

        // init graphical user interface.
        if (guiToggled) {
            gui = new GUI("N-body problem: sequential", bodies);
        }
      
        // start simulation.
        simulate();
    }

    /*
     * Simulate runs the simulation for specified number of steps.
     */
    private void simulate() {
        for (int i = 0; i < numSteps; i++) {



            bodies.printBodies();

            if (guiToggled) {
                gui.repaint();
            }

            // try {
            //     Thread.sleep(1000);
            // } catch (InterruptedException e) {

            // }

            calculateForces();
            moveBodies(); 
        }
    }

    /*
     * Calculates total force for every pair of bodies.
     */
    private void calculateForces() {
        double distance;
        double magnitude;
        double dirX;
        double dirY;

        for (int i = 0; i < numBodies - 1; i++) {
            for (int j = i + 1; j < numBodies; j++) {
                Body b1 = bodies.get(i);
                Body b2 = bodies.get(j);

                distance = Math.sqrt(Math.pow(b1.getX()-b2.getX(), 2) + Math.pow(b1.getY()-b2.getY(), 2));
                magnitude = (G * b1.getMass() * b2.getMass()) / (distance * distance);
                dirX = b2.getX() - b1.getX();
                dirY = b2.getY() - b2.getY();

                b1.setFx(b1.getFx() + magnitude * dirX / distance);
                b2.setFx(b2.getFx() - magnitude * dirX / distance);
                b1.setFy(b1.getFy() + magnitude * dirY / distance);
                b2.setFy(b2.getFy() - magnitude * dirY / distance);
            }
        }
    }        

    /*
     * Calculates new velocity and position for each body.
     */
    private void moveBodies() {
        for (int i = 0; i < numBodies; i++) {
            Body b = bodies.get(i);

            double dVx = (b.getFx() / b.getMass()) * DT;
            double dVy = (b.getFy() / b.getMass()) * DT;
            double dPx = (b.getVx() + dVx / 2.0) * DT;
            double dPy = (b.getVy() + dVy / 2.0) * DT;

            b.setVx(b.getVx() + dVx);
            b.setVy(b.getVy() + dVy);
            b.setX(b.getX() + dPx);
            b.setY(b.getY() + dPy);
            b.setFx(0.0);
            b.setFy(0.0);
        }
    }
}
