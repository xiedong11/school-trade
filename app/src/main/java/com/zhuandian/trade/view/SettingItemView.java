package com.zhuandian.trade.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zhuandian.trade.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingItemView extends RelativeLayout implements View.OnClickListener {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.right_switch)
    Switch rightSwitch;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.v_under_line)
    View vUnderLine;
    @BindView(R.id.ll_click_span)
    LinearLayout llClickSpan;
    private Context context;
    private String leftTextStr;
    private String rightTextStr;
    private static final int TYPE_TEXT = 0;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_SWITCH = 2;
    private int rightType;
    private ItemClickListener clickListener;
    private OnSwitchChangeListener switchChangeListener;
    private int rightTextColor;
    private boolean isShowUnderLine = true;


    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
        leftTextStr = typedArray.getString(R.styleable.SettingItemView_left_text);
        rightTextStr = typedArray.getString(R.styleable.SettingItemView_right_text);
        rightTextColor = typedArray.getColor(R.styleable.SettingItemView_right_text_color, getResources().getColor(R.color.bar_grey));
        rightType = typedArray.getInt(R.styleable.SettingItemView_right_type, 0);
        isShowUnderLine = typedArray.getBoolean(R.styleable.SettingItemView_is_show_under_line, true);
        typedArray.recycle();

        initView();
    }

    private void initView() {
        ButterKnife.bind(this, LayoutInflater.from(context).inflate(R.layout.view_setting_item, this));
        tvLeft.setText(leftTextStr);
        tvRight.setText(rightTextStr);
        tvRight.setTextColor(rightTextColor);
        vUnderLine.setVisibility(isShowUnderLine ? VISIBLE : INVISIBLE);

        switch (rightType) {
            case TYPE_TEXT:
                tvRight.setVisibility(VISIBLE);
                ivRight.setVisibility(GONE);
                rightSwitch.setVisibility(GONE);
                break;
            case TYPE_IMAGE:
                tvRight.setVisibility(GONE);
                ivRight.setVisibility(VISIBLE);
                rightSwitch.setVisibility(GONE);
                break;
            case TYPE_SWITCH:
                tvRight.setVisibility(GONE);
                ivRight.setVisibility(GONE);
                rightSwitch.setVisibility(VISIBLE);
                break;
        }
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        if (itemClickListener != null) {
            llClickSpan.setOnClickListener(this);
            this.clickListener = itemClickListener;
        }

    }

    public void setRightText(String text) {
        tvRight.setText(text);
    }

    public String getRightText() {
        return tvRight.getText().toString();
    }

    public void setRightTextColor(int color) {
        tvRight.setTextColor(color);
    }

    @Override
    public void onClick(View view) {
        if (clickListener != null) {
            clickListener.itemClick();
        }
    }

    public interface ItemClickListener {
        void itemClick();
    }


    public void setOnSwitchChangeListener(final OnSwitchChangeListener onSwitchChangeListener) {
        if (onSwitchChangeListener != null) {
            rightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    onSwitchChangeListener.isCheck(isChecked);
                }
            });
        }
    }

    public void setSwitchState(boolean isChecked) {
        rightSwitch.setChecked(isChecked);
    }

    public interface OnSwitchChangeListener {
        void isCheck(boolean isChecked);
    }
}
