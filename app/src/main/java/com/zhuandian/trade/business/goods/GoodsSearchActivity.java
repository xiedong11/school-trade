package com.zhuandian.trade.business.goods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class GoodsSearchActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.brv_list)
    BaseRecyclerView brvList;
    private List<GoodsEntity> mDatas = new ArrayList<>();
    private GoodsAdapter goodsAdapter;
    private int currentCount = -10;
    private String keyWord;
    private boolean sortByPriceDown = true;
    private boolean sortByTimeDown = true;
    private int sortType = 1; //1,时间，2，价格


    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_search;
    }

    @Override
    protected void setUpView() {
        keyWord = getIntent().getStringExtra("keyWord");
        tvTitle.setText("搜索结果");
        goodsAdapter = new GoodsAdapter(this, mDatas);
        brvList.setRecyclerViewAdapter(goodsAdapter);
        goodsAdapter.setItemClickListener(new GoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GoodsEntity entity) {
                Intent intent = new Intent(GoodsSearchActivity.this, GoodsItemActivity.class);
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
                refreshList();

            }
        });
        brvList.setLoadMoreListener(new BaseRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadDatas();
            }
        });


    }

    private void refreshList() {
        currentCount = -10; //重新置位
        mDatas.clear();
        goodsAdapter.notifyDataSetChanged();
        loadDatas();
    }


    private void loadDatas() {
        currentCount = currentCount + 10;
        BmobQuery<GoodsEntity> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        String sortByTime = sortByTimeDown ? "-createdAt" : "createdAt";
        String sortByPrice = sortByPriceDown ? "goodsPrice" : "-goodsPrice";
        query.order(sortType == 1 ? sortByTime : sortByPrice);
        query.setLimit(10);
        query.include("goodsOwner");// 查出发布人信息
        query.addWhereEqualTo("goodsLocal", BmobUser.getCurrentUser(UserEntity.class).getUserSchool());
        query.setSkip(currentCount);
        //这里查询规则有点绕，备注一下，如果userEntity不为null代表需要检索指定用户发布的二手商品信息，如果为null则检索全部
        //如果userEntity为当前登录用户，则需要有对商品下架的权限，否则只有查看权限
        query.findObjects(new FindListener<GoodsEntity>() {
            @Override
            public void done(List<GoodsEntity> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getGoodsTitle().contains(keyWord)) {
                            mDatas.add(list.get(i));
                        }
                    }
                    goodsAdapter.notifyDataSetChanged();
                    brvList.setRefreshLayoutState(false);
                } else {
                    brvList.setRefreshLayoutState(false);
                }
            }
        });
    }


    @OnClick({R.id.iv_back, R.id.tv_sort_time, R.id.tv_sort_price})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_sort_price:
                sortType = 2;
                sortByPriceDown = !sortByPriceDown;
                refreshList();
                break;
            case R.id.tv_sort_time:
                sortType = 1;
                sortByTimeDown = !sortByTimeDown;
                refreshList();
                break;

        }

    }
}
