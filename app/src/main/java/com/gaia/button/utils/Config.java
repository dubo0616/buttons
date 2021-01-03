package com.gaia.button.utils;


import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.gaia.button.GaiaApplication;

import java.io.File;

public class Config {
	public static String sdJindanRootDir = FileHelper.getRootDir() + "/gaia/";
	/** 下载缓存目录(App更新) */
	public final static String downloadDir = sdJindanRootDir + "download/";
	/** WebView缓存目录(图片压缩文件等) */
	public final static String cacheDir = sdJindanRootDir + "cache/";
	/** WebView数据库缓存目录 */
	public final static String cacheDatabase = sdJindanRootDir + "database/";
	/** WebView缓存 */
	public final static String webCacheDir = sdJindanRootDir + "web_image_cache/";
	
	public final static String userDir = sdJindanRootDir + "user/";
	
	public static String log_Dir = sdJindanRootDir + "log/";
	
	public static String sdcard_download_file_path = sdJindanRootDir + "download/file";
	
	public final static String userFolderNameAvatar = "avatar";
	
	public final static String userFolderNameIDCard = "idcard";
	
    public static String SDCARD_PATH_APK = "/gaia/download/file/";
    
    public static final String CRASH_LOG = log_Dir +"crash/";
	
	
	// 下载防重复控制
	public static boolean isLoading = false;

	/***************控制是否开启webview缓存*******************/
	public static final  boolean WEBVIEW_CACHE_FALG = false;

	/**
	 * 获取文件路径后面的文件名
	 *
	 * @param filePath
	 * @return
	 */
	public static String splitFilePath(String filePath) {
		String[] str = filePath.split("/");
		if (str != null && str.length > 0) {
			return str[str.length - 1];
		}
		return null;
	}
	/**
	 * 获取应用私有缓存目录, 此目录无需存储权限
	 * getExternalCacheDir : /storage/emulated/0/Android/data/com.alive.android/cache
	 * getCacheDir : /data/user/0/com.alive.android/cache
	 */
	public static File getAvailableCacheDir(String dirName) {
		File cacheDir = null;
		Context context = GaiaApplication.getInstance().getApplicationContext();

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
			cacheDir = context.getExternalCacheDir();
		}
		if (cacheDir == null) {
			cacheDir = context.getCacheDir();
		}

		if (!TextUtils.isEmpty(dirName)) {
			File file = new File(cacheDir.getPath() + File.separator + dirName);
			if (!file.exists()) {
				file.mkdirs();
			}
			return file;
		}

		return cacheDir;
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

		File availableCacheDir = getAvailableCacheDir("updater");
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
	
}
