package com.zhuandian.trade.business.goods;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuandian.base.BaseActivity;
import com.zhuandian.trade.R;
import com.zhuandian.trade.adapter.GoodsAdapter;
import com.zhuandian.trade.entity.GoodsEntity;
import com.zhuandian.trade.entity.UserEntity;
import com.zhuandian.trade.utils.BaseRecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * desc :我交易过的
 * author：xiedong
 * date：2020/03/08
 */
public class MyTradeAcitivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.brv_list)
    BaseRecyclerView brvList;
    private List<GoodsEntity> mDatas = new ArrayList<>();
    private GoodsAdapter goodsAdapter;
    public static final int REQUEST_RELEASE_GOODS = 1;
    private UserEntity userEntity;
    private int currentCount = -10;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_trade_acitivity;
    }

    @Override
    protected void setUpView() {
        tvTitle.setText("我交易过的");
        goodsAdapter = new GoodsAdapter(this, mDatas);
        brvList.setRecyclerViewAdapter(goodsAdapter);
        goodsAdapter.setItemClickListener(new GoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GoodsEntity entity) {
                Intent intent = new Intent(MyTradeAcitivity.this, GoodsItemActivity.class);
                intent.putExtra("goods_entity", entity);
                startActivity(intent);
            }
        });
        loadDatas();
        initRefreshListener();
    }

    private void initRefreshListener() {
        brvList.setRefreshListener(new BaseRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentCount = -10; //重新置位
                mDatas.clear();
                goodsAdapter.notifyDataSetChanged();
                loadDatas();

            }
        });
        brvList.setLoadMoreListener(new BaseRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadDatas();
            }
        });


    }


    private void loadDatas() {
        currentCount = currentCount + 10;
        SharedPreferences sharedPreferences = getSharedPreferences("goods", Context.MODE_PRIVATE);
        Set<String> collection = sharedPreferences.getStringSet("trade", new HashSet<>());
        BmobQuery<GoodsEntity> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.order("-updatedAt");
        query.setLimit(10);
        query.addWhereEqualTo("goodsLocal", BmobUser.getCurrentUser(UserEntity.class).getUserSchool());
        query.addWhereContainedIn("objectId",collection);
        query.setSkip(currentCount);
        //这里查询规则有点绕，备注一下，如果userEntity不为null代表需要检索指定用户发布的二手商品信息，如果为null则检索全部
        //如果userEntity为当前登录用户，则需要有对商品下架的权限，否则只有查看权限
        query.findObjects(new FindListener<GoodsEntity>() {
            @Override
            public void done(List<GoodsEntity> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        mDatas.add(list.get(i));
                    }
                    goodsAdapter.notifyDataSetChanged();
                    brvList.setRefreshLayoutState(false);
                } else {
                    brvList.setRefreshLayoutState(false);
                }
            }
        });
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
