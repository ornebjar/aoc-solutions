package se.pabi.aoc.year2023;

import se.phet.aoc.AdventOfCode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day3 extends AdventOfCode<String[]> {



    @Override
    public String[] input(String input) {
        return input.lines().toArray(String[]::new);
    }

    static class Number {
        int value;
        boolean partNumber = false;

        public Number(int value) {
            this.value = value;
        }
    }

    @Override
    public Object part1(String[] input) {
        var numbers = getNumbers(input);

        for (int row = 0; row < input.length; row++) {
            var line = input[row];
            for (int col = 0; col < line.length(); col++) {
                var c = line.charAt(col);

                if (getDigit(c).isEmpty() && c != '.') {
                    for (int or = -1; or < 2; or++) {
                        for (int oc = -1; oc < 2; oc++) {
                            if (numbers[row + or][col + oc] != null) {
                                numbers[row + or][col + oc].partNumber = true;
                            }
                        }
                    }
                }
            }
        }

        return Arrays.stream(numbers)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .stream()
                .filter(n -> n.partNumber)
                .mapToInt(n -> n.value)
                .sum();
    }

    @Override
    public Object part2(String[] input) {
        var numbers = getNumbers(input);

        int sum = 0;

        for (int row = 0; row < input.length; row++) {
            var line = input[row];
            for (int col = 0; col < line.length(); col++) {
                var c = line.charAt(col);

                if (c == '*') {
                    var mul = new HashSet<Number>();
                    for (int or = -1; or < 2; or++) {
                        for (int oc = -1; oc < 2; oc++) {
                            if (numbers[row + or][col + oc] != null) {
                                mul.add(numbers[row + or][col + oc]);
                            }
                        }
                    }
                    if (mul.size() == 2) {
                        sum += mul.stream()
                                .mapToInt(n -> n.value)
                                .reduce(1, (a, b) -> a * b);
                    }
                }
            }
        }

        return sum;
    }

    private Number[][] getNumbers(String[] input) {
        var numbers = new Number[input.length][input[0].length()];

        for (int row = 0; row < input.length; row++) {
            var line = input[row];
            for (int col = 0; col < line.length(); col++) {
                var c = line.charAt(col);

                var digit = getDigit(c);
                if (digit.isPresent()) {
                    if (col > 0 && numbers[row][col - 1] != null) {
                        var number = numbers[row][col] = numbers[row][col - 1];
                        number.value = number.value * 10 + digit.get();
                    } else {
                        numbers[row][col] = new Number(digit.get());
                    }
                }
            }
        }
        return numbers;
    }

    private static Optional<Integer> getDigit(char c) {
        return c >= '0' && c <= '9'
                ? Optional.of(c - '0')
                : Optional.empty();
    }
}
