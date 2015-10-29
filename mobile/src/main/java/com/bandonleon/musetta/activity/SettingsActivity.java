package com.bandonleon.musetta.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.widget.DrawerLayout;

import com.bandonleon.musetta.R;

/**
 * Created by dombhuphaibool on 10/28/15.
 */
public class SettingsActivity extends NavigationFlowActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
    }

    @Override
    public DrawerLayout getDrawer() {
        return null;
    }

    @Override
    public @IdRes int getContainerViewId() {
        return R.id.fragment_container;
    }
}
