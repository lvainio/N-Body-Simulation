
/*
 * Simple body class to store information about a single body.
 */

public class Body {
    private Point p;
    private Velocity v;
    private Force f;
    private double mass;

    public Body(double x, double y, double vx, double vy, double mass) {
        p = new Point(x, y);
        v = new Velocity(vx, vy);
        f = new Force(0.0, 0.0);
        this.mass = mass;
    }

    // ----- GETTERS ----- //

    public double getX() {
        return p.x;
    }

    public double getY() {
        return p.y;
    }

    public double getVx() {
        return v.vx;
    }

    public double getVy() {
        return v.vy;
    }

    public double getFx() {
        return f.fx;
    }

    public double getFy() {
        return f.fy;
    }

    public double getMass() {
        return mass;
    }


    // ----- SETTERS ----- //

    public void setX(double x) {
        p.x = x;
    }

    public void setY(double y) {
        p.y = y;
    }

    public void setVx(double vx) {
        v.vx = vx;
    }

    public void setVy(double vy) {
        v.vy = vy;
    }

    public void setFx(double fx) {
        f.fx = fx;
    }

    public void setFy(double fy) {
        f.fy = fy;
    }

    /*
     * Point holds information about the bodys position.
     */
    class Point {
        double x;
        double y;
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    /*
     * Velocity holds information about the bodys velocity.
     */
    class Velocity {
        double vx;
        double vy;
        public Velocity(double vx, double vy) {
            this.vx = vx;
            this.vy = vy;
        }
    }

    /*
     * Force holds information about the bodys force.
     */
    class Force {
        double fx;
        double fy;
        public Force(double fx, double fy) {
            this.fx = fx;
            this.fy = fy;
        }
    }
}
