package com.pm.intelligent.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class SlideMenu extends ViewGroup {

    private static final String TAG = "SlideMenu";
    private int mMostRecentX;    // x轴最后一次的偏移量
    private final int MENU_SCREEN = 1;    // 菜单界面
    private final int MAIN_SCREEN = 2;    // 主界面
    private int currentScreen = MAIN_SCREEN;        // 当前的屏幕显示界面, 默认为主界面
    private Scroller scroller;        // 用于模拟数据
    private int mTouchSlop;


    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        scroller = new Scroller(context);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 初始化菜单和主界面的宽和高
        initView(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 测量菜单和主界面的宽和高
     *
     * @param widthMeasureSpec  slideMenu控件的宽度测量规格
     * @param heightMeasureSpec slideMenu控件的高度测量规格
     */
    private void initView(int widthMeasureSpec, int heightMeasureSpec) {
        // 菜单对象
        View menuView = this.getChildAt(0);
        menuView.measure(menuView.getLayoutParams().width, heightMeasureSpec);

        // 主界面
        View mainView = this.getChildAt(1);
        mainView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 菜单对象
        View menuView = this.getChildAt(0);
        menuView.layout(-menuView.getMeasuredWidth(), 0, 0, b);

        // 主界面
        View mainView = this.getChildAt(1);
        mainView.layout(l, t, r, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMostRecentX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int currentX = (int) event.getX();    // 当前最新的x轴偏移量;

                // 1. 计算增量值: 增量值 = x轴的最后一次偏移量 -  当前最新的x轴偏移量;
                int delta = mMostRecentX - currentX;
                // 2. 根据增量值, 更新屏幕显示的位置

                // 判断移动后是否已经超过了两个边界
                int scrollX = getScrollX() + delta;        // 移动后的x轴的偏移量

                if (scrollX < -getChildAt(0).getWidth()) {        // 当前超出了左边界
                    scrollTo(-getChildAt(0).getWidth(), 0);    // 滚动到菜单的左边界
                } else if (scrollX > 0) {        // 超出了右边界
                    scrollTo(0, 0);        // 滚动到主界面的左边界
                } else {        // 正常移动
                    scrollBy(delta, 0);
                }

                // 3. x轴的最后一次偏移量(43) =  当前最新的x轴偏移量(43);
                mMostRecentX = currentX;

                break;
            case MotionEvent.ACTION_UP:
                // 菜单的中心点
                int menuCenter = -getChildAt(0).getWidth() / 2;

                // 获得当前屏幕x轴的偏移量
                int _x = getScrollX();    // x轴当前的偏移量

                if (_x > menuCenter) {        // 切换到主界面
                    currentScreen = MAIN_SCREEN;
                } else {    // 切换到菜单界面
                    currentScreen = MENU_SCREEN;
                }

                switchScreen();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 用于拦截事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMostRecentX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int diff = moveX - mMostRecentX;
                if (Math.abs(diff) > mTouchSlop) {
                    if (isShowMenu()) {
                        if (moveX < 450) {
                            return true;
                        }
                    } else {
                        if (moveX < 180) {
                            return true;
                        }
                    }
                }

                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        // 更新当前屏幕的x轴的偏移量

        if (scroller.computeScrollOffset()) {        // 返回true代表正在模拟数据中, false 已经停止模拟数据
            scrollTo(scroller.getCurrX(), 0);        // 更新了x轴的偏移量

            invalidate();
        }
    }

    /**
     * 根据currentScreen变量切换屏幕
     */
    private void switchScreen() {
        int startX = getScrollX();
        int dx = 0;

        if (currentScreen == MAIN_SCREEN) {
            dx = 0 - startX;
        } else if (currentScreen == MENU_SCREEN) {
            dx = -getChildAt(0).getWidth() - startX;
        }

        // 开始模拟数据
        scroller.startScroll(startX, 0, dx, 0, Math.abs(dx) * 1);

        // 刷新界面
        invalidate();  // invalidate -> drawChild -> child.draw -> computeScroll
    }

    /**
     * 判断是否正在显示菜单
     *
     * @return
     */
    public boolean isShowMenu() {
        return currentScreen == MENU_SCREEN;
    }

    /**
     * 隐藏菜单
     */
    public void hideMenu() {
        currentScreen = MAIN_SCREEN;
        switchScreen();
    }

    /**
     * 显示菜单
     */
    public void showMenu() {
        currentScreen = MENU_SCREEN;
        switchScreen();
    }

}
