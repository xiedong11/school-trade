package com.zhuandian.trade.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zhuandian.base.BaseAdapter;
import com.zhuandian.base.BaseViewHolder;
import com.zhuandian.trade.R;
import com.zhuandian.trade.entity.GoodsEntity;
import com.zhuandian.trade.entity.UserEntity;
import com.zhuandian.trade.utils.MyUtils;
import com.zhuandian.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * desc :
 * author：xiedong
 * date：2020/03/05
 */
public class GoodsAdapter extends BaseAdapter<GoodsEntity, BaseViewHolder> {
    @BindView(R.id.civ_header)
    CircleImageView civHeader;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.rv_goods_img)
    RecyclerView rvGoodsImg;
    @BindView(R.id.tv_goods_content)
    TextView tvGoodsContent;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @BindView(R.id.root_view)
    LinearLayout rootView;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_is_bargain)
    TextView tvBargain;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.iv_goods_sold_out)
    ImageView ivSoldOut;
    private LinearLayoutManager layoutManager;
    private OnItemClickListener itemClickListener;

    public GoodsAdapter(Context mContext, List<GoodsEntity> mData) {
        super(mData,mContext);
    }



    @Override
    public int getItemLayoutId() {
        return R.layout.item_goods_adapter;
    }

    @Override
    protected void converData(BaseViewHolder holder, final GoodsEntity goodsEntity, int position) {
        ButterKnife.bind(this, holder.itemView);
        tvGoodsContent.setText(goodsEntity.getGoodsContent());
        Glide.with(mContext).load(goodsEntity.getGoodsOwner().getHeadImgUrl()).into(civHeader);
        layoutManager = new LinearLayoutManager(mContext);
        tvUserName.setText(goodsEntity.getGoodsOwner().getUsername());
        tvGoodsName.setText(goodsEntity.getGoodsTitle());
        tvGoodsPrice.setText(String.format("￥%s", goodsEntity.getGoodsPrice()));
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvGoodsImg.setLayoutManager(layoutManager);
        tvBargain.setVisibility((goodsEntity.getTradeType() == GoodsEntity.TRADE_TYPE_BARGAIN && goodsEntity.getTradeState() == GoodsEntity.GOODS_STATE_ON_SALE) ? View.VISIBLE : View.GONE);
        ivSoldOut.setVisibility(goodsEntity.getTradeState() == GoodsEntity.GOODS_STATE_SOLD_OUT ? View.VISIBLE : View.GONE);
        RecyclerItemImgAdapter recyclerItemImgAdapter = new RecyclerItemImgAdapter(mContext, goodsEntity.getGoodsUrl());
        rvGoodsImg.setAdapter(recyclerItemImgAdapter);
        formatTime(goodsEntity.getCreatedAt());
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(goodsEntity);
                }
            }
        });
        UserEntity currentUser = BmobUser.getCurrentUser(UserEntity.class);

        ivDelete.setVisibility((currentUser.getObjectId() != null && currentUser.getObjectId().equals(goodsEntity.getGoodsOwner().getObjectId())) ? View.VISIBLE : View.GONE);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSoldOutGoods(goodsEntity);
            }
        });

    }

    /**
     * 确认是否下架商品
     *
     * @param goodsEntity
     */
    private void confirmSoldOutGoods(final GoodsEntity goodsEntity) {

        new AlertDialog.Builder(mContext)
                .setTitle("确定下架商品？")
                .setMessage("请确保商品已经成功交易或您暂时不想转手改商品!!")
                .setPositiveButton("下架商品", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        goodsEntity.setTradeState(GoodsEntity.GOODS_STATE_SOLD_OUT);
                        goodsEntity.update(goodsEntity.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                notifyDataSetChanged();
                            }
                        });
                    }
                }).setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }


    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }




    public interface OnItemClickListener {
        void onItemClick(GoodsEntity entity);
    }

    private void formatTime(String createdAt) {
        String createtTime[] = createdAt.split(" ");
        String currentTime[] = MyUtils.currentTime().split(" ");
        //判断创建时间跟当前时间是否同一天，是，只显示时间，不是，显示创建的日期，不显示时间
        if (createtTime[0].equals(currentTime[0])) {
            String createtTime1[] = createtTime[1].split(":");
            tvTime.setText("今天 " + createtTime1[0] + ":" + createtTime1[1]);
        } else {
            String createtTime1[] = createtTime[0].split("-");  //正则切割月份
            String createtTime2[] = createtTime[1].split(":");  //正则切割时间
            tvTime.setText(createtTime1[1] + "/" + createtTime1[2] + " " + createtTime2[0] + ":" + createtTime2[1]);
        }
    }
}
