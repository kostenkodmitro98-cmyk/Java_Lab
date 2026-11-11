import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static List<Book> books = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1 - Найдовший рядок за кількістю слів");
            System.out.println("2 - Додати книгу");
            System.out.println("3 - Показати книги");
            System.out.println("4 - Зберегти книги");
            System.out.println("5 - Завантажити книги");
            System.out.println("6 - Зашифрувати файл");
            System.out.println("7 - Розшифрувати файл");
            System.out.println("8 - Порахувати HTML-теги");
            System.out.println("0 - Вихід");
            System.out.print("Вибір: ");

            String choice = sc.nextLine();

            try {
                switch (choice) {
                    case "1" -> longestLine();
                    case "2" -> addBook();
                    case "3" -> books.forEach(System.out::println);
                    case "4" -> {
                        FileManager.saveBooks(ask("Файл для збереження: "), books);
                        System.out.println("Збережено");
                    }
                    case "5" -> {
                        books = FileManager.loadBooks(ask("Файл для читання: "));
                        System.out.println("Завантажено: " + books.size());
                    }
                    case "6" -> crypt(true);
                    case "7" -> crypt(false);
                    case "8" -> tags();
                    case "0" -> {
                        return;
                    }
                    default -> System.out.println("Немає такого пункту");
                }
            } catch (Exception e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }

    static String ask(String text) {
        System.out.print(text);
        return sc.nextLine().trim();
    }

    static void longestLine() throws IOException {
        Path path = Path.of(ask("Шлях до текстового файлу: "));
        String longest = "";
        int max = 0;

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;

            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                int words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;

                if (words > max) {
                    max = words;
                    longest = line;
                }
            }
        }

        System.out.println("Слів: " + max);
        System.out.println("Рядок: " + longest);
    }

    static void addBook() {
        String title = ask("Назва книги: ");
        String author = ask("Автор: ");

        if (title.isEmpty() || author.isEmpty()) {
            throw new IllegalArgumentException("Заповніть обидва поля");
        }

        books.add(new Book(title, author));
    }

    static void crypt(boolean encrypt) throws IOException {
        String input = ask("Вхідний файл: ");
        String output = ask("Вихідний файл: ");
        String keyText = ask("Ключовий символ: ");

        if (keyText.length() != 1) {
            throw new IllegalArgumentException("Потрібен один символ ключа");
        }

        int shift = keyText.charAt(0);

        if (Path.of(input).toAbsolutePath().normalize()
                .equals(Path.of(output).toAbsolutePath().normalize())) {
            throw new IllegalArgumentException("Вхідний і вихідний файли мають відрізнятися");
        }

        if (encrypt) {
            try (Reader r = Files.newBufferedReader(Path.of(input), StandardCharsets.UTF_8);
                 Writer w = new ShiftWriter(
                         Files.newBufferedWriter(Path.of(output), StandardCharsets.UTF_8), shift)) {
                copy(r, w);
            }
        } else {
            try (Reader r = new ShiftReader(
                    Files.newBufferedReader(Path.of(input), StandardCharsets.UTF_8), shift);
                 Writer w = Files.newBufferedWriter(Path.of(output), StandardCharsets.UTF_8)) {
                copy(r, w);
            }
        }

        System.out.println("Готово");
    }

    static void copy(Reader reader, Writer writer) throws IOException {
        char[] buffer = new char[2048];
        int count;

        while ((count = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, count);
        }
    }

    static void tags() throws Exception {
        String url = ask("URL (Enter = https://example.com): ");

        if (url.isEmpty()) {
            url = "https://example.com";
        }

        Map<String, Integer> counts = new TreeMap<>();
        Pattern pattern = Pattern.compile("<\\s*([a-zA-Z][a-zA-Z0-9:-]*)\\b[^>]*>");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(URI.create(url).toURL().openStream(),
                        StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {
                    counts.merge(matcher.group(1).toLowerCase(Locale.ROOT), 1, Integer::sum);
                }
            }
        }

        System.out.println("За назвою:");
        counts.forEach((name, count) -> System.out.println(name + ": " + count));

        System.out.println("За частотою (зростання):");
        counts.entrySet().stream()
                .sorted(Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                        .thenComparing(Map.Entry::getKey))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
    }
}

class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    String title;
    String author;

    Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String toString() {
        return title + " — " + author;
    }
}

class ShiftWriter extends FilterWriter {
    private final int shift;

    ShiftWriter(Writer writer, int shift) {
        super(writer);
        this.shift = shift;
    }

    public void write(int c) throws IOException {
        super.write((char) (c + shift));
    }

    public void write(char[] data, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) {
            write(data[i]);
        }
    }

    public void write(String str, int off, int len) throws IOException {
        write(str.toCharArray(), off, len);
    }
}

class ShiftReader extends FilterReader {
    private final int shift;

    ShiftReader(Reader reader, int shift) {
        super(reader);
        this.shift = shift;
    }

    public int read() throws IOException {
        int c = super.read();
        return c == -1 ? -1 : (char) (c - shift);
    }

    public int read(char[] data, int off, int len) throws IOException {
        int n = super.read(data, off, len);

        for (int i = off; i < off + Math.max(n, 0); i++) {
            data[i] = (char) (data[i] - shift);
        }

        return n;
    }
}
