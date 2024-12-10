package se.pabi.aoc.year2024;

import se.pabi.aoc.base.AdventOfCode;

import java.util.*;
import java.util.stream.Stream;

public class Day10 extends AdventOfCode<int[][]> {

    @Override
    public int[][] input(String input) {
        return input.lines()
                .map(line -> line.chars().map(c -> c == '.' ? -2 : c - '0').toArray())
                .toArray(int[][]::new);
    }

    record Point(int x, int y) {
        boolean inside(int[][] grid) {
            return x >= 0 && x < grid[0].length && y >= 0 && y < grid.length;
        }
        Stream<Point> neighbors(int[][] grid) {
            return Stream.of(
                    new Point(x - 1, y),
                    new Point(x + 1, y),
                    new Point(x, y - 1),
                    new Point(x, y + 1)
            ).filter(p -> p.inside(grid));
        }
    }

    @Override
    public Object part1(int[][] input) {
        return findStarts(input).stream()
                .mapToLong(p -> countTrails(input, p))
                .sum();
    }

    private static Set<Point> findStarts(int[][] input) {
        Set<Point> starts = new HashSet<>();
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length; x++) {
                if (input[y][x] == 0) {
                    starts.add(new Point(x, y));
                }
            }
        }
        return starts;
    }

    private static long countTrails(int[][] input, Point start) {
        Set<Point> open = new HashSet<>();
        open.add(start);
        Set<Point> visited = new HashSet<>();
        while (!open.isEmpty()) {
            Point current = open.iterator().next();
            open.remove(current);
            visited.add(current);
            current.neighbors(input).filter(p -> input[p.y][p.x] - input[current.y][current.x] == 1).forEach(p -> {
                if (!visited.contains(p)) {
                    open.add(p);
                }
            });
        }
        return visited.stream().filter(point -> input[point.y][point.x] == 9).count();
    }

    @Override
    public Object part2(int[][] input) {
        return findStarts(input).stream()
                .mapToLong(p -> countAllTrails(input, p))
                .sum();
    }

    private static long countAllTrails(int[][] input, Point start) {
        Map<Point, Long> open = new HashMap<>();
        open.put(start, 1L);
        Map<Point, Long> result = new HashMap<>();

        while (!open.isEmpty()) {
            var currentEntry = open.entrySet().stream()
                    .min(Comparator.comparingInt(e -> input[e.getKey().y][e.getKey().x]))
                    .orElseThrow();
            var current = currentEntry.getKey();
            var value = currentEntry.getValue();
            current.neighbors(input).filter(p -> input[p.y][p.x] - input[current.y][current.x] == 1).forEach(p -> {
                long newValue = value;
                if (open.containsKey(p)) {
                    newValue += open.get(p);
                }
                open.put(p, newValue);
            });
            if (input[current.y][current.x] == 9) {
                result.put(current, value);
            }
            open.remove(current);
        }
        return result.values().stream().mapToLong(Long::longValue).sum();
    }
}
