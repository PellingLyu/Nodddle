package com.example.lvpeiling.nodddle.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lvpeiling.nodddle.R;
import com.example.lvpeiling.nodddle.model.CommentVO;
import com.example.lvpeiling.nodddle.model.UserVO;
import com.example.lvpeiling.nodddle.network.Api;
import com.example.lvpeiling.nodddle.network.OkHttpClientManager;
import com.example.lvpeiling.nodddle.network.ResultCallBack;
import com.example.lvpeiling.nodddle.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lvpeiling on 2017/5/17.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<CommentVO> mCommentList;
    private Context mContext;
    private AppCompatActivity activity;
    private String mShotId;

    public CommentAdapter(){

    }
    public CommentAdapter(String shotId){
        mShotId = shotId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        activity = (AppCompatActivity) parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_comment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mCommentList != null){
            CommentVO item = mCommentList.get(position);
            if(item.getBody() != null){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    holder.tvCommentBody.setText(Html.fromHtml(item.getBody(), Html.FROM_HTML_MODE_LEGACY));
                }else {
                    holder.tvCommentBody.setText(Html.fromHtml(item.getBody()));
                }
            }
            if (item.getCreated_at() != null){
                holder.tvTime.setText(item.getCreated_at());
            }
            if(item.getUser() != null){
                UserVO userVO = item.getUser();
                if(userVO.getAvatar_url()!=null){
                    Glide.with(activity).load(userVO.getAvatar_url()).error(R.drawable.avatar_default).
                            placeholder(R.drawable.avatar_default).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.civUserhead);
                }
                if (userVO.getName()!=null){
                    holder.tvName.setText(userVO.getName());
                }
            }
            holder.tvLike.setText(item.getLikes_count()+"");

            checkLikeComment(item.getId(),holder.ivLike);
        }

    }

    private void checkLikeComment(int commentId, final ImageView likeView) {

        OkHttpClientManager.getInstance().getAsync(String.format(Api.CHECK_COMMENT_LIKE,mShotId,commentId), null, new ResultCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                likeView.setSelected(false);
            }
            @Override
            public void onResponse(Object response) {
                Response mResponse = (Response) response;
                if(mResponse.isSuccessful()){
                    likeView.setSelected(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCommentList == null) {
            return 0;
        }
        return mCommentList.size();
    }

    public void setCommentList(List<CommentVO> commentList) {
        mCommentList = commentList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civ_userhead)
        CircleImageView civUserhead;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.iv_like)
        ImageView ivLike;
        @BindView(R.id.tv_like)
        TextView tvLike;
        @BindView(R.id.tv_comment_body)
        TextView tvCommentBody;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
