package se.pabi.aoc.year2024;

import se.phet.aoc.AdventOfCode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.LongStream;

public class Day22 extends AdventOfCode<LongStream> {

    @Override
    public LongStream input(String input) {
        return input.lines().mapToLong(Long::parseLong);
    }

    private static long nextSequence(long i) {
        i ^= 64 * i;
        i %= 16777216;
        i ^= i / 32;
        i %= 16777216;
        i ^= i * 2048;
        i %= 16777216;
        return i;
    }

    @Override
    public Object part1(LongStream input) {
        return input.map(i -> {
            for (int k = 0; k < 2000; k++) {
                i = nextSequence(i);
            }
            return i;
        }).sum();
    }

    @Override
    public Object part2(LongStream input) {
        var values = new HashMap<Long, Long>();
        input.forEach(i -> {
            long[] price = new long[2000];
            for (int k = 0; k < 2000; k++) {
                i = nextSequence(i);
                price[k] = i % 10;
            }
            long[] changes = new long[1999];
            for (int k = 0; k < 1999; k++) {
                changes[k] = price[k + 1] - price[k];
            }
            Set<Long> history = new HashSet<>();
            for (int k = 4; k < 1999; k++) {
                long key = (changes[k - 4] + 10) * 8000 + (changes[k - 3] + 10) * 400 +
                        (changes[k - 2] + 10) * 20 + (changes[k - 1] + 10);
                if (history.add(key)) {
                    values.merge(key, price[k], Long::sum);
                }
            }
        });
        return values.values().stream()
                .max(Long::compare)
                .orElseThrow();
    }

}
