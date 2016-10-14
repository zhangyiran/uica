package com.yiran.demoapplication;

import android.app.Application;

/**
 * Created by zhangyiran on 16/10/14.
 */
public class AppApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread() {
            @Override
            public void run() {
                CommonUtil.findAndCopyShell(AppApplication.this);
            }
        }.start();

    }
}
