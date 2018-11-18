package com.pm.intelligent.utils;

import android.os.Environment;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者：pengjf on 2017/12/aaa
 * 邮箱：pengjf@softsz.com
 */

public class LogFile {
    private static final LogFile ourInstance = new LogFile();
    private final String ROOT_DIR = "Android/data/";

    public static LogFile getInstance() {
        return ourInstance;
    }

    private LogFile() {
    }

    public void writeLog(String content) throws IOException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String date = sf.format(new Date());
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/log.txt";
        File file = new File(path);
        if (!file.exists())
            file.createNewFile();
        FileOutputStream fout = new FileOutputStream(file);
        String info = "时间：" + date + "内容：" + content;
        byte [] bytes = info.getBytes();
        fout.write(bytes);
        fout.close();
    }

//    public void writeLogDate(String content){
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
//        String date = sf.format(new Date());
//        //String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/log.txt";
//        File file = new File(MyApplication.getContext().getFilesDir().getPath()+"/log.txt");
//        if (!file.exists())
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        FileOutputStream fout = null;
//        try {
//            fout = new FileOutputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        String info = "时间：" + date + "内容：" + content;
//        byte [] bytes = info.getBytes();
//        try {
//            fout.write(bytes);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            fout.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }



    private String logPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        sb.append(ROOT_DIR);
        sb.append(File.separator);
        sb.append("log");
        sb.append(File.separator);
        return sb.toString();
    }
}
