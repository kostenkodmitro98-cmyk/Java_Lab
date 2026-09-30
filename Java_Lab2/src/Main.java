import com.google.gson.Gson;
import java.util.Objects;

// Крок 1. Клас Person з полями
class Person {
    private String lastName;
    private String firstName;
    private int age;

    public Person(String lastName, String firstName, int age) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.age = age;
    }

    // Реалізація методу equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
                Objects.equals(lastName, person.lastName) &&
                Objects.equals(firstName, person.firstName);
    }

    // Завжди перевизначаємо hashCode разом з equals
    @Override
    public int hashCode() {
        return Objects.hash(lastName, firstName, age);
    }

    @Override
    public String toString() {
        return "Person{lastName='" + lastName + "', firstName='" + firstName + "', age=" + age + "}";
    }
}

public class Main {
    public static void main(String[] args) {
        // Крок 2.a: Створюємо екземпляр Person
        Person originalPerson = new Person("Костенко", "Дмитро", 19);
        System.out.println("Початковий об'єкт: " + originalPerson);

        // Створюємо об'єкт Gson
        Gson gson = new Gson();

        // Крок 2.b: Конвертуємо в JSON (Серіалізація)
        String jsonString = gson.toJson(originalPerson);
        System.out.println("JSON рядок: " + jsonString);

        // Крок 2.c: Конвертуємо назад в об'єкт (Десеріалізація)
        Person restoredPerson = gson.fromJson(jsonString, Person.class);
        System.out.println("Відновлений об'єкт: " + restoredPerson);

        // Крок 2.d: Перевіряємо equals-ом початковий і одержаний об'єкти
        boolean isEqual = originalPerson.equals(restoredPerson);
        System.out.println("Чи рівні об'єкти (equals)? -> " + isEqual);
    }
}