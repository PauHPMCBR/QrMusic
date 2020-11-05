import javax.sound.midi.*;

public class ReadEvents {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final int SET_INSTRUMENT = 0xC0;
    public static final int SET_TEMPO = 0x51;
    public static final int TIME_SIGNATURE = 0x58;

    static void read() {
        Sequence sequence = ReadMIDI.sequence;
        int trackNumber = 0;
        for (Track track : sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == NOTE_ON) {
                        int key = sm.getData1();
                        int velocity = sm.getData2();
                        EventManager.addNote(new int[]{(int) event.getTick(), key, velocity});
                    }
                    else if (sm.getCommand() == NOTE_OFF) {
                        int key = sm.getData1();
                        EventManager.addNote(new int[]{(int) event.getTick(), key, 0});
                    }
                    else if (sm.getCommand() == SET_INSTRUMENT) {
                        int instrument = sm.getData1();
                        EventManager.setInstrument((int) event.getTick(), instrument);
                    }
                    else {
                        System.out.println("Command:" + sm.getCommand() + "  entire message:" + sm);
                    }
                } else if (message instanceof MetaMessage) {
                    MetaMessage mt = (MetaMessage) message;
                    if (mt.getType() == SET_TEMPO) {
                        byte[] bt = mt.getData();
                        int tempo = (bt[0] & 0xff) << 16 | (bt[1] & 0xff) << 8 | (bt[2] & 0xff);
                        System.out.println("TEMPO: " + tempo);
                        int bpm = 60000000 / tempo;
                        EventManager.setTempo((int) event.getTick(), bpm);
                    }
                    else if (mt.getType() == TIME_SIGNATURE) {
                        byte[] bt = mt.getData();
                        EventManager.setTimeSignature((int)event.getTick(), bt[0], (int)Math.pow(2, bt[1]));
                    }
                }
                else {
                    System.out.println("Other message: " + message.getClass() + "  entire message:" + message);
                }
            }

            System.out.println();
        }

    }
}
