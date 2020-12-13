package com.gaia.button.net;


import android.util.Log;

import com.gaia.button.utils.ConstantUtil;

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
