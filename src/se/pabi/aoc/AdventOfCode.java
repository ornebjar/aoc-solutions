package se.pabi.aoc;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public abstract class AdventOfCode<T> {

    public AdventOfCode() {
        Package pkg = getClass().getPackage();
        String year = pkg.getName().replaceAll("\\D+", "");
        String day = getClass().getSimpleName().replaceAll("\\D+", "");

        T i1 = input(readInput(year, day));
        long startTime = System.currentTimeMillis();
        Object r1 = part1(i1);
        long timeTaken = System.currentTimeMillis() - startTime;
        System.out.printf("Part 1 in %s: %s%n", millisToString(timeTaken), r1);

        T i2 = input(readInput(year, day));
        startTime = System.currentTimeMillis();
        Object r2 = part2(i2);
        timeTaken = System.currentTimeMillis() - startTime;
        System.out.printf("Part 2 in %s: %s%n", millisToString(timeTaken), r2);
    }

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat(
            "0.00",
            new DecimalFormatSymbols(Locale.US));
    private static String millisToString(long ms) {
        return ms >= 1000
                ? DECIMAL_FORMAT.format((double) ms / 1000) + "s"
                : ms + "ms";
    }

    abstract public T input(String input);

    public Object part1(T input) {
        return null;
    }

    public Object part2(T input) {
        return null;
    }

    private static final String URL = "https://adventofcode.com/%s/day/%s/input";

    private static final String COOKIE = "_ga=GA1.2.1410517933.1650308922;session=" +
            "53616c7465645f5fbd477ab32bc059042914783489e6fce" + "cac593a9e513d7705" +
            "85f30e144df505f6612d282b57d9beb45a000636eee431c349a3abf158d4eeee";

    private String readInput(String year, String day) {
        Path inputPath = Path.of("/temp/aoc/%s/input%s.csv".formatted(year, day));
        try {
            if (!Files.exists(inputPath)) {
                String download = downloadInput(year, day);
                if ("Puzzle inputs differ by user.  Please log in to get your puzzle input.".equals(download)) {
                    throw new AuthenticationException("New cookie required");
                }
                Files.createDirectories(inputPath.getParent());
                Files.createFile(inputPath);
                Files.writeString(inputPath, download);
            } else {
                System.out.println("Using downloaded file from " + inputPath);
            }
            return Files.readString(inputPath);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String downloadInput(String year, String day) {
        try {
            String url = URL.formatted(year, day);
            System.err.println("Downloading file " + url);
            return HttpClient.newHttpClient().send(
                    HttpRequest.newBuilder()
                            .uri(new URI(url))
                            .header("cookie", COOKIE)
                            .GET()
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            ).body();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
