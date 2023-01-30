/**
 * 
 */

public class QuadTree {
    private QuadTree northWest;
    private QuadTree northEast;
    private QuadTree southWest;
    private QuadTree southEast;
    
    private Quadrant quad;
    private Body body;
    
    private Vector centerOfMass;
    private double groupMass;

    private double threshold;

    public QuadTree(double threshold) {
        this.groupMass = 0.0;
        

        this.threshold = threshold;
    }

    public void insert(Body body) {
        if (this.body == null) {
            this.body = body;

        } else if (isInternal()) {
            double x = (groupMass * centerOfMass.getX() + body.getX() * body.getMass()) / (body.getMass() + groupMass);
            double y = (groupMass * centerOfMass.getY() + body.getY() * body.getMass()) / (body.getMass() + groupMass);
            groupMass += body.getMass();
            centerOfMass.setX(x);
            centerOfMass.setY(y);

            // can we just call a contains() method on each of its childrens QUAD.
            // the one who is true is were body goes.

            // just insert in proper quadrant.

        } else {

        }

        
        // Recursively insert the body b in the appropriate quadrant.
    }

    public void calculateForce(Body body) {

    }

    /*
     * Returns true if the node is internal. A node is internal if it has
     * subtrees.
     */
    private boolean isInternal() {
        if (northWest != null) { // arbitrary choice of quadrant.
            return true;
        }
        return false;
    }

    /*
     * 
     */
    private boolean isExternal() {
        return false;
    }
}
