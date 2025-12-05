package se.orne.aoc.year2022;

import se.orne.aoc.AdventOfCode;

import java.util.stream.Stream;

public class Day4 extends AdventOfCode<Stream<Day4.Input>> {

    public record Input(Range left, Range right) {
        public Input(String left, String right) {
            this(Range.parse(left), Range.parse(right));
        }
        private record Range(int from, int to) {
            static Range parse(String string) {
                var parts = string.split("-");
                return new Range(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            }

            boolean contains(int point) {
                return from <= point && point <= to;
            }
        }
    }

    @Override
    public Stream<Input> input(String input) {
        return input.lines().map(line -> {
            var parts = line.split(",");
            return new Input(parts[0], parts[1]);
        });
    }

    @Override
    public Object part1(Stream<Input> pairs) {
        return pairs.filter(pair ->
                (pair.left.from >= pair.right.from && pair.left.to <= pair.right.to) ||
                        (pair.right.from >= pair.left.from && pair.right.to <= pair.left.to)
        ).count();
    }

    @Override
    public Object part2(Stream<Input> pairs) {
        return pairs.filter(pair ->
                pair.left.contains(pair.right.from) || pair.left.contains(pair.right.to) ||
                        pair.right.contains(pair.left.from) || pair.right.contains(pair.left.to)
        ).count();
    }

}
