/**
 * A record that holds all the settings of the simulation.
 * 
 * @author Leo Vainio
 */

public record Settings (int numBodies,
                        int numSteps,
                        double far,
                        double threshold,
                        boolean guiToggled,
                        boolean ringToggled,
                        double DT,
                        double G,
                        double radius,
                        double mass) {}
