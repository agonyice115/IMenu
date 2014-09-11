package com.huassit.imenu.android.util;

import com.huassit.imenu.android.BaseActivity;

import java.util.Stack;

/**
 * Created by Sylar on 14-7-11.
 */
public class ActivityStackManager {
    private Stack<BaseActivity> activityStack;
    private static ActivityStackManager manager = null;

    private ActivityStackManager() {
        activityStack = new Stack<BaseActivity>();
    }

    public static ActivityStackManager getActivityManager() {
        if (manager == null) {
            synchronized (ActivityStackManager.class) {
                if (manager == null) {
                    manager = new ActivityStackManager();
                }
            }
        }
        return manager;
    }

    public void push(BaseActivity activity) {
        if (activityStack != null) {
            activityStack.push(activity);
        }
    }

    public BaseActivity pop() {
        if (activityStack != null && !activityStack.isEmpty()) {
            return activityStack.pop();
        }
        return null;
    }

    public void finishAllExceptOne(Class clazz) {
        BaseActivity activity;
        while ((activity = pop()) != null) {
            if (activity.getClass() != clazz) {
                activity.finish();
            }
        }
    }
}
