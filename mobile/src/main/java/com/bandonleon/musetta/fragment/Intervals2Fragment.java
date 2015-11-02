package com.bandonleon.musetta.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bandonleon.musetta.navigation.NavigationPage;
import com.bandonleon.musetta.navigation.NavigationPageInstantiator;

/**
 * Created by dombhuphaibool on 10/25/15.
 */
public class Intervals2Fragment extends IntervalsFragment {

    public static NavigationPageInstantiator createInstantiator() {
        return new NavigationPageInstantiator() {
            @Override
            public NavigationPage instantiate() {
                return new Intervals2Fragment();
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        getNoteSelector().setNoteColorStyle(2);
        return rootView;
    }
}
