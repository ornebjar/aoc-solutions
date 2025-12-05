package se.orne.aoc.year2021;

import se.orne.aoc.AdventOfCode;

public class Day1 extends AdventOfCode<int[]> {
    @Override
    public int[] input(String input) {
        return input.lines().mapToInt(Integer::parseInt).toArray();
    }

    @Override
    public Object part1(int[] input) {
        int sum = 0;
        for (int i = 1; i < input.length; i++) {
            if (input[i] > input[i - 1]) {
                sum++;
            }
        }
        return sum;
    }

    @Override
    public Object part2(int[] input) {
        int sum = 0;
        for (int i = 2; i < input.length-1; i++) {
            if (input[i-1] + input[i] + input[i+1]  > input[i-2] + input[i-1] + input[i]) {
                sum++;
            }
        }
        return sum;
    }
}
