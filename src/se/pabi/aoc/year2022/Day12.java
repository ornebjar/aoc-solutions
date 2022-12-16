package se.pabi.aoc.year2022;

import se.pabi.aoc.AdventOfCode;

import java.util.*;

import static java.lang.Math.abs;

public class Day12 extends AdventOfCode {

    static final class Pos {
        private final int x;
        private final int y;
        private final char height;
        private final List<Pos> n;

        Pos(int x, int y, char height, List<Pos> n) {
            this.x = x;
            this.y = y;
            this.height = height;
            this.n = n;
        }

        Pos(int x, int y, char height) {
            this(x, y, switch (height) {
                case 'S' -> 'a';
                case 'E' -> 'z';
                default -> height;
            }, new ArrayList<>());
        }

        private void addN(int nx, int ny) {
            if (nx >= 0 && nx < w &&
                    ny >= 0 && ny < h &&
                    height + 1 >= map[ny][nx].height) {
                n.add(map[ny][nx]);
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Pos) obj;
            return this.x == that.x &&
                    this.y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Pos[" +
                    "x=" + x + ", " +
                    "y=" + y + ", " +
                    "height=" + height + ", " +
                    "n=" + n.size() + ']';
        }

    }

    static Pos[][] map;
    static int w, h;
    static Pos start, goal;

    @Override
    public void input(String input) {
        var lines = input.lines().toArray(String[]::new);
        h = lines.length;
        w = lines[0].length();
        map = new Pos[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                char c = lines[y].charAt(x);
                map[y][x] = new Pos(x, y, c);
                switch (c) {
                    case 'S' -> start = map[y][x];
                    case 'E' -> goal = map[y][x];
                }
            }
        }
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                map[y][x].addN(x + 1, y);
                map[y][x].addN(x - 1, y);
                map[y][x].addN(x, y + 1);
                map[y][x].addN(x, y - 1);
            }
        }
    }

    @Override
    public String part1() {
        return String.valueOf(shortestPath(start, goal));
    }

    @Override
    public String part2() {
        return "" + Arrays.stream(map)
                .flatMap(Arrays::stream)
                .filter(p -> p.height == 'a')
                .mapToInt(p -> shortestPath(p, goal))
                .min()
                .orElse(-1);
    }

    private int shortestPath(Pos start, Pos goal) {
        int[][] fScore = new int[h][w];
        int[][] gScore = new int[h][w];
        for (int y = 0; y < h; y++) {
            Arrays.fill(fScore[y], Integer.MAX_VALUE);
            Arrays.fill(gScore[y], Integer.MAX_VALUE);
        }
        var open = new PriorityQueue<Pos>(Comparator.comparingInt(p -> fScore[p.y][p.x]));
        open.add(start);
        gScore[start.y][start.x] = 0;
        fScore[start.y][start.x] = abs(start.x - goal.x) + abs(start.y - goal.y);
        Pos current;
        while ((current = open.poll()) != null) {
            if (current == goal) {
                return gScore[current.y][current.x];
            }
            for (Pos n : current.n) {
                int tentative_gScore = gScore[current.y][current.x] + 1;
                if (tentative_gScore < gScore[n.y][n.x]) {
                    gScore[n.y][n.x] = tentative_gScore;
                    fScore[n.y][n.x] = tentative_gScore + abs(goal.x - n.x) + abs(goal.y - n.y);
                    if (!open.contains(n)) {
                        open.add(n);
                    }
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    public static void main(String[] args) {
        new Day12();
    }
}
