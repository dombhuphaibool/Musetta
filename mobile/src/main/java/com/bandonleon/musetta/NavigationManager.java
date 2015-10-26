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

import com.bandonleon.musetta.fragment.Intervals2Fragment;
import com.bandonleon.musetta.fragment.IntervalsFragment;
import com.bandonleon.musetta.fragment.NavigationPageFragment;
import com.bandonleon.musetta.fragment.PlaceholderFragment;

import java.util.ArrayDeque;

/**
 * Created by dombhuphaibool on 10/24/15.
 *
 * This is an experimental class constructed to handle complex fragment navigations.
 * Some example of this is when popping the current fragment will skip x previous fragments.
 * This is cumbersome using the Android backstack. Let's experiment with a custom NavigationManager.
 *
 * The terminology here is that Flows are equivalent to Android Activities and
 * Pages are equivalent to Android Fragments.
 */
public class NavigationManager implements NavigationView.OnNavigationItemSelectedListener {

    public interface NavigationFlow {
        DrawerLayout getDrawer();
        @IdRes int getContainerViewId();
        AppCompatActivity getActivity();
        NavigationPage getRootPage();
    }

    public interface NavigationPage {
        Fragment getFragment();
        String getFragmentTag();
        void handleActionButtonClicked(View view);
    }

    private static class Stack<T> extends ArrayDeque<T> {
        public void push(T item) {
            super.addFirst(item);
        }

        public T pop() {
            return super.removeFirst();
        }

        public T peek() {
            return super.peekFirst();
        }
    }

    private static class NavigationFlowStackItem {
        private final NavigationFlow mFlow;
        private Stack<NavigationPage> mPages;

        public NavigationFlowStackItem(NavigationFlow flow) {
            mFlow = flow;
            mPages = new Stack<>();
        }

        public NavigationFlow getFlow() {
            return mFlow;
        }

        public void pushPage(NavigationPage page) {
            mPages.push(page);
        }

        public NavigationPage popPage() {
            return mPages.pop();
        }

        public NavigationPage peekPage() {
            return mPages.peek();
        }

        public int getNumPagesOnStack() {
            return mPages.size();
        }
    }

    private Stack<NavigationFlowStackItem> mFlows;

    public NavigationManager() {
        mFlows = new Stack<>();
    }

    public void registerFlow(NavigationFlow flow) {
        NavigationFlowStackItem flowItem = new NavigationFlowStackItem(flow);
        flowItem.pushPage(flow.getRootPage());
        mFlows.push(flowItem);
    }

    public void unregisterFlow(NavigationFlow flow) {
        NavigationFlowStackItem poppedFlowItem = mFlows.pop();
        if (poppedFlowItem.getFlow() != flow) {
            throw new IllegalStateException("Attempting to pop a flow that is not on top of the stack");
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        NavigationFlowStackItem currentFlowItem = mFlows.peek();
        if (currentFlowItem == null) {
            throw new IllegalStateException("onNavigationItemSelected() called but there's no flow on the stack");
        }
        FragmentManager currentFM = getCurrentFragmentManager();
        if (currentFM != null) {
            FragmentTransaction ft = currentFM.beginTransaction();
            NavigationPageFragment fragment = null;
            switch (item.getItemId()) {
                case R.id.nav_intervals_note:
                    fragment = IntervalsFragment.newInstance();
                    break;

                case R.id.nav_intervals_detection:
                    fragment = Intervals2Fragment.newInstance();
                    break;

                case R.id.nav_melodic_dictation:
                    fragment = PlaceholderFragment.newInstance("Who loves you baby boo?");
                    break;

                case R.id.nav_chords:
                    fragment = PlaceholderFragment.newInstance("Your bhu bhu loves you! =)");
                    break;

                case R.id.nav_progressions:
                    fragment = PlaceholderFragment.newInstance("Very, very much!!!");
                    break;

                case R.id.nav_share:

                    break;

                case R.id.nav_send:

                    break;

                default:

                    break;
            }
            if (fragment != null) {
                ft.replace(getCurrentContainerViewId(), fragment, fragment.getFragmentTag());
                currentFlowItem.pushPage(fragment);
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
        NavigationFlowStackItem currentFlowItem = mFlows.peek();
        if (currentFlowItem != null) {
            NavigationPage currentPage = currentFlowItem.peekPage();
            if (currentPage != null) {
                currentPage.handleActionButtonClicked(view);
            }
        }
    }

    public boolean onBackPressed() {
        boolean handled = false;
        DrawerLayout currentDrawer = getCurrentDrawer();
        if (currentDrawer != null && currentDrawer.isDrawerOpen(GravityCompat.START)) {
            currentDrawer.closeDrawer(GravityCompat.START);
            handled = true;
        } else {
            NavigationFlowStackItem currentFlowItem = mFlows.peek();
            if (currentFlowItem != null) {
                if (currentFlowItem.getNumPagesOnStack() > 1) {
                    // There is one or more flow on the stack, and the current flow has more
                    // than one pages on the stack. Therefore, just pop the current page's
                    // fragment and replace it with the one underneath it.
                    currentFlowItem.popPage();
                    NavigationPage page = currentFlowItem.peekPage();
                    NavigationFlow currentFlow = currentFlowItem.getFlow();
                    FragmentManager fm = currentFlow.getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(currentFlow.getContainerViewId(), page.getFragment(), page.getFragmentTag());
                    ft.commit();
                    handled = true;
                }
                // Note: Activity.onStop() should pop the current flow off of the flow stack
            }
        }
        return handled;
    }

    /*
     * Helper methodds to return NavigationFlow interface methods for current flow
     */
    private DrawerLayout getCurrentDrawer() {
        DrawerLayout currentDrawer = null;
        NavigationFlowStackItem currentFlowItem = mFlows.peek();
        if (currentFlowItem != null) {
            currentDrawer = currentFlowItem.getFlow().getDrawer();
        }
        return currentDrawer;
    }

    private @IdRes int getCurrentContainerViewId() {
        int currentContainerViewId = 0;
        NavigationFlowStackItem currentFlowItem = mFlows.peek();
        if (currentFlowItem != null) {
            currentContainerViewId = currentFlowItem.getFlow().getContainerViewId();
        }
        return currentContainerViewId;
    }

    private FragmentManager getCurrentFragmentManager() {
        FragmentManager currentFM = null;
        NavigationFlowStackItem currentFlowItem = mFlows.peek();
        if (currentFlowItem != null) {
            currentFM = currentFlowItem.getFlow().getActivity().getSupportFragmentManager();
        }
        return currentFM;
    }
}
