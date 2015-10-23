package com.bandonleon.musetta.music;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by dombhuphaibool on 10/22/15.
 */
public class NoteGenerator extends RandomGenerator<Note> {
    /*
    private Random mRandom;
    private Set<Note> mIncludedNotes;
    private Note[] mNotes;

    public NoteGenerator() {
        mRandom = new Random();
        mIncludedNotes = new HashSet<>();
        mNotes = null;
    }
    */

    public NoteGenerator() {
        super(Note.class);
    }

    /*
    private NoteGenerator includeNotes(boolean include, Note[] notes) {
        for (Note note : notes) {
            if (include) {
                mIncludedNotes.add(note);
            } else {
                mIncludedNotes.remove(note);
            }
        }
        return this;
    }

    public NoteGenerator includeNaturals(boolean include) {
        mNotes = null;
        return includeNotes(include, Note.naturals());
    }

    public NoteGenerator includeSharps(boolean include) {
        mNotes = null;
        return includeNotes(include, Note.sharps());
    }

    public NoteGenerator includeFlats(boolean include) {
        mNotes = null;
        return includeNotes(include, Note.flats());
    }

    public NoteGenerator includeDoubleSharps(boolean include) {
        mNotes = null;
        return includeNotes(include, Note.doubleSharps());
    }

    public NoteGenerator includeDoubleFlats(boolean include) {
        mNotes = null;
        return includeNotes(include, Note.doubleFlats());
    }

    public Note random() {
        if (mNotes == null) {
            mNotes = mIncludedNotes.toArray(new Note[mIncludedNotes.size()]);
        }

        return mNotes[mRandom.nextInt(mNotes.length)];
    }
    */

    public NoteGenerator includeNaturals(boolean include) {
        invalidateAvailable();
        includeItems(include, Note.naturals());
        return this;
    }

    public NoteGenerator includeSharps(boolean include) {
        invalidateAvailable();
        includeItems(include, Note.sharps());
        return this;
    }

    public NoteGenerator includeFlats(boolean include) {
        invalidateAvailable();
        includeItems(include, Note.flats());
        return this;
    }

    public NoteGenerator includeDoubleSharps(boolean include) {
        invalidateAvailable();
        includeItems(include, Note.doubleSharps());
        return this;
    }

    public NoteGenerator includeDoubleFlats(boolean include) {
        invalidateAvailable();
        includeItems(include, Note.doubleFlats());
        return this;
    }
}
