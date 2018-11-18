package com.pm.intelligent.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.pm.intelligent.R;

/**
 * Created by pumin on 2018/8/2.
 */

public class RippleView extends RelativeLayout {

    private int mRippleColor;// 水波颜色

    private float mMaskAlpha;// 遮掩透明度
    private float mRippleAlpha;// 水波透明度

    private int mRippleTime;// 水波动画时间
    private ObjectAnimator mRippleAnim;// 显示水波动画

    private float mRadius;// 当前的半径
    private float mMaxRadius;// 水波最大半径

    private float mDownX; // 记录手指按下的位置
    private float mDownY;
    private boolean mIsMask;
    private boolean mIsAnimating;// 是否正在播放动画

    private RectF mRect;
    private Path mPath;
    private Paint mMaskPaint;
    private Paint mRipplePaint;

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        // 获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.RippleView);
        mRippleColor = ta.getColor(R.styleable.RippleView_rippleColor,
                mRippleColor);
        mMaskAlpha = ta.getFloat(R.styleable.RippleView_maskAlpha, mMaskAlpha);
        mRippleAlpha = ta.getFloat(R.styleable.RippleView_rippleAlpha,
                mRippleAlpha);
        mRippleTime = ta.getInt(R.styleable.RippleView_rippleTime, mRippleTime);
        ta.recycle();
        initLast();
    }

    // 初始化方法
    private void init() {
        mRippleColor = Color.BLACK;
        mMaskAlpha = 0.4f;
        mRippleAlpha = 0.8f;
        mRippleTime = 800;

        mPath = new Path();
        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRipplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    // 初始化
    private void initLast() {
        mMaskPaint.setColor(mRippleColor);
        mRipplePaint.setColor(mRippleColor);
        mMaskPaint.setAlpha((int) (mMaskAlpha * 255));
        mRipplePaint.setAlpha((int) (mRippleAlpha * 255));
    }

    // 初始化动画
    private void initAnim() {
        mRippleAnim = ObjectAnimator.ofFloat(this, "radius", 0, mMaxRadius);
        mRippleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mRippleAnim.setDuration(mRippleTime);
        mRippleAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setRadius(0);
                mIsMask = false;
                mIsAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 获取最大半径
        mMaxRadius = (float) Math.sqrt(w * w + h * h);
        // 获取该类布局范围
        mRect = new RectF(0f, 0f, getMeasuredWidth() * 1.0f,
                getMeasuredHeight() * 1.0f);
        // 初始化动画
        initAnim();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 按下事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mIsMask = true;
            invalidate();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            invalidate();
        }
        // 抬起事件
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            mDownX = event.getX();
            mDownY = event.getY();
            if (mIsAnimating) {
                mRippleAnim.cancel();
            }
            boolean isInView = mRect.contains(mDownX, mDownY);
            if (isInView) {
                mRippleAnim.start();
            } else {
                mIsMask = false;
                invalidate();
            }

        }

        return true;
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制遮掩
        if (mIsMask) {
           // canvas.save(Canvas.CLIP_SAVE_FLAG);
            mPath.reset();
            mPath.addRect(mRect, Path.Direction.CW);
            canvas.clipPath(mPath);
            canvas.drawRect(mRect, mMaskPaint);
            canvas.restore();

        }

        // 绘制水波
       // canvas.save(Canvas.CLIP_SAVE_FLAG);
        mPath.reset();
        mPath.addCircle(mDownX, mDownY, mRadius, Path.Direction.CW);
        canvas.clipPath(mPath);
        canvas.drawCircle(mDownX, mDownY, mRadius, mRipplePaint);
        canvas.restore();

    }

    // 属性动画回调的方法
    public void setRadius(final float radius) {
        mRadius = radius;
        invalidate();
    }

}
