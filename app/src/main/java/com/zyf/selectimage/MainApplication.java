package com.zyf.selectimage;

import android.app.Application;
import android.content.Context;

import com.zyf.selectimage.util.FrescoUtil;

/**
 * Created by zyf on 2017/12/4.
 */

public class MainApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        FrescoUtil.initFresco(this.getApplicationContext());//在Application 初始化时，进行初始化Fresco

    }


}
