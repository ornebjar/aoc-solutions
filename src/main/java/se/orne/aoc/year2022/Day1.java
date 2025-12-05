package se.orne.aoc.year2022;

import se.orne.aoc.AdventOfCode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

public class Day1 extends AdventOfCode<IntStream> {

    @Override
    public IntStream input(String input) {
        return Arrays.stream(input.split("\r\n\r\n"))
                .mapToInt(l -> l.lines().mapToInt(Integer::parseInt).sum());
    }

    @Override
    public Object part1(IntStream input) {
        return input.max().orElseThrow();
    }

    @Override
    public Object part2(IntStream input) {
        return input.boxed()
                .sorted(Comparator.reverseOrder())
                .mapToInt(i -> i)
                .limit(3)
                .sum();
    }

}
