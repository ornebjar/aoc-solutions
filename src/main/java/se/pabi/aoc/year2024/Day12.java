package se.pabi.aoc.year2024;

import se.phet.aoc.AdventOfCode;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 extends AdventOfCode<Stream<Set<Day12.Point>>> {

    public record Point(int x, int y) {
        Point add(Point other) {
            return new Point(x + other.x, y + other.y);
        }
    }
    private final static Set<Point> DIRECTIONS = Set.of(
            new Point(1, 0),
            new Point(-1, 0),
            new Point(0, 1),
            new Point(0, -1)
    );

    @Override
    public Stream<Set<Point>> input(String input) {
        char[][] lines = input.lines()
                .map(String::toCharArray)
                .toArray(char[][]::new);
        Set<Point> visited = new HashSet<>();
        Stream.Builder<Set<Point>> builder = Stream.builder();

        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length; x++) {
                if (!visited.contains(new Point(x, y))) {
                    HashSet<Point> region = new HashSet<>();
                    createRegion(lines[y][x], lines, x, y, region);
                    visited.addAll(region);
                    builder.accept(region);
                }
            }
        }
        return builder.build();
    }

    private static void createRegion(char name, char[][] lines, int x, int y, HashSet<Point> region) {
        if (x >= 0 && y >= 0 && x < lines[0].length && y < lines.length && lines[y][x] == name) {
            if (region.add(new Point(x, y))) {
                createRegion(name, lines, x + 1, y, region);
                createRegion(name, lines, x - 1, y, region);
                createRegion(name, lines, x, y + 1, region);
                createRegion(name, lines, x, y - 1, region);
            }
        }
    }

    @Override
    public Object part1(Stream<Set<Point>> input) {
        return input.mapToLong(region -> {
            long perimeter = region.stream()
                    .flatMap(point -> DIRECTIONS.stream().map(point::add))
                    .filter(p -> !region.contains(p))
                    .count();
            return region.size() * perimeter;
        }).sum();
    }

    @Override
    public Object part2(Stream<Set<Point>> input) {
        return input.mapToLong(region -> {
            Map<Point, Set<Point>> fences = DIRECTIONS.stream()
                    .collect(Collectors.toMap(d -> d, _ -> new HashSet<>()));

            for (Point point : region) {
                for (Point direction : DIRECTIONS) {
                    Point neighbor = point.add(direction);
                    if (!region.contains(neighbor)) {
                        fences.get(direction).add(neighbor);
                    }
                }
            }

            long sides = fences.entrySet().stream()
                    .mapToLong(entry -> {
                        Point direction = entry.getKey();
                        Set<Point> fence = entry.getValue();
                        return sides(direction, fence);
                    }).sum();


            return region.size() * sides;
        }).sum();
    }

    private static int sides(Point direction, Set<Point> fences) {
        Map<Integer, List<Integer>> sides;
        if (direction.x == 0) {
            sides = fences.stream()
                    .collect(Collectors.groupingBy(p -> p.y, Collectors.mapping(p -> p.x, Collectors.toList())));
        } else {
            sides = fences.stream()
                    .collect(Collectors.groupingBy(p -> p.x, Collectors.mapping(p -> p.y, Collectors.toList())));
        }
        return sides.values().stream()
                .mapToInt(s -> {
                    int count = 1;
                    s.sort(Integer::compareTo);
                    int prev = s.getFirst();

                    for (int current : s) {
                        if (current - prev > 1) {
                            count++;
                        }
                        prev = current;
                    }
                    return count;
                })
                .sum();
    }
}
