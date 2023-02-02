# n_body_simulation

## Usage:

### Compile: 
- javac *.java [java 14 or later required]

### Run:

#### Sequential:
- java NBodySimulation [default settings]
- java NBodySimulation <numBodies> <numSteps> 
- java NBodySimulation <numBodies> <numSteps> -g -r

#### Parallel:
- java NBodySimulation [default settings]
- java NBodySimulation <numBodies> <numSteps> <numWorkers>
- java NBodySimulation <numBodies> <numSteps> <numWorkers> -g -r

#### Barnes-Hut sequential:
- java NBodySimulation [default settings]
- java NBodySimulation <numBodies> <numSteps> <theta>
- java NBodySimulation <numBodies> <numSteps> <theta> -g -r

#### Barnes-Hut parallel:
- java NBodySimulation [default settings]
- java NBodySimulation <numBodies> <numSteps> <theta> <numWorkers>
- java NBodySimulation <numBodies> <numSteps> <theta> <numWorkers> -g -r

The flags "-g" and "-r" can be set after the other arguments:
- g: the simulation will be shown in a gui.
- r: the bodies will be generated in a ring formation around a central, more massive body.

