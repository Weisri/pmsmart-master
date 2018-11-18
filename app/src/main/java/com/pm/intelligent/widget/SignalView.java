package com.pm.intelligent.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.pm.intelligent.R;


/**
 * Created by WeiSir on 2018/8/14.
 */
@SuppressLint("AppCompatCustomView")
public class SignalView extends ImageView {
    private Context mContext;
    private int thr1_2 = 4;
    private int thr2_3 = 5;
    private int thr3_4 = 7;
    private Drawable drawable0,drawable1,drawable2,drawable3,drawable4;
    private final int ZERO = 0,ONE = 1,TWO = 2,THREE = 3, FOUR = 4;
    private int signalStrength = ZERO;


    public SignalView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public SignalView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SignalView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SignalView, 0, 0 );
        thr1_2 = a.getInt(R.styleable.SignalView_thr1_2, thr1_2);
        thr2_3 = a.getInt(R.styleable.SignalView_thr2_3, thr2_3);
        thr3_4 = a.getInt(R.styleable.SignalView_thr3_4, thr3_4);
        a.recycle();
        if (drawable0 == null) drawable0 = getResources().getDrawable(R.mipmap.signal2);
        if (drawable1 == null) drawable1 = getResources().getDrawable(R.mipmap.signal2 );
        if (drawable2 == null) drawable2 = getResources().getDrawable(R.mipmap.signal3);
        if (drawable3 == null) drawable3 = getResources().getDrawable(R.mipmap.signal3);
        if (drawable4 == null) drawable4 = getResources().getDrawable(R.mipmap.signal4);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        drawable0.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());
        drawable1.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());
        drawable2.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());
        drawable3.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());
        drawable4.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());
    }

    private void setSignalStrength(int inUse,int count){
        if(inUse<=thr1_2){
            setSignalStrength(ONE);
        }else if(inUse<=thr2_3){
            setSignalStrength(TWO);
        }else{
            setSignalStrength(THREE);
        }
    }

    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
        invalidate();
    }

    public int getSignalStrength() {
        return signalStrength;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (signalStrength) {
            case ZERO:
                drawable0.draw(canvas);
                break;
            case ONE:
                drawable1.draw(canvas);
                break;
            case TWO:
                drawable2.draw(canvas);
                break;
            case THREE:
                drawable3.draw(canvas);
                break;
            case FOUR:
                drawable4.draw(canvas);
                break;
                default:
                    break;
        }
    }















}

