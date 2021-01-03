package com.gaia.button.net;


import android.util.Log;

import com.gaia.button.utils.ConstantUtil;

import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_ArticleCollect;

public final class JsonHelper {
	/**
	 * 应答结果解析
	 * 说明: 根据请求标签按照约定格式解析应答报文体
	 * 
	 */
	public BaseResult parserWithTag(int requestTag, String resData) throws Exception {
		BaseResult res = null;

		if (ConstantUtil.DEBUG) {
			Log.e("JsonHelper","return--strData=" + resData);
		}

		switch (requestTag) {
			case ConstantUtil.Net_Tag_UserLogin_Sms:
				res = JsonManager.getJsonParser().parserLoginSms(resData);
				break;
			case ConstantUtil.Net_Tag_User_Login_SEND_CODE:
				res = JsonManager.getJsonParser().parserLoginSendCode(resData);
				break;
			case ConstantUtil.Net_Tag_UserLogin_Pwd:
				res = JsonManager.getJsonParser().parserLoginPwd(resData);
				break;
			case ConstantUtil.Net_Tag_User_Login_FORGETPASS:
				res = JsonManager.getJsonParser().parserLoginPwd(resData);
				break;
			case ConstantUtil.Net_Tag_User_Login_SETPASS:
				res = JsonManager.getJsonParser().parserSetPwd(resData);
				break;
			case ConstantUtil.Net_Tag_User_ArticleList:
				res = JsonManager.getJsonParser().parserArticleList(resData);
				break;
			case ConstantUtil.Net_Tag_User_ArticleCollect:
				res = JsonManager.getJsonParser().parserArticleCollect(resData);
				break;
			case ConstantUtil.Net_Tag_User_ProductList:
				res = JsonManager.getJsonParser().parserProductList(resData);
				break;
			case ConstantUtil.Net_Tag_User_GetCollect:
				res = JsonManager.getJsonParser().parserGetCollect(resData);
				break;
			case ConstantUtil.Net_Tag_User_GetDevice:
				res = JsonManager.getJsonParser().parserGetDevice(resData);
				break;
			case ConstantUtil.Net_Tag_LogOut:
				res = JsonManager.getJsonParser().parserLoginout(resData);
				break;
			case ConstantUtil.Net_Tag_User_WechatLogin:
				res = JsonManager.getJsonParser().parserWeixinLogin(resData);
				break;
			case ConstantUtil.Net_Tag_User_WechatBindPhone:
				res = JsonManager.getJsonParser().parserWeixinLoginBindphone(resData);
				break;
			case ConstantUtil.Net_Tag_User_AUTOPLAY:
				res = JsonManager.getJsonParser().parserAutoPlay(resData);
				break;
			case ConstantUtil.Net_Tag_User_GetVersion:
				res = JsonManager.getJsonParser().parserUpdate(resData);
				break;
			case ConstantUtil.Net_Tag_User_UpdatePassword:
				res = JsonManager.getJsonParser().parserLoginout(resData);
				break;
			case ConstantUtil.Net_Tag_User_GetFirmwareVersion:
				res = JsonManager.getJsonParser().parserAirUpdate(resData);
				break;

			default:
				break;
		}

//		res.setErrorCode(DcError.DC_BAFFLE_ERROR);
//		res.setErrorString("服务器升级中");
		return res;
	}

	/**
	 * 获取通用的errorcode 如9999
	 * @param resData
	 * @return
	 * @throws Exception
	 */
	public static NetProtocolHeader parserHeaderForHttp(String resData) throws Exception {
		return JsonManager.getJsonParser().parserHeader(resData);
	}
}
