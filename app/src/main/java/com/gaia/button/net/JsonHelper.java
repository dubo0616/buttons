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
