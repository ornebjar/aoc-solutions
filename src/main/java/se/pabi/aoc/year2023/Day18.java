package se.pabi.aoc.year2023;

import se.phet.aoc.AdventOfCode;
import se.pabi.aoc.util.Util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
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

    private record HLine(long x, long sy, long ey) {
    }

    private static long solve(Input[] inputs) {
        var linesByStart = new HashMap<Long, ArrayList<HLine>>();
        var linesByEnd = new HashMap<Long, ArrayList<HLine>>();
        long min = 0;
        long max = 0;
        var pos = new Pos(0, 0);
        for (Input input : inputs) {
            if ((input.dir & 1) == 1) {
                HLine line = input.dir == 1
                        ? new HLine(pos.x, pos.y, pos.y + input.steps)
                        : new HLine(pos.x, pos.y - input.steps, pos.y);
                linesByStart.computeIfAbsent(line.sy, _ -> new ArrayList<>()).add(line);
                linesByEnd.computeIfAbsent(line.ey, _ -> new ArrayList<>()).add(line);
            }
            if (pos.y < min) {
                min = pos.y;
            }
            if (pos.y > max) {
                max = pos.y;
            }
            pos = pos.move(input.dir, input.steps);
        }
        var active = new PriorityQueue<>(Comparator.comparingLong(HLine::x));
        long result = 0;
        for (long y = min; y <= max; y++) {
            if (linesByStart.containsKey(y)) {
                active.addAll(linesByStart.get(y));
            }
            boolean include = true;
            boolean connected = true;
            var it = active.iterator();
            HLine from = it.next();
            HLine to;
            do {
                if (from.ey == y) {
                    it.remove();
                }
                to = it.next();
                if (include) {
                    if (connected) {
                        if (from.ey == y && to.sy == y) {
                            result += to.x - from.x;
                        } else if (from.ey == y && to.ey == y) {
                            result += to.x - from.x + 1;
                            include = false;
                        } else if (from.sy == y && to.sy == y) {
                            result += to.x - from.x + 1;
                            include = false;
                        } else if (from.sy == y && to.ey == y) {
                            result += to.x - from.x;
                        } else {
                            result += to.x - from.x + 1;
                            include = false;
                        }
                        connected = false;
                    } else {
                        result += to.x - from.x + 1;
                        include = false;
                        connected = true;
                    }
                } else {
                    if (connected) {
                        if (from.ey == y && to.sy == y) {
                            result += to.x - from.x + 1;
                        } else if (from.ey == y && to.ey == y) {
                            result += to.x - from.x + 1;
                            include = true;
                        } else if (from.sy == y && to.sy == y) {
                            result += to.x - from.x + 1;
                            include = true;
                        } else if (from.sy == y && to.ey == y) {
                            result += to.x - from.x + 1;
                        } else {
                            include = true;
                        }
                        connected = false;
                    } else {
                        result += to.x - from.x + 1;
                        include = true;
                        connected = true;
                    }
                }
                from = to;
            }
            while (it.hasNext());
            if (to.ey == y) {
                it.remove();
            }
        }

        return result;
    }

}
