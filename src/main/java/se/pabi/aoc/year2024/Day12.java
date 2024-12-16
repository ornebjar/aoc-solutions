package se.pabi.aoc.year2024;

import se.pabi.aoc.util.IntPoint;
import se.phet.aoc.AdventOfCode;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 extends AdventOfCode<Stream<Set<IntPoint>>> {

    private final static Set<IntPoint> DIRECTIONS = Set.of(
            new IntPoint(1, 0),
            new IntPoint(-1, 0),
            new IntPoint(0, 1),
            new IntPoint(0, -1)
    );

    @Override
    public Stream<Set<IntPoint>> input(String input) {
        char[][] lines = input.lines()
                .map(String::toCharArray)
                .toArray(char[][]::new);
        Set<IntPoint> visited = new HashSet<>();
        Stream.Builder<Set<IntPoint>> builder = Stream.builder();

        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length; x++) {
                if (!visited.contains(new IntPoint(x, y))) {
                    HashSet<IntPoint> region = new HashSet<>();
                    createRegion(lines[y][x], lines, x, y, region);
                    visited.addAll(region);
                    builder.accept(region);
                }
            }
        }
        return builder.build();
    }

    private static void createRegion(char name, char[][] lines, int x, int y, HashSet<IntPoint> region) {
        if (x >= 0 && y >= 0 && x < lines[0].length && y < lines.length && lines[y][x] == name) {
            if (region.add(new IntPoint(x, y))) {
                createRegion(name, lines, x + 1, y, region);
                createRegion(name, lines, x - 1, y, region);
                createRegion(name, lines, x, y + 1, region);
                createRegion(name, lines, x, y - 1, region);
            }
        }
    }

    @Override
    public Object part1(Stream<Set<IntPoint>> input) {
        return input.mapToLong(region -> {
            long perimeter = region.stream()
                    .flatMap(point -> DIRECTIONS.stream().map(point::add))
                    .filter(p -> !region.contains(p))
                    .count();
            return region.size() * perimeter;
        }).sum();
    }

    @Override
    public Object part2(Stream<Set<IntPoint>> input) {
        return input.mapToLong(region -> {
            Map<IntPoint, Set<IntPoint>> fences = DIRECTIONS.stream()
                    .collect(Collectors.toMap(d -> d, _ -> new HashSet<>()));

            for (IntPoint point : region) {
                for (IntPoint direction : DIRECTIONS) {
                    IntPoint neighbor = point.add(direction);
                    if (!region.contains(neighbor)) {
                        fences.get(direction).add(neighbor);
                    }
                }
            }

            long sides = fences.entrySet().stream()
                    .mapToLong(entry -> {
                        IntPoint direction = entry.getKey();
                        Set<IntPoint> fence = entry.getValue();
                        return sides(direction, fence);
                    }).sum();


            return region.size() * sides;
        }).sum();
    }

    private static int sides(IntPoint direction, Set<IntPoint> fences) {
        Map<Integer, List<Integer>> sides;
        if (direction.x() == 0) {
            sides = fences.stream()
                    .collect(Collectors.groupingBy(IntPoint::y, Collectors.mapping(IntPoint::x, Collectors.toList())));
        } else {
            sides = fences.stream()
                    .collect(Collectors.groupingBy(IntPoint::x, Collectors.mapping(IntPoint::y, Collectors.toList())));
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
