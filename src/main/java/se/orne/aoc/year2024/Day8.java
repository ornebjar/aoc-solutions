package se.orne.aoc.year2024;

import se.orne.aoc.util.IntPoint;
import se.orne.aoc.AdventOfCode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day8 extends AdventOfCode<char[][]> {


    @Override
    public char[][] input(String input) {
        return input.lines()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    record Antenna(char id) {
        static Antenna from(char c) {
            return new Antenna(c);
        }
    }

    private static boolean inside(char[][] input, IntPoint point) {
        return point.x() >= 0 && point.x() < input[0].length && point.y() >= 0 && point.y() < input.length;
    }

    @Override
    public Object part1(char[][] input) {
        Map<Antenna, Set<IntPoint>> antennas = new HashMap<>();
        Set<IntPoint> antiNodes = new HashSet<>();

        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length; x++) {
                if (input[y][x] != '.') {
                    Antenna antenna = Antenna.from(input[y][x]);
                    Set<IntPoint> points = antennas.computeIfAbsent(
                            antenna,
                            _ -> new HashSet<>()
                    );

                    IntPoint next = new IntPoint(x, y);

                    for (IntPoint point : points) {
                        IntPoint antiNode = new IntPoint(next.x() + next.x() - point.x(), next.y() + next.y() - point.y());
                        if (inside(input, antiNode)) {
                            antiNodes.add(antiNode);
                        }
                        antiNode = new IntPoint(point.x() + point.x() - next.x(), point.y() + point.y() - next.y());
                        if (inside(input, antiNode)) {
                            antiNodes.add(antiNode);
                        }
                    }

                    points.add(next);
                }
            }
        }

        return antiNodes.size();
    }

    @Override
    public Object part2(char[][] input) {
        Map<Antenna, Set<IntPoint>> antennas = new HashMap<>();
        Set<IntPoint> antiNodes = new HashSet<>();

        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length; x++) {
                if (input[y][x] != '.') {
                    Antenna antenna = Antenna.from(input[y][x]);
                    Set<IntPoint> points = antennas.computeIfAbsent(
                            antenna,
                            _ -> new HashSet<>()
                    );

                    IntPoint next = new IntPoint(x, y);

                    for (IntPoint point : points) {
                        IntPoint jump = new IntPoint(next.x() - point.x(), next.y() - point.y());
                        IntPoint antiNode = next;
                        while (inside(input, antiNode)) {
                            antiNodes.add(antiNode);
                            antiNode = antiNode.add(jump);
                        }
                        jump = new IntPoint(point.x() - next.x(), point.y() - next.y());
                        antiNode = point;
                        while (inside(input, antiNode)) {
                            antiNodes.add(antiNode);
                            antiNode = antiNode.add(jump);
                        }
                    }

                    points.add(next);
                }
            }
        }

        return antiNodes.size();
    }
}
