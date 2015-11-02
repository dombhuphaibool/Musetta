package com.bandonleon.musetta;

import android.app.Activity;
import android.app.Application;

import com.bandonleon.musetta.activity.AboutActivity;
import com.bandonleon.musetta.activity.HomeActivity;
import com.bandonleon.musetta.activity.SettingsActivity;
import com.bandonleon.musetta.event.SoundAssetLoadedEvent;
import com.bandonleon.musetta.fragment.Intervals2Fragment;
import com.bandonleon.musetta.fragment.IntervalsFragment;
import com.bandonleon.musetta.fragment.PlaceholderFragment;
import com.bandonleon.musetta.navigation.HostableFlows;
import com.bandonleon.musetta.navigation.NavigationFlow;
import com.bandonleon.musetta.navigation.NavigationManager;
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
        registerFlows(mNavigationManager);
        registerPages(mNavigationManager);

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

    private void registerFlows(NavigationManager navigationManager) {
        navigationManager.registerFlow(0, HomeActivity.getIntent(this));
        navigationManager.registerFlow(R.id.action_settings, SettingsActivity.getIntent(this));
        navigationManager.registerFlow(R.id.action_about, AboutActivity.getIntent(this));
    }

    private void registerPages(NavigationManager navigationManager) {
        navigationManager.registerPage(R.id.nav_intervals_note,
                new HostableFlows().addFlows(getHomeActivityClass()),
                IntervalsFragment.createInstantiator());
        navigationManager.registerPage(R.id.nav_intervals_detection,
                new HostableFlows().addFlows(getHomeActivityClass()),
                Intervals2Fragment.createInstantiator());
        navigationManager.registerPage(R.id.nav_melodic_dictation,
                new HostableFlows().addFlows(getHomeActivityClass()),
                PlaceholderFragment.createInstantiator("Who loves you baby boo?"));
        navigationManager.registerPage(R.id.nav_chords,
                new HostableFlows().addFlows(getHomeActivityClass()),
                PlaceholderFragment.createInstantiator("Your bhu bhu loves you! =)"));
        navigationManager.registerPage(R.id.nav_progressions,
                new HostableFlows().addFlows(getHomeActivityClass()),
                PlaceholderFragment.createInstantiator("Yes, he does y con mucho cariños <3"));

        // R.id.nav_share
        // R.id.nav_send
    }

    protected Class<? extends HomeActivity> getHomeActivityClass() {
        return HomeActivity.class;
    }
}
