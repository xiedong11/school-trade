package com.zhuandian.trade.entity;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * desc :
 * author：xiedong
 * date：2020/03/05
 */
public class GoodsEntity extends BmobObject implements Serializable {
    public static final int GOODS_TYPYE_DAILY_USE = 0; //日常用品
    public static final int GOODS_TYPYE_OTHER_USE = 1;//杂七杂八
    public static final int GOODS_TYPYE_ELECTRONICE = 2;//电子产品
    public static final int GOODS_TYPYE_BOOK = 3;//资料书籍
    public static final int GOODS_TYPYE_COSMETICE = 4;//美妆洗化
    public static final int TRADE_TYPE_BARGAIN = 1; //可议价
    public static final int TRADE_TYPE_NO_BARGAIN = 2; //不可议价
    public static final int GOODS_STATE_SOLD_OUT = 3; //商品下架
    public static final int GOODS_STATE_ON_SALE = 4; //商品在售
    private List<String> goodsUrl;
    private String goodsTitle;
    private String goodsContent;
    private String goodsPrice;
    private String ownerContactNum;
    private UserEntity goodsOwner;
    private BmobRelation collections; //收藏
    private int tradeType;  //交易类型
    private int goodsType;

    public BmobRelation getCollections() {
        return collections;
    }

    public void setCollections(BmobRelation collections) {
        this.collections = collections;
    }

    public int getTradeState() {
        return tradeState;
    }

    public void setTradeState(int tradeState) {
        this.tradeState = tradeState;
    }

    private int tradeState;


    public List<String> getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(List<String> goodsUrl) {
        this.goodsUrl = goodsUrl;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsContent() {
        return goodsContent;
    }

    public void setGoodsContent(String goodsContent) {
        this.goodsContent = goodsContent;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getOwnerContactNum() {
        return ownerContactNum;
    }

    public void setOwnerContactNum(String ownerContactNum) {
        this.ownerContactNum = ownerContactNum;
    }

    public UserEntity getGoodsOwner() {
        return goodsOwner;
    }

    public void setGoodsOwner(UserEntity goodsOwner) {
        this.goodsOwner = goodsOwner;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }
}
