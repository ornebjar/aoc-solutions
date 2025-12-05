package se.orne.aoc.year2022;

import se.orne.aoc.AdventOfCode;

import java.util.List;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public class Day3 extends AdventOfCode<String[]> {

    @Override
    public String[] input(String input) {
        return input.lines().toArray(String[]::new);
    }

    @Override
    public Object part1(String[] rows) {
        int s = 0;
        for (String row : rows) {
            List<Integer> items = row.chars().mapToObj(c -> {
                if (c >= 'a' && c <= 'z') {
                    return c - 'a' + 1;
                }
                return c - 'A' + 27;
            }).toList();

            Set<Integer> a = items.stream().limit(row.length() / 2).collect(Collectors.toSet());
            Set<Integer> b = items.stream().skip(row.length() / 2).collect(Collectors.toSet());

            a.retainAll(b);
            s += a.stream().findFirst().orElse(-1000000);
        }
        return s;
    }

    @Override
    public Object part2(String[] rows) {
        int s = 0;
        for (int i = 0; i < rows.length; i += 3) {
            IntFunction<Integer> char2Num = c -> {
                if (c >= 'a' && c <= 'z') {
                    return c - 'a' + 1;
                }
                return c - 'A' + 27;
            };

            Set<Integer> g = rows[i].chars().mapToObj(char2Num).collect(Collectors.toSet());
            g.retainAll(rows[i + 1].chars().mapToObj(char2Num).collect(Collectors.toSet()));
            g.retainAll(rows[i + 2].chars().mapToObj(char2Num).collect(Collectors.toSet()));

            s += g.stream().findFirst().orElse(-10000000);
        }
        return s;
    }

}
