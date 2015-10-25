package com.bandonleon.musetta.event;

/**
 * Created by dombhuphaibool on 10/25/15.
 */
public class SoundAssetLoadedEvent {
    public enum State {
        Idle,
        Loading,
        LoadedSuccessfully,
        FailedToLoad
    }

    private State mState;

    public SoundAssetLoadedEvent() {
        this(State.Idle);
    }

    public SoundAssetLoadedEvent(State state) {
        setState(state);
    }

    public void setState(State state) {
        mState = state;
    }

    public State getState() {
        return mState;
    }
}
