package com.bandonleon.musetta.navigation;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by dom on 11/1/15.
 */
public interface NavigationPage {
    Fragment getFragment();
    String getFragmentTag();
    boolean isTransient();
    void handleActionButtonClicked(View view);
}
