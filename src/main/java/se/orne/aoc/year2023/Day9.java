package se.orne.aoc.year2023;

import se.orne.aoc.AdventOfCode;

import java.util.Arrays;
import java.util.stream.Stream;

public class Day9 extends AdventOfCode<Stream<long[]>> {

    @Override
    public Stream<long[]> input(String input) {
        return input.lines().map(line ->
                Arrays.stream(line.split(" "))
                        .mapToLong(Long::parseLong)
                        .toArray()
        );
    }

    @Override
    public Object part1(Stream<long[]> input) {
        return input.mapToLong(this::calcRight).sum();
    }

    private long calcRight(long[] line) {
        int n = line.length - 1;
        long[] next = new long[n];
        boolean allZero = true;
        for (int i = 0; i < n; i++) {
            if ((next[i] = line[i + 1] - line[i]) != 0) {
                allZero = false;
            }
        }
        if (allZero) {
            return line[n];
        }
        return line[n] + calcRight(next);
    }

    @Override
    public Object part2(Stream<long[]> input) {
        return input.mapToLong(this::calcLeft).sum();
    }

    private long calcLeft(long[] line) {
        long[] next = new long[line.length - 1];
        boolean allZero = true;
        for (int i = 0; i < line.length - 1; i++) {
            if ((next[i] = line[i + 1] - line[i]) != 0) {
                allZero = false;
            }
        }
        if (allZero) {
            return line[0];
        }
        return line[0] - calcLeft(next);
    }

}
