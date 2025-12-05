package se.orne.aoc.year2022;

import se.orne.aoc.AdventOfCode;

import java.util.List;

public class Day10 extends AdventOfCode<List<Day10.Input>> {

    enum Cmd {
        addx, noop
    }

    public record Input(Cmd cmd, int value) {
    }

    @Override
    public List<Input> input(String input) {
        return input.lines().map(line -> {
            var parts = line.split(" ");
            return new Input(Cmd.valueOf(parts[0]), line.split(" ").length == 1
                    ? 0
                    : Integer.parseInt(line.split(" ")[1]));
        }).toList();
    }

    @Override
    public Object part1(List<Input> inputs) {
        int x = 1;
        int sum = 0;
        int cycle = 0;
        int next = 20;
        for (var input : inputs) {
            switch (input.cmd) {
                case noop -> cycle++;
                case addx -> cycle += 2;
            }

            if (cycle >= next) {
                sum += x * next;
                next += 40;
            }

            if (input.cmd == Cmd.addx) {
                x += input.value;
            }
        }
        return sum;
    }

    @Override
    public Object part2(List<Input> inputs) {
        int x = 1;
        int cycle = 0;
        String[] rows = new String[] {"", "", "", "", "", ""};
        for (var input : inputs) {

            rows[cycle / 40] += (cycle%40) >= x-1 && (cycle%40) <= x+1 ? "#" : ".";
            cycle++;

            if (input.cmd == Cmd.addx) {
                rows[cycle / 40] += (cycle%40) >= x-1 && (cycle%40) <= x+1 ? "#" : ".";
                cycle++;

                x += input.value;
            }
        }
        return "\n" + String.join("\n", rows);
    }

}
