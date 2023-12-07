package se.pabi.aoc;

import se.pabi.aoc.base.Aoc;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Year2021 extends Aoc {

    public Year2021() {
        super(2021);
    }

    public record Day2(String dir, int len) {}
    public static int day2p1(List<Day2> rows) {

        int h = 0;
        int d = 0;

        for (Day2 row : rows) {
            if (row.dir.equals("up")) {
                d-=row.len;
            } else if (row.dir.equals("down")) {
                d+=row.len;
            } else { // forward
                h+=row.len;
            }
        }

        return d*h;
    }
    public static int day2p2(List<Day2> rows) {

        int h = 0;
        int d = 0;
        int aim = 0;

        for (Day2 row : rows) {
            if (row.dir.equals("up")) {
                aim -= row.len;
            } else if (row.dir.equals("down")) {
                aim += row.len;
            } else { // forward
                h += row.len;
                d += aim*row.len;
            }
        }

        return d*h;
    }

    public static record Day3(String bin) {}
    public static int day3p1(List<Day3> rows) {

        int len = rows.get(0).bin.length();

        long[] counts = new long[len];

        for (int i = 0; i < len; i++) {
            final int pos = i;
            counts[i] = rows.stream().map(r -> r.bin.charAt(pos))
                    .filter(c -> c == '1')
                    .count();
        }

        String g = "";
        String e = "";

        for (int i = 0; i < len; i++) {
            if (counts[i] >= rows.size() - counts[i]) {
                g += "1";
                e += "0";
            } else {
                g += "0";
                e += "1";
            }
        }


        return Integer.parseInt(g, 2) * Integer.parseInt(e, 2);
    }
    public static int day3p2(List<Day3> rows) {

        int len = rows.get(0).bin.length();

        List<Day3> o = new ArrayList<>(rows);
        List<Day3> c = new ArrayList<>(rows);


        for (int i = 0; i < len; i++) {
            final int pos = i;
            if (o.size() > 1) {
                long count = o.stream().map(r -> r.bin.charAt(pos))
                        .filter(ch -> ch == '1')
                        .count();
                if (count >= o.size() - count) {
                    o = o.stream().filter(r -> r.bin.charAt(pos) == '1').collect(Collectors.toList());
                } else {
                    o = o.stream().filter(r -> r.bin.charAt(pos) == '0').collect(Collectors.toList());
                }
            }

            if (c.size() > 1) {
                long count = c.stream().map(r -> r.bin.charAt(pos))
                        .filter(ch -> ch == '0')
                        .count();
                if (count <= c.size() - count) {
                    c = c.stream().filter(r -> r.bin.charAt(pos) == '0').collect(Collectors.toList());
                } else {
                    c = c.stream().filter(r -> r.bin.charAt(pos) == '1').collect(Collectors.toList());
                }
            }
        }


        return Integer.parseInt(o.get(0).bin, 2) * Integer.parseInt(c.get(0).bin, 2);
    }

    public static int day4p1(String[] input) {
        List<Integer> numbers = Arrays.stream(input[0].split(",")).map(Integer::parseInt).collect(Collectors.toList());
        int boardCount = input.length / 6;
        int[][][] boards = new int[boardCount][5][5];

        for (int i = 0; i < boardCount; i++) {
            for (int r = 0; r < 5; r++) {
                List<Integer> row = Arrays.stream(input[2 + i * 6 + r].trim().split("[ ]+"))
                        .map(Integer::parseInt).collect(Collectors.toList());
                for (int c = 0; c < 5; c++) {
                    boards[i][r][c] = row.get(c);
                }
            }
        }

        for (Integer n : numbers) {
            for (int b = 0; b < boardCount; b++) {
                for (int r = 0; r < 5; r++) {
                    for (int c = 0; c < 5; c++) {
                        if (boards[b][r][c] == n) {
                            boards[b][r][c] = -1;
                            if ((boards[b][r][0] == -1 &&
                                    boards[b][r][1] == -1 &&
                                    boards[b][r][2] == -1 &&
                                    boards[b][r][3] == -1 &&
                                    boards[b][r][4] == -1) ||
                                    (boards[b][0][c] == -1 &&
                                            boards[b][1][c] == -1 &&
                                            boards[b][2][c] == -1 &&
                                            boards[b][3][c] == -1 &&
                                            boards[b][4][c] == -1)) {
                                int sum = 0;
                                for (int x = 0; x < 5; x++) {
                                    for (int y = 0; y < 5; y++) {
                                        if (boards[b][y][x] > -1) {
                                            sum += boards[b][y][x];
                                        }
                                    }
                                }
                                return n * sum;
                            }
                        }
                    }
                }
            }
        }

        return 1;
    }
    public static int day4p2(String[] input) {
        List<Integer> numbers = Arrays.stream(input[0].split(",")).map(Integer::parseInt).collect(Collectors.toList());
        int boardCount = input.length / 6;
        int[][][] boards = new int[boardCount][5][5];

        for (int i = 0; i < boardCount; i++) {
            for (int r = 0; r < 5; r++) {
                List<Integer> row = Arrays.stream(input[2 + i * 6 + r].trim().split("[ ]+"))
                        .map(Integer::parseInt).collect(Collectors.toList());
                for (int c = 0; c < 5; c++) {
                    boards[i][r][c] = row.get(c);
                }
            }
        }

        boolean[] winning = new boolean[boardCount];
        int winners = 0;

        for (Integer n : numbers) {
            for (int b = 0; b < boardCount; b++) {
                if (!winning[b]) {
                    for (int r = 0; r < 5; r++) {
                        for (int c = 0; c < 5; c++) {
                            if (boards[b][r][c] == n) {
                                boards[b][r][c] = -1;
                                if ((boards[b][r][0] == -1 &&
                                        boards[b][r][1] == -1 &&
                                        boards[b][r][2] == -1 &&
                                        boards[b][r][3] == -1 &&
                                        boards[b][r][4] == -1) ||
                                        (boards[b][0][c] == -1 &&
                                                boards[b][1][c] == -1 &&
                                                boards[b][2][c] == -1 &&
                                                boards[b][3][c] == -1 &&
                                                boards[b][4][c] == -1)) {

                                    if (++winners == boardCount) {
                                        int sum = 0;
                                        for (int x = 0; x < 5; x++) {
                                            for (int y = 0; y < 5; y++) {
                                                if (boards[b][y][x] > -1) {
                                                    sum += boards[b][y][x];
                                                }
                                            }
                                        }
                                        return n * sum;
                                    } else {
                                        winning[b] = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return -7;
    }

    public static long day5p1(String[] input) {
        Stream<Point> points = Arrays.stream(input).flatMap(line -> {
            String[] ends = line.split(" -> ");
            String[] p1 = ends[0].split(",");
            String[] p2 = ends[1].split(",");
            int x1 = Integer.parseInt(p1[0]);
            int y1 = Integer.parseInt(p1[1]);
            int x2 = Integer.parseInt(p2[0]);
            int y2 = Integer.parseInt(p2[1]);

            Stream<Integer> xs = IntStream.rangeClosed(Math.min(x1, x2), Math.max(x1, x2)).boxed();
            Stream<Integer> ys = IntStream.rangeClosed(Math.min(y1, y2), Math.max(y1, y2)).boxed();

            if (x1 == x2) {
                return ys.map(y -> new Point(x1, y));
            }
            if (y1 == y2) {
                return xs.map(x -> new Point(x, y1));
            }
            return Stream.of();
        });
        Map<Point, Long> collect = points.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return collect
                .entrySet().stream().filter(e -> e.getValue() >= 2).count();
    }
    public static long day5p2(String[] input) {
        Stream<Point> points = Arrays.stream(input).flatMap(line -> {
            String[] ends = line.split(" -> ");
            String[] p1 = ends[0].split(",");
            String[] p2 = ends[1].split(",");
            int x1 = Integer.parseInt(p1[0]);
            int y1 = Integer.parseInt(p1[1]);
            int x2 = Integer.parseInt(p2[0]);
            int y2 = Integer.parseInt(p2[1]);

            Stream<Integer> xs = x1 > x2
                    ? IntStream.rangeClosed(x2, x1).map(i -> x1 - i + x2).boxed()
                    : IntStream.rangeClosed(x1, x2).boxed();
            Stream<Integer> ys = y1 > y2
                    ? IntStream.rangeClosed(y2, y1).map(i -> y1 - i + y2).boxed()
                    : IntStream.rangeClosed(y1, y2).boxed();

            if (x1 == x2) {
                return ys.map(y -> new Point(x1, y));
            }
            if (y1 == y2) {
                return xs.map(x -> new Point(x, y1));
            }
            Iterator<Integer> y = ys.iterator();
            return xs.map(x -> {
                Point point = new Point(x, y.next());
                return point;
            });
        });
        Map<Point, Long> collect = points.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return collect
                .entrySet().stream().filter(e -> e.getValue() >= 2).count();
    }

    public static long day6p1(String[] input) {
        String[] parts = input[0].split(",");
        int[] counts = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (String part : parts) {
            counts[Integer.parseInt(part)]++;
        }
        for (int d = 0; d < 80; d++) {
            int birth = counts[0];
            System.arraycopy(counts, 1, counts, 0, 8);
            counts[8] = birth;
            counts[6] += birth;
        }

        return Arrays.stream(counts).asLongStream().sum();
    }
    public static long day6p2(String[] input) {
        String[] parts = input[0].split(",");
        long[] counts = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (String part : parts) {
            counts[Integer.parseInt(part)]++;
        }
        for (int d = 0; d < 256; d++) {
            long birth = counts[0];
            System.arraycopy(counts, 1, counts, 0, 8);
            counts[8] = birth;
            counts[6] += birth;
        }

        return Arrays.stream(counts).sum();
    }

    public static long day7p1(String[] input) {
        List<Integer> crabs = Arrays.stream(input[0].split(",")).map(Integer::parseInt).collect(Collectors.toList());
        int sum = crabs.stream().mapToInt(Integer::intValue).sum();

        return IntStream.rangeClosed(Collections.min(crabs), Collections.max(crabs)).map(i ->
                crabs.stream().mapToInt(v -> Math.abs(v - i)).sum()
        ).min().getAsInt();
    }
    public static long day7p2(String[] input) {
        List<Integer> crabs = Arrays.stream(input[0].split(",")).map(Integer::parseInt).collect(Collectors.toList());
        int sum = crabs.stream().mapToInt(Integer::intValue).sum();

        return IntStream.rangeClosed(Collections.min(crabs), Collections.max(crabs)).map(i ->
                crabs.stream().mapToInt(v -> Math.abs(v - i)).map(v -> (v * (v+1)) / 2).sum()
        ).min().getAsInt();
    }

    public static record Day8(String seg, String res) {
        static String delim = " \\| ";
    }
    public static long day8p1(List<Day8> input) {
        Set<Integer> unique = Set.of(2, 3, 4, 7);
        return input.stream()
                .map(Day8::res)
                .map(r -> r.split(" "))
                .flatMap(Arrays::stream)
                .map(String::length)
                .filter(unique::contains)
                .count();
    }
    public static long day8p2(List<Day8> input) {
        return input.stream().mapToInt(i -> {

            List<String> numbers = Arrays.stream(i.seg.split(" "))
                    .collect(Collectors.toList());

            Set<Integer> n1 = numbers.stream()
                    .map(s -> s.chars().boxed().collect(Collectors.toSet()))
                    .filter(s -> s.size() == 2)
                    .findFirst().get();
            Set<Integer> n4 = numbers.stream()
                    .map(s -> s.chars().boxed().collect(Collectors.toSet()))
                    .filter(s -> s.size() == 4)
                    .findFirst().get();

            return Arrays.stream(i.res.split(" "))
                    .map(s -> {
                        switch (s.length()) {
                            case 2: return 1;
                            case 3: return 7;
                            case 4: return 4;
                            case 7: return 8;
                        }
                        Set<Integer> current = s.chars().boxed().collect(Collectors.toSet());

                        if (s.length() == 5) { // 2 | 3 | 5
                            if (current.containsAll(n1)) {
                                return 3;
                            }
                            current.retainAll(n4);
                            if (current.size() == 2) {
                                return 2;
                            }
                            return 5;
                        }

                        if (s.length() == 6) { // 0 | 6 | 9
                            if (!current.containsAll(n1)) {
                                return 6;
                            }
                            if (current.containsAll(n4)) {
                                return 9;
                            }
                            return 0;
                        }

                        return -1;
                    })
                    .reduce(0, (p, n) -> p * 10 + n);
        }).sum();
    }

    public static long day9p1(String[] input) {
        ArrayList<Character> low = new ArrayList<>();
        for (int r = 0; r < input.length; r++) {
            String row = input[r];
            for (int c = 0; c < row.length(); c++) {
                char point = row.charAt(c);
                if ((c == 0 || point < row.charAt(c-1)) && (c == row.length()-1 || point < row.charAt(c+1)) &&
                    (r == 0 || point < input[r-1].charAt(c)) && (r == input.length-1 || point < input[r+1].charAt(c))) {
                    low.add(point);
                }
            }
        }
        return low.stream().mapToInt(c -> c - '0').map(i -> i+1).sum();
    }
    record Low(char c, Point p) {}
    public static long day9p2(String[] input) {
        List<Integer> basins = new ArrayList<>();
        for (int r = 0; r < input.length; r++) {
            String row = input[r];
            for (int c = 0; c < row.length(); c++) {
                char point = row.charAt(c);
                if ((c == 0 || point < row.charAt(c-1)) && (c == row.length()-1 || point < row.charAt(c+1)) &&
                        (r == 0 || point < input[r-1].charAt(c)) && (r == input.length-1 || point < input[r+1].charAt(c))) {

                    LinkedList<Low> open = new LinkedList<>();
                    open.add(new Low(point, new Point(r, c)));

                    Set<Point> basin = new HashSet<>();
                    for (Low next = open.pollFirst(); next != null; next = open.pollFirst()) {
                        if (basin.add(next.p)) {
                            char nextc = next.c;
                            BiConsumer<Integer, Integer> addOpen = (x, y) -> {
                                char step = input[x].charAt(y);
                                if (step > nextc && step != '9') {
                                    open.add(new Low(step, new Point(x, y)));
                                }
                            };
                            if (next.p.y > 0) {
                                addOpen.accept(next.p.x, next.p.y - 1);
                            }
                            if (next.p.y < input.length - 1) {
                                addOpen.accept(next.p.x, next.p.y + 1);
                            }
                            if (next.p.x > 0) {
                                addOpen.accept(next.p.x - 1, next.p.y);
                            }
                            if (next.p.x < row.length() - 1) {
                                addOpen.accept(next.p.x + 1, next.p.y);
                            }
                        }
                    }
                    basins.add(basin.size());
                }
            }
        }
        return basins.stream().sorted(Comparator.reverseOrder()).limit(3).reduce(1, (a, b) -> a * b);
    }

    public static long day10p1(String[] input) {

        Map<Character, Integer> scores = Map.of(
                ')', 3,
                ']', 57,
                '}', 1197,
                '>', 25137
        );
        Map<Character, Character> openClose = Map.of(
                '(', ')',
                '[', ']',
                '{', '}',
                '<', '>'
        );
        int errorSum = 0;


        for (String line : input) {
            LinkedList<Character> queue = new LinkedList<>();
            for (char c : line.toCharArray()) {
                if (openClose.containsKey(c)) {
                    queue.push(openClose.get(c));
                } else if (queue.pollFirst() != c) {
                    errorSum += scores.get(c);
                    break;
                }
            }
        }
        return errorSum;
    }
    public static long day10p2(String[] input) {
        Map<Character, Long> scores = Map.of(
                ')', 1L,
                ']', 2L,
                '}', 3L,
                '>', 4L
        );
        Map<Character, Character> openClose = Map.of(
                '(', ')',
                '[', ']',
                '{', '}',
                '<', '>'
        );
        List<Long> results = Arrays.stream(input).map(line -> {
            LinkedList<Character> queue = new LinkedList<>();
            for (char c : line.toCharArray()) {
                if (openClose.containsKey(c)) {
                    queue.push(openClose.get(c));
                } else if (queue.pollFirst() != c) {
                    return 0L;
                }
            }
            return queue.stream().map(scores::get).reduce(0L, (s, v) -> s * 5 + v);
        }).filter(s -> s > 0).sorted().collect(Collectors.toList());
        return results.get(results.size() / 2);
    }

    public static long day11p1(String[] input) {
        int[][] oct = new int[input.length][];

        for (int i = 0; i < input.length; i++) {
            oct[i] = input[i].chars().map(c -> c - '0').toArray();
        }

        int flashed = 0;

        for (int day = 0; day < 100; day++) {

            var open = new LinkedList<Point>();
            var flash = new HashSet<Point>();

            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if (++oct[x][y] > 9) {
                        open.add(new Point(x, y));
                    }
                }
            }

            for (Point n = open.pollFirst(); n != null ; n = open.pollFirst()) {
                if (flash.add(n)) {
                    for (int x = Math.max(n.x-1, 0); x <= Math.min(n.x+1, 9); x++) {
                        for (int y = Math.max(n.y-1, 0); y <= Math.min(n.y+1, 9); y++) {
                            if ((x != n.x || y != n.y) && ++oct[x][y] > 9) {
                                open.add(new Point(x, y));
                            }
                        }
                    }
                }
            }

            for (Point n : flash) {
                oct[n.x][n.y] = 0;
            }

            flashed += flash.size();

        }

        return flashed;
    }
    public static long day11p2(String[] input) {
        int[][] oct = new int[input.length][];

        for (int i = 0; i < input.length; i++) {
            oct[i] = input[i].chars().map(c -> c - '0').toArray();
        }

        for (int day = 0; day < 1000; day++) {

            var open = new LinkedList<Point>();
            var flash = new HashSet<Point>();

            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if (++oct[x][y] > 9) {
                        open.add(new Point(x, y));
                    }
                }
            }

            for (Point n = open.pollFirst(); n != null ; n = open.pollFirst()) {
                if (flash.add(n)) {
                    for (int x = Math.max(n.x-1, 0); x <= Math.min(n.x+1, 9); x++) {
                        for (int y = Math.max(n.y-1, 0); y <= Math.min(n.y+1, 9); y++) {
                            if ((x != n.x || y != n.y) && ++oct[x][y] > 9) {
                                open.add(new Point(x, y));
                            }
                        }
                    }
                }
            }

            if (flash.size() == 100) {
                return day+1;
            }

            for (Point n : flash) {
                oct[n.x][n.y] = 0;
            }

        }

        return -1;
    }

    public static void main(String[] args) {
        int day = 11;
        new Year2021().invoke(day, 1);
        new Year2021().invoke(day, 2);
    }

}
