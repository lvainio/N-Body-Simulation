import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 
 */

public class Worker extends Thread {
    // Constants.
    private final int NUM_STEPS;
    private final double G = 6.67e-4;
    private final double DT = 1;
    
    private int numWorkers;
    private int id;

    private Body[] bodies;
    private Vector[][] forces;

    private GUI gui;
    private boolean guiToggled;

    private CyclicBarrier barrier;

    /*
     * 
     */
    public Worker(Body[] bodies, int id, int numWorkers, int numSteps, CyclicBarrier barrier, boolean guiToggled, boolean donutToggled) {
        this.bodies = bodies;
        this.numWorkers = numWorkers;
        this.id = id;
        this.guiToggled = guiToggled;
        this.barrier = barrier;
        NUM_STEPS = numSteps;

        forces = new Vector[numWorkers][bodies.length];
        for (int i = 0; i < numWorkers; i++) {
            for (int j = 0; j < bodies.length; j++) {
                forces[i][j] = new Vector(0.0, 0.0);
            }
        }

        if (id == 0 && guiToggled) {
            gui = new GUI("N-body problem: parallel", bodies, donutToggled);
        }
    }

    /*
     * Runs the simulation for a set number of steps.
     */
    @Override
    public void run() {
        try {
            for (int i = 0; i < NUM_STEPS; i++) {
                calculateForces();
                barrier.await();
                moveBodies();
                barrier.await();
                if (id == 0 && guiToggled) {
                    gui.repaint();
                }
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
     * Calculates total force for every pair of bodies.
     */
    private void calculateForces() {
        double distance;
        double magnitude;
        double dirX;
        double dirY;

        for (int i = id; i < bodies.length; i += numWorkers) {
            for (int j = i + 1; j < bodies.length; j++) {
                Body b1 = bodies[i];
                Body b2 = bodies[j];

                distance = Math.sqrt(Math.pow(b1.getX()-b2.getX(), 2) + Math.pow(b1.getY()-b2.getY(), 2));
                magnitude = (G * b1.getMass() * b2.getMass()) / (distance * distance);
                dirX = b2.getX() - b1.getX();
                dirY = b2.getY() - b1.getY();

                forces[id][i].setX(forces[id][i].getX() + magnitude * dirX / distance);
                forces[id][j].setX(forces[id][j].getX() - magnitude * dirX / distance);
                forces[id][i].setY(forces[id][i].getY() + magnitude * dirY / distance);
                forces[id][j].setY(forces[id][j].getY() - magnitude * dirY / distance);
            }
        }
    }  

    /*
     * Calculates new velocity and position for each body.
     */
    private void moveBodies() { 
        Vector force = new Vector(0.0, 0.0);
        for (int i = id; i < bodies.length; i += numWorkers) {
            for (int j = 0; j < numWorkers; j++) {
                force.setX(force.getX() + forces[j][i].getX());
                force.setY(force.getY() + forces[j][i].getY());
                forces[j][i] = new Vector(0.0, 0.0);
            }

            Body b = bodies[i];

            double dVx = force.getX() / b.getMass() * DT;
            double dVy = force.getY() / b.getMass() * DT;
            double dPx = (b.getVx() + dVx / 2.0) * DT;
            double dPy = (b.getVy() + dVy / 2.0) * DT;

            b.setVx(b.getVx() + dVx);
            b.setVy(b.getVy() + dVy);
            b.setX(b.getX() + dPx);
            b.setY(b.getY() + dPy);

            force.setX(0.0);
            force.setY(0.0);
        }   
    }
}