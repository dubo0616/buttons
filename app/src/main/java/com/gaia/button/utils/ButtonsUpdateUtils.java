package com.gaia.button.utils;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class ButtonsUpdateUtils {

    /**
     * @brief 获取应用版本号
     * @param app
     * @return
     */
    public static long  getVersionCode(Application app) {
        long vcode = 0;
        try {
            PackageManager pm = app.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(app.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                vcode = pi.getLongVersionCode();
            } else {
                vcode = pi.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return vcode;
    }

    public static String getPackageName(Application app) {
        String pkgname = null;
        try {
            PackageManager pm = app.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(app.getPackageName(), 0);
            pkgname = pi.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return pkgname;
    }

    /**
     * @brief 删除文件
     * @param fname 目标文件
     * @return 成功返回true, 否则返回false
     */
    public static boolean removeFile(String fname) {
        if( TextUtils.isEmpty(fname) ) {
            return false;
        }
        File file =  new File(fname);
        if( !file.exists() ) {
            return true;
        }
        if( file.isDirectory() ) {
            return false;
        }
        if( !file.delete() ) {
            return false;
        }
        return true;
    }

    /**
     * @brief 获取文件长度
     * @param fname 目标文件
     * @return 成功返回文件长度, 否则返回<0的值
     */
    public static long getFileLength(String fname) {
        if( TextUtils.isEmpty(fname) ) {
            return -1;
        }
        File file =  new File(fname);
        if( !file.exists() ) {
            return -2;
        }
        if( !file.isFile() ) {
            return -3;
        }
        return file.length();
    }

    /**
     * @brief 获取文件MD5
     * @param fname 目标文件
     * @return 成功返回md5, 否则返回null
     */
    public static String getFileMD5(String fname) {
        if( TextUtils.isEmpty(fname) ) {
            return null;
        }
        File file =  new File(fname);
        if( !file.exists() ) {
            return null;
        }
        if( !file.isFile() ) {
            return null;
        }
        if( file.length() <= 0 ) {
            file.delete();
            return null;
        }

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if( null == digest ) {
            return null;
        }

        //打开文件
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //读取内容
        byte [] buffer = new byte[8192];
        do {
            int result = 0;

            try {
                result = fis.read(buffer);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if( result < 0 ) {
                break;
            }
            if( 0 == result ) {
                continue;
            }

            digest.update(buffer, 0, result);
        } while( true );

        //关闭文件
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //获取HASH
        byte[] hash = null;
        try {
            hash = digest.digest();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if( null == hash ) {
            return null;
        }

        //转换为16进制串
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString().toUpperCase();
    }

    /**
     * @brief 获取更新缓存路径
     * @param app 应用实例
     * @return 成功返回缓存路径, 否则返回null
     */
    public static String getCachePath(Application app) {
        if( null == app ) {
            return null;
        }

        File availableCacheDir = BaseUtils.getAvailableCacheDir("updater");
        if (availableCacheDir == null) {
            return null;
        }
        String cachePath = availableCacheDir.getPath();

        //路径为空时不能继续
        if( TextUtils.isEmpty(cachePath) ) {
            return null;
        }
        return cachePath;
    }

    /**
     * @brief 构造一个带有随机时间戳的url
     * @param url 原始url
     * @return 成功返回修正后的url, 否则返回null
     */
    public static String getTimeUrl(String url, int minutes) {
        if( TextUtils.isEmpty(url) ) {
            return null;
        }
        String value = null;
        if( minutes <= 0 ) {
            value = "" + (System.currentTimeMillis() / 1000);
        } else if( minutes > 30 ) {
            minutes = 30;
        }
        if( minutes > 0 ) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            value = "" + minutes;
            value += "_" + year;
            value += "_" + month;
            value += "_" + day;
            value += "_" + hour;
            value += "_" + (minute/minutes);
        }

        if( url.contains("?") ) {
            return url += "&rdt=" + value;
        }
        return url += "?rdt=" + value;
    }
}
