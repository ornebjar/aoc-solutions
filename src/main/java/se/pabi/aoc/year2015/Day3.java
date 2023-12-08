package se.pabi.aoc.year2015;

import se.pabi.aoc.base.AdventOfCode;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Day3 extends AdventOfCode<String> {
    @Override
    public String input(String input) {
        return input;
    }

    @Override
    public Object part1(String input) {

        Set<Point> houses = new HashSet<>();
        houses.add(new Point(0, 0));

        int x = 0;
        int y = 0;

        for (char c : input.toCharArray()) {
            x = hor(x, c);
            y = ver(y, c);
            houses.add(new Point(x, y));
        }

        return houses.size();
    }

    @Override
    public Object part2(String input) {
        Set<Point> houses = new HashSet<>();
        houses.add(new Point(0, 0));

        int sx = 0;
        int sy = 0;
        int rx = 0;
        int ry = 0;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (i % 2 == 0) {
                rx = hor(rx, c);
                ry = ver(ry, c);
                houses.add(new Point(rx, ry));
            } else {
                sx = hor(sx, c);
                houses.add(new Point(sx, sy));
            }
        }

        return houses.size();
    }

    private int hor(int x, char c) {
        return switch (c) {
            case '<' -> x - 1;
            case '>' -> x + 1;
            default -> x;
        };
    }

    private int ver(int y, char c) {
        return switch (c) {
            case '^' -> y - 1;
            case 'v' -> y + 1;
            default -> y;
        };
    }
}
