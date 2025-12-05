package se.orne.aoc.year2021;

import se.orne.aoc.AdventOfCode;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;

public class Day11 extends AdventOfCode<String[]> {

    @Override
    public String[] input(String input) {
        return input.lines().toArray(String[]::new);
    }

    @Override
    public Object part1(String[] input) {
        int[][] oct = new int[input.length][];

        for (int i = 0; i < input.length; i++) {
            oct[i] = input[i].chars().map(c -> c - '0').toArray();
        }

        int flashed = 0;

        for (int day = 0; day < 100; day++) {

            var open = new LinkedList<Point>();
            var flash = new HashSet<Point>();

            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if (++oct[x][y] > 9) {
                        open.add(new Point(x, y));
                    }
                }
            }

            for (Point n = open.pollFirst(); n != null ; n = open.pollFirst()) {
                if (flash.add(n)) {
                    for (int x = Math.max(n.x-1, 0); x <= Math.min(n.x+1, 9); x++) {
                        for (int y = Math.max(n.y-1, 0); y <= Math.min(n.y+1, 9); y++) {
                            if ((x != n.x || y != n.y) && ++oct[x][y] > 9) {
                                open.add(new Point(x, y));
                            }
                        }
                    }
                }
            }

            for (Point n : flash) {
                oct[n.x][n.y] = 0;
            }

            flashed += flash.size();

        }

        return flashed;
    }

    @Override
    public Object part2(String[] input) {
        int[][] oct = new int[input.length][];

        for (int i = 0; i < input.length; i++) {
            oct[i] = input[i].chars().map(c -> c - '0').toArray();
        }

        for (int day = 0; day < 1000; day++) {

            var open = new LinkedList<Point>();
            var flash = new HashSet<Point>();

            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if (++oct[x][y] > 9) {
                        open.add(new Point(x, y));
                    }
                }
            }

            for (Point n = open.pollFirst(); n != null ; n = open.pollFirst()) {
                if (flash.add(n)) {
                    for (int x = Math.max(n.x-1, 0); x <= Math.min(n.x+1, 9); x++) {
                        for (int y = Math.max(n.y-1, 0); y <= Math.min(n.y+1, 9); y++) {
                            if ((x != n.x || y != n.y) && ++oct[x][y] > 9) {
                                open.add(new Point(x, y));
                            }
                        }
                    }
                }
            }

            if (flash.size() == 100) {
                return day+1;
            }

            for (Point n : flash) {
                oct[n.x][n.y] = 0;
            }

        }

        return -1;
    }

}
