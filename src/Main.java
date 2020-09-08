import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, IOException {

        ReadMIDI.loadSequence("dos negres.mid");
        ReadMIDI.createTracks();
        //ReadMIDI.printEvents(ReadMIDI.tracks[0]);
        ReadMIDI.playSequence();
        printEvents.print();
    }
}
