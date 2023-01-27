import java.util.Random;

public class Data {
    private final double MASS = 5.9722e24;
    static final double DIAMETER = 1e15;
    
    private Body[] bodies;

    /*
     * Constructor for data.
     */
    public Data(int numBodies) {
        bodies = new Body[numBodies];
        generateBodies(numBodies);
    }

    /*
     * Generate bodies with randomation. 
     */
    private void generateBodies(int numBodies) {
        Random rng = new Random();
        rng.setSeed(System.nanoTime());

        for (int i = 0; i < bodies.length; i++) {
            double x = rng.nextDouble() * 1_000_000_000;
            double y = rng.nextDouble() * 1_000_000_000; 

            // TODO: randomize
            double vx = rng.nextDouble() * 0.0; 
            double vy = rng.nextDouble() * 0.0; 
            double fx = rng.nextDouble() * 0.0; 
            double fy = rng.nextDouble() * 0.0;
            double mass = MASS;

            bodies[i] = new Body(x, y, vx, vy, fx, fy, mass);
        }
    }

    /*
     * Returns the body at specified index.
     */
    public Body get(int index) {
        return bodies[index];
    }

    /*
     * Returns the number of bodies.
     */
    public int getNumBodies() {
        return bodies.length;
    }

    public void printBodies() {
        for (Body body : bodies) {
            System.out.println("x: " + body.getX() + ", y: " + body.getY() + "\n");
        }
    }
}
