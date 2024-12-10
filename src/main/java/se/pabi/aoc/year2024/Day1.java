package se.pabi.aoc.year2024;

import se.pabi.aoc.base.AdventOfCode;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static se.pabi.aoc.year2024.Day1.D1;

public class Day1 extends AdventOfCode<List<D1>> {

    public record D1(int a, int b) {
        D1(String line) {
            this(line.split(" {3}"));
        }
        D1(String[] line) {
            this(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
        }
    }

    @Override
    public List<D1> input(String input) {
        return input.lines().map(D1::new).toList();
    }

    @Override
    public Object part1(List<D1> input) {

        int[] a = input.stream().mapToInt(D1::a).sorted().toArray();
        int[] b = input.stream().mapToInt(D1::b).sorted().toArray();

        return IntStream.range(0, a.length)
                .map(i -> Math.abs(a[i] - b[i]))
                .sum();
    }

    @Override
    public Object part2(List<D1> input) {
        var counts = input.stream()
                .map(D1::b)
                .collect(groupingBy(i -> i, counting()));
        return input.stream()
                .mapToInt(D1::a)
                .mapToLong(i -> i * counts.getOrDefault(i, 0L))
                .sum();
    }

}
