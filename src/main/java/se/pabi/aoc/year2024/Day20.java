package se.pabi.aoc.year2024;

import se.pabi.aoc.util.IntPoint;
import se.phet.aoc.AdventOfCode;

import java.util.*;

import static java.util.Comparator.comparingInt;

public class Day20 extends AdventOfCode<boolean[][]> {

    private IntPoint start;
    private IntPoint end;

    private int width;
    private int height;

    private Node[][] cache;

    private record Node(IntPoint point, Node next, int distance) {
        @Override
        public String toString() {
            return "Node{" +
                    "point=" + point +
                    ", next=" + (next == null ? null : next.point) +
                    ", distance=" + distance +
                    '}';
        }
    }

    private static final IntPoint[] DIRECTIONS = new IntPoint[] {
            new IntPoint(0, 1),
            new IntPoint(0, -1),
            new IntPoint(1, 0),
            new IntPoint(-1, 0)
    };

    @Override
    public boolean[][] input(String input) {
        char[][] map = input.lines().map(String::toCharArray).toArray(char[][]::new);
        height = map.length;
        width = map[0].length;
        cache = new Node[height][];
        boolean[][] grid = new boolean[height][];
        for (int y = 0; y < height; y++) {
            cache[y] = new Node[width];
            grid[y] = new boolean[width];
            for (int x = 0; x < width; x++) {
                switch (map[y][x]) {
                    case '#':
                        grid[y][x] = true;
                        break;
                    case 'S':
                        start = new IntPoint(x, y);
                        break;
                    case 'E':
                        end = new IntPoint(x, y);
                        break;
                }
            }
        }
        return grid;
    }

    private Node path(boolean[][] input) {
        var queue = new PriorityQueue<>(comparingInt(Node::distance));
        queue.add(new Node(end, null, 0));
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            Node cached = cache[node.point.y()][node.point.x()];
            if (cached != null) {
                continue;
            }
            cache[node.point.y()][node.point.x()] = node;
            if (node.point.equals(start)) {
                continue;
            }
            for (IntPoint direction : DIRECTIONS) {
                IntPoint next = node.point.add(direction);
                if (oob(next)) {
                    continue;
                }
                if (input[next.y()][next.x()]) {
                    continue;
                }
                queue.add(new Node(next, node, node.distance + 1));
            }
        }
        return cache[start.y()][start.x()];
    }

    private boolean oob(IntPoint next) {
        return next.x() < 0 || next.x() >= width || next.y() < 0 || next.y() >= height;
    }

    private int better(boolean[][] input, int cheats) {
        Node node = path(input);
        int cheated = 0;
        while (node != null) {
            for (int y = -cheats; y <= cheats; y++) {
                for (int x = -cheats; x <= cheats; x++) {
                    int cheatDistance = Math.abs(x) + Math.abs(y);
                    if (cheatDistance > cheats) {
                        continue;
                    }
                    var point = node.point.add(x, y);
                    if (oob(point)) {
                        continue;
                    }
                    var cached = cache[point.y()][point.x()];
                    if (cached == null) {
                        continue;
                    }
                    var improvement = node.distance - cached.distance - cheatDistance;
                    if (improvement >= 100) {
                        cheated++;
                    }
                }
            }
            node = node.next();
        }
        return cheated;
    }

    @Override
    public Object part1(boolean[][] input) {
        return better(input, 2);
    }

    @Override
    public Object part2(boolean[][] input) {
        return better(input, 20);
    }

}
