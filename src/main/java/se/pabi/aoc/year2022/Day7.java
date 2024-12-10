package se.pabi.aoc.year2022;

import se.phet.aoc.AdventOfCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day7 extends AdventOfCode<String[]> {

    record Directory(Directory parent, Map<String, Path> files) implements Path {
        Directory(Directory parent) {
            this(parent, new HashMap<>());
        }

        Directory cd(String name) {
            Path to = files.get(name);
            if (to instanceof Directory toDir) {
                return toDir;
            } else {
                throw new IllegalArgumentException("Unable to cd into file: " + name);
            }
        }

        @Override
        public long size() {
            return files.values().stream().mapToLong(Path::size).sum();
        }
    }

    record File(long size) implements Path {
        @Override
        public long size() {
            return size;
        }
    }

    interface Path {
        long size();
    }

    @Override
    public String[] input(String input) {
        return input.lines().toArray(String[]::new);
    }

    @Override
    public Object part1(String[] input) {
        List<Directory> dirs = new ArrayList<>();
        getRoot(input, dirs);
        return dirs.stream()
                .filter(d -> d.size() <= 100000)
                .mapToLong(Directory::size)
                .sum();
    }

    @Override
    public Object part2(String[] input) {
        List<Directory> dirs = new ArrayList<>();
        var root = getRoot(input, dirs);
        long total = 70000000;
        long needed = 30000000;
        long free = total - root.size();
        long release = needed - free;
        return dirs.stream()
                .mapToLong(Directory::size)
                .filter(d -> d >= release)
                .min()
                .orElse(0);
    }

    private static Directory getRoot(String[] input, List<Directory> dirs) {
        var root = new Directory(null);
        Directory current = root;
        for (String line : input) {
            String[] parts = line.split(" ");
            if ("$".equals(parts[0])) {
                if (parts[1].equals("cd")) {
                    if ("/".equals(parts[2])) {
                        current = root;
                    } else {
                        current = "..".equals(parts[2]) ? current.parent : current.cd(parts[2]);
                    }
                }
            } else {
                if ("dir".equals(parts[0])) {
                    Directory to = new Directory(current);
                    dirs.add(to);
                    current.files.put(parts[1], to);
                } else {
                    current.files.put(parts[1], new File(Long.parseLong(parts[0])));
                }
            }
        }
        return root;
    }

}
