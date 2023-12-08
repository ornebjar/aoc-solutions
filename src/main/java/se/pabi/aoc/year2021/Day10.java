package se.pabi.aoc.year2021;

import se.pabi.aoc.base.AdventOfCode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Day10 extends AdventOfCode<String[]> {

    @Override
    public String[] input(String input) {
        return input.lines().toArray(String[]::new);
    }

    @Override
    public Object part1(String[] input) {
        Map<Character, Integer> scores = Map.of(
                ')', 3,
                ']', 57,
                '}', 1197,
                '>', 25137
        );
        Map<Character, Character> openClose = Map.of(
                '(', ')',
                '[', ']',
                '{', '}',
                '<', '>'
        );
        int errorSum = 0;


        for (String line : input) {
            LinkedList<Character> queue = new LinkedList<>();
            for (Character c : line.toCharArray()) {
                if (openClose.containsKey(c)) {
                    queue.push(openClose.get(c));
                } else if (queue.poll() != c) {
                    errorSum += scores.get(c);
                    break;
                }
            }
        }
        return errorSum;
    }

    @Override
    public Object part2(String[] input) {
        Map<Character, Long> scores = Map.of(
                ')', 1L,
                ']', 2L,
                '}', 3L,
                '>', 4L
        );
        Map<Character, Character> openClose = Map.of(
                '(', ')',
                '[', ']',
                '{', '}',
                '<', '>'
        );
        List<Long> results = Arrays.stream(input).map(line -> {
            LinkedList<Character> queue = new LinkedList<>();
            for (Character c : line.toCharArray()) {
                if (openClose.containsKey(c)) {
                    queue.push(openClose.get(c));
                } else if (queue.poll() != c) {
                    return 0L;
                }
            }
            return queue.stream().map(scores::get).reduce(0L, (s, v) -> s * 5 + v);
        }).filter(s -> s > 0).sorted().toList();
        return results.get(results.size() / 2);
    }
}
