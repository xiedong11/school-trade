package com.zhuandian.trade.business.tab;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.zhuandian.base.BaseFragment;
import com.zhuandian.trade.R;
import com.zhuandian.trade.adapter.GoodsAdapter;
import com.zhuandian.trade.business.goods.NewReleaseActivity;
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

/**
 * desc :
 * author：xiedong
 * date：2020/03/03
 */
public class GoodsFragment extends BaseFragment {
    @BindView(R.id.brv_list)
    BaseRecyclerView brvGoodsList;

    private List<GoodsEntity> mDatas = new ArrayList<>();
    private GoodsAdapter goodsAdapter;
    public static final int REQUEST_RELEASE_GOODS = 1;
    private boolean isCurrentUser;//是否为当前用户
    private UserEntity userEntity;
    private int currentCount = -10;
    private LinearLayoutManager linearLayoutManager;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods;
    }

    @Override
    protected void initView() {
        userEntity = (UserEntity) actitity.getIntent().getSerializableExtra("user_entity");
        if (userEntity != null) {
            isCurrentUser = (userEntity.getObjectId().equals(BmobUser.getCurrentUser(UserEntity.class).getObjectId()));
        }
        goodsAdapter = new GoodsAdapter(actitity, mDatas);
        //fix bug 在此不能重新定义layoutmanager，会影响抽象基类中的上拉加载更多回调
//        linearLayoutManager = new LinearLayoutManager(actitity);
//        brvGoodsList.setRecyclerViewLayoutManager(linearLayoutManager);
        brvGoodsList.setRecyclerViewAdapter(goodsAdapter);
        goodsAdapter.setItemClickListener(new GoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GoodsEntity entity) {
//                Intent intent = new Intent(actitity, GoodsItemActivity.class);
//                intent.putExtra(GlobalVariable.GOODS_ENTIT, entity);
//                startActivity(intent);
            }
        });
        loadDatas();
        initRefreshListener();
    }
    private void initRefreshListener() {
        brvGoodsList.setRefreshListener(new BaseRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentCount = -10; //重新置位
                mDatas.clear();
                goodsAdapter.notifyDataSetChanged();
                loadDatas();

            }
        });
        brvGoodsList.setLoadMoreListener(new BaseRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadDatas();
            }
        });


    }



    private void loadDatas() {
        currentCount = currentCount + 10;
        BmobQuery<GoodsEntity> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.order("-updatedAt");
        query.include("goodsOwner");// 查出发布人信息
        query.setLimit(10);
        query.setSkip(currentCount);
        //这里查询规则有点绕，备注一下，如果userEntity不为null代表需要检索指定用户发布的二手商品信息，如果为null则检索全部
        //如果userEntity为当前登录用户，则需要有对商品下架的权限，否则只有查看权限
        if (userEntity != null) {
            query.addWhereEqualTo("goodsOwner", userEntity);
        }
        query.findObjects(new FindListener<GoodsEntity>() {
            @Override
            public void done(List<GoodsEntity> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        mDatas.add(list.get(i));
                    }
                    goodsAdapter.notifyDataSetChanged();
                    brvGoodsList.setRefreshLayoutState(false);
                } else {
                    brvGoodsList.setRefreshLayoutState(false);
                }
            }
        });
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
