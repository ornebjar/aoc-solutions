package se.pabi.aoc.year2022;

import se.phet.aoc.AdventOfCode;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day5 extends AdventOfCode<Day5.Input> {

    public record Input(Stream<String> lines, List<Stack<String>> stacks) {
        public Input(String input) {
            this(input.split("\n\n")[1].lines(), parseStacks(input));
        }
        private static List<Stack<String>> parseStacks(String input) {
            String[] parts = input.split("\n\n");
            System.out.println(parts[0]);
            List<String> start = parts[0].lines().toList();
            int startHeight = start.size() - 1;
            int n = (start.get(startHeight).length() + 1) / 4;
            var stacks = Stream.generate(() -> new Stack<String>()).limit(n).toList();

            for (int k = 0; k < n; k++) {
                var index = k * 4 + 1;
                for (int i = startHeight - 1; i >= 0; i--) {
                    char c = start.get(i).charAt(index);
                    if (c != ' ') {
                        stacks.get(k).push(String.valueOf(c));
                    }
                }
            }
            return stacks;
        }
    }

    @Override
    public Input input(String input) {
        return new Input(input);
    }

    @Override
    public Object part1(Input input) {
        input.lines.forEach(move -> {
            String[] parts = move.split(" ");
            int count = Integer.parseInt(parts[1]);
            int from = Integer.parseInt(parts[3]) - 1;
            int to = Integer.parseInt(parts[5]) - 1;

            Stream.generate(() -> input.stacks.get(from).pop())
                    .limit(count)
                    .forEach(c -> input.stacks.get(to).push(c));
        });

        return input.stacks.stream()
                .map(Stack::peek)
                .collect(Collectors.joining());
    }

    @Override
    public Object part2(Input input) {
        input.lines.forEach(move -> {
            String[] parts = move.split(" ");
            int count = Integer.parseInt(parts[1]);
            int from = Integer.parseInt(parts[3]) - 1;
            int to = Integer.parseInt(parts[5]) - 1;

            Stream.generate(() -> input.stacks.get(from).pop())
                    .limit(count)
                    .collect(Collectors.toCollection(ArrayDeque::new))
                    .descendingIterator()
                    .forEachRemaining(c -> input.stacks.get(to).push(c));
        });

        return input.stacks.stream()
                .map(Stack::peek)
                .collect(Collectors.joining());
    }
}
