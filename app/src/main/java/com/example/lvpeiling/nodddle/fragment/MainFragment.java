package com.example.lvpeiling.nodddle.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.lvpeiling.nodddle.R;
import com.example.lvpeiling.nodddle.adapter.ShotsAdapter;
import com.example.lvpeiling.nodddle.adapter.loadmore.EndlessRecyclerOnScrollListener;
import com.example.lvpeiling.nodddle.adapter.loadmore.HeaderAndFooterWrapper;
import com.example.lvpeiling.nodddle.model.ShotVO;
import com.example.lvpeiling.nodddle.network.Api;
import com.example.lvpeiling.nodddle.network.OkHttpClientManager;
import com.example.lvpeiling.nodddle.network.ResultCallBack;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "MainFragment";
    @BindView(R.id.rv_shots)
    RecyclerView rvShots;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    //    @BindView(R.id.fab)
//    FloatingActionButton fab;
    @BindView(R.id.spinner_list)
    Spinner spinnerList;
    @BindView(R.id.spinner_timeframe)
    Spinner spinnerTimeframe;
    @BindView(R.id.spinner_sort)
    Spinner spinnerSort;
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
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private ContentLoadingProgressBar progressBar;

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
        mContext = getContext();
        setView();
        onRefresh();
    }


    private void setView() {
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

        refreshLayout.setOnRefreshListener(this);
        rvShots.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                page++;
                getDataFromNetwork(page, list, timeframe, sort);
            }
        });
//        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(shotsAdapter);
//        mLoadMoreWrapper = new LoadMoreWrapper(mHeaderAndFooterWrapper);
//        mLoadMoreWrapper.setLoadMoreView(R.layout.common_list_footer);
//        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener()
//        {
//            @Override
//            public void onLoadMoreRequested()
//            {
//                page++;
//                getDataFromNetwork(page, list, timeframe, sort);
//            }
//        });




    }


    private void getDataFromNetwork(final int page, @Nullable String list, @Nullable String timeframe, @Nullable String sort) {
        Map<String, Object> param = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(list)) {
            param.put("list", list);
        }
        if (!TextUtils.isEmpty(timeframe)) {
            param.put("timeframe", timeframe);
        }

        if (!TextUtils.isEmpty(sort)) {
            param.put("sort", sort);
        }
        param.put("page", page + "");
        OkHttpClientManager.getInstance().getAsync(Api.SHOTS_URL, param, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Log.e(TAG, e.getMessage());
                Snackbar.make(rvShots,e.getMessage(),Snackbar.LENGTH_SHORT).show();
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
                    Log.e(TAG,responceStr);
                    if (responceStr.length() <= 4){
                        Toast.makeText(mContext,"暂无更多数据",Toast.LENGTH_SHORT).show();
                        return;
                    }
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