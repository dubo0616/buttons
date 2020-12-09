package com.gaia.button;

import android.app.Application;

import com.gaia.button.net.RetrofitHelper;
import com.xcheng.retrofit.RetrofitFactory;

public class GaiaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitFactory.DEFAULT = RetrofitHelper.getInstance().getRetrofit();
    }
}
