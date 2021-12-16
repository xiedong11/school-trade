package com.zhuandian.trade.entity;

import cn.bmob.v3.BmobObject;

/**
 * desc :
 *
 * date：
 */
public class GoodsCommentEntity extends BmobObject {
    private String content;
    private UserEntity userEntity;
    private GoodsEntity goodsEntity;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public GoodsEntity getGoodsEntity() {
        return goodsEntity;
    }

    public void setGoodsEntity(GoodsEntity goodsEntity) {
        this.goodsEntity = goodsEntity;
    }
}
