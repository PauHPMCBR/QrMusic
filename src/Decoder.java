import java.io.*;
import java.util.*;
import javax.sound.midi.*; // package for all midi classes
public class Decoder {

    public static String data;
    public static int fromASCIItoInt(char c) {
        return (int)c;
    }
    public static int readPos(int pos) {
        return fromASCIItoInt(data.charAt(pos));
    }

    public static int timeToTick(int time) {
        //float v = (float)time;
        //v /= 16f;
        //return (int)(v*48);
        return (int)(time*1.5f);
    }

    public static float getTypeDuration(int n) {
        if (n == 0) return 0; //corxera amb punt
        if (n == 1) return 0.5f; //corxera
        if (n == 2) return 0.333f; //treset
        if (n == 3) return 0.25f; //semicorxera
        return 0.125f;
    }

    public static int durationToTick(int duration) {
        int notFull = duration % 5;
        float negres = (float)duration / 5f;
        negres += getTypeDuration(notFull);
        return (int)(negres*24);
    }

    public static void decode(String fileName) throws IOException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        data = scanner.nextLine();
        scanner.close();
        try
        {
//****  Create a new MIDI sequence with 24 ticks per beat  ****
            Sequence s = new Sequence(javax.sound.midi.Sequence.PPQ,24);

//****  Obtain a MIDI track from the sequence  ****
            Track t = s.createTrack();

//****  General MIDI sysex -- turn on General MIDI sound set  ****
            byte[] b = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
            SysexMessage sm = new SysexMessage();
            sm.setMessage(b, 6);
            MidiEvent me = new MidiEvent(sm,(long)0);
            t.add(me);

//TODO ****  set tempo (meta event)  ****
            MetaMessage mt = new MetaMessage();
            int bpm = readPos(2);
            int tempo = 60000000 / bpm;
            //tempo *= 2;
            byte[] bt = new byte[] { 0, 0, 0 };

            for (int i = 0; i < 3; i++) {
                int shift = (3 - 1 - i) * 8;
                bt[i] = (byte) (tempo >> shift);
            }
            mt.setMessage(0x51 ,bt, 3);
            me = new MidiEvent(mt,(long)0);
            t.add(me);


//****  set track name (meta event)  ****
            mt = new MetaMessage();
            String TrackName = new String("midifile track");
            mt.setMessage(0x03 ,TrackName.getBytes(), TrackName.length());
            me = new MidiEvent(mt,(long)0);
            t.add(me);

//****  set omni on  ****
            ShortMessage mm = new ShortMessage();
            mm.setMessage(0xB0, 0x7D,0x00);
            me = new MidiEvent(mm,(long)0);
            t.add(me);

//****  set poly on  ****
            mm = new ShortMessage();
            mm.setMessage(0xB0, 0x7F,0x00);
            me = new MidiEvent(mm,(long)0);
            t.add(me);

//TODO ****  set instrument to Piano  ****
            /*mm = new ShortMessage();
            mm.setMessage(0xC0, (byte)readPos(3), 0x00);
            me = new MidiEvent(mm,(long)0);
            t.add(me);*/

/*TODO ****  note on - middle C  ****
            mm = new ShortMessage();
            mm.setMessage(0x90,0x3C,0x60);
            me = new MidiEvent(mm,(long)1);
            t.add(me);

//TODO ****  note off - middle C - 120 ticks later  ****
            mm = new ShortMessage();
            mm.setMessage(0x80,0x3C,0x40);
            me = new MidiEvent(mm,(long)121);
            t.add(me); */

            int i = 4;
            int time = 0;
            int prevTime = 0;
            while (i < data.length()) {
                //int difference = time-prevTime;
                //if (Math.abs(difference-200) < 10) time = prevTime+200;
                System.out.println(time);
                prevTime = time;
                if (readPos(i) < 128) { //it's a note
                    int pitch = readPos(i);
                    ++i;
                    time += timeToTick(readPos(i));
                    ++i;
                    int volume = readPos(i);
                    ++i;
                    int duration = volume%50;
                    volume /= 50;
                    duration = durationToTick(duration);

                    mm = new ShortMessage();
                    mm.setMessage(0x90, (byte)pitch, (byte)volume*30);
                    me = new MidiEvent(mm,(long)time);
                    t.add(me);

                    mm = new ShortMessage();
                    mm.setMessage(0x80, (byte)pitch, 0x40);
                    me = new MidiEvent(mm, (long)time+duration);
                    t.add(me);
                }
                else if (readPos(i) == 128) { //instrument change
                    ++i;
                    time += timeToTick(readPos(i));
                    ++i;
                    int instrument = readPos(i);
                    ++i;
                    mm = new ShortMessage();
                    mm.setMessage(0xC0, (byte)instrument, 0x00);
                    me = new MidiEvent(mm,(long)time);
                    t.add(me);
                }
                else if (readPos(i) == 129) { //tempo change
                    ++i;
                    time += timeToTick(readPos(i));
                    ++i;
                    bpm = readPos(i);
                    ++i;
                    /*tempo = 60000000 / bpm;
                    one = (byte) ((byte)tempo%16);
                    tempo /= 16;
                    two = (byte) ((byte)tempo%16);
                    tempo /= 16;
                    tre = (byte) ((byte)tempo%16);
                    bt = new byte[]{tre, two, one};
                    mt.setMessage(0x51 ,bt, 3);
                    me = new MidiEvent(mt,(long)time);
                    t.add(me);*/
                }
                else if (readPos(i) == 130) { //time signature change
                    ++i;
                    ++i;
                    ++i;
                    ++i;
                    //ups nothing
                }
            }

//TODO ****  set end of track (meta event) 19 ticks later  ****
            mt = new MetaMessage();
            byte[] bet = {}; // empty array
            mt.setMessage(0x2F,bet,0);
            me = new MidiEvent(mt, (long)140);
            t.add(me);

//****  write the MIDI sequence to a MIDI file  ****
            File f = new File("midifile.mid");
            MidiSystem.write(s,1,f);
        } //try
        catch(Exception e)
        {
            System.out.println("Exception caught " + e.toString());
        } //catch
        System.out.println("midifile end ");
    } //main
} //midifile