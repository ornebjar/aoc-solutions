package se.pabi.aoc.year2023;

import se.pabi.aoc.base.AdventOfCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Day15 extends AdventOfCode<String[]> {

    @Override
    public String[] input(String input) {
        return input.lines().findFirst().orElseThrow().split(",");
    }

    @Override
    public Object part1(String[] input) {
        return Arrays.stream(input)
                .mapToLong(Day15::hash)
                .sum();
    }

    @Override
    public Object part2(String[] input) {
        var boxes = new HashMap<Integer, LinkedHashMap<String, Integer>>();
        Arrays.stream(input).forEach(x -> {
            if (x.charAt(x.length()-1) == '-') {
                String name = x.substring(0, x.length() - 1);
                int hash = hash(name);
                var box = boxes.computeIfPresent(hash, (i, l) -> {
                    l.remove(name);
                    return l;
                });
                if (box != null && box.isEmpty()) {
                    boxes.remove(hash);
                }
            } else {
                String[] split = x.split("=");
                String name = split[0];
                boxes.computeIfAbsent(hash(name), i -> new LinkedHashMap<>())
                        .put(name, Integer.parseInt(split[1]));
            }
        });
        return boxes.entrySet().stream()
                .mapToInt(entry -> {
                    int i = 0;
                    int value = 0;
                    for (Integer integer : entry.getValue().values()) {
                        value += integer * ++i;
                    }
                    return value * (entry.getKey() + 1);
                })
                .sum();
    }

    private static int hash(String word) {
        return word.chars().reduce(0, (r, c) -> ((r + c) * 17) & 255);
    }
}
