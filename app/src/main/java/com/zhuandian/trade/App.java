package com.zhuandian.trade;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * desc :
 *
 * date：
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "1ae0b48041931e180f74a51c5b6e2eac");
    }
}
