package se.pabi.aoc.year2022;

import se.phet.aoc.AdventOfCode;

public class Day6 extends AdventOfCode<String> {

    @Override
    public String input(String input) {
        return input;
    }

    @Override
    public Object part1(String input) {
        return day6(input, 4);
    }

    @Override
    public Object part2(String input) {
        return day6(input, 14);
    }

    private Object day6(String input, int size) {
        for (int i = size; i <= input.length(); i++) {
            String sub = input.substring(i - size, i);
            if (sub.chars().distinct().count() == size) {
                return i;
            }
        }
        throw new IllegalArgumentException("No match found");
    }

}
