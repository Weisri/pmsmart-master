package com.pm.intelligent.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pm.intelligent.R;


/**
 * Created by Administrator on 2018/7/14.
 */

public class SwichView extends RelativeLayout {


    private ImageView ivSwich;
    private boolean isOpen;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
        ivSwich.setImageDrawable(getResources().getDrawable(isOpen ? R.mipmap.open : R.mipmap.close));
    }

    private OnSwichViewClickListener listener;

    public SwichView(Context context) {
        this(context, null);
    }

    public SwichView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwichView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.swich_view, this, true);

        ivSwich = rootView.findViewById(R.id.iv_swich);
        ivSwich.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ivSwich.setImageDrawable(getResources().getDrawable(isOpen ? R.mipmap.close : R.mipmap.open));
                isOpen = !isOpen;
                if (listener != null){
                    listener.onSwichClicked(SwichView.this);
                }
            }
        });


    }

    public void setOnClickListener(OnSwichViewClickListener listener){
        this.listener = listener;
    }

    public interface OnSwichViewClickListener{

        /**
         * @param swichView
         */
        void onSwichClicked(SwichView swichView);
    }





}
