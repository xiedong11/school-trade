package com.zhuandian.trade.business.tab;

import android.content.Intent;
import android.view.View;

import com.zhuandian.base.BaseFragment;
import com.zhuandian.trade.R;
import com.zhuandian.trade.business.goods.NewReleaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * desc :
 * author：xiedong
 * date：2020/03/03
 */
public class GoodsFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.iv_new_release})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_new_release:
                startActivity(new Intent(actitity, NewReleaseActivity.class));
                break;
        }
    }
}
