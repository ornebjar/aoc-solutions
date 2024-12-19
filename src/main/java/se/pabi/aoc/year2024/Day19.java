package se.pabi.aoc.year2024;

class Day19 extends AdventOfCode<Stream<String>> {

    enum Color {
        r, g, b, u, w;

        static Color from(char c) {
            return switch (c) {
                case 'r' -> r;
                case 'g' -> g;
                case 'b' -> b;
                case 'u' -> u;
                case 'w' -> w;
                default -> throw new IllegalArgumentException("Unknown color: " + c);
            };
        }
    }

    record Words(Words[] next, boolean word) {

        Words(boolean word) {
            this(new Words[5], word);
        }

        Words() {
            this(new Words[5], false);
        }

        boolean isWord(String word) {
            return isWord(word, 0);
        }

        private boolean isWord(String word, int i) {
            if (i == word.length()) {
                return this.word;
            }
            int index = Color.from(word.charAt(i)).ordinal();
            return next[index] != null && next[index].isWord(word, i + 1);
        }

        void addWord(String word) {
            addWord(word, 0);
        }

        private void addWord(String word, int i) {
            int index = Color.from(word.charAt(i)).ordinal();
            if (next[index] == null) {
                if (i == word.length() - 1) {
                    next[index] = new Words(true);
                    return;
                } else {
                    next[index] = new Words();
                }
            }
            next[index].addWord(word, i + 1);
        }

        IntStream matches(String word, int i) {
            IntStream.Builder builder = IntStream.builder();
            matches(word, builder, i, 0);
            return builder.build();
        }

        void matches(String word, IntStream.Builder builder, int i, int len) {
            if (i == word.length()) {
                return;
            }
            int index = Color.from(word.charAt(i)).ordinal();
            if (next[index] != null) {
                if (next[index].word) {
                    builder.accept(len + 1);
                }
                next[index].matches(word, builder, i + 1, len + 1);
            }
        }

    }

    public static void main(String[] args) throws IOException {


        var lines = Files.lines(Path.of(
                "/Users/patbjo/Library/Application Support/JetBrains/IntelliJIdea2024.3/scratches/scratch_101.txt"
        )).toList();

//        var lines = """
//                r, wr, b, g, bwu, rb, gb, br
//
//                brwrr
//                bggr
//                gbbr
//                rrbgbr
//                ubwu
//                bwurrg
//                brgr
//                bbrgwb
//                """.lines().toList();

        var words = new Words();

        Arrays.stream(lines.getFirst().split(", ")).sorted().forEach(words::addWord);

        var result = lines.stream().skip(2)
                .mapToLong(line -> {
                    long[] count = new long[line.length() + 1];
                    check(line, words, count);
                    return count[line.length()];
                }).sum();

        System.out.println(result);
    }

    private static void check(String line, Words words, long[] count) {
        count[0] = 1;
        for (int i = 0; i < line.length(); i++) {
            if (count[i] == 0) {
                continue;
            }
            var matches = words.matches(line, i);
            int finalI = i;
            matches.forEach(len -> count[finalI + len] += count[finalI]);
        }
    }
}