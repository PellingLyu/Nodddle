package com.example.lvpeiling.nodddle.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.lvpeiling.nodddle.R;
import com.example.lvpeiling.nodddle.adapter.ShotsAdapter;
import com.example.lvpeiling.nodddle.adapter.loadmore.EndlessRecyclerOnScrollListener;
import com.example.lvpeiling.nodddle.model.ShotVO;
import com.example.lvpeiling.nodddle.network.Api;
import com.example.lvpeiling.nodddle.network.OkHttpClientManager;
import com.example.lvpeiling.nodddle.network.ResultCallBack;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class ShotsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_shots)
    RecyclerView rvShots;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Context mContext;
    private ShotsAdapter mAdapter;
    private int page = 1;
    private int bucketId;
    private List<ShotVO> mShotsList;

    @Override
    public int getContentViewId() {
        return R.layout.activity_shots;
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        mContext = this;
        bucketId = getIntent().getIntExtra("bucket_id", 0);
        setView();
    }

    private void setView() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvShots.setLayoutManager(linearLayoutManager);
        mAdapter = new ShotsAdapter();
        rvShots.setAdapter(mAdapter);
        rvShots.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                page++;
                if (!swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(true);
                }
                getDataFromNetwork(page);
            }
        });
        onRefresh();
    }


    @Override
    public void onRefresh() {
        page = 1;
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
        getDataFromNetwork(page);
    }

    private void getDataFromNetwork(final int page) {
        if (bucketId == 0) {
            Snackbar.make(rvShots, "获取作品集相关信息失败，请重试", Snackbar.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("page", String.valueOf(page));
        OkHttpClientManager.getInstance().getAsync(String.format(Api.BUCKET_SHOTS, bucketId), param, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Snackbar.make(rvShots, e.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(Object response) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Response resp = (Response) response;
                List<ShotVO> shots;
                try {
                    String respStr = resp.body().string();
                    if (respStr.length() <= 4) {
                        Snackbar.make(rvShots, "暂无数据", Snackbar.LENGTH_SHORT).show();
                    }
                    shots = JSON.parseArray(respStr, ShotVO.class);
                    if (page == 1) {
                        mShotsList = shots;
                    } else {
                        mShotsList.addAll(shots);
                    }
                    mAdapter.setShotsList(mShotsList);
                    mAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
