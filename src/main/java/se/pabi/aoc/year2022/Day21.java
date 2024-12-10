package se.pabi.aoc.year2022;

import se.phet.aoc.AdventOfCode;

import java.math.BigInteger;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day21 extends AdventOfCode<Stream<String>> {


    interface Answer {
    }

    record Known(BigInteger value) implements Answer {
    }

    record Question(Function<BigInteger, BigInteger> value) implements Answer {
        BigInteger apply(BigInteger target) {
            return value.apply(target);
        }
    }

    static class StaticMonkey extends Monkey {
        final BigInteger value;

        StaticMonkey(String name, BigInteger value) {
            super(name);
            this.value = value;
        }

        @Override
        BigInteger value(Map<String, Monkey> ignored) {
            return value;
        }

        @Override
        Answer solve(Map<String, Monkey> monkeys) {
            if ("humn".equals(name)) {
                return new Question(target -> target);
            }
            return new Known(value);
        }
    }

    static class OperatorMonkey extends Monkey {
        final String left, right;
        final char operator;

        OperatorMonkey(String name, String left, char operator, String right) {
            super(name);
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        BigInteger value(Map<String, Monkey> monkeys) {
            var m1 = monkeys.get(left).value(monkeys);
            var m2 = monkeys.get(right).value(monkeys);
            return operate(m1, m2);
        }

        private BigInteger operate(BigInteger m1, BigInteger m2) {
            return switch (operator) {
                case '+' -> m1.add(m2);
                case '-' -> m1.subtract(m2);
                case '*' -> m1.multiply(m2);
                case '/' -> m1.divide(m2);
                default -> throw new IllegalArgumentException("Unknown operator " + operator);
            };
        }

        @Override
        Answer solve(Map<String, Monkey> monkeys) {
            Answer lAnswer = monkeys.get(left).solve(monkeys);
            Answer rAnswer = monkeys.get(right).solve(monkeys);

            if (lAnswer instanceof Known(BigInteger value) && rAnswer instanceof Known(BigInteger value1)) {
                return new Known(operate(value, value1));
            }

            if (lAnswer instanceof Question lQuestion && rAnswer instanceof Known(BigInteger value)) {
                return new Question(target -> switch (operator) {
                    case '+' -> lQuestion.apply(target.subtract(value)); // l + r = target  =>  l = target - r
                    case '-' -> lQuestion.apply(target.add(value));      // l - r = target  =>  l = target + r
                    case '*' -> lQuestion.apply(target.divide(value));   // l * r = target  =>  l = target / r
                    case '/' -> lQuestion.apply(target.multiply(value)); // l / r = target  =>  l = target * r
                    default -> throw new IllegalArgumentException("Unknown operator " + operator);
                });
            }

            if (lAnswer instanceof Known(BigInteger value) && rAnswer instanceof Question rQuestion) {
                return new Question(target -> switch (operator) {
                    case '+' -> rQuestion.apply(target.subtract(value)); // l + r = target  =>  r = target - l
                    case '-' -> rQuestion.apply(value.subtract(target)); // l - r = target  =>  r = l - target
                    case '*' -> rQuestion.apply(target.divide(value));   // l * r = target  =>  r = target / l
                    case '/' -> rQuestion.apply(value.divide(target));   // l / r = target  =>  r = l / target
                    default -> throw new IllegalArgumentException("Unknown operator " + operator);
                });
            }

            throw new IllegalStateException("Both left and right contain human answer");
        }
    }

    static abstract class Monkey {
        final String name;

        Monkey(String name) {
            this.name = name;
        }

        static Monkey monkey(String input) {
            String[] parts = input.split(" ");
            String name = parts[0].substring(0, 4);
            if (parts.length == 4) {
                return new OperatorMonkey(name, parts[1], parts[2].charAt(0), parts[3]);
            }
            return new StaticMonkey(name, new BigInteger(parts[1]));
        }

        abstract BigInteger value(Map<String, Monkey> monkeys);

        abstract Answer solve(Map<String, Monkey> monkeys);
    }

    @Override
    public Stream<String> input(String input) {
//        input = """
//                root: pppw + sjmn
//                dbpl: 5
//                cczh: sllz + lgvd
//                zczc: 2
//                ptdq: humn - dvpt
//                dvpt: 3
//                lfqf: 4
//                humn: 5
//                ljgn: 2
//                sjmn: drzm * dbpl
//                sllz: 4
//                pppw: cczh / lfqf
//                lgvd: ljgn * ptdq
//                drzm: hmdt - zczc
//                hmdt: 32""";
        return input.lines();
    }

    @Override
    public Object part1(Stream<String> input) {
        Map<String, Monkey> monkeys = input
                .map(Monkey::monkey)
                .collect(Collectors.toMap(m -> m.name, m -> m));
        return monkeys.get("root").value(monkeys);
    }

    @Override
    public Object part2(Stream<String> input) {
        Map<String, Monkey> monkeys = input
                .map(Monkey::monkey)
                .collect(Collectors.toMap(m -> m.name, m -> m));

        OperatorMonkey root = (OperatorMonkey) monkeys.get("root");

        Answer left = monkeys.get(monkeys.get(root.left).name).solve(monkeys);
        Answer right = monkeys.get(monkeys.get(root.right).name).solve(monkeys);

        if (left instanceof Known(BigInteger value) && right instanceof Question question) {
            return question.apply(value);
        }
        if (left instanceof Question question && right instanceof Known(BigInteger value)) {
            return question.apply(value);
        }

        return 0;
    }
}
