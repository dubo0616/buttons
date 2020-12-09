package com.gaia.button;

import android.app.Application;


public class GaiaApplication extends Application {
    static GaiaApplication s_ins;
    @Override
    public void onCreate() {
        super.onCreate();
//        NoHttp.initialize(this);
    }
    public static GaiaApplication getInstance() {
        return s_ins;
    }

    public GaiaApplication() {
        s_ins = this;
    }
}
