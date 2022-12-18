package se.pabi.aoc.year2022;

import se.pabi.aoc.AdventOfCode;
import se.pabi.aoc.Helper;

import java.awt.*;
import java.math.BigInteger;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Math.*;

public class Day15 extends AdventOfCode<Stream<Day15.Link>> {

    record Link(Point sensor, Point beacon) {
        int dist() {
            return abs(sensor.x - beacon.x) + abs(sensor.y - beacon.y);
        }
    }

    record Range(int from, int to) {
        int count() {
            return to - from + 1;
        }

        static LinkedList<Range> reduce(Collection<Range> ranges) {
            return ranges.stream()
                    .sorted(Comparator.comparingInt(Range::from))
                    .reduce(new LinkedList<>(), (merged, next) -> {
                        if (merged.isEmpty() || next.from > merged.getLast().to) {
                            merged.add(next);
                        } else {
                            Range last = merged.removeLast();
                            merged.add(new Range(last.from, Math.max(last.to, next.to)));
                        }
                        return merged;
                    }, (m, n) -> m);
        }
    }

    @Override
    public Stream<Link> input(String input) {
        Pattern pattern = Pattern.compile("^Sensor at x=(.*), y=(.*): closest beacon is at x=(.*), y=(.*)$");
        return input.lines()
                .map(line -> {
                    int[] v = Helper.groups(line, pattern).mapToInt(Integer::parseInt).toArray();
                    return new Link(new Point(v[0], v[1]), new Point(v[2], v[3]));
                });
    }

    @Override
    public Object part1(Stream<Link> input) {
        List<Range> on = new ArrayList<>();
        Set<Integer> bx = new HashSet<>();
        int target = 2000000;

        input.forEach(link -> {
            int dist = link.dist();
            int to = abs(link.sensor.y - target);
            if (to <= dist) {
                int range = dist - to;
                on.add(new Range(link.sensor.x - range, link.sensor.x + range));
                if (link.beacon.y == target) {
                    bx.add(link.beacon.x);
                }
            }
        });

        List<Range> list = Range.reduce(on);
        if (list.size() != 1) {
            throw new IllegalStateException("Did not manage to reduce the range list");
        }
        return list.get(0).count() - bx.size();
    }

    @Override
    public Object part2(Stream<Link> input) {
        List<Link> links = input.toList();
        for (int i = 0; i < 4000000; i++) {
            int target = i;
            List<Range> on = new ArrayList<>();
            links.forEach(link -> {
                int dist = link.dist();
                int to = abs(link.sensor.y - target);
                if (to <= dist) {
                    int range = dist - to;
                    on.add(new Range(link.sensor.x - range, link.sensor.x + range));
                }
            });
            List<Range> list = Range.reduce(on);
            if (list.size() > 1) {
                return BigInteger.valueOf(list.get(0).to + 1)
                        .multiply(BigInteger.valueOf(4000000))
                        .add(BigInteger.valueOf(target));
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        new Day15();
    }

}
