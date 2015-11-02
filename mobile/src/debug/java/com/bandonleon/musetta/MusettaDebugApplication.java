package com.bandonleon.musetta;

import com.bandonleon.musetta.activity.DebugHomeActivity;
import com.bandonleon.musetta.activity.HomeActivity;

/**
 * Created by dombhuphaibool on 10/25/15.
 */
public class MusettaDebugApplication extends MusettaApplication {
    protected Class<? extends HomeActivity> getHomeActivityClass() {
        return DebugHomeActivity.class;
    }
}
