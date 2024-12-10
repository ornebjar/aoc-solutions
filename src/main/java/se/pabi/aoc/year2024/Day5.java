package se.pabi.aoc.year2024;

import se.phet.aoc.AdventOfCode;

import java.util.*;

public class Day5 extends AdventOfCode<Day5.D5> {

    public record Rule(int a, int b) {
    }

    public record D5(List<Rule> rules, List<List<Integer>> values) {
    }

    @Override
    public D5 input(String input) {

//        input = """
//                47|53
//                97|13
//                97|61
//                97|47
//                75|29
//                61|13
//                75|53
//                29|13
//                97|29
//                53|29
//                61|53
//                97|53
//                61|29
//                47|13
//                75|47
//                97|75
//                47|61
//                75|61
//                47|29
//                75|13
//                53|13
//
//                75,47,61,53,29
//                97,61,53,29,13
//                75,29,13
//                75,97,47,61,53
//                61,13,29
//                97,13,75,29,47
//                """;

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

//        List <Integer> line = new ArrayList<>(input);
//        for (Rule rule : rules) {
//            var a = line.indexOf(rule.a());
//            var b = line.indexOf(rule.b());
//
//
//            if (a == -1 || b == -1) {
//                continue;
//            }
//            if (a > b) {
//                line.set(a, rule.b());
//                line.set(b, rule.a());
//            }
//        }

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
