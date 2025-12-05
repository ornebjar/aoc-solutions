package se.orne.aoc.year2024;

import se.orne.aoc.util.IntPoint;
import se.orne.aoc.AdventOfCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day21 extends AdventOfCode<Stream<String>> {

    // +---+---+---+
    // | 7 | 8 | 9 |
    // +---+---+---+
    // | 4 | 5 | 6 |
    // +---+---+---+
    // | 1 | 2 | 3 |
    // +---+---+---+
    //     | 0 | A |
    //     +---+---+
    private static final IntPoint NUM_A = new IntPoint(2, 0);
    private static final IntPoint[] NUM_PAD = {
            new IntPoint(1, 0),
            new IntPoint(0, 1),
            new IntPoint(1, 1),
            new IntPoint(2, 1),
            new IntPoint(0, 2),
            new IntPoint(1, 2),
            new IntPoint(2, 2),
            new IntPoint(0, 3),
            new IntPoint(1, 3),
            new IntPoint(2, 3),
            NUM_A,
    };
    private final static IntPoint NUM_VOID = new IntPoint(0, 0);

    //     +---+---+
    //     | ^ | A |
    // +---+---+---+
    // | < | v | > |
    // +---+---+---+
    private static final IntPoint DIR_A = new IntPoint(2, 1);
    private static final IntPoint[] DIR_PAD = {
            new IntPoint(0, 0),
            new IntPoint(1, 0),
            new IntPoint(2, 0),
            new IntPoint(1, 1),
            DIR_A
    };
    private final static IntPoint DIR_VOID = new IntPoint(0, 1);

    private static final Map<IntPoint, Integer> DIR_MAP = Map.of(
            new IntPoint(-1, 0), 0,
            new IntPoint(0, -1), 1,
            new IntPoint(1, 0), 2,
            new IntPoint(0, 1), 3,
            new IntPoint(0, 0), 4
    );

    private static IntPoint numPosition(char key) {
        return key == 'A'
                ? NUM_A
                : NUM_PAD[key - '0'];
    }

    private static IntPoint dirPosition(IntPoint dir) {
        return DIR_PAD[DIR_MAP.get(dir)];
    }

    private record CacheKey(IntPoint from, IntPoint to, int level) {
    }

    private Map<CacheKey, Long> cache;

    @Override
    public Stream<String> input(String input) {
        cache = new HashMap<>();
        return input.lines();
    }

    private long getPresses(List<IntPoint> dirPresses, int level) {
        if (level == 1) {
            long presses = 0;
            IntPoint dirArm = DIR_A;
            for (IntPoint dirPress : dirPresses) {
                IntPoint dirTo = dirPosition(dirPress);
                IntPoint move = dirTo.subtract(dirArm);
                presses += move.length() + 1;
                dirArm = dirTo;
            }
            return presses;
        }
        long presses = 0;
        IntPoint dirArm = DIR_A;
        for (IntPoint dirPress : dirPresses) {
            IntPoint dirTo = dirPosition(dirPress);
            CacheKey key = new CacheKey(dirArm, dirTo, level - 1);
            if (cache.containsKey(key)) {
                presses += cache.get(key);
                dirArm = dirTo;
                continue;
            }
            long calc = simulate(dirArm, dirTo, DIR_VOID, level - 1);
            cache.put(key, calc);
            presses += calc;
            dirArm = dirTo;
        }
        return presses;
    }

    private static List<IntPoint> concat(List<IntPoint> a, List<IntPoint> b, IntPoint c) {
        List<IntPoint> result = new ArrayList<>(a);
        result.addAll(b);
        result.add(c);
        return result;
    }

    private long simulate(IntPoint from, IntPoint to, IntPoint voidPos, int directionalPads) {
        IntPoint move = to.subtract(from);
        int yDir = move.y() > 0 ? 1 : -1;
        int xDir = move.x() > 0 ? 1 : -1;
        IntPoint a = new IntPoint(0, 0);
        var yPresses = Stream.generate(() -> new IntPoint(0, yDir))
                .limit(Math.abs(move.y()))
                .toList();
        var xPresses = Stream.generate(() -> new IntPoint(xDir, 0))
                .limit(Math.abs(move.x()))
                .toList();
        if (from.y() == voidPos.y() && to.x() == voidPos.x()) {
            return getPresses(concat(yPresses, xPresses, a), directionalPads);
        } else if (from.x() == voidPos.x() && to.y() == voidPos.y()) {
            return getPresses(concat(xPresses, yPresses, a), directionalPads);
        }
        return Math.min(
                getPresses(concat(yPresses, xPresses, a), directionalPads),
                getPresses(concat(xPresses, yPresses, a), directionalPads));
    }

    private Object simulate(Stream<String> input, int directionalPads) {
        return input.mapToLong(line -> {
            long value = Long.parseLong(line.substring(0, line.length() - 1));
            long presses = 0;

            IntPoint numArm = NUM_A;
            for (char c : line.toCharArray()) {
                IntPoint to = numPosition(c);
                presses += simulate(numArm, to, NUM_VOID, directionalPads);
                numArm = to;
            }
            return presses * value;
        }).sum();
    }

    @Override
    public Object part1(Stream<String> input) {
        return simulate(input, 2);
    }

    @Override
    public Object part2(Stream<String> input) {
        return simulate(input, 25);
    }

}
