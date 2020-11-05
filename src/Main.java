import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        Scanner scan = new Scanner(System.in);
        System.out.print("0 for encode, 1 for decode: ");
        int option = scan.nextInt();
        scan.nextLine();
        System.out.print("Input the file name: ");
        String name = scan.nextLine();
        if (option == 0) {
            if (!name.contains(".mid")) name = name + ".mid";
            ReadMIDI.loadSequence(name); //loads the midi with that name
            ReadMIDI.playSequence(); //plays the midi

            for (int i = 0; i < 256; ++i) System.out.print(CompressedFile.intToASCII(i));
            System.out.println();

            ReadEvents.read(); //processes the midi file and separates all events

            EventManager.processEvents(); //compresses all elements into a priority queue
            if (name.contains(".mid")) name = name.substring(0, name.length() - 4);
            CompressedFile.createCompressedFile(name);

            CompressedFile.writeEvents();

            CompressedFile.close();
        }
        else{
            if (!name.contains(".cmi")) name = name + ".cmi";
            Decoder.decode(name);
        }
    }
}
