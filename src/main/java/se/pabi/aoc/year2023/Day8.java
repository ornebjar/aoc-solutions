package se.pabi.aoc.year2023;

import se.pabi.aoc.base.AdventOfCode;
import se.pabi.aoc.util.Util;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day8 extends AdventOfCode<Day8.Input> {

    public record Input(String directions, Map<String, Pair> map) {
    }

    private record Pair(String left, String right) {
    }

    @Override
    public Input input(String input) {
        String[] split = input.split("\n\n");
        Pattern pattern = Pattern.compile("^(.*) = \\((.*), (.*)\\)$");
        return new Input(split[0], split[1].lines().map(line -> Util.groups(line, pattern))
                .collect(Collectors.toMap(parts -> parts[0], parts -> new Pair(parts[1], parts[2]))));
    }

    @Override
    public Object part1(Input input) {
        int steps = 0;
        String pos = "AAA";
        while (!"ZZZ".equals(pos)) {
            if (input.directions().charAt(steps % input.directions.length()) == 'R') {
                pos = input.map.get(pos).right();
            } else {
                pos = input.map.get(pos).left();
            }
            steps++;
        }
        return steps;
    }

    @Override
    public Object part2(Input input) {
//        System.out.println(input.map);
        var cycles = new HashMap<String, List<Integer>>();
        input.map.entrySet().stream()
                .filter(entry -> entry.getKey().endsWith("A"))
                .forEach(entry -> {
                    var visited = new HashSet<String>();
                    var cycle = new ArrayList<Integer>();
                    var pos = entry.getKey();
                    int steps = 0;
                    while (!visited.contains(pos)) {
                        if (input.directions().charAt(steps % input.directions.length()) == 'R') {
                            pos = input.map.get(pos).right();
                        } else {
                            pos = input.map.get(pos).left();
                        }
                        if (pos.endsWith("Z")) {
                            visited.add(pos);
                            cycle.add(steps + 1);
                        }
                        steps++;
                    }
                    cycles.put(entry.getKey(), cycle);
                });

        System.out.println(cycles);

        return Util.lcm(cycles.values().stream()
                .map(List::getFirst)
                .map(BigInteger::valueOf)
                .toList());
    }
}
