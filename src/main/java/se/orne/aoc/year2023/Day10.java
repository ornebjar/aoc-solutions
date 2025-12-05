package se.orne.aoc.year2023;

import se.orne.aoc.AdventOfCode;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

public class Day10 extends AdventOfCode<String[]> {
    @Override
    public String[] input(String input) {
        return input.lines().toArray(String[]::new);
    }

    private record Pos(int x, int y) {
        Pos left() {
            return new Pos(x - 1, y);
        }
        Pos right() {
            return new Pos(x + 1, y);
        }
        Pos up() {
            return new Pos(x, y - 1);
        }
        Pos down() {
            return new Pos(x, y + 1);
        }
    }

    @Override
    public Object part1(String[] input) {
        var start = findStart(input);
        var next = firstStep(input, start);
        var prev = start;
        int i = 1;
        for (; !next.equals(start); i++) {
            var current = next;
            next = nextStep(input, prev, next);
            prev = current;
        }
        return i / 2;
    }

    @Override
    public Object part2(String[] input) {
        var start = findStart(input);
        var next = firstStep(input, start);
        var prev = start;
        var loop = new HashSet<Pos>();
        loop.add(next);
        while (!next.equals(start)) {
            var current = next;
            next = nextStep(input, prev, next);
            prev = current;
            loop.add(next);
        }

        next = firstStep(input, start);
        prev = start;
        var enclosed = new HashSet<Pos>();
        seek(enclosed, loop, start, next);
        while (!next.equals(start)) {
            var current = next;
            next = nextStep(input, prev, next);
            prev = current;
            seek(enclosed, loop, prev, next);
        }

        prettyPrint(input, (int) Math.sqrt(new Random().nextDouble(36)), loop, enclosed);

        return enclosed.size();
    }

    private static final char[][] STYLES = new char[][] {
            new char[] { 'F', 'J', 'L', '7', '|', '-' },
            new char[] { '╒', '╛', '╘', '╕', '│', '═' },
            new char[] { '╓', '╜', '╙', '╖', '║', '─' },
            new char[] { '╔', '╝', '╚', '╗', '║', '═' },
            new char[] { '┌', '┘', '└', '┐', '│', '─' },
            new char[] { '╭', '╯', '╰', '╮', '│', '─' },
    } ;

    private void prettyPrint(String[] input, int style, HashSet<Pos> loop, HashSet<Pos> enclosed) {
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length(); x++) {
                var pos = new Pos(x, y);
                if (loop.contains(pos)) {
                    switch (get(input, pos)) {
                        case 'S' -> System.out.print("S");
                        case 'F' -> System.out.print(STYLES[style][0]);
                        case 'J' -> System.out.print(STYLES[style][1]);
                        case 'L' -> System.out.print(STYLES[style][2]);
                        case '7' -> System.out.print(STYLES[style][3]);
                        case '|' -> System.out.print(STYLES[style][4]);
                        case '-' -> System.out.print(STYLES[style][5]);
                        default -> throw new RuntimeException("Unknown char: " + get(input, pos));
                    }
                } else if (enclosed.contains(pos)) {
                    System.out.print("X");
                } else {
                    System.out.print(get(input, pos) == '.' ? "." : " ");
                }
            }
            System.out.println();
        }
    }

    private void seek(HashSet<Pos> enclosed, HashSet<Pos> loop, Pos from, Pos to) {
        var queue = new LinkedList<Pos>();
        if (from.equals(to.left())) {
            queue.add(from.down());
            queue.add(to.down());
        } else if (from.equals(to.right())) {
            queue.add(from.up());
            queue.add(to.up());
        } else if (from.equals(to.up())) {
            queue.add(from.left());
            queue.add(to.left());
        } else if (from.equals(to.down())) {
            queue.add(from.right());
            queue.add(to.right());
        } else {
            throw new RuntimeException("Not adjacent");
        }
        while (!queue.isEmpty()) {
            var pos = queue.poll();
            if (!loop.contains(pos) && enclosed.add(pos)) {
                queue.add(pos.left());
                queue.add(pos.right());
                queue.add(pos.up());
                queue.add(pos.down());
            }
        }
    }

    private Pos nextStep(String[] input, Pos prev, Pos next) {
        return switch (get(input, next)) {
            case 'F' -> next.down().equals(prev) ? next.right() : next.down();
            case 'J' -> next.up().equals(prev) ? next.left() : next.up();
            case 'L' -> next.right().equals(prev) ? next.up() : next.right();
            case '7' -> next.left().equals(prev) ? next.down() : next.left();
            case '|' -> next.up().equals(prev) ? next.down() : next.up();
            case '-' -> next.right().equals(prev) ? next.left() : next.right();
            default -> throw new RuntimeException("Unknown char: " + get(input, next));
        };
    }

    private Pos firstStep(String[] input, Pos start) {
        if (Set.of('J', '7', '-').contains(get(input, start.right()))) {
            return start.right();
        } else if (Set.of('7', 'F', '|').contains(get(input, start.up()))) {
            return start.up();
        } else if (Set.of('F', 'L', '-').contains(get(input, start.left()))) {
            return start.left();
        } else if (Set.of('J', 'L', '|').contains(get(input, start.down()))) {
            return start.down();
        }
        throw new RuntimeException("No first step found");
    }

    private char get(String[] input, Pos pos) {
        if (pos.y < 0 || pos.y >= input.length || pos.x < 0 || pos.x >= input[pos.y].length()) {
            return ' ';
        }
        return input[pos.y].charAt(pos.x);
    }

    private static Pos findStart(String[] input) {
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length(); x++) {
                if (input[y].charAt(x) == 'S') {
                    return new Pos(x, y);
                }
            }
        }
        throw new RuntimeException("No start found");
    }
}
