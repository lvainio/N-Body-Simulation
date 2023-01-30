# n_body_simulation

## Usage:

#### compile: 
- javac *.java 

#### run (sequential): 
- java NBodySimulation [default settings]
- java NbodySimulation <num_bodies> <num_steps> 
- java NBodySimulation <num_bodies> <num_steps> -g
- java NBodySimulation <num_bodies> <num_steps> -g -d 

The flags -g -d can be set after the first two arguments.

-g: the simulation will be shown in a gui.
-d: the bodies will be generated in a ring formation around a central, more massive body.

#### run (parallel):
- java NBodySimulation [default settings]
- java NbodySimulation <num_bodies> <num_steps> <num_workers>
- java NBodySimulation <num_bodies> <num_steps> <num_workers> -g
- java NBodySimulation <num_bodies> <num_steps> <num_workers> -g -d 

The flags -g -d can be set after the first two arguments.

-g: the simulation will be shown in a gui.
-d: the bodies will be generated in a ring formation around a central, more massive body.



## Settings:

- M=1, D=100 000, DT=1, v=rng*1-0.5
- M=100, D=1 000 000, DT=1, v=rng*5-2-5
- M=1000, -||-
- M=1000, D= 1 000 000, DT=1, v=rng*25-12.5 (this one was nice)

## Donut settings:

- MASS = 100, MASS_CENTER = 100 000 000 000.0, vel*10, DIAMETER=1 000 000, G = 6.67e-4, DT = 1
