
/*
 * A record that holds all the settings of the simulation.
 */

public record Settings (int numBodies,
                        int numSteps,
                        boolean guiToggled,
                        boolean ringToggled,
                        double DT,
                        double G,
                        double radius,
                        double mass) {}
