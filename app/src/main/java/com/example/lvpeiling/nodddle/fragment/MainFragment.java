package com.example.lvpeiling.nodddle.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

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

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainFragment";
    @BindView(R.id.rv_shots)
    RecyclerView rvShots;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.spinner_list)
    Spinner spinnerList;
    @BindView(R.id.spinner_timeframe)
    Spinner spinnerTimeframe;
    @BindView(R.id.spinner_sort)
    Spinner spinnerSort;
    Unbinder unbinder1;
    private List<ShotVO> shots;
    private Context mContext;
    private ShotsAdapter shotsAdapter;
    private static int page = 1;

    @BindArray(R.array.list)
    String[] listsArr;
    @BindArray(R.array.sort)
    String[] sortArr;
    @BindArray(R.array.timeframe)
    String[] timeframeArr;
    private String list;
    private String timeframe;
    private String sort;

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
        spinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list = listsArr[position];
                onRefresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sort = sortArr[position];
                onRefresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerTimeframe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeframe = timeframeArr[position];
                onRefresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        onRefresh();
        refreshLayout.setOnRefreshListener(this);
        rvShots.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                page++;
                getDataFromNetwork(page, list, timeframe, sort);
            }
        });
    }


    private void getDataFromNetwork(final int page, @Nullable String list, @Nullable String timeframe, @Nullable String sort) {
        Map<String, Object> param = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(list)) {
            param.put("list", list);
        }
        if (!TextUtils.isEmpty(timeframe)) {
            param.put("timeframe", timeframe);
        }

        param.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        if (!TextUtils.isEmpty(sort)) {
            param.put("sort", sort);
        }
        param.put("page", page + "");
        OkHttpClientManager.getInstance().getAsync(Api.SHOTS_URL, param, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Log.e(TAG, e.getMessage());
                if(refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onResponse(Object response) {
                List<ShotVO> mShots;
                Response mResponse = (Response) response;
                try {
                    String responceStr = mResponse.body().string();
                    mShots = JSONObject.parseArray(responceStr, ShotVO.class);
                    if (page == 1) {
                        shots = mShots;
                    } else {
                        shots.addAll(mShots);
                    }
                    shotsAdapter.setShotsList(shots);
                    shotsAdapter.notifyDataSetChanged();
                    if(refreshLayout.isRefreshing()){
                        refreshLayout.setRefreshing(false);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        page = 1;
        getDataFromNetwork(page, list, timeframe, sort);
    }

}
