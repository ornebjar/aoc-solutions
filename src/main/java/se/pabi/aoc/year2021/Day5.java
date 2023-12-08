package se.pabi.aoc.year2021;

import se.pabi.aoc.base.AdventOfCode;

import java.awt.*;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day5 extends AdventOfCode<Stream<String>> {

    @Override
    public Stream<String> input(String input) {
        return input.lines();
    }

    @Override
    public Object part1(Stream<String> input) {
        Stream<Point> points = input.flatMap(line -> {
            String[] ends = line.split(" -> ");
            String[] p1 = ends[0].split(",");
            String[] p2 = ends[1].split(",");
            int x1 = Integer.parseInt(p1[0]);
            int y1 = Integer.parseInt(p1[1]);
            int x2 = Integer.parseInt(p2[0]);
            int y2 = Integer.parseInt(p2[1]);

            Stream<Integer> xs = IntStream.rangeClosed(Math.min(x1, x2), Math.max(x1, x2)).boxed();
            Stream<Integer> ys = IntStream.rangeClosed(Math.min(y1, y2), Math.max(y1, y2)).boxed();

            if (x1 == x2) {
                return ys.map(y -> new Point(x1, y));
            }
            if (y1 == y2) {
                return xs.map(x -> new Point(x, y1));
            }
            return Stream.of();
        });
        Map<Point, Long> collect = points.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return collect
                .entrySet().stream().filter(e -> e.getValue() >= 2).count();
    }

    @Override
    public Object part2(Stream<String> input) {
        Stream<Point> points = input.flatMap(line -> {
            String[] ends = line.split(" -> ");
            String[] p1 = ends[0].split(",");
            String[] p2 = ends[1].split(",");
            int x1 = Integer.parseInt(p1[0]);
            int y1 = Integer.parseInt(p1[1]);
            int x2 = Integer.parseInt(p2[0]);
            int y2 = Integer.parseInt(p2[1]);

            Stream<Integer> xs = x1 > x2
                    ? IntStream.rangeClosed(x2, x1).map(i -> x1 - i + x2).boxed()
                    : IntStream.rangeClosed(x1, x2).boxed();
            Stream<Integer> ys = y1 > y2
                    ? IntStream.rangeClosed(y2, y1).map(i -> y1 - i + y2).boxed()
                    : IntStream.rangeClosed(y1, y2).boxed();

            if (x1 == x2) {
                return ys.map(y -> new Point(x1, y));
            }
            if (y1 == y2) {
                return xs.map(x -> new Point(x, y1));
            }
            Iterator<Integer> y = ys.iterator();
            return xs.map(x -> new Point(x, y.next()));
        });
        Map<Point, Long> collect = points.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return collect
                .entrySet().stream().filter(e -> e.getValue() >= 2).count();
    }

}
