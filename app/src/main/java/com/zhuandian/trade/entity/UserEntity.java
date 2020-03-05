package com.zhuandian.trade.entity;

import cn.bmob.v3.BmobUser;

/**
 * desc :
 * author：xiedong
 * date：2020/03/04
 */
public class UserEntity extends BmobUser {
    private String nikeName;
    private String userInfo;
    private String headImgUrl;

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }
}
