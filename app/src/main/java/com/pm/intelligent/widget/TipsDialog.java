package com.pm.intelligent.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.pm.intelligent.R;

import java.util.List;

/**
 * Created by pumin on 2018/8/9.
 */

public enum TipsDialog {

    INSTANCE;
    private Dialog dialog;
    private View view;
    List<String> mList;
    private int mPosition = 0;

    public void showDialog(String title, Context context, String content, final TipsListener tipsListener) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_tips, null);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvContent = view.findViewById(R.id.tv_content);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);

        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        }

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.tv_confirm) {
                    if (dialog != null)
                        dialog.dismiss();
                    tipsListener.tipsListener();
                }
            }
        };

        tvConfirm.setOnClickListener(listener);

        dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams p = window.getAttributes();
        //p.width = ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 240f);
        p.width = WindowManager.LayoutParams.WRAP_CONTENT;
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(p);
        dialog.show();

    }


    public interface TipsListener {
        void tipsListener();
    }


}
