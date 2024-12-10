package se.pabi.aoc.year2022;

import se.pabi.aoc.base.AdventOfCode;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day9 extends AdventOfCode<Stream<Day9.Input>> {

    public record Input(Dir dir, int steps) {
        enum Dir {
            LEFT, RIGHT, UP, DOWN;

            public static Dir from(String s) {
                return switch (s.toUpperCase()) {
                    case "L" -> LEFT;
                    case "R" -> RIGHT;
                    case "U" -> UP;
                    case "D" -> DOWN;
                    default -> throw new IllegalArgumentException("Invalid direction: " + s);
                };
            }
        }

        public Input(String dir, int steps) {
            this(Dir.from(dir), steps);
        }
    }

    @Override
    public Stream<Input> input(String input) {
        return input.lines().map(line -> {
            var parts = line.split(" ");
            return new Input(parts[0], Integer.parseInt(parts[1]));
        });
    }

    @Override
    public Object part1(Stream<Input> input) {
        return day9(input, 2);
    }

    @Override
    public Object part2(Stream<Input> input) {
        return day9(input, 10);
    }

    private static int day9(Stream<Input> input, int len) {
        var visited = new HashSet<Point>();
        visited.add(new Point(0, 0));

        LinkedList<Point> tail = Stream.generate(() -> new Point(0, 0))
                .limit(len -1)
                .collect(Collectors.toCollection(LinkedList::new));
        Point head = new Point(0, 0);


        input.forEach(move -> {
            for (int i = 0; i < move.steps(); i++) {
                var h = head;
                switch (move.dir) {
                    case LEFT -> h.x--;
                    case RIGHT -> h.x++;
                    case UP -> h.y--;
                    case DOWN -> h.y++;
                }

                for (Point t : tail) {
                    var hor = h.x - t.x;
                    var ver = h.y - t.y;
                    if (Math.abs(hor) == 2 || Math.abs(ver) == 2) {
                        t.translate(Integer.signum(hor), Integer.signum(ver));
                    }
                    h = t;
                }

                visited.add(new Point(h));
            }
        });

        return visited.size();
    }
}
