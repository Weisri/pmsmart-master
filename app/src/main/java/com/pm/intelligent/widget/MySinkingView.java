package com.pm.intelligent.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.pm.intelligent.R;

/**
 * Created by pumin on 2018/8/6.
 */

public class MySinkingView extends FrameLayout {

    private static final int DEFAULT_TEXTCOLOT = 0xFFFFFFFF;

    private static final int DEFAULT_TEXTSIZE = 60;

    private float mPercent;

    private Paint mPaint = new Paint();

    private Bitmap mBitmap;

    private Bitmap mScaledBitmap;

    private float mLeft, mTop;

    private int mSpeed = 15;

    private int mRepeatCount = 0;

    private Status mFlag = Status.NONE;

    private int mTextColor = DEFAULT_TEXTCOLOT;

    private int mTextSize = DEFAULT_TEXTSIZE;

    public MySinkingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTextColor(int color) {
        mTextColor = color;
    }

    public void setTextSize(int size) {
        mTextSize = size;
    }

    public void setPercent(float percent) {
        mFlag = Status.RUNNING;
        mPercent = percent;
        postInvalidate();

    }

    public void setStatus(Status status) {
        mFlag = status;
    }

    public void clear() {
        mFlag = Status.NONE;
        recycleAll();
    }

    public void complete() {
        mFlag = Status.COMPLETE;
        recycleAll();
    }

    private void recycleAll() {
        if (mScaledBitmap != null) {
            mScaledBitmap.recycle();
            mScaledBitmap = null;
        }

        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        // 绘制外圆环
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(width-1);
//        mPaint.setAntiAlias(true);
        //mPaint.setColor(Color.rgb(33, 211, 39));
//        mPaint.setColor(getResources().getColor(R.color.ring_bg));
//        canvas.drawCircle(width / 2, height / 2, 1, mPaint);

        //裁剪成圆区域
        Path path = new Path();
        canvas.save();
        path.reset();
        canvas.clipPath(path);
        path.addCircle(width / 2, height / 2, width / 2, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.REPLACE);

        if (mFlag == Status.RUNNING) {
            if (mScaledBitmap == null) {
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.wave2);
                mScaledBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth(), getHeight(), false);
                mBitmap.recycle();
                mBitmap = null;
                mRepeatCount = (int) Math.ceil(getWidth() / mScaledBitmap.getWidth() + 0.5) + 1;
            }
            for (int idx = 0; idx < mRepeatCount; idx++) {
                canvas.drawBitmap(mScaledBitmap, mLeft + (idx - 1) * mScaledBitmap.getWidth(), (1 - mPercent) * getHeight(), null);
            }
            String str = (int) (mPercent * 100) + "%";
            mPaint.setColor(mTextColor);
            mPaint.setTextSize(mTextSize);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
            canvas.drawText(str, (getWidth() - mPaint.measureText(str)) / 2, getHeight() / 2 + mTextSize / 2, mPaint);

            mLeft += mSpeed;
            if (mLeft >= mScaledBitmap.getWidth())
                mLeft = 0;


            postInvalidateDelayed(0);
        } else if (mFlag == Status.COMPLETE) {

            canvas.drawColor(Color.parseColor("#8CDD7B"));

            String str = (int) (mPercent * 100) + "%";
            mPaint.setColor(mTextColor);
            mPaint.setTextSize(mTextSize);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
            canvas.drawText(str, (getWidth() - mPaint.measureText(str)) / 2, getHeight() / 2 + mTextSize / 2, mPaint);

        }
        canvas.restore();
    }





    public enum Status {
        RUNNING, NONE, COMPLETE
    }

}
