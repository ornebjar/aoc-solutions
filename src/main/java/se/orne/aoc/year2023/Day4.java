package se.orne.aoc.year2023;

import se.orne.aoc.AdventOfCode;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day4 extends AdventOfCode<Stream<Day4.Card>> {

    public record Card(Set<Integer> winning, Set<Integer> numbers) {
        public Card(String line) {
            this(line.split(" \\| +"));
        }

        public Card(String[] split) {
            this(Arrays.stream(split[0].split(": +")[1].split(" +"))
                            .map(Integer::parseInt)
                            .collect(Collectors.toSet()),
                    Arrays.stream(split[1].split(" +"))
                            .map(Integer::parseInt)
                            .collect(Collectors.toSet()));
        }
    }

    @Override
    public Stream<Card> input(String input) {
        return input.lines().map(Card::new);
    }

    @Override
    public Object part1(Stream<Card> cards) {
        return cards.mapToInt(card ->
                (int) Math.pow(2, (int) card.winning.stream()
                        .filter(card.numbers::contains)
                        .count()) / 2).sum();
    }

    @Override
    public Object part2(Stream<Card> input) {
        var cards = input.toList();
        var copies = new int[cards.size()];

        for (int i = 0; i < cards.size(); i++) {
            copies[i]++;
            var card = cards.get(i);
            var winCount = card.winning.stream()
                    .filter(card.numbers::contains)
                    .count();
            for (int k = 1; k <= winCount; k++) {
                copies[i+k] += copies[i];
            }
        }

        return Arrays.stream(copies).asLongStream().sum();
    }
}
