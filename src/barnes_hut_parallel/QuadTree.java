/**
 * Each node in the tree has four children that represents each quadrant. A leaf of this 
 * tree is called an external node and may or may not contain a body. A node contains at 
 * most one body. Internal nodes does however sum up the mass and keep track of the center
 * of mass of all the bodies it contains.
 * 
 * @author Leo Vainio
 */

public class QuadTree {
    private QuadTree northWest = null;
    private QuadTree northEast = null;
    private QuadTree southWest = null;
    private QuadTree southEast = null;
    
    private Quadrant quadrant;
    
    private Body groupBody = null;
    private Body body = null;

    private Settings settings;

    /*
     * Create a quadtree.
     */
    public QuadTree(Quadrant quadrant, Settings settings) {
        this.quadrant = quadrant;
        this.settings = settings;
    }

    /*
     * 
     */
    public void reset(Quadrant quadrant) {
        this.quadrant = quadrant;
        northWest = null;
        northEast = null;
        southWest = null;
        southEast = null;
        groupBody = null;
        body = null;
    }

    /*
     * Insert an array of bodies into the quadtree.
     */
    public void insertBodies(Body[] bodies) {
        for (Body body : bodies) {
            insert(body);
        }
    }

    /*
     * Insert a single body into the tree. 
     */
    private void insert(Body body) {
        // Do not add a body that is not in this quadrant.
        if (!this.quadrant.containsBody(body)) {
            return;
        }
        // External empty node.
        else if (this.body == null) {
            this.body = body;
        } 
        // Internal node.
        else if (isInternal()) {
            groupBody.addBody(body);
            insertInQuadrant(body);
        } 
        // External full node.
        else if (isExternal()) {
            double x = quadrant.getX();
            double y = quadrant.getY();
            double r = quadrant.getRadius();

            northWest = new QuadTree(new Quadrant(x - r/2.0, y - r/2.0, r/2.0), settings);
            northEast = new QuadTree(new Quadrant(x + r/2.0, y - r/2.0, r/2.0), settings);
            southWest = new QuadTree(new Quadrant(x - r/2.0, y + r/2.0, r/2.0), settings);
            southEast = new QuadTree(new Quadrant(x + r/2.0, y + r/2.0, r/2.0), settings);

            insertInQuadrant(this.body);
            insertInQuadrant(body);

            double centerX = (this.body.getX()*this.body.getMass() + body.getX()*body.getMass()) / (this.body.getMass() + body.getMass());
            double centerY = (this.body.getY()*this.body.getMass() + body.getY()*body.getMass()) / (this.body.getMass() + body.getMass());

            groupBody = new Body(centerX, centerY, 0.0, 0.0, this.body.getMass()+body.getMass(), settings);
        }   
    }

    /*
     * Insert the body recursively into the correct quadrant.
     */
    private void insertInQuadrant(Body b) {
        if (northWest.getQuadrant().containsBody(b)) {
            northWest.insert(b);
        } 
        else if (northEast.getQuadrant().containsBody(b)) {
            northEast.insert(b);
        } 
        else if (southWest.getQuadrant().containsBody(b)) {
            southWest.insert(b);
        } 
        else if (southEast.getQuadrant().containsBody(b)) {
            southEast.insert(b);
        }
    }

    /*
     * Calculate the force exerted on a single body by traversing the tree and
     * adding up the total force.
     */
    public void computeForce(Body body) {
        if (isExternal() && body != this.body && this.body != null) {
            body.addForce(this.body);
        } 
        else if (groupBody != null) {
            double distance = Math.sqrt(Math.pow(body.getX()-groupBody.getX(), 2) + Math.pow(body.getY()-groupBody.getY(), 2));
            if (distance > settings.approximationDistance()) {
                body.addForce(this.groupBody);
            } 
            else {
                if (northWest != null)
                    northWest.computeForce(body);
                if (northEast != null)
                    northEast.computeForce(body);
                if (southWest != null)
                    southWest.computeForce(body);
                if (southEast != null)
                    southEast.computeForce(body);
            }
        }
    }

    /*
     * Returns true iff the node is internal. A node is internal if it has
     * subtrees.
     */
    private boolean isInternal() {
        if (northWest != null) { // arbitrary choice of quadrant.
            return true;
        }
        return false;
    }

    /*
     * Returns true iff the node is external. A node is external if it has no
     * subtrees, i.e. the node is a leaf.
     */
    private boolean isExternal() {
        if (northWest == null) { // arbitrary choice of quadrant.
            return true;
        }
        return false;
    }

    /*
    * Returns the quadrant of this node.
    */
    private Quadrant getQuadrant() {
        return this.quadrant;
    }
}
