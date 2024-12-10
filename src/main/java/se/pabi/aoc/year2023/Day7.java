package se.pabi.aoc.year2023;

import se.phet.aoc.AdventOfCode;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day7 extends AdventOfCode<Stream<Day7.Play>> {

    public record Play(String hand, int bid) {
    }

    @Override
    public Stream<Play> input(String input) {
        return input.lines()
                .map(line -> line.split(" "))
                .map(split -> new Play(split[0], Integer.parseInt(split[1])));
    }

    @Override
    public Object part1(Stream<Play> plays) {
        List<Play> list = plays
                .sorted(Comparator.comparingInt((Play p) -> rank(p.hand))
                        .thenComparing(p -> replace(p.hand, false))
                        .reversed())
                .toList();
        return IntStream.range(0, list.size()).map(i -> list.get(i).bid * (list.size() - i)).sum();
    }

    @Override
    public Object part2(Stream<Play> plays) {
        List<Play> list = plays
                .sorted(Comparator.comparingInt((Play p) -> jokerRank(p.hand))
                        .thenComparing(p -> replace(p.hand, true))
                        .reversed())
                .toList();
        return IntStream.range(0, list.size()).map(i -> list.get(i).bid * (list.size() - i)).sum();
    }

    private static String replace(String cards, boolean joker) {
        var builder = new StringBuilder();
        for (char c : cards.toCharArray()) {
            switch (c) {
                case 'T' -> builder.append('B');
                case 'K' -> builder.append('V');
                case 'A' -> builder.append('Y');
                case 'J' -> builder.append(joker ? '1' : c);
                default -> builder.append(c);
            }
        }
        return builder.toString();
    }

    private static int rank(String cards) {
        var sets = new int[5];

        for (char c : cards.toCharArray()) {
            sets[(int) cards.chars().filter(ch -> ch == c).count() - 1]++;
        }

        if (sets[4] == 5) {
            return 6;
        } else if (sets[3] == 4) {
            return 5;
        } else if (sets[2] == 3) {
            if (sets[1] == 2) {
                return 4;
            }
            return 3;
        } else if (sets[1] == 4) {
            return 2;
        } else if (sets[1] == 2) {
            return 1;
        }
        return 0;
    }

    private static int jokerRank(String cards) {
        int jokers = (int) cards.chars().filter(ch -> ch == 'J').count();
        var sets = new int[5];

        if (jokers == 0 || jokers == 5) {
            return rank(cards);
        }

        for (char c : cards.toCharArray()) {
            if (c != 'J') {
                sets[(int) cards.chars().filter(ch -> ch == c).count() - 1]++;
            }
        }

        return switch (jokers) {
            case 1 -> {
                if (sets[3] == 4) {
                    yield 6;
                } else if (sets[2] == 3) {
                    yield 5;
                } else if (sets[1] == 4) {
                    yield 4;
                } else if (sets[1] == 2) {
                    yield 3;
                }
                yield 1;
            }
            case 2 -> {
                if (sets[2] == 3) {
                    yield 6;
                }
                if (sets[1] == 2) {
                    yield 5;
                }
                yield 3;
            }
            case 3 -> sets[1] == 2 ? 6 : 5;
            case 4 -> 6;
            default -> throw new IllegalStateException("Should not happen");
        };

    }
}
