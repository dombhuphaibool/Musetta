package com.bandonleon.musetta.sound;

import com.bandonleon.musetta.music.Note;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dombhuphaibool on 10/25/15.
 *
 * This class manages the mapping of note pitches (in Note.java) to sound files
 * in the assets folder. All knowledge of sound files in the assets folder should
 * solely reside here.
 */
public class NoteAssetManager {
    private static final String BASE_SOUND_FILEPATH = "mp3/Piano.ff.";
    private static final String SOUND_EXTENSION = ".mp3";

    private Map<Integer, String> mPitchToNoteNameMap;

    public NoteAssetManager() {
        mPitchToNoteNameMap = new HashMap<>();
        mPitchToNoteNameMap.put(0, "C");
        mPitchToNoteNameMap.put(1, "Db");
        mPitchToNoteNameMap.put(2, "D");
        mPitchToNoteNameMap.put(3, "Eb");
        mPitchToNoteNameMap.put(4, "E");
        mPitchToNoteNameMap.put(5, "F");
        mPitchToNoteNameMap.put(6, "Gb");
        mPitchToNoteNameMap.put(7, "G");
        mPitchToNoteNameMap.put(8, "Ab");
        mPitchToNoteNameMap.put(9, "A");
        mPitchToNoteNameMap.put(10, "Bb");
        mPitchToNoteNameMap.put(11, "B");
    }

    // Method to generate a String key for a Note and octave combination
    public String getNoteSound(Note note, int octave) {
        return mPitchToNoteNameMap.get(note.getPitch()) + octave;
    }

    public NoteAsset createNoteAsset(Note note, int octave) {
        String soundName = getNoteSound(note, octave);
        String filepath = BASE_SOUND_FILEPATH + soundName + SOUND_EXTENSION;
        return new NoteAsset(soundName, filepath);
    }
}
