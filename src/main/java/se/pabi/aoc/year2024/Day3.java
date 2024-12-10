package se.pabi.aoc.year2024;

import se.pabi.aoc.base.AdventOfCode;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class Day3 extends AdventOfCode<String> {

    @Override
    public String input(String input) {
        return input;
    }

    @Override
    public Object part1(String input) {
        var pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
        return pattern.matcher(input).results()
                .mapToLong(m -> Long.parseLong(m.group(1)) * Long.parseLong(m.group(2)))
                .sum();
    }

    @Override
    public Object part2(String input) {
        var pattern = Pattern.compile("(mul|do|don't)\\((\\d+)?,?(\\d+)?\\)");
        var enabled = new AtomicBoolean(true);
        return pattern.matcher(input).results()
                .mapToLong(m -> {
                    switch (m.group(1)) {
                        case "mul" -> {
                            return enabled.get() ? Long.parseLong(m.group(2)) * Long.parseLong(m.group(3)) : 0;
                        }
                        case "do" -> enabled.set(true);
                        case "don't" -> enabled.set(false);
                    };
                    return 0;
                })
                .sum();
    }
}

