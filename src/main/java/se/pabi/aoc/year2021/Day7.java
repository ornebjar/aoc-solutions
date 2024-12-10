package se.pabi.aoc.year2021;

import se.phet.aoc.AdventOfCode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Day7 extends AdventOfCode<List<Integer>> {
    @Override
    public List<Integer> input(String input) {
        return Arrays.stream(input.trim().split(",")).map(Integer::parseInt).toList();
    }

    @Override
    public Object part1(List<Integer> crabs) {
        return IntStream.rangeClosed(Collections.min(crabs), Collections.max(crabs)).map(i ->
                crabs.stream().mapToInt(v -> Math.abs(v - i)).sum()
        ).min().orElseThrow();
    }

    @Override
    public Object part2(List<Integer> crabs) {
        return IntStream.rangeClosed(Collections.min(crabs), Collections.max(crabs)).map(i ->
                crabs.stream().mapToInt(v -> Math.abs(v - i)).map(v -> (v * (v+1)) / 2).sum()
        ).min().orElseThrow();
    }
}
