package com.example.lvpeiling.nodddle.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lvpeiling.nodddle.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BucketsFragment extends BaseFragment {


    @Override
    public int getContentViewId() {
        return R.layout.fragment_buckets;
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buckets, container, false);
    }

}
