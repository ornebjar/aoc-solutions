package se.orne.aoc.year2025;

import se.orne.aoc.AdventOfCode;

import java.math.BigInteger;
import java.util.Collections;
import java.util.stream.Stream;

public class Day3 extends AdventOfCode<Stream<String>> {

    @Override
    public Stream<String> input(String input) {
        return input.lines();
    }

    @Override
    public Integer part1(Stream<String> input) {
        return input.mapToInt(line -> {
            var charList = line.chars().mapToObj(c -> c - '0').toList();
            int first = charList.stream().limit(line.length() - 1).max(Integer::compare).orElseThrow();
            int index = charList.indexOf(first);
            int last = charList.stream().skip(index + 1).max(Integer::compare).orElseThrow();
            return first * 10 + last;
        }).sum();
    }

    @Override
    public BigInteger part2(Stream<String> input) {
        return input.map(line -> {
            var charList = line.chars().mapToObj(c -> c - '0').toList();
            var total = BigInteger.ZERO;
            int pos = 0;
            for (int i = 0; i < 12; i++) {
                var sublist = charList.stream().skip(pos).limit(line.length() - pos - 11 +  i).toList();
                int max = Collections.max(sublist);
                pos = sublist.indexOf(max) + pos + 1;
                total = total.multiply(BigInteger.TEN).add(BigInteger.valueOf(max));
            }
            return total;
        }).reduce(BigInteger.ZERO, BigInteger::add);
    }

}
