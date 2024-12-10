package se.pabi.aoc.year2024;

import se.pabi.aoc.base.AdventOfCode;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;

public class Day9 extends AdventOfCode<int[]> {

    @Override
    public int[] input(String input) {
        return input.chars()
                .map(c -> c - '0')
                .toArray();
    }

    @Override
    public Object part1(int[] input) {
        BigInteger result = BigInteger.ZERO;
        int id = 0;
        long blockid = 0;
        int last = (input.length-1) / 2;

        for (int i = 0; id <= last; i++) {
            if (i % 2 == 0) {
                for (int k = 0; k < input[id * 2]; k++) {
                    result = result.add(BigInteger.valueOf(blockid++ * id));
                }
                id++;
            } else {
                for (int k = 0; k < input[id * 2 - 1]; k++) {
                    while (input[last * 2] == 0) {
                        last--;
                    }
                    if (last >= id) {
                        input[last * 2]--;
                        result = result.add(BigInteger.valueOf(blockid++ * last));
                    }
                }
            }
        }
        return result;
    }

    sealed interface Disk permits File, Space {
    }

    record File(int id, int size) implements Disk {
    }

    record Space(int size) implements Disk {
    }

    @Override
    public Object part2(int[] input) {
        LinkedList<Disk> disks = new LinkedList<>();
        for (int i = 0; i < input.length; i++) {
            if (i % 2 == 0) {
                disks.add(new File(i / 2, input[i]));
            } else {
                disks.add(new Space(input[i]));
            }
        }

        for (File file : disks.reversed().stream().filter(d -> d instanceof File).map(File.class::cast).toList()) {
            for (ListIterator<Disk> iterator = disks.listIterator(); iterator.hasNext(); ) {
                Disk disk1 = iterator.next();
                if (disk1 instanceof Space(int size)) {
                    if (size >= file.size) {
                        iterator.set(file);
                        if (size > file.size) {
                            iterator.add(new Space(size - file.size));
                        }
                        break;
                    }
                }
            }
        }

        return getResult(disks);
    }

    private static BigInteger getResult(LinkedList<Disk> disks) {
        var result = BigInteger.ZERO;
        var used = new HashSet<>();
        long blockId = 0;
        for (Disk disk : disks) {
            switch (disk) {
                case File file -> {
                    if (used.add(file.id)) {
                        for (int i = 0; i < file.size; i++) {
                            result = result.add(BigInteger.valueOf(blockId++ * file.id));
                        }
                    } else {
                        blockId += file.size;
                    }
                }
                case Space space -> blockId += space.size;
            }
        }
        return result;
    }
}
