package com.bandonleon.musetta.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.bandonleon.musetta.MusettaApplication;
import com.bandonleon.musetta.NavigationManager.NavigationPage;

/**
 * Created by dombhuphaibool on 10/24/15.
 */
public abstract class NavigationPageFragment extends Fragment implements NavigationPage {

    protected MusettaApplication getApplication() {
        MusettaApplication application = null;
        Activity activity = getActivity();
        if (activity != null) {
            application = (MusettaApplication) activity.getApplication();
        }
        return application;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void handleActionButtonClicked(View view) {
        // Nothing to do
    }
}
