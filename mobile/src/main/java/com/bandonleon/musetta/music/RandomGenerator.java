package com.bandonleon.musetta.music;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by dombhuphaibool on 10/22/15.
 */
public abstract class RandomGenerator<T> {
    private Class<T> mItemClass;
    private Random mRandom;
    private Set<T> mIncluded;
    private T[] mAvailable;

    public RandomGenerator(Class<T> itemClass) {
        mItemClass = itemClass;
        mRandom = new Random();
        mIncluded = new HashSet<>();
        mAvailable = null;
    }

    protected void invalidateAvailable() {
        mAvailable = null;
    }

    protected void includeItem(boolean include, T item) {
        if (include) {
            mIncluded.add(item);
        } else {
            mIncluded.remove(item);
        }
    }

    protected void includeItems(boolean include, T[] items) {
        for (T item : items) {
            if (include) {
                mIncluded.add(item);
            } else {
                mIncluded.remove(item);
            }
        }
    }

    public T random() {
        if (mAvailable == null) {
            // mAvailable = mIncluded.toArray(new T[mIncluded.size()]);
            mAvailable = mIncluded.toArray((T[]) Array.newInstance(mItemClass, mIncluded.size()));
        }
        return mAvailable[mRandom.nextInt(mAvailable.length)];
    }
}
