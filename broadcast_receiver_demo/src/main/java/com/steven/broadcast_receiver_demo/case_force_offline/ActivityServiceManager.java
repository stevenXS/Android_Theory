package com.steven.broadcast_receiver_demo.case_force_offline;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityServiceManager {
    private static List<Activity> activityList = new ArrayList<>();

    public static void addActivity(Activity activity){
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity : activityList) {
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
