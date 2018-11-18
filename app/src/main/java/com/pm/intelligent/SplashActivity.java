package com.pm.intelligent;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.pm.intelligent.module.home.activity.MainActivity;
import com.pm.intelligent.module.login.activity.LoginActivity;
import com.pm.intelligent.utils.ActivityUtil;
import com.pm.intelligent.utils.Log;
import com.pm.intelligent.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiSir on 2018/6/27.
 */

public class SplashActivity extends Activity {
    private Context mContext;

    public void checkPermission(Context context) {
        //所有的权限列表
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean flag = true;
//        permissions.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
//        permissions.add(Manifest.permission.WAKE_LOCK);
//        permissions.add(Manifest.permission.READ_PHONE_STATE);
//       // permissions.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
//        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        //未获取的权限列表
        List<String> notGrantedList = new ArrayList<>();
        for (int i = 0; i < permissions.size(); i++) {
            String permission = permissions.get(i);
            int ret = ContextCompat.checkSelfPermission(context, permission);
            if (ret != PackageManager.PERMISSION_GRANTED) {
                flag = false;
                notGrantedList.add(permission);
            }
        }
        if (flag){
            jumpToMainActivity();
        }else {
            //申请未获取的权限
            int notGrantedCount = notGrantedList.size();
            if (notGrantedCount > 0) {
                String[] permissionArr = notGrantedList.toArray(new String[notGrantedCount]);
                ActivityCompat.requestPermissions((Activity) context, permissionArr, 0);

            }
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //申请权限
                    checkPermission(mContext);
                } else {
                    jumpToMainActivity();
                }
            }
        }, 250);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean flag = true;
        android.util.Log.e("====", "onRequestPermissionsResult: "+"zole" );
        int permissionLength = permissions.length;
        android.util.Log.e("===", "onRequestPermissionsResult: "+permissionLength );


        for (int i = 0; i < permissionLength; i++) {
            int ret = ContextCompat.checkSelfPermission(this, permissions[i]);
            Log.i("没有权限");
            if (ret != PackageManager.PERMISSION_GRANTED) {
                Log.i("没有权限");
                flag = false;
                boolean b = this.shouldShowRequestPermissionRationale(permissions[i]);
                if (!b) {
                    //标示拒绝且点击了不再提示
                    //引导设置
                    showAlerDialog();
                    return;
                }
            }
        }
        android.util.Log.e("====", "onRequestPermissionsResult: "+flag );
        if (!flag) {
             //表示所有权限都已申请，但有拒绝的权限
            //引导设置
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("我们需要获取权限！");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(uri);
                    SplashActivity.this.startActivity(intent);
                }
            });
            builder.show();
        }else {
            //表示所有权限都已申请成功
            jumpToMainActivity();
       }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkPermission(mContext);
    }

    /**
     * 跳转
     */
    private void jumpToMainActivity() {
        android.util.Log.i("登录状态 。。", "jumpToMainActivity: "+SPUtils.getInstance(this).getUserId());
//        if (SPUtils.getInstance(this).getUserId() != null) {
//            startActivity(new Intent(SplashActivity.this,MainActivity.class));
//            finish();
//        } else {
//            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//          finish();
//        }
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();

    }

    private void showAlerDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("我们需要获取权限！");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        builder.show();
    }
}
