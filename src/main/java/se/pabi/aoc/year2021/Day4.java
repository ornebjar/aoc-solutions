package se.pabi.aoc.year2021;

import se.phet.aoc.AdventOfCode;

import java.util.Arrays;
import java.util.List;

public class Day4 extends AdventOfCode<String[]> {
    @Override
    public String[] input(String input) {
        return input.lines().toArray(String[]::new);
    }

    @Override
    public Object part1(String[] input) {
        var numbers = Arrays.stream(input[0].split(",")).map(Integer::parseInt).toList();
        int boardCount = input.length / 6;
        var boards = new int[boardCount][5][5];

        extract(input, boardCount, boards);

        for (Integer n : numbers) {
            for (int b = 0; b < boardCount; b++) {
                for (int r = 0; r < 5; r++) {
                    for (int c = 0; c < 5; c++) {
                        if (boards[b][r][c] == n) {
                            boards[b][r][c] = -1;
                            if (isMatch(r, c, boards[b])) {
                                return score(n, boards, b);
                            }
                        }
                    }
                }
            }
        }

        return 1;
    }

    @Override
    public Object part2(String[] input) {
        var numbers = Arrays.stream(input[0].split(","))
                .map(Integer::parseInt).toList();
        int boardCount = input.length / 6;
        var boards = new int[boardCount][5][5];

        extract(input, boardCount, boards);

        boolean[] winning = new boolean[boardCount];
        int winners = 0;

        for (Integer n : numbers) {
            for (int b = 0; b < boardCount; b++) {
                if (!winning[b]) {
                    for (int r = 0; r < 5; r++) {
                        for (int c = 0; c < 5; c++) {
                            if (boards[b][r][c] == n) {
                                boards[b][r][c] = -1;
                                if (isMatch(r, c, boards[b])) {

                                    if (++winners == boardCount) {
                                        return score(n, boards, b);
                                    } else {
                                        winning[b] = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return -7;
    }

    private static int score(Integer n, int[][][] boards, int b) {
        int sum = 0;
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (boards[b][y][x] > -1) {
                    sum += boards[b][y][x];
                }
            }
        }
        return n * sum;
    }

    private static boolean isMatch(int r, int c, int[][] boards) {
        return (boards[r][0] == -1 &&
                boards[r][1] == -1 &&
                boards[r][2] == -1 &&
                boards[r][3] == -1 &&
                boards[r][4] == -1) ||
                (boards[0][c] == -1 &&
                        boards[1][c] == -1 &&
                        boards[2][c] == -1 &&
                        boards[3][c] == -1 &&
                        boards[4][c] == -1);
    }

    private static void extract(String[] input, int boardCount, int[][][] boards) {
        for (int i = 0; i < boardCount; i++) {
            for (int r = 0; r < 5; r++) {
                List<Integer> row = Arrays.stream(input[2 + i * 6 + r].trim().split(" +"))
                        .map(Integer::parseInt).toList();
                for (int c = 0; c < 5; c++) {
                    boards[i][r][c] = row.get(c);
                }
            }
        }
    }
}
