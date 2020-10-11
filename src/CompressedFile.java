import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class CompressedFile {
    public static File file;
    public static FileWriter fileWriter;
    public static void createCompressedFile(String name) throws IOException {
        name = name+".cmi";
        file = new File(name);
        file.createNewFile();
        fileWriter = new FileWriter(file);
    }
    public static void createCompressedFile() throws IOException {
        createCompressedFile("output");
    }
    public static void close() throws IOException {
        fileWriter.close();
    }
}
