/**
 * 
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
     * 
     */
    public QuadTree(Quadrant quadrant, Settings settings) {
        this.quadrant = quadrant;
        this.settings = settings;
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
     * 
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
     * 
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
     * 
     */
    public void calculateForces(Body[] bodies) {
        for (Body body : bodies) {
            calculateForce(body);
        }
    }

    /*
     * 
     */
    private void calculateForce(Body body) {
        if (isExternal() && body != this.body && this.body != null) {
            body.addForce(this.body);
        } 
        else if (groupBody != null) {
            double s = quadrant.getRadius() * 2;
            double d = Math.sqrt(Math.pow(body.getX()-groupBody.getX(), 2) + Math.pow(body.getY()-groupBody.getY(), 2));
            if (s / d < settings.threshold()) {
                body.addForce(this.groupBody);
            } 
            else {
                if (northWest != null)
                    northWest.calculateForce(body);
                if (northEast != null)
                    northEast.calculateForce(body);
                if (southWest != null)
                    southWest.calculateForce(body);
                if (southEast != null)
                    southEast.calculateForce(body);
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
