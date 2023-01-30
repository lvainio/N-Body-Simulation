import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 * Parallel implementation of the n-body problem.
 * 
 * Usage:
 *      compile: javac *.java
 *      execute: java NbodySimulation <num_bodies> <num_steps> <num_workers>
 *      graphic: java NBodySimulation <num_bodies> <num_steps> <num_workers> -g
 *      donut: java NBodySimulation <num_bodies> <num_steps> <num_workers> -g -d
 * 
 * @author: Leo Vainio
 */

public class NBodySimulation {
    private static final int MAX_NUM_BODIES = 400;
    private static final int MAX_NUM_STEPS = 10_000_000;
    private static final int MAX_NUM_WORKERS = 16;
    
    private static Settings settings;

    private Random rng;
    private Timer timer;

    /*
     * Read command line arguments and toggle the settings for the simulation.
     */
    public static void main(String[] args) {
        int numBodies = MAX_NUM_BODIES;
        int numSteps = MAX_NUM_STEPS;
        int numWorkers = MAX_NUM_WORKERS;
        boolean guiToggled = false;
        boolean donutToggled = false;

        try {
            if (args.length >= 1) {
                numBodies = Integer.parseInt(args[0]);
                numBodies = (numBodies > MAX_NUM_BODIES || numBodies < 1) ? MAX_NUM_BODIES : numBodies;
            }
            if (args.length >= 2) {
                numSteps = Integer.parseInt(args[1]);
                numSteps = (numSteps > MAX_NUM_STEPS || numSteps < 1) ? MAX_NUM_STEPS : numSteps;
            }
            if (args.length >= 3) {
                numWorkers = Integer.parseInt(args[2]);
                numWorkers = (numWorkers > MAX_NUM_WORKERS || numWorkers < 1) ? MAX_NUM_WORKERS : numWorkers;
            }
            if (args.length >= 4) {
                if (args[3].equals("-g")) { // graphics
                    guiToggled = true;
                }
            }
            if (args.length >= 5) {
                if (args[4].equals("-d")) { // donut
                    donutToggled = true;
                }
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            System.exit(1);
        }

        final double DT = 1.0;
        final double G = 6.67e-11;
        final double radius = 500_000.0;
        final double mass = 100.0;

        settings = new Settings(numBodies, numSteps, numWorkers, guiToggled, donutToggled, DT, G, radius, mass);

        System.out.println("\n> Simulating the gravitational n-body problem with:");
        System.out.println("\t - " + settings.numBodies() + " bodies");
        System.out.println("\t - " + settings.numSteps() + " steps");
        System.out.println("\t - " + settings.numWorkers() + " workers\n");

        new NBodySimulation();
    }

     /*
     * Generate bodies, run simulation and time it.
     */
    public NBodySimulation() {
        rng = new Random();
        rng.setSeed(System.nanoTime());

        // generate bodies.
        Body[] bodies;
        if (!settings.donutToggled()) {
            bodies = generateBodies();
        } else {
            bodies = generateBodiesDonut();
        }

        // run simulation.
        timer = new Timer();
        timer.start();

        Worker[] workers = new Worker[settings.numWorkers()]; 
        CyclicBarrier barrier = new CyclicBarrier(settings.numWorkers());
        Vector[][] forces = new Vector[settings.numWorkers()][settings.numBodies()];
        for (int i = 0; i < settings.numWorkers(); i++) {
            for (int j = 0; j < settings.numBodies(); j++) {
                forces[i][j] = new Vector(0.0, 0.0);
            }
        }
    
        // Create threads.
        for (int id = 0; id < settings.numWorkers(); id++) {
            workers[id] = new Worker(id, bodies, barrier, forces, settings);
            workers[id].start();
        }
        // Join threads.
        for (int id = 0; id < settings.numWorkers(); id++) {
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
    private Body[] generateBodies() {
        Body[] bodies = new Body[settings.numBodies()];
        for (int i = 0; i < bodies.length; i++) {
            double x = rng.nextDouble() * (settings.radius() * 2);
            double y = rng.nextDouble() * (settings.radius() * 2);
            double vx = rng.nextDouble() * 25 - 12.5; 
            double vy = rng.nextDouble() * 25 - 12.5; 
            double mass = settings.mass();
            bodies[i] = new Body(x, y, vx, vy, mass);
        }
        return bodies;
    }

    /*
     * Generate bodies in a donut formation with a huge attracting body in the middle. 
     */
    private Body[] generateBodiesDonut() {
        Body[] bodies = new Body[settings.numBodies()];
        bodies[0] = new Body(settings.radius(), settings.radius(), 0.0, 0.0, 100_000_000_000_000_000_0.0);
        for (int i = 1; i < bodies.length; i++) {
            Vector unit = getRandomUnitVector();

            double r = (settings.radius() * 0.6) + (settings.radius() * 0.8 - settings.radius() * 0.6) * rng.nextDouble();
            double x = unit.getX() * r + settings.radius();
            double y = unit.getY() * r + settings.radius();

            Vector vel = getOrthogonalVector(unit);
            double vx = vel.getX() * 15.0;
            double vy = vel.getY() * 15.0;
            
            double mass = settings.mass();
            bodies[i] = new Body(x, y, vx, vy, mass);
        }
        return bodies;
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
