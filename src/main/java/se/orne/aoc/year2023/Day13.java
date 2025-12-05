package se.orne.aoc.year2023;

import se.orne.aoc.AdventOfCode;

import java.util.Arrays;
import java.util.stream.Stream;

public class Day13 extends AdventOfCode<Stream<char[][]>> {
    @Override
    public Stream<char[][]> input(String input) {
        return Arrays.stream(input.split("\n\n"))
                .map(s -> s.lines().map(String::toCharArray).toArray(char[][]::new));
    }

    @Override
    public Object part1(Stream<char[][]> input) {
        return input.mapToLong(i -> calc(i, 0)).sum();
    }

    @Override
    public Object part2(Stream<char[][]> input) {
        return input.mapToLong(i -> {
            var part1Score = calc(i, 0);
            for (int n = 0; n < i.length; n++) {
                for (int m = 0; m < i[n].length; m++) {
                    switch (i[n][m]) {
                        case '.' -> {
                            i[n][m] = '#';
                            var score = calc(i, part1Score);
                            if (score > 0) {
                                return score;
                            }
                            i[n][m] = '.';
                        }
                        case '#' -> {
                            i[n][m] = '.';
                            var score = calc(i, part1Score);
                            if (score > 0) {
                                return score;
                            }
                            i[n][m] = '#';
                        }
                    }
                }
            }
            throw new RuntimeException("No solution found");
        }).sum();
    }

    private static long calc(char[][] map, long skip) {
        for (int r = 0; r < map.length - 1; r++) {
            if (isMirrored(map, r)) {
                long score = (r + 1) * 100L;
                if (score != skip) {
                    return score;
                }
            }
        }

        map = transpose(map);

        for (int r = 0; r < map.length - 1; r++) {
            if (isMirrored(map, r)) {
                int score = r + 1;
                if (score != skip) {
                    return score;
                }
            }
        }

        return 0;
    }

    private static boolean isMirrored(char[][] map, int r) {
        for (int n = r + 1; r >= 0 && n < map.length; r--, n++) {
            if (!Arrays.equals(map[r], map[n])) {
                return false;
            }
        }
        return true;
    }

    public static char[][] transpose(char[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        char[][] transposedMatrix = new char[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }
        return transposedMatrix;
    }
}
