package com.pm.intelligent.module.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.pm.intelligent.Contants;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.module.home.activity.MainActivity;
import com.pm.intelligent.module.login.dao.OkLoginDao;
import com.pm.intelligent.utils.SPUtils;
import com.pm.intelligent.utils.ToastUtils;
import com.pm.intelligent.widget.LoadingDialog;
import com.pm.intelligent.widget.TipsDialog;
import com.pm.okgolibrary.okInit.OkCallBack;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by WeiSir on 2018/6/21.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = Activity.class.getSimpleName();
    @BindView(R.id.et_userName)
    EditText etUserName;
    @BindView(R.id.et_passWord)
    EditText etPassWord;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.ib_delete)
    ImageButton ibDelete;
    @BindView(R.id.ib_what)
    ImageButton ibWhat;
    @BindView(R.id.tv_login_error)
    TextView tvLoginError;
    private int mNetStatus;

    boolean isUserNameEmpty;
    boolean isPassWordEmpty;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login_layout;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
//        etUserName.setText("xian");
//        etPassWord.setText("xian");

    }

    @Override
    protected void initViews() {
        btnLogin.setOnClickListener(this);
        ibDelete.setOnClickListener(this);
        ibWhat.setOnClickListener(this);
        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                isUserNameEmpty = TextUtils.isEmpty(text);
                isPassWordEmpty = TextUtils.isEmpty(etPassWord.getText().toString());
                ibDelete.setVisibility(isUserNameEmpty ? View.GONE : View.VISIBLE);
                if (!isUserNameEmpty && !isPassWordEmpty) {
                    btnLogin.setBackground(getResources().getDrawable(R.drawable.shap_round_bg_black));
                } else {
                    btnLogin.setBackground(getResources().getDrawable(R.drawable.shap_round_bg_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                isPassWordEmpty = TextUtils.isEmpty(text);
                isUserNameEmpty = TextUtils.isEmpty(etUserName.getText().toString());
                ibWhat.setVisibility(isPassWordEmpty ? View.GONE : View.VISIBLE);
                if (!isUserNameEmpty && !isPassWordEmpty) {
                    btnLogin.setBackground(getResources().getDrawable(R.drawable.shap_round_bg_black));
                } else {
                    btnLogin.setBackground(getResources().getDrawable(R.drawable.shap_round_bg_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    protected void initDatas() {

    }

    private void doLoginRequset() {
        HashMap<String, String> params = new HashMap<>();
        params.put("userName", etUserName.getText().toString());
        params.put("userPassword", etPassWord.getText().toString());
        if (mNetStatus == -1) {
            TipsDialog.INSTANCE.showDialog("登录提醒", LoginActivity.this, "当前网络连接不可用，请检查网络！", new TipsDialog.TipsListener() {
                @Override
                public void tipsListener() {

                }
            });
            return;
        }
        try {

            OkLoginDao.doLoginRequest(Contants.LOGIN, params, new OkCallBack() {
                @Override
                public void success(Object obj) {
                    Log.i(TAG, "success: 登录成功嘿嘿" + obj.toString());
                    String jsonStr = obj.toString();
                    SPUtils.getInstance(LoginActivity.this).setUserToken(jsonStr);

                    //  存储用户信息
                    SPUtils.getInstance(LoginActivity.this).setUserId(etUserName.getText().toString().trim());
                    Logger.i(SPUtils.getInstance(LoginActivity.this).getUserId());
                    SPUtils.getInstance(LoginActivity.this).setUserPwd(etPassWord.getText().toString().trim());
                    SPUtils.getInstance(LoginActivity.this).setLoginState(1);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    LoadingDialog.INSTANCE.dimissDialog();
                    finish();
                }

                @Override
                public void failed(int error, Object obj) {
                    Logger.i(TAG, "failed: 登录失败啊啊啊啊啊啊" + error);
                    LoadingDialog.INSTANCE.dimissDialog();
//                    TipsDialog.INSTANCE.showDialog("登录提醒", LoginActivity.this, "登录失败！", new TipsDialog.TipsListener() {
//                        @Override
//                        public void tipsListener() {
//
//                        }
//                    });
                    if (error == 201) {
                        ToastUtils.showShort(LoginActivity.this, "登录失败，账号或密码错误");
                    }
                    ToastUtils.showShort(LoginActivity.this, "登录失败,账号或密码错误");
                    return;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkout() {
        return true;
    }


    boolean isPassword;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (checkout()) {
                    if (mNetStatus == -1) {
                        TipsDialog.INSTANCE.showDialog("登录提醒", LoginActivity.this, "当前网络连接不可用，请检查网络！", new TipsDialog.TipsListener() {
                            @Override
                            public void tipsListener() {

                            }
                        });
                        return;
                    }
                    LoadingDialog.INSTANCE.showDialog(this, "正在登录...");
                    doLoginRequset();
                }
                break;
            case R.id.ib_delete:
                etUserName.getText().clear();
                break;
            case R.id.ib_what:
                isPassword = !isPassword;
                if (isPassword) {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    etPassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    //选择状态 显示明文--设置为可见的密码
                    etPassWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                etPassWord.setSelection(etPassWord.getText().toString().length());
                break;
            default:
                break;
        }

    }


    @Override
    public void onNetChange(int netWorkState) {
        this.mNetStatus = netWorkState;
        if (mNetStatus == -1) {
            ToastUtils.showLong(this, "当前网络不可用");
        }
    }

}
