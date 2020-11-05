import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class EventManager {
    public static Note[] queuedNotes = new Note[1024];
    public static PriorityQueue<Note> Notes = new PriorityQueue<Note>();
    public static PriorityQueue<CompressedEvent> CmpEvents = new PriorityQueue<CompressedEvent>();
    public static ArrayList<int[]> startup = new ArrayList<>();
    public static int startingInstrument = 1;
    public static int startingTempo = 120;
    public static int TS_numerator = 4;
    public static int TS_denominator = 4;

    public static void addNote(int[] element) {
        Note prov = new Note(element[0], element[1], element[2]);
        if (queuedNotes[prov.pitch] != null) {
            if (prov.volume == 0) {
                queuedNotes[prov.pitch].setTimeEnd(prov.timeStart);
                Notes.add(queuedNotes[prov.pitch]);
                queuedNotes[prov.pitch] = null;
            } else {
                queuedNotes[prov.pitch].setTimeEnd(prov.timeStart - 1);
                Notes.add(queuedNotes[prov.pitch]);
                queuedNotes[prov.pitch] = prov;
            }
        } else {
            if (prov.volume > 0) {
                queuedNotes[prov.pitch] = prov;
            }

        }
    }

    public static void addElement(int[] element) {
        if (startup.contains(element)) return;
        startup.add(element);
    }

    public static void setInstrument(int tick, int instrument) {
        if (tick == 0) startingInstrument = instrument;
        else addElement(new int[]{128, tick, instrument});
    }

    public static void setTempo(int tick, int tempo) {
        if (tick == 0) startingTempo = tempo;
        else addElement(new int[]{129, tick, tempo});
    }

    public static void setTimeSignature(int tick, int numerator, int denominator) {
        if (tick == 0) {
            TS_numerator = numerator;
            TS_denominator = denominator;
        }
        else addElement(new int[]{130, tick, numerator, denominator});
    }

    public static void processEvents() {
        while (!Notes.isEmpty()) {
            Note prov = Notes.poll();
            if (prov.timeStart == -1) continue;
            CmpEvents.add(prov.compress());
        }
        for (int[] i : startup) {
            if (i.length == 4) CmpEvents.add(new CompressedEvent("TS", i[0], CompressedFile.processedTime(i[1]), i[2], i[3]));
            else CmpEvents.add(new CompressedEvent("special", i[0], CompressedFile.processedTime(i[1]), i[2]));
        }
    }


    /*public static void printElements() {
        System.out.println("Starting instrument: " + startingInstrument);
        System.out.println("Starting tempo: " + startingTempo);
        System.out.println("Starting time signature: " + TS_numerator + '/' + TS_denominator);

        while (!Notes.isEmpty()) {
            Note prov = Notes.poll();
            if (prov.timeStart == -1) continue;
            System.out.println(prov.timeStart + " " + prov.timeEnd + " " + prov.pitch + " " + prov.volume);
        }
        for (int[] i : startup) System.out.println(Arrays.toString(i));
    }*/
}
