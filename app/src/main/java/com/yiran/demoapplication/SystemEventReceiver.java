package com.yiran.demoapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by zhangyiran on 16/10/14.
 */
public class SystemEventReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"onReceive", Toast.LENGTH_SHORT).show();
            if (CommonUtil.getFlag(context) == true) {
                String path = CommonUtil.getPath(context);

                try {
                    CommonUtil.execCommand("sh " + path);
                    Toast.makeText(context,"exec "+path, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.e("test", e.getMessage(), e);
                }

            } else {
                Log.d("test", "not copy~");
            }

    }

}
