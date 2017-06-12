package com.example.lvpeiling.nodddle.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lvpeiling.nodddle.R;
import com.example.lvpeiling.nodddle.model.BucketVO;
import com.example.lvpeiling.nodddle.ui.ShotsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lpl72 on 2017/6/11.
 */

public class BucketAdapter extends RecyclerView.Adapter<BucketAdapter.ViewHolder> {
    private Context mContext;
    private List<BucketVO> mBucketList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_bucket, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mBucketList != null) {
            final BucketVO bucketVO = mBucketList.get(position);
            if (bucketVO.getName() != null) {
                holder.tvName.setText(bucketVO.getName());
            }
            if (bucketVO.getUpdated_at() != null) {
                holder.tvTime.setText(bucketVO.getUpdated_at());
            }
            if (bucketVO.getDescription() != null) {
                holder.tvDescription.setText(bucketVO.getDescription());
            }
            if (bucketVO.getUser() != null && bucketVO.getUser().getName() != null) {
                holder.tvUserName.setVisibility(View.VISIBLE);
                holder.tvUserName.setText(bucketVO.getUser().getName());
            }
            holder.tvShotCount.setText(String.valueOf(bucketVO.getShots_count()));
            holder.layoutBucket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ShotsActivity.class);
                    intent.putExtra("bucket_id",bucketVO.getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public void setBucketList(List<BucketVO> bucketList) {
        mBucketList = bucketList;
    }

    @Override
    public int getItemCount() {
        if (mBucketList != null) {
            return mBucketList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_shot_count)
        TextView tvShotCount;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_user_name)
        TextView tvUserName;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.layout_bucket)
        LinearLayout layoutBucket;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
