package com.yiran.demoapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class CommonUtil {
    public static final String TAG = CommonUtil.class.getSimpleName();

    public static void saveFlag(Context context, boolean flag) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("configuration", 0);
        SharedPreferences.Editor  editor  =  sharedPreferences.edit();
        editor.putBoolean("iscopy",flag);
        editor.commit();
    }

    public static boolean getFlag(Context context) {
        SharedPreferences  sharedPreferences = context.getSharedPreferences("configuration", 0);
        return sharedPreferences.getBoolean("iscopy", false);
    }

    public static void savePath(Context context, String path) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("configuration", 0);
        SharedPreferences.Editor  editor  =  sharedPreferences.edit();
        editor.putString("path", path);
        editor.commit();
    }

    public static String getPath(Context context) {
        SharedPreferences  sharedPreferences = context.getSharedPreferences("configuration", 0);
        return sharedPreferences.getString("path", "");
    }

    public static synchronized void findAndCopyShell(Context context) {
        String[] files = null;
        try {// 遍历assest文件夹，读取压缩包及安装包
            files = context.getAssets().list("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i];

            if (fileName.contains(".sh")) {//拷贝sh文件
                String shellName = fileName;
                String s = String.valueOf(context.getFilesDir());
                String cachePath = (new StringBuilder(s)).append(
                        "/uica.start.sh").toString();// 临时文件
                if (CommonUtil.copyFileFromAssets(context, shellName, cachePath)) {
                    saveFlag(context, true);
                    savePath(context, cachePath);
                    Log.d("test", "copy " + cachePath +" success!");
                }
            }


        }
    }

    public static void writeFileToSD(String filename, String content) {
        String sdStatus = Environment.getExternalStorageState();
        if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.d(TAG, "SD card is not avaiable/writeable right now.");
            return;
        }
        try {
            String pathName= Environment.getExternalStorageDirectory().getPath();
            String fileName="/"+filename;
            File file = new File(pathName + fileName);

            if( !file.exists()) {
                Log.d(TAG, "Create the file:" + fileName);
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(file);

            byte[] buf = content.getBytes();
            stream.write(buf);
            stream.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean copyFileFromAssets(Context context, String apkName, String path){
        boolean flag = false;
        int BUFFER = 10240;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        AssetFileDescriptor fileDescriptor = null;
        byte b[] = null;
        try {
            fileDescriptor = context.getAssets().openFd(apkName);
            File file = new File(path);
            if (file.exists()) {
                if (fileDescriptor != null
                        && fileDescriptor.getLength() == file.length()) {
                    flag = true;
                } else
                    file.delete();
            }
            if (!flag) {
                in = new BufferedInputStream(fileDescriptor.createInputStream(),
                        BUFFER);
                boolean isOK = file.createNewFile();
                if (in != null && isOK) {
                    out = new BufferedOutputStream(new FileOutputStream(file),
                            BUFFER);
                    b = new byte[BUFFER];
                    int read = 0;
                    while ((read = in.read(b)) > 0) {
                        out.write(b, 0, read);
                    }
                    flag = true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("test", e.getMessage(), e);
        }finally{
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test", e.getMessage(), e);
                }
            }
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test", e.getMessage(), e);
                }
            }
            if(fileDescriptor != null){
                try {
                    fileDescriptor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test", e.getMessage(), e);
                }
            }
        }
        return flag;
    }

    public static void execCommand(String command) throws IOException {
        // start the ls command running
        //String[] args =  new String[]{"sh", "-c", command};
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec(command);        //这句话就是shell与高级语言间的调用
        //如果有参数的话可以用另外一个被重载的exec方法
        //实际上这样执行时启动了一个子进程,它没有父进程的控制台
        //也就看不到输出,所以我们需要用输出流来得到shell执行后的输出
        InputStream inputstream = proc.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        // read the ls output
        String line = "";
        StringBuilder sb = new StringBuilder(line);
        while ((line = bufferedreader.readLine()) != null) {
            //System.out.println(line);
            sb.append(line);
            sb.append('\n');
        }
        //tv.setText(sb.toString());
        //使用exec执行不会等执行成功以后才返回,它会立即返回
        //所以在某些情况下是很要命的(比如复制文件的时候)
        //使用wairFor()可以等待命令执行完成以后才返回
        try {
            if (proc.waitFor() != 0) {
                System.err.println("exit value = " + proc.exitValue());
                Log.e("test", "exit value = " + proc.exitValue());
            } else {
                Log.e("test", "proc.waitFor()==0 exit value = " + proc.exitValue());
            }
        }
        catch (InterruptedException e) {
            System.err.println(e);
            Log.e("test", e.getMessage(), e);
        }
    }

}
