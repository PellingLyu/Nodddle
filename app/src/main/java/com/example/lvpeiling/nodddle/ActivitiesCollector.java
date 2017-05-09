package com.example.lvpeiling.nodddle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvpeiling on 2017/5/2.
 */
public class ActivitiesCollector {

    private static List<AppCompatActivity> activities;
    public static void addActivity(AppCompatActivity activity){
        if(activities == null){
            activities = new ArrayList<>();
        }
        activities.add(activity);

    }
    public static void removeActivity(AppCompatActivity activity){
        activities.remove(activity);
    }
    public void finishAll(){
        for(AppCompatActivity activity : activities) {
            if(!activity.isFinishing()){
                activity.finish();
            }

        }
    }

}
