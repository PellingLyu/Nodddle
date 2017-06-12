package com.example.lvpeiling.nodddle.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.lvpeiling.nodddle.R;
import com.example.lvpeiling.nodddle.adapter.BucketAdapter;
import com.example.lvpeiling.nodddle.adapter.loadmore.EndlessRecyclerOnScrollListener;
import com.example.lvpeiling.nodddle.model.BucketVO;
import com.example.lvpeiling.nodddle.network.Api;
import com.example.lvpeiling.nodddle.network.OkHttpClientManager;
import com.example.lvpeiling.nodddle.network.ResultCallBack;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BucketsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "BucketsFragment";
    @BindView(R.id.rv_buckets)
    RecyclerView rvBuckets;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fab)
    FloatingActionButton fabAdd;
    @BindView(R.id.layout_no_bucket)
    RelativeLayout layoutNoBucket;
    private Context mContext;
    private BucketAdapter mBucketAdapter;
    private int page = 1;
    private List<BucketVO> mBucketList;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_buckets;
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        mContext = getContext();
        setView();
    }

    private void setView() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建作品集
            }
        });
        LinearLayoutManager llManager = new LinearLayoutManager(mContext);
        rvBuckets.setLayoutManager(llManager);
        mBucketAdapter = new BucketAdapter();
        rvBuckets.setAdapter(mBucketAdapter);
        rvBuckets.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                page++;
                getDataFromNetwork(page);
            }
        });
        onRefresh();
    }

    private void getDataFromNetwork(final int page) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("page", String.valueOf(page));
//        OkHttpClientManager.getInstance().getAsync(Api.MY_BUCKETS, param, new ResultCallBack() {
          OkHttpClientManager.getInstance().getAsync(String.format(Api.USER_BUCKETS,24078), param, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Snackbar.make(rvBuckets, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onResponse(Object response) {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
                List<BucketVO> bucketList;
                Response resp = (Response) response;
                try {
                    String respStr = resp.body().string();
                    Log.e(TAG, respStr);
                    if (respStr.length() <= 4) {
                        layoutNoBucket.setVisibility(View.VISIBLE);
                        return;
                    }
                    bucketList = JSON.parseArray(respStr, BucketVO.class);
                    if (page == 1) {
                        mBucketList = bucketList;
                    } else {
                        mBucketList.addAll(bucketList);
                    }
                    if (mBucketList.size() <= 0) {
                        layoutNoBucket.setVisibility(View.VISIBLE);
                    }else {
                        layoutNoBucket.setVisibility(View.GONE);
                    }
                    mBucketAdapter.setBucketList(mBucketList);
                    mBucketAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        page = 1;
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        getDataFromNetwork(page);
    }

}
