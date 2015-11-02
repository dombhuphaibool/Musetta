package com.bandonleon.musetta.navigation;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dom on 11/1/15.
 */
public class HostableFlows extends HashSet<Class<? extends NavigationFlow>> {
    public HostableFlows addFlows(Class<? extends NavigationFlow>... flows) {
        for (Class<? extends NavigationFlow> flow : flows) {
            add(flow);
        }
        return this;
    }
}
