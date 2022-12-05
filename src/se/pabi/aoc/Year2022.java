package se.pabi.aoc;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Year2022 extends Aoc {

    public Year2022() {
        super(2022);
    }

    public record Day1(String elf) {
        static String splitter = "\r\n\r\n";
    }

    public static int day1p1(List<Day1> elfs) {
        return elfs.stream()
                .mapToInt(e -> e.elf.lines()
                        .mapToInt(Integer::parseInt)
                        .sum()
                ).max().orElse(0);

    }

    public static int day1p2(List<Day1> elfs) {
        return elfs.stream()
                .map(e -> e.elf.lines()
                        .mapToInt(Integer::parseInt)
                        .sum()
                ).sorted(Comparator.reverseOrder())
                .mapToInt(i -> i)
                .limit(3)
                .sum();

    }

    public record Day2(Hand opp, String outcome) {
        enum Hand {
            ROCK, PAPER, SCISSORS
        }

        public Day2(String opp, String other) {
            this(switch (opp) {
                case "A" -> Hand.ROCK;
                case "B" -> Hand.PAPER;
                default -> Hand.SCISSORS;
            }, other);
        }
    }

    public static int day2p1(List<Day2> rows) {
        int s = 0;
        for (Day2 row : rows) {
            s += switch (row.outcome) {
                case "X" -> switch (row.opp) {
                    case ROCK -> 4;
                    case PAPER -> 1;
                    case SCISSORS -> 7;
                };
                case "Y" -> switch (row.opp) {
                    case ROCK -> 8;
                    case PAPER -> 5;
                    case SCISSORS -> 2;
                };
                case "Z" -> switch (row.opp) {
                    case ROCK -> 3;
                    case PAPER -> 9;
                    case SCISSORS -> 6;
                };
                default -> throw new IllegalStateException("IMPOSSIBLE");
            };
        }
        return s;
    }

    public static int day2p2(List<Day2> hands) {
        int s = 0;
        for (Day2 hand : hands) {
            s += switch (hand.outcome) {
                case "X" -> switch (hand.opp) {
                    case ROCK -> 3;
                    case PAPER -> 1;
                    case SCISSORS -> 2;
                };
                case "Y" -> switch (hand.opp) {
                    case ROCK -> 3 + 1;
                    case PAPER -> 3 + 2;
                    case SCISSORS -> 3 + 3;
                };
                case "Z" -> switch (hand.opp) {
                    case ROCK -> 6 + 2;
                    case PAPER -> 6 + 3;
                    case SCISSORS -> 6 + 1;
                };
                default -> throw new IllegalStateException("IMPOSSIBLE");
            };
        }
        return s;
    }

    public static int day3p1(String[] rows) {
        int s = 0;
        for (String row : rows) {
            List<Integer> items = row.chars().mapToObj(c -> {
                if (c >= 'a' && c <= 'z') {
                    return c - 'a' + 1;
                }
                return c - 'A' + 27;
            }).toList();

            Set<Integer> a = items.stream().limit(row.length() / 2).collect(Collectors.toSet());
            Set<Integer> b = items.stream().skip(row.length() / 2).collect(Collectors.toSet());

            a.retainAll(b);
            s += a.stream().findFirst().orElse(-1000000);
        }
        return s;
    }

    public static int day3p2(String[] rows) {
        int s = 0;
        for (int i = 0; i < rows.length; i += 3) {
            IntFunction<Integer> char2Num = c -> {
                if (c >= 'a' && c <= 'z') {
                    return c - 'a' + 1;
                }
                return c - 'A' + 27;
            };

            Set<Integer> g = rows[i].chars().mapToObj(char2Num).collect(Collectors.toSet());
            g.retainAll(rows[i + 1].chars().mapToObj(char2Num).collect(Collectors.toSet()));
            g.retainAll(rows[i + 2].chars().mapToObj(char2Num).collect(Collectors.toSet()));

            s += g.stream().findFirst().orElse(-10000000);
        }
        return s;
    }

    public record Day4(Range left, Range right) {
        static String delim = ",";

        public Day4(String left, String right) {
            this(Range.parse(left), Range.parse(right));
        }

        private record Range(int from, int to) {
            static Range parse(String string) {
                var parts = string.split("-");
                return new Range(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            }

            boolean contains(int point) {
                return from <= point && point <= to;
            }
        }
    }

    public static long day4p1(List<Day4> pairs) {
        return pairs.stream().filter(pair ->
                (pair.left.from >= pair.right.from && pair.left.to <= pair.right.to) ||
                        (pair.right.from >= pair.left.from && pair.right.to <= pair.left.to)
        ).count();
    }

    public static long day4p2(List<Day4> pairs) {
        return pairs.stream().filter(pair ->
                pair.left.contains(pair.right.from) || pair.left.contains(pair.right.to) ||
                        pair.right.contains(pair.left.from) || pair.right.contains(pair.left.to)
        ).count();
    }

    public record Day5(String input) {
        static String splitter = "\r\n\r\n";
        static String delim = "¤¤¤¤";
    }

    public static String day5p1(List<Day5> input) {
        System.out.println(input.get(0).input);
        List<String> start = input.get(0).input.lines().toList();
        int startHeight = start.size() - 1;
        int n = (start.get(startHeight).length() + 1) / 4;
        Stack<String>[] stacks = new Stack[n];

        for (int k = 0; k < n; k++) {
            stacks[k] = new Stack<>();
            var index = k * 4 + 1;
            for (int i = startHeight - 1; i >= 0; i--) {
                char c = start.get(i).charAt(index);
                if (c != ' ') {
                    stacks[k].push(String.valueOf(c));
                }
            }
        }

        input.get(1).input.lines().forEach(move -> {
            String[] parts = move.split(" ");
            int count = Integer.parseInt(parts[1]);
            int from = Integer.parseInt(parts[3]) - 1;
            int to = Integer.parseInt(parts[5]) - 1;

            Stream.generate(() -> stacks[from].pop())
                    .limit(count)
                    .forEach(c -> stacks[to].push(c));
        });

        return Arrays.stream(stacks)
                .map(Stack::peek)
                .collect(Collectors.joining());
    }

    public static String day5p2(List<Day5> input) {
        System.out.println(input.get(0).input);
        List<String> start = input.get(0).input.lines().toList();
        int startHeight = start.size() - 1;
        int n = (start.get(startHeight).length() + 1) / 4;
        Stack<String>[] stacks = new Stack[n];

        for (int k = 0; k < n; k++) {
            stacks[k] = new Stack<>();
            var index = k * 4 + 1;
            for (int i = startHeight - 1; i >= 0; i--) {
                char c = start.get(i).charAt(index);
                if (c != ' ') {
                    stacks[k].push(String.valueOf(c));
                }
            }
        }

        input.get(1).input.lines().forEach(move -> {
            String[] parts = move.split(" ");
            int count = Integer.parseInt(parts[1]);
            int from = Integer.parseInt(parts[3]) - 1;
            int to = Integer.parseInt(parts[5]) - 1;

            Stream.generate(() -> stacks[from].pop())
                    .limit(count)
                    .collect(Collectors.toCollection(ArrayDeque::new))
                    .descendingIterator()
                    .forEachRemaining(c -> stacks[to].push(c));
        });

        return Arrays.stream(stacks)
                .map(Stack::peek)
                .collect(Collectors.joining());
    }

    public static void main(String[] args) {
        new Year2022().invoke(5, 2);
    }
}
