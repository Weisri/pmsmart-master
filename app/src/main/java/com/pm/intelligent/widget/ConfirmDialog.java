package com.pm.intelligent.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.pm.intelligent.R;

/**
 * Created by WeiSir on 2018/8/15.
 */
public enum  ConfirmDialog {
    INSTANCE;
    private Dialog dialog;
    private static AlertDialog.Builder builer = null;
    public void showConfirmDialog(final Context context, String text,final OnConfirmListener confirmListener) {
        if (context!=null) {
            builer = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
            TextView title =  view.findViewById(R.id.tv_title_unlike);
            TextView cancel = view.findViewById(R.id.tv_cancel_unlike);
            TextView confirm = view.findViewById(R.id.tv_confirm_unlike);
            title.setText(text);
            dialog = builer.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);
            Window window = dialog.getWindow();
            window.setContentView(view);
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams p = window.getAttributes();
            //p.width = ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 240f);
            p.width = WindowManager.LayoutParams.WRAP_CONTENT;
            p.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(p);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        mCancelClickListener.onCancelClick();
                    dialog.dismiss();
                }
            });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmListener.onConfirmClick();
                    dialog.dismiss();
                }
            });
        }
    }

    public interface OnConfirmListener {
        void onConfirmClick();
    }


}
