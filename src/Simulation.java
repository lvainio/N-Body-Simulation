import java.util.Random;

/**
 * Sequential implementation of the n-body problem.
 * 
 * Usage:
 *      compile: javac *.java
 *      execute: java Simulation <num_bodies> <num_steps> 
 *      graphic: java Simulation <num_bodies> <num_steps> -g
 *      donut: java Simulation <num_bodies> <num_steps> -g -d
 * 
 * @author: Leo Vainio
 */

public class Simulation {
    // Command line args (initialized with default values).
    private static int numBodies = 100;
    private static int numSteps = 100_000;
    private static boolean guiToggled = false;
    private static boolean donutToggled = false;

    // Constants.
    static final double RADIUS = 500_000.0;
    private final double MASS = 100.0;
    private final double G = 6.67e-4;
    //private final double G = 6.67e-11;
    private final double DT = 1;

    private GUI gui;
    private Timer timer;
    
    private Random rng;
    private Body[] bodies;

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

        new Simulation();
    }

     /*
     * Constructor for sequential.
     */
    public Simulation() {
        rng = new Random();
        rng.setSeed(System.nanoTime());

        // generate bodies.
        bodies = new Body[numBodies];
        if (!donutToggled) {
            generateBodies();
        } else {
            generateBodiesDonut();
        }

        // init gui.
        if (guiToggled) {
            gui = new GUI("N-body problem: sequential", bodies);
        }

        // time
        timer = new Timer();
        timer.start();
      
        // start simulation.
        simulate();

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
        bodies[0] = new Body(RADIUS, RADIUS, 0.0, 0.0, 100000000000.0);
        for (int i = 1; i < bodies.length; i++) {
            Vector vec = getRandomUnitVector();

            double r = (RADIUS * 0.5) + (RADIUS * 0.75 - RADIUS * 0.5) * rng.nextDouble();
            double x = vec.x * r + RADIUS;
            double y = vec.y * r + RADIUS;

            Vector vel = getOrthogonalVector(vec);
            double vx = vel.x * 10.0;
            double vy = vel.y * 10.0;
            
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
        return new Vector(vec.y, -vec.x);
    }

    /*
     * Run the simulation for specified number of steps.
     */
    private void simulate() {
        for (int i = 0; i < numSteps; i++) {
            if (guiToggled) {
                gui.repaint();
            }
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
                Body b1 = bodies[i];
                Body b2 = bodies[j];

                distance = Math.sqrt(Math.pow(b1.getX()-b2.getX(), 2) + Math.pow(b1.getY()-b2.getY(), 2));
                magnitude = (G * b1.getMass() * b2.getMass()) / (distance * distance);
                dirX = b2.getX() - b1.getX();
                dirY = b2.getY() - b1.getY();

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
            Body b = bodies[i];

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
