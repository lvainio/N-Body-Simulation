
/**
 * 
 */

public class Worker extends Thread {

    // Constants.
    private final double G = 6.67e-4;
    private final double DT = 1;
    
    private Body[] bodies;
    private int numBodies;

    private int id;
    private int numSteps;

    private GUI gui;
    private boolean guiToggled;

    /*
     * 
     */
    public Worker(Body[] bodies, int id, int numSteps, boolean guiToggled, boolean donutToggled) {
        this.bodies = bodies;
        this.numBodies = bodies.length;
        this.id = id;
        this.guiToggled = guiToggled;
        this.numSteps = numSteps;
        if (id == 0 && guiToggled) {
            gui = new GUI("N-body problem: parallel", bodies, donutToggled);
        }
    }

    /*
     * 
     */
    @Override
    public void run() { // TODO: implement barrier.
        for (int i = 0; i < numSteps; i++) {
            if (id == 0) { // TODO: remove
                calculateForces(id);
            }
            // TODO: force;
            // TODO: barrier;
            if (id == 0) { // TODO: remove
                moveBodies(id);
            }
            // TODO: move;
            if (id == 0 && guiToggled) {
                gui.repaint();
            }
            // TODO: barrier;
        }  
    }

    /*
     * Calculates total force for every pair of bodies.
     */
    private void calculateForces(int w) { // TODO: split up work between worker threads
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
    private void moveBodies(int w) { // TODO: split up work between worker threads.
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
