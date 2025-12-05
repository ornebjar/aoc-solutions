package se.orne.aoc.year2024;

import se.orne.aoc.AdventOfCode;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.System.arraycopy;

public class Day7 extends AdventOfCode<Stream<Day7.D7>> {

    public record D7(BigInteger result, BigInteger[] values) {
        public D7(String line) {
            this(new BigInteger(
                            line.split(": ")[0]),
                    Arrays.stream(line.split(": ")[1].split(" "))
                            .map(BigInteger::new)
                            .toArray(BigInteger[]::new)
            );
        }
    }

    @Override
    public Stream<D7> input(String input) {
        return input.lines().map(D7::new);
    }

    interface Operator {
        BigInteger apply(BigInteger a, BigInteger b);
    }

    @Override
    public Object part1(Stream<D7> input) {
        var ops = Set.<Operator>of(
                BigInteger::add,
                BigInteger::multiply
        );
        return calc(ops, input);
    }

    @Override
    public Object part2(Stream<D7> input) {
        var ops = Set.<Operator>of(
                BigInteger::add,
                BigInteger::multiply,
                (a, b) -> new BigInteger(a.toString() + b.toString())
        );
        return calc(ops, input);
    }

    private static BigInteger calc(Set<Operator> ops, Stream<D7> input) {
        return input
                .filter(d7 -> match(ops, d7.result, d7.values))
                .map(D7::result)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    private static boolean match(Set<Operator> ops, BigInteger result, BigInteger[] values) {
        if (values.length == 1) {
            return result.equals(values[0]);
        }
        return ops.stream().anyMatch(op -> match(op, ops, result, values));
    }

    private static boolean match(Operator op, Set<Operator> ops, BigInteger result, BigInteger[] values) {
        var reduce = new BigInteger[values.length - 1];
        reduce[0] = op.apply(values[0], values[1]);
        arraycopy(values, 2, reduce, 1, values.length - 2);
        return match(ops, result, reduce);
    }

}
