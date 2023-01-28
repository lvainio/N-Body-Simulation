# n_body_simulation

## Usage:

compile: javac *.java (in any of the source folders)

run: 

- java Simulate
- java Simulate <num_bodies>
- java Simulate <num_bodies> <num_steps>
- java Simulate <num_bodies> <num_steps> -g
- java Simulate <num_bodies> <num_steps> -g -d

## Settings:

- M=1, D=100 000, DT=1, v=rng*1-0.5
- M=100, D=1 000 000, DT=1, v=rng*5-2-5
- M=1000, -||-
- M=1000, D= 1 000 000, DT=1, v=rng*25-12.5 (this one was nice)

## Donut settings:

- MASS = 100, MASS_CENTER = 100 000 000 000.0, vel*10, DIAMETER=1 000 000, G = 6.67e-4, DT = 1
