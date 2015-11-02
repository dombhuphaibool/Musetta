package com.bandonleon.musetta.navigation;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by dom on 11/1/15.
 */
public interface NavigationFlow {
    Activity getActivity();
    void navigateToPage(NavigationPage page);
}
