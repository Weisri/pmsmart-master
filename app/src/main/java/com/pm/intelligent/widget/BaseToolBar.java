package com.pm.intelligent.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pm.intelligent.R;

/**
 * Created by WeiSir on 2018/6/26.
 *自定义ToolBar
 *
 */

public class BaseToolBar extends Toolbar implements TextWatcher{
    //搜索框
    private EditText toolbar_editText;
    //搜索按钮
    private TextView toolbar_rightbtn;
    //标题
    private TextView toolbar_title;
    //右侧按钮ImageView
    private ImageButton toolbar_imgBtn;
    //左侧按钮ImageView
    private ImageButton toolbar_LeftimgBtn;
    //右侧TextView
    private TextView toolbar_rightTextBtn;
    private MyToolBarBtnListenter btnListenter;
    private MyToolBarEditTextListener editTextListener;
    private MyToolBarEditClick editClick;
    //右侧TextView点击
    private MyToolBarRightTextBtnListener textBtnListener;
    //搜索点击
    private MyToolBarSearchListener searchBtnclick;
    private MyToolBarLeftBtnListener leftBtnListener;
    //接收输入内容
    private String keywords;
    private View view;


    public BaseToolBar(Context context) {
        this(context, null);
    }

    public BaseToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.toolbarStyle);
        initView();
    }
    @SuppressLint("RestrictedApi")
    public BaseToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
//        setContentInsetsRelative(100,100);
        //获取自定义属性
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                R.styleable.BaseToolBar, defStyleAttr, 0);
        Drawable drawableRight = a.getDrawable(R.styleable.BaseToolBar_RightImgButtonIcon);
        Drawable drawableLeft = a.getDrawable(R.styleable.BaseToolBar_LeftImgButtonIcon);
        boolean aBoolean = a.getBoolean(R.styleable.BaseToolBar_isShowEditText, false);
        String hint = a.getString(R.styleable.BaseToolBar_editHint);
        String title = a.getString(R.styleable.BaseToolBar_myTitle);
        String rightBtnTitle = a.getString(R.styleable.BaseToolBar_RightButtonTitle);
        String rightText = a.getString(R.styleable.BaseToolBar_RightTextBtn);
        int textColor = a.getColor(R.styleable.BaseToolBar_textColor, Color.GRAY);
