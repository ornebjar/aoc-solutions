package se.pabi.aoc.year2024;

import se.phet.aoc.AdventOfCode;

public class Day4 extends AdventOfCode<char[][]> {

    @Override
    public char[][] input(String input) {
        return input.lines()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    @Override
    public Object part1(char[][] input) {
        int count = 0;
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[0].length; x++) {
                count += hasXmas(input, x, y);
            }
        }
        return count;

    }

    private int hasXmas(char[][] input, int x, int y) {
        return hasXmas(input, x, y, 0, 1) +
                hasXmas(input, x, y, 0, -1) +
                hasXmas(input, x, y, 1, 0) +
                hasXmas(input, x, y, -1, 0) +
                hasXmas(input, x, y, 1, 1) +
                hasXmas(input, x, y, 1, -1) +
                hasXmas(input, x, y, -1, 1) +
                hasXmas(input, x, y, -1, -1);
    }

    private static final char[] XMAS = "XMAS".toCharArray();

    private int hasXmas(char[][] input, int x, int y, int dx, int dy) {
        for (int i = 0; i < XMAS.length; i++) {
            int xx = x + i * dx;
            int yy = y + i * dy;
            if (hasChar(input, xx, yy, XMAS[i])) {
                return 0;
            }
        }
        return 1;
    }

    @Override
    public Object part2(char[][] input) {
        int count = 0;
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[0].length; x++) {
                if (hasMas(input, x, y)) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean hasMas(char[][] input, int x, int y) {
        return (
                (hasChar(input, x-1, y-1, 'M') && hasChar(input, x, y, 'A') && hasChar(input, x + 1, y + 1, 'S')) ||
                (hasChar(input, x+1, y+1, 'M') && hasChar(input, x, y, 'A') && hasChar(input, x - 1, y - 1, 'S'))
        ) && (
                (hasChar(input, x-1, y+1, 'M') && hasChar(input, x, y, 'A') && hasChar(input, x + 1, y - 1, 'S')) ||
                (hasChar(input, x+1, y-1, 'M') && hasChar(input, x, y, 'A') && hasChar(input, x - 1, y + 1, 'S'))
        );
    }

    private boolean hasChar(char[][] input, int x, int y, char m) {
        return x >= 0 && x < input[0].length && y >= 0 && y < input.length && input[y][x] == m;
    }
}

