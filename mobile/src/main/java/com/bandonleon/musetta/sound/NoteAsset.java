package com.bandonleon.musetta.sound;

/**
 * Created by dombhuphaibool on 10/25/15.
 */
public class NoteAsset {
    public static final int INVALID_SOUND_ID = -1;

    private final String mName;
    private final String mPath;
    private int mSoundId;

    public NoteAsset(String name, String path) {
        mName = name;
        mPath = path;
        mSoundId = INVALID_SOUND_ID;
    }

    public String getName() {
        return mName;
    }

    public String getPath() {
        return mPath;
    }

    public int getSoundId() {
        return mSoundId;
    }

    public void setSoundId(int soundId) {
        mSoundId = soundId;
    }
}