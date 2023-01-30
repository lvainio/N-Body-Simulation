# n_body_simulation

## Usage:

#### compile: 
- javac *.java [java 14 or later required]

#### run (sequential): 
- java NBodySimulation [default settings]
- java NbodySimulation <num_bodies> <num_steps> 
- java NBodySimulation <num_bodies> <num_steps> -g
- java NBodySimulation <num_bodies> <num_steps> -g -r 

#### run (parallel):
- java NBodySimulation [default settings]
- java NbodySimulation <num_bodies> <num_steps> <num_workers>
- java NBodySimulation <num_bodies> <num_steps> <num_workers> -g
- java NBodySimulation <num_bodies> <num_steps> <num_workers> -g -r 

#### run (barnes-hut sequential):
- java NBodySimulation [default settings]
- java NbodySimulation <num_bodies> <num_steps> \<threshold>
- java NBodySimulation <num_bodies> <num_steps> \<threshold> -g
- java NBodySimulation <num_bodies> <num_steps> \<threshold> -g -r

#### run (barnes-hut parallel):
- java NBodySimulation [default settings]
- java NbodySimulation <num_bodies> <num_steps> \<threshold> <num_workers>
- java NBodySimulation <num_bodies> <num_steps> \<threshold> <num_workers> -g
- java NBodySimulation <num_bodies> <num_steps> \<threshold> <num_workers> -g -r

The flags -g -r can be set after the other arguments.

g: the simulation will be shown in a gui.

r: the bodies will be generated in a ring formation around a central, more massive body.

