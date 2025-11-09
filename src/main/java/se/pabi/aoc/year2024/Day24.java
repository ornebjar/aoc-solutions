package se.pabi.aoc.year2024;

import se.phet.aoc.AdventOfCode;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Day24 extends AdventOfCode<Stream<String>> {

    private enum Operator {
        OR,
        AND,
        XOR
    }

    private static Map<String, Source> sources;

    private interface Source {
        long value();
    }

    private record Operation(String a, Operator op, String b) implements Source {
        public long value() {
            return switch (op) {
                case OR -> sources.get(a).value() | sources.get(b).value();
                case AND -> sources.get(a).value() & sources.get(b).value();
                case XOR -> sources.get(a).value() ^ sources.get(b).value();
            };
        }
    }

    private record Value(long value) implements Source {
    }

    @Override
    public Stream<String> input(String input) {
//        input = """
//                x00: 1
//                x01: 0
//                x02: 1
//                x03: 1
//                x04: 0
//                y00: 1
//                y01: 1
//                y02: 1
//                y03: 1
//                y04: 1
//
//                ntg XOR fgs -> mjb
//                y02 OR x01 -> tnw
//                kwq OR kpj -> z05
//                x00 OR x03 -> fst
//                tgd XOR rvg -> z01
//                vdt OR tnw -> bfw
//                bfw AND frj -> z10
//                ffh OR nrd -> bqk
//                y00 AND y03 -> djm
//                y03 OR y00 -> psh
//                bqk OR frj -> z08
//                tnw OR fst -> frj
//                gnj AND tgd -> z11
//                bfw XOR mjb -> z00
//                x03 OR x00 -> vdt
//                gnj AND wpb -> z02
//                x04 AND y00 -> kjc
//                djm OR pbm -> qhw
//                nrd AND vdt -> hwm
//                kjc AND fst -> rvg
//                y04 OR y02 -> fgs
//                y01 AND x02 -> pbm
//                ntg OR kjc -> kwq
//                psh XOR fgs -> tgd
//                qhw XOR tgd -> z09
//                pbm OR djm -> kpj
//                x03 XOR y03 -> ffh
//                x00 XOR y04 -> ntg
//                bfw OR bqk -> z06
//                nrd XOR fgs -> wpb
//                frj XOR qhw -> z04
//                bqk OR frj -> z07
//                y03 OR x01 -> nrd
//                hwm AND bqk -> z03
//                tgd XOR rvg -> z12
//                tnw OR pbm -> gnj
//                """;
        var parts = input.split("\n\n");

        sources = new HashMap<>();

        parts[0].lines().forEach(line -> {
            var split = line.split(": ");
            sources.put(split[0], new Value(Long.parseLong(split[1])));
        });

        parts[1].lines().forEach(line -> {
            var split = line.split(" -> ");
            var args = split[0].split(" ");
            sources.put(split[1], new Operation(args[0], Operator.valueOf(args[1]), args[2]));
        });

        return sources.keySet().stream();
    }

    @Override
    public boolean progressTracking() {
        return false;
    }

    private static long calc(String name) {
        return sources.keySet().stream()
                .filter(key -> key.startsWith(name))
                .sorted(Comparator.reverseOrder())
                .mapToLong(source -> sources.get(source).value())
                .reduce(0, (a, b) -> (a << 1) | b);
    }

    @Override
    public Object part1(Stream<String> keys) {
        return calc("z");
    }

    private static long calcDiff() {
        long x = calc("x");
        long y = calc("y");
        long z = calc("z");
        return z - x - y;
    }

    @Override
    public Object part2(Stream<String> keys) {
        var diff = calcDiff();

        long x = calc("x");
        long y = calc("y");
        long z = calc("z");

        printBinary(x);
        printBinary(y);
        printBinary(x + y);
        printBinary(z);

        printBinary(diff);







        return -1;
    }

    private static void printBinary(long value) {
        System.out.printf("%46s%n", Long.toBinaryString(value));
    }
}
