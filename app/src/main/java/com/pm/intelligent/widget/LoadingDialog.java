package com.pm.intelligent.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.pm.intelligent.R;

import java.util.List;

/**
 * Created by pumin on 2018/8/9.
 */

public enum LoadingDialog {
    INSTANCE;
    private Dialog dialog;
    private View view;
    List<String> mList;
    private int mPosition = 0;
    private ImageView iv;

    public void showDialog(Context context, String content) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        iv = view.findViewById(R.id.iv_dialog);

        TextView tv = view.findViewById(R.id.tv_dialog);
        if (!TextUtils.isEmpty(content)) {
            tv.setText(content);
        }

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.loading_anim);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        iv.startAnimation(animation);


        dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams p = window.getAttributes();
        //p.width = ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 240f);
        p.width = WindowManager.LayoutParams.WRAP_CONTENT;
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(p);
        dialog.show();

    }


    public void dimissDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                if (iv != null) {
                    iv.clearAnimation();
                }
                dialog.dismiss();

            }
        }
    }

}
