import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CompressedFile {

    public static char intToASCII(int value) {
        return (char)value;
    }

    public static int processedTime(int value) {
        float v = (float)value/456f;
        int negres = (int)v;
        v -= negres;
        v *= 16;
        int nonFull = (int)v;
        return nonFull + 16*negres;
    }

    public static int findTypeDuration(float n) {
        if (n - (int)n < 0.01) return 0; //no i a
        if (n >= 0.45f) return 1; //corxera
        if (n >= 0.3f) return 2; //treset
        if (n >= 0.2) return 3; //semicorxera
        return 4; //fusa
    }

    public static int processedDuration(int value) {
        //corxera, semi, fusa, treset, res
        float v = (float)value/456f;
        int negres = (int)v;
        v -= negres;
        return findTypeDuration(v) + 5*negres;
    }

    public static int processedVolume(int value) {
        //volumes: (120, 90, 60, 30, 0)
        if (value > 110) return 4;
        if (value > 80) return 3;
        if (value > 50) return 2;
        if (value > 20) return 1;
        return 0;
    }

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

    public static void writeEvent(CompressedEvent e, int time) throws IOException {
        if (e.type.equals("TS")) {
            fileWriter.write(intToASCII(e.pitch));
            fileWriter.write(intToASCII(time));
            fileWriter.write(intToASCII(e.durationAndVolume));
            fileWriter.write(intToASCII(e.additionalSpace));
        }
        else {
            fileWriter.write(intToASCII(e.pitch));
            fileWriter.write(intToASCII(time));
            fileWriter.write(intToASCII(e.durationAndVolume));
        }
    }

    public static void writeStart() throws IOException {
        fileWriter.write(intToASCII(EventManager.TS_numerator));
        fileWriter.write(intToASCII(EventManager.TS_denominator));
        fileWriter.write(intToASCII(EventManager.startingTempo));
        fileWriter.write(intToASCII(EventManager.startingInstrument));
    }

    public static void writeEvents() throws IOException {
        writeStart();
        int currentTime = 0;
        while (!EventManager.CmpEvents.isEmpty()) {
            CompressedEvent temp = EventManager.CmpEvents.poll();
            temp.print();
            writeEvent(temp, temp.time-currentTime);
            currentTime = temp.time;
        }
    }

}
