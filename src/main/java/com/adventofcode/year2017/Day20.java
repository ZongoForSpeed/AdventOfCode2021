package com.adventofcode.year2017;

import com.adventofcode.map.Point3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Day20 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day20.class);
    private static final Pattern PATTERN = Pattern.compile("p=<(.*)>, v=<(.*)>, a=<(.*)>");

    private Day20() {
        // No-Op
    }

    private static List<Particle> readParticles(Scanner scanner) {
        List<Particle> particles = new ArrayList<>();
        int count = 0;
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            particles.add(Particle.of(count++, s));
        }

        LOGGER.info("particles={}", particles);
        return particles;
    }

    /**
     * --- Day 20: Particle Swarm ---
     *
     * Suddenly, the GPU contacts you, asking for help. Someone has asked it to
     * simulate too many particles, and it won't be able to finish them all in
     * time to render the next frame at this rate.
     *
     * It transmits to you a buffer (your puzzle input) listing each particle in
     * order (starting with particle 0, then particle 1, particle 2, and so on).
     * For each particle, it provides the X, Y, and Z coordinates for the
     * particle's position (p), velocity (v), and acceleration (a), each in the
     * format <X,Y,Z>.
     *
     * Each tick, all particles are updated simultaneously. A particle's
     * properties are updated in the following order:
     *
     *   - Increase the X velocity by the X acceleration.
     *   - Increase the Y velocity by the Y acceleration.
     *   - Increase the Z velocity by the Z acceleration.
     *   - Increase the X position by the X velocity.
     *   - Increase the Y position by the Y velocity.
     *   - Increase the Z position by the Z velocity.
     *
     * Because of seemingly tenuous rationale involving z-buffering, the GPU would
     * like to know which particle will stay closest to position <0,0,0> in the
     * long term. Measure this using the Manhattan distance, which in this
     * situation is simply the sum of the absolute values of a particle's X, Y,
     * and Z position.
     *
     * For example, suppose you are only given two particles, both of which stay
     * entirely on the X-axis (for simplicity). Drawing the current states of
     * particles 0 and 1 (in that order) with an adjacent a number line and
     * diagram of current X positions (marked in parentheses), the following
     * would take place:
     *
     * p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
     * p=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0>                         (0)(1)
     *
     * p=< 4,0,0>, v=< 1,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
     * p=< 2,0,0>, v=<-2,0,0>, a=<-2,0,0>                      (1)   (0)
     *
     * p=< 4,0,0>, v=< 0,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
     * p=<-2,0,0>, v=<-4,0,0>, a=<-2,0,0>          (1)               (0)
     *
     * p=< 3,0,0>, v=<-1,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
     * p=<-8,0,0>, v=<-6,0,0>, a=<-2,0,0>                         (0)
     *
     * At this point, particle 1 will never be closer to <0,0,0> than particle 0,
     * and so, in the long run, particle 0 will stay closest.
     *
     * Which particle will stay closest to position <0,0,0> in the long term?
     *
     * Your puzzle answer was 125.
     */
    static Particle nearestParticle(Scanner scanner) {
        List<Particle> particles = readParticles(scanner);

        for (int i = 0; i < 1000; ++i) {
            particles = particles.stream().map(Particle::tick).sorted(Comparator.comparingInt(p -> Point3D.manhattanDistance(Point3D.ORIGIN, p.p()))).toList();

            Particle particle = particles.get(0);
            LOGGER.info("nearset particule = {}", particle);
        }
        return particles.get(0);
    }

    /**
     * --- Part Two ---
     *
     * To simplify the problem further, the GPU would like to remove any particles
     * that collide. Particles collide if their positions ever exactly match.
     * Because particles are updated simultaneously, more than two particles can
     * collide at the same time and place. Once particles collide, they are
     * removed and cannot collide with anything else after that tick.
     *
     * For example:
     *
     * p=<-6,0,0>, v=< 3,0,0>, a=< 0,0,0>
     * p=<-4,0,0>, v=< 2,0,0>, a=< 0,0,0>    -6 -5 -4 -3 -2 -1  0  1  2  3
     * p=<-2,0,0>, v=< 1,0,0>, a=< 0,0,0>    (0)   (1)   (2)            (3)
     * p=< 3,0,0>, v=<-1,0,0>, a=< 0,0,0>
     *
     * p=<-3,0,0>, v=< 3,0,0>, a=< 0,0,0>
     * p=<-2,0,0>, v=< 2,0,0>, a=< 0,0,0>    -6 -5 -4 -3 -2 -1  0  1  2  3
     * p=<-1,0,0>, v=< 1,0,0>, a=< 0,0,0>             (0)(1)(2)      (3)
     * p=< 2,0,0>, v=<-1,0,0>, a=< 0,0,0>
     *
     * p=< 0,0,0>, v=< 3,0,0>, a=< 0,0,0>
     * p=< 0,0,0>, v=< 2,0,0>, a=< 0,0,0>    -6 -5 -4 -3 -2 -1  0  1  2  3
     * p=< 0,0,0>, v=< 1,0,0>, a=< 0,0,0>                       X (3)
     * p=< 1,0,0>, v=<-1,0,0>, a=< 0,0,0>
     *
     * ------destroyed by collision------
     * ------destroyed by collision------    -6 -5 -4 -3 -2 -1  0  1  2  3
     * ------destroyed by collision------                      (3)
     * p=< 0,0,0>, v=<-1,0,0>, a=< 0,0,0>
     *
     * In this example, particles 0, 1, and 2 are simultaneously destroyed at the
     * time and place marked X. On the next tick, particle 3 passes through
     * unharmed.
     *
     * How many particles are left after all collisions are resolved?
     *
     * Your puzzle answer was 461.
     */
    static int particlesCollision(Scanner scanner) {
        List<Particle> particles = readParticles(scanner);

        for (int i = 0; i < 1000; ++i) {
            Map<Point3D, List<Particle>> collisions = new HashMap<>();
            particles.stream().map(Particle::tick).forEach(
                    p -> collisions.computeIfAbsent(p.p(), ignore -> new ArrayList<>()).add(p)
            );

            particles = collisions.values().stream().filter(l -> l.size() == 1)
                    .flatMap(Collection::stream).toList();

            if (particles.size() == 1) {
                break;
            }

            LOGGER.info("particule count = {}", particles.size());
        }
        return particles.size();
    }

    record Particle(int id, Point3D p, Point3D v, Point3D a) {
        public static Particle of(int id, Point3D p, Point3D v, Point3D a) {
            return new Particle(id, p, v, a);
        }

        public static Particle of(int id, String input) {
            Matcher matcher = PATTERN.matcher(input);
            if (matcher.matches()) {
                Point3D position = parse(matcher.group(1));
                Point3D velocity = parse(matcher.group(2));
                Point3D acceleration = parse(matcher.group(3));

                return new Particle(id, position, velocity, acceleration);
            }

            throw new IllegalStateException("Cannot parse input '" + input + "'");
        }

        private static Point3D parse(String input) {
            int[] array = Arrays.stream(input.split(","))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .toArray();

            return Point3D.of(array[0], array[1], array[2]);
        }

        public Particle tick() {
            Point3D newVelocity = Point3D.add(v, a);
            Point3D newPosition = Point3D.add(p, newVelocity);
            return new Particle(id, newPosition, newVelocity, a);
        }
    }
}
