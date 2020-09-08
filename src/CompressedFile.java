import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CompressedFile {

    public static File outputFile;
    public static FileWriter writer;

    public static void createFile(String name) throws IOException {
        outputFile = new File(name);
        outputFile.createNewFile();
        writer = new FileWriter(name);
    }
    public static void createFile() throws IOException {
        createFile("output.cmid");
    }
    public static char ASCIIcompress(int n) {
        return (char)n;
    }
    public static void writeNote(char c1, char c2, char c3) throws IOException {
        writer.write(new char[]{c1, c2, c3});
    }
}
