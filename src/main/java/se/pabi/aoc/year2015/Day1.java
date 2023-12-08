package se.pabi.aoc.year2015;

import se.pabi.aoc.base.AdventOfCode;

public class Day1 extends AdventOfCode<String> {
    @Override
    public String input(String input) {
        return input;
    }

    @Override
    public Object part1(String input) {
        int floor = 0;
        for (char c : input.toCharArray()) {
            if (c == '(') {
                floor++;
            } else if (c == ')'){
                floor--;
            }
        }
        return floor;
    }

    @Override
    public Object part2(String input) {
        int floor = 0;
        for (int i = 0; i < input.length(); i++) {
            char p = input.charAt(i);
            if (p == '(') {
                floor++;
            } else {
                if (floor-- == 0) {
                    return i+1;
                }
            }
        }
        return -1;
    }
}
