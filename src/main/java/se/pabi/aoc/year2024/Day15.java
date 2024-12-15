package se.pabi.aoc.year2024;

import se.phet.aoc.AdventOfCode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day15 extends AdventOfCode<Stream<Day15.Point>> {

    private static final Map<Character, Point> DIRECTIONS = Map.of(
            '^', new Point(0, -1),
            'v', new Point(0, 1),
            '<', new Point(-1, 0),
            '>', new Point(1, 0)
    );

    private boolean[][] walls;
    private Point position;
    private Set<Point> boxes;

    public record Point(int x, int y) {
        public Point add(int dx, int dy) {
            return new Point(x + dx, y + dy);
        }

        public Point add(Point other) {
            return add(other.x, other.y);
        }
    }

    @Override
    public Stream<Point> input(String input) {
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
                        boxes.add(new Point(x, y));
                        break;
                    case '@':
                        position = new Point(x, y);
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
    public Object part1(Stream<Point> input) {
        input.filter(dir -> push(position, dir))
                .forEach(dir -> position = position.add(dir));
        return boxes.stream().mapToInt(p -> p.x + p.y * 100).sum();
    }

    private boolean push(Point from, Point direction) {
        var to = from.add(direction);
        if (walls[to.y][to.x]) {
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
    public Object part2(Stream<Point> input) {
        var newMap = new boolean[walls.length][];

        for (int y = 0; y < walls.length; y++) {
            newMap[y] = new boolean[walls[y].length * 2];
            for (int x = 0; x < walls[y].length; x++) {
                newMap[y][x * 2] = newMap[y][x * 2 + 1] = walls[y][x];
            }
        }

        walls = newMap;
        position = new Point(position.x * 2, position.y);
        boxes = boxes.stream()
                .map(p -> new Point(p.x * 2, p.y))
                .collect(Collectors.toSet());

        input.filter(dir -> push2(position, dir))
                .forEach(dir -> position = position.add(dir));

        return boxes.stream().mapToInt(p -> p.x + p.y * 100).sum();
    }

    private boolean push2(Point from, Point direction) {
        Set<Point> slide = new HashSet<>();
        LinkedHashSet<Point> check = new LinkedHashSet<>();

        if (direction.x == 0) {
            check.add(from.add(direction));
            if (boxes.contains(from.add(-1, direction.y))) {
                check.add(from.add(-1, direction.y));
            }
        } else if (direction.x == -1) {
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
            if (walls[p.y][p.x]) {
                return false;
            }
            if (boxes.contains(p)) {
                if (direction.x == 0) {
                    if (boxes.contains(p.add(-1, direction.y))) {
                        check.add(p.add(-1, direction.y));
                    }
                    check.add(p.add(direction));
                    check.add(p.add(1, direction.y));
                } else if (direction.x == -1) {
                    if (boxes.contains(p.add(-2, 0))) {
                        check.add(p.add(-2, 0));
                    } else {
                        check.add(p.add(-1, 0));
                    }
                } else {
                    check.add(p.add(direction.x * 2, 0));
                }
                slide.add(p);
            }
        }

        boxes.removeAll(slide);
        for (Point p : slide) {
            boxes.add(p.add(direction));
        }
        return true;
    }

}
