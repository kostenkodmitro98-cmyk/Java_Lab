import java.io.*;

public class FileManager {
    public static void saveShapes(String filename, Shape[] shapes)
            throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            output.writeObject(shapes);
        }
    }

    public static Shape[] loadShapes(String filename)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(filename))) {
            return (Shape[]) input.readObject();
        }
    }
}
