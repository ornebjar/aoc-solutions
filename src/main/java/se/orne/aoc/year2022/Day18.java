package se.orne.aoc.year2022;

import se.orne.aoc.AdventOfCode;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static java.util.function.Predicate.not;

public class Day18 extends AdventOfCode<Set<Day18.Cube>> {

    public record Cube(int x, int y, int z) {
        Cube translate(int dx, int dy, int dz) {
            return new Cube(x + dx, y + dy, z + dz);
        }
    }

    @Override
    public Set<Cube> input(String input) {
        return input.lines()
                .map(line -> line.split(","))
                .map(s -> new Cube(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2])))
                .collect(Collectors.toSet());
    }

    @Override
    public Object part1(Set<Cube> cubes) {
        return cubes.stream()
                .mapToInt(cube -> (int) Stream.of(
                                cube.translate(1, 0, 0),
                                cube.translate(-1, 0, 0),
                                cube.translate(0, 1, 0),
                                cube.translate(0, -1, 0),
                                cube.translate(0, 0, 1),
                                cube.translate(0, 0, -1))
                        .filter(not(cubes::contains)).count()).sum();
    }

    @Override
    public Object part2(Set<Cube> lava) {

        int minX = Collections.min(lava, comparingInt(Cube::x)).x;
        int maxX = Collections.max(lava, comparingInt(Cube::x)).x;
        int minY = Collections.min(lava, comparingInt(Cube::y)).y;
        int maxY = Collections.max(lava, comparingInt(Cube::y)).y;
        int minZ = Collections.min(lava, comparingInt(Cube::z)).z;
        int maxZ = Collections.max(lava, comparingInt(Cube::z)).z;

        LinkedList<Cube> open = new LinkedList<>();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                Cube lowCube = new Cube(x, y, minZ);
                if (!lava.contains(lowCube)) {
                    open.add(lowCube);
                }
                Cube highCube = new Cube(x, y, maxZ);
                if (!lava.contains(highCube)) {
                    open.add(highCube);
                }
            }
        }

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                Cube lowCube = new Cube(x, minY, z);
                if (!lava.contains(lowCube)) {
                    open.add(lowCube);
                }
                Cube highCube = new Cube(x, maxY, z);
                if (!lava.contains(highCube)) {
                    open.add(highCube);
                }
            }
        }

        for (int z = minZ; z <= maxZ; z++) {
            for (int y = minY; y <= maxY; y++) {
                Cube lowCube = new Cube(minX, y, z);
                if (!lava.contains(lowCube)) {
                    open.add(lowCube);
                }
                Cube highCube = new Cube(maxX, y, z);
                if (!lava.contains(highCube)) {
                    open.add(highCube);
                }
            }
        }

        Set<Cube> water = new HashSet<>();

        Cube next;
        while ((next = open.poll()) != null) {

            if (lava.contains(next)) {
                continue;
            }
            if (water.add(next)) {
                if (next.x < maxX) {
                    open.add(next.translate(1, 0, 0));
                }
                if (next.x > minX) {
                    open.add(next.translate(-1, 0, 0));
                }
                if (next.y < maxY) {
                    open.add(next.translate(0, 1, 0));
                }
                if (next.y > minY) {
                    open.add(next.translate(0, -1, 0));
                }
                if (next.z < maxZ) {
                    open.add(next.translate(0, 0, 1));
                }
                if (next.z > minZ) {
                    open.add(next.translate(0, 0, -1));
                }
            }
        }

        for (int x = minX+1; x < maxX; x++) {
            for (int y = minY+1; y < maxY; y++) {
                for (int z = minZ+1; z < maxZ; z++) {
                    Cube cube = new Cube(x, y, z);
                    if (!water.contains(cube)) {
                        lava.add(cube);
                    }
                }
            }
        }

        return part1(lava);
    }

}
