package se.pabi.aoc.year2022;

import se.phet.aoc.AdventOfCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day13 extends AdventOfCode<Day13.Pair[]> {

    interface Ob extends Comparable<Ob> {
    }

    static class Li implements Ob {
        final List<Ob> objects = new ArrayList<>();

        int scan(String input, int pos) {
            pos++;
            char c;
            while ((c = input.charAt(pos)) != ']') {
                if (c == '[') {
                    Li li = new Li();
                    objects.add(li);
                    pos = li.scan(input, pos);
                } else if (c == ',') {
                    pos++;
                } else {
                    Va va = new Va();
                    objects.add(va);
                    pos = va.scan(input, pos);
                }
            }
            return pos + 1;
        }

        @Override
        public int compareTo(Ob o) {
            if (o instanceof Li li) {
                for (int i = 0; i < Math.max(objects.size(), li.objects.size()); i++) {
                    if (i == objects.size()) {
                        return -1;
                    } else if (i == li.objects.size()) {
                        return 1;
                    } else {
                        int compare = objects.get(i).compareTo(li.objects.get(i));
                        if (compare != 0) {
                            return compare;
                        }
                    }
                }
            } else if (o instanceof Va va) {
                Li single = new Li();
                single.objects.add(va);
                return this.compareTo(single);
            }
            return 0;
        }
    }

    static class Va implements Ob {
        Integer value = 0;

        int scan(String input, int pos) {
            char c;
            while ((c = input.charAt(pos)) >= '0' && c <= '9') {
                value = value * 10 + (c - '0');
                pos++;
            }
            return pos;
        }

        @Override
        public int compareTo(Ob o) {
            if (o instanceof Li li) {
                Li single = new Li();
                single.objects.add(this);
                return single.compareTo(li);
            } else if (o instanceof Va va) {
                return value.compareTo(va.value);
            }
            return 0;
        }
    }

    public record Pair(Li left, Li right) {
        Pair(List<String> pair) {
            this(new Li(), new Li());
            left.scan(pair.get(0), 0);
            right.scan(pair.get(1), 0);
        }
    }
    @Override
    public Pair[] input(String input) {
        String[] pairs = input.split("\n\n");
        return Arrays.stream(pairs)
                .map(String::lines)
                .map(Stream::toList)
                .map(Pair::new)
                .toArray(Pair[]::new);
    }

    @Override
    public String part1(Pair[] pairs) {
        int sum = 0;
        for (int i = 0; i < pairs.length; i++) {
            Pair pair = pairs[i];
            if (pair.left.compareTo(pair.right) < 0) {
                sum += i + 1;
            }
        }
        return sum + "";
    }

    @Override
    public String part2(Pair[] pairs) {
        Pair dividers = new Pair(List.of("[[2]]", "[[6]]"));
        Li left = dividers.left;
        Li right = dividers.right;
        List<Li> sorted = Stream.concat(Arrays.stream(pairs), Stream.of(dividers))
                .flatMap(p -> Stream.of(p.left, p.right))
                .sorted()
                .toList();
        int first = sorted.indexOf(left) + 1;
        int second = sorted.indexOf(right) + 1;
        return String.valueOf(first * second);
    }
}
