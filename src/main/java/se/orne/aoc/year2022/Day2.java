package se.orne.aoc.year2022;

import se.orne.aoc.AdventOfCode;

import java.util.stream.Stream;

public class Day2 extends AdventOfCode<Stream<Day2.Input>> {

    public record Input(Hand opp, char outcome) {
        enum Hand {
            ROCK, PAPER, SCISSORS
        }

        public Input(char opp, char other) {
            this(switch (opp) {
                case 'A' -> Hand.ROCK;
                case 'B' -> Hand.PAPER;
                default -> Hand.SCISSORS;
            }, other);
        }
    }

    @Override
    public Stream<Input> input(String input) {
        return input.lines()
                .map(line -> new Input(line.charAt(0), line.charAt(2)));
    }

    @Override
    public Object part1(Stream<Input> input) {
        return input.mapToInt(i -> switch (i.outcome) {
            case 'X' -> switch (i.opp) {
                case ROCK -> 4;
                case PAPER -> 1;
                case SCISSORS -> 7;
            };
            case 'Y' -> switch (i.opp) {
                case ROCK -> 8;
                case PAPER -> 5;
                case SCISSORS -> 2;
            };
            case 'Z' -> switch (i.opp) {
                case ROCK -> 3;
                case PAPER -> 9;
                case SCISSORS -> 6;
            };
            default -> throw new IllegalStateException("IMPOSSIBLE");
        }).sum();
    }

    @Override
    public Object part2(Stream<Input> input) {
        return input.mapToInt(i -> switch (i.outcome) {
            case 'X' -> switch (i.opp) {
                case ROCK -> 3;
                case PAPER -> 1;
                case SCISSORS -> 2;
            };
            case 'Y' -> switch (i.opp) {
                case ROCK -> 3 + 1;
                case PAPER -> 3 + 2;
                case SCISSORS -> 3 + 3;
            };
            case 'Z' -> switch (i.opp) {
                case ROCK -> 6 + 2;
                case PAPER -> 6 + 3;
                case SCISSORS -> 6 + 1;
            };
            default -> throw new IllegalStateException("IMPOSSIBLE");
        }).sum();
    }

}
