package com.bandonleon.musetta.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.widget.DrawerLayout;

import com.bandonleon.musetta.R;
import com.bandonleon.musetta.navigation.NavigationPage;

/**
 * Created by dombhuphaibool on 10/28/15.
 */
public class SettingsActivity extends NavigationFlowActivity {

    public static Intent getIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
    }

    @Override
    public @IdRes int getContainerViewId() {
        return R.id.fragment_container;
    }

    @Override
    protected NavigationPage getBasePage() {
        return null;
    }
}
