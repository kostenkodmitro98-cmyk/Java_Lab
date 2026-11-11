import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

interface Drawable {
    void draw();
}

abstract class Shape implements Drawable, Serializable {
    private static final long serialVersionUID = 1L;
    protected String shapeColor;

    public Shape(String shapeColor) {
        this.shapeColor = shapeColor;
    }

    public String getShapeColor() {
        return shapeColor;
    }

    public abstract double calcArea();

    public String toString() {
        return "Колір фігури: " + shapeColor;
    }
}

class Rectangle extends Shape {
    private double a, b;

    public Rectangle(String color, double a, double b) {
        super(color);
        this.a = a;
        this.b = b;
    }

    public double calcArea() {
        return a * b;
    }

    public void draw() {
        System.out.println(this);
    }

    public String toString() {
        return "Прямокутник (колір: " + shapeColor +
                ", площа: " + calcArea() + ")";
    }
}

class Triangle extends Shape {
    private double base, height;

    public Triangle(String color, double base, double height) {
        super(color);
        this.base = base;
        this.height = height;
    }

    public double calcArea() {
        return 0.5 * base * height;
    }

    public void draw() {
        System.out.println(this);
    }

    public String toString() {
        return "Трикутник (колір: " + shapeColor +
                ", площа: " + calcArea() + ")";
    }
}

class Circle extends Shape {
    private double radius;

    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    public double calcArea() {
        return Math.PI * radius * radius;
    }

    public void draw() {
        System.out.println(this);
    }

    public String toString() {
        return "Коло (колір: " + shapeColor +
                ", площа: " + calcArea() + ")";
    }
}

class Model {
    private Shape[] shapes;

    public Model(Shape[] shapes) {
        this.shapes = shapes;
    }

    public Shape[] getShapes() {
        return shapes;
    }

    public void setShapes(Shape[] shapes) {
        this.shapes = shapes;
    }

    public double getTotalArea() {
        double sum = 0;
        for (Shape shape : shapes) {
            sum += shape.calcArea();
        }
        return sum;
    }

    public double getAreaByType(String typeName) {
        double sum = 0;
        for (Shape shape : shapes) {
            if (shape.getClass().getSimpleName().equalsIgnoreCase(typeName)) {
                sum += shape.calcArea();
            }
        }
        return sum;
    }

    public void sortShapesByArea() {
        Arrays.sort(shapes, Comparator.comparingDouble(Shape::calcArea));
    }

    public void sortShapesByColor() {
        Arrays.sort(shapes, Comparator.comparing(Shape::getShapeColor));
    }
}

class View {
    public void printShapes(Shape[] shapes) {
        for (Shape shape : shapes) {
            shape.draw();
        }
        System.out.println();
    }

    public void printText(String text) {
        System.out.println(text);
    }
}

