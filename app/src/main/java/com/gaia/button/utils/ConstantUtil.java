package com.gaia.button.utils;

import com.gaia.button.BuildConfig;
import com.gaia.button.net.NetConfig;

public class ConstantUtil {

	/**
	 * 输出log开关
	 */
	public static final boolean DEBUG = BuildConfig.DEBUG;

	public static String NEW_BAPI_NAME_DEFAULT = "http://img.lovetoshare168.com/bz/";

	public static String NEW_API_URL_SUFFIX = "v1/";
	public static String NEW_BAPI_NAME = NEW_BAPI_NAME_DEFAULT;
	public static String NEW_BAPI_URL = NEW_BAPI_NAME + NEW_API_URL_SUFFIX;

	public static final String SERVER_URL_NAME_LOGIN_SMS = "smsLogin";

	public static final String SERVER_URL_NAME_SMSCODE= "smsCode";
	public static final String SERVER_URL_NAME_PWDLOGIN= "pwdLogin";
	public static final String SERVER_URL_NAME_FORGETPASS= "smsFindPwd";

	public static final String SERVER_URL_NAME_REGISTER = "register";

	public static final String SERVER_URL_NAME_LOGINPWD_SET = "setPassword";
	public static final String SERVER_URL_NAME_ArticleList = "articleList";
	public static final String SERVER_URL_NAME_ArticleCollect = "articleCollect";
	public static final String SERVER_URL_NAME_ProductList = "productList";
	public static final String SERVER_URL_NAME_GetCollect = "getCollect";
	public static final String SERVER_URL_NAME_GetDevice = "getDevice";
	public static final String SERVER_URL_NAME_WechatLogin = "wechatLogin";
	public static final String SERVER_URL_NAME_WechatBindphone= "wechatBindPhone";

	public static final String SERVER_URL_NAME_LOGINPWD_RESET = "reset_password";

	public static final String SERVER_URL_NAME_PAYPWD_RESET = "reset_pay_password";

	public static final String SERVER_URL_NAME_PAYPWD_SET = "set_pay_password";

	public static final String SERVER_URL_NAME_LOGOUT = "logout";
	public static final NetConfig.SubmitDataType HTTP_DATA_TYPE = NetConfig.SubmitDataType.JSON;
	public static String SERVER_URL_TAG_39 = NEW_BAPI_URL + "user_avatar";
	public static final int Net_Tag_LogOut = 100;
//

	/** NET Protocol Tag start **/
	public static final int Net_Invalid_Tag = -1;


	public static final int Net_Tag_UserLogin_Pwd = 110;

	public static final int Net_Tag_UserLogin_Sms = 111;

	public static final int Net_Tag_RegistWithPhone = 112;

	public static final int Net_Tag_User_Login_SEND_CODE= 113;
	public static final int Net_Tag_User_Login_FORGETPASS= 114;
	public static final int Net_Tag_User_Login_SETPASS= 115;
	public static final int Net_Tag_User_ArticleList= 116;
	public static final int Net_Tag_User_ArticleCollect= 117;
	public static final int Net_Tag_User_ProductList= 118;
	public static final int Net_Tag_User_GetCollect= 119;
	public static final int Net_Tag_User_GetDevice= 120;
	public static final int Net_Tag_User_WechatLogin= 121;
	public static final int Net_Tag_User_WechatBindPhone= 122;






}
