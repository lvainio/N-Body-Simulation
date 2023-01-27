/*
 * Simple GUI class that display the bodies movement.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

class GUI extends JFrame {
    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;
    private final int WIDTH = 800;
    private final int HEIGHT = 800;
    
    int hej = 100;

    // private Body[] bodies;
    private Color[] colors; // every bodies color;

    private JPanel panel;

    /*
     * Constructor for GUI object.  
     */
    public GUI(String title) { // TODO: take in a reference to all the bodies i guess? 
        System.setProperty("sun.java2d.opengl", "true"); // Enable video acceleration

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH = (int) screenSize.getWidth();
        SCREEN_HEIGHT = (int) screenSize.getHeight();

        // TODO: generate a color for every body.

        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100,0, WIDTH, HEIGHT);
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

            // TODO: fill background

            // TODO: draw all bodies

            // TODO: make sure they get placed in a reasonable position. depending on how their coordinate system
            // works we might have to put (x=0, y=0) in the middle of the screen and similar.

            // TODO: draw a line were the bodies has traveled?

            // TODO: change size of body depending on z-value to fake 3d effect.

            g.setColor(Color.GREEN);
            g.drawOval(200, 200, hej, hej);
            g.fillOval(200, 200, hej, hej);

            g.fillOval(700, 700, hej, hej);
        }
    }
}