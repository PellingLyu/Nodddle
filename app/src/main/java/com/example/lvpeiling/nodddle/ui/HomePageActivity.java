package com.example.lvpeiling.nodddle.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lvpeiling.nodddle.R;
import com.example.lvpeiling.nodddle.adapter.ShotsAdapter;
import com.example.lvpeiling.nodddle.adapter.loadmore.EndlessRecyclerOnScrollListener;
import com.example.lvpeiling.nodddle.model.ShotVO;
import com.example.lvpeiling.nodddle.model.UserVO;
import com.example.lvpeiling.nodddle.network.Api;
import com.example.lvpeiling.nodddle.network.OkHttpClientManager;
import com.example.lvpeiling.nodddle.network.ResultCallBack;
import com.example.lvpeiling.nodddle.util.ShareUtil;
import com.example.lvpeiling.nodddle.view.CircleImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class HomePageActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rv_user_shots)
    RecyclerView rvUserShots;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.civ_userhead)
    CircleImageView civUserhead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.layout_user)
    LinearLayout layoutUser;
    private ShotsAdapter mShotsAdapter;
    private static int page = 1;
    private int userId;
    private List<ShotVO> mShotList;
    private UserVO userVO;
    private static final String TAG = "HomePageActivity";
    private Context mContext;

    @Override
    public int getContentViewId() {
        return R.layout.activity_home_page;
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        setToolbar();
        mContext = this;
        userId = getIntent().getIntExtra("userId", 0);
        getUserInfoFromNetwork();
        checkIsFollowing();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fab.isSelected()) {
                    followUser();
                } else {
                    unfollowUser();
                }
            }


        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvUserShots.setLayoutManager(layoutManager);
        mShotsAdapter = new ShotsAdapter();
        rvUserShots.setAdapter(mShotsAdapter);
        swipeRefresh.setOnRefreshListener(this);
        rvUserShots.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                page++;
                getDataFromNetwork(page);
            }
        });
        rvUserShots.setHasFixedSize(true);
        onRefresh();
        layoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this,UserInfoActivity.class);
                intent.putExtra("user",userVO);
                startActivity(intent);
            }
        });
    }

    /**
     * 不再关注某人
     */
    private void unfollowUser() {
        if (userId == 0) {
            Snackbar.make(rvUserShots, "获取用户信息失败", Snackbar.LENGTH_SHORT).show();
            return;
        }
        OkHttpClientManager.getInstance().putAsync(String.format(Api.FOLLOW_USER, userId), null, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Snackbar.make(rvUserShots, e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response) {
                Response mResponse = (Response) response;
                if(mResponse.isSuccessful()){
                    fab.setSelected(true);
                }
            }
        });
    }

    /**
     * 关注某人
     */
    private void followUser() {
        if (userId == 0) {
            Snackbar.make(rvUserShots, "获取用户信息失败", Snackbar.LENGTH_SHORT).show();
            return;
        }
        OkHttpClientManager.getInstance().deleteAsync(String.format(Api.FOLLOW_USER, userId), null, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Snackbar.make(rvUserShots, e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response) {
                Response mResponse = (Response) response;
                if(mResponse.isSuccessful()){
                    fab.setSelected(false);
                }
            }
        });
    }

    /**
     * 是否已关注此人
     */
    private void checkIsFollowing() {
        if (userId == 0) {
            Snackbar.make(rvUserShots, "获取用户信息失败", Snackbar.LENGTH_SHORT).show();
            return;
        }
        OkHttpClientManager.getInstance().getAsync(String.format(Api.CHECK_USER_FOLLOWING, userId + ""), null, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Log.e(TAG,e.getMessage());

            }

            @Override
            public void onResponse(Object response) {
                Response mResponse = (Response) response;
                if(mResponse.isSuccessful()){
                    fab.setSelected(true);
                }else {
                    fab.setSelected(false);
                }
            }
        });
    }

    private void getUserInfoFromNetwork() {
        if (userId == 0) {
            Snackbar.make(rvUserShots, "获取用户信息失败", Snackbar.LENGTH_SHORT).show();
            return;
        }
        swipeRefresh.setRefreshing(true);
        OkHttpClientManager.getInstance().getAsync(String.format(Api.USERINFO, userId + ""), null, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                }
                Snackbar.make(rvUserShots, e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response) {
                Response mResponse = (Response) response;
                try {
                    String respStr = mResponse.body().string();
                    if (respStr.length() <= 4) {
                        Snackbar.make(rvUserShots, "暂无更多数据", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    if (mResponse.isSuccessful()) {
                        userVO = JSON.parseObject(respStr, UserVO.class);
                        setDataView();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setDataView() {
        if (userVO != null) {
            if (userVO.getName() != null) {
                tvName.setText(userVO.getName());
            }
            if (userVO.getAvatar_url() != null) {
                Glide.with(this).load(userVO.getAvatar_url()).placeholder(R.drawable.avatar_default).
                        error(R.drawable.avatar_default).diskCacheStrategy(DiskCacheStrategy.ALL).into(civUserhead);
            }
        }
    }

    private void setToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
                        if (userVO.getHtml_url() != null) {
//                            shareHtml(userVO.getHtml_url());
                            ShareUtil.shareUrl(mContext,"Nodddle","我在Nodddle上看到一个优秀的设计师，一起来看一下他的主页吧!!",userVO.getHtml_url());
                        }
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shots_detail, menu);
        return true;
    }

    @Override
    public void onRefresh() {
        swipeRefresh.setRefreshing(true);
        page = 1;
        getDataFromNetwork(page);
    }

    private void getDataFromNetwork(final int page) {
        if (userId == 0) {
            Snackbar.make(rvUserShots, "获取用户信息失败", Snackbar.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("page",String.valueOf(page));
        OkHttpClientManager.getInstance().getAsync(String.format(Api.USER_SHOT, userId + ""), param, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                }
                Snackbar.make(rvUserShots, e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response) {
                Response mResponse = (Response) response;
                try {
                    String mRespoStr = mResponse.body().string();
                    Log.e(TAG, mRespoStr);
                    if (mResponse.isSuccessful()) {
                        if (mRespoStr.length() <= 4) {
                            Snackbar.make(rvUserShots, "暂无更多数据", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        List<ShotVO> mShots = JSONArray.parseArray(mRespoStr, ShotVO.class);
                        if (page == 1) {
                            mShotList = mShots;
                        } else {
                            mShotList.addAll(mShots);
                        }
                        mShotsAdapter.setShotsList(mShotList);
                        mShotsAdapter.notifyDataSetChanged();
                        if (swipeRefresh.isRefreshing()) {
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
