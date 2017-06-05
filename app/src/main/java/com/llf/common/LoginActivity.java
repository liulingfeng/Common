package com.llf.common;

import android.widget.EditText;
import com.llf.basemodel.base.BaseActivity;
import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @Bind(R.id.et_name)
    EditText mEtName;
    @Bind(R.id.et_password)
    EditText mEtPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if ("llf".equals(mEtName.getText().toString()) && "123".equals(mEtPassword.getText().toString())) {
            showToast("登录成功");
            finish();
        }else {
            showToast("用户名或密码错误");
        }
    }
}