class Controller {
    private Model model;
    private View view;
    private Scanner scanner = new Scanner(System.in);

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    private String input(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    public void run() {
        while (true) {
            System.out.println("\n--- МЕНЮ ---");
            System.out.println("1. Показати фігури");
            System.out.println("2. Сумарна площа");
            System.out.println("3. Площа фігур заданого виду");
            System.out.println("4. Сортувати за площею");
            System.out.println("5. Сортувати за кольором");
            System.out.println("6. Зберегти фігури у файл");
            System.out.println("7. Завантажити фігури з файлу");
            System.out.println("8. Рядок з найбільшою кількістю слів");
            System.out.println("9. Зашифрувати файл");
            System.out.println("10. Розшифрувати файл");
            System.out.println("11. Підрахувати HTML-теги");
            System.out.println("0. Вихід");

            String choice = input("Вибір: ");

            try {
                switch (choice) {
                    case "1":
                        view.printShapes(model.getShapes());
                        break;
                    case "2":
                        view.printText("Сумарна площа: " + model.getTotalArea());
                        break;
                    case "3":
                        String type = input("Тип (Circle, Triangle, Rectangle): ");
                        view.printText("Площа: " + model.getAreaByType(type));
                        break;
                    case "4":
                        model.sortShapesByArea();
                        view.printShapes(model.getShapes());
                        break;
                    case "5":
                        model.sortShapesByColor();
                        view.printShapes(model.getShapes());
                        break;
                    case "6":
                        FileManager.saveShapes(
                                input("Шлях для збереження: "), model.getShapes());
                        view.printText("Фігури збережено");
                        break;
                    case "7":
                        model.setShapes(FileManager.loadShapes(
                                input("Шлях до файлу: ")));
                        view.printText("Фігури завантажено");
                        view.printShapes(model.getShapes());
                        break;
                    case "8":
                        longestLine();
                        break;
                    case "9":
                        crypt(true);
                        break;
                    case "10":
                        crypt(false);
                        break;
                    case "11":
                        countTags();
                        break;
                    case "0":
                        return;
                    default:
                        view.printText("Невірний пункт меню");
                }
            } catch (Exception e) {
                view.printText("Помилка: " + e.getMessage());
            }
        }
    }

    private void longestLine() throws IOException {
        String filename = input("Шлях до текстового файлу: ");
        String longest = "";
        int maxWords = 0;

        try (BufferedReader reader = Files.newBufferedReader(
                Path.of(filename), StandardCharsets.UTF_8)) {
            String line;

            while ((line = reader.readLine()) != null) {
                String text = line.trim();
                int count = text.isEmpty() ? 0 : text.split("\\s+").length;

                if (count > maxWords) {
                    maxWords = count;
                    longest = line;
                }
            }
        }

        System.out.println("Найбільша кількість слів: " + maxWords);
        System.out.println("Рядок: " + longest);
    }

    private void crypt(boolean encrypt) throws IOException {
        String source = input("Вхідний файл: ");
        String target = input("Вихідний файл: ");
        String key = input("Ключовий символ: ");

        if (key.length() != 1) {
            throw new IllegalArgumentException("Введіть один символ");
        }

        if (Path.of(source).toAbsolutePath().normalize().equals(
                Path.of(target).toAbsolutePath().normalize())) {
            throw new IllegalArgumentException("Файли мають бути різними");
        }

        int shift = key.charAt(0);

        if (encrypt) {
            try (Reader reader = Files.newBufferedReader(
                    Path.of(source), StandardCharsets.UTF_8);
                 Writer writer = new ShiftWriter(
                         Files.newBufferedWriter(Path.of(target),
                                 StandardCharsets.UTF_8), shift)) {
                copy(reader, writer);
            }
        } else {
            try (Reader reader = new ShiftReader(
                    Files.newBufferedReader(Path.of(source),
                            StandardCharsets.UTF_8), shift);
                 Writer writer = Files.newBufferedWriter(
                         Path.of(target), StandardCharsets.UTF_8)) {
                copy(reader, writer);
            }
        }

        System.out.println("Операцію виконано");
    }

    private void copy(Reader reader, Writer writer) throws IOException {
        char[] buffer = new char[1024];
        int count;

        while ((count = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, count);
        }
    }

    private void countTags() throws Exception {
        String url = input("Введіть URL: ");
        Map<String, Integer> tags = new TreeMap<>();

        Pattern pattern = Pattern.compile(
                "<\\s*([a-zA-Z][a-zA-Z0-9:-]*)\\b[^>]*>");

        var connection = URI.create(url).toURL().openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(),
                        StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {
                    String tag = matcher.group(1).toLowerCase(Locale.ROOT);
                    tags.put(tag, tags.getOrDefault(tag, 0) + 1);
                }
            }
        }

        System.out.println("\nЗа назвою:");
        for (String tag : tags.keySet()) {
            System.out.println(tag + ": " + tags.get(tag));
        }

        System.out.println("\nЗа частотою:");
        tags.entrySet().stream()
                .sorted(Comparator
                        .comparingInt((Map.Entry<String, Integer> e) -> e.getValue())
                        .thenComparing(Map.Entry::getKey))
                .forEach(e -> System.out.println(
                        e.getKey() + ": " + e.getValue()));
    }
}

class ShiftWriter extends FilterWriter {
    private int shift;

    public ShiftWriter(Writer writer, int shift) {
        super(writer);
        this.shift = shift;
    }

    public void write(int c) throws IOException {
        super.write((char) (c + shift));
    }

    public void write(char[] chars, int offset, int length) throws IOException {
        for (int i = offset; i < offset + length; i++) {
            write(chars[i]);
        }
    }

    public void write(String str, int offset, int length) throws IOException {
        write(str.toCharArray(), offset, length);
    }
}

class ShiftReader extends FilterReader {
    private int shift;

    public ShiftReader(Reader reader, int shift) {
        super(reader);
        this.shift = shift;
    }

    public int read() throws IOException {
        int c = super.read();
        return c == -1 ? -1 : (char) (c - shift);
    }

    public int read(char[] chars, int offset, int length) throws IOException {
        int count = super.read(chars, offset, length);

        for (int i = offset; i < offset + Math.max(count, 0); i++) {
            chars[i] = (char) (chars[i] - shift);
        }

        return count;
    }
}

public class Main {
    public static void main(String[] args) {
        Shape[] shapes = {
                new Rectangle("Red", 4, 5),
                new Circle("Blue", 3),
                new Triangle("Green", 3, 4),
                new Rectangle("Yellow", 2, 2),
                new Circle("Red", 5),
                new Triangle("Blue", 4, 5),
                new Rectangle("Green", 3, 6),
                new Circle("Yellow", 2),
                new Triangle("Red", 5, 5),
                new Rectangle("Blue", 1, 10)
        };

        Model model = new Model(shapes);
        View view = new View();
        Controller controller = new Controller(model, view);
        controller.run();
    }
}
