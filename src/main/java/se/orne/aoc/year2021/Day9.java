package se.orne.aoc.year2021;

import se.orne.aoc.AdventOfCode;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;

public class Day9 extends AdventOfCode<String[]> {
    @Override
    public String[] input(String input) {
        return input.lines().toArray(String[]::new);
    }

    @Override
    public Object part1(String[] input) {
        ArrayList<Character> low = new ArrayList<>();
        for (int r = 0; r < input.length; r++) {
            String row = input[r];
            for (int c = 0; c < row.length(); c++) {
                char point = row.charAt(c);
                if ((c == 0 || point < row.charAt(c-1)) && (c == row.length()-1 || point < row.charAt(c+1)) &&
                        (r == 0 || point < input[r-1].charAt(c)) && (r == input.length-1 || point < input[r+1].charAt(c))) {
                    low.add(point);
                }
            }
        }
        return low.stream().mapToInt(c -> c - '0').map(i -> i+1).sum();
    }

    record Low(char c, Point p) {}

    @Override
    public Object part2(String[] input) {
        List<Integer> basins = new ArrayList<>();
        for (int r = 0; r < input.length; r++) {
            String row = input[r];
            for (int c = 0; c < row.length(); c++) {
                char point = row.charAt(c);
                if ((c == 0 || point < row.charAt(c-1)) && (c == row.length()-1 || point < row.charAt(c+1)) &&
                        (r == 0 || point < input[r-1].charAt(c)) && (r == input.length-1 || point < input[r+1].charAt(c))) {

                    LinkedList<Low> open = new LinkedList<>();
                    open.add(new Low(point, new Point(r, c)));

                    Set<Point> basin = new HashSet<>();
                    for (Low next = open.pollFirst(); next != null; next = open.pollFirst()) {
                        if (basin.add(next.p)) {
                            char nextc = next.c;
                            BiConsumer<Integer, Integer> addOpen = (x, y) -> {
                                char step = input[x].charAt(y);
                                if (step > nextc && step != '9') {
                                    open.add(new Low(step, new Point(x, y)));
                                }
                            };
                            if (next.p.y > 0) {
                                addOpen.accept(next.p.x, next.p.y - 1);
                            }
                            if (next.p.y < input.length - 1) {
                                addOpen.accept(next.p.x, next.p.y + 1);
                            }
                            if (next.p.x > 0) {
                                addOpen.accept(next.p.x - 1, next.p.y);
                            }
                            if (next.p.x < row.length() - 1) {
                                addOpen.accept(next.p.x + 1, next.p.y);
                            }
                        }
                    }
                    basins.add(basin.size());
                }
            }
        }
        return basins.stream().sorted(Comparator.reverseOrder()).limit(3).reduce(1, (a, b) -> a * b);
    }

}
