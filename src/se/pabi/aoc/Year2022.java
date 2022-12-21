package se.pabi.aoc;

import se.pabi.aoc.base.Aoc;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("unused")
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
        var stacks = Stream.generate(() -> new Stack<String>()).limit(n).toList();

        for (int k = 0; k < n; k++) {
            var index = k * 4 + 1;
            for (int i = startHeight - 1; i >= 0; i--) {
                char c = start.get(i).charAt(index);
                if (c != ' ') {
                    stacks.get(k).push(String.valueOf(c));
                }
            }
        }

        input.get(1).input.lines().forEach(move -> {
            String[] parts = move.split(" ");
            int count = Integer.parseInt(parts[1]);
            int from = Integer.parseInt(parts[3]) - 1;
            int to = Integer.parseInt(parts[5]) - 1;

            Stream.generate(() -> stacks.get(from).pop())
                    .limit(count)
                    .forEach(c -> stacks.get(to).push(c));
        });

        return stacks.stream()
                .map(Stack::peek)
                .collect(Collectors.joining());
    }

    public static String day5p2(List<Day5> input) {
        System.out.println(input.get(0).input);
        var start = input.get(0).input.lines().toList();
        int startHeight = start.size() - 1;
        int n = (start.get(startHeight).length() + 1) / 4;
        var stacks = Stream.generate(() -> new Stack<String>()).limit(n).toList();

        for (int k = 0; k < n; k++) {
            var index = k * 4 + 1;
            for (int i = startHeight - 1; i >= 0; i--) {
                char c = start.get(i).charAt(index);
                if (c != ' ') {
                    stacks.get(k).push(String.valueOf(c));
                }
            }
        }

        input.get(1).input.lines().forEach(move -> {
            String[] parts = move.split(" ");
            int count = Integer.parseInt(parts[1]);
            int from = Integer.parseInt(parts[3]) - 1;
            int to = Integer.parseInt(parts[5]) - 1;

            Stream.generate(() -> stacks.get(from).pop())
                    .limit(count)
                    .collect(Collectors.toCollection(ArrayDeque::new))
                    .descendingIterator()
                    .forEachRemaining(c -> stacks.get(to).push(c));
        });

        return stacks.stream()
                .map(Stack::peek)
                .collect(Collectors.joining());
    }

    public static int day6p1(String[] input) {
        String s = input[0];
        int size = 4;
        for (int i = size; i <= s.length(); i++) {
            String sub = s.substring(i - size, i);
            if (sub.chars().distinct().count() == size) {
                return i;
            }
        }
        return 0;
    }

    public static int day6p2(String[] input) {
        String s = input[0];
        int size = 14;
        for (int i = size; i <= s.length(); i++) {
            String sub = s.substring(i - size, i);
            if (sub.chars().distinct().count() == size) {
                return i;
            }
        }
        return 0;
    }

    record Directory(Directory parent, Map<String, Path> files) implements Path {
        Directory(Directory parent) {
            this(parent, new HashMap<>());
        }
        Directory cd(String name) {
            Path to = files.get(name);
            if (to instanceof Directory toDir) {
                return toDir;
            } else {
                throw new IllegalArgumentException("Unable to cd into file: " + name);
            }
        }
        @Override
        public long size() {
            return files.values().stream().mapToLong(Path::size).sum();
        }
    }

    record File(long size) implements Path {
        @Override
        public long size() {
            return size;
        }
    }

    interface Path {
        long size();
    }

    public static long day7p1(String[] input) {
        var root = new Directory(null);
        List<Directory> dirs = new ArrayList<>();
        Directory current = root;
        for (String line : input) {
            String[] parts = line.split(" ");
            if ("$".equals(parts[0])) {
                if (parts[1].equals("cd")) {
                    if ("/".equals(parts[2])) {
                        current = root;
                    } else {
                        current = "..".equals(parts[2]) ? current.parent : current.cd(parts[2]);
                    }
                }
            } else {
                if ("dir".equals(parts[0])) {
                    Directory to = new Directory(current);
                    dirs.add(to);
                    current.files.put(parts[1], to);
                } else {
                    current.files.put(parts[1], new File(Long.parseLong(parts[0])));
                }
            }
        }
        return dirs.stream()
                .filter(d -> d.size() <= 100000)
                .mapToLong(Directory::size)
                .sum();
    }

    public static long day7p2(String[] input) {
        var root = new Directory(null);
        List<Directory> dirs = new ArrayList<>();
        Directory current = root;
        for (String line : input) {
            String[] parts = line.split(" ");
            if ("$".equals(parts[0])) {
                if (parts[1].equals("cd")) {
                    if ("/".equals(parts[2])) {
                        current = root;
                    } else {
                        current = "..".equals(parts[2]) ? current.parent : current.cd(parts[2]);
                    }
                }
            } else {
                if ("dir".equals(parts[0])) {
                    Directory to = new Directory(current);
                    dirs.add(to);
                    current.files.put(parts[1], to);
                } else {
                    current.files.put(parts[1], new File(Long.parseLong(parts[0])));
                }
            }
        }
        long total = 70000000;
        long needed = 30000000;
        long free = total - root.size();
        long release = needed - free;
        return dirs.stream()
                .mapToLong(Directory::size)
                .filter(d -> d >= release)
                .min()
                .orElse(0);
    }

    public static int day8p1(String[] trees) {
        int h = trees.length;
        int w = trees[0].length();
        Set<Point> visible = new HashSet<>();
        Stream.of(
                IntStream.range(0, h).mapToObj(y -> IntStream.range(0, w).mapToObj(x -> new Point(x, y)).toList()),
                IntStream.range(0, w).mapToObj(x -> IntStream.range(0, h).mapToObj(y -> new Point(x, y)).toList()),
                IntStream.range(0, h).mapToObj(y -> IntStream.range(0, w).mapToObj(x -> new Point(w - x - 1, y)).toList()),
                IntStream.range(0, w).mapToObj(x -> IntStream.range(0, h).mapToObj(y -> new Point(x, h - y - 1)).toList())
        ).flatMap(s -> s).forEach(line -> {
            char high = '0' - 1;
            for (Point p : line) {
                char c = trees[p.y].charAt(p.x);
                if (c > high) {
                    visible.add(p);
                    if (c == '9') {
                        break;
                    }
                    high = c;
                }
            }
        });
        return visible.size();
    }

    public static long day8p2(String[] trees) {
        int h = trees.length;
        int w = trees[0].length();
        long high = 0;
        for (int y = 1; y < h-1; y++) {
            for (int x = 1; x < w-1; x++) {
                var me = trees[y].charAt(x);
                long value = Stream.of(
                        Stream.iterate(new Point(x-1, y), p -> p.x >= 0, p -> {
                            if (trees[p.y].charAt(p.x) >= me) {
                                p.x = 0;
                            }
                            p.x--;
                            return p;
                        }),
                        Stream.iterate(new Point(x+1, y), p -> p.x < w, p -> {
                            if (trees[p.y].charAt(p.x) >= me) {
                                p.x = w;
                            }
                            p.x++;
                            return p;
                        }),
                        Stream.iterate(new Point(x, y-1), p -> p.y >= 0, p -> {
                            if (trees[p.y].charAt(p.x) >= me) {
                                p.y = 0;
                            }
                            p.y--;
                            return p;
                        }),
                        Stream.iterate(new Point(x, y+1), p -> p.y < h, p -> {
                            if (trees[p.y].charAt(p.x) >= me) {
                                p.y = h;
                            }
                            p.y++;
                            return p;
                        })
                ).mapToLong(Stream::count).reduce(1, (a, b) -> a * b);
                high = Math.max(high, value);
            }
        }
        return high;
    }


    public record Day9(Dir dir, int steps) {
        public enum Dir {
            LEFT, RIGHT, UP, DOWN;

            public static Dir from(String s) {
                return switch (s.toUpperCase()) {
                    case "L" -> LEFT;
                    case "R" -> RIGHT;
                    case "U" -> UP;
                    case "D" -> DOWN;
                    default -> throw new IllegalArgumentException("Invalid direction: " + s);
                };
            }
        }

        public Day9(String dir, int steps) {
            this(Dir.from(dir), steps);
        }
    }

    public static int day9p1(List<Day9> input) {
        return day9(input, 2);
    }


    public static int day9p2(List<Day9> input) {
        return day9(input, 10);
    }

    private static int day9(List<Day9> input, int len) {
        var visited = new HashSet<Point>();
        visited.add(new Point(0, 0));

        LinkedList<Point> tail = Stream.generate(() -> new Point(0, 0))
                .limit(len -1)
                .collect(Collectors.toCollection(LinkedList::new));
        Point head = new Point(0, 0);


        input.forEach(move -> {
            for (int i = 0; i < move.steps(); i++) {
                var h = head;
                switch (move.dir) {
                    case LEFT -> h.x--;
                    case RIGHT -> h.x++;
                    case UP -> h.y--;
                    case DOWN -> h.y++;
                }

                for (Point t : tail) {
                    var hor = h.x - t.x;
                    var ver = h.y - t.y;
                    if (Math.abs(hor) == 2 || Math.abs(ver) == 2) {
                        t.translate(Integer.signum(hor), Integer.signum(ver));
                    }
                    h = t;
                }

                visited.add(new Point(h));
            }
        });

        return visited.size();
    }

    public record Day10(Cmd cmd, int value) {
        static String delim = "¤¤¤¤";
        public Day10(String line) {
            this(Cmd.valueOf(line.split(" ")[0]), line.split(" ").length == 1 ? 0 : Integer.parseInt(line.split(" ")[1]));
        }
        enum Cmd {addx, noop}
    }

    public static int day10p1(List<Day10> inputs) {
        int x = 1;
        int sum = 0;
        int cycle = 0;
        int next = 20;
        for (Day10 input : inputs) {
            switch (input.cmd) {
                case noop -> cycle++;
                case addx -> cycle += 2;
            }

            if (cycle >= next) {
                sum += x * next;
                next += 40;
            }

            if (input.cmd == Day10.Cmd.addx) {
                x += input.value;
            }
        }
        return sum;
    }


    public static int day10p2(List<Day10> inputs) {
        int x = 1;
        int cycle = 0;
        String[] rows = new String[] {"", "", "", "", "", ""};
        for (Day10 input : inputs) {

            rows[cycle / 40] += (cycle%40) >= x-1 && (cycle%40) <= x+1 ? "#" : ".";
            cycle++;

            if (input.cmd == Day10.Cmd.addx) {
                rows[cycle / 40] += (cycle%40) >= x-1 && (cycle%40) <= x+1 ? "#" : ".";
                cycle++;

                x += input.value;
            }
        }
        Arrays.stream(rows).forEach(System.out::println);
        return 0;
    }


    public static void main(String[] args) {
        new Year2022().invoke(10, 2);
    }
}
