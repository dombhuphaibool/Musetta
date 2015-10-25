package com.bandonleon.musetta.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bandonleon.musetta.R;
import com.bandonleon.musetta.event.SoundAssetLoadedEvent;
import com.bandonleon.musetta.music.Interval;
import com.bandonleon.musetta.music.IntervalGenerator;
import com.bandonleon.musetta.music.Note;
import com.bandonleon.musetta.music.NoteGenerator;
import com.bandonleon.musetta.sound.NotePlayer;
import com.bandonleon.musetta.view.ConcentricView;
import com.bandonleon.musetta.view.MusicConcentricView;
import com.squareup.otto.Subscribe;

/**
 * Created by dombhuphaibool on 10/24/15.
 */
public class IntervalsFragment extends NavigationPageFragment {
    private static final String TAG = "IntervalsFragment";

    private MusicConcentricView mNoteSelector;
    private TextView mIntervalTxt;
    private TextView mFeedbackTxt;

    private NotePlayer mNotePlayer;
    private NoteGenerator mNoteGenerator;
    private IntervalGenerator mIntervalGenerator;

    private Note mCurrentNote = Note.D;
    private Interval mCurrentInterval;

    public static IntervalsFragment newInstance() {
        return new IntervalsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_intervals_note, container, false);
        mNoteSelector = (MusicConcentricView) rootView.findViewById(R.id.note_selector);
        mIntervalTxt = (TextView) rootView.findViewById(R.id.interval_txt);
        mFeedbackTxt = (TextView) rootView.findViewById(R.id.feedback_txt);
        init();
        return rootView;
    }

    private void init() {

        mNotePlayer = new NotePlayer();
        mNotePlayer.loadSoundAssets(getContext().getAssets());

        mNoteGenerator = new NoteGenerator();
        mNoteGenerator.includeNaturals(true);
        mNoteGenerator.includeSharps(true);
        mNoteGenerator.includeFlats(true);

        mIntervalGenerator = new IntervalGenerator();
        mIntervalGenerator.includeCommonIntervals(true);
        mIntervalGenerator.includeUnison(false);
        mIntervalGenerator.includeOctave(false);

        mCurrentInterval = mIntervalGenerator.random();
        mIntervalTxt.setText(mCurrentInterval.getName());

        if (mNoteSelector != null) {
            mNoteSelector.setOnSectionTouchListner(new ConcentricView.OnSectionTouchListener() {
                @Override
                public boolean onSectionTouched(int section) {
                    // Toast.makeText(MainActivity.this, "Section touched: " + section, Toast.LENGTH_SHORT).show();
                    Log.e("Dom", "Section touched: " + section);
                    return true;
                }
            });

            mNoteSelector.setOnNoteSelectedListener(new MusicConcentricView.OnNoteSelectedListener() {
                @Override
                public void onNoteSelected(Note note) {
                    if (note != mCurrentNote) {
                        String feedback = mCurrentNote.getName() + " -> " + note.getName() + " : ";
                        Interval selectedInterval = note.intervalFrom(mCurrentNote);
                        feedback += selectedInterval == Interval.Invalid ?
                                selectedInterval.getName() + " Interval" :
                                selectedInterval.getName();
                        mFeedbackTxt.setText(feedback);
                        Log.e("Dom", "Note selected: " + note.getName() + ", interval is " + selectedInterval.getName());
                    }
                    // @TODO: Do haptic feedback & play sound
                    // PlaySound(note)
                    mNoteSelector.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    mNotePlayer.play(note, 3);
                }
            });
        }
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public void handleActionButtonClicked(View view) {
        mCurrentInterval = mIntervalGenerator.random();
        mIntervalTxt.setText(mCurrentInterval.getName());

        mCurrentNote = mNoteGenerator.random();
        mNoteSelector.setCurrentNote(mCurrentNote);
    }

    @Subscribe
    public void onSoundAssetLoaded(SoundAssetLoadedEvent event) {
        // @TODO: Hide the loading text view...
        // Don't forget to regist the Otto event bus
    }
}
