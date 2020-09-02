import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.Scanner;
import java.io.FileNotFoundException;

public class ReadFile {

    public static void createFile(File outputFile) {
        try {
            if (outputFile.createNewFile()) {
                System.out.println("File created: " + outputFile.getName());
            } else {
                System.out.println("File already exists. Resetting file...");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    private static String asciiToHex(String asciiStr) {
        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString((int) ch));
        }

        return hex.toString();
    }

    private static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }

    public static String decToHex(int dec) {
        return Integer.toHexString(dec);
    }

    public static void main(String[] args) throws IOException, FileNotFoundException {
        File outputFile = new File("output.txt");
        FileWriter myWriter = new FileWriter("output.txt");
        File inputFile = new File("input.txt");
        Scanner myReader = new Scanner(inputFile);
        createFile(outputFile);

        System.out.println("Type 1 if you want to convert numbers (in hex) to text or 2 if you want the other way");
        Scanner scan = new Scanner(System.in);
        String action = scan.nextLine();
        if (action.equals("1")) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                myWriter.write(hexToAscii(data));
                myWriter.write("\n");
            }
        }
        else if (action.equals("2")) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                myWriter.write(asciiToHex(data));
                myWriter.write("\n");
            }
        }


        myWriter.close();
        myReader.close();
    }
}