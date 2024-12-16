package se.pabi.aoc.year2024;

import se.pabi.aoc.util.IntPoint;
import se.phet.aoc.AdventOfCode;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day6 extends AdventOfCode<char[][]> {

    @Override
    public char[][] input(String input) {
        return input.lines()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    @Override
    public Object part1(char[][] input) {

        IntPoint pos = findStart(input);
        IntPoint dir = getDir(input[pos.y()][pos.x()]);

        Set<IntPoint> visited = new HashSet<>();

        while (inside(input, pos)) {
            visited.add(pos);
            IntPoint nextPos = new IntPoint(pos.x() + dir.x(), pos.y() + dir.y());
            if (inside(input, nextPos) && input[nextPos.y()][nextPos.x()] == '#') {
                dir = dir.rotate90();
            } else {
                pos = nextPos;
            }
        }

        return visited.size();
    }

    private boolean inside(char[][] input, IntPoint pos) {
        return pos.x() >= 0 && pos.x() < input[0].length && pos.y() >= 0 && pos.y() < input.length;
    }

    private IntPoint findStart(char[][] input) {
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[0].length; x++) {
                try {
                    getDir(input[y][x]);
                    return new IntPoint(x, y);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        throw new IllegalArgumentException("No start found");
    }

    private static IntPoint getDir(char c) {
        return switch (c) {
            case '^' -> new IntPoint(0, -1);
            case 'v' -> new IntPoint(0, 1);
            case '<' -> new IntPoint(-1, 0);
            case '>' -> new IntPoint(1, 0);
            default -> throw new IllegalArgumentException("Invalid direction: " + c);
        };
    }

    @Override
    public Object part2(char[][] input) {
        IntPoint start = findStart(input);
        IntPoint startDir = getDir(input[start.y()][start.x()]);


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

    private boolean looping(char[][] input, IntPoint start, IntPoint startDir) {
        Map<IntPoint, Set<IntPoint>> visited = Map.of(
                new IntPoint(0, 1), new HashSet<>(),
                new IntPoint(1, 0), new HashSet<>(),
                new IntPoint(0, -1), new HashSet<>(),
                new IntPoint(-1, 0), new HashSet<>()
        );
        IntPoint pos = new IntPoint(start.x(), start.y());
        IntPoint dir = new IntPoint(startDir.x(), startDir.y());
        while (inside(input, pos)) {
            if (!visited.get(dir).add(pos)) {
                return true;
            }
            IntPoint nextPos = new IntPoint(pos.x() + dir.x(), pos.y() + dir.y());
            if (inside(input, nextPos) && input[nextPos.y()][nextPos.x()] == '#') {
                dir = dir.rotate90();
            } else {
                pos = nextPos;
            }
        }
        return false;
    }
}
