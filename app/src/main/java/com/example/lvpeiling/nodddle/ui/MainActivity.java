package com.example.lvpeiling.nodddle.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lvpeiling.nodddle.R;
import com.example.lvpeiling.nodddle.fragment.BucketsFragment;
import com.example.lvpeiling.nodddle.fragment.MainFragment;
import com.example.lvpeiling.nodddle.model.UserVO;
import com.example.lvpeiling.nodddle.network.Api;
import com.example.lvpeiling.nodddle.network.OkHttpClientManager;
import com.example.lvpeiling.nodddle.network.ResultCallBack;

import java.io.IOException;

import butterknife.BindString;
import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    ImageView ivAvatar;
    TextView tvName;
    TextView tvUsername;
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
    private static final String TAG = "MainActivity";
    private UserVO userVO;
    private View headerLayout;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        setDefaultFragment();
        getUserInfo();

    }

    private void setUserInfoView() {
        headerLayout = navView.inflateHeaderView(R.layout.nav_header_main);
        ivAvatar = (ImageView) headerLayout.findViewById(R.id.iv_avatar);
        tvUsername = (TextView) headerLayout.findViewById(R.id.tv_username);
        tvName = (TextView) headerLayout.findViewById(R.id.tv_name);
        if(userVO != null){
            Glide.with(this).load(userVO.getAvatar_url()).diskCacheStrategy(DiskCacheStrategy.ALL).
                    placeholder(R.drawable.avatar_default).error(R.drawable.avatar_default).crossFade().into(ivAvatar);
            tvName.setText(userVO.getName());
            tvUsername.setText(userVO.getUsername());
        }
    }

    private void getUserInfo() {
        OkHttpClientManager.getInstance().getAsync(Api.USER_URL, null, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Object response) {
                Response mResponse = (Response) response;
                try {
                    String responceStr = mResponse.body().string();
                    userVO = JSONObject.parseObject(responceStr, UserVO.class);
                    setUserInfoView();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setDefaultFragment() {
        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, mainFragment);
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

    public void hideAllFragments() {
        if (mainFragment != null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mainFragment);
        }
        if (bucketsFragment != null) {
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
            if (mainFragment == null) {
                mainFragment = new MainFragment();
                fragmentTransaction.add(R.id.content_frame, mainFragment);
                fragmentTransaction.commit();
            } else {
                fragmentTransaction.show(mainFragment).commit();
            }
        } else if (id == R.id.nav_buckets) {
            toolbar.setTitle(bucketsStr);
            hideAllFragments();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (bucketsFragment == null) {
                bucketsFragment = new BucketsFragment();
                fragmentTransaction.add(R.id.content_frame, bucketsFragment);
                fragmentTransaction.commit();
            } else {
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

