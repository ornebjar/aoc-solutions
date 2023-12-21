package se.pabi.aoc.year2023;

import se.pabi.aoc.base.AdventOfCode;
import se.pabi.aoc.util.Util;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day18 extends AdventOfCode<Stream<String[]>> {

    public record Input(int dir, int steps) {
    }

    @Override
    public Stream<String[]> input(String input) {
//        input = """
//                R 6 (#70c710)
//                D 5 (#0dc571)
//                L 2 (#5713f0)
//                D 2 (#d2c081)
//                R 2 (#59c680)
//                D 2 (#411b91)
//                L 5 (#8ceee2)
//                U 2 (#caa173)
//                L 1 (#1b58a2)
//                U 2 (#caa171)
//                R 2 (#7807d2)
//                U 3 (#a77fa3)
//                L 2 (#015232)
//                U 2 (#7a21e3)
//                """;
        var pattern = Pattern.compile("^([RDLU]) (\\d+) \\(#(\\w+)\\)$");
        return input.lines().map(line -> Util.groups(line, pattern));
    }

    private record Pos(long x, long y) {
        Pos move(int dir, long steps) {
            return switch (dir) {
                case 0 -> new Pos(x + steps, y);
                case 1 -> new Pos(x, y + steps);
                case 2 -> new Pos(x - steps, y);
                case 3 -> new Pos(x, y - steps);
                default -> throw new IllegalArgumentException("Unknown direction " + dir);
            };
        }
    }

    @Override
    public Object part1(Stream<String[]> lines) {
        return solve(lines
                .map(input -> new Input("RDLU".indexOf(input[0]), Integer.parseInt(input[1])))
                .toArray(Input[]::new));
    }

    private static long solve(Input[] inputs) {
        var min = new Pos(0, 0);
        var max = new Pos(0, 0);
        Pos pos = new Pos(0, 0);
        for (Input input : inputs) {
            pos = pos.move(input.dir, input.steps);
            min = new Pos(Math.min(min.x, pos.x), Math.min(min.y, pos.y));
            max = new Pos(Math.max(max.x, pos.x), Math.max(max.y, pos.y));
        }

        int startIndex = 0;
        for (; startIndex < inputs.length; startIndex++) {
            if (pos.x == min.x || pos.x == max.x || pos.y == min.y || pos.y == max.y) {
                break;
            }
            Input input = inputs[startIndex];
            pos = pos.move(input.dir, input.steps);
        }


        var prevDir = inputs[startIndex].dir;
        long prevStep = inputs[startIndex].steps;
        pos = pos.move(prevDir, prevStep);
        long exclude = 0L;
        for (int i = 1; i < inputs.length; i++) {
            Input input = inputs[i];
            pos = pos.move(input.dir, input.steps);

            if ((input.dir + 1) % 4 == prevDir) { // left rot
                exclude += prevStep * input.steps;
            } else if ((input.dir + 3) % 4 == prevDir) { // right rot
                prevStep = input.steps;
                prevDir = input.dir;
            } else if (input.dir == prevDir) { // same dir
                prevStep += input.steps;
            } else { // opposite dir
                exclude -= prevStep;
                prevStep = input.steps - prevStep;
                prevDir = input.dir;
            }
        }
        return (max.x - min.x + 1) * (max.y - min.y + 1) - exclude;
    }

}
