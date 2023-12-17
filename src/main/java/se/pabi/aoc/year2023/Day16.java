package se.pabi.aoc.year2023;

import se.pabi.aoc.base.AdventOfCode;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

public class Day16 extends AdventOfCode<char[][]> {
    @Override
    public char[][] input(String input) {
        return input.lines().map(String::toCharArray).toArray(char[][]::new);
    }

    private record Pos(int x, int y) {}
    private enum Dir { UP, DOWN, LEFT, RIGHT }
    private record Beam(Pos pos, Dir dir) {}

    @Override
    public Object part1(char[][] input) {
        return energized(input, new Beam(new Pos(-1, 0), Dir.RIGHT));
    }

    @Override
    public Object part2(char[][] input) {
        int max = 0;
        for (int i = 0; i < input.length; i++) {
            int energized = Math.max(
                    energized(input, new Beam(new Pos(-1, i), Dir.RIGHT)),
                    energized(input, new Beam(new Pos(input[0].length, i), Dir.LEFT)));
            if (energized > max) {
                max = energized;
            }
        }
        for (int i = 0; i < input[0].length; i++) {
            int energized = Math.max(
                    energized(input, new Beam(new Pos(i, -1), Dir.DOWN)),
                    energized(input, new Beam(new Pos(i, input.length), Dir.UP)));
            if (energized > max) {
                max = energized;
            }
        }
        return max;
    }

    private static int energized(char[][] input, Beam start) {
        var energized = Map.of(
                Dir.LEFT, new HashSet<Pos>(),
                Dir.RIGHT, new HashSet<Pos>(),
                Dir.UP, new HashSet<Pos>(),
                Dir.DOWN, new HashSet<Pos>()
        );
        var beams = new LinkedList<Beam>();
        beams.add(start);
        while (!beams.isEmpty()) {
            var beam = beams.poll();
            var pos = switch (beam.dir) {
                case UP -> new Pos(beam.pos.x, beam.pos.y - 1);
                case DOWN -> new Pos(beam.pos.x, beam.pos.y + 1);
                case LEFT -> new Pos(beam.pos.x - 1, beam.pos.y);
                case RIGHT -> new Pos(beam.pos.x + 1, beam.pos.y);
            };
            if (pos.x < 0 || pos.y < 0 || pos.x >= input[0].length || pos.y >= input.length) {
                continue;
            }
            if (energized.get(beam.dir).add(pos)) {
                switch (input[pos.y][pos.x]) {
                    case '-' -> {
                        if (beam.dir == Dir.UP || beam.dir == Dir.DOWN) {
                            beams.add(new Beam(pos, Dir.LEFT));
                            beams.add(new Beam(pos, Dir.RIGHT));
                        } else {
                            beams.add(new Beam(pos, beam.dir));
                        }
                    }
                    case '|' -> {
                        if (beam.dir == Dir.LEFT || beam.dir == Dir.RIGHT) {
                            beams.add(new Beam(pos, Dir.UP));
                            beams.add(new Beam(pos, Dir.DOWN));
                        } else {
                            beams.add(new Beam(pos, beam.dir));
                        }
                    }
                    case '/' -> beams.add(new Beam(pos, switch (beam.dir) {
                        case UP -> Dir.RIGHT;
                        case DOWN -> Dir.LEFT;
                        case LEFT -> Dir.DOWN;
                        case RIGHT -> Dir.UP;
                    }));
                    case '\\' -> beams.add(new Beam(pos, switch (beam.dir) {
                        case UP -> Dir.LEFT;
                        case DOWN -> Dir.RIGHT;
                        case LEFT -> Dir.UP;
                        case RIGHT -> Dir.DOWN;
                    }));
                    case '.' -> beams.add(new Beam(pos, beam.dir));
                }
            }
        }
        return energized.values().stream().flatMap(Collection::stream).collect(Collectors.toSet()).size();
    }
}
