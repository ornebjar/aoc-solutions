package se.pabi.aoc.year2022;

import se.phet.aoc.AdventOfCode;
import se.pabi.aoc.util.Util;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Day22 extends AdventOfCode<Day22.Input> {

    public record Input(String[] map, int w, int h, int s, String instructions) {
    }

    public Input input(String input) {
        input = """
                        ...#
                        .#..
                        #...
                        ....
                ...#.......#
                ........#...
                ..#....#....
                ..........#.
                        ...#....
                        .....#..
                        .#......
                        ......#.

                10R5L5R10L4R5L5""";

        var k = """
                
                  1122
                  1122
                  33
                  33
                4455
                4455
                66
                66
                
                """;

        String[] lines = input.lines().toArray(String[]::new);

        int w = Arrays.stream(lines).mapToInt(String::length).max().orElseThrow();
        int h = lines.length - 2;
        return new Input(
                Arrays.copyOf(lines, h),
                w,
                h,
                Util.gcd(w, h),
                lines[lines.length - 1]
        );
    }

    private final static int[] DX = new int[]{1, 0, -1, 0};
    private final static int[] DY = new int[]{0, 1, 0, -1};

    public Object part1(Input input) {
        var actions = Arrays.stream(Util.groups(
                input.instructions,
                Pattern.compile("((\\d+|[LR]))")
        )).toList();

        var y = 0;
        var x = input.map[0].indexOf('.');
        var f = 0;

        for (String action : actions) {
            switch (action) {
                case "R" -> f = (f + 5) % 4;
                case "L" -> f = (f + 3) % 4;
                default -> {
                    var nx = x;
                    var ny = y;
                    for (int i = 0; i < Integer.parseInt(action); i++) {
                        do {
                            nx = (nx + DX[f] + input.w) % input.w;
                            ny = (ny + DY[f] + input.h) % input.h;
                        } while (oob(input.map, nx, ny));
                        if (input.map[ny].charAt(nx) == '#') {
                            break;
                        }
                        x = nx;
                        y = ny;
                    }
                }
            }
        }

        return 1000 * (y + 1) + 4 * (x + 1) + f;
    }

    public Object part2(Input input) {
        System.out.println(String.join("\n", input.map));
        System.out.println(input.instructions);

        var actions = Arrays.stream(Util.groups(
                input.instructions,
                Pattern.compile("((\\d+|[LR]))")
        )).toList();

        var y = 0;
        var x = input.map[0].indexOf('.');
        var f = 0;

        for (String action : actions) {
            switch (action) {
                case "R" -> f = rot(f, 1);
                case "L" -> f = rot(f, -1);
                default -> {
                    var nx = x;
                    var ny = y;
                    var nf = f;
                    for (int i = 0; i < Integer.parseInt(action); i++) {

                        if (oob(input.map, nx + DX[nf], ny + DY[nf])) {
                            int fx = nx / input.s;
                            int fy = ny / input.s;


                        } else {
                            nx += DX[nf];
                            ny += DY[nf];
                        }

                        if (input.map[ny].charAt(nx) == '#') {
                            break;
                        }
                        x = nx;
                        y = ny;
                        f = nf;
                    }
                }
            }
        }

        return 1000 * (y + 1) + 4 * (x + 1) + f;
    }

    private static int rot(int facing, int angle) {
        return (facing + 5 + angle) % 4;
    }

    private static boolean oob(String[] map, int nx, int ny) {
        return ny < 0 || ny >= map.length || nx < 0 || nx >= map[ny].length() || map[ny].charAt(nx) == ' ';
    }
}
