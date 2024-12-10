package se.pabi.aoc.year2024;

import se.phet.aoc.AdventOfCode;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day6 extends AdventOfCode<char[][]> {


    @Override
    public char[][] input(String input) {
//        input = """
//                ....#.....
//                .........#
//                ..........
//                ..#.......
//                .......#..
//                ..........
//                .#..^.....
//                ........#.
//                #.........
//                ......#...
//                """;
        return input.lines()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    record Point(int x, int y) {
        Point add(Point other) {
            return new Point(x + other.x, y + other.y);
        }
        Point rot90() {
            return new Point(-y, x);
        }
    }

    @Override
    public Object part1(char[][] input) {

        Point pos = findStart(input);
        Point dir = getDir(input[pos.y()][pos.x()]);

        Set<Point> visited = new HashSet();

        while (inside(input, pos)) {
            visited.add(pos);
            Point nextPos = new Point(pos.x() + dir.x(), pos.y() + dir.y());
            if (inside(input, nextPos) && input[nextPos.y()][nextPos.x()] == '#') {
                dir = dir.rot90();
            } else {
                pos = nextPos;
            }
        }

        return visited.size();
    }

    private boolean inside(char[][] input, Point pos) {
        return pos.x() >= 0 && pos.x() < input[0].length && pos.y() >= 0 && pos.y() < input.length;
    }

    private Point findStart(char[][] input) {
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[0].length; x++) {
                try {
                    getDir(input[y][x]);
                    return new Point(x, y);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        throw new IllegalArgumentException("No start found");
    }

    private static Point getDir(char c) {
        return switch (c) {
            case '^' -> new Point(0, -1);
            case 'v' -> new Point(0, 1);
            case '<' -> new Point(-1, 0);
            case '>' -> new Point(1, 0);
            default -> throw new IllegalArgumentException("Invalid direction: " + c);
        };
    }

    @Override
    public Object part2(char[][] input) {
        Point start = findStart(input);
        Point startDir = getDir(input[start.y()][start.x()]);


        int count = 0;

        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[0].length; x++) {
                if (input[y][x] != '.') {
                    continue;
                }
                input[y][x] = '#';
                if (looping(input, start, startDir)) {
                    count++;
                }
                input[y][x] = '.';
            }
        }


        return count;
    }

    private boolean looping(char[][] input, Point start, Point startDir) {
        Map<Point, Set<Point>> visited = Map.of(
                new Point(0, 1), new HashSet<>(),
                new Point(1, 0), new HashSet<>(),
                new Point(0, -1), new HashSet<>(),
                new Point(-1, 0), new HashSet<>()
        );
        Point pos = new Point(start.x(), start.y());
        Point dir = new Point(startDir.x(), startDir.y());
        while (inside(input, pos)) {
            if (!visited.get(dir).add(pos)) {
                return true;
            }
            Point nextPos = new Point(pos.x() + dir.x(), pos.y() + dir.y());
            if (inside(input, nextPos) && input[nextPos.y()][nextPos.x()] == '#') {
                dir = dir.rot90();
            } else {
                pos = nextPos;
            }
        }
        return false;
    }
}
