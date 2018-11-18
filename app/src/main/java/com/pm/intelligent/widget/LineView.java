package com.pm.intelligent.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pm.intelligent.R;

/**
 * Created by Administrator on 2018/7/27.
 */

public class LineView extends LinearLayout {


    private View line;

    public LineView(Context context) {
        this(context,null);

    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        View view = LayoutInflater.from(context).inflate(R.layout.line_view, this, false);

        line = view.findViewById(R.id.view_line);
        addView(view);


    }


    public void setLineMargin(@Px int left,@Px int top,@Px int right,@Px int bootom){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) line.getLayoutParams();
        params.setMargins(left,top,right,bootom);
        line.setLayoutParams(params);

    }




}
