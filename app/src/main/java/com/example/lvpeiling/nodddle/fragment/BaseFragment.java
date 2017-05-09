package com.example.lvpeiling.nodddle.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lvpeiling.nodddle.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    public abstract int getContentViewId();
    protected abstract void initAllView(Bundle savedInstanceState);
    protected View mRootView;
    private Unbinder unbinder;
    public BaseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(), container, false);
        unbinder = ButterKnife.bind(this,mRootView);
        initAllView(savedInstanceState);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
