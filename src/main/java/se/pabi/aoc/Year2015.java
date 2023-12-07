package se.pabi.aoc;

import se.pabi.aoc.base.Aoc;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Year2015 extends Aoc {

    public Year2015() {
        super(2015);
    }

    public static int day1p2(String input) {

        int floor = 0;

        for (int i = 0; i < input.length(); i++) {
            char p = input.charAt(i);
            if (p == '(') {
                floor++;
            } else {
                if (floor-- == 0) {
                    return i+1;
                }
            }
        }

        return -1;
    }

    record Day2(int l, int w, int h) {
        static String delim = "x";
    }
    public static int day2p1(List<Day2> input) {

        int p = 0;

        for (Day2 box : input) {
            int s1 = box.l * box.w;
            int s2 = box.w * box.h;
            int s3 = box.l * box.h;

            p += 2 * (s1 + s2 + s3) + Math.min(s1, Math.min(s2, s3));
        }

        return p;
    }
    public static int day2p2(List<Day2> input) {

        int r = 0;

        for (Day2 box : input) {
            int s1 = box.w;
            int s2 = box.h;
            int s3 = box.l;

            r += 2 * (s1 + s2 + s3 - Math.max(s1, Math.max(s2, s3))) + (s1*s2*s3);
        }

        return r;
    }

    public static int day3p1(String input) {

        int p = 0;

        Set<Point> houses = new HashSet<>();
        houses.add(new Point(0, 0));

        int x = 0;
        int y = 0;

        for (char c : input.toCharArray()) {
            switch (c) {
                case '<': x--; break;
                case '>': x++; break;
                case '^': y--; break;
                case 'v': y++; break;
            }
            houses.add(new Point(x, y));
        }

        return houses.size();
    }
    public static int day3p2(String input) {

        int p = 0;

        Set<Point> houses = new HashSet<>();
        houses.add(new Point(0, 0));

        int sx = 0;
        int sy = 0;
        int rx = 0;
        int ry = 0;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (i % 2 == 0) {
                switch (c) {
                    case '<': rx--; break;
                    case '>': rx++; break;
                    case '^': ry--; break;
                    case 'v': ry++; break;
                }
                houses.add(new Point(rx, ry));
            } else {
                switch (c) {
                    case '<': sx--; break;
                    case '>': sx++; break;
                    case '^': sy--; break;
                    case 'v': sy++; break;
                }
                houses.add(new Point(sx, sy));
            }
        }

        return houses.size();
    }

    public static void main(String[] args) {
        new Year2015().invoke(3, 2);
    }

}
