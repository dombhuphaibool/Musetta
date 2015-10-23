package com.bandonleon.musetta.music;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by dombhuphaibool on 10/22/15.
 */
public class IntervalGenerator extends RandomGenerator<Interval> {

    public IntervalGenerator() {
        super(Interval.class);
    }

    public IntervalGenerator includeCommonIntervals(boolean include) {
        invalidateAvailable();
        includeItems(include, Interval.commonValues());
        return this;
    }

    public IntervalGenerator includeUnison(boolean include) {
        invalidateAvailable();
        includeItem(include, Interval.Unison);
        return this;
    }

    public IntervalGenerator includeOctave(boolean include) {
        invalidateAvailable();
        includeItem(include, Interval.Octave);
        return this;
    }
}
