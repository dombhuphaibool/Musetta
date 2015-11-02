package com.bandonleon.musetta.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bandonleon.musetta.MusettaApplication;
import com.bandonleon.musetta.navigation.NavigationFlow;
import com.bandonleon.musetta.navigation.NavigationManager;
import com.bandonleon.musetta.navigation.NavigationPage;
import com.bandonleon.musetta.util.Stack;

/**
 * Created by dombhuphaibool on 10/24/15.
 */
public abstract class NavigationFlowActivity extends AppCompatActivity implements NavigationFlow {

    private NavigationManager mNavigationManager;
    private Stack<NavigationPage> mPages;

    protected abstract @IdRes int getContainerViewId();
    protected abstract NavigationPage getBasePage();

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void navigateToPage(NavigationPage page) {
        mPages.push(page);
        replaceCurrentPage(page);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPages = new Stack<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavigationManager = ((MusettaApplication) getApplication()).getNavigationManager();
        mNavigationManager.activateFlow(this);
    }

    @Override
    protected void onPause() {
        mNavigationManager.deactivateFlow(this);
        mNavigationManager = null;
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        boolean handled = false;
        DrawerLayout drawer = getDrawer();
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            handled = true;
        } else if (!mPages.isEmpty()) {
            NavigationPage page = null;
            do {
                page = mPages.pop();
            } while (!mPages.isEmpty() && page.isTransient());   // @TODO: we can extend this
            // condition to getMode() and check for other modes TRANSIENT, etc...

            if (page.isTransient()) {
                page = getBasePage();
            }
            if (!page.isTransient()) {
                replaceCurrentPage(page);
                handled = true;
            }
        }

        if (!handled) {
            super.onBackPressed();
        }
    }

    protected DrawerLayout getDrawer() {
        return null;
    }

    protected NavigationManager getNavigationManager() {
        return mNavigationManager;
    }

    private void replaceCurrentPage(NavigationPage page) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(getContainerViewId(), page.getFragment(), page.getFragmentTag());
        ft.commit();
    }

    protected void onActionButtonClicked(View view) {
        NavigationPage currentPage = mPages.isEmpty() ? getBasePage() : mPages.peek();
        if (currentPage != null) {
            currentPage.handleActionButtonClicked(view);
        }
    }
}
