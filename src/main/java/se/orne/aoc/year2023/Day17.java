package se.orne.aoc.year2023;

import se.orne.aoc.AdventOfCode;

import java.util.*;

public class Day17 extends AdventOfCode<int[][]> {
    @Override
    public int[][] input(String input) {
        return input.lines().map(s -> {
            int[] row = new int[s.length()];
            for (int i = 0; i < s.length(); i++) {
                row[i] = s.charAt(i) - '0';
            }
            return row;
        }).toArray(int[][]::new);
    }

    private record Pos(int x, int y) {
        int dir(Pos  from) {
            int dx = x - from.x;
            int dy = y - from.y;
            return dx == 0 ? dy == 1 ? 1 : 3 : dx == 1 ? 0 : 2;
        }
        Pos move(int dir) {
            return switch (dir) {
                case 0 -> new Pos(x + 1, y);
                case 1 -> new Pos(x, y + 1);
                case 2 -> new Pos(x - 1, y);
                case 3 -> new Pos(x, y - 1);
                default -> throw new IllegalArgumentException("Unknown direction " + dir);
            };
        }
    }

    private record Node(Pos p, int s, int d, int g, int h, Node from) {
        Node(Pos p, int g, int h, Node from) {
            this(p, p.dir(from.p), g, h, from);
        }
        Node(Pos p, int d, int g, int h, Node from) {
            this(p, d == from.d ? from.s + 1 : 1, d, g, h, from);
        }
        int f() {
            return g + h;
        }
        int key() {
            return s * 10 + d;
        }
    }

    @Override
    public Object part1(int[][] input) {
        Node goal = bestPath(input, 1, 3);
        print(input, goal);
        return goal.g;
    }

    @Override
    public Object part2(int[][] input) {
        Node goal = bestPath(input, 4, 10);
        print(input, goal);
        return goal.g;
    }

    private static void print(int[][] input, Node goal) {
        var path = new HashSet<Pos>();
        for (var current = goal; current != null; current = current.from) {
            path.add(current.p);
        }
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length; x++) {
                System.out.print(!path.contains(new Pos(x, y)) ? "." : input[y][x]);
            }
            System.out.println();
        }
    }

    static Node bestPath(int[][] input, int minLength, int maxLength) {
        var open = new PriorityQueue<>(Comparator.comparing(Node::f));
        var closed = new HashMap<Pos, Map<Integer, Node>>();
        Pos goal = new Pos(input.length - 1, input[0].length - 1);
        Node start = new Node(new Pos(0, 0), 0, -1, 0, goal.x + goal.y, null);
        open.add(new Node(new Pos(1, 0), 1, 0, input[0][1], goal.x - 1 + goal.y, start));
        open.add(new Node(new Pos(0, 1), 1, 1, input[1][0], goal.x + goal.y - 1, start));
        while (!open.isEmpty()) {
            var current = open.poll();
            var dirMap = closed.computeIfAbsent(current.p, _ -> new HashMap<>());
            var key = current.key();
            var closedPrev = dirMap.get(key);
            if (goal.equals(current.p)) {
                if (current.s >= minLength) {
                    return current;
                }
                continue;
            }
            if (closedPrev != null && closedPrev.g <= current.g) {
                continue;
            }
            dirMap.put(key, current);
            for (int r = -1; r <= 1; r++) {
                var n = current.p.move((current.d + 4 + r) % 4);
                if (n.x >= 0 && n.y >= 0 && n.x <= goal.x && n.y <= goal.y) {
                    Node node = new Node(
                            n,
                            current.g + input[n.y][n.x],
                            goal.x - n.x + goal.y - n.y,
                            current
                    );
                    if (node.s <= maxLength && (node.s > 1 || current.s >= minLength)) {
                        open.add(node);
                    }
                }
            }
        }
        throw new IllegalStateException("No path found");
    }

}
