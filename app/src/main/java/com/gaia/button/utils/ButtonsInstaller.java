package com.gaia.button.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;

public class ButtonsInstaller {


    /**
     * @brief 读写外部SD卡权限
     * @param activity
     * @param rcode
     * @return
     */
    public static int requestWriteExternalStoragePermission(final Activity activity, final int rcode) {
        if( null == activity ) {
            return -1;
        }
        String[] permissions = { Manifest.permission.WRITE_EXTERNAL_STORAGE };
        int state = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if( PackageManager.PERMISSION_GRANTED == state ) {
            return 0;
        }
        ActivityCompat.requestPermissions(activity, permissions, rcode);
        return -10;
    }

    /**
     * @brief 8.0以上安装未知来源应用权限
     * @param activity
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int requestInstallThirdApkPermission(final Activity activity, final int rcode) {
        if( null == activity ) {
            return -1;
        }

        PackageManager pm = activity.getPackageManager();
        boolean hasInstallPermission = pm.canRequestPackageInstalls();
        if( hasInstallPermission ) {
            return 0;
        }

        Uri packageUri = Uri.parse("package:"+ ButtonsUpdateUtils.getPackageName(activity.getApplication()));
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
        try {
            activity.startActivityForResult(intent, rcode);
        } catch (Exception e) {
            e.printStackTrace();
            return -10;
        } catch (Throwable e) {
            e.printStackTrace();
            return -11;
        }
        return -20;
    }

    /**
     * @brief 安装APK
     * @param activity 所在视图控制器
     * @param fname 要安装的apk文件
     * @param rcode1 请求外部SD卡读写权限
     * @param rcode2 请求安装未知来源权限
     * @return 成功返回0, 否则返回非0
     */
    public static int installApk(final Activity activity, final String fname, final int rcode1, final int rcode2) {
        if( null == activity ) {
            return -1;
        }
        File file = new File(fname);
        if( !file.exists() ) {
            return -2;
        }
        if( !file.isFile() ) {
            return -3;
        }
        if( file.length() <= 0 ) {
            return -4;
        }

        final String pkgName = ButtonsUpdateUtils.getPackageName(activity.getApplication());
        if( TextUtils.isEmpty(pkgName) ) {
            return -5;
        }

        int result = 0;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            //6.0以上动态权限
            result = requestWriteExternalStoragePermission(activity, rcode1);
            if( 0 != result ) {
                return -10;
            }
        }

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            //8.0安装未知来源应用权限
            result = requestInstallThirdApkPermission(activity, rcode2);
            if( 0 != result ) {
                return -11;
            }
        }

        Uri uri = null;
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ) {
            //7.0避免安装出现解析包异常
            uri = FileProvider.getUriForFile(activity,  pkgName + ".provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            //7.0以下
            uri = Uri.fromFile(file);
            //没有在Activity环境下启动Activity设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        intent.setDataAndType(uri, "application/vnd.android.package-archive");

        try {
            if( Looper.getMainLooper() == Looper.myLooper() ) {
                activity.startActivity(intent);
            } else {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            activity.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -50;
        } catch (Throwable e) {
            e.printStackTrace();
            return -51;
        }
        return 0;
    }
}
