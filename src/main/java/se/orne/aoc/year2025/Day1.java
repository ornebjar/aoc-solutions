package se.orne.aoc.year2025;

import se.orne.aoc.AdventOfCode;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Day1 extends AdventOfCode<IntStream> {

    @Override
    public IntStream input(String input) {
        return input.lines().mapToInt(line -> {
            int rot = Integer.parseInt(line.substring(1));
            return line.charAt(0) == 'L' ? -rot : rot;
        });
    }

    @Override
    public Object part1(IntStream input) {
        var dial = new AtomicInteger(50);
        return input
                .map(delta -> dial.updateAndGet(d -> (d + delta) % 100))
                .filter(i -> i == 0)
                .count();
    }

    @Override
    public Object part2(IntStream input) {
        var dial = new AtomicInteger(50);
        return input.map(delta -> {
                    int deltaSign = Integer.signum(delta);
                    int count = 0;
                    for (int i = 0; i < Math.abs(delta); i++) {
                        int newPos = (dial.get() + deltaSign + 100) % 100;
                        dial.set(newPos);
                        if (newPos == 0) {
                            count++;
                        }
                    }
                    return count;
                })
                .sum();
    }

}
