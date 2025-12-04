package se.pabi.aoc.year2025;

import se.phet.aoc.AdventOfCode;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Day4 extends AdventOfCode<boolean[][]> {

    @Override
    public boolean[][] input(String input) {
        return input.lines().map(line -> line.chars()
                        .mapToObj(c -> c == '@')
                        .toArray(Boolean[]::new))
                .map(arr -> {
                    boolean[] boolArr = new boolean[arr.length];
                    for (int i = 0; i < arr.length; i++) {
                        boolArr[i] = arr[i];
                    }
                    return boolArr;
                })
                .toArray(boolean[][]::new);
    }

    private static int adjPaperRoll(boolean[][] grid, int ox, int oy) {
        int count = 0;
        for (int dy = max(0, oy - 1); dy < min(grid.length, oy + 2); dy++) {
            for (int dx = max(0, ox - 1); dx < min(grid[0].length, ox + 2); dx++) {
                if (grid[dy][dx]) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public Object part1(boolean[][] input) {
        int total = 0;
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[0].length; x++) {
                if (input[y][x] && adjPaperRoll(input, x, y) < 5) {
                    total++;
                }
            }
        }
        return total;
    }

    @Override
    public Object part2(boolean[][] input) {
        int total = 0;
        var run = true;
        while (run) {
            run = false;
            for (int y = 0; y < input.length; y++) {
                for (int x = 0; x < input[0].length; x++) {
                    if (input[y][x] && adjPaperRoll(input, x, y) < 5) {
                        run = true;
                        input[y][x] = false;
                        total++;
                    }
                }
            }
        }
        return total;
    }

}
