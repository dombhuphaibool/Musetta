package com.bandonleon.musetta.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;

import com.bandonleon.musetta.R;
import com.bandonleon.musetta.navigation.NavigationPage;

/**
 * Created by dom on 11/1/15.
 */
public class AboutActivity extends NavigationFlowActivity {

    public static Intent getIntent(Context context) {
        return new Intent(context, AboutActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
    }

    @Override
    public @IdRes
    int getContainerViewId() {
        return R.id.fragment_container;
    }

    @Override
    protected NavigationPage getBasePage() {
        return null;
    }
}
