package com.zhuandian.trade.business.goods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuandian.base.BaseActivity;
import com.zhuandian.trade.R;

import butterknife.BindView;
import butterknife.OnClick;

public class NewReleaseActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_release;
    }

    @Override
    protected void setUpView() {
        tvTitle.setText("发布商品");
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
