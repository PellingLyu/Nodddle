package com.example.lvpeiling.nodddle.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.example.lvpeiling.nodddle.R;
import com.example.lvpeiling.nodddle.adapter.CommentAdapter;
import com.example.lvpeiling.nodddle.adapter.loadmore.EndlessRecyclerOnScrollListener;
import com.example.lvpeiling.nodddle.model.CommentVO;
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

public class CommentActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{
    private static final String TAG = "CommentActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private CommentAdapter mCommentAdapter;
    private List<CommentVO> mCommentList;
    private static int page = 1;
    private String shotId;
    private Context mContext;
    @BindView(R.id.actv_comment_body)
    TextView tvCommentBody;
    @BindView(R.id.ib_send)
    ImageButton ibSend;

    @Override
    public int getContentViewId() {
        return R.layout.activity_comment;
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        setToolbar();
        mContext = this;
        shotId = getIntent().getStringExtra("shotId");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvComment.setLayoutManager(layoutManager);
        mCommentAdapter = new CommentAdapter(shotId);
        rvComment.setAdapter(mCommentAdapter);
        rvComment.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
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
        param.put("page", page + "");
        OkHttpClientManager.getInstance().getAsync(String.format(Api.COMMENT_SHOT, shotId), param, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Log.e(TAG, e.getMessage());
                Snackbar.make(rvComment, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(Object response) {
                Response mResponse = (Response) response;
                List<CommentVO> commentList;
                try {
                    String resp = mResponse.body().string();
                    if (resp.length() <= 4){
                        Toast.makeText(mContext,"暂无更多数据",Toast.LENGTH_SHORT).show();
                        if(swipeRefresh.isRefreshing()){
                            swipeRefresh.setRefreshing(false);
                        }

                        return;
                    }
                    commentList = JSONArray.parseArray(resp, CommentVO.class);
                    if(commentList != null){
                        if (page == 1) {
                            mCommentList = commentList;
                        } else {
                            mCommentList.addAll(commentList);
                        }

                        mCommentAdapter.setCommentList(mCommentList);
                        mCommentAdapter.notifyDataSetChanged();
                    }
                    if(swipeRefresh.isRefreshing()){
                        swipeRefresh.setRefreshing(false);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setToolbar() {
        toolbar.setTitle(R.string.comment);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ibSend.setOnClickListener(this);

    }


    @Override
    public void onRefresh() {
        swipeRefresh.setRefreshing(true);
        page = 1;
        getDataFromNetwork(page);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_send:
                addComment();
                break;
        }
    }

    /**
     * 发送评论
     */
    private void addComment() {

    }
}