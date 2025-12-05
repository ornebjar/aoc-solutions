package se.orne.aoc.year2023;

import org.junit.jupiter.api.Test;
import se.orne.aoc.year2023.Day5.MapRange;
import se.orne.aoc.year2023.Day5.Range;

import static org.assertj.core.api.Assertions.assertThat;

class Day5Test {

    @Test
    void testMapRange() {
        var mapRange = new MapRange(20, 10, 2);
        assertThat(mapRange.end()).isEqualTo(12);
        assertThat(mapRange.get(mapRange.source())).isEqualTo(20);
        assertThat(mapRange.get(mapRange.end())).isEqualTo(22);
    }

    @Test
    void testRange() {
        var mapRange = new MapRange(20, 10, 2);
        var range = new Range(9, 13);
        assertThat(range.overlap(mapRange)).isEqualTo(new Range(20, 22));
        assertThat(range.before(mapRange)).isEqualTo(new Range(9, 10));
        assertThat(range.after(mapRange)).isEqualTo(new Range(12, 13));
    }

    @Test
    void testRangeBefore() {
        var mapRange = new MapRange(20, 10, 2);
        assertThat(new Range(8, 9).before(mapRange)).isEqualTo(new Range(8, 9));
        assertThat(new Range(8, 10).before(mapRange)).isEqualTo(new Range(8, 10));
        assertThat(new Range(8, 11).before(mapRange)).isEqualTo(new Range(8, 10));
    }

    @Test
    void testRangeAfter() {
        var mapRange = new MapRange(20, 10, 2);
        assertThat(new Range(13, 14).after(mapRange)).isEqualTo(new Range(13, 14));
        assertThat(new Range(12, 14).after(mapRange)).isEqualTo(new Range(12, 14));
        assertThat(new Range(11, 14).after(mapRange)).isEqualTo(new Range(12, 14));
    }

    @Test
    void testRangeOverlap() {
        var mapRange = new MapRange(20, 10, 2);
        assertThat(new Range(8, 9).overlap(mapRange)).isNull();
        assertThat(new Range(8, 10).overlap(mapRange)).isNull();
        assertThat(new Range(8, 11).overlap(mapRange)).isEqualTo(new Range(20, 21));
        assertThat(new Range(9, 11).overlap(mapRange)).isEqualTo(new Range(20, 21));
        assertThat(new Range(9, 11).overlap(mapRange)).isEqualTo(new Range(20, 21));
        assertThat(new Range(10, 11).overlap(mapRange)).isEqualTo(new Range(20, 21));
        assertThat(new Range(10, 12).overlap(mapRange)).isEqualTo(new Range(20, 22));
        assertThat(new Range(11, 12).overlap(mapRange)).isEqualTo(new Range(21, 22));
        assertThat(new Range(11, 13).overlap(mapRange)).isEqualTo(new Range(21, 22));
        assertThat(new Range(12, 13).overlap(mapRange)).isNull();
        assertThat(new Range(12, 14).overlap(mapRange)).isNull();
        assertThat(new Range(13, 14).overlap(mapRange)).isNull();
    }
}