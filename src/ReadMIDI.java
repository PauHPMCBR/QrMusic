import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class ReadMIDI {
    public static Sequence sequence;
    public static Track[] tracks;
    public static Sequencer sequencer;

    //loads the midi and creates the sequence and tracks
    public static void loadSequence(String fileName) throws IOException, InvalidMidiDataException {
        sequence = MidiSystem.getSequence(new File(fileName));
        tracks = sequence.getTracks();
    }

    //not necessary, plays the midi
    public static void playSequence() throws InvalidMidiDataException, MidiUnavailableException {
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setSequence(sequence);
        sequencer.start();
    }

}
