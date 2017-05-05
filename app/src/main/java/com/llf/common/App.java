package com.llf.common;

import android.os.Build;
import android.os.StrictMode;

import com.llf.basemodel.base.BaseApplication;

/**
 * Created by llf on 2017/3/10.
 */

public class App extends BaseApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            builder.detectFileUriExposure();
        }
    }
}
