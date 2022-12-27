package se.pabi.aoc.year2022;

import se.pabi.aoc.base.AdventOfCode;
import se.pabi.aoc.util.Util;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day19 extends AdventOfCode<Stream<Day19.Blueprint>> {

    enum Resource {
        ORE(0), CLAY(1), OBSIDIAN(2), GEODE(3);

        final int n;

        Resource(int n) {
            this.n = n;
        }
    }

    record Blueprint(
            int id,
            int[][] cost
    ) {
        Blueprint(int id, int ore, int clay, int obsidianOre, int obsidianClay, int geodeOre, int geodeObsidian) {
            this(id, new int[][]{
                    {ore},
                    {clay},
                    {obsidianOre, obsidianClay},
                    {geodeOre, 0, geodeObsidian}
            });
        }

        Blueprint(int[] values) {
            this(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
        }
    }

    @Override
    public Stream<Blueprint> input(String input) {
//        input = "Blueprint 1: " +
//                "Each ore robot costs 4 ore. " +
//                "Each clay robot costs 2 ore. " +
//                "Each obsidian robot costs 3 ore and 14 clay. " +
//                "Each geode robot costs 2 ore and 7 obsidian.\n" +
//                "Blueprint 2: " +
//                "Each ore robot costs 2 ore. " +
//                "Each clay robot costs 3 ore. " +
//                "Each obsidian robot costs 3 ore and 8 clay. " +
//                "Each geode robot costs 3 ore and 12 obsidian.";
        Pattern pattern = Pattern.compile(
                "Blueprint (.*): Each ore robot costs (.*) ore. " +
                        "Each clay robot costs (.*) ore. " +
                        "Each obsidian robot costs (.*) ore and (.*) clay. " +
                        "Each geode robot costs (.*) ore and (.*) obsidian.");
        return input.lines()
                .map(line -> Util.groups(line, pattern))
                .map(groups -> groups.mapToInt(Integer::parseInt).toArray())
                .map(Blueprint::new);
    }

    @Override
    public Object part1(Stream<Blueprint> input) {
        return input
                .mapToInt(blueprint -> calc(blueprint, 24) * blueprint.id)
                .sum();
    }

    @Override
    public Object part2(Stream<Blueprint> input) {
        return input.limit(3)
                .mapToInt(blueprint -> calc(blueprint, 32))
                .reduce(1, (a, b) -> a * b);
    }

    private int calc(Blueprint blueprint, int minutes) {

        int[] robots = new int[]{1, 0, 0, 0};
        int[] resources = new int[]{0, 0, 0, 0};

        int score = calc(blueprint, minutes, resources, robots, 0);

        System.out.printf("#%s: %s%n", blueprint.id, score);

        return score;
    }

    private int calc(Blueprint blueprint, int minute, int[] resources, int[] robots, int top) {
        if (minute <= 0) {
            return resources[Resource.GEODE.n] + minute * robots[Resource.GEODE.n];
        }

        int high = resources[Resource.GEODE.n] + robots[Resource.GEODE.n] * minute + (minute * (minute - 1) / 2);

        if (high <= top) {
            return top;
        }

        if ((minute - 1) * blueprint.cost[Resource.GEODE.n][Resource.ORE.n] <= resources[Resource.ORE.n] &&
                (minute - 1) * blueprint.cost[Resource.GEODE.n][Resource.OBSIDIAN.n] <= resources[Resource.OBSIDIAN.n]) {
            return high;
        }

        for (Resource resource : Resource.values()) {
            int i = tryBuild(blueprint, resource, minute, resources, robots, top);
            if (i > top) {
                if (i == high) {
                    return high;
                }
                top = i;
            }
        }
        return top;
    }

    private int tryBuild(Blueprint blueprint, Resource build, int minute, int[] resources, int[] robots, int top) {
        int[] cost = blueprint.cost[build.n];

        for (int i = 0; i < cost.length; i++) {
            if (cost[i] > 0 && robots[i] == 0) {
                return 0;
            }
        }

        int wait = 0;
        for (int i = 0; i < cost.length; i++) {
            int need = cost[i];
            if (need > 0) {
                int have = resources[i];
                int botCount = robots[i];
                int turns = Math.ceilDiv(need - have, botCount);
                if (turns > wait) {
                    wait = turns;
                }
            }
        }
        wait++;

        for (int i = 0; i < cost.length; i++) {
            resources[i] -= cost[i];
        }
        for (int i = 0; i < robots.length; i++) {
            resources[i] += robots[i] * wait;
        }
        robots[build.n]++;

        int score = calc(blueprint, minute - wait, resources, robots, top);

        robots[build.n]--;
        for (int i = 0; i < robots.length; i++) {
            resources[i] -= robots[i] * wait;
        }
        for (int i = 0; i < cost.length; i++) {
            resources[i] += cost[i];
        }

        return score;
    }

    public static void main(String[] args) {
        new Day19();
    }
}
