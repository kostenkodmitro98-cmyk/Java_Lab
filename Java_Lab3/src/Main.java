import java.util.Arrays;
import java.util.Comparator;

// Інтерфейс за завданням
interface Drawable {
    void draw();
}

// Абстрактний клас фігури
abstract class Shape implements Drawable {
    protected String shapeColor; // поле для кольору

    public Shape(String shapeColor) {
        this.shapeColor = shapeColor;
    }

    public String getShapeColor() {
        return shapeColor;
    }

    // Абстрактний метод обчислення площі
    public abstract double calcArea();

    @Override
    public String toString() {
        return "Колір фігури: " + shapeColor;
    }
}

// Клас Прямокутник
class Rectangle extends Shape {
    private double a;
    private double b;

    public Rectangle(String color, double a, double b) {
        super(color);
        this.a = a;
        this.b = b;
    }

    @Override
    public double calcArea() {
        return a * b;
    }

    @Override
    public void draw() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "Прямокутник (колір: " + shapeColor + ", площа: " + calcArea() + ")";
    }
}

// Клас Трикутник
class Triangle extends Shape {
    private double base;
    private double height;

    public Triangle(String color, double base, double height) {
        super(color);
        this.base = base;
        this.height = height;
    }

    @Override
    public double calcArea() {
        return 0.5 * base * height;
    }

    @Override
    public void draw() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "Трикутник (колір: " + shapeColor + ", площа: " + calcArea() + ")";
    }
}

// Клас Коло
class Circle extends Shape {
    private double radius;

    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public double calcArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public void draw() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "Коло (колір: " + shapeColor + ", площа: " + calcArea() + ")";
    }
}


// === ПАТЕРН MVC ===

// Клас Model: зберігає дані та виконує розрахунки
class Model {
    private Shape[] shapes;

    public Model(Shape[] shapes) {
        this.shapes = shapes;
    }

    public Shape[] getShapes() {
        return shapes;
    }

    // Сумарна площа всіх фігур
    public double getTotalArea() {
        double sum = 0;
        for (int i = 0; i < shapes.length; i++) {
            sum += shapes[i].calcArea();
        }
        return sum;
    }

    // Сумарна площа фігур певного виду (наприклад, тільки кіл)
    public double getAreaByType(String typeName) {
        double sum = 0;
        for (int i = 0; i < shapes.length; i++) {
            // Перевіряємо назву класу фігури
            if (shapes[i].getClass().getSimpleName().equals(typeName)) {
                sum += shapes[i].calcArea();
            }
        }
        return sum;
    }

    // Впорядкування за площею через Comparator
    public void sortShapesByArea() {
        Arrays.sort(shapes, new Comparator<Shape>() {
            @Override
            public int compare(Shape s1, Shape s2) {
                return Double.compare(s1.calcArea(), s2.calcArea());
            }
        });
    }

    // Впорядкування за кольором через Comparator
    public void sortShapesByColor() {
        Arrays.sort(shapes, new Comparator<Shape>() {
            @Override
            public int compare(Shape s1, Shape s2) {
                return s1.getShapeColor().compareTo(s2.getShapeColor());
            }
        });
    }
}

// Клас View: відповідає лише за виведення на екран
class View {
    public void printShapes(Shape[] shapes) {
        for (int i = 0; i < shapes.length; i++) {
            shapes[i].draw();
        }
        System.out.println();
    }

    public void printText(String text) {
        System.out.println(text);
    }
}

// Клас Controller: зв'язує Model та View
class Controller {
    private Model model;
    private View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void run() {
        view.printText("--- Початковий масив фігур ---");
        view.printShapes(model.getShapes());

        view.printText("Сумарна площа всіх фігур: " + model.getTotalArea());
        view.printText("Сумарна площа всіх кіл: " + model.getAreaByType("Circle"));
        view.printText("");

        view.printText("--- Сортування масиву за площею (від меншої до більшої) ---");
        model.sortShapesByArea();
        view.printShapes(model.getShapes());

        view.printText("--- Сортування масиву за кольором ---");
        model.sortShapesByColor();
        view.printShapes(model.getShapes());
    }
}

// === ГОЛОВНИЙ КЛАС ===
public class Main {
    public static void main(String[] args) {
        // Створюємо масив на 10 елементів (завдання вимагає не менше 10)
        Shape[] myShapes = {
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

        // Створюємо об'єкти для MVC
        Model model = new Model(myShapes);
        View view = new View();
        Controller controller = new Controller(model, view);

        // Запускаємо програму
        controller.run();
    }
}