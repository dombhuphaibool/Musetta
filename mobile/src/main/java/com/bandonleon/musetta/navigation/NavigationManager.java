package com.bandonleon.musetta.navigation;

import android.content.Intent;
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

import com.bandonleon.musetta.R;
import com.bandonleon.musetta.fragment.Intervals2Fragment;
import com.bandonleon.musetta.fragment.IntervalsFragment;
import com.bandonleon.musetta.fragment.NavigationPageFragment;
import com.bandonleon.musetta.fragment.PlaceholderFragment;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

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
public class NavigationManager {

    private static class NavigationPageDescriptor {
        private final HostableFlows hostableFlows;
        private final NavigationPageInstantiator pageInstantiator;

        public NavigationPageDescriptor(HostableFlows hostableFlows,
                                        NavigationPageInstantiator pageInstantiator) {
            this.hostableFlows = hostableFlows;
            this.pageInstantiator = pageInstantiator;
        }
    }

    private Map<Integer, NavigationPageDescriptor> mPages;
    private Map<Integer, Intent> mFlows;
    private NavigationFlow mCurrentFlow;

    public NavigationManager() {
        mPages = new HashMap<>();
        mFlows = new HashMap<>();
    }

    /**
     *
     * @param id - Unique id of a flow
     * @param intent - An intent to start an activity for the flow
     * @return boolean - Returns true if there was already a previously registered flow that
     *      we are overriding
     */
    public boolean registerFlow(int id, Intent intent) {
        return mFlows.put(id, intent) != null;
    }

    /**
     *
     * @param id - Unique id of the flow to deregister
     * @return boolean - Returns true if the flow was found or false if the requested flow to
     *      deregister was not found
     */
    public boolean deregisterFlow(int id) {
        return mFlows.remove(id) != null;
    }

    public void activateFlow(NavigationFlow flow) {
        mCurrentFlow = flow;
    }

    public void deactivateFlow(NavigationFlow flow) {
        if (mCurrentFlow != flow) {
            throw new IllegalStateException("Attempting to deactivate a flow that is not current");
        }
        mCurrentFlow = null;
    }

    public boolean navigateToFlow(int id) {
        if (mCurrentFlow == null) {
            throw new IllegalStateException("Cannot navigate to a new flow when there are no flow currently active");
        }
        Intent flowIntent = mFlows.get(id);
        if (flowIntent != null) {
            mCurrentFlow.getActivity().startActivity(flowIntent);
        }
        return flowIntent != null;
    }

    public void registerPage(int id, HostableFlows hostableFlows, NavigationPageInstantiator pageInstantiator) {
        mPages.put(id, new NavigationPageDescriptor(hostableFlows, pageInstantiator));
    }

    public void deregisterPage(int id) {
        mPages.remove(id);
    }

    public boolean navigateToPage(int id) {
        boolean handled = false;
        if (mCurrentFlow == null) {
            throw new IllegalStateException("Cannot navigate to a new page when there are no flow currently active");
        }
        NavigationPageDescriptor pageDescriptor = mPages.get(id);
        if (pageDescriptor != null) {
            if (!pageDescriptor.hostableFlows.contains(mCurrentFlow.getClass())) {
                throw new IllegalStateException("Requested page is not permitted in the current flow");
            }
            mCurrentFlow.navigateToPage(pageDescriptor.pageInstantiator.instantiate());
            handled = true;
        }
        return handled;
    }
}
