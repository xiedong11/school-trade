package com.zhuandian.trade.business.tab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhuandian.base.BaseFragment;
import com.zhuandian.trade.R;
import com.zhuandian.trade.adapter.GoodsAdapter;
import com.zhuandian.trade.business.goods.GoodsFilterActivity;
import com.zhuandian.trade.business.goods.GoodsItemActivity;
import com.zhuandian.trade.business.goods.GoodsSearchActivity;
import com.zhuandian.trade.entity.GoodsEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * desc :
 * author：xiedong
 * date：2020/03/03
 */
public class HomeFragment extends BaseFragment {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.et_key_word)
    EditText etKeyWord;
    private List<GoodsEntity> mDatas = new ArrayList<>();
    private GoodsAdapter goodsAdapter;
    List<String> images = new ArrayList<String>() {
        {
            add("http://img.zcool.cn/community/0114a856640b6d32f87545731c076a.jpg");
            add("http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg");
            add("http://img.zcool.cn/community/018fdb56e1428632f875520f7b67cb.jpg");
            add("http://img.zcool.cn/community/01c8dc56e1428e6ac72531cbaa5f2c.jpg");
        }
    };

    //设置图片标题:自动对应
    List<String> titles = new ArrayList<String>() {
        {
            add("十大星级品牌联盟");
            add("全场2折起");
            add("嗨购5折不要停");
            add("双12趁现在");
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        initBanner();
        goodsAdapter = new GoodsAdapter(actitity, mDatas);
        rvList.setAdapter(goodsAdapter);
        rvList.setLayoutManager(new LinearLayoutManager(actitity));
        goodsAdapter.setItemClickListener(new GoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GoodsEntity entity) {
                Intent intent = new Intent(actitity, GoodsItemActivity.class);
                intent.putExtra("goods_entity", entity);
                startActivity(intent);
            }
        });
    }

    private void initBanner() {
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(actitity).load((String) path).into(imageView);
            }
        });
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);//设置圆形指示器与标题
        banner.setIndicatorGravity(BannerConfig.RIGHT);//设置指示器位置
        banner.setDelayTime(3000);//设置轮播时间
        banner.setImages(images);//设置图片源
        banner.setBannerTitles(titles);//设置标题源
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //Banner 点击事件自己处理
            }
        });

        initList();
    }

    private void initList() {
        SharedPreferences sharedPreferences = actitity.getSharedPreferences("goods", Context.MODE_PRIVATE);
        int likeType = sharedPreferences.getInt("goods_type", 0);
        BmobQuery<GoodsEntity> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.order("-updatedAt");
        query.setLimit(5);
        query.addWhereEqualTo("goodsType", likeType);
        query.findObjects(new FindListener<GoodsEntity>() {
            @Override
            public void done(List<GoodsEntity> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        mDatas.add(list.get(i));
                    }
                    goodsAdapter.notifyDataSetChanged();

                } else {
                }
            }
        });
    }

    @OnClick({R.id.ll_music, R.id.ll_life, R.id.ll_book, R.id.ll_makeup, R.id.ll_other, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_music:
                go2GoodsFilterActivity(2, "电子设备");
                break;
            case R.id.ll_life:
                go2GoodsFilterActivity(0, "日常用品");
                break;
            case R.id.ll_book:
                go2GoodsFilterActivity(3, "资料书籍");
                break;
            case R.id.ll_makeup:
                go2GoodsFilterActivity(4, "美妆洗化");
                break;
            case R.id.ll_other:
                go2GoodsFilterActivity(1, "杂七杂八");
                break;
            case R.id.tv_sure:
                Intent intent = new Intent(actitity, GoodsSearchActivity.class);
                intent.putExtra("keyWord", etKeyWord.getText().toString());
                startActivity(intent);
                break;
        }
    }

    private void go2GoodsFilterActivity(int goodsType, String title) {
        Intent intent = new Intent(actitity, GoodsFilterActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("goodsType", goodsType);
        startActivity(intent);
    }
}
