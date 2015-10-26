package com.bandonleon.musetta.sound;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by dombhuphaibool on 10/25/15.
 */
public class SoundLoaderTask extends AsyncTask<Collection<NoteAsset>, Integer, Void> {

    public interface OnProgressUpdateListener {
        void onProgressUpdate(int percentageCompleted);
    }
    public interface OnCompletedListener {
        void onCompleted();
    }

    private AssetManager mAssetManager;
    private SoundPool mSoundPool;
    private OnProgressUpdateListener mProgressListener;
    private OnCompletedListener mCompletedListener;

    public SoundLoaderTask(AssetManager assetManager, SoundPool soundPool) {
        mAssetManager = assetManager;
        mSoundPool = soundPool;
        mProgressListener = null;
        mCompletedListener = null;
    }

    public void setOnProgressUpdateListener(OnProgressUpdateListener listener) {
        mProgressListener = listener;
    }

    public void setOnCompletedListener(OnCompletedListener listener) {
        mCompletedListener = listener;
    }

    @Override
    protected Void doInBackground(Collection<NoteAsset>... noteAssets) {
        int numAssets = 0;
        int numAssetsCompleted = 0;
        int numCollections = noteAssets.length;
        for (int collectionIdx=0; collectionIdx<numCollections; ++collectionIdx) {
            numAssets += noteAssets[collectionIdx].size();
        }
        for (int collectionIdx=0; collectionIdx<numCollections; ++collectionIdx) {
            for (NoteAsset noteAsset : noteAssets[collectionIdx]) {
                try {
                    AssetFileDescriptor afd = mAssetManager.openFd(noteAsset.getPath());
                    noteAsset.setSoundId(mSoundPool.load(afd, 1));
                } catch (IOException ex) {
                    Log.e("Dom", "Unable to open asset " + noteAsset.getPath());
                }
                publishProgress((int) ((++numAssetsCompleted / (float) numAssets) * 100));
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (mProgressListener != null) {
            mProgressListener.onProgressUpdate(values[0]);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mCompletedListener != null) {
            mCompletedListener.onCompleted();
        }
    }
}
