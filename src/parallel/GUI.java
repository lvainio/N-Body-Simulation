/**
 * GUI class that show how the bodies move.
 * 
 * @author Leo Vainio
 */

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JFrame;

import java.util.Random;

class GUI extends JFrame {
    private final int FRAME_WIDTH = 800;
    private final int FRAME_HEIGHT = 800;
    private final int RADIUS = 10;

    private JPanel panel;

    private Body[] bodies;
    private Color[] colors; 
    private boolean donutToggled;

    /*
     * Constructor for GUI object.  
     */
    public GUI(String title, Body[] bodies, boolean donutToggled) {
        System.setProperty("sun.java2d.opengl", "true"); // Enable video acceleration.

        this.bodies = bodies;
        this.donutToggled = donutToggled;

        // generate colors for each body.
        colors = new Color[bodies.length];
        Random rng = new Random();
        rng.setSeed(System.nanoTime());
        for (int i = 0; i < colors.length; i++) {
            int r = rng.nextInt(256);
            int g = rng.nextInt(256);
            int b = rng.nextInt(256);
            colors[i] = new Color(r, g, b);
        }

        // initialize frame.
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100,0, FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setVisible(true);
        panel = new Panel();
        add(panel);
    }

    /*
     * repaint gets called in every step of the simulation to update the position of the bodies
     * on the screen.
     */
    @Override
    public void repaint() {
        panel.repaint();
    }

    /*
     * A simple nested class that handles drawing the bodies onto the canvas in each step.
     */
    class Panel extends JPanel {
        public void paint(Graphics g) {
            // draw background.
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

            // draw bodies.
            double scale = 800.0 / (Simulation.RADIUS*2);
            int i = 0;
            if (donutToggled) {
                Body body = bodies[0];
                g.setColor(colors[0]);
                g.fillOval((int) (body.getX()*scale), (int) (body.getY()*scale), RADIUS*3, RADIUS*3);
                i = 1;
            }
            for (; i < colors.length; i++) {
                Body body = bodies[i];
                g.setColor(colors[i]);
                g.fillOval((int) (body.getX()*scale), (int) (body.getY()*scale), RADIUS, RADIUS);
            }
            g.dispose();
        }
    }
}