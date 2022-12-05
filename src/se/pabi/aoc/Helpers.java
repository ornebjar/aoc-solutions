package se.pabi.aoc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

abstract class Helpers {

    public static <T> Supplier<T> memoize(Supplier<T> original) {
        ConcurrentHashMap<Object, T> store=new ConcurrentHashMap<>();
        return ()->store.computeIfAbsent("dummy", key->original.get());
    }

    public static <T> Supplier<T> memoize1(Supplier<T> original) {
        return new Supplier<T>() {
            Supplier<T> delegate = this::firstTime;
            boolean initialized;
            public T get() {
                return delegate.get();
            }
            private synchronized T firstTime() {
                if(!initialized) {
                    T value=original.get();
                    delegate=() -> value;
                    initialized=true;
                }
                return delegate.get();
            }
        };
    }
}
