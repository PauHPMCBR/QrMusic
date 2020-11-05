public class Note implements Comparable<Note>{
    int timeStart = -1;
    int timeEnd = -1;
    int pitch = -1;
    int volume = -1;

    public Note(int timeStart, int pitch, int volume) {
        this.timeStart = timeStart;
        this.pitch = pitch;
        this.volume = volume;
    }

    public void setTimeEnd(int timeEnd) {
        this.timeEnd = timeEnd;
    }

    public CompressedEvent compress() {
        int time = CompressedFile.processedTime(timeStart);
        int duration = CompressedFile.processedDuration(timeEnd-timeStart);
        int newVolume = CompressedFile.processedVolume(volume);
        if (duration > 49) duration = 49;
        return new CompressedEvent(pitch, time, newVolume*50+duration);
    }

    @Override
    public int compareTo(Note o) {
        int comparisonOutcome = Integer.compare(o.timeStart, this.timeStart);
        if (comparisonOutcome != 1 && comparisonOutcome != -1) {
            comparisonOutcome = Integer.compare(o.timeEnd, this.timeEnd);
        }
        return comparisonOutcome*-1;
    }
}
