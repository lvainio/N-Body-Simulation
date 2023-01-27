
/*
 * Sequential implementation of the n-body problem.
 * 
 * Usage:
 *      compile: javac *.java
 *      execute: java Sequential <num_bodies> <num_steps> 
 *      graphic: java Sequential <num_bodies> <num_steps> -g
 */

public class Sequential {
    private GUI gui;
    private Timer timer;

    private static int numBodies = 100;
    private static int numSteps = 100;

    private static boolean guiToggled = false;

    public Sequential() {
        timer = new Timer();
        timer.start();

        if (guiToggled) {
            gui = new GUI("N-body problem: sequential");
        }
        
        System.out.println(numBodies + " " + numSteps);

        timer.stopAndPrint();
    }

    /*
     * Main.
     */
    public static void main(String[] args) {
        // Command line args
        try {
            if (args.length >= 1) {
                numBodies = Integer.parseInt(args[0]);
            }
            if (args.length >= 2) {
                numSteps = Integer.parseInt(args[1]);
            }
            if (args.length >= 3) {
                if (args[2].equals("-g")) {
                    guiToggled = true;
                }
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            System.exit(1);
        }

        Sequential nBody = new Sequential();

    }
}
