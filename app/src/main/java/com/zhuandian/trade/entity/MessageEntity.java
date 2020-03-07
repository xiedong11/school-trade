package com.zhuandian.trade.entity;

import cn.bmob.v3.BmobObject;

/**
 * desc :
 * author：xiedong
 * date：2020/03/07
 */
public class MessageEntity extends BmobObject {
    private String content;
    private String title;
    private int type;   //1.失误招领  2.寻物启事，3，系统消息

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
