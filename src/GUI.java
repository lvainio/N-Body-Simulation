/*
 * Simple GUI class that display the bodies movement.
 */

import java.awt.Color;
import java.awt.Graphics;


import java.util.Random;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

class GUI extends JFrame {
    private final int WIDTH = 800;
    private final int HEIGHT = 800;

    private final int BODY_RADIUS = 20;

    private JPanel panel;
    private BufferedImage background;

    private Data bodies;
    private Color[] colors; 

    /*
     * Constructor for GUI object.  
     */
    public GUI(String title, Data bodies) {
        System.setProperty("sun.java2d.opengl", "true"); // Enable video acceleration.

        this.bodies = bodies;

        // generate colors for each body.
        colors = new Color[bodies.getNumBodies()];
        Random rng = new Random();
        rng.setSeed(System.nanoTime());
        for (int i = 0; i < colors.length; i++) {
            int r = rng.nextInt(256);
            int g = rng.nextInt(256);
            int b = rng.nextInt(256);
            colors[i] = new Color(r, g, b);
        }

        // load background.
        try {
            background = ImageIO.read(getClass().getResourceAsStream("./images/background.jpg"));
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        // initialize frame.
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
            double scale = 800.0 / 1_000_000_000.0; // TODO: right scale?
            g.drawImage(background, 0, 0, null);
            for (int i = 0; i < colors.length; i++) {
                Body body = bodies.get(i);
                int x = (int) (body.getX() * scale); 
                int y = (int) (body.getY() * scale);
                g.setColor(colors[i]);
                g.fillOval(x, y, BODY_RADIUS, BODY_RADIUS);
            }
            g.dispose();
        }
    }
}