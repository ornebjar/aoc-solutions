package se.orne.aoc.year2023;

import se.orne.aoc.AdventOfCode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

public class Day14 extends AdventOfCode<char[][]> {
    @Override
    public char[][] input(String input) {
        return input.lines().map(String::toCharArray).toArray(char[][]::new);
    }

    @Override
    public Object part1(char[][] input) {
        tilt(input, 1, 0, 0, 0);
        return score(input);
    }

    @Override
    public Object part2(char[][] input) {
        var scores = new LinkedList<Long>();
        for (int i = 0; i < 160; i++) {
            tilt(input);
            scores.push(score(input));
        }
        var cycle = new LinkedList<Long>();
        while (!scores.isEmpty()) {
            cycle.add(scores.pop());
            Iterator<Long> scoresIterator = scores.iterator();
            boolean cycleFound = true;
            for (int i = 0; i < 5; i++) {
                if (cycle.stream().anyMatch(c -> {
                    Long score = scoresIterator.next();
                    return !Objects.equals(c, score);
                })) {
                    cycleFound = false;
                    break;
                }
            }
            if (cycleFound) {
                return cycle.get(cycle.size() - (int) ((1000000000L - 160) % cycle.size()));
            }
        }

        throw new RuntimeException("No solution found");
    }

    private static long score(char[][] input) {
        long sum = 0;
        for (int n = 0; n < input.length; n++) {
            for (int m = 0; m < input[n].length; m++) {
                if (input[n][m] == 'O') {
                    sum += input.length - n;
                }
            }
        }
        return sum;
    }

    private static void tilt(char[][] input) {
        tilt(input, 1, 0, 0, 0);
        tilt(input, 0, 1, 0, 0);
        tilt(input, 0, 0, 1, 0);
        tilt(input, 0, 0, 0, 1);
    }

    private static void tilt(char[][] input, int n, int w, int s, int e) {
        boolean unchanged = true;
        while (unchanged) {
            unchanged = false;
            for (int y = n; y < input.length - s; y++) {
                for (int x = w; x < input[y].length - e; x++) {
                    if (input[y][x] == 'O' && input[y - n + s][x - w + e] == '.') {
                        input[y - n + s][x - w + e] = 'O';
                        input[y][x] = '.';
                        unchanged = true;
                    }
                }
            }
        }
    }
}
