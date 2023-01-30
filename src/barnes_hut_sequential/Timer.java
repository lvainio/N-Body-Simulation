/**
 * Timer class. High resolution timer used to benchmark the simulation.
 * 
 * @author Leo Vainio
 */

public class Timer {
    private long startTime;
    private long endTime;
    
    /*
     * Constructor for Timer.
     */
    public Timer() {
        startTime = System.nanoTime();
        endTime = System.nanoTime();
    }

    /*
     * Saves start time.
     */
    public void start() {
        startTime = System.nanoTime();
    }

    /*
     * Saves end time and print the elapsed time.
     */
    public void stopAndPrint() {
        stop();
        print();
    }

    /*
     * Saves end time.
     */
    public void stop() {
        endTime = System.nanoTime();
    }

    /*
     * Print the elapsed time.
     */
    public void print() {
        double elapsedSeconds = (endTime-startTime) / 1_000_000_000.0;
        System.out.println("> Execution time: " + elapsedSeconds);
    }
}
