
/*
 * A record that holds all the settings of the simulation.
 */

public record Settings (int numBodies,
                        int numSteps,
                        int numWorkers,
                        boolean guiToggled,
                        boolean donutToggled,
                        double DT,
                        double G,
                        double radius,
                        double mass) {}
