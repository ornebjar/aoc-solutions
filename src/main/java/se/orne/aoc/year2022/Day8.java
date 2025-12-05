package se.orne.aoc.year2022;

import se.orne.aoc.AdventOfCode;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day8 extends AdventOfCode<String[]> {

    @Override
    public String[] input(String input) {
        return input.lines().toArray(String[]::new);
    }

    @Override
    public Object part1(String[] trees) {
        int h = trees.length;
        int w = trees[0].length();
        Set<Point> visible = new HashSet<>();
        Stream.of(
                IntStream.range(0, h).mapToObj(y -> IntStream.range(0, w).mapToObj(x -> new Point(x, y)).toList()),
                IntStream.range(0, w).mapToObj(x -> IntStream.range(0, h).mapToObj(y -> new Point(x, y)).toList()),
                IntStream.range(0, h).mapToObj(y -> IntStream.range(0, w).mapToObj(x -> new Point(w - x - 1, y)).toList()),
                IntStream.range(0, w).mapToObj(x -> IntStream.range(0, h).mapToObj(y -> new Point(x, h - y - 1)).toList())
        ).flatMap(s -> s).forEach(line -> {
            char high = '0' - 1;
            for (Point p : line) {
                char c = trees[p.y].charAt(p.x);
                if (c > high) {
                    visible.add(p);
                    if (c == '9') {
                        break;
                    }
                    high = c;
                }
            }
        });
        return visible.size();
    }

    @Override
    public Object part2(String[] trees) {
        int h = trees.length;
        int w = trees[0].length();
        long high = 0;
        for (int y = 1; y < h-1; y++) {
            for (int x = 1; x < w-1; x++) {
                var me = trees[y].charAt(x);
                long value = Stream.of(
                        Stream.iterate(new Point(x-1, y), p -> p.x >= 0, p -> {
                            if (trees[p.y].charAt(p.x) >= me) {
                                p.x = 0;
                            }
                            p.x--;
                            return p;
                        }),
                        Stream.iterate(new Point(x+1, y), p -> p.x < w, p -> {
                            if (trees[p.y].charAt(p.x) >= me) {
                                p.x = w;
                            }
                            p.x++;
                            return p;
                        }),
                        Stream.iterate(new Point(x, y-1), p -> p.y >= 0, p -> {
                            if (trees[p.y].charAt(p.x) >= me) {
                                p.y = 0;
                            }
                            p.y--;
                            return p;
                        }),
                        Stream.iterate(new Point(x, y+1), p -> p.y < h, p -> {
                            if (trees[p.y].charAt(p.x) >= me) {
                                p.y = h;
                            }
                            p.y++;
                            return p;
                        })
                ).mapToLong(Stream::count).reduce(1, (a, b) -> a * b);
                high = Math.max(high, value);
            }
        }
        return high;
    }

}
