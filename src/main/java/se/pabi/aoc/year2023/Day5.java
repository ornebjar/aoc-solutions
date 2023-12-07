package se.pabi.aoc.year2023;

import se.pabi.aoc.base.AdventOfCode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day5 extends AdventOfCode<Day5.Data>{

    public record Data(List<Long> seeds, List<Map> maps) {
        public Data(String input) {
            this(
                    Arrays.stream(input.lines().findFirst().orElseThrow().substring(7).split(" "))
                            .map(Long::parseLong)
                            .collect(Collectors.toList()),
                    Arrays.stream(input.split("\n.*map:\n")).skip(1).map(Map::new).toList()
            );
        }
    }

    public record Map(List<MapRange> mapRanges) {
        public Map(String input) {
            this(input.lines().map(MapRange::new).toList());
        }

        public Optional<Long> get(long value) {
            return mapRanges.stream()
                    .filter(mapRange -> mapRange.source <= value && value < mapRange.end())
                    .findFirst()
                    .map(mapRange -> mapRange.get(value));
        }
    }

    record MapRange(long dest, long source, long length) {
        MapRange(String line) {
            this(line.split(" "));
        }

        MapRange(String[] split) {
            this(Long.parseLong(split[0]), Long.parseLong(split[1]), Long.parseLong(split[2]));
        }

        MapRange {
            if (length < 0 || source < 0 || dest < 0) {
                throw new IllegalArgumentException("All values must be positive.");
            }
        }

        long end() {
            return source + length;
        }

        long get(long value) {
            return value - source + dest;
        }
    }


    @Override
    public Data input(String input) {
//        input = """
//                seeds: 79 14 55 13
//
//                seed-to-soil map:
//                50 98 2
//                52 50 48
//
//                soil-to-fertilizer map:
//                0 15 37
//                37 52 2
//                39 0 15
//
//                fertilizer-to-water map:
//                49 53 8
//                0 11 42
//                42 0 7
//                57 7 4
//
//                water-to-light map:
//                88 18 7
//                18 25 70
//
//                light-to-temperature map:
//                45 77 23
//                81 45 19
//                68 64 13
//
//                temperature-to-humidity map:
//                0 69 1
//                1 0 69
//
//                humidity-to-location map:
//                60 56 37
//                56 93 4
//                """;
        return new Data(input);
    }

    @Override
    public Object part1(Data data) {
        return data.seeds.stream().mapToLong(value -> {
            for (Map map : data.maps) {
                value = map.get(value).orElse(value);
            }
            return value;
        }).min().orElseThrow();
    }

    @Override
    public Object part2(Data data) {

        long min = Long.MAX_VALUE;
        for (int i = 0; i < data.seeds.size(); i+=2) {
            long start = data.seeds.get(i);
            long end = start + data.seeds.get(i+1);

            Range range = new Range(start, end);
            long calc = calc(range, data.maps, 0);
            if (calc < min) {
                min = calc;
            }
        }

        return min;
    }

    private long calc(Range input, List<Map> maps, int level) {
        if (level == maps.size()) {
            return input.start;
        }

        var queue = new LinkedList<Range>();
        queue.add(input);
        var min = Long.MAX_VALUE;

        for (MapRange mapRange : maps.get(level).mapRanges) {
            var nextQueue = new LinkedList<Range>();
            for (Range range : queue) {
                Range overlap = range.overlap(mapRange);
                if (overlap != null) {
                    long calc = calc(overlap, maps, level + 1);
                    if (calc < min) {
                        min = calc;
                    }
                }
                Range before = range.before(mapRange);
                if (before != null) {
                    nextQueue.add(before);
                }
                Range after = range.after(mapRange);
                if (after != null) {
                    nextQueue.add(after);
                }
            }
            queue = nextQueue;
        }

        return queue.stream()
                .mapToLong(range -> calc(range, maps, level + 1))
                .reduce(min, Math::min);
    }

    record Range(long start, long end) {

        Range before(MapRange mapRange) {
            if (mapRange.source <= start) {
                return null;
            } else if (mapRange.source >= end) {
                return this;
            }
            return new Range(start, mapRange.source);
        }

        Range after(MapRange mapRange) {
            if (mapRange.end() >= end) {
                return null;
            } else if (mapRange.end() <= start) {
                return this;
            }
            return new Range(mapRange.end(), end);
        }

        Range overlap(MapRange mapRange) {
            if (mapRange.source >= start && mapRange.end() <= end) {
                return new Range(mapRange.get(mapRange.source), mapRange.get(mapRange.end()));
            } else if (mapRange.source < start && mapRange.end() > end) {
                return new Range(mapRange.get(start), mapRange.get(end));
            } else if (mapRange.source < start && mapRange.end() > start) {
                return new Range(mapRange.get(start), mapRange.get(mapRange.end()));
            } else if (mapRange.source < end && mapRange.end() > end) {
                return new Range(mapRange.get(mapRange.source), mapRange.get(end));
            }
            return null;
        }
    }
}
