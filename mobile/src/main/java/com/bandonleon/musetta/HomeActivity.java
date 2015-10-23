package com.bandonleon.musetta;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.bandonleon.musetta.music.Interval;
import com.bandonleon.musetta.music.IntervalGenerator;
import com.bandonleon.musetta.music.Note;
import com.bandonleon.musetta.music.NoteGenerator;
import com.bandonleon.musetta.sound.NotePlayer;
import com.bandonleon.musetta.view.ConcentricView;
import com.bandonleon.musetta.view.MusicConcentricView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Improve your musicianship!!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */
                mCurrentInterval = mIntervalGenerator.random();
                mIntervalTxt.setText(mCurrentInterval.getName());

                mCurrentNote = mNoteGenerator.random();
                mNoteSelector.setCurrentNote(mCurrentNote);
            }
        });
        //

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
    }

    private NotePlayer mNotePlayer;
    private MusicConcentricView mNoteSelector;
    private TextView mIntervalTxt;
    private TextView mFeedbackTxt;
    private NoteGenerator mNoteGenerator;
    private IntervalGenerator mIntervalGenerator;
    private Note mCurrentNote = Note.D;
    private Interval mCurrentInterval;
    private void init() {
        mNoteSelector = (MusicConcentricView) findViewById(R.id.note_selector);
        mIntervalTxt = (TextView) findViewById(R.id.interval_txt);
        mFeedbackTxt = (TextView) findViewById(R.id.feedback_txt);

        mNotePlayer = new NotePlayer();
        mNotePlayer.loadSoundAssets(getAssets());

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // @TODO...
            return true;
        } else if (id == R.id.action_about) {
            // @TODO...
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_intervals_note) {

        } else if (id == R.id.nav_intervals_detection) {

        } else if (id == R.id.nav_melodic_dictation) {

        } else if (id == R.id.nav_chords) {

        } else if (id == R.id.nav_progressions) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
