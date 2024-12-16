package se.pabi.aoc.year2024;

import se.pabi.aoc.util.IntPoint;
import se.phet.aoc.AdventOfCode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day15 extends AdventOfCode<Stream<IntPoint>> {

    private static final Map<Character, IntPoint> DIRECTIONS = Map.of(
            '^', new IntPoint(0, -1),
            'v', new IntPoint(0, 1),
            '<', new IntPoint(-1, 0),
            '>', new IntPoint(1, 0)
    );

    private boolean[][] walls;
    private IntPoint position;
    private Set<IntPoint> boxes;

    @Override
    public Stream<IntPoint> input(String input) {
        var parts = input.split("\n\n");
        var map = parts[0].lines().map(String::toCharArray).toArray(char[][]::new);
        boxes = new HashSet<>();
        walls = new boolean[map.length][];
        for (int y = 0; y < map.length; y++) {
            walls[y] = new boolean[map[y].length];
            for (int x = 0; x < map[y].length; x++) {
                switch (map[y][x]) {
                    case '#':
                        walls[y][x] = true;
                        break;
                    case 'O':
                        boxes.add(new IntPoint(x, y));
                        break;
                    case '@':
                        position = new IntPoint(x, y);
                        break;
                }
            }
        }
        return parts[1].chars()
                .mapToObj(c -> (char) c)
                .filter(c -> c != '\n')
                .map(DIRECTIONS::get);
    }

    @Override
    public Object part1(Stream<IntPoint> input) {
        input.filter(dir -> push(position, dir))
                .forEach(dir -> position = position.add(dir));
        return boxes.stream().mapToInt(p -> p.x() + p.y() * 100).sum();
    }

    private boolean push(IntPoint from, IntPoint direction) {
        var to = from.add(direction);
        if (walls[to.y()][to.x()]) {
            return false;
        }
        if (boxes.contains(to)) {
            var free = push(to, direction);
            if (!free) {
                return false;
            }
        }
        if (boxes.remove(from)) {
            boxes.add(to);
        }
        return true;
    }

    @Override
    public Object part2(Stream<IntPoint> input) {
        var newMap = new boolean[walls.length][];

        for (int y = 0; y < walls.length; y++) {
            newMap[y] = new boolean[walls[y].length * 2];
            for (int x = 0; x < walls[y].length; x++) {
                newMap[y][x * 2] = newMap[y][x * 2 + 1] = walls[y][x];
            }
        }

        walls = newMap;
        position = new IntPoint(position.x() * 2, position.y());
        boxes = boxes.stream()
                .map(p -> new IntPoint(p.x() * 2, p.y()))
                .collect(Collectors.toSet());

        input.filter(dir -> push2(position, dir))
                .forEach(dir -> position = position.add(dir));

        return boxes.stream().mapToInt(p -> p.x() + p.y() * 100).sum();
    }

    private boolean push2(IntPoint from, IntPoint direction) {
        Set<IntPoint> slide = new HashSet<>();
        LinkedHashSet<IntPoint> check = new LinkedHashSet<>();

        if (direction.x() == 0) {
            check.add(from.add(direction));
            if (boxes.contains(from.add(-1, direction.y()))) {
                check.add(from.add(-1, direction.y()));
            }
        } else if (direction.x() == -1) {
            if (boxes.contains(from.add(-2, 0))) {
                check.add(from.add(-2, 0));
            } else {
                check.add(from.add(-1, 0));
            }
        } else {
            check.add(from.add(1, 0));
        }

        while (!check.isEmpty()) {
            var p = check.removeFirst();
            if (walls[p.y()][p.x()]) {
                return false;
            }
            if (boxes.contains(p)) {
                if (direction.x() == 0) {
                    if (boxes.contains(p.add(-1, direction.y()))) {
                        check.add(p.add(-1, direction.y()));
                    }
                    check.add(p.add(direction));
                    check.add(p.add(1, direction.y()));
                } else if (direction.x() == -1) {
                    if (boxes.contains(p.add(-2, 0))) {
                        check.add(p.add(-2, 0));
                    } else {
                        check.add(p.add(-1, 0));
                    }
                } else {
                    check.add(p.add(direction.x() * 2, 0));
                }
                slide.add(p);
            }
        }

        boxes.removeAll(slide);
        for (IntPoint p : slide) {
            boxes.add(p.add(direction));
        }
        return true;
    }

}
