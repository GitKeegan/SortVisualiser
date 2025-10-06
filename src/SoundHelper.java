import javax.sound.midi.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SoundHelper{
    private static Synthesizer synth;
    private static final int MIN_NOTE = 40; // Low E
    private static final int MAX_NOTE = 80; // High G
    private static final int MIN_VAL = 0;   // Assumed min rectangle value
    private static final int MAX_VAL = 500; // Assumed max rectangle value

    // Use a ScheduledExecutorService for the brief delay to turn the note off
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    static {
        try {
            // Get the default synthesizer
            synth = MidiSystem.getSynthesizer();
            synth.open();
        } catch (MidiUnavailableException e) {
            System.err.println("MIDI unavailable. Cannot play sound.");
            synth = null;
        }
    }

    /**
     * Converts a rectangle value to a MIDI note number (40-80).
     */
    private static int valueToNote(int value) {
        // Clamp the value to the expected range
        value = Math.max(MIN_VAL, Math.min(MAX_VAL, value));

        // Linear mapping formula
        double normalizedValue = (double) (value - MIN_VAL) / (MAX_VAL - MIN_VAL);
        return (int) (MIN_NOTE + normalizedValue * (MAX_NOTE - MIN_NOTE));
    }

    /**
     * Plays a tone of a given pitch (value) and duration (ms) using MIDI.
     * This method is NON-BLOCKING.
     * @param value The value of the rectangle (determines pitch).
     * @param durationMs The duration of the tone in milliseconds.
     */
    public static void playTone(int value, int durationMs) {
        if (synth == null) return;

        try {
            Receiver receiver = synth.getReceiver();
            int note = valueToNote(value);

            // 1. Note On: Send the signal to start playing the note
            ShortMessage noteOn = new ShortMessage();
            noteOn.setMessage(ShortMessage.NOTE_ON, 0, note, 90); // Channel 0, Note, Velocity 90
            receiver.send(noteOn, -1);

            // 2. Schedule Note Off: Send the signal to stop the note after the duration
            scheduler.schedule(() -> {
                try {
                    ShortMessage noteOff = new ShortMessage();
                    noteOff.setMessage(ShortMessage.NOTE_OFF, 0, note, 90);
                    receiver.send(noteOff, -1);
                } catch (InvalidMidiDataException e) {
                    e.printStackTrace();
                }
            }, durationMs, TimeUnit.MILLISECONDS);

        } catch (InvalidMidiDataException | MidiUnavailableException e) {
            e.printStackTrace();
        }
    }
}