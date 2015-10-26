package com.bandonleon.musetta;

import android.app.Application;

import com.bandonleon.musetta.event.SoundAssetLoadedEvent;
import com.bandonleon.musetta.sound.NotePlayer;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

/**
 * Created by dombhuphaibool on 10/24/15.
 */
public class MusettaApplication extends Application {
    // private static MusettaApplication sAppInstance;

    private Bus mEventBus;

    private NavigationManager mNavigationManager;
    private NotePlayer mNotePlayer;
    private SoundAssetLoadedEvent.State mSoundAssetLoadState;

    // Only use this if you absolutely can't get the application in any other form
    // or if the object lifecycle prohibits you from getting the application instance
    // through the normal manner. (example: fragment onAttach() trying to get the
    // event bus to register itself)
    // public static MusettaApplication getInstance() {
    //    return sAppInstance;
    // }

    @Override
    public void onCreate() {
        super.onCreate();

        // sAppInstance = this;

        mEventBus = new Bus();
        mEventBus.register(this);

        mNavigationManager = new NavigationManager();

        mSoundAssetLoadState = SoundAssetLoadedEvent.State.Idle;
        mNotePlayer = new NotePlayer();
        mNotePlayer.loadSoundAssets(getAssets(), new NotePlayer.SoundLoadListener() {
            @Override
            public void onProgressUpdate(int percentage) {
                mSoundAssetLoadState = SoundAssetLoadedEvent.State.Loading;
            }

            @Override
            public void onLoadCompleted() {
                mSoundAssetLoadState = SoundAssetLoadedEvent.State.LoadedSuccessfully;
                onSoundAssetLoaded();
            }
        });
    }

    public Bus getEventBus() {
        return mEventBus;
    }

    public NavigationManager getNavigationManager() {
        return mNavigationManager;
    }

    public NotePlayer getNotePlayer() {
        return mNotePlayer;
    }

    // @TODO: Remove this method!!!
    public boolean isSoundLoaded() {
        return mSoundAssetLoadState == SoundAssetLoadedEvent.State.LoadedSuccessfully;
    }

    private void onSoundAssetLoaded() {
        mEventBus.post(new SoundAssetLoadedEvent(mSoundAssetLoadState));
    }

    @Produce
    public SoundAssetLoadedEvent produceSoundAssetLoadedEvent() {
        return (new SoundAssetLoadedEvent(mSoundAssetLoadState));
    }
}
