import java.util.Random;

// semi successful inputs parameters:
//  - M=1, D=100 000, DT=1, v=rng*1-0.5
//  - M=100, D=1 000 000, DT=1, v=rng*5-2-5
//  - M=1000, -||-
//  - M=1000, D= 1 000 000, DT=1, v=rng*25-12.5 (this one was nice)

public class Data {
    Random rng;

    private final double MASS = 100.0;
    static final double DIAMETER = 1_000_000.0;
    static final double RADIUS = 500_000.0;
    
    private Body[] bodies;

    /*
     * Constructor for data.
     */
    public Data(int numBodies) {
        bodies = new Body[numBodies];
        rng = new Random();
        generateBodies(numBodies);
    }

    // private void generateBodies(int numBodies) {
    //     Random rng = new Random();
    //     rng.setSeed(System.nanoTime());

    //     for (int i = 0; i < bodies.length; i++) {
    //         double x = rng.nextDouble() * DIAMETER;
    //         double y = rng.nextDouble() * DIAMETER;
    //         double vx = rng.nextDouble() * 25 - 12.5; 
    //         double vy = rng.nextDouble() * 25 - 12.5; 
    //         double mass = MASS;
    //         bodies[i] = new Body(x, y, vx, vy, mass);
    //     }
    // }

    /*
     * Generate bodies with randomation. 
     */
    private void generateBodies(int numBodies) {
        rng.setSeed(System.nanoTime());

        bodies[0] = new Body(RADIUS, RADIUS, 0.0, 0.0, 100000000000.0);

        for (int i = 1; i < bodies.length; i++) {
            Vector vec = getRandomUnitVector();

            double m = (RADIUS * 0.5) + (RADIUS * 0.75 - RADIUS * 0.5) * rng.nextDouble();
            double x = vec.x * m + RADIUS;
            double y = vec.y * m + RADIUS;

            Vector vel = getOrthogonalVector(vec); // multiply with something to get more speed.
            double vx = vel.x * 10.0;
            double vy = vel.y * 10.0;
            
            double mass = MASS;
            bodies[i] = new Body(x, y, vx, vy, mass);
        }
    }

    /*
     * 
     */
    private Vector getRandomUnitVector() {
        double angle = rng.nextDouble() * 2 * Math.PI;
        return new Vector(Math.cos(angle), Math.sin(angle));
    }

    private Vector getOrthogonalVector(Vector vec) {
        return new Vector(vec.y, -vec.x);
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

    /*
     * Simple 2D vector class
     */
    class Vector {
        double x;
        double y;
        public Vector(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
