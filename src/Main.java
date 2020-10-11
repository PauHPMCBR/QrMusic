import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        String name = "negra 100.mid";
        ReadMIDI.loadSequence(name); //loads the midi with that name
        ReadMIDI.playSequence(); //plays the midi

        ReadEvents.read(); //processes the midi file and separates all events

        EventManager.printElements(); //prints all elements one by one by priority queue
        if (name.contains(".mid")) name = name.substring(0, name.length()-4);
        CompressedFile.createCompressedFile(name);


        CompressedFile.close();
    }
}
