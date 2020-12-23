package com.gaia.button;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;


public class GaiaApplication extends Application {
    static GaiaApplication s_ins;
    public static final String WX_APPID = "wx543c45f9090e3794";
    public static final String WX_APPSECRET = "eebe35893d084c5210d586c789e30119";
    private ArrayList<Activity> activities = new ArrayList<Activity>();

    public void addActivity(Activity act) {
        synchronized (activities) {
            if (!activities.contains(act)) {
                activities.add(act);
            }
        }
    }

    public void removeActivity(Activity act) {
        synchronized (activities) {
            activities.remove(act);
        }
    }

    public void clearActivities() {
        if (activities != null) {
            synchronized (activities) {
                for (int i = activities.size() - 1; i >= 0; i--) {
                    activities.get(i).finish();
                }
            }
        }
    }
    /** 微信分享API */
    private static IWXAPI WEIXINAPI;
    @Override
    public void onCreate() {
        super.onCreate();
        init(this);
    }
    private static void init(Context context){
        if (WEIXINAPI == null) {
            WEIXINAPI = WXAPIFactory.createWXAPI(context, WX_APPID,false);
            WEIXINAPI.registerApp(WX_APPID);
        }
    }
    /** 获取微信授权 **/
    public static IWXAPI getWeinAPIHandler (Context context) {
        if (WEIXINAPI == null) {
            init(context);
        }
        return WEIXINAPI;
    }
    public static GaiaApplication getInstance() {
        return s_ins;
    }

    public GaiaApplication() {
        s_ins = this;
    }
}
