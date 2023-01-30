/**
 * 
 */

public class Quadrant {
    private Vector center; 

    private Quadrant(double x, double y) {
        // TODO: set the fields.
    }

    public boolean containsBody(Body body) {
        double bx = body.getX();
        double by = body.getY();

        double cx = center.getX();
        double cy = center.getY();

        // TODO: contains stuff here


        return false;
    }
}
