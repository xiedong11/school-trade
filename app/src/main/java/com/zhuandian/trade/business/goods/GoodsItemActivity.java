package com.zhuandian.trade.business.goods;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.zhuandian.base.BaseActivity;
import com.zhuandian.trade.R;
import com.zhuandian.trade.adapter.GoodsCommentAdapter;
import com.zhuandian.trade.entity.GoodsCommentEntity;
import com.zhuandian.trade.entity.GoodsEntity;
import com.zhuandian.trade.entity.UserEntity;
import com.zhuandian.trade.utils.MyUtils;
import com.zhuandian.view.CircleImageView;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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
    @BindView(R.id.tv_goods_content)
    TextView tvGoodsContent;
    @BindView(R.id.iv_goods_sold_out)
    ImageView ivGoodsSoldOut;
    @BindView(R.id.comment_listview)
    RecyclerView commentRecyclewView;
    @BindView(R.id.comment_content)
    EditText commentContent;
    @BindView(R.id.submit_comment)
    TextView submitComment;
    @BindView(R.id.submit_comment_layout)
    LinearLayout submitCommentLayout;
    @BindView(R.id.fab)
    FloatingActionButton fabButton;
    @BindView(R.id.tv_likes)
    TextView tvCollections;
    private GoodsEntity goodsEntity;
    private List<GoodsCommentEntity> commentDatas = new ArrayList<>();
    private GoodsCommentAdapter userCommentAdapter;
    private boolean LIKES_FLAG = true; //收藏记录标志位
    private SharedPreferences sharedPreferences;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_item;
    }


    @Override
    public void setUpView() {
        goodsEntity = (GoodsEntity) getIntent().getSerializableExtra("goods_entity");
        if (goodsEntity.getGoodsOwner().getObjectId().equals(BmobUser.getCurrentUser(UserEntity.class).getObjectId()) && goodsEntity.getTradeState() == GoodsEntity.GOODS_STATE_ON_TRADE) {
            new AlertDialog.Builder(this)
                    .setTitle("交易中")
                    .setMessage("当前有买家正在购买此商品，是否同意出售")
                    .setPositiveButton("同意出售", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            goodsEntity.delete(goodsEntity.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    dialog.dismiss();
                                    Toast.makeText(GoodsItemActivity.this, "交易完成，当前商品已经卖出", Toast.LENGTH_SHORT).show();
                                    GoodsItemActivity.this.finish();
                                }
                            });
                        }
                    }).setNegativeButton("暂不出售", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
        tvTitle.setText("商品详情");
        sharedPreferences = getSharedPreferences("goods", Context.MODE_PRIVATE);
        tvUserName.setText(goodsEntity.getGoodsOwner().getUsername());
        tvGoodsPrice.setText(String.format("￥%s", goodsEntity.getGoodsPrice()));
        ivGoodsSoldOut.setVisibility(goodsEntity.getTradeState() == GoodsEntity.GOODS_STATE_SOLD_OUT ? View.VISIBLE : View.GONE);
        tvGoodsContent.setText(goodsEntity.getGoodsContent());
        formatTime(goodsEntity.getCreatedAt(), tvReleasTime);
        tvIsBargain.setText(goodsEntity.getTradeType() == GoodsEntity.TRADE_TYPE_BARGAIN ? "可议价" : "一口价");
        Glide.with(this).load(goodsEntity.getGoodsOwner().getHeadImgUrl()).into(civHeader);
        initGoodsImg(goodsEntity.getGoodsUrl());
        userCommentAdapter = new GoodsCommentAdapter(commentDatas, this);
        commentRecyclewView.setAdapter(userCommentAdapter);
        commentRecyclewView.setLayoutManager(new LinearLayoutManager(this));
        getAllUserComment();  //得到所有参与该动态评论的用户信息和内容

        // 更新猜你喜欢商品tpye
        sharedPreferences.edit().putInt("goods_type", goodsEntity.getGoodsType()).commit();
    }


    private void initGoodsImg(final List<String> goodsUrl) {
        llImgContainer.removeAllViews();
        for (int i = 0; i < goodsUrl.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            imageView.setPadding(0, 10, 0, 0);
            Glide.with(this).load(goodsUrl.get(i)).into(imageView);
            llImgContainer.addView(imageView);
        }
    }


    @OnClick({R.id.civ_header, R.id.iv_back, R.id.submit_comment, R.id.fab, R.id.tv_likes, R.id.tv_add_car})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.civ_header:
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.submit_comment:  //提交评论
                submitUserComment();
                break;
            case R.id.fab:  //新建评论
                commentRecyclewView.setVisibility(View.GONE);
                submitCommentLayout.setVisibility(View.VISIBLE);
                fabButton.setVisibility(View.GONE);
                break;
            case R.id.tv_likes:
                addCollection();
                break;
            case R.id.tv_add_car:
                addShopingCar();
                break;
        }
    }

    /**
     * 加入购物车
     */
    private void addShopingCar() {
        if (goodsEntity.getTradeState() != GoodsEntity.GOODS_STATE_ON_SALE) {
            Toast.makeText(this, "当前商品已下架或正在交易中", Toast.LENGTH_SHORT).show();
            return;
        }

        Set<String> collection = sharedPreferences.getStringSet("shop_car", new HashSet<>());
        if (collection.contains(goodsEntity.getObjectId())) {
            collection.remove(goodsEntity.getObjectId());
            Toast.makeText(this, "移除成功", Toast.LENGTH_SHORT).show();
        } else {
            collection.add(goodsEntity.getObjectId());
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
        }

        sharedPreferences.edit().putStringSet("shop_car", collection).commit();
    }

    /**
     * 添加收藏记录
     */
    private void addCollection() {
        Set<String> collection = sharedPreferences.getStringSet("collection", new HashSet<>());
        if (collection.contains(goodsEntity.getObjectId())) {
            collection.remove(goodsEntity.getObjectId());
            Toast.makeText(this, "移除成功", Toast.LENGTH_SHORT).show();
        } else {
            collection.add(goodsEntity.getObjectId());
            Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
        }

        sharedPreferences.edit().putStringSet("collection", collection).commit();
    }

    private void submitUserComment() {
        String userComment = commentContent.getText().toString();  //得到用户输入框的评论内容
        if (TextUtils.isEmpty(userComment)) {
            Snackbar.make(tvTitle, "评论内容不允许为空...", Snackbar.LENGTH_LONG).show();
        } else {

            UserEntity user = BmobUser.getCurrentUser(UserEntity.class);  //得到当前用户
            GoodsEntity post = new GoodsEntity();   //当前动态内容
            post.setObjectId(goodsEntity.getObjectId());  //得到当前的动态的id，与评论建立关联
            final GoodsCommentEntity commentEntity = new GoodsCommentEntity();
            commentEntity.setContent(userComment);
            commentEntity.setUserEntity(user);
            commentEntity.setGoodsEntity(goodsEntity);
            commentEntity.save(new SaveListener<String>() {
                @SuppressLint("RestrictedApi")
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        Snackbar.make(tvTitle, "评论成功", Snackbar.LENGTH_LONG).show();
                        commentContent.setText(""); //清空输入框，防止用户二次评论时影响用户体验
                        commentRecyclewView.setVisibility(View.VISIBLE);
                        submitCommentLayout.setVisibility(View.GONE);
                        fabButton.setVisibility(View.VISIBLE);

                        getAllUserComment();  //重新加载一遍数据
                    } else {
                        Log.e("评论失败", "-----");
                    }
                }

            });
        }
    }


    /**
     * 绑定当前动态下的所有评论信息
     */
    private void getAllUserComment() {
        BmobQuery<GoodsCommentEntity> query = new BmobQuery<GoodsCommentEntity>();
        //用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        GoodsEntity post = new GoodsEntity();
        post.setObjectId(goodsEntity.getObjectId());   //得到当前动态的Id号，
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.order("updatedAt");
        query.addWhereEqualTo("goodsEntity", new BmobPointer(post));
        //希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.include("userEntity,goodsEntity.userEntity");
        query.findObjects(new FindListener<GoodsCommentEntity>() {
            @Override
            public void done(List<GoodsCommentEntity> objects, BmobException e) {
                if (e == null) {
                    //插入数据，通知列表更新
//                    commentDatas = objects;  直接赋值，因为前后绑定的apapter对应的list地址不是同一个，所以 notifyDataSetChanged无效，
                    commentDatas.clear();
                    commentDatas.addAll(objects); //把数据添加进集合
                    userCommentAdapter.notifyDataSetChanged();
                } else {
                    Log.e("查询数据失败", "tag");
                }
            }
        });


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
