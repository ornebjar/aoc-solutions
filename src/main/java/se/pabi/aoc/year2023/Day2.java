package se.pabi.aoc.year2023;

import se.phet.aoc.AdventOfCode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day2 extends AdventOfCode<Stream<String>> {

    @Override
    public Stream<String> input(String input) {
        return input.lines();
    }

    enum Color {
        red, green, blue
    }

    @Override
    public Object part1(Stream<String> input) {
        var available = Map.of(
                Color.red, 12,
                Color.green, 13,
                Color.blue, 14
        );
        return input.filter(line -> parseLine(line).stream().allMatch(set ->
                set.entrySet().stream().allMatch(entry ->
                        available.get(entry.getKey()) >= entry.getValue()
                ))).mapToInt(line -> Integer.parseInt(line.split(": ")[0].split(" ")[1])).sum();
    }

    @Override
    public Object part2(Stream<String> input) {
        return input.mapToInt(line -> {
            var sets = parseLine(line);
            return Stream.of(Color.red, Color.green, Color.blue)
                    .mapToInt(color -> sets.stream()
                            .mapToInt(set -> set.getOrDefault(color, 0)).max()
                            .orElse(0)
                    ).reduce(1, (a, b) -> a * b);
        }).sum();
    }

    private List<Map<Color, Integer>> parseLine(String line) {
        var lineSplit = line.split(": ");
        return Arrays.stream(lineSplit[1].split("; ")).map(set ->
                Arrays.stream(set.split(", ")).map(hand -> {
                    var handSplit = hand.split(" ");
                    var color = Color.valueOf(handSplit[1]);
                    var num = Integer.parseInt(handSplit[0]);
                    return Map.entry(color, num);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        ).toList();
    }
}
