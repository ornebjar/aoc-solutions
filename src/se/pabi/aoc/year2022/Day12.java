package se.pabi.aoc.year2022;

import se.pabi.aoc.AdventOfCode;

import java.util.*;

import static java.lang.Math.abs;

public class Day12 extends AdventOfCode<Day12.River> {

    record River(Pos[][] map, int w, int h, Pos start, Pos goal) {
    }

    static class Pos {
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

        public Pos(int x, int y, char height) {
            this(x, y, switch (height) {
                case 'S' -> 'a';
                case 'E' -> 'z';
                default -> height;
            }, new ArrayList<>());
        }

        private void addN(int nx, int ny, Pos[][] map) {
            if (ny >= 0 && ny < map.length &&
                    nx >= 0 && nx < map[ny].length &&
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



    @Override
    public River input(String input) {
        var lines = input.lines().toArray(String[]::new);
        int h = lines.length;
        int w = lines[0].length();
        var map = new Pos[h][w];
        Pos start = null;
        Pos goal = null;
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
                map[y][x].addN(x + 1, y, map);
                map[y][x].addN(x - 1, y, map);
                map[y][x].addN(x, y + 1, map);
                map[y][x].addN(x, y - 1, map);
            }
        }
        return new River(map, w, h, start, goal);
    }

    @Override
    public String part1(River river) {
        return String.valueOf(shortestPath(river, river.start));
    }

    @Override
    public String part2(River river) {
        return "" + Arrays.stream(river.map)
                .flatMap(Arrays::stream)
                .filter(p -> p.height == 'a')
                .mapToInt(p -> shortestPath(river, p))
                .min()
                .orElse(-1);
    }

    private int shortestPath(River river, Pos start) {
        int[][] fScore = new int[river.h][river.w];
        int[][] gScore = new int[river.h][river.w];
        for (int y = 0; y < river.h; y++) {
            Arrays.fill(fScore[y], Integer.MAX_VALUE);
            Arrays.fill(gScore[y], Integer.MAX_VALUE);
        }
        var open = new PriorityQueue<Pos>(Comparator.comparingInt(p -> fScore[p.y][p.x]));
        open.add(start);
        gScore[start.y][start.x] = 0;
        fScore[start.y][start.x] = abs(start.x - river.goal.x) + abs(start.y - river.goal.y);
        Pos current;
        while ((current = open.poll()) != null) {
            if (current == river.goal) {
                return gScore[current.y][current.x];
            }
            for (Pos n : current.n) {
                int tentative_gScore = gScore[current.y][current.x] + 1;
                if (tentative_gScore < gScore[n.y][n.x]) {
                    gScore[n.y][n.x] = tentative_gScore;
                    fScore[n.y][n.x] = tentative_gScore + abs(river.goal.x - n.x) + abs(river.goal.y - n.y);
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
