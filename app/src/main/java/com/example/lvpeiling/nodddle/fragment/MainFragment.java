package com.example.lvpeiling.nodddle.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.example.lvpeiling.nodddle.R;
import com.example.lvpeiling.nodddle.adapter.ShotsAdapter;
import com.example.lvpeiling.nodddle.adapter.loadmore.EndlessRecyclerOnScrollListener;
import com.example.lvpeiling.nodddle.model.ShotVO;
import com.example.lvpeiling.nodddle.network.Api;
import com.example.lvpeiling.nodddle.network.OkHttpClientManager;
import com.example.lvpeiling.nodddle.network.ResultCallBack;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "MainFragment";
    @BindView(R.id.rv_shots)
    RecyclerView rvShots;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private List<ShotVO> shots;
    private Context mContext;
    private ShotsAdapter shotsAdapter;
    private static int page = 1;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        setView();
    }


    private void setView() {
        mContext = getContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvShots.setLayoutManager(layoutManager);
        shotsAdapter = new ShotsAdapter();
        rvShots.setAdapter(shotsAdapter);
        onRefresh();
        refreshLayout.setOnRefreshListener(this);
        rvShots.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                page++;
                getDataFromNetwork(page);
            }
        });
    }


    private void getDataFromNetwork(final int page) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("list", "animated");
        param.put("timeframe", "week");
        param.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        param.put("sort", "comments");
        param.put("page", page+"");
        OkHttpClientManager.getInstance().getAsync(Api.SHOTS_URL, param, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Object response) {
                List<ShotVO> mShots;
                Response mResponse = (Response) response;
                try {
                    String responceStr = mResponse.body().string();
                    mShots = JSONObject.parseArray(responceStr, ShotVO.class);
                    if(page == 1){
                        shots = mShots;
                    }else {
                        shots.addAll(mShots);
                    }
                    shotsAdapter.setShotsList(shots);
                    shotsAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        page = 1;
        getDataFromNetwork(page);
        refreshLayout.setRefreshing(false);
    }
}
