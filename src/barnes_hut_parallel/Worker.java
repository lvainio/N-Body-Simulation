import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * worker 0 is special
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
                if (id == 0) {
                    if (settings.guiToggled()) {
                        gui.repaint();
                    }
                    Quadrant quadrant = new Quadrant(settings.universeRadius(), settings.universeRadius(), settings.approximationDistance());
                    quadTree.reset(quadrant);
                    quadTree.insertBodies(bodies);

                    // TODO: inserting can be parallelised if we consider that bodies in 
                    // different quadrants can be inserted independently.
                    // so if thread 0 insert every body in quadrant 1
                    // thread 1 insert in quadrant 2 and so on.

                    // how to do this with multiple threads tho?
                    // maybe insert one body first
                    // then we surely have the 4 quadrants available
                    // somehoe split the work among the threads in a good way.
                    // easy when we have 1, 2, 4, 8, threads and so on but a
                    // bit harder with 3
                    // maybe in the case of 3 threads we can just skip the third one

                    // One issue with this is that every body needs to be compared to the
                    // groupbody of the first HUGE quadrant and actually the first body inserted
                    // will also need to move down a step after a while.

                }
                barrier.await();
                computeForces();
                barrier.await();
                moveBodies();
                barrier.await();
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
     * Build quadtree and compute total force exerted on each body. 
     */
    private void computeForces() {
        for (int i = id; i < settings.numBodies(); i += settings.numWorkers()) {
            quadTree.computeForce(bodies[i]);
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
