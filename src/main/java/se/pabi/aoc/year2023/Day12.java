package se.pabi.aoc.year2023;

import se.pabi.aoc.base.AdventOfCode;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 extends AdventOfCode<Stream<Day12.Input>> {

    public record Input(String springs, int[] groups) {
    }

    @Override
    public boolean progressTracking() {
        return false;
    }

    @Override
    public Stream<Input> input(String input) {
        return input.lines().map(line -> {
            String[] split = line.split(" ");
            return new Input(
                    split[0],
                    Arrays.stream(split[1].split(",")).mapToInt(Integer::parseInt).toArray()
            );
        });
    }

    @Override
    public Object part1(Stream<Input> inputs) {
        return inputs
                .mapToLong(input -> calc(input.springs(), input.groups()))
                .sum();
    }

    @Override
    public Object part2(Stream<Input> inputs) {
        return inputs
                .mapToLong(input -> calc(
                        Stream.generate(input::springs).limit(5).collect(Collectors.joining("?")),
                        Stream.generate(input::groups).limit(5).flatMapToInt(Arrays::stream).toArray()))
                .sum();
    }

    private static long calc(String springs, int[] groups) {
        springs = Arrays.stream(springs.split("\\.+"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining("."));
        var cache = new long[springs.length()][groups.length];
        long score = calc(springs, 0, groups, 0, cache);
        System.out.printf("""
                        ╭───────�
                        │ %s
                        │ %s
                        │ Count %d%n""", springs, Arrays.toString(groups), score);
        return score;
    }

    private static long calc(String springs, int p, int[] groups, int g, long[][] cache) {
        if (p >= springs.length()) {
            return g == groups.length ? 1 : 0;
        }
        if (g == groups.length) {
            return springs.indexOf('#', p) == -1 ? 1 : 0;
        }
        if (cache[p][g] != 0) {
            return cache[p][g] - 1;
        }

        var group = groups[g];
        int end = p + group;

        boolean canSet = (end == springs.length() || (end < springs.length() && springs.charAt(end) != '#')) &&
                springs.indexOf('.', p, end) == -1;
        boolean mustSet = springs.charAt(p) == '#';

        long score;
        if (canSet && mustSet) {
            score = calc(springs, end + 1, groups, g + 1, cache);
        } else if (canSet) {
            score = calc(springs, end + 1, groups, g + 1, cache) +
                    calc(springs, p + 1, groups, g, cache);
        } else if (!mustSet) {
            score = calc(springs, p + 1, groups, g, cache);
        } else {
            score = 0;
        }
        cache[p][g] = score + 1;
        return score;
    }


}
