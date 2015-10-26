package com.bandonleon.musetta.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bandonleon.musetta.MusettaApplication;
import com.bandonleon.musetta.NavigationManager;

/**
 * Created by dombhuphaibool on 10/24/15.
 */
public abstract class NavigationFlowActivity extends AppCompatActivity
        implements NavigationManager.NavigationFlow {

    private NavigationManager mNavigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNavigationManager = ((MusettaApplication) getApplication()).getNavigationManager();
        mNavigationManager.registerFlow(this);
    }

    @Override
    protected void onDestroy() {
        mNavigationManager.unregisterFlow(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!mNavigationManager.onBackPressed()) {
            super.onBackPressed();
        }
    }

    protected NavigationManager getNavigationManager() {
        return mNavigationManager;
    }
}
