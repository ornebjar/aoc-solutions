package se.orne.aoc.year2024;

import se.orne.aoc.AdventOfCode;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day13 extends AdventOfCode<Stream<Day13.Machine>> {

    record Point(long x, long y) {
    }

    public record Machine(Point a, Point b, Point price) {
    }

    @Override
    public Stream<Machine> input(String input) {
        var pattern = Pattern.compile("X[+=](\\d+), Y[+=](\\d+)");
        return Arrays.stream(input.split("\n\n"))
                .map(s -> s.lines().map(pattern::matcher).map(m -> {
                    if (!m.find()) {
                        throw new IllegalArgumentException();
                    }
                    return new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
                }).toArray(Point[]::new))
                .map(p -> new Machine(p[0], p[1], p[2]));
    }

    @Override
    public Object part1(Stream<Machine> input) {
        return input.mapToLong(m ->
                tokens(m, 0)
        ).sum();
    }

    @Override
    public Object part2(Stream<Machine> input) {
        return input.mapToLong(m ->
                tokens(m, 10_000_000_000_000L)
        ).sum();
    }

    private static long tokens(Machine m, long i) {
        long px = m.price.x + i;
        long py = m.price.y + i;

        long b = (m.a.y * px - py * m.a.x) / (m.a.y * m.b.x - m.b.y * m.a.x);
        long a = (px - b * m.b.x) / m.a.x;

        return a * m.a.x + b * m.b.x == px && a * m.a.y + b * m.b.y == py
                ? a * 3L + b
                : 0;
    }
}
