package se.pabi.aoc.year2021;

import se.phet.aoc.AdventOfCode;

import java.util.List;

public class Day2 extends AdventOfCode<List<Day2.Move>> {

    public record Move(String dir, int len) {}

    @Override
    public List<Move> input(String input) {
        return input.lines()
                .map(line -> line.split(" "))
                .map(split -> new Move(split[0], Integer.parseInt(split[1])))
                .toList();
    }

    @Override
    public Object part1(List<Move> rows) {
        int h = 0;
        int d = 0;

        for (Move row : rows) {
            if (row.dir.equals("up")) {
                d-=row.len;
            } else if (row.dir.equals("down")) {
                d+=row.len;
            } else { // forward
                h+=row.len;
            }
        }

        return d*h;
    }

    @Override
    public Object part2(List<Move> rows) {
        int h = 0;
        int d = 0;
        int aim = 0;

        for (Move row : rows) {
            if (row.dir.equals("up")) {
                aim -= row.len;
            } else if (row.dir.equals("down")) {
                aim += row.len;
            } else { // forward
                h += row.len;
                d += aim*row.len;
            }
        }

        return d*h;
    }
}
