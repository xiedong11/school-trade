package com.zhuandian.trade.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhuandian.base.BaseAdapter;
import com.zhuandian.base.BaseViewHolder;
import com.zhuandian.trade.R;
import com.zhuandian.trade.entity.GoodsCommentEntity;
import com.zhuandian.trade.entity.MessageCommentEntity;
import com.zhuandian.trade.utils.MyUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * desc :
 * author：xiedong
 * date：2020/03/07
 */
public class MessageCommentAdapter extends BaseAdapter<MessageCommentEntity, BaseViewHolder> {


    @BindView(R.id.iv_user_header)
    ImageView ivUserHeader;
    @BindView(R.id.name)
    TextView commenterName;
    @BindView(R.id.time)
    TextView tvCommentTime;
    @BindView(R.id.content)
    TextView tvContent;
    public MessageCommentAdapter(List<MessageCommentEntity> mDatas, Context context) {
        super(mDatas, context);
    }

    @Override
    protected void converData(BaseViewHolder holder, MessageCommentEntity comment, int position) {
        ButterKnife.bind(this, holder.itemView);
        String createtTime[] = comment.getCreatedAt().split(" ");
        String currentTime[] = MyUtils.currentTime().split(" ");


        //判断创建时间跟当前时间是否同一天，是，只显示时间，不是，显示创建的日期，不显示时间
        if (createtTime[0].equals(currentTime[0])) {
            String createtTime1[] = createtTime[1].split(":");
            tvCommentTime.setText("今天 " + createtTime1[0] + ":" + createtTime1[1]);
        } else {
            String createtTime1[] = createtTime[0].split("-");  //正则切割月份
            String createtTime2[] = createtTime[1].split(":");  //正则切割时间
            tvCommentTime.setText(createtTime1[1] + "/" + createtTime1[2] + " " + createtTime2[0] + ":" + createtTime2[1]);

        }
        tvContent.setText(comment.getContent());
        commenterName.setText(comment.getUserEntity().getUsername());   //设置评论者信息
        Glide.with(mContext).load(comment.getUserEntity().getHeadImgUrl()).into(ivUserHeader);

    }

    @Override
    public int getItemLayoutId() {
        return R.layout.comment_listview_item;
    }
}