//        int barBackground = a.getColor(R.styleable.BaseToolBar_barBackground, R.color.colorPrimaryDark);
        a.recycle();
        if(!TextUtils.isEmpty(hint)){
            toolbar_editText.setHint(hint);
        }
        if (drawableRight != null) {
            setRightImageBtnDrawable(drawableRight);
        }
        if (drawableLeft != null) {
            setLeftImageBtnDrawable(drawableLeft);
        }
        if (!TextUtils.isEmpty(title)) {
            toolbar_title.setText(title);
            toolbar_title.setTextColor(textColor);
        }
        if (!TextUtils.isEmpty(rightBtnTitle)) {
            toolbar_rightbtn.setText(rightBtnTitle);
            showtoolbar_rightbtn();
        }
        if (!TextUtils.isEmpty(rightText)) {
            toolbar_rightTextBtn.setText(rightText);
            showRightTextBtn();
        }


        /**
         * 如果显示editText为true，则把editText 设为显示，TextView设为隐藏
         */
        if (aBoolean) {
            showEditText();
            hintTextView();
        } else {
            hintEditText();
        }

    }



    private void initView() {
        view = View.inflate(getContext(), R.layout.widget_base_toolbar_layout, null);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
        addView(view, params);

        toolbar_editText = (EditText) this.findViewById(R.id.toolbar_editText);
        keywords = toolbar_editText.getText().toString().trim();

        toolbar_title = (TextView) this.findViewById(R.id.toolbar_title);
        toolbar_imgBtn = (ImageButton) this.findViewById(R.id.toolbar_rightImgBtn);
        toolbar_LeftimgBtn = (ImageButton) this.findViewById(R.id.toolbar_leftImgBtn);
        toolbar_rightbtn = (TextView) this.findViewById(R.id.toolbar_search);
        toolbar_rightTextBtn = (TextView) this.findViewById(R.id.toolbar_rightTextBtn);
        toolbar_editText.addTextChangedListener(this);
        toolbar_rightTextBtn.setOnClickListener(    new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textBtnListener != null) {
                    textBtnListener.textRightBtnClick();
                }
            }
        });
        toolbar_editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != editClick) {
                    editClick.editTextClick();
                }
            }
        });
        toolbar_imgBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != btnListenter) {
                    btnListenter.imageRightBtnclick();
                }
            }
        });

        toolbar_LeftimgBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != leftBtnListener) {
                    leftBtnListener.imageLeftBtnclick();
                }

            }
        });
        toolbar_rightbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != searchBtnclick) {
                    if(TextUtils.isEmpty(toolbar_editText.getText())){
                        Toast.makeText(getContext(),"输入为空",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    searchBtnclick.searchBtnclick(toolbar_editText.getText().toString());
                }
            }
        });
        toolbar_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Log.e("editText", "失去焦点");
                    // 失去焦点
                    toolbar_editText.setCursorVisible(false);
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(toolbar_editText.getWindowToken(), 0);
                }else{
                    Log.e("editText", "获得焦点");
                    toolbar_editText.setCursorVisible(true);
                }
            }
        });

    }


    public View getToolbarView(){
        return view;
    }
    public void showTextView() {
        toolbar_title.setVisibility(View.VISIBLE);
    }

    public void showEditText() {
        toolbar_editText.setVisibility(View.VISIBLE);
    }

    public void showRightBtnIcon() {
        toolbar_imgBtn.setVisibility(View.VISIBLE);
    }

    public void showLeftBtnIcon() {
        toolbar_LeftimgBtn.setVisibility(View.VISIBLE);
    }

    public void showtoolbar_rightbtn(){
        toolbar_rightbtn.setVisibility(View.VISIBLE);
    }

    public void showRightTextBtn() {
        toolbar_rightTextBtn.setVisibility(View.VISIBLE);
    }

    public void hinttoolbar_rightbtn(){
        toolbar_rightbtn.setVisibility(View.GONE);
    }
    public void hintTextView() {
        toolbar_title.setVisibility(View.GONE);
    }

    public void hintEditText() {
        toolbar_editText.setVisibility(View.GONE);
    }

    public void hintRightBtnIcon() {
        toolbar_imgBtn.setVisibility(View.GONE);
    }

    public void isEditTextFocus(boolean b) {
        if (b==false) {
            toolbar_editText.setFocusable(false);
        } else {
            toolbar_editText.setFocusable(true);
        }
    }



    @Override
    public void setTitle(@StringRes int resId) {
        showTextView();
        if (resId != 0) {
            toolbar_title.setText(resId);
        }
    }
    @Override
    public void setTitle(CharSequence title) {
        initView();
        showTextView();
        if (title != null) {
            toolbar_title.setText(title);
        }
    }

    public void setLeftBtnPadding(@Px int left,@Px int top,@Px int right,@Px int bottom){
        toolbar_LeftimgBtn.setPadding(left,top,right,bottom);
    }

    public void setLeftImageBtnDrawable(Drawable resId) {
        showLeftBtnIcon();
        toolbar_LeftimgBtn.setImageDrawable(resId);
    }

    public void setLeftImageBtnDrawable(int resId) {
        showLeftBtnIcon();
        toolbar_LeftimgBtn.setImageResource(resId);
    }

    public void setRightImageBtnDrawable(Drawable resId) {
        showRightBtnIcon();
        toolbar_imgBtn.setImageDrawable(resId);
    }

    public void setRightImageBtnResource(int resId) {
        showRightBtnIcon();
        toolbar_imgBtn.setImageResource(resId);
    }


    public void setMyToolBarBtnListenter(MyToolBarBtnListenter btnListenter) {
        this.btnListenter = btnListenter;
    }

    public void setMyToolBarEditTextListener(MyToolBarEditTextListener editTextListener) {
        this.editTextListener = editTextListener;
    }

    public void setMyToolBarEditClcik(MyToolBarEditClick editClcikListener) {
        this.editClick = editClcikListener;
    }

    public void setMyToolBarRightTextClick(MyToolBarRightTextBtnListener rightTextClick) {
        this.textBtnListener = rightTextClick;

    }

    public void setMyToolBarSearchClick(MyToolBarSearchListener searchListener) {
        this.searchBtnclick = searchListener;
    }
    public void setMyToolBarLeftBtnClick(MyToolBarLeftBtnListener barLeftBtnClick){
        this.leftBtnListener = barLeftBtnClick;
    }

    /**
     * Log.i(TAG, "输入文本之前的状态");
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (null != editTextListener) {
            editTextListener.beforeTextChanged(s, start, count, after);
        }
    }

    /**
     * Log.i(TAG, "输入文字中的状态，");
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (null != editTextListener) {
            editTextListener.onTextChanged(s, start, before, count);
        }
    }

    /**
     * Log.i(TAG, "输入文字后的状态");
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
//        if(TextUtils.isEmpty(s)){
//            showRightBtnIcon();
//            hinttoolbar_rightbtn();
//        }else {
//            hintRightBtnIcon();
//            showtoolbar_rightbtn();
//        }
        if (null != editTextListener) {
            editTextListener.afterTextChanged(s);
        }
    }

    public interface MyToolBarBtnListenter {
        void imageRightBtnclick();

    }
    public interface MyToolBarEditClick{
        void editTextClick();
    }
    public interface MyToolBarRightTextBtnListener {
        void textRightBtnClick();
    }
    public interface MyToolBarSearchListener {
        void searchBtnclick(String content);
    }
    public interface MyToolBarLeftBtnListener{
        void imageLeftBtnclick();
    }



    /**
     * editText变化监听
     */
    public interface MyToolBarEditTextListener {
        void beforeTextChanged(CharSequence s, int start, int count, int after);

        void onTextChanged(CharSequence s, int start, int before, int count);

        void afterTextChanged(Editable s);
    }
}
