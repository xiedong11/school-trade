package com.zhuandian.trade.entity;

import cn.bmob.v3.BmobUser;

/**
 * desc :
 *
 *
 */
public class UserEntity extends BmobUser {
    private String nikeName;
    private String userInfo;
    private String headImgUrl;
    private String userSchool;

    public String getUserSchool() {
        return userSchool;
    }

    public void setUserSchool(String userSchool) {
        this.userSchool = userSchool;
    }

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
