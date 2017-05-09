package com.example.lvpeiling.nodddle;

import android.app.Application;
import android.util.Log;

/**
 * Created by lvpeiling on 2017/4/28.
 */
public class NodddleApplication extends Application {
    private static String mTokenCode;

    private static  NodddleApplication instance;
    public static String getTokenCode() {
        return mTokenCode;
    }

    public static void setTokenCode(String tokenCode) {
        mTokenCode = tokenCode;
        Log.e("app token",mTokenCode);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static NodddleApplication getInstance() {
        return instance;
    }


}

