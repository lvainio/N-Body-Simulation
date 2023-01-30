import java.util.Random;

/**
 * Sequential implementation of Barnes-Hut simulation.
 * 
 * Usage:
 *      compile:
 *          - javac *.java
 * 
 *      run:
 *          - java NBodySimulation [default settings]
 *          - java NBodySimulation <num_bodies> <num_steps> 
 *          - java NBodySimulation <num_bodies> <num_steps -g -r
 * 
 * The flags -g -r can be specified after the first two arguments.
 * 
 * -g: the simulation will be shown in a gui.
 * -r: the bodies will be generated in a ring formation around a central body with greater mass. 
 *      
 * @author: Leo Vainio
 */

public class NBodySimulation {
    private static final int MAX_NUM_BODIES = 1000;
    private static final int MAX_NUM_STEPS = 100_000_000;
    private static Settings settings;

    private Random rng;
    private Timer timer;

    /*
     * Parse command line arguments and adjust the settings for the simulation.
     */
    public static void main(String[] args) {
        int numBodies = MAX_NUM_BODIES;
        int numSteps = MAX_NUM_STEPS;
        boolean guiToggled = false;
        boolean ringToggled = false;

        try {
            if (args.length >= 1) {
                numBodies = Integer.parseInt(args[0]);
                numBodies = (numBodies > MAX_NUM_BODIES || numBodies < 1) ? MAX_NUM_BODIES : numBodies;
            }
            if (args.length >= 2) {
                numSteps = Integer.parseInt(args[1]);
                numSteps = (numSteps > MAX_NUM_STEPS || numSteps < 1) ? MAX_NUM_STEPS : numSteps;
            }
            if (args.length >= 3)
                if (args[2].equals("-g")) 
                    guiToggled = true;
            if (args.length >= 4) 
                if (args[3].equals("-r")) 
                    guiToggled = true;

        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            System.exit(1);
        }

        final double DT = 1.0;
        final double G = 6.67e-11;
        final double radius = 500_000.0;
        final double mass = 100.0;

        settings = new Settings(numBodies, numSteps, guiToggled, ringToggled, DT, G, radius, mass);

        System.out.println("\n> Simulating the gravitational n-body problem with the following settings:");
        System.out.println("\t- " + settings);

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
        if (!settings.ringToggled()) {
            bodies = generateBodies();
        } else {
            bodies = generateBodiesDonut();
        }

        // run simulation.
        timer = new Timer();
        timer.start();

        // TODO: simulation.

        timer.stopAndPrint();
    }

    /*
     * Generate bodies randomly within set boundaries (settings.radius()).
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
     * Generate bodies in a ring-like formation with a massive attracting body in the center. 
     */
    private Body[] generateBodiesDonut() {
        Body[] bodies = new Body[settings.numBodies()];

        // Create the massive body in the center.
        double centerX = settings.radius();
        double centerY = settings.radius();
        double centerVx = 0.0;
        double centerVy = 0.0;
        double centerMass = 1e18;
        bodies[0] = new Body(centerX, centerY, centerVx, centerVy, centerMass);

        // Generate the ring of bodies.
        for (int i = 1; i < bodies.length; i++) {
            Vector unit = getRandomUnitVector();

            double magnitude = (settings.radius() * 0.6) + (settings.radius() * 0.8 - settings.radius() * 0.6) * rng.nextDouble();
            double x = unit.getX() * magnitude + settings.radius();
            double y = unit.getY() * magnitude + settings.radius();

            Vector velocity = getOrthogonalVector(unit);
            double vx = velocity.getX() * 15.0;
            double vy = velocity.getY() * 15.0;
            
            bodies[i] = new Body(x, y, vx, vy, settings.mass());
        }
        return bodies;
    }

    /*
     * Returns a randomized unit vector.
     */
    private Vector getRandomUnitVector() {
        double radians = rng.nextDouble() * 2 * Math.PI;
        return new Vector(Math.cos(radians), Math.sin(radians));
    }

    /*
     * Returns a vector that is orthogonal to the input vector.
     */
    private Vector getOrthogonalVector(Vector v) {
        return new Vector(v.getY(), -v.getX());
    }
}
