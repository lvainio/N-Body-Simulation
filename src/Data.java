import java.util.Random;

public class Data {
    // private final double EARTH_MASS = 5.97e24;
    private final double EARTH_MASS = 1.0;

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
            double x = rng.nextDouble() * 1000.0; // TODO: increase ALOT
            double y = rng.nextDouble() * 1000.0; // TODO: increase ALOT
            double vx = rng.nextDouble() * 1.0; // TODO: increase ALOT
            double vy = rng.nextDouble() * 1.0; // TODO: increase ALOT
            double fx = rng.nextDouble() * 1.0; // TODO: increase ALOT
            double fy = rng.nextDouble() * 1.0; // TODO: increase ALOT
            double mass = EARTH_MASS;

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
