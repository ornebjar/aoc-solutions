package se.pabi.aoc.year2024;

import se.phet.aoc.AdventOfCode;

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

    record Point(int x, int y) {
        boolean inside(char[][] input) {
            return x >= 0 && x < input[0].length && y >= 0 && y < input.length;
        }
        Point add(Point other) {
            return new Point(x + other.x, y + other.y);
        }
    }

    @Override
    public Object part1(char[][] input) {
        Map<Antenna, Set<Point>> antennas = new HashMap<>();
        Set<Point> antiNodes = new HashSet<>();

        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length; x++) {
                if (input[y][x] != '.') {
                    Antenna antenna = Antenna.from(input[y][x]);
                    Set<Point> points = antennas.computeIfAbsent(
                            antenna,
                            _ -> new HashSet<>()
                    );

                    Point next = new Point(x, y);

                    for (Point point : points) {
                        Point antiNode = new Point(next.x + next.x - point.x, next.y + next.y - point.y);
                        if (antiNode.inside(input)) {
                            antiNodes.add(antiNode);
                        }
                        antiNode = new Point(point.x + point.x - next.x, point.y + point.y - next.y);
                        if (antiNode.inside(input)) {
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
        Map<Antenna, Set<Point>> antennas = new HashMap<>();
        Set<Point> antiNodes = new HashSet<>();

        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length; x++) {
                if (input[y][x] != '.') {
                    Antenna antenna = Antenna.from(input[y][x]);
                    Set<Point> points = antennas.computeIfAbsent(
                            antenna,
                            _ -> new HashSet<>()
                    );

                    Point next = new Point(x, y);

                    for (Point point : points) {
                        Point jump = new Point(next.x - point.x, next.y - point.y);
                        Point antiNode = next;
                        while (antiNode.inside(input)) {
                            antiNodes.add(antiNode);
                            antiNode = antiNode.add(jump);
                        }
                        jump = new Point(point.x - next.x, point.y - next.y);
                        antiNode = point;
                        while (antiNode.inside(input)) {
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
