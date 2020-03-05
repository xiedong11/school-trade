package com.zhuandian.trade.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhuandian.base.BaseAdapter;
import com.zhuandian.base.BaseViewHolder;
import com.zhuandian.trade.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * desc :
 * author：xiedong
 * date：2020/03/05
 */
public class RecyclerItemImgAdapter extends BaseAdapter<String, BaseViewHolder> {
    @BindView(R.id.iv_goods_item)
    ImageView ivGoodsItem;

    public RecyclerItemImgAdapter(Context mContext, List<String> mData) {
        super(mData,mContext);
    }

    @Override
    protected void converData(BaseViewHolder myViewHolder, String s, int position) {
        ButterKnife.bind(this, myViewHolder.itemView);
        Glide.with(mContext).load(s).into(ivGoodsItem);

    }

    @Override
    public int getItemLayoutId() {
         return R.layout.item_recycler_img;
    }

}
