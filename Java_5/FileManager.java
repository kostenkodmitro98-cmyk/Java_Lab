import java.io.*;
import java.util.*;

public class FileManager {
    public static void saveBooks(String filename, List<Book> books) throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filename))) {
            output.writeObject(books);
        }
    }

    public static List<Book> loadBooks(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename))) {
            Object data = input.readObject();

            if (!(data instanceof List<?> list)) {
                throw new IOException("Неправильний формат файлу");
            }

            List<Book> books = new ArrayList<>();

            for (Object item : list) {
                if (!(item instanceof Book book)) {
                    throw new IOException("Неправильний тип об'єкта");
                }

                books.add(book);
            }

            return books;
        }
    }
}
