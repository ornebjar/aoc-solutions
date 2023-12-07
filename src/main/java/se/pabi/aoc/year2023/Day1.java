package se.pabi.aoc.year2023;

import se.pabi.aoc.base.AdventOfCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class Day1 extends AdventOfCode<Stream<String>> {

    @Override
    public Stream<String> input(String input) {
        return input.lines();
    }

    @Override
    public Object part1(Stream<String> input) {
        Num num = new Num();
        for (int i = 1; i < 10; i++) {
            num.put(String.valueOf(i), i);
        }
        return input.mapToInt(line ->
                num.first(line).orElseThrow() * 10 + num.last(line).orElseThrow()
        ).sum();
    }

    @Override
    public Object part2(Stream<String> input) {
        Num num = new Num();
        for (int i = 1; i < 10; i++) {
            num.put(String.valueOf(i), i);
        }
        num.put("one", 1);
        num.put("two", 2);
        num.put("three", 3);
        num.put("four", 4);
        num.put("five", 5);
        num.put("six", 6);
        num.put("seven", 7);
        num.put("eight", 8);
        num.put("nine", 9);

        return input.mapToInt(line ->
                num.first(line).orElseThrow() * 10 + num.last(line).orElseThrow()
        ).sum();
    }

    static class Num {
        Map<Character, Num> next = new HashMap<>();
        Integer value = null;

        Optional<Integer> first(String line) {
            for (int i = 0; i < line.length(); i++) {
                Optional<Integer> num = get(line, i);
                if (num.isPresent()) {
                    return num;
                }
            }
            return Optional.empty();
        }

        Optional<Integer> last(String line) {
            for (int i = line.length()-1; i >= 0; i--) {
                Optional<Integer> num = get(line, i);
                if (num.isPresent()) {
                    return num;
                }
            }
            return Optional.empty();
        }

        Optional<Integer> get(String line, int pos) {
            return get(line.substring(pos));
        }

        Optional<Integer> get(String line) {
            if (line.isEmpty()) {
                return Optional.ofNullable(value);
            }
            char c = line.charAt(0);
            if (value != null) {
                return Optional.of(value);
            }
            return Optional.ofNullable(next.get(c))
                    .flatMap(num -> num.get(line.substring(1)));
        }

        public void put(String word, int i) {
            if (word.isEmpty()) {
                value = i;
                return;
            }
            char c = word.charAt(0);
            next.computeIfAbsent(c, k -> new Num()).put(word.substring(1), i);
        }
    }

}
