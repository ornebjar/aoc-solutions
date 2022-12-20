package se.pabi.aoc.year2022;

import se.pabi.aoc.AdventOfCode;
import se.pabi.aoc.Helper;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16 extends AdventOfCode<Day16.Valve[]> {

    class Valve {
        final String name;
        int rate;

        final LinkedHashMap<Valve, Integer> neighbours = new LinkedHashMap<>();

        Valve(String name, int rate) {
            this.name = name;
            this.rate = rate;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    public Valve[] input(String input) {
//        input = """
//                Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
//                Valve BB has flow rate=13; tunnels lead to valves CC, AA
//                Valve CC has flow rate=2; tunnels lead to valves DD, BB
//                Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
//                Valve EE has flow rate=3; tunnels lead to valves FF, DD
//                Valve FF has flow rate=0; tunnels lead to valves EE, GG
//                Valve GG has flow rate=0; tunnels lead to valves FF, HH
//                Valve HH has flow rate=22; tunnel leads to valve GG
//                Valve II has flow rate=0; tunnels lead to valves AA, JJ
//                Valve JJ has flow rate=21; tunnel leads to valve II""";
        Pattern pattern = Pattern.compile("^Valve (.*) has flow rate=(.*); tunnels? leads? to valves? (.*)$");
        List<String[]> neighbours = new ArrayList<>();
        Valve[] valves = input.lines().map(line -> {
            String[] groups = Helper.groups(line, pattern).toArray(String[]::new);
            neighbours.add(groups[2].split(", "));
            return new Valve(groups[0], Integer.parseInt(groups[1]));
        }).toArray(Valve[]::new);
        for (int i = 0; i < neighbours.size(); i++) {
            Valve v = valves[i];
            Set<String> nName = Arrays.stream(neighbours.get(i)).collect(Collectors.toSet());
            Arrays.stream(valves)
                    .filter(valve -> nName.contains(valve.name))
                    .forEach(valve -> v.neighbours.put(valve, 1));
        }
        for (Valve valve : valves) {
            LinkedList<Valve> open = new LinkedList();
            open.addAll(valve.neighbours.keySet());
            int dist = 1;
            Valve next;
            while (!open.isEmpty()) {
                LinkedList<Valve> nextOpen = new LinkedList<>();
                int curr = ++dist;
                while ((next = open.poll()) != null) {
                    next.neighbours.entrySet().stream()
                            .filter(e -> e.getValue() == 1 && e.getKey() != valve)
                            .forEach(e -> valve.neighbours.computeIfAbsent(e.getKey(), k -> {
                                nextOpen.add(k);
                                return curr;
                            }));
                }
                open = nextOpen;
            }
        }
        return valves;
    }

    @Override
    public Object part1(Valve[] input) {
        Valve start = Arrays.stream(input).filter(v -> "AA".equals(v.name)).findFirst().orElseThrow();
        LinkedList<Valve> paths = start.neighbours.keySet().stream().filter(v -> v.rate > 0).collect(Collectors.toCollection(LinkedList::new));

        return calc(start, paths, 30);
    }

    private int calc(Valve current, LinkedList<Valve> paths, int time) {
        if (time < 1) {
            return 0;
        }

        return time * current.rate + IntStream.range(0, paths.size()).map(i -> {
            Valve next = paths.pop();

            int dist = current.neighbours.get(next);

            int score = calc(next, paths, time - dist - 1);

            paths.add(next);
            return score;
        }).max().orElse(0);
    }

    @Override
    public Object part2(Valve[] input) {
        Valve start = Arrays.stream(input).filter(v -> "AA".equals(v.name)).findFirst().orElseThrow();
        Valve elephant = start;
        LinkedList<Valve> paths = start.neighbours.keySet().stream().filter(v -> v.rate > 0).collect(Collectors.toCollection(LinkedList::new));

        return calc2(start, elephant, paths, 26, 26);
    }

    private int calc2(Valve current1, Valve current2, LinkedList<Valve> paths, int time1, int time2) {
        if (time1 < 1 && time2 < 1) {
            return 0;
        }

        int moveScore = time1 * current1.rate;

        IntStream intStream = time1 >= time2 ? IntStream.range(0, paths.size()).map(i -> {
            Valve next = paths.pop();
            int dist = current1.neighbours.get(next);
            int score = calc2(next, current2, paths, time1 - dist - 1, time2);
            paths.add(next);
            return score;
        }) : IntStream.range(0, paths.size()).map(i -> {
            Valve next = paths.pop();
            int dist = current2.neighbours.get(next);
            int score = calc2(next, current1, paths, time2 - dist - 1, time1);
            paths.add(next);
            return score;
        });

        return moveScore + intStream.max().orElse(0);
    }

    public static void main(String[] args) {
        new Day16();
    }
}
