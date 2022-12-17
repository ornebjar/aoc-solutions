package se.pabi.aoc.year2022;

import se.pabi.aoc.AdventOfCode;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Day14 extends AdventOfCode<Day14.Cave> {

    record Cave(Set<Point> stones) {
        void drop(int abyss, Point stone) {
            while (stone.y < abyss) {
                stone.y++;
                if (stones.contains(stone)) {
                    stone.x--;
                    if (stones.contains(stone)) {
                        stone.x += 2;
                        if (stones.contains(stone)) {
                            stone.x--;
                            stone.y--;
                            stones.add(stone);
                            return;
                        }
                    }
                }
            }
        }

        void drop(Point stone, int floor) {
            while (true) {
                if (stone.y + 1 == floor) {
                    stones.add(stone);
                    return;
                }
                stone.y++;
                if (stones.contains(stone)) {
                    stone.x--;
                    if (stones.contains(stone)) {
                        stone.x += 2;
                        if (stones.contains(stone)) {
                            stone.x--;
                            stone.y--;
                            stones.add(stone);
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public Cave input(String input) {
        Set<Point> stones = new HashSet<>();
        input.lines().forEach(line -> {
            //noinspection ResultOfMethodCallIgnored
            Arrays.stream(line.split(" -> ")).reduce((a, b) -> {
                int[] from = Arrays.stream(a.split(",")).mapToInt(Integer::parseInt).toArray();
                int[] to = Arrays.stream(b.split(",")).mapToInt(Integer::parseInt).toArray();
                int sx = Math.min(from[0], to[0]);
                int sy = Math.min(from[1], to[1]);
                int ex = Math.max(from[0], to[0]);
                int ey = Math.max(from[1], to[1]);
                for (int y = sy; y <= ey; y++) {
                    for (int x = sx; x <= ex; x++) {
                        stones.add(new Point(x, y));
                    }
                }
                return b;
            });
        });
        return new Cave(stones);
    }

    @Override
    public Object part1(Cave cave) {
        int abyss = cave.stones.stream().mapToInt(p -> p.y).max().orElse(0);
        int first = cave.stones.stream().filter(p -> p.x == 500).mapToInt(p -> p.y).min().orElse(abyss);
        int count = -1;
        int sx = 500;
        int sy = first - 1;
        Point stone;
        do {
            stone = new Point(sx, sy);
            cave.drop(abyss, stone);
            if (stone.x == sx && stone.y == sy) {
                sy--;
            }
            count++;
        } while (stone.y < abyss);

        return count;
    }

    @Override
    public Object part2(Cave cave) {
        int floor = cave.stones.stream().mapToInt(p -> p.y).max().orElse(0) + 2;
        int count = 0;
        int sx = 500;
        int sy = 0;
        Point stone;
        do {
            stone = new Point(sx, sy);
            cave.drop(stone, floor);
            count++;
        } while (stone.y > 0);

        return count;
    }

    public static void main(String[] args) {
        new Day14();
    }
}
