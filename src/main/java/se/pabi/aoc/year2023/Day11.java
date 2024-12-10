package se.pabi.aoc.year2023;

import se.phet.aoc.AdventOfCode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class Day11 extends AdventOfCode<String[]> {
    @Override
    public String[] input(String input) {
        return input.lines().toArray(String[]::new);
    }

    private record Point(int x, int y) {}

    @Override
    public Object part1(String[] input) {
        return calculate(input, 1);
    }

    @Override
    public Object part2(String[] input) {
        return calculate(input, 999999);
    }

    private static BigInteger calculate(String[] input, int expand) {
        var horExpand = new boolean[input.length];
        var verExpand = new boolean[input[0].length()];

        Arrays.fill(horExpand, true);
        Arrays.fill(verExpand, true);

        var galaxies = new ArrayList<Point>();

        for (int y = 0; y < input.length; y++) {
            var row = input[y];
            for (int x = 0; x < row.length(); x++) {
                if (row.charAt(x) == '#') {
                    horExpand[y] = false;
                    verExpand[x] = false;
                    galaxies.add(new Point(x, y));
                }
            }
        }

        return getMinSum(galaxies, horExpand, verExpand, expand);
    }

    private static BigInteger getMinSum(ArrayList<Point> galaxies, boolean[] horExpand, boolean[] verExpand, int expand) {
        var sum = BigInteger.ZERO;
        for (int i = 0; i < galaxies.size(); i++) {
            var g1 = galaxies.get(i);
            for (int k = i + 1; k < galaxies.size(); k++) {
                var g2 = galaxies.get(k);
                var from = new Point(Math.min(g1.x, g2.x), Math.min(g1.y, g2.y));
                var to = new Point(Math.max(g1.x, g2.x), Math.max(g1.y, g2.y));
                int yExpand = 0;
                for (int e = from.y + 1; e < to.y; e++) {
                    if (horExpand[e]) {
                        yExpand++;
                    }
                }
                int xExpand = 0;
                for (int e = from.x + 1; e < to.x; e++) {
                    if (verExpand[e]) {
                        xExpand++;
                    }
                }
                sum = sum.add(BigInteger.valueOf((to.x - from.x) + (to.y - from.y)));
                sum = sum.add(BigInteger.valueOf(xExpand + yExpand).multiply(BigInteger.valueOf(expand)));
            }
        }
        return sum;
    }
}
