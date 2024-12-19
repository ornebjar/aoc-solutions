package se.pabi.aoc.year2024;

import se.phet.aoc.AdventOfCode;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day19 extends AdventOfCode<Stream<String>> {

    enum Color {
        r, g, b, u, w;

        static Color from(char c) {
            return switch (c) {
                case 'r' -> r;
                case 'g' -> g;
                case 'b' -> b;
                case 'u' -> u;
                case 'w' -> w;
                default -> throw new IllegalArgumentException("Unknown color: " + c);
            };
        }
    }

    record Words(Words[] next, boolean word) {

        Words(boolean word) {
            this(new Words[5], word);
        }

        Words() {
            this(new Words[5], false);
        }

        void addWord(String word) {
            addWord(word, 0);
        }

        private void addWord(String word, int i) {
            int index = Color.from(word.charAt(i)).ordinal();
            if (next[index] == null) {
                if (i == word.length() - 1) {
                    next[index] = new Words(true);
                    return;
                } else {
                    next[index] = new Words();
                }
            }
            next[index].addWord(word, i + 1);
        }

        IntStream matches(String word, int i) {
            IntStream.Builder builder = IntStream.builder();
            matches(word, builder, i, 0);
            return builder.build();
        }

        void matches(String word, IntStream.Builder builder, int i, int len) {
            if (i == word.length()) {
                return;
            }
            int index = Color.from(word.charAt(i)).ordinal();
            if (next[index] != null) {
                if (next[index].word) {
                    builder.accept(len + 1);
                }
                next[index].matches(word, builder, i + 1, len + 1);
            }
        }

    }

    private Words words;

    @Override
    public Stream<String> input(String input) {

        words = new Words();
        var lines = input.split("\n");
        Arrays.stream(lines[0].split(", ")).sorted().forEach(words::addWord);
        return Arrays.stream(lines, 2, lines.length);
    }

    @Override
    public Object part1(Stream<String> input) {
        return input
                .filter(line -> check(line, words) > 0)
                .count();
    }

    @Override
    public Object part2(Stream<String> input) {
        return input
                .mapToLong(line -> check(line, words))
                .sum();
    }

    private static long check(String line, Words words) {
        long[] count = new long[line.length() + 1];
        count[0] = 1;
        for (int i = 0; i < line.length(); i++) {
            if (count[i] == 0) {
                continue;
            }
            var matches = words.matches(line, i);
            int finalI = i;
            matches.forEach(len -> count[finalI + len] += count[finalI]);
        }
        return count[line.length()];
    }

}
