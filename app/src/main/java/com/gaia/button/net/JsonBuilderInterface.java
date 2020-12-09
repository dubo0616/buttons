package com.jindan.p2p.json;

import java.util.HashMap;

public interface JsonBuilderInterface {

	/*
	 * tag 1 Default regist
	 */
	String buildDefaultString(String funName);

	String buildDefaultString(String funName, HashMap<String, String> params);

	Object buildRequestUploadLog(String content);

	Object buildGetGroupFinanceString(String funJsonString);

	Object buildGetGroupOwerString (String funJsonString);

//	/*
//	 * tag 2 regist with user name and password
//	 */
//	String buildRegistWithPhoneString(String phone, String msmcode);
//
//	/*
//	 * tag 4 login
//	 */
//	String buildLoginBySmsString(String phone, String msmcode);
//
//	/*
//	 * tag 4 login
//	 */
//	String buildLoginByPwdString(String phone, String msmcode);
//
//	/*
//	 * tag 5 modify password
//	 */
//	String buildRetrievePwdString();
//
//	/*
//	 * tag 5 modify password
//	 */
//	String requestSetPayPwd(String newpwd, String oldpwd, String ck);
//
//	/*
//	 * 设置 和 修改登录密码
//	 */
//	String requestSetLoginPwd(String newPwd, String oldPwd, String ck, String mobile);
//
//	/*
//	 * tag 8 request verify code for modify phone number
//	 */
//	String buildRequestVerifyCodeString(String phonenumber, int type, int verifyType);
//
//	/*
//	 * tag 9 Build get user info request request user info
//	 */
//	String buildRequestUserNewestInfoString();
//
//	/*
//	 * tag 10 bind phone number
//	 */
//	String buildBindBankCardString(String funName, String username, String idCard, String bankcard, String bankphone,
//			String bankcardid);
//
//	/*
//	 * tag 10 bind phone number
//	 */
//	String buildBindBankStatusString(String funName);
//
//	/**
//	 * 新的绑卡参数拼装
//	 *
//	 * @param funName
//	 * @param bankcard
//	 * @param bankphone
//	 * @param bankcardid
//	 * @return
//	 */
//	String buildNewStringBindBankCardString(String funName, String bankcard, String bankphone, String bankcardid);
//
//	/**
//	 * 首页和详情页的Banner图
//	 *
//	 * @param funName
//	 * @param resourceCode
//	 * @return
//	 */
//	String buildGetImageResourceString(String funName, ResourceModel.Type... resourceCode);
//
////	/*
////	 * tag 11 active detail info
////	 */
////	String buildRequestActiveDetailInfoString(String activeId);
////
////	/*
////	 * tag 12 Build active info
////	 */
////	String buildRequestNewActivityInfoString();
////
////	/*
////	 * tag 13 Build active info
////	 */
////	String buildRequestNewGoodsInfoString();
//
//	/*
//	 * tag 14 Build active info
//	 */
//	String buildRequestGoodsDetailInfoString(String goodsId);
//
////	/*
////	 * tag 15 Build active info
////	 */
////	String buildRequestInvestString(String money, String pay_pwd, String goodsId);
////
////	/*
////	 * tag 16 Build active info
////	 */
////	String buildRequestRedeemString(String money, String pay_pwd);
//
//	/*
//	 * tag 17 Build active info
//	 */
//	String buildRequestRedeemListString(int pageNum, int pageSize);
//
//	/*
//	 * tag 18 Build active info
//	 */
//	String buildRequestInvestListString(int pageNum, int pageSize);
//
//	/*
//	 * tag 19 Build active info
//	 */
//	String buildRequestGoodsListString(String funName, int pageNum, int pageSize);
//
//	/*
//	 * tag 20 Build active info
//	 */
//	String buildRequestBankListString(String funName, int pageNum, int pageSize);
//
//	/*
//	 * tag 23 Build active info
//	 */
////	String requestTotalMoneyProfitInfo();
//
//	Object buildRequestInvestStatusListString(int pageNum, int pageSize);
//
//	Object buildRequestIncomeStatusListString(int pageNum, int pageSize);
//
//	Object buildRequestNewsPushListString(int pageNum, int pageSize, int type, int lastNewsId);
//
//	/*
//	 * tag 36 Build active info
//	 */
//	Object buildActiveFinalDetailString(String activeId, int type);
//
//	Object buildRequestDiscoveryHtmlString();
//
//	Object buildRequestSaftyHtmlString();
//
//	Object requestSmsCodeResetPwd(String phonenum, int verifyType);
//
//	Object requestSmsCodeVerifyResetPwd(String phonenum, String smsCode);
//
//	Object requestChangeBankPhone(String mobile, String smsCode);
//
//	Object requestCheckIdCardResetPwd(String phonenum, String idCard);
//
//	Object requestCheckOldPwdResetPwd(String oldPwd);
//
//	Object requestPreOrder(String goodsId);
//
//
//
//	Object buildRetrieveLoginPwd(String mobile, String ck);
//
//	Object buildRequestCheckForUpdate(String platform, String version, int build);
//
////	Object buildRequestRewardListString(int pageNum, int pageSize);
//
//	Object buildRequestInvestOrderIdString(String iUnlockConfig, String money, String goodsId, int type, int fromType, String bId, int refuseMatchBidding, String regularAcceptDay);
//
//	Object buildRequestInvestPayString(String pay_pwd, String orderId, int fromType);
//
//	Object buildRequestRedeemOrderIdString(String money, String pId, int toType, int redeemType, int redeemAll);
//
//	Object buildRequestSureRedeemOrderString(String orderId);
//
//	Object buildRequestCreateWithdrawOrderString(String money, String orderId, String bankSiteId);
//
//	Object buildRequestRedeemNewString(String pay_pwd, String orderId, String pId);
//
////	Object buildVerifyPayPwd(String pay_pwd);
//
//	String buildRrequestBindBankCardVerifyString(String funName, String msmcode);
//
//	/**
//	 * 验证金额
//	 *
//	 * @param funName
//	 * @param orderId
//	 * @param msmcode
//	 * @return
//	 */
//	String buildRrequestBindBankCardVerifyMoney(String funName, String orderId, String msmcode);
//
//	Object buildRequestVerifyCodeString(String funName);
//
//	/**
//	 * 重新发送金额
//	 *
//	 * @param funName
//	 * @param orderId
//	 * @return
//	 */
//	Object buildRrequestBindBankCardGetMoney(String funName, String orderId);
//
//	/**
//	 * 获取产品图
//	 * @param funName
//	 * @return
//	 */
//	Object buildGetProductImage(String funName);
//
//	/**
//	 * 获取首页今日标的记录
//	 * @param funName
//	 * @return
//	 */
//	Object buildGetTodayBiddingRecord(String funName, int limit, int offset);
//
//	/**
//	 * 获取我的页面列表数据
//	 * @param funName
//	 * @return
//	 */
//	Object buildGetOwerListInfo(String funName);
//
//	/**
//	 * 获取首页新手礼包
//	 * @param funName
//	 * @return
//	 */
//	Object buildGetNovicePacks(String funName);
//
//
//
//
//	Object buildGetGroupDiscoverString (String funJsonString);
//
//	Object buildGetInvestAndRedeemConfigString (String pId, String type, String bId);
//	Object buildGetNewInvestAndRedeemConfigString(String funName, String pId, String type, String bId);
//
//	Object buildGetRedeemConfigString (String pId, String type);
//
//	Object buildGetWithdrawConfigString ();
//
//	Object buildGetInvestBalanceString ();
//
//	/**
//	 * 生成获取消息列表请求数据字符串
//	 *
//	 * @param lastPublishTime: 最后一条消息的发布时间
//	 */
//	Object buildGetNewsPushListString(String lastPublishTime);
//	/**
//	 * 生成设置消息是否已读请求数据字符串
//	 *
//	 * @param messageId: 消息ID
//	 */
//	Object buildSetNewsIsReadString(String messageId);
//	/**
//	 * 生成获取消息Tip请求数据字符串
//	 *
//	 */
//	Object buildGetMessageTipString();
//	/**
//	 * 生成获取下拉刷新文案请求数据字符串
//	 *
//	 */
//	Object buildGetRefreshTipString();
//
//	Object buildGetTodayCreditListString(String funName, String direct,
//			int offsetId);
//
//	Object buildGetRechargeOrderIdString(String money);
//
//	Object buildGetRechargeConfirmString(String orderId, String authSeq);
//	/** 生成银行托管请求数据字符串 */
//	Object buildOpenBankAccountString(String username, String id_card, String bank_card, String phone_num, String sms_code, String bankCode);
//	/** 生成获取设置交易密码签名接口 */
//	Object buildGetSetPasswordSignString(String funName);
//
//	/** 大额提现，获取省市 */
//	Object buildGetRegionString(String funName, String regionId);
//	/** 大额提现，获取网点 */
//	Object buildGetRegionSiteString(String funName, String regionId, String keyword);
//
//	Object buildGetHtmlDocString(String funName, String name);
//
//	Object buildSetDotClickString(String funName, String dotType, String dotId);


//	@Override
//	public String buildRequestActiveDetailInfoString(String activeId) {
//		return null;
//	}
//
//	@Override
//	public String buildRequestNewActivityInfoString() {
//		return null;
//	}
//
//	@Override
//	public String buildRequestNewGoodsInfoString() {
//		return null;
//	}
//
//	@Override
//	public String requestTotalMoneyProfitInfo() {
//		return null;
//	}
//	@Override
//	public Object buildVerifyPayPwd(String pay_pwd) {
//		return null;
//	}
//	@Override
//	public String buildRequestInvestString(String money, String pay_pwd, String goodsId) {
//		return null;
//	}
//
//	@Override
//	public String buildRequestRedeemString(String money, String pay_pwd) {
//		return null;
//	}
//
//	@Override
//	public Object buildRequestRewardListString(int pageNum, int pageSize) {
//		return null;
//	}
}