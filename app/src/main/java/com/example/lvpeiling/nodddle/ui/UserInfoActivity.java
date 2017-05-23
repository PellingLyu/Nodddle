package com.example.lvpeiling.nodddle.ui;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lvpeiling.nodddle.R;
import com.example.lvpeiling.nodddle.model.TeamVO;
import com.example.lvpeiling.nodddle.model.UserVO;
import com.example.lvpeiling.nodddle.network.Api;
import com.example.lvpeiling.nodddle.network.OkHttpClientManager;
import com.example.lvpeiling.nodddle.network.ResultCallBack;
import com.example.lvpeiling.nodddle.util.ShareUtil;
import com.example.lvpeiling.nodddle.view.CircleImageView;

import java.io.IOException;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.civ_userhead)
    CircleImageView civUserhead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.layout_web)
    LinearLayout layoutWeb;
    @BindView(R.id.tv_twitter)
    TextView tvTwitter;
    @BindView(R.id.layout_twitter)
    LinearLayout layoutTwitter;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.tv_web)
    TextView tvWeb;
    @BindView(R.id.layout_location)
    LinearLayout layoutLocation;
    @BindView(R.id.tv_bio)
    TextView tvBio;
    @BindView(R.id.btn_follow)
    AppCompatButton btnFollow;
    @BindView(R.id.layout_team)
    LinearLayout layoutTeam;
    @BindView(R.id.tv_bucket_count)
    TextView tvBucketCount;
    private UserVO userVO;
    @BindString(R.string.follow)
    String followStr;
    @BindString(R.string.unfollow)
    String unfollowStr;
    @BindColor(R.color.colorAccent)
    ColorStateList colorAccent;
    @BindColor(R.color.divider)
    ColorStateList divider;
    private static final String TAG = "UserInfoActivity";
    List<TeamVO> teams;

    @Override
    public int getContentViewId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        setToolbar();
        userVO = getIntent().getParcelableExtra("user");
        if (userVO != null) {
            if (userVO.getAvatar_url() != null) {
                Glide.with(this).load(userVO.getAvatar_url()).placeholder(R.drawable.avatar_default).error(R.drawable.avatar_default).diskCacheStrategy(DiskCacheStrategy.ALL).into(civUserhead);
            }
            if (userVO.getName() != null) {
                tvName.setText(userVO.getName());
            }
            if (userVO.getLocation() != null) {
                layoutLocation.setVisibility(View.VISIBLE);
                tvName.setText(userVO.getLocation());
            }
            if (userVO.getBio() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvBio.setText(Html.fromHtml(userVO.getBio(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    tvBio.setText(Html.fromHtml(userVO.getBio()));
                }
            } else {
                tvBio.setText("此人暂未留下自我介绍~");
            }
            if (userVO.getLinks().getWeb() != null) {
                layoutWeb.setVisibility(View.VISIBLE);
                tvWeb.setText(userVO.getLinks().getWeb());
            }
            if (userVO.getLinks().getTwitter() != null) {
                layoutTwitter.setVisibility(View.VISIBLE);
                tvTwitter.setText(userVO.getLinks().getTwitter());
            }
            checkIsFollowing();
            if (userVO.getTeams_url() != null) {
                OkHttpClientManager.getInstance().getAsync(String.format(Api.USER_TEAM, userVO.getId()), null, new ResultCallBack() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(Object response) {
                        Response mResponse = (Response) response;
                        try {
                            String mRespStr = mResponse.body().string();
                            if (mResponse.isSuccessful()) {
                                teams = JSONArray.parseArray(mRespStr, TeamVO.class);
                                if (teams.size() > 0) {
                                    for (TeamVO item : teams) {
                                        TextView textView = new TextView(mContext);
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        params.setMargins(8, 8, 8, 8);
                                        textView.setLayoutParams(params);
                                        textView.setGravity(Gravity.CENTER);
                                        textView.setText(item.getName());
                                        layoutTeam.addView(textView);
                                    }
                                } else {
                                    TextView textView = new TextView(mContext);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(8, 8, 8, 8);
                                    textView.setLayoutParams(params);
                                    textView.setGravity(Gravity.CENTER);
                                    textView.setText("暂无数据");
                                    layoutTeam.addView(textView);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
            tvBucketCount.setText(userVO.getBuckets_count() + "份作品集");

        }

        btnFollow.setOnClickListener(this);
        tvBucketCount.setOnClickListener(this);

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
                            ShareUtil.shareUrl(mContext, "Nodddle", "我在Nodddle上看到一个优秀的设计师，一起来看一下他的主页吧!!", userVO.getHtml_url());
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_follow:

                break;
            default:
                break;
        }
    }

    /**
     * 是否已关注此人
     */
    private void checkIsFollowing() {
        if (userVO.getId() == 0) {
            Snackbar.make(tvTwitter, "获取用户信息失败", Snackbar.LENGTH_SHORT).show();
            return;
        }
        OkHttpClientManager.getInstance().getAsync(String.format(Api.CHECK_USER_FOLLOWING, userVO.getId() + ""), null, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Object response) {
                Response mResponse = (Response) response;
                Log.e(TAG, mResponse.code() + "");
                if (mResponse.isSuccessful()) {
                    btnFollow.setText(unfollowStr);
                    btnFollow.setTextColor(divider);
                    btnFollow.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_grey_stroke_button));
                } else {
                    btnFollow.setText(followStr);
                    btnFollow.setTextColor(colorAccent);
                    btnFollow.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_color_accent_stroke_button));
                }
            }
        });
    }
}
