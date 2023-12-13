package se.pabi.aoc.year2023;

import se.pabi.aoc.base.AdventOfCode;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day12 extends AdventOfCode<Stream<Day12.Input>> {

    public record Input(String springs, int[] groups) {
    }

    @Override
    public Stream<Input> input(String input) {
        input = """
                ???.### 1,1,3
                .??..??...?##. 1,1,3
                ?#?#?#?#?#?#?#? 1,3,1,6
                ????.#...#... 4,1,1
                ????.######..#####. 1,6,5
                ?###???????? 3,2,1
                """;
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
        var counter = new AtomicInteger();
        return inputs
                .mapToLong(input -> calc("." + input.springs + ".", input.groups, counter))
                .sum();
    }

    @Override
    public Object part2(Stream<Input> inputs) {
        var counter = new AtomicInteger();
        return inputs
                .parallel()
                .map(input -> {
                    var springs = ("." + String.join("?", input.springs, input.springs,
                            input.springs, input.springs, input.springs) + ".").replaceAll("\\.\\.+", ".");
                    var groups = Stream.of(input.groups, input.groups, input.groups, input.groups, input.groups)
                            .map(Arrays::stream)
                            .flatMap(IntStream::boxed)
                            .mapToInt(Integer::intValue)
                            .toArray();
                    return calc(springs, groups, counter);
                })
                .map(BigInteger::valueOf)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    private static int[] findLast(String springs, int[] groups) {
        var last = new int[groups.length];

        int pos = springs.length() - 1;
        int groupPos = groups.length - 1;
        int count = 0;

        while (pos >= 0 && groupPos >= 0) {
            char current = springs.charAt(pos);
            if (count == groups[groupPos]) {
                last[groupPos] = pos + 1;
                count = 0;
                groupPos--;
            } else if (current == '.') {
                count = 0;
            } else {
                count++;
            }
            pos--;
        }
        return last;
    }

    private static long calc(String springs, int[] groups, AtomicInteger counter) {
        var startTimer = System.currentTimeMillis();
        int[] last = findLast(springs, groups);
        long score = calc(springs, groups, last, '.', 1, 0, 0);
        prettyPrint(springs, last, groups, System.currentTimeMillis() - startTimer, counter.incrementAndGet());
        return score;
    }

    private static void prettyPrint(String springs, int[] last, int[] groups, long time, int counter) {
        var arrows = new StringBuilder();
        var counts = new StringBuilder();
        for (int i = 0, r = 0; i < last.length; i++) {
            arrows.append(" ".repeat(last[i] - r)).append('^');
            counts.append(" ".repeat(last[i] - r)).append(groups[i]);
            r = last[i] + String.valueOf(groups[i]).length();
        }
        System.out.printf("""
                ╭───────�
                │ %s
                │ %s
                │ %s
                │ #%d Done in %sms
                """, springs, arrows, counts, counter, time);
    }

    private static long calc(String springs, int[] groups, int[] last, char prev,
                             int springPos, int groupCount, int groupPos) {
        if (springPos == springs.length()) {
            if (groupPos == groups.length) {
                return 1;
            }
            return 0;
        }
        if (groupPos < groups.length && springPos - groupCount > last[groupPos]) {
            return 0;
        }

        char current = springs.charAt(springPos);

        if (current == '?') {
            return calcCurrent(springs, groups, last, '.', prev, springPos, groupCount, groupPos) +
                    calcCurrent(springs, groups, last, '#', prev, springPos, groupCount, groupPos);
        }
        return calcCurrent(springs, groups, last, current, prev, springPos, groupCount, groupPos);
    }

    private static long calcCurrent(String springs, int[] groups, int[] last, char current, char prev,
                                    int springPos, int groupCount, int groupPos) {
        switch (prev) {
            case '.' -> {
                return switch (current) {
                    case '.' -> calc(springs, groups, last, current, springPos + 1, 0, groupPos);
                    case '#' -> calc(springs, groups, last, current, springPos + 1, 1, groupPos);
                    default -> throw new RuntimeException("Unknown char: " + prev);
                };
            }
            case '#' -> {
                return switch (current) {
                    case '.' -> {
                        if (groupPos == groups.length) {
                            yield 0;
                        }
                        if (groups[groupPos] == groupCount) {
                            yield calc(springs, groups, last, current, springPos + 1, 0, groupPos + 1);
                        }
                        yield 0;
                    }
                    case '#' -> {
                        if (groupPos == groups.length) {
                            yield 0;
                        }
                        if (groups[groupPos] == groupCount) {
                            yield 0;
                        }
                        yield calc(springs, groups, last, current, springPos + 1, groupCount + 1, groupPos);
                    }
                    default -> throw new RuntimeException("Unknown char: " + prev);
                };
            }
            default -> throw new RuntimeException("Unknown char: " + current);
        }
    }
}
