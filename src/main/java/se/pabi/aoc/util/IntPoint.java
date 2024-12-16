package se.pabi.aoc.util;

public record IntPoint(int x, int y) {
    public IntPoint add(int dx, int dy) {
        return new IntPoint(x + dx, y + dy);
    }

    public IntPoint add(IntPoint other) {
        return add(other.x, other.y);
    }
}
