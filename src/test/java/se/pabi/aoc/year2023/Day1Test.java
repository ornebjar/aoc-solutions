package se.pabi.aoc.year2023;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day1Test {

    @Test
    void testNum() {
        var num = new Day1.Num();
        num.put("hej", 2);
        num.put("hoj", 3);
        num.put("0", 0);


        assertThat(num.get("he")).isNotPresent();
        assertThat(num.get("a")).isNotPresent();

        assertThat(num.get("hej")).hasValue(2);
        assertThat(num.get("hoj")).hasValue(3);
        assertThat(num.get("0")).hasValue(0);
    }

    @Test
    void testLine() {
        var num = new Day1.Num();
        for (int i = 0; i < 10; i++) {
            num.put(String.valueOf(i), i);
        }
        var line = "53sevenvvqm";

        assertThat(num.first(line)).hasValue(5);
        assertThat(num.last(line)).hasValue(3);
    }
}