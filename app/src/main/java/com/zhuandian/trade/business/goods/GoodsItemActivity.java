package com.zhuandian.trade.business.goods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhuandian.base.BaseActivity;
import com.zhuandian.trade.R;
import com.zhuandian.trade.entity.GoodsEntity;
import com.zhuandian.trade.entity.UserEntity;
import com.zhuandian.trade.utils.MyUtils;
import com.zhuandian.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GoodsItemActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.civ_header)
    CircleImageView civHeader;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_releas_time)
    TextView tvReleasTime;
    @BindView(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @BindView(R.id.tv_is_bargain)
    TextView tvIsBargain;
    @BindView(R.id.ll_img_container)
    LinearLayout llImgContainer;
    @BindView(R.id.tv_favorite)
    TextView tvFavorite;
    @BindView(R.id.tv_chat)
    TextView tvBuy;
    @BindView(R.id.tv_goods_content)
    TextView tvGoodsContent;
    @BindView(R.id.tv_personal_data)
    TextView tvPersonalData;
    @BindView(R.id.iv_goods_sold_out)
    ImageView ivGoodsSoldOut;
    private GoodsEntity goodsEntity;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_item;
    }


    @Override
    public void setUpView() {
        goodsEntity = (GoodsEntity) getIntent().getSerializableExtra("goods_entity");
        tvTitle.setText("商品详情");
        tvUserName.setText(goodsEntity.getGoodsOwner().getUsername());
        tvGoodsPrice.setText(String.format("￥%s", goodsEntity.getGoodsPrice()));
        ivGoodsSoldOut.setVisibility(goodsEntity.getTradeState() == GoodsEntity.GOODS_STATE_SOLD_OUT ? View.VISIBLE : View.GONE);
        tvGoodsContent.setText(goodsEntity.getGoodsContent());
        formatTime(goodsEntity.getCreatedAt(), tvReleasTime);
        tvIsBargain.setText(goodsEntity.getTradeType() == GoodsEntity.TRADE_TYPE_BARGAIN ? "可议价" : "一口价");
        Glide.with(this).load(goodsEntity.getGoodsOwner().getHeadImgUrl()).into(civHeader);
        initGoodsImg(goodsEntity.getGoodsUrl());

    }

    private void initGoodsImg(final List<String> goodsUrl) {
        llImgContainer.removeAllViews();
        for (int i = 0; i < goodsUrl.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            imageView.setPadding(0, 10, 0, 0);
            Glide.with(this).load(goodsUrl.get(i)).into(imageView);
            final String currentImgUrl = goodsUrl.get(i);
            int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    PreviewPictureActivity.showPicture(GoodsItemActivity.this, goodsUrl, finalI);
                }
            });
            llImgContainer.addView(imageView);
        }
    }




    @OnClick({R.id.tv_favorite, R.id.tv_chat, R.id.tv_personal_data, R.id.civ_header})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_favorite:
                break;
            case R.id.tv_chat:
                break;
            case R.id.civ_header:
            case R.id.tv_personal_data:
//                if (goodsEntity.getGoodsOwner().getObjectId() != null) {
//                    showPersonalDataPage(goodsEntity.getGoodsOwner());
//                } else {
//                    Toast.makeText(actitity, "用户信息不明确,无法查看...", Toast.LENGTH_SHORT).show();
//                }

                break;

//            case R.id.iv_right_image:
//                ShareUtils.shareCommon(actitity,
//                        RouteUtils.SHARE_GOODS_DETAIL + goodsEntity.getObjectId(),
//                        "我在跳蚤市场新发布宝贝啦",
//                        goodsEntity.getGoodsContent(),
//                        goodsEntity.getGoodsTitle()
//                );
//                break;
        }
    }





    /**
     * 格式化商品发布时间
     *
     * @param createdAt
     * @param textView
     */
    private void formatTime(String createdAt, TextView textView) {
        String createtTime[] = createdAt.split(" ");
        String currentTime[] = MyUtils.currentTime().split(" ");
        //判断创建时间跟当前时间是否同一天，是，只显示时间，不是，显示创建的日期，不显示时间
        if (createtTime[0].equals(currentTime[0])) {
            String createtTime1[] = createtTime[1].split(":");
            textView.setText("今天 " + createtTime1[0] + ":" + createtTime1[1]);
        } else {
            String createtTime1[] = createtTime[0].split("-");  //正则切割月份
            String createtTime2[] = createtTime[1].split(":");  //正则切割时间
            textView.setText(createtTime1[1] + "/" + createtTime1[2] + " " + createtTime2[0] + ":" + createtTime2[1]);
        }
    }


}
