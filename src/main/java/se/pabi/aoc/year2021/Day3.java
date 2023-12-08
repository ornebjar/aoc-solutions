package se.pabi.aoc.year2021;

import se.pabi.aoc.base.AdventOfCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day3 extends AdventOfCode<List<String>> {
    @Override
    public List<String> input(String input) {
        return input.lines().toList();
    }

    @Override
    public Object part1(List<String> rows) {
        int len = rows.getFirst().length();

        long[] counts = new long[len];

        for (int i = 0; i < len; i++) {
            final int pos = i;
            counts[i] = rows.stream().map(r -> r.charAt(pos))
                    .filter(c -> c == '1')
                    .count();
        }

        StringBuilder g = new StringBuilder();
        StringBuilder e = new StringBuilder();

        for (int i = 0; i < len; i++) {
            if (counts[i] >= rows.size() - counts[i]) {
                g.append("1");
                e.append("0");
            } else {
                g.append("0");
                e.append("1");
            }
        }

        return Integer.parseInt(g.toString(), 2) * Integer.parseInt(e.toString(), 2);
    }

    @Override
    public Object part2(List<String> rows) {
        int len = rows.getFirst().length();

        List<String> o = new ArrayList<>(rows);
        List<String> c = new ArrayList<>(rows);


        for (int i = 0; i < len; i++) {
            final int pos = i;
            if (o.size() > 1) {
                long count = o.stream().map(r -> r.charAt(pos))
                        .filter(ch -> ch == '1')
                        .count();
                if (count >= o.size() - count) {
                    o = o.stream().filter(r -> r.charAt(pos) == '1').collect(Collectors.toList());
                } else {
                    o = o.stream().filter(r -> r.charAt(pos) == '0').collect(Collectors.toList());
                }
            }

            if (c.size() > 1) {
                long count = c.stream().map(r -> r.charAt(pos))
                        .filter(ch -> ch == '0')
                        .count();
                if (count <= c.size() - count) {
                    c = c.stream().filter(r -> r.charAt(pos) == '0').collect(Collectors.toList());
                } else {
                    c = c.stream().filter(r -> r.charAt(pos) == '1').collect(Collectors.toList());
                }
            }
        }

        return Integer.parseInt(o.getFirst(), 2) * Integer.parseInt(c.getFirst(), 2);
    }
}
