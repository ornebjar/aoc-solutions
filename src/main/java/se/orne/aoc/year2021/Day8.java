package se.orne.aoc.year2021;

import se.orne.aoc.AdventOfCode;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day8 extends AdventOfCode<Stream<Day8.Input>> {

    public record Input(String seg, String res) {
    }


    @Override
    public Stream<Input> input(String input) {
        return input.lines().map(l -> {
            String[] parts = l.split(" \\| ");
            return new Input(parts[0], parts[1]);
        });
    }

    @Override
    public Object part1(Stream<Input> input) {
        Set<Integer> unique = Set.of(2, 3, 4, 7);
        return input
                .map(Input::res)
                .map(r -> r.split(" "))
                .flatMap(Arrays::stream)
                .map(String::length)
                .filter(unique::contains)
                .count();
    }

    @Override
    public Object part2(Stream<Input> input) {
        return input.mapToInt(i -> {

            List<String> numbers = Arrays.stream(i.seg.split(" "))
                    .toList();

            Set<Integer> n1 = numbers.stream()
                    .map(s -> s.chars().boxed().collect(Collectors.toSet()))
                    .filter(s -> s.size() == 2)
                    .findFirst().orElseThrow();
            Set<Integer> n4 = numbers.stream()
                    .map(s -> s.chars().boxed().collect(Collectors.toSet()))
                    .filter(s -> s.size() == 4)
                    .findFirst().orElseThrow();

            return Arrays.stream(i.res.split(" "))
                    .map(s -> {
                        switch (s.length()) {
                            case 2: return 1;
                            case 3: return 7;
                            case 4: return 4;
                            case 7: return 8;
                        }
                        Set<Integer> current = s.chars().boxed().collect(Collectors.toSet());

                        if (s.length() == 5) { // 2 | 3 | 5
                            if (current.containsAll(n1)) {
                                return 3;
                            }
                            current.retainAll(n4);
                            if (current.size() == 2) {
                                return 2;
                            }
                            return 5;
                        }

                        if (s.length() == 6) { // 0 | 6 | 9
                            if (!current.containsAll(n1)) {
                                return 6;
                            }
                            if (current.containsAll(n4)) {
                                return 9;
                            }
                            return 0;
                        }

                        return -1;
                    })
                    .reduce(0, (p, n) -> p * 10 + n);
        }).sum();
    }
}
