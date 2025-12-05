package se.orne.aoc.year2024;

import se.orne.aoc.AdventOfCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

public class Day11 extends AdventOfCode<LongStream> {

    @Override
    public LongStream input(String input) {
        return Arrays.stream(input.trim().split(" ")).mapToLong(Long::parseLong);
    }

    @Override
    public Object part1(LongStream input) {
        return input.map(n -> calc(n, 25)).sum();
    }

    @Override
    public Object part2(LongStream input) {
        return input.map(n -> calc(n, 75)).sum();
    }

    private record Key(long n, int level) {
    }

    private static final Map<Key, Long> CACHE = new HashMap<>();

    private static long calc(long n, int level) {
        if (level == 0) {
            return 1;
        }
        Key key = new Key(n, level);
        if (CACHE.containsKey(key)) {
            return CACHE.get(key);
        }
        long result;
        if (n == 0) {
            result = calc(1, level - 1);
        } else {
            var string = String.valueOf(n);
            if (string.length() % 2 == 0) {
                result = calc(Long.parseLong(string.substring(0, string.length() / 2)), level - 1) +
                        calc(Long.parseLong(string.substring(string.length() / 2)), level - 1);
            } else {
                result = calc(n * 2024, level - 1);
            }
        }
        CACHE.put(key, result);
        return result;
    }
}
