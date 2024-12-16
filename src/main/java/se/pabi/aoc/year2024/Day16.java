package se.pabi.aoc.year2024;

import se.pabi.aoc.util.IntPoint;
import se.phet.aoc.AdventOfCode;

import java.util.*;

public class Day16 extends AdventOfCode<boolean[][]> {

    private IntPoint start;
    private IntPoint end;

    @Override
    public boolean[][] input(String input) {
        var lines = input.lines().toArray(String[]::new);
        var map = new boolean[lines.length][];
        for (int y = 0; y < lines.length; y++) {
            map[y] = new boolean[lines[y].length()];
            for (int x = 0; x < lines[y].length(); x++) {
                switch (lines[y].charAt(x)) {
                    case '#' -> map[y][x] = true;
                    case 'S' -> start = new IntPoint(x, y);
                    case 'E' -> end = new IntPoint(x, y);
                }
            }
        }
        return map;
    }

    private static final Map<IntPoint, Integer> DIRECTIONS = Map.of(
            new IntPoint(0, 1), 0,
            new IntPoint(1, 0), 1,
            new IntPoint(0, -1), 2,
            new IntPoint(-1, 0), 3
    );

    private record Node(
            IntPoint position,
            IntPoint direction,
            int distance,
            int heuristic
    ) implements Comparable<Node> {

        @Override
        public int compareTo(Node o) {
            return Integer.compare(distance + heuristic, o.distance + o.heuristic);
        }
    }

    private static int signum(int x) {
        return Integer.compare(x, 0);
    }

    private int heuristic(IntPoint position, IntPoint direction) {
        int h = 2000;
        int xd = end.x() - position.x();
        int yd = end.y() - position.y();

        if (signum(xd) == direction.x() || xd == 0) {
            h -= 1_000;
        }
        if (signum(yd) == direction.y() || yd == 0) {
            h -= 1_000;
        }

        return h + Math.abs(xd) + Math.abs(yd);
    }

    @Override
    public Object part1(boolean[][] walls) {
        var visited = new int[4][walls.length][];
        search(walls, visited);

        return DIRECTIONS.keySet().stream().mapToInt(d -> {
            var from = end.add(d);
            var reverse = new IntPoint(-d.x(), -d.y());
            return visited[DIRECTIONS.get(reverse)][from.y()][from.x()];
        }).min().orElse(-1);
    }

    @Override
    public Object part2(boolean[][] walls) {
        var visited = new int[4][walls.length][];
        search(walls, visited);

        Set<IntPoint> benches = new HashSet<>();
        LinkedHashMap<IntPoint, IntPoint> open = new LinkedHashMap<>();
        open.put(end, new IntPoint(0, 0));

        while (!open.isEmpty()) {
            var entry = open.pollFirstEntry();
            var next = entry.getKey();
            var fromDirection = entry.getValue();
            benches.add(next);
            if (next.equals(start)) {
                continue;
            }
            var visit = DIRECTIONS.keySet().stream()
                    .map(direction -> {
                        var from = next.add(direction);
                        var reverse = new IntPoint(-direction.x(), -direction.y());
                        var distance = visited[DIRECTIONS.get(reverse)][from.y()][from.x()];
                        return new Node(from, reverse, distance, reverse.equals(fromDirection) ? 0 : 1_000);
                    }).sorted()
                    .toList();
            var min = visit.getFirst();
            for (var node : visit) {
                if (min.compareTo(node) != 0) {
                    break;
                }
                open.put(node.position(), node.direction());
            }
        }

        return benches.size();
    }

    private void search(boolean[][] walls, int[][][] visited) {
        var queue = new PriorityQueue<Node>();

        for (int d = 0; d < visited.length; d++) {
            for (int y = 0; y < walls.length; y++) {
                visited[d][y] = new int[walls[y].length];
                for (int x = 0; x < walls[y].length; x++) {
                    visited[d][y][x] = 1_000_000_000;
                }
            }
        }

        queue.add(new Node(
                start,
                new IntPoint(1, 0),
                0,
                heuristic(start, new IntPoint(1, 0))
        ));

        while (!queue.isEmpty()) {
            var node = queue.poll();
            for (var entry : DIRECTIONS.entrySet()) {
                var direction = entry.getKey();
                var directionIndex = entry.getValue();
                if (node.direction().x() == -direction.x() && node.direction().y() == -direction.y()) {
                    continue;
                }
                var next = node.position().add(direction);
                if (walls[next.y()][next.x()]) {
                    continue;
                }
                int distance = node.distance() + 1 + (node.direction().equals(direction) ? 0 : 1_000);
                if (visited[directionIndex][node.position().y()][node.position().x()] < distance) {
                    continue;
                }
                visited[directionIndex][node.position().y()][node.position().x()] = distance;
                queue.add(new Node(
                        next,
                        direction,
                        distance,
                        heuristic(next, direction)
                ));
            }
        }
    }

}
