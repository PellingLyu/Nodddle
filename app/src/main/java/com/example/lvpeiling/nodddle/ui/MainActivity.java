package com.example.lvpeiling.nodddle.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.lvpeiling.nodddle.R;
import com.example.lvpeiling.nodddle.fragment.BucketsFragment;
import com.example.lvpeiling.nodddle.fragment.MainFragment;

import butterknife.BindString;
import butterknife.BindView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private FragmentTransaction fragmentTransaction;
    private MainFragment mainFragment;
    private BucketsFragment bucketsFragment;
    @BindString(R.string.main)
    String mainStr;
    @BindString(R.string.projects)
    String projectsStr;
    @BindString(R.string.team)
    String teamStr;
    @BindString(R.string.mine)
    String mineStr;
    @BindString(R.string.buckets)
    String bucketsStr;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        setDefaultFragment();
    }

    private void setDefaultFragment() {
        if(mainFragment == null){
            mainFragment = new MainFragment();
        }
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame,mainFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void hideAllFragments(){
        if(mainFragment != null){
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mainFragment);
        }
        if(bucketsFragment!=null){
            fragmentTransaction.hide(bucketsFragment);
        }
        fragmentTransaction.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_shots) {
            toolbar.setTitle(mainStr);
            hideAllFragments();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if(mainFragment == null){
                mainFragment = new MainFragment();
                fragmentTransaction.add(R.id.content_frame,mainFragment);
                fragmentTransaction.commit();
            }else {
                fragmentTransaction.show(mainFragment).commit();
            }
        }else if (id == R.id.nav_buckets){
            toolbar.setTitle(bucketsStr);
            hideAllFragments();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if(bucketsFragment == null){
                bucketsFragment = new BucketsFragment();
                fragmentTransaction.add(R.id.content_frame,bucketsFragment);
                fragmentTransaction.commit();
            }else {
                fragmentTransaction.show(bucketsFragment).commit();
            }

        } else if (id == R.id.nav_projects) {
            toolbar.setTitle(projectsStr);
        } else if (id == R.id.nav_teams) {
            toolbar.setTitle(teamStr);
        } else if (id == R.id.nav_mine) {
            toolbar.setTitle(mineStr);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
