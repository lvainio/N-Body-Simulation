import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * This class computes the forces of the bodies and moves the bodies. 
 * Each thread is assigned a subset of the calculations.
 * 
 * @author Leo Vainio
 */

public class Worker extends Thread {    
    private int id;
    private Body[] bodies;
    private CyclicBarrier barrier;    
    private GUI gui;
    private QuadTree quadTree; 
    private Settings settings;

    Timer t = new Timer();
    double time = 0.0;

    public Worker(int id, Body[] bodies, CyclicBarrier barrier, QuadTree quadTree, Settings settings) {
        this.id = id;
        this.bodies = bodies;
        this.barrier = barrier;
        this.quadTree = quadTree;
        this.settings = settings;
        if (id == 0 && settings.guiToggled()) {
            gui = new GUI(bodies, settings);
        } 
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < settings.numSteps(); i++) {
                barrier.await();

                // maybe insert can be parallelized by having one thread inserting all the bodies into level one
                // then grabbing nw, ne, sw, se and then inserting the bodies in their corresponding quadrant
                // recursively. 

                // is the below insertions dependent on the insertion in above? i dont think so.

                // then we could have a mutex for each of the four quadrants and a counter variable
                // so we go through all bodies and increment counter so each thread works on one body
                // at a time.

                // we would need 4 mutexes + 1 counter mutex. we would need getters for each quadrant. 

                // we need to make sure of correctness.

                if (id == 0) {
                    if (settings.guiToggled()) {
                        gui.repaint();
                    }
                    Quadrant quadrant = getBoundaries();
                    quadTree.reset(quadrant);
                    quadTree.insertBodies(bodies);
                }
                barrier.await();
                computeForces();
                moveBodies();
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            System.exit(1);
        } catch (BrokenBarrierException bbe) {
            bbe.printStackTrace();
            System.exit(1);
        } 
    }

    /*
     * Returns a square quadrant that covers all bodies of the simulation.
     */
    private Quadrant getBoundaries() {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (Body body : bodies) {
            if (body.getX() < min) min = body.getX();
            if (body.getX() > max) max = body.getX();
            if (body.getY() < min) min = body.getY();
            if (body.getY() > max) max = body.getY();
        }
        double x = (min + max) / 2;
        double y = (min + max) / 2;
        double radius = ((max - min) / 2) + 100.0; // + 100.0 to make sure bodies on the edge are contained.
        return new Quadrant(x, y, radius);
    }

    /*
     * Build quadtree and compute total force exerted on each body. 
     */
    private void computeForces() {
        for (int i = id; i < settings.numBodies(); i += settings.numWorkers()) {
            quadTree.calculateForce(bodies[i]);
        }
    }

     /*
     * Move all the bodies depending on the force exerted on each body.
     */
    private void moveBodies() {
        for (int i = id; i < settings.numBodies(); i += settings.numWorkers()) {
            bodies[i].move();
        }
    }
}
