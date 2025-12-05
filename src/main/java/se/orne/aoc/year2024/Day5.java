package se.orne.aoc.year2024;

import se.orne.aoc.AdventOfCode;

import java.util.*;

public class Day5 extends AdventOfCode<Day5.D5> {

    record Rule(int a, int b) {
    }

    public record D5(List<Rule> rules, List<List<Integer>> values) {
    }

    @Override
    public D5 input(String input) {
        var split = input.split("\n\n");
        var rules = split[0].lines()
                .map(l -> {
                    var s = l.split("\\|");
                    return new Rule(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
                })
                .toList();
        var values = split[1].lines()
                .map(l -> Arrays.stream(l.split(",")).map(Integer::parseInt).toList())
                .toList();

        return new D5(rules, values);
    }

    @Override
    public Object part1(D5 input) {

        return input.values.stream()
                .filter(line -> isCorrect(input.rules, line))
                .mapToLong(line -> line.get(line.size() / 2))
                .sum();
    }

    private boolean isCorrect(List<Rule> rules, List<Integer> line) {
        Set<Rule> matched = new HashSet<>();
        for (Integer nr : line) {
            for (Rule rule : rules) {
                if (rule.b() == nr) {
                    matched.add(rule);
                }
            }
            for (Rule rule : matched) {
                if (rule.a() == nr) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Object part2(D5 input) {
        return input.values.stream()
                .filter(line -> !isCorrect(input.rules, line))
                .mapToLong(line -> middleSorted(input.rules, line))
                .sum();
    }

    private static long middleSorted(List<Rule> rules, List<Integer> input) {
        var line = input.stream()
                .sorted((o1, o2) -> {
                    for (Rule rule : rules) {
                        if (rule.a() == o1 && rule.b() == o2) {
                            return -1;
                        }
                        if (rule.a() == o2 && rule.b() == o1) {
                            return 1;
                        }
                    }
                    return 0;
                })
                .toList();
        return line.get(line.size() / 2);
    }
}
