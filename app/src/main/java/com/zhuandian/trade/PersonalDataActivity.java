package com.zhuandian.trade;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuandian.base.BaseActivity;
import com.zhuandian.trade.entity.UserEntity;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class PersonalDataActivity extends BaseActivity {
    @BindView(R.id.et_nick_name)
    EditText etNickName;
    @BindView(R.id.et_user_password)
    EditText etUserPassword;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private UserEntity userEntity;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_data;
    }

    @Override
    protected void setUpView() {
        tvTitle.setText("修改资料");
        userEntity = BmobUser.getCurrentUser(UserEntity.class);
        etNickName.setText(userEntity.getNikeName() == null ? userEntity.getUsername() : userEntity.getNikeName());
        etUserPassword.setText(userEntity.getUserInfo() == null ? "" : userEntity.getUserInfo());
    }


    @OnClick({R.id.iv_back, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit:
                if (!TextUtils.isEmpty(etNickName.getText().toString()) || !TextUtils.isEmpty(etUserPassword.getText().toString())) {
                    userEntity.setNikeName(etNickName.getText().toString());
                    //用户输入密码长度大于1时才可修改密码，不输入密码则只修改用户昵称
                    if (etUserPassword.getText().toString().length() > 1) {
                        userEntity.setPassword(etUserPassword.getText().toString());
                    }
                    userEntity.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                finish();
                                Toast.makeText(PersonalDataActivity.this, "更新成功，重启生效...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
        }
    }
}
