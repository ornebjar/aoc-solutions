package se.pabi.aoc.year2024;

import se.pabi.aoc.util.IntPoint;
import se.phet.aoc.AdventOfCode;

import java.util.*;
import java.util.stream.Stream;

public class Day10 extends AdventOfCode<int[][]> {

    @Override
    public int[][] input(String input) {
        return input.lines()
                .map(line -> line.chars().map(c -> c == '.' ? -2 : c - '0').toArray())
                .toArray(int[][]::new);
    }

    private static Stream<IntPoint> neighbors(IntPoint point, int[][] grid) {
        return Stream.of(
                new IntPoint(point.x() - 1, point.y()),
                new IntPoint(point.x() + 1, point.y()),
                new IntPoint(point.x(), point.y() - 1),
                new IntPoint(point.x(), point.y() + 1)
        ).filter(p -> p.x() >= 0 && p.x() < grid[0].length && p.y() >= 0 && p.y() < grid.length);
    }

    @Override
    public Object part1(int[][] input) {
        return findStarts(input).stream()
                .mapToLong(p -> countTrails(input, p))
                .sum();
    }

    private static Set<IntPoint> findStarts(int[][] input) {
        Set<IntPoint> starts = new HashSet<>();
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length; x++) {
                if (input[y][x] == 0) {
                    starts.add(new IntPoint(x, y));
                }
            }
        }
        return starts;
    }

    private static long countTrails(int[][] input, IntPoint start) {
        Set<IntPoint> open = new HashSet<>();
        open.add(start);
        Set<IntPoint> visited = new HashSet<>();
        while (!open.isEmpty()) {
            IntPoint current = open.iterator().next();
            open.remove(current);
            visited.add(current);
            neighbors(current, input).filter(p -> input[p.y()][p.x()] - input[current.y()][current.x()] == 1).forEach(p -> {
                if (!visited.contains(p)) {
                    open.add(p);
                }
            });
        }
        return visited.stream().filter(point -> input[point.y()][point.x()] == 9).count();
    }

    @Override
    public Object part2(int[][] input) {
        return findStarts(input).stream()
                .mapToLong(p -> countAllTrails(input, p))
                .sum();
    }

    private static long countAllTrails(int[][] input, IntPoint start) {
        Map<IntPoint, Long> open = new HashMap<>();
        open.put(start, 1L);
        Map<IntPoint, Long> result = new HashMap<>();

        while (!open.isEmpty()) {
            var currentEntry = open.entrySet().stream()
                    .min(Comparator.comparingInt(e -> input[e.getKey().y()][e.getKey().x()]))
                    .orElseThrow();
            var current = currentEntry.getKey();
            var value = currentEntry.getValue();
            neighbors(current, input).filter(p -> input[p.y()][p.x()] - input[current.y()][current.x()] == 1).forEach(p -> {
                long newValue = value;
                if (open.containsKey(p)) {
                    newValue += open.get(p);
                }
                open.put(p, newValue);
            });
            if (input[current.y()][current.x()] == 9) {
                result.put(current, value);
            }
            open.remove(current);
        }
        return result.values().stream().mapToLong(Long::longValue).sum();
    }
}
