package se.pabi.aoc.year2021;

import se.pabi.aoc.base.AdventOfCode;

import java.util.Arrays;

public class Day6 extends AdventOfCode<String[]> {
    @Override
    public String[] input(String input) {
        return input.lines().toArray(String[]::new);
    }

    @Override
    public Object part1(String[] input) {
        String[] parts = input[0].split(",");
        int[] counts = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (String part : parts) {
            counts[Integer.parseInt(part)]++;
        }
        for (int d = 0; d < 80; d++) {
            int birth = counts[0];
            for (int i = 1; i < counts.length; i++) {
                counts[i-1] = counts[i];
            }
            counts[8] = birth;
            counts[6] += birth;
        }

        return Arrays.stream(counts).asLongStream().sum();
    }

    @Override
    public Object part2(String[] input) {
        String[] parts = input[0].split(",");
        long[] counts = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (String part : parts) {
            counts[Integer.parseInt(part)]++;
        }
        for (int d = 0; d < 256; d++) {
            long birth = counts[0];
            for (int i = 1; i < counts.length; i++) {
                counts[i-1] = counts[i];
            }
            counts[8] = birth;
            counts[6] += birth;
        }

        return Arrays.stream(counts).sum();
    }

}
