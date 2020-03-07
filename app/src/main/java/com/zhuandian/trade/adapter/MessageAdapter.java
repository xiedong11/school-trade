package com.zhuandian.trade.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zhuandian.base.BaseAdapter;
import com.zhuandian.base.BaseViewHolder;
import com.zhuandian.trade.R;
import com.zhuandian.trade.business.MessageItemAcitivity;
import com.zhuandian.trade.entity.MessageEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * desc :
 * author：xiedong
 * date：2020/03/07
 */
public class MessageAdapter extends BaseAdapter<MessageEntity, BaseViewHolder> {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_type)
    TextView tvType;

    public MessageAdapter(List<MessageEntity> mDatas, Context context) {
        super(mDatas, context);
    }

    @Override
    protected void converData(BaseViewHolder myViewHolder, MessageEntity messageEntity, int position) {
        ButterKnife.bind(this, myViewHolder.itemView);
        tvTitle.setText(messageEntity.getTitle());
        tvContent.setText(messageEntity.getContent());
        tvTime.setText(messageEntity.getCreatedAt());
        switch (messageEntity.getType()) {
            case 1:
                tvType.setText("失物招领");
                break;
            case 2:
                tvType.setText("寻物启事");
                break;
            case 3:
                tvType.setText("系统消息");
                break;
        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageItemAcitivity.class);
                intent.putExtra("message_entity", messageEntity);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_message;
    }

}
