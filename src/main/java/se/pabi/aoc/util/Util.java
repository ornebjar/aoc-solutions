package se.pabi.aoc.util;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Util {
    private Util() {}

    public static Stream<String> groups(String line, Pattern pattern) {
        var matcher = pattern.matcher(line);
        var builder = Stream.<String>builder();
        while (matcher.find()) {
            for (int i = 1; i < matcher.groupCount(); i++) {
                builder.add(matcher.group(i));
            }
        }
        return builder.build();
    }

    public static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

}
