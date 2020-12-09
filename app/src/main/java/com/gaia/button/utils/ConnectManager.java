package com.gaia.button.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConnectManager
{
    // Connect type
    public static final int Net_ConnType_2G = 1;
    public static final int Net_ConnType_3G = 2;
    public static final int Net_ConnType_WIFI = 3;

    public static final String MOBILE_UNI_PROXY_IP = "10.0.0.172";
    public static final String TELCOM_PROXY_IP = "10.0.0.200";
    public static final String PROXY_PORT = "80";

    public static final String CHINA_MOBILE_WAP = "CMWAP";
    public static final String CHINA_UNI_WAP = "UNIWAP";
    public static final String CHINA_UNI_3G = "3GWAP";
    public static final String CHINA_TELCOM = "CTWAP";

    private static final String TAG = "ConnectManager";
    private static final boolean DEBUG = false;
    private String mApn;
    private String mProxy;
    private String mPort;
    private boolean mUseWap;
    public static final Uri PREFERRED_APN_URI = Uri
            .parse("content://telephony/carriers/preferapn");

    public ConnectManager(Context context)
    {
        try
        {
            checkNetworkType(context);
        }
        catch (SecurityException e)
        {
            checkConnectType(context);
        }

    }

    private void checkApn(Context context)
    {
        Cursor cursor = context.getContentResolver().query(PREFERRED_APN_URI,
                new String[]
                {
                        "_id", "apn", "proxy", "port"
                }, null, null, null);
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                int i = cursor.getColumnIndex("apn");
                int j = cursor.getColumnIndex("proxy");
                int k = cursor.getColumnIndex("port");
                mApn = cursor.getString(i);
                mProxy = cursor.getString(j);
                mPort = cursor.getString(k);
                if (mProxy != null && mProxy.length() > 0)
                {
                    if ("10.0.0.172".equals(mProxy.trim()))
                    {
                        mUseWap = true;
                        mPort = "80";
                    }
                    else if ("10.0.0.200".equals(mProxy.trim()))
                    {
                        mUseWap = true;
                        mPort = "80";
                    }
                    else
                    {
                        mUseWap = false;
                    }
                }
                else if (mApn != null)
                {
                    String s = mApn.toUpperCase();
                    if (s.equals("CMWAP") || s.equals("UNIWAP")
                            || s.equals("3GWAP"))
                    {
                        mUseWap = true;
                        mProxy = "10.0.0.172";
                        mPort = "80";
                    }
                    else if (s.equals("CTWAP"))
                    {
                        mUseWap = true;
                        mProxy = "10.0.0.200";
                        mPort = "80";
                    }
                }
                else
                {
                    mUseWap = false;
                }
            }
            cursor.close();
        }
    }

    /*
     * Get Current connection type
     * 
     * @Return ConnectType have three kind of type
     */
    public void checkConnectType(Context context)
    {

        final ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conn != null)
        {

            NetworkInfo info = conn.getActiveNetworkInfo();

            if (info != null)
            {
                String connStr = info.getTypeName();

                if (connStr.equalsIgnoreCase("WIFI"))
                {

                    // set member param
                    mUseWap = false;

                }
                else if (connStr.equalsIgnoreCase("MOBILE"))
                {

                    String apn = info.getExtraInfo();

                    if (apn == null)
                    {
                        mUseWap = false;
                    }
                    else
                    {
                        if (apn.indexOf("wap") > -1)
                        {

                            if (apn.equals("cmwap") || apn.equals("uniwap")
                                    || apn.equals("3gwap"))
                            {

                                mUseWap = true;
                                mProxy = "10.0.0.172";
                                mPort = "80";

                            }
                            else if (apn.equals("ctwap"))
                            {

                                mUseWap = true;
                                mProxy = "10.0.0.200";
                                mPort = "80";

                            }
                            else
                            {
                                // not use wap
                                mUseWap = false;
                            }

                        }
                        else
                        {
                            // not use wap
                            mUseWap = false;
                        }
                    }

                }
            }
        }
    }

    private void checkNetworkType(Context context)
    {
        ConnectivityManager connectivitymanager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        if (networkinfo != null) {
            if ("wifi".equals(networkinfo.getTypeName().toLowerCase()))
                mUseWap = false;
            else
                checkApn(context);
        }
    }

    public static boolean isNetworkConnected(Context context) {
        int sdkNum = 0;
		try {
			sdkNum = Integer.parseInt(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (sdkNum <= 7) {
			return true;
		}
        ConnectivityManager connectivitymanager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        if (networkinfo != null)
            return networkinfo.isConnectedOrConnecting();
        else
            return false;
    }

    /**
     * make true current connect service is wifi
     * 
     * @param context
     *            Application context
     * @return
     */
    public static boolean isWifi(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI)
        {
            return true;
        }
        return false;
    }

    /**
     * Judge whether fast connection
     * 
     * @param context
     *            Application context true if fast connection, otherwise false
     */
    public static boolean isConnectionFast(Context context)
    {

        try
        {
            State wifiState = null;
            State mobileState = null;
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiState = connectivityManager.getNetworkInfo(
                    ConnectivityManager.TYPE_WIFI).getState();
            mobileState = connectivityManager.getNetworkInfo(
                    ConnectivityManager.TYPE_MOBILE).getState();

            int subType;

            if (wifiState != null && mobileState != null
                    && State.CONNECTED != wifiState
                    && State.CONNECTED != mobileState)
            {
                return false;
            }

            TelephonyManager telephoneManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            if (telephoneManager == null)
            {
                return false;
            }

            subType = telephoneManager.getNetworkType();

            if (wifiState != null && State.CONNECTED == wifiState)
            {
                System.out.println("CONNECTED VIA WIFI");
                return true;
            }
            else if (wifiState != null && mobileState != null
                    && State.CONNECTED != wifiState
                    && State.CONNECTED == mobileState)
            {
                switch (subType)
                {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        return false; // ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        return false; // ~ 14-64 kbps
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        return false; // ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        return true; // ~ 400-1000 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        return true; // ~ 600-1400 kbps
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        return false; // ~ 100 kbps
                        // case TelephonyManager.NETWORK_TYPE_HSDPA:
                        // return true; // ~ 2-14 Mbps
                        // case TelephonyManager.NETWORK_TYPE_HSPA:
                        // return true; // ~ 700-1700 kbps
                        // case TelephonyManager.NETWORK_TYPE_HSUPA:
                        // return true; // ~ 1-23 Mbps
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        return true; // ~ 400-7000 kbps
                        // NOT AVAILABLE YET IN API LEVEL 7
                        // case Connectivity.NETWORK_TYPE_EHRPD:
                        // return true; // ~ 1-2 Mbps
                        // case Connectivity.NETWORK_TYPE_EVDO_B:
                        // return true; // ~ 5 Mbps
                        // case Connectivity.NETWORK_TYPE_HSPAP:
                        // return true; // ~ 10-20 Mbps
                        // case Connectivity.NETWORK_TYPE_IDEN:
                        // return false; // ~25 kbps
                        // case Connectivity.NETWORK_TYPE_LTE:
                        // return true; // ~ 10+ Mbps
                        // Unknown
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                        return false;
                    default:
                        return false;
                }
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }


    public static TYPE_NET checkNetworkState(Context context){
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
            if(networkInfo == null) {
                return TYPE_NET.NONE;
            }
            //1.判断是否有网络连接
            boolean networkAvailable = networkInfo.isAvailable();
            if (!networkAvailable) {
                return TYPE_NET.NONE;
            }
            //2.获取当前网络连接的类型信息
            int networkType = networkInfo.getType();
            if(ConnectivityManager.TYPE_WIFI == networkType){
                //当前为wifi网络
                return TYPE_NET.WIFI;
            } else if(ConnectivityManager.TYPE_MOBILE == networkType){
                //当前为mobile网络
                return TYPE_NET.MOBILE;
            }
        }
        return TYPE_NET.NONE;
    }

    public enum TYPE_NET {
        WIFI, MOBILE, NONE
    }

    public boolean isWapNetwork()
    {
        return mUseWap;
    }

    // public String getApn()
    // {
    // return mApn;
    // }

    public String getProxy()
    {
        return mProxy;
    }

    public String getProxyPort()
    {
        return mPort;
    }
    
    /*
    * @category 判断是否有外网连接
    * @return
    */ 
   public static boolean checkNetStateByPing() { 
    
	   return pingNetUrl("www.baidu.com");
		
   }
   /**
    * 查看制定url是否正常
    * @param url
    * @return
    */
   public static boolean pingNetUrl(String url) {
	   
	   boolean result = false;
		InputStream input;
		try {
			Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + url);// ping网址3次
			// 读取ping的内容，可以不加
			input = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer stringBuffer = new StringBuffer();
			String content = "";
			while ((content = in.readLine()) != null) {
				stringBuffer.append(content);
			}
			// ping的状态
			int status = p.waitFor();
			if (status == 0) {
				result = true;
			} else {
				result = false;
			}
		} catch (IOException e) {
			result = false;
		} catch (InterruptedException e) {
			result = false;
		} finally {
			input = null;
		}
		return result;
   }

}