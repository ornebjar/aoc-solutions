package se.pabi.aoc.year2024;

import se.pabi.aoc.util.IntPoint;
import se.phet.aoc.AdventOfCode;

import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day18 extends AdventOfCode<Stream<IntPoint>> {

    @Override
    public Stream<IntPoint> input(String input) {
        return input.lines().map(IntPoint::parse);
    }

    private static final int SIZE = 71;

    private static final IntPoint START = new IntPoint(0, 0);
    private static final IntPoint END = new IntPoint(SIZE - 1, SIZE - 1);

    @Override
    public Object part1(Stream<IntPoint> input) {
        Set<IntPoint> walls = input.limit(1024).collect(Collectors.toSet());
        boolean[][] grid = new boolean[SIZE][SIZE];
        walls.forEach(w -> grid[w.x()][w.y()] = true);
        return path(grid);
    }

    @Override
    public Object part2(Stream<IntPoint> input) {
        boolean[][] grid = new boolean[SIZE][SIZE];
        return input.filter(wall -> {
                    grid[wall.x()][wall.y()] = true;
                    return path(grid) == -1;
                })
                .findFirst()
                .map(wall -> wall.x() + "," + wall.y())
                .orElse("No solution");
    }

    private record Node(IntPoint point, int distance) implements Comparable<Node> {
        @Override
        public int compareTo(Node o) {
            return Integer.compare(distance, o.distance);
        }
    }

    private static final Set<IntPoint> DIRECTIONS = Set.of(
            new IntPoint(0, 1),
            new IntPoint(0, -1),
            new IntPoint(1, 0),
            new IntPoint(-1, 0)
    );

    private static int path(boolean[][] grid) {
        boolean[][] visited = new boolean[SIZE][SIZE];
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(START, 0));
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node.point.equals(END)) {
                return node.distance;
            }
            if (visited[node.point.x()][node.point.y()]) {
                continue;
            }
            visited[node.point.x()][node.point.y()] = true;
            for (IntPoint dir : DIRECTIONS) {
                IntPoint next = node.point.add(dir);
                if (next.x() < 0 || next.x() >= SIZE || next.y() < 0 || next.y() >= SIZE || grid[next.x()][next.y()]) {
                    continue;
                }
                queue.add(new Node(next, node.distance + 1));
            }
        }
        return -1;
    }

}
