package se.orne.aoc.year2023;

import se.orne.aoc.AdventOfCode;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Day6 extends AdventOfCode<Day6.Race> {

    public record Race(String time, String record) {
    }

    @Override
    public Race input(String input) {
        var race = input.lines()
                .map(line -> Arrays.stream(line.split(": +")).skip(1).findFirst().orElseThrow())
                .toArray(String[]::new);
        return new Race(race[0], race[1]);
    }

    @Override
    public Object part1(Race races) {
        int[] times = Arrays.stream(races.time.split(" +"))
                .mapToInt(Integer::parseInt)
                .toArray();
        int[] records = Arrays.stream(races.record.split(" +"))
                .mapToInt(Integer::parseInt)
                .toArray();

        return IntStream.range(0, times.length).mapToLong(i -> {
            var t = times[i];
            var r = records[i];
            return possibles(t, r);
        }).reduce(1, (a, b) -> a * b);
    }

    @Override
    public Object part2(Race input) {
        var time = Long.parseLong(input.time.replaceAll(" +", ""));
        var record = Long.parseLong(input.record.replaceAll(" +", ""));

        return possibles(time, record);
    }

    private static long possibles(long time, long record) {
        double v = Math.sqrt(Math.pow(time, 2) - 4 * record);
        long bMin = (long) Math.floor((time - v) / 2 + 1);
        long bMax = (long) Math.ceil((time + v) / 2 - 1);
        return bMax - bMin + 1;
    }
}
