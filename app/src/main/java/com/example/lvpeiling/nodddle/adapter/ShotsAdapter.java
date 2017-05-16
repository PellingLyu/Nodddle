package com.example.lvpeiling.nodddle.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lvpeiling.nodddle.R;
import com.example.lvpeiling.nodddle.model.ShotVO;
import com.example.lvpeiling.nodddle.ui.ShotsDetailActivity;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lvpeiling on 2017/5/4.
 */
public class ShotsAdapter extends RecyclerView.Adapter<ShotsAdapter.ViewHolder> {


    private Context mContext;
    private AppCompatActivity activity;
    private List<ShotVO> mShots;
    private static final int FOOTER_LAYOUT = 1;
    private static final int OTHER_LAYOUT = 0;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        activity = (AppCompatActivity) mContext;
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_shot, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void setShotsList(List<ShotVO> shotVOs) {
        mShots = shotVOs;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ShotVO item;
        if (mShots != null) {
            item = mShots.get(position);
            holder.tvComment.setText(item.getComments_count() + "");
            holder.tvView.setText(item.getViews_count() + "");
            holder.tvLike.setText(item.getLikes_count() + "");
            Glide.with(mContext).load(item.getImages().getNormal()).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.2f).placeholder(R.mipmap.placeholder).crossFade().into(holder.ivThumbnail);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ShotsDetailActivity.class);
                    intent.putExtra("id",item.getId());
                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mShots == null) {
            return 0;
        }
        return mShots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_thumbnail)
        ImageView ivThumbnail;
        @BindView(R.id.tv_view)
        TextView tvView;
        @BindView(R.id.tv_comment)
        TextView tvComment;
        @BindView(R.id.tv_like)
        TextView tvLike;
        @BindView(R.id.layout)
        CardView layout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}