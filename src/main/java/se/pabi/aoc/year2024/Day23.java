package se.pabi.aoc.year2024;

import se.phet.aoc.AdventOfCode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day23 extends AdventOfCode<Stream<String>> {

    private Map<String, Computer> computers;

    private record Computer(String name, Set<String> connections) {

        private Computer(String name) {
            this(name, new HashSet<>());
        }

        public void connect(String other) {
            connections.add(other);
        }
    }

    @Override
    public Stream<String> input(String input) {
        computers = new HashMap<>();
        input.lines().forEach(line -> {
            var connection = line.split("-");
            var computer1 = computers.computeIfAbsent(connection[0], Computer::new);
            var computer2 = computers.computeIfAbsent(connection[1], Computer::new);
            computer1.connect(computer2.name);
            computer2.connect(computer1.name);
        });
        return computers.keySet().stream();
    }

    @Override
    public Object part1(Stream<String> input) {
        Set<Set<String>> tris = new HashSet<>();
        input.forEach(computer1 -> {
            var connections = computers.get(computer1).connections;
            connections.forEach(computer2 -> {
                var connections2 = computers.get(computer2).connections;
                connections2.forEach(computer3 -> {
                    if (connections.contains(computer3)) {
                        tris.add(Set.of(computer1, computer2, computer3));
                    }
                });
            });
        });
        return tris.stream()
                .filter(tri -> tri.stream().anyMatch(computer -> computer.startsWith("t")))
                .count();
    }

    @Override
    public Object part2(Stream<String> input) {
        return input.map(computer -> party(computer, new LinkedHashSet<>(computers.get(computer).connections)))
                .max(Comparator.comparingInt(Set::size))
                .orElseThrow().stream()
                .sorted()
                .collect(Collectors.joining(","));
    }

    private Set<String> party(String computer, LinkedHashSet<String> test) {
        Set<String> best = new HashSet<>();
        while (!test.isEmpty()) {
            var next = test.removeFirst();
            var common = new LinkedHashSet<>(computers.get(next).connections);
            common.retainAll(test);
            var result = party(next, common);
            result.add(next);
            result.add(computer);
            if (result.size() > best.size()) {
                best = result;
            }
            test.removeAll(result);
        }
        return best;
    }
}
