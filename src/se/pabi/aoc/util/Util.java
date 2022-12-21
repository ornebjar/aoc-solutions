package se.pabi.aoc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Util {
    private Util() {}

    public static Stream<String> groups(String line, Pattern pattern) {
        Matcher matcher = pattern.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Input is broken");
        }
        return IntStream.rangeClosed(1, matcher.groupCount())
                .mapToObj(matcher::group);
    }

}
