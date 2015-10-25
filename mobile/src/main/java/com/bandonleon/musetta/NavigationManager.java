package com.bandonleon.musetta;

import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.bandonleon.musetta.fragment.IntervalsFragment;
import com.bandonleon.musetta.fragment.NavigationPageFragment;

import java.util.ArrayDeque;

/**
 * Created by dombhuphaibool on 10/24/15.
 */
public class NavigationManager implements NavigationView.OnNavigationItemSelectedListener {

    public interface NavigationFlow {
        DrawerLayout getDrawer();
        @IdRes int getContainerViewId();
        AppCompatActivity getActivity();
    }

    public interface NavigationPage {
        String getFragmentTag();
        void handleActionButtonClicked(View view);
    }

    private static class NavigationStack extends ArrayDeque<NavigationFlow> {
        public void push(NavigationFlow flow) {
            super.addFirst(flow);
        }

        public NavigationFlow pop() {
            return super.removeFirst();
        }

        public NavigationFlow peek() {
            return super.peekFirst();
        }
    }

    private NavigationStack mFlows;
    private NavigationPage mCurrentPage;  // @TODO: Clean this up!!!

    public NavigationManager() {
        mFlows = new NavigationStack();
    }

    public void registerFlow(NavigationFlow flow) {
        mFlows.push(flow);
    }

    public void unregisterFlow(NavigationFlow flow) {
        NavigationFlow poppedFlow = mFlows.pop();
        if (poppedFlow != flow) {
            throw new IllegalStateException("Attempting to pop a flow that is not on top of the stack");
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager currentFM = getCurrentFragmentManager();
        if (currentFM != null) {
            FragmentTransaction ft = currentFM.beginTransaction();
            NavigationPageFragment fragment = null;
            switch (item.getItemId()) {
                case R.id.nav_intervals_note:
                    fragment = IntervalsFragment.newInstance();
                    ft.replace(getCurrentContainerViewId(), fragment, fragment.getFragmentTag());
                    mCurrentPage = fragment;
                    break;

                case R.id.nav_intervals_detection:

                    break;

                case R.id.nav_melodic_dictation:

                    break;

                case R.id.nav_chords:

                    break;

                case R.id.nav_progressions:

                    break;

                case R.id.nav_share:

                    break;

                case R.id.nav_send:

                    break;

                default:

                    break;
            }
            ft.commit();
        }

        DrawerLayout currentDrawer = getCurrentDrawer();
        if (currentDrawer != null) {
            currentDrawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    public void onActionButtonClicked(View view) {
        if (mCurrentPage != null) {
            mCurrentPage.handleActionButtonClicked(view);
        }
    }

    public boolean onBackPressed() {
        boolean handled = false;
        DrawerLayout currentDrawer = getCurrentDrawer();
        if (currentDrawer != null && currentDrawer.isDrawerOpen(GravityCompat.START)) {
            currentDrawer.closeDrawer(GravityCompat.START);
            handled = true;
        }
        return handled;
    }

    /*
     * Helper methodds to return NavigationFlow interface methods for current flow
     */
    private DrawerLayout getCurrentDrawer() {
        DrawerLayout currentDrawer = null;
        NavigationFlow currentFlow = mFlows.peek();
        if (currentFlow != null) {
            currentDrawer = currentFlow.getDrawer();
        }
        return currentDrawer;
    }

    private @IdRes int getCurrentContainerViewId() {
        int currentContainerViewId = 0;
        NavigationFlow currentFlow = mFlows.peek();
        if (currentFlow != null) {
            currentContainerViewId = currentFlow.getContainerViewId();
        }
        return currentContainerViewId;
    }

    private FragmentManager getCurrentFragmentManager() {
        FragmentManager currentFM = null;
        NavigationFlow currentFlow = mFlows.peek();
        if (currentFlow != null) {
            currentFM = currentFlow.getActivity().getSupportFragmentManager();
        }
        return currentFM;
    }
}
