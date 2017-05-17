package com.example.lvpeiling.nodddle.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lvpeiling.nodddle.R;
import com.example.lvpeiling.nodddle.model.LikeResponse;
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

public class ShotsDetailActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.tv_view)
    TextView tvView;
    @BindView(R.id.tv_like)
    TextView tvLike;
    @BindView(R.id.tv_attachement)
    TextView tvAttachement;
    @BindView(R.id.tv_buckets)
    TextView tvBuckets;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String shotId;
    private ShotVO shotItem;
    @BindView(R.id.layout_user)
    View userView;
    @BindView(R.id.layout_tag)
    LinearLayout tagLayout;
    @BindColor(R.color.white)
    ColorStateList white;
    @BindView(R.id.iv_like)
    ImageView ivLike;
    private int likeCount;
    private Context mContext;


    @Override
    public int getContentViewId() {
        return R.layout.activity_shots_detail;
    }


    @Override
    protected void initAllView(Bundle savedInstanceState) {
        setView();
        getData();
    }

    private void getData() {
        loadingProgressbar.show();
        shotId = String.valueOf(getIntent().getIntExtra("id", 0));
        OkHttpClientManager.getInstance().getAsync(String.format(Api.GET_SHOT_URL, shotId), null, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Log.e(TAG, e.getMessage());
                Snackbar.make(ivLike, e.getMessage(), Snackbar.LENGTH_SHORT).show();
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

    private void setViewData() {
        if (shotItem != null) {
            UserVO userVO = shotItem.getUser();
            Glide.with(this).load(shotItem.getImages().getNormal()).placeholder(R.mipmap.placeholder).error(R.mipmap.error).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivPicture);
            Glide.with(this).load(userVO.getAvatar_url()).placeholder(R.drawable.avatar_default).error(R.mipmap.error).diskCacheStrategy(DiskCacheStrategy.ALL).into(civUserhead);
            if (userVO.getName() != null) {
                tvName.setText(userVO.getName());
            }
            if (shotItem.getTitle() != null) {
                tvTitle.setText(shotItem.getTitle());
            }
            if (shotItem.getCreated_at() != null) {
                tvTime.setText(shotItem.getCreated_at());
            }
            if (shotItem.getDescription() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvDescription.setText(Html.fromHtml(shotItem.getDescription(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    tvDescription.setText(Html.fromHtml(shotItem.getDescription()));
                }

            }
            if (shotItem.getTags().size() > 0) {
                for (String tag : shotItem.getTags()) {
                    TextView textView = new TextView(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 8, 8, 8);
                    textView.setLayoutParams(params);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        textView.setBackground(ContextCompat.getDrawable(this, R.drawable.tag));

                    } else {
                        textView.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.tag));
                    }
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(white);
                    textView.setText(tag);
                    tagLayout.addView(textView);
                }

            }
            tvAttachement.setText(shotItem.getAttachments_count() + "");
            tvBuckets.setText(shotItem.getBuckets_count() + "");
            tvView.setText(shotItem.getViews_count() + "");
            tvLike.setText(shotItem.getLikes_count() + "");
            likeCount = shotItem.getLikes_count();
        }
    }

    private void setView() {
        mContext = this;
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_share:
                        if(shotItem.getHtml_url() != null){
                            shareHtml(shotItem.getHtml_url() );
                        }
                        break;
                }
                return true;
            }
        });
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fab.setOnClickListener(this);
        userView.setOnClickListener(this);
        ivLike.setOnClickListener(this);
        checkIsLike();
    }

    private void shareHtml(String html_url) {
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);
        share_intent.setType("text/plain");
        share_intent.putExtra(Intent.EXTRA_SUBJECT,"Nodddle分享");
        share_intent.putExtra(Intent.EXTRA_TEXT,"我在Nodddle上看到一个优秀的作品，点击链接一起来看一下吧!!"+html_url);
        share_intent =Intent.createChooser(share_intent,"share");
        startActivity(share_intent);
    }

    /**
     * 点赞状态（是否已点赞）
     * 已点赞返回id和点赞时间   未点赞则 404NOT FOUND
     */
    private void checkIsLike() {
        if (shotId == null) {
            shotId = String.valueOf(getIntent().getIntExtra("id", 0));
        }
        OkHttpClientManager.getInstance().getAsync(String.format(Api.LIKE_SHOT, shotId), null, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                ivLike.setSelected(false);
            }

            @Override
            public void onResponse(Object response) {
                Response mResponse = (Response) response;
                if (mResponse.isSuccessful()) {
                    try {
                        String respStr = mResponse.body().string();
                        LikeResponse likeResp = JSONObject.parseObject(respStr, LikeResponse.class);
                        if (likeResp != null) {
                            ivLike.setSelected(true);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_user:
                Intent userIntent = new Intent(ShotsDetailActivity.this, HomePageActivity.class);
                startActivity(userIntent);
                break;
            case R.id.fab:
                Intent commentIntent = new Intent(ShotsDetailActivity.this, CommentActivity.class);
                commentIntent.putExtra("shotId", shotId);
                startActivity(commentIntent);
                break;
            case R.id.iv_like:
                if (ivLike.isSelected()) {
                    unlikeShot();
                } else {
                    likeShot();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 点赞
     */
    private void likeShot() {
        if (shotId == null) {
            shotId = String.valueOf(getIntent().getIntExtra("id", 0));
        }
        OkHttpClientManager.getInstance().postAsync(String.format(Api.LIKE_SHOT, shotId), null, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Object response) {
                Response mResponse = (Response) response;
                if (mResponse.isSuccessful()) {
                    try {
                        String respoStr = mResponse.body().string();
                        Log.e("like", respoStr);
                        LikeResponse likeResp = JSONObject.parseObject(respoStr, LikeResponse.class);
                        if (likeResp != null) {
                            ivLike.setSelected(true);
                            likeCount++;
                            tvLike.setText(likeCount + "");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 取消点赞
     */
    private void unlikeShot() {
        if (shotId == null) {
            shotId = String.valueOf(getIntent().getIntExtra("id", 0));
        }
        OkHttpClientManager.getInstance().deleteAsync(String.format(Api.LIKE_SHOT, shotId), null, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Log.e(TAG, e.getMessage());
                Snackbar.make(ivLike, "取消点赞失败：" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response) {
                Response mResponse = (Response) response;
                if (mResponse.code() == 204) {
                    ivLike.setSelected(false);
                    likeCount--;
                    tvLike.setText(likeCount + "");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shots_detail,menu);
        return true;
    }


}
