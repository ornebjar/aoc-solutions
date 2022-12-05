package se.pabi.aoc;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aoc {

    private static final String URL = "https://adventofcode.com/%s/day/%s/input";

    private static final String COOKIE = "Cookie here";

    private final int year;

    public Aoc(int year) {
        this.year = year;
    }

    public void invoke(int day, int part) {
        try {
            Object input;
            Method method;
            try {
                method = this.getClass().getMethod("day" + day + "p" + part, List.class);
                Class<?> dayRecord = Class.forName(this.getClass().getName() + "$Day" + day);
                Constructor<?> rowConstructor = dayRecord.getDeclaredConstructors()[0];
                Field[] fields = dayRecord.getDeclaredFields();
                String delim = staticField(dayRecord, "delim").orElse(" ");
                Optional<String> splitter = staticField(dayRecord, "splitter");
                input = readInput(day, splitter)
                        .map(line -> convertLine(delim, rowConstructor, line))
                        .collect(Collectors.toList());
            } catch (NoSuchMethodException e) {
                method = this.getClass().getMethod("day" + day + "p" + part, String[].class);
                input = readInput(day, Optional.empty()).toArray(String[]::new);
            }
            long startTime = System.currentTimeMillis();
            Object result = method.invoke(null, input);
            long timeTaken = System.currentTimeMillis() - startTime;
            System.out.printf("Result: %s Taken: %sms%n", result, timeTaken);
        } catch (NoSuchMethodException | ClassNotFoundException |
                IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Optional<String> staticField(Class<?> dayRecord, String fieldName) {
        return Arrays.stream(dayRecord.getDeclaredFields())
                .filter(f -> fieldName.equals(f.getName()))
                .findFirst()
                .map(f -> {
                    try {
                        return f.get(f);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(String.class::cast);
    }

    private Object convertLine(String delim, Constructor<?> rowConstructor, String line) {
        try {
            String[] parts = line.split(delim);
            Object[] arguments = new Object[parts.length];

            Class<?>[] types = rowConstructor.getParameterTypes();
            for (int i = 0; i < parts.length; i++) {
                arguments[i] = convert(types[i], parts[i]);
            }
            return rowConstructor.newInstance(arguments);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static Object convert(Class<?> type, String part) {
        if (type == String.class) {
            return part;
        } else if (type == Integer.TYPE) {
            return Integer.parseInt(part);
        }
        return null;
    }

    private Stream<String> readInput(int day, Optional<String> splitter) {
        Path inputPath = Path.of("/temp/aoc/%s/input%s.csv".formatted(year, day));
        try {
            if (!Files.exists(inputPath)) {
                List<String> download = downloadInput(day).collect(Collectors.toList());
                if (download.equals(List.of("Puzzle inputs differ by user.  Please log in to get your puzzle input."))) {
                    throw new AuthenticationException("New cookie required");
                }
                Files.createDirectories(inputPath.getParent());
                Files.createFile(inputPath);
                Files.write(inputPath, download);
            }
            String s = Files.readString(inputPath);
            if (splitter.isPresent()) {
                return Arrays.stream(s.split(splitter.get(), -1));
            }
            return s.lines();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Stream<String> downloadInput(int day) {
        try {
            String url = URL.formatted(year, day);
            System.err.println("Downloading file " + url);
            return HttpClient.newHttpClient().send(
                    HttpRequest.newBuilder()
                            .uri(new URI(url))
                            .header("cookie", COOKIE)
                            .GET()
                            .build(),
                    HttpResponse.BodyHandlers.ofLines()
            ).body();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
