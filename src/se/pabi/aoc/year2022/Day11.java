package se.pabi.aoc.year2022;

import se.pabi.aoc.base.AdventOfCode;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day11 extends AdventOfCode<Day11.Monkey[]> {

    static final class Monkey {
        final List<BigInteger> items;
        final Function<BigInteger, BigInteger> op;
        final Function<BigInteger, Integer> to;

        final BigInteger divisor;

        Monkey(String input) {
            Map<String, String> inputs = input.lines()
                    .skip(1)
                    .map(line -> line.split(": "))
                    .collect(Collectors.toMap(parts -> parts[0].trim(), parts -> parts[1]));

            items = Arrays.stream(inputs.get("Starting items").split(", "))
                    .map(BigInteger::new)
                    .collect(Collectors.toList());

            String[] opInputs = inputs.get("Operation").split(" = ")[1].split(" ");
            BinaryOperator<BigInteger> operator = switch (opInputs[1]) {
                case "*" -> BigInteger::multiply;
                case "+" -> BigInteger::add;
                default -> throw new IllegalArgumentException("Unknown operator: " + opInputs[1]);
            };
            Optional<BigInteger> a = staticOperatorInput(opInputs[0]);
            Optional<BigInteger> b = staticOperatorInput(opInputs[2]);
             op = old -> operator.apply(a.orElse(old), b.orElse(old));

            int trueThrow = Integer.parseInt(inputs.get("If true").split(" ")[3]);
            int falseThrow = Integer.parseInt(inputs.get("If false").split(" ")[3]);
            divisor = new BigInteger(inputs.get("Test").split(" ")[2]);
            to = (num) -> (BigInteger.ZERO.equals(num.remainder(divisor))) ? trueThrow : falseThrow;
        }

        private static Optional<BigInteger> staticOperatorInput(String staticInput) {
            return "old".equals(staticInput)
                    ? Optional.empty()
                    : Optional.of(new BigInteger(staticInput));
        }
    }

    public Monkey[] input(String input) {
        return Arrays.stream(input.split("\r\n\r\n")).map(Monkey::new).toArray(Monkey[]::new);
    }

    public static final BigInteger THREE = BigInteger.valueOf(3);

    public String part1(Monkey[] monkeys) {
        int[] interactions = new int[monkeys.length];
        for (int j = 0; j < 20; j++) {
            for (int m = 0; m < monkeys.length; m++) {
                Monkey monkey = monkeys[m];
                interactions[m] += monkey.items.size();
                monkey.items.stream()
                        .map(monkey.op)
                        .map(i -> i.divide(THREE))
                        .forEach(i -> monkeys[monkey.to.apply(i)].items.add(i));
                monkey.items.clear();
            }
        }
        Arrays.sort(interactions);
        return String.valueOf(interactions[monkeys.length - 2] * interactions[monkeys.length - 1]);
    }

    public String part2(Monkey[] monkeys) {
        BigInteger mod = Arrays.stream(monkeys).map(m -> m.divisor).reduce(BigInteger.ONE, BigInteger::multiply);
        long[] interactions = new long[monkeys.length];
        for (int j = 0; j < 10000; j++) {
            for (int m = 0; m < monkeys.length; m++) {
                Monkey monkey = monkeys[m];
                interactions[m] += monkey.items.size();
                monkey.items.stream()
                        .map(monkey.op)
                        .map(i -> i.mod(mod))
                        .forEach(i -> monkeys[monkey.to.apply(i)].items.add(i));
                monkey.items.clear();
            }
        }
        Arrays.sort(interactions);
        return String.valueOf(interactions[monkeys.length - 2] * interactions[monkeys.length - 1]);
    }

    public static void main(String[] args) {
        new Day11();
    }
}
