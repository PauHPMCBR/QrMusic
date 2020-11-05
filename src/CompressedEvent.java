public class CompressedEvent implements Comparable<CompressedEvent> {
    int pitch;
    int time;
    int durationAndVolume;
    int additionalSpace;
    String type;

    public CompressedEvent(int pitch, int time, int durationAndVolume) {
        this.pitch = pitch;
        this.time = time;
        this.durationAndVolume = durationAndVolume;
        this.type = "note";
    }
    public CompressedEvent(String type, int pitch, int time, int durationAndVolume) {
        this.pitch = pitch;
        this.time = time;
        this.durationAndVolume = durationAndVolume;
        this.type = type;
    }
    public CompressedEvent(String type, int pitch, int time, int durationAndVolume, int additionalSpace) {
        this.pitch = pitch;
        this.time = time;
        this.durationAndVolume = durationAndVolume;
        this.additionalSpace = additionalSpace;
        this.type = type;
    }

    public void print() {
        System.out.println("Type: " + type + "  Pitch: " + pitch + "  Time: " + time + "  Duration+Volume: " + durationAndVolume);
    }

    @Override
    public int compareTo(CompressedEvent o) {
        return Integer.compare(o.time, this.time)*-1;
    }
}
