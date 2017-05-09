package com.example.lvpeiling.nodddle.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.lvpeiling.nodddle.ActivitiesCollector;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    public abstract int getContentViewId();
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(getContentViewId());
        unbinder = ButterKnife.bind(this);
        initAllView(savedInstanceState);
        ActivitiesCollector.addActivity(this);
    }
    protected abstract void initAllView(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        ActivitiesCollector.removeActivity(this);
    }
}
