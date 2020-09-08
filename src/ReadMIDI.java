import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.sound.midi.*;

public class ReadMIDI {
    public static Sequence sequence;
    public static Sequencer sequencer;
    public static Track[] tracks;

    public static void loadSequence(String fileName) throws InvalidMidiDataException, IOException {
        sequence = MidiSystem.getSequence(new File(fileName));
    }

    public static void createTracks() {
        tracks = sequence.getTracks();
    }

    public static void printEvents(Track track) {
        for (int i = 11; i < track.size()-1; ++i) {
            MidiEvent event = track.get(i);
            byte[] message = event.getMessage().getMessage();
            System.out.println("pitch: " + message[1] + "  volume: " + message[2]);
        }
    }

    public static void playSequence() throws InvalidMidiDataException, IOException, MidiUnavailableException {
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setSequence(sequence);
        sequencer.start();
    }

}
