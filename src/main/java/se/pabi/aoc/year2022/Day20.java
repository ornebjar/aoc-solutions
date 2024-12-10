package se.pabi.aoc.year2022;

import se.phet.aoc.AdventOfCode;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day20 extends AdventOfCode<IntStream> {

    static class Node {
        final int move;
        final BigInteger org;
        Node prev = null;
        Node next = null;

        Node(BigInteger org, BigInteger size) {
            this.org = org;
            this.move = org.mod(size).intValue();
        }

        public void remove() {
            prev.next = next;
            next.prev = prev;
        }

        @Override
        public String toString() {
            return String.valueOf(org);
        }
    }

    @Override
    public IntStream input(String input) {
//        input = "1\n2\n-3\n3\n-2\n0\n4";
        return input.lines().mapToInt(Integer::parseInt);
    }

    @Override
    public Object part1(IntStream input) {
        int[] ints = input.toArray();
        BigInteger size = BigInteger.valueOf(ints.length - 1);
        List<Node> nodes = Arrays.stream(ints)
                .mapToObj(org -> new Node(BigInteger.valueOf(org), size))
                .toList();

        linkNodes(nodes);
        List<Node> result = mix(nodes);

        return groveCoordinates(result);
    }

    @Override
    public Object part2(IntStream input) {
        BigInteger key = new BigInteger("811589153");
        int[] ints = input.toArray();
        BigInteger size = BigInteger.valueOf(ints.length - 1);

        List<Node> nodes = Arrays.stream(ints)
                .mapToObj(BigInteger::valueOf)
                .map(key::multiply)
                .map(org -> new Node(org, size))
                .toList();


        linkNodes(nodes);

        for (int i = 0; i < 9; i++) {
            mix(nodes);
        }
        List<Node> result =  mix(nodes);

        return groveCoordinates(result);
    }

    private static List<Node> mix(List<Node> order) {
        for (Node node : order) {
            if (node.move != 0) {
                node.remove();
                Node next = node;
                for (int i = 0; i < node.move; i++) {
                    next = next.next;
                }
                //  next  -  node  -  next.next
                next.next.prev = node;
                node.next = next.next;
                node.prev = next;
                next.next = node;
            }
        }

        Node zero = order.stream().filter(n -> n.move == 0).findFirst().orElseThrow();

        return Stream.iterate(zero, node -> node.next).limit(order.size()).toList();
    }

    private static void linkNodes(List<Node> nodes) {
        for (int i = 1; i < nodes.size(); i++) {
            nodes.get(i).prev = nodes.get(i-1);
            nodes.get(i-1).next = nodes.get(i);
        }
        Node last = nodes.get(nodes.size() - 1);
        nodes.get(0).prev = last;
        last.next = nodes.get(0);
    }

    private static BigInteger groveCoordinates(List<Node> result) {
        return Stream.of(1000, 2000, 3000)
                .map(i -> i % result.size())
                .map(result::get)
                .map(i -> i.org)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

}
