package com.jindan.p2p.utils;


import android.webkit.WebSettings;

import com.jindan.p2p.P2PApplication;

public class Config {
	public static String sdJindanRootDir = FileHelper.getRootDir() + "/jindan/";
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
	
	public static String sdcard_download_file_path = sdJindanRootDir + "download/file/";
	
	public final static String userFolderNameAvatar = "avatar";
	
	public final static String userFolderNameIDCard = "idcard";
	
    public static String SDCARD_PATH_APK = "/jindan/download/file/";
    
    public static final String CRASH_LOG = log_Dir +"crash/";
	
	
	// 下载防重复控制
	public static boolean isLoading = false;

	/***************控制是否开启webview缓存*******************/
	public static final  boolean WEBVIEW_CACHE_FALG = false;

	/***
	 * 根据网络判断webview缓存模式
	 * @return
     */
	public static int getWebviewCacheMode(){

		return  ConnectManager.isNetworkConnected(P2PApplication.getInstance())?
				WebSettings.LOAD_DEFAULT :WebSettings.LOAD_CACHE_ELSE_NETWORK;
	}

	
}
