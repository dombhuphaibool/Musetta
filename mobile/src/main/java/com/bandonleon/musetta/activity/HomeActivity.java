package com.bandonleon.musetta.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bandonleon.musetta.R;
import com.bandonleon.musetta.fragment.IntervalsFragment;
import com.bandonleon.musetta.navigation.NavigationPage;

public class HomeActivity extends NavigationFlowActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Intent getIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    private DrawerLayout mDrawer;
    private NavigationPage mBasePage;

    @Override
    protected NavigationPage getBasePage() {
        return mBasePage;
    }

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

                onActionButtonClicked(view);
            }
        });
        //

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mBasePage = IntervalsFragment.createInstantiator().instantiate();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, mBasePage.getFragment(), mBasePage.getFragmentTag());
        ft.commit();
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
        return getNavigationManager().navigateToFlow(id) ? true : super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        boolean handled = getNavigationManager().navigateToPage(item.getItemId());
        if (mDrawer != null) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
        return handled;
    }
}
