package com.example.lvpeiling.nodddle.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lvpeiling.nodddle.R;
import com.example.lvpeiling.nodddle.view.ZoomImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LargeImageActivity extends BaseActivity {
    @BindView(R.id.iv_picture)
    ZoomImageView ivPicture;
    TextView tvSave;
    private String imageUrl;
    private AppCompatDialog dialog;

    @Override
    public int getContentViewId() {
        return R.layout.activity_large_image;
    }


    @Override
    protected void initAllView(Bundle savedInstanceState) {
        imageUrl = getIntent().getStringExtra("imageUrl");
        dialog = new AppCompatDialog(mContext);
        dialog.setContentView(R.layout.layout_picture_dialog);
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).placeholder(R.mipmap.placeholder).error(R.mipmap.error).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(ivPicture);
        }
        ivPicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                return true;
            }
        });

        tvSave = (TextView) dialog.findViewById(R.id.tv_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePicture();
            }
        });

    }

    private void savePicture() {
        if(imageUrl != null){

        }
    }


}
