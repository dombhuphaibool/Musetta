package com.bandonleon.musetta.sound;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import com.bandonleon.musetta.music.Note;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dombhuphaibool on 10/23/15.
 */
public class NotePlayer {
    private static final int MAX_STREAMS = 5;

    private static final String BASE_SOUND_FILEPATH = "mp3/Piano.ff.";
    private static final String SOUND_EXTENSION = ".mp3";
    private static final int INVALID_SOUND_ID = -1;

    private static class NoteAsset {
        private final String path;
        private int soundId;
        // private AssetFileDescriptor afd;

        public NoteAsset(String path) {
            this.path = path;
            soundId = INVALID_SOUND_ID;
            // afd = null;
        }
    }

    private SoundPool mSoundPool;
    private Map<String, NoteAsset> mNotePaths;
    private Map<Integer, String> mPitchToNoteNameMap;

    public NotePlayer() {
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

        mNotePaths = new HashMap<>();
        for (int octave = 2; octave < 5; ++octave) {
            addNoteSounds(Note.naturals(), octave);
            addNoteSounds(Note.sharps(), octave);
            addNoteSounds(Note.flats(), octave);
        }
        mSoundPool = createSoundPool();
    }

    // Method to generate a String key for a Note and octave combination
    private String getNoteSound(Note note, int octave) {
        return mPitchToNoteNameMap.get(note.getPitch()) + octave;
    }

    private void addNoteSounds(Note[] notes, int octave) {
        for (Note note : notes) {
            String noteSound = getNoteSound(note, octave);
            String filepath = BASE_SOUND_FILEPATH + noteSound + SOUND_EXTENSION;
            mNotePaths.put(noteSound, new NoteAsset(filepath));
        }
    }

    public void loadSoundAssets(AssetManager assetManager) {
        for (NoteAsset noteAsset : mNotePaths.values()) {
            try {
                AssetFileDescriptor afd = assetManager.openFd(noteAsset.path);
                noteAsset.soundId = mSoundPool.load(afd, 1);
            } catch (IOException ex) {
                Log.e("Dom", "Unable to open asset " + noteAsset.path);
            }
        }
    }

    public void play(Note note, int octave) {
        String noteSound = getNoteSound(note, octave);
        mSoundPool.play(mNotePaths.get(noteSound).soundId, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    @SuppressWarnings("Deprecated")
    private SoundPool createSoundPool() {
        SoundPool soundPool = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setMaxStreams(MAX_STREAMS)
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
                    .build();
        } else {
            soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        return soundPool;
    }


}
