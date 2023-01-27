import java.util.Random;

// semi successful inputs parameters:
//  - M=1, D=100 000, DT=1, v=rng*1-0.5
//  - M=100, D=1 000 000, DT=1, v=rng*5-2-5
//  - M=1000, -||-
//  - M=1000, D= 1 000 000, DT=1, v=rng*25-12.5 (this one was nice)

public class Data {
    private final double MASS = 1000;
    static final double DIAMETER = 1_000_000;
    
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
            double x = rng.nextDouble() * DIAMETER;
            double y = rng.nextDouble() * DIAMETER;
            double vx = rng.nextDouble() * 25 - 12.5; 
            double vy = rng.nextDouble() * 25 - 12.5; 
            double mass = MASS;
            bodies[i] = new Body(x, y, vx, vy, mass);
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
}
