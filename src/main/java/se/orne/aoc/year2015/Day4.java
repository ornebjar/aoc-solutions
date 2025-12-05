package se.orne.aoc.year2015;

import se.orne.aoc.AdventOfCode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class Day4 extends AdventOfCode<String> {

    @Override
    public String input(String input) {
        return input.trim();
    }

    @Override
    public Object part1(String input) {
        return findLeading(input, "00000");
    }

    @Override
    public Object part2(String input) {
        return findLeading(input, "000000");
    }

    private static int findLeading(String input, String prefix) {
        int i = -1;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            while (true) {
                String hash = HexFormat.of().formatHex(md.digest((input + ++i).getBytes()));
                if (hash.startsWith(prefix)) {
                    return i;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
