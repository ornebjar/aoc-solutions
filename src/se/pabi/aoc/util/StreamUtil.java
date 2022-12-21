package se.pabi.aoc.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class StreamUtil {
    private StreamUtil() {}

    public static <T, K> Stream<K> mapWithIndex(Stream<T> stream, BiFunction<T, Integer, K> mapper) {
        AtomicInteger index = new AtomicInteger(0);
        return stream.map(t -> mapper.apply(t, index.getAndIncrement()));
    }

}
