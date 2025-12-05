package se.orne.aoc.util;

public record IntPoint(int x, int y) {

    public IntPoint add(int dx, int dy) {
        return new IntPoint(x + dx, y + dy);
    }

    public IntPoint add(IntPoint other) {
        return add(other.x, other.y);
    }

    public IntPoint subtract(IntPoint other) {
        return new IntPoint(x - other.x, y - other.y);
    }

    public IntPoint multiply(int factor) {
        return new IntPoint(x * factor, y * factor);
    }

    public IntPoint rotate90() {
        //noinspection SuspiciousNameCombination
        return new IntPoint(-y, x);
    }

    public static IntPoint parse(String s) {
        String[] parts = s.split(",");
        return new IntPoint(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    public int length() {
        return Math.abs(x) + Math.abs(y);
    }

}
