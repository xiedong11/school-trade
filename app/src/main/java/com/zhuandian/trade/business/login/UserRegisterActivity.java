package com.zhuandian.trade.business.login;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.zhuandian.base.BaseActivity;
import com.zhuandian.trade.R;
import com.zhuandian.trade.entity.UserEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class UserRegisterActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.et_school)
    EditText etSchool;
    @BindView(R.id.et_phone)
    EditText etPhone;
    private String userName;
    private String passWord;
    private String userPhone;
    private String userSchool;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_register;
    }

    @Override
    protected void setUpView() {
        setTitle("注册");
    }


    @OnClick(R.id.tv_register)
    public void onClick() {
        doRegister();
    }

    private void doRegister() {
        userName = etUsername.getText().toString();
        passWord = etPassword.getText().toString();
        userPhone = etPhone.getText().toString();
        userSchool = etSchool.getText().toString();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)||TextUtils.isEmpty(userPhone)||TextUtils.isEmpty(userSchool)) {
            Toast.makeText(this, "请完善注册信息...", Toast.LENGTH_SHORT).show();
        } else {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(userName);
            userEntity.setPassword(passWord);
            userEntity.setUserSchool(userSchool);
            userEntity.setMobilePhoneNumber(userPhone);
            userEntity.signUp(new SaveListener<Object>() {
                @Override
                public void done(Object o, BmobException e) {
                    if (e == null) {
                        showRegisterDialog();
                    } else {
                        Toast.makeText(UserRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showRegisterDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("注册成功")
                .setMessage("注册成功，是否马上去登陆？")
                .setNegativeButton("稍后登陆", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("马上登陆", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

}
