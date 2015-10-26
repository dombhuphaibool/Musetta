package com.bandonleon.musetta.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bandonleon.musetta.NavigationManager;
import com.bandonleon.musetta.NavigationManager.NavigationPage;
import com.bandonleon.musetta.R;
import com.bandonleon.musetta.fragment.IntervalsFragment;
import com.bandonleon.musetta.fragment.NavigationPageFragment;

public class HomeActivity extends NavigationFlowActivity {

    private DrawerLayout mDrawer;
    private NavigationPageFragment mDefaultFragment;

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

                getNavigationManager().onActionButtonClicked(view);
            }
        });
        //

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(getNavigationManager());

        mDefaultFragment = IntervalsFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, mDefaultFragment, mDefaultFragment.getFragmentTag());
        ft.commit();
    }

    @Override
    public NavigationPage getRootPage() {
        return mDefaultFragment;
    }

    @Override
    public DrawerLayout getDrawer() {
        return mDrawer;
    }

    @Override
    public @IdRes int getContainerViewId() {
        return R.id.fragment_container;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
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
}
