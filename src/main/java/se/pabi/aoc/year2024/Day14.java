package se.pabi.aoc.year2024;

import se.phet.aoc.AdventOfCode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day14 extends AdventOfCode<Stream<Day14.Robot>> {

    record Point(int x, int y) {
        public Point add(Point other) {
            return new Point(x + other.x, y + other.y);
        }
        public Point multiply(int factor) {
            return new Point(x * factor, y * factor);
        }
    }

    public record Robot(Point position, Point direction) {
    }

    @Override
    public Stream<Robot> input(String input) {
        var pattern = Pattern.compile("p=(.+),(.+) v=(.+),(.+)");
        return input.lines()
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(matcher -> new Robot(
                        new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
                        new Point(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)))
                ));
    }

    @Override
    public Object part1(Stream<Robot> input) {
        var width = 101;
        var height = 103;
        Map<Point, Integer> map = new HashMap<>();
        input.forEach(robot -> {
            var position = robot.position.add(robot.direction.multiply(100));
            var x = mod0to(position.x, width);
            var y = mod0to(position.y, height);
            map.merge(new Point(signum(x, width), signum(y, height)), 1, Integer::sum);
        });
        return Stream.of(
                new Point(1, 1),
                new Point(1, -1),
                new Point(-1, 1),
                new Point(-1, -1)
        ).mapToInt(q -> map.getOrDefault(q, 0)).reduce(1, (a, b) -> a * b);
    }

    private static int signum(int p, int size) {
        //noinspection IntegerDivisionInFloatingPointContext
        return (int) Math.signum(p - (size / 2));
    }

    private static int mod0to(int value, int size) {
        return (value % size + size) % size;
    }

    @Override
    public boolean progressTracking() {
        return false;
    }

    @Override
    public Object part2(Stream<Robot> input) {
        List<Robot> robots = input.toList();

        var target = "#".repeat(10);

        for (int i = 1; i < 100000; i++) {
            int finalI = i;
            var formation = robots.stream().map(robot ->{
                var next = robot.position.add(robot.direction.multiply(finalI));
                return new Point(mod0to(next.x, 101), mod0to(next.y, 103));
            } ).collect(Collectors.toSet());


            List<String> lines = new ArrayList<>();
            for (int y = 0; y < 103; y++) {
                StringBuilder line = new StringBuilder();
                for (int x = 0; x < 101; x++) {
                    line.append(formation.contains(new Point(x, y)) ? '#' : '·');
                }
                lines.add(line.toString());
            }

            if (lines.stream().anyMatch(line -> line.contains(target))) {
                print(lines);
                return i;
            }
        }

        return 0;
    }

    private static void print(List<String> lines) {
        lines.add(" ".repeat(lines.getFirst().length()));
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < lines.size(); y+=2) {
            var line1 = lines.get(y);
            var line2 = lines.get(y + 1);
            for (int x = 0; x < line1.length(); x++) {
                var upper = line1.charAt(x) == '#';
                var lower = line2.charAt(x) == '#';
                sb.append(upper && lower ? '█' : upper ? '▀' : lower ? '▄' : ' ');
            }
            sb.append('\n');
        }
        System.out.print(sb);
    }

}