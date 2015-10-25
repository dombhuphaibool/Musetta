package com.bandonleon.musetta;

import android.app.Application;

import com.bandonleon.musetta.event.SoundAssetLoadedEvent;
import com.bandonleon.musetta.event.SoundAssetLoadedEvent.State;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

/**
 * Created by dombhuphaibool on 10/24/15.
 */
public class MusettaApplication extends Application {

    private Bus mEventBus;
    private NavigationManager mNavigationManager;
    private State mSoundAssetLoadState;

    @Override
    public void onCreate() {
        super.onCreate();

        mEventBus = new Bus();
        mEventBus.register(this);

        mNavigationManager = new NavigationManager();

        // @TODO: Replace with AsyncTask
        mSoundAssetLoadState = State.LoadedSuccessfully;
    }

    public Bus getEventBus() {
        return mEventBus;
    }

    public NavigationManager getNavigationManager() {
        return mNavigationManager;
    }

    private void onSoundAssetLoaded() {
        mEventBus.post(new SoundAssetLoadedEvent(mSoundAssetLoadState));
    }

    @Produce
    public SoundAssetLoadedEvent produceSoundAssetLoadedEvent() {
        return (new SoundAssetLoadedEvent(mSoundAssetLoadState));
    }
}
