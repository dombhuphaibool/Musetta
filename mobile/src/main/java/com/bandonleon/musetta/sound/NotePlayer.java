package com.bandonleon.musetta.sound;

import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.bandonleon.musetta.music.Note;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dombhuphaibool on 10/23/15.
 */
public class NotePlayer {
    private static final int MAX_STREAMS = 5;

    public interface SoundLoadListener {
        void onProgressUpdate(int percentage);
        void onLoadCompleted();
    }

    private NoteAssetManager mAssetManager;
    private Map<String, NoteAsset> mNoteAssets;
    private SoundPool mSoundPool;

    public NotePlayer() {
        mAssetManager = new NoteAssetManager();
        mNoteAssets = new HashMap<>();
        for (int octave = 2; octave < 5; ++octave) {
            addNoteSounds(Note.naturals(), octave);
            addNoteSounds(Note.sharps(), octave);
            addNoteSounds(Note.flats(), octave);
        }
        mSoundPool = createSoundPool();
    }

    private void addNoteSounds(Note[] notes, int octave) {
        for (Note note : notes) {
            NoteAsset noteAsset = mAssetManager.createNoteAsset(note, octave);
            mNoteAssets.put(noteAsset.getName(), noteAsset);
        }
    }

    public void loadSoundAssets(AssetManager assetManager, final SoundLoadListener listener) {
        SoundLoaderTask loaderTask = new SoundLoaderTask(assetManager, mSoundPool);
        loaderTask.setOnProgressUpdateListener(new SoundLoaderTask.OnProgressUpdateListener() {
            @Override
            public void onProgressUpdate(int percentageCompleted) {
                listener.onProgressUpdate(percentageCompleted);
            }
        });
        loaderTask.setOnCompletedListener(new SoundLoaderTask.OnCompletedListener() {
            @Override
            public void onCompleted() {
                listener.onLoadCompleted();
            }
        });
        loaderTask.execute(mNoteAssets.values());
    }

    public void play(Note note, int octave) {
        String noteSound = mAssetManager.getNoteSound(note, octave);
        mSoundPool.play(mNoteAssets.get(noteSound).getSoundId(), 1.0f, 1.0f, 0, 0, 1.0f);
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
