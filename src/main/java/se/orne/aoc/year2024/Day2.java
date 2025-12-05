package se.orne.aoc.year2024;

import se.orne.aoc.AdventOfCode;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day2 extends AdventOfCode<Stream<int[]>> {

    @Override
    public Stream<int[]> input(String input) {
        return input.lines()
                .map(line -> line.split(" "))
                .map(line -> Arrays.stream(line).mapToInt(Integer::parseInt))
                .map(IntStream::toArray);
    }

    @Override
    public Object part1(Stream<int[]> input) {
        return input.mapToInt(line ->
                safe(line) ? 1 : 0
        ).sum();
    }

    @Override
    public Object part2(Stream<int[]> input) {
        return input.mapToInt(line ->
                safe(line) || safeRemove(line) ? 1 : 0
        ).sum();
    }

    private static boolean safeRemove(int[] line) {
        for (int i = 0; i < line.length; i++) {
            int[] copy = new int[line.length - 1];
            System.arraycopy(line, 0, copy, 0, i);
            System.arraycopy(line, i + 1, copy, i, line.length - i - 1);
            if (safe(copy)) {
                return true;
            }
        }
        return false;
    }

    private static boolean safe(int[] line) {
        int signum = (int)Math.signum(line[1] - line[0]);
        for (int i = 1; i < line.length; i++) {
            int value = (line[i] - line[i - 1]) * signum;
            if (value < 1 || value > 3) {
                return false;
            }
        }
        return true;
    }

}

