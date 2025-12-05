package se.orne.aoc.year2015;

import se.orne.aoc.AdventOfCode;

import java.util.List;

public class Day2 extends AdventOfCode<List<Day2.Box>> {

    public record Box(int l, int w, int h) {
    }

    @Override
    public List<Box> input(String input) {
        return input.lines()
                .map(line -> line.split("x"))
                .map(split -> new Box(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])))
                .toList();
    }

    @Override
    public Object part1(List<Box> input) {
        int p = 0;

        for (Box box : input) {
            int s1 = box.l * box.w;
            int s2 = box.w * box.h;
            int s3 = box.l * box.h;

            p += 2 * (s1 + s2 + s3) + Math.min(s1, Math.min(s2, s3));
        }

        return p;
    }

    @Override
    public Object part2(List<Box> input) {
        int r = 0;

        for (Box box : input) {
            int s1 = box.w;
            int s2 = box.h;
            int s3 = box.l;

            r += 2 * (s1 + s2 + s3 - Math.max(s1, Math.max(s2, s3))) + (s1*s2*s3);
        }

        return r;
    }
}
