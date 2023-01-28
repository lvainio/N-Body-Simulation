import java.util.Random;

/**
 * Sequential implementation of the n-body problem.
 * 
 * Usage:
 *      compile: javac *.java
 *      execute: java NbodySimulation <num_bodies> <num_steps> 
 *      graphic: java NBodySimulation <num_bodies> <num_steps> -g
 *      donut: java NBodySimulation <num_bodies> <num_steps> -g -d
 * 
 * @author: Leo Vainio
 */

public class NBodySimulation {
    // Command line args (initialized with default values).
    private static int numBodies = 100;
    private static int numSteps = 100_000;
    private static boolean guiToggled = false;
    private static boolean donutToggled = false;

    // Constants.
    static final double RADIUS = 500_000.0;
    private final double MASS = 100.0;

    private Random rng;
    private Timer timer;

    private Body[] bodies;

    private int numWorkers = 20;

    /*
     * Read command line arguments and start simulation.
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
                if (args[2].equals("-g")) { // graphics
                    guiToggled = true;
                }
            }
            if (args.length >= 4) {
                if (args[3].equals("-d")) { // donut
                    donutToggled = true;
                }
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            System.exit(1);
        }

        System.out.println("\n> Simulating the gravitational n-body problem with:");
        System.out.println("\t - " + numBodies + " bodies");
        System.out.println("\t - " + numSteps + " steps\n");

        new NBodySimulation();
    }

     /*
     * Generate bodies, run simulation and time it.
     */
    public NBodySimulation() {
        rng = new Random();
        rng.setSeed(System.nanoTime());

        // generate bodies.
        bodies = new Body[numBodies];
        if (!donutToggled) {
            generateBodies();
        } else {
            generateBodiesDonut();
        }

        // run simulation.
        timer = new Timer();
        timer.start();
        // Create threads.
        Worker[] workers = new Worker[numWorkers]; 
        for (int id = 0; id < numWorkers; id++) {
            workers[id] = new Worker(bodies, id, numSteps, guiToggled, donutToggled);
            workers[id].start();
        }
        // Join threads.
        for (int id = 0; id < numWorkers; id++) {
            try {
                workers[id].join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                System.exit(1);
            }
        }
        timer.stopAndPrint();
    }

    /*
     * Generate bodies randomly within set diameter.
     */
    private void generateBodies() {
        for (int i = 0; i < bodies.length; i++) {
            double x = rng.nextDouble() * (RADIUS * 2);
            double y = rng.nextDouble() * (RADIUS * 2);
            double vx = rng.nextDouble() * 25 - 12.5; 
            double vy = rng.nextDouble() * 25 - 12.5; 
            double mass = MASS;
            bodies[i] = new Body(x, y, vx, vy, mass);
        }
    }

    /*
     * Generate bodies in a donut formation with a huge attracting body in the middle. 
     */
    private void generateBodiesDonut() {
        bodies[0] = new Body(RADIUS, RADIUS, 0.0, 0.0, 100_000_000_000.0);
        for (int i = 1; i < bodies.length; i++) {
            Vector unit = getRandomUnitVector();

            double r = (RADIUS * 0.6) + (RADIUS * 0.8 - RADIUS * 0.6) * rng.nextDouble();
            double x = unit.getX() * r + RADIUS;
            double y = unit.getY() * r + RADIUS;

            Vector vel = getOrthogonalVector(unit);
            double vx = vel.getX() * 10.0;
            double vy = vel.getY() * 10.0;
            
            double mass = MASS;
            bodies[i] = new Body(x, y, vx, vy, mass);
        }
    }

    /*
     * Returns a randomized unitvector.
     */
    private Vector getRandomUnitVector() {
        double angle = rng.nextDouble() * 2 * Math.PI;
        return new Vector(Math.cos(angle), Math.sin(angle));
    }

    /*
     * Returns a vector that is orthogonal to the input vector.
     */
    private Vector getOrthogonalVector(Vector vec) {
        return new Vector(vec.getY(), -vec.getX());
    }
}
