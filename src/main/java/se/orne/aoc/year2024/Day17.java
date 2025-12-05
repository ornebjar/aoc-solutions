package se.orne.aoc.year2024;

import se.orne.aoc.AdventOfCode;

import java.util.Arrays;
import java.util.function.LongUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day17 extends AdventOfCode<int[]> {

    private long A;
    private long B;
    private long C;

    @Override
    public int[] input(String input) {
        String[] split = input.split("\n");
        A = Long.parseLong(split[0].split(": ")[1]);
        B = Long.parseLong(split[1].split(": ")[1]);
        C = Long.parseLong(split[2].split(": ")[1]);
        return Arrays.stream(split[4]
                        .split(" ")[1]
                        .split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    @Override
    public Object part1(int[] input) {
        var result = LongStream.builder();

        for (int i = 0; i < input.length; i+=2) {
            int op = input[i];
            int lit = input[i + 1];
            long combo = switch (lit) {
                case 4 -> A;
                case 5 -> B;
                case 6 -> C;
                default -> lit;
            };
            switch (op) {
                case 0 -> A >>= combo;
                case 1 -> B ^= lit;
                case 2 -> B = combo % 8;
                case 3 -> {
                    if (A != 0) {
                        i = lit - 2;
                    }
                }
                case 4 -> B ^= C;
                case 5 -> result.add(combo % 8);
                case 6 -> B = A >> combo;
                case 7 -> C = A >> combo;
            }
        }

        return result.build()
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public Object part2(int[] input) {
        var tail = Arrays.copyOfRange(input, input.length - 6, input.length);
        if (tail[4] != 3 || tail[5] != 0) {
            return "Assuming end of program is 3,0 = JUMP 0";
        }
        if (tail[2] != 0 || tail[3] != 3) {
            return "Assuming end of program 0,3,.,. = A <<= 3";
        }
        if (tail[0] != 5 || tail[1] < 4 || tail[1] > 6) {
            return "Assuming end of program is 5,.,.,.,.,. = OUTPUT A, B or C";
        }

        LongUnaryOperator program = (a) -> {
            long b = 0;
            long c = 0;
            for (int i = 0; i < input.length - 6; i+=2) {
                int lit = input[i + 1];
                long combo = switch (lit) {
                    case 4 -> a;
                    case 5 -> b;
                    case 6 -> c;
                    default -> lit;
                };
                switch (input[i]) {
                    case 0 -> a >>= combo;
                    case 1 -> b ^= lit;
                    case 2 -> b = combo % 8;
                    case 4 -> b ^= c;
                    case 6 -> b = a >> combo;
                    case 7 -> c = a >> combo;
                }
            }
            return switch (tail[1]) {
                case 4 -> a;
                case 5 -> b;
                case 6 -> c;
                default -> -1;
            } % 8;
        };

        return findA(0L, input.length - 1, input, program);
    }

    private long findA(long a, int pos, int[] input, LongUnaryOperator program) {
        if (pos < 0) {
            return a;
        }
        a <<= 3;
        long target = input[pos];
        for (int i = 0; i < 8; i++, a++) {
            if (program.applyAsLong(a) == target) {
                long result = findA(a, pos - 1, input, program);
                if (result != -1) {
                    return result;
                }
            }
        }
        return -1;
    }

}
