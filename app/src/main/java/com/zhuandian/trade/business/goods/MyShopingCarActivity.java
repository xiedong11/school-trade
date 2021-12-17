package com.zhuandian.trade.business.goods;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
import cn.bmob.v3.listener.UpdateListener;

/**
 * desc :购物车
 *
 * date：
 */
public class MyShopingCarActivity extends BaseActivity {

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
        return R.layout.activity_my_shoping_car;
    }

    @Override
    protected void setUpView() {
        tvTitle.setText("购物车");
        goodsAdapter = new GoodsAdapter(this, mDatas);
        brvList.setRecyclerViewAdapter(goodsAdapter);
        goodsAdapter.setItemClickListener(new GoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GoodsEntity entity) {
                Intent intent = new Intent(MyShopingCarActivity.this, GoodsItemActivity.class);
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
        Set<String> collection = sharedPreferences.getStringSet("shop_car", new HashSet<>());
        BmobQuery<GoodsEntity> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
//        query.addWhereEqualTo("goodsLocal", BmobUser.getCurrentUser(UserEntity.class).getUserSchool());
        query.order("-updatedAt");
        query.setLimit(10);
        query.include("goodsOwner");// 查出发布人信息
        query.addWhereContainedIn("objectId", collection);
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


    @OnClick({R.id.iv_back, R.id.tv_total})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.tv_total:
                calcTotalPrice();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void calcTotalPrice() {
        SharedPreferences sharedPreferences = getSharedPreferences("goods", Context.MODE_PRIVATE);
        Set<String> collection = sharedPreferences.getStringSet("trade", new HashSet<>()); //存储我交易过的记录
        int sum = 0;
        for (GoodsEntity goodsEntity : mDatas) {
            collection.add(goodsEntity.getObjectId());
            goodsEntity.setTradeState(GoodsEntity.GOODS_STATE_ON_TRADE);
            goodsEntity.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    goodsAdapter.notifyDataSetChanged();
                }
            });
            sum += Integer.parseInt(goodsEntity.getGoodsPrice());
        }
        sharedPreferences.edit().putStringSet("trade",collection).commit();
        new AlertDialog.Builder(this)
                .setTitle("交易中...")
                .setMessage("您一共消费" + sum + "元\n请等待卖家确认\n并在线下完成交易\n卖家联系方式："+mDatas.get(0).getGoodsOwner().getMobilePhoneNumber())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                }).show();
    }
}
