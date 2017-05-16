package com.example.lvpeiling.nodddle.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lvpeiling.nodddle.R;
import com.example.lvpeiling.nodddle.model.ShotVO;
import com.example.lvpeiling.nodddle.model.UserVO;
import com.example.lvpeiling.nodddle.network.Api;
import com.example.lvpeiling.nodddle.network.OkHttpClientManager;
import com.example.lvpeiling.nodddle.network.ResultCallBack;
import com.example.lvpeiling.nodddle.view.CircleImageView;

import java.io.IOException;

import butterknife.BindColor;
import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

public class ShotsDetailActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "ShotsDetailActivity";
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.iv_picture)
    ImageView ivPicture;
    @BindView(R.id.loading_progressbar)
    ContentLoadingProgressBar loadingProgressbar;
    @BindView(R.id.civ_userhead)
    CircleImageView civUserhead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    private String shotId;
    private ShotVO shotItem;
    @BindView(R.id.layout_user)
    View userView;
    @BindView(R.id.layout_tag)
    LinearLayout tagLayout;
    @BindColor(R.color.white)
    ColorStateList white;
    @Override
    public int getContentViewId() {
        return R.layout.activity_shots_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        setView();
        getData();
    }

    private void getData() {
        loadingProgressbar.show();
        shotId = String.valueOf(getIntent().getIntExtra("id", 0));
        Log.e("url", String.format(Api.GET_SHOT_URL, shotId));
        OkHttpClientManager.getInstance().getAsync(String.format(Api.GET_SHOT_URL, shotId), null, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Object response) {
                Response mResponce = (Response) response;
                try {
                    String responceStr = mResponce.body().string();
                    Log.e(TAG, responceStr);
                    shotItem = JSONObject.parseObject(responceStr, ShotVO.class);
                    if (loadingProgressbar.isShown()) {
                        loadingProgressbar.hide();
                    }
                    setViewData();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @TargetApi(Build.VERSION_CODES.N)
    private void setViewData() {
        if (shotItem != null) {
            UserVO userVO = shotItem.getUser();
            Glide.with(this).load(shotItem.getImages().getNormal()).placeholder(R.mipmap.placeholder).error(R.mipmap.error).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivPicture);
            Glide.with(this).load(userVO.getAvatar_url()).placeholder(R.drawable.avatar_default).error(R.mipmap.error).diskCacheStrategy(DiskCacheStrategy.ALL).into(civUserhead);
            if(userVO.getName() != null) {
                tvName.setText(userVO.getName());
            }
            if (shotItem.getTitle() != null){
                tvTitle.setText(shotItem.getTitle());
            }
            if(shotItem.getCreated_at() != null){
                tvTime.setText(shotItem.getCreated_at());
            }
            if (shotItem.getDescription()!=null){
                tvDescription.setText(Html.fromHtml(shotItem.getDescription(),Html.FROM_HTML_MODE_LEGACY));
            }
            if(shotItem.getTags().size() > 0){
                for(String tag: shotItem.getTags()){
                    TextView textView = new TextView(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8,8,8,8);
                    textView.setLayoutParams(params);
                    textView.setBackground(getDrawable(R.drawable.tag));
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(white);
                    textView.setText(tag);
                    tagLayout.addView(textView);
                }

            }
        }
    }

    private void setView() {
        fab.setOnClickListener(this);
        userView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_user:
                Intent userIntent = new Intent(ShotsDetailActivity.this, HomePageActivity.class);
                startActivity(userIntent);
                break;
            case R.id.fab:
                break;
        }
    }
}
