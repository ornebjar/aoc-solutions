package se.pabi.aoc.year2024;

import se.phet.aoc.AdventOfCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day25 extends AdventOfCode<Stream<int[]>> {

    private List<int[]> locks;
    private List<int[]> keys;

    @Override
    public Stream<int[]> input(String input) {
//        input = """
//                #####
//                .####
//                .####
//                .####
//                .#.#.
//                .#...
//                .....
//
//                #####
//                ##.##
//                .#.##
//                ...##
//                ...#.
//                ...#.
//                .....
//
//                .....
//                #....
//                #....
//                #...#
//                #.#.#
//                #.###
//                #####
//
//                .....
//                .....
//                #.#..
//                ###..
//                ###.#
//                ###.#
//                #####
//
//                .....
//                .....
//                .....
//                #....
//                #.#..
//                #.#.#
//                #####
//                """;
        locks = new ArrayList<>();
        keys = new ArrayList<>();
        for (String row : input.split("\n\n")) {
            char type = row.charAt(0);
            String[] rows = row.split("\n");
            int[] values = new int[5];
            for (int i = 0; i < 5; i++) {
                for (int y = 1; y < 7; y++) {
                    if (rows[y].charAt(i) != type) {
                        if (type == '#') {
                            values[i] = y - 1;
                        } else {
                            values[i] = 6 - y;
                        }
                        break;
                    }
                }
            }
            if (type == '#') {
                locks.add(values);
            } else {
                keys.add(values);
            }
        }
        return locks.stream();
    }

    @Override
    public Object part1(Stream<int[]> input) {
        return input.mapToLong(lock ->
                keys.stream().filter(key -> {
                    for (int i = 0; i < 5; i++) {
                        if (lock[i] + key[i] > 5) {
                            return false;
                        }
                    }
                    return true;
                }).count()
        ).sum();
    }

    @Override
    public Object part2(Stream<int[]> input) {
        return super.part2(input);
    }

}
