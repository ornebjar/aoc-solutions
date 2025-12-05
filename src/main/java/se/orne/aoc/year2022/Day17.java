package se.orne.aoc.year2022;

import se.orne.aoc.AdventOfCode;

import java.math.BigInteger;
import java.util.*;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;
import static se.orne.aoc.util.StreamUtil.mapWithIndex;

public class Day17 extends AdventOfCode<Day17.Input> {

    record Point(int x, int y) {
        Point translate(int dx, int dy) {
            return new Point(x + dx, y + dy);
        }
    }

    static class Rock {
        final Point[] body;
        final Point[] left;
        final Point[] right;
        final Point[] down;
        final int height;

        Rock(String input) {
            body = mapWithIndex(input.lines(), (row, y) ->
                    mapWithIndex(row.chars().boxed(), (c, x) -> (c == '#') ? new Point(x, y) : null))
                    .flatMap(s -> s)
                    .filter(Objects::nonNull)
                    .toArray(Point[]::new);
            left = stream(body).collect(groupingBy(Point::y)).values().stream()
                    .map(line -> Collections.min(line, comparingInt(Point::x)))
                    .map(p -> p.translate(-1, 0))
                    .toArray(Point[]::new);
            right = stream(body).collect(groupingBy(Point::y)).values().stream()
                    .map(line -> Collections.max(line, comparingInt(Point::x)))
                    .map(p -> p.translate(1, 0))
                    .toArray(Point[]::new);
            down = stream(body).collect(groupingBy(Point::x)).values().stream()
                    .map(line -> Collections.min(line, comparingInt(Point::y)))
                    .map(p -> p.translate(0, -1))
                    .toArray(Point[]::new);
            height = (int) input.lines().count();
        }

        static Rock[] rocks = new Rock[]{
                new Rock("####"),
                new Rock("""
                        .#
                        ###
                        .#"""),
                new Rock("""
                        ###
                        ..#
                        ..#"""),
                new Rock("""
                        #
                        #
                        #
                        #"""),
                new Rock("""
                        ##
                        ##""")
        };

        boolean canMoveDown(Set<Point> map, int ox, int oy) {
            return stream(down)
                    .map(p -> p.translate(ox, oy))
                    .noneMatch(p -> map.contains(p) || p.y() == 0);
        }

        boolean canMoveLeft(Set<Point> map, int ox, int oy) {
            return stream(left)
                    .map(p -> p.translate(ox, oy))
                    .noneMatch(p -> map.contains(p) || p.x() == 0);
        }

        boolean canMoveRight(Set<Point> map, int ox, int oy) {
            return stream(right)
                    .map(p -> p.translate(ox, oy))
                    .noneMatch(p -> map.contains(p) || p.x() == 8);
        }
    }

    public record Input(int[] winds, Rock[] rocks) {
    }


    @Override
    public Input input(String input) {
//        input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>";
        int[] winds = input.chars().map(c -> c == '<' ? -1 : 1).toArray();
        return new Input(winds, Rock.rocks);
    }

    @Override
    public Object part1(Input input) {
        // Broken after solving part2, can't handle low numbers now.
        return solve(input, BigInteger.valueOf(2022));
    }

    @Override
    public Object part2(Input input) {
        return solve(input, new BigInteger("1000000000000"));
    }

    record State(int r, int w, int x, int h) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return r == state.r && w == state.w && x == state.x;
        }

        @Override
        public int hashCode() {
            return Objects.hash(r, w, x);
        }
    }

    private static BigInteger solve(Input input, BigInteger target) {
        List<State> states = new ArrayList<>();
        Map<State, Integer> count = new HashMap<>();

        int cycleHeight = 0;
        BigInteger cycles = BigInteger.ZERO;
        int times = 0;
        boolean foundCycle = false;

        Set<Point> map = new HashSet<>();

        int height = 0;
        long r = 0;
        long w = 0;

        int i = 0;
        for (; !foundCycle || i < times; i++) {
            int rx = 3;
            int ry = height + 4;
            Rock rock = input.rocks[(int) (r++ % input.rocks.length)];
            while (true) {
                int dx = input.winds[(int) (w++ % input.winds.length)];
                if ((dx < 0 && rock.canMoveLeft(map, rx, ry)) || (dx > 0 && rock.canMoveRight(map, rx, ry))) {
                    rx += dx;
                }
                if (!rock.canMoveDown(map, rx, ry)) {
                    break;
                }
                ry--;
            }

            if (!foundCycle) {
                State state = new State((int) (r % input.rocks.length), (int) (w % input.winds.length), rx, height);
                if (count.merge(state, 1, Integer::sum) == 3) {
                    foundCycle = true;

                    int first = states.lastIndexOf(state);
                    int cycle = states.size() - first;
                    cycleHeight = state.h - states.get(first).h;

                    BigInteger[] divideAndRemainder = target.subtract(BigInteger.valueOf(states.size()))
                            .divideAndRemainder(BigInteger.valueOf(cycle));
                    cycles = divideAndRemainder[0];
                    times = i + divideAndRemainder[1].intValue();
                }
                states.add(state);
            }

            height = addRock(map, height, rock, rx, ry);
        }

        return cycles.multiply(BigInteger.valueOf(cycleHeight)).add(BigInteger.valueOf(height));
    }

    private static int addRock(Set<Point> map, int height, Rock rock, int rx, int ry) {
        return stream(rock.body)
                .map(p -> p.translate(rx, ry))
                .peek(map::add)
                .map(Point::y)
                .reduce(height, Integer::max);
    }

}
