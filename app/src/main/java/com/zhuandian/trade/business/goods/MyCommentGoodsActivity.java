package com.zhuandian.trade.business.goods;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuandian.base.BaseActivity;
import com.zhuandian.trade.R;
import com.zhuandian.trade.adapter.GoodsAdapter;
import com.zhuandian.trade.entity.GoodsCommentEntity;
import com.zhuandian.trade.entity.GoodsEntity;
import com.zhuandian.trade.entity.UserEntity;
import com.zhuandian.trade.utils.BaseRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * desc :我评论的
 *
 * date：
 */
public class MyCommentGoodsActivity extends BaseActivity {


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
        return R.layout.activity_my_release;
    }

    @Override
    protected void setUpView() {
        tvTitle.setText("我评论的");
        goodsAdapter = new GoodsAdapter(this, mDatas);
        brvList.setRecyclerViewAdapter(goodsAdapter);
        goodsAdapter.setItemClickListener(new GoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GoodsEntity entity) {
                Intent intent = new Intent(MyCommentGoodsActivity.this, GoodsItemActivity.class);
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
        BmobQuery<GoodsCommentEntity> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
        query.order("-updatedAt");
        query.include("userEntity,goodsEntity.goodsOwner");
        query.setLimit(10);
        query.setSkip(currentCount);
//        query.include("userEntity,goodsEntity.userEntity");
        query.findObjects(new FindListener<GoodsCommentEntity>() {
            @Override
            public void done(List<GoodsCommentEntity> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                            if (BmobUser.getCurrentUser(UserEntity.class).getObjectId().equals(list.get(i).getUserEntity().getObjectId())) {
                            mDatas.add(list.get(i).getGoodsEntity());
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


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
