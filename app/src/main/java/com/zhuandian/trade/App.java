package com.zhuandian.trade;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * desc :
 * author：xiedong
 * date：2020/03/03
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "e8d84029fd228ea10f2c332a807073f2");
    }
}
