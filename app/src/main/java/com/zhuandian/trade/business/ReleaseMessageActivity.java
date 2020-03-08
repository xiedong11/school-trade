package com.zhuandian.trade.business;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuandian.base.BaseActivity;
import com.zhuandian.trade.R;
import com.zhuandian.trade.entity.MessageEntity;
import com.zhuandian.trade.entity.UserEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class ReleaseMessageActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_message_title)
    EditText etMessageTitle;
    @BindView(R.id.et_message_content)
    EditText etMessageContent;
    @BindView(R.id.rb_type_1)
    RadioButton rbType1;
    @BindView(R.id.rb_type_2)
    RadioButton rbType2;
    @BindView(R.id.rb_type_3)
    RadioButton rbType3;
    @BindView(R.id.tv_release)
    TextView tvRelease;
    private int messageType = 1; //1.失误招领  2.寻物启事，3，系统消息

    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_message;
    }

    @Override
    protected void setUpView() {

    }


    @OnClick({R.id.iv_back, R.id.rb_type_1, R.id.rb_type_2, R.id.rb_type_3, R.id.tv_release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rb_type_1:
                messageType = 1;
                break;
            case R.id.rb_type_2:
                messageType = 2;
                break;
            case R.id.rb_type_3:
                messageType = 3;
                break;
            case R.id.tv_release:
                addNewMessage();
                break;
        }
    }

    private void addNewMessage() {
        String messageTitle = etMessageTitle.getText().toString();
        String messageContent = etMessageContent.getText().toString();
        if (TextUtils.isEmpty(messageContent) || TextUtils.isEmpty(messageTitle)) {
            Toast.makeText(this, "发布内容不允许为空...", Toast.LENGTH_SHORT).show();
            return;
        }

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setTitle(messageTitle);
        messageEntity.setContent(messageContent);
        messageEntity.setType(messageType);
        messageEntity.setUserEntity(BmobUser.getCurrentUser(UserEntity.class));
        messageEntity.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(ReleaseMessageActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    ReleaseMessageActivity.this.finish();
                }
            }
        });
    }
}
