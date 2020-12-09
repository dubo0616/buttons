/**
 * 
 */
package com.jindan.p2p.json;

import com.jindan.p2p.json.model.ActiveDetailModel;
import com.jindan.p2p.json.model.ActivityFinalInfoModel;
import com.jindan.p2p.json.model.ActivityInfoModel;
import com.jindan.p2p.json.model.ActivityPopInfoModel;
import com.jindan.p2p.json.model.AdvertListInfoModel;
import com.jindan.p2p.json.model.AnnouncementMainModel;
import com.jindan.p2p.json.model.BankCardsListInfoModel;
import com.jindan.p2p.json.model.BankListInfoModel;
import com.jindan.p2p.json.model.BannerMainListModel;
import com.jindan.p2p.json.model.BindBankInfoModel;
import com.jindan.p2p.json.model.DefaultPostDataUrlInfoModel;
import com.jindan.p2p.json.model.FinanceIndexBiddingListModel;
import com.jindan.p2p.json.model.GoodsInfoModel;
import com.jindan.p2p.json.model.GoodsListInfoModel;
import com.jindan.p2p.json.model.HelpCommonModel;
import com.jindan.p2p.json.model.IncomeStatusListInfoModel;
import com.jindan.p2p.json.model.InvestConfigModel;
import com.jindan.p2p.json.model.InvestItemInfoModel;
import com.jindan.p2p.json.model.MainBiaoListModel;
import com.jindan.p2p.json.model.MainGoodsListModel;
import com.jindan.p2p.json.model.MainIndexBiddingListModel;
import com.jindan.p2p.json.model.OwerListModel;
import com.jindan.p2p.json.model.ProductImage;
import com.jindan.p2p.json.model.PromoteMainListModel;
import com.jindan.p2p.json.model.RechargeConfigModel;
import com.jindan.p2p.json.model.RechargeOrderIdModel;
import com.jindan.p2p.json.model.RedeemItemInfoModel;
import com.jindan.p2p.json.model.RedeemOrderInfoModel;
import com.jindan.p2p.json.model.RewardListInfoModel;
import com.jindan.p2p.json.model.SoftUpdateInfoModel;
import com.jindan.p2p.json.model.TodayBiddingRecord;
import com.jindan.p2p.json.model.TotalAmountModel;
import com.jindan.p2p.json.model.UserBankCardModel;
import com.jindan.p2p.json.model.UserConfigNewModel;
import com.jindan.p2p.json.model.UserModel.UserInfo;
import com.jindan.p2p.json.model.WithdrawOrderInfoModel;
import com.jindan.p2p.net.BaseResult;

public interface JsonParserInterface {

//	/*
//	 * parser http response header
//	 */
//	NetProtocolHeader parserHeader(String responseString) throws Exception;

	/*
	 * tag = 1
	 */
	UserInfo parserDefaultRegistResponse(String responseString) throws Exception;

	/*
	 * tag = 2
	 */
	UserInfo parserUserRegistWithPhoneNumberResponse(String responseStr) throws Exception;

	/*
	 * tag = 3
	 */
	UserInfo parserLoginBySmsResponse(String responseStr) throws Exception;

	/*
	 * tag = 4
	 */
	UserInfo parserLoginByPwdResponse(String responseStr) throws Exception;

	/*
	 * tag = 3
	 */
	UserInfo parserUserInfoResponse(String responseStr) throws Exception;

	/*
	 * 设置修改支付密码
	 * 
	 * @return
	 */
	BaseResult parserModifyPwdResponse(String responseStr) throws Exception;

	/*
	 * 重置支付密码
	 * 
	 * @return
	 */
	BaseResult parserResetPayPwdResponse(String responseStr) throws Exception;

	/*
	 * 重置登录密码
	 * 
	 * @return
	 */
	BaseResult parserResetLoginPwdResponse(String responseStr) throws Exception;

	/*
	 * 重置登录密码--发送验证码
	 * 
	 * @return
	 */
	BaseResult parserGetSmsCodeResetLoginPwdResponse(String responseStr) throws Exception;

	/*
	 * tag = 8
	 */
	BaseResult parserSMSCode(String responseStr) throws Exception;

	/*
	 * tag = 8
	 */
	BaseResult parserSetLoginPwd(String responseStr) throws Exception;

	/*
	 * tag = 115
	 */
//	BannerMainListModel parserBannerListInfo(String responseStr) throws Exception;

	/*
	 * tag = 117
	 */
	PromoteMainListModel parserPromoteListInfo(String responseStr) throws Exception;
	
	/*
	 * tag = 116
	 */
//	AnnouncementMainModel parserAnnouncementMainInfo(String responseStr) throws Exception;

	/*
	 * tag = 119
	 */
	TotalAmountModel parserTotalAmountInfo(String responseStr) throws Exception;

	/*
	 * tag = 120
	 */
	MainGoodsListModel parserMainBiaoListInfo(String responseStr) throws Exception;

	/*
	 * tag = 121
	 */
	BaseResult parserBiaoDetailInfo(String responseStr) throws Exception;

	/*
	 * tag = 122
	 */
	MainBiaoListModel parserBiaoHistoryListInfo(String responseStr) throws Exception;

	/*
	 * tag = 22
	 */
	BaseResult parserDiscoveryHtmlInfo(String responseStr) throws Exception;

	/*
	 * tag = 22
	 */
//	BaseResult parserSaftyHtmlInfo(String responseStr) throws Exception;

	BaseResult parserUploadLogInfo(String responseStr) throws Exception;

	/*
	 * tag = 10
	 */
	UserBankCardModel parserBindBankCard(String responseStr) throws Exception;

	/*
	 * tag = 10
	 */
	ActiveDetailModel parserActiveDetail(String responseStr) throws Exception;

	/*
	 * tag = 12
	 */
	ActivityInfoModel parserActivityNewInfo(String responseStr) throws Exception;

	/*
	 * tag = 13
	 */
	GoodsInfoModel parserGoodsInfo(String responseStr) throws Exception;

	/*
	 * tag = 14
	 */
	GoodsInfoModel parserGoodsDetail(String responseStr) throws Exception;

	/*
	 * tag = 15
	 */
	InvestItemInfoModel parserInvestInfo(String responseStr) throws Exception;

	/*
	 * tag = 17
	 */
	BaseResult parserRedeemListInfo(String responseStr) throws Exception;

	/*
	 * tag = 18
	 */
	BaseResult parserInvestListInfo(String responseStr) throws Exception;

	/*
	 * tag = 19
	 */
	GoodsListInfoModel parserGoodsListInfo(String responseStr) throws Exception;

	/*
	 * tag = 20
	 */
	BankListInfoModel parserBankListInfo(String responseStr) throws Exception;

	/*
	 * tag = 20
	 */
	BankCardsListInfoModel parserBankListNewInfo(String responseStr) throws Exception;

	/*
	 * tag = 21
	 */
	BaseResult parserInvestStatusListInfo(String responseStr) throws Exception;

	/*
	 * tag = 22
	 */
	BaseResult parserIncomeStatusListInfo(String responseStr) throws Exception;

	/*
	 * tag = 23
	 */
	BaseResult parserTotalMoneyProfitInfo(String responseStr) throws Exception;

	/*
	 * tag = 24
	 */
	RewardListInfoModel parserRewardListInfo(String responseStr) throws Exception;

//	/*
//	 * tag = 25
//	 */
//	BaseResult parserRechargeOrderIdInfo(String responseStr) throws Exception;

	/*
	 * tag = 26
	 */
	RedeemItemInfoModel parserInvestNewInfo(String responseStr) throws Exception;

	/*
	 * tag = 27
	 */
	RedeemOrderInfoModel parserRedeemOrderIdInfo(String responseStr) throws Exception;

	/*
	 * tag = 27
	 */
	WithdrawOrderInfoModel parserWithdrawOrderInfo(String responseStr) throws Exception;

	BaseResult parserSureWithdrawOrderInfo(String responseStr) throws Exception;

	BaseResult parserSureRedeemOrderInfo(String responseStr) throws Exception;

	BaseResult parserGetRedeemConfig(String responseStr) throws Exception;

	BaseResult parserGetWithdrawConfig(String responseStr) throws Exception;

	RechargeConfigModel parserRechargeConfigInfo(String responseStr) throws Exception;

	InvestConfigModel parserInvestConfigInfo(String responseStr) throws Exception;

	/*
	 * tag = 28
	 */
	RedeemItemInfoModel parserRedeemNewInfo(String responseStr) throws Exception;

	/*
	 * tag = 29
	 */
	BaseResult parserVerifyPwdInfo(String responseStr) throws Exception;

	/*
	 * tag = 30
	 */
	BaseResult parserCreditDetailInfo(String responseStr) throws Exception;

	/*
	 * tag = 31
	 */
	SoftUpdateInfoModel parserSoftUpdateInfo(String responseStr) throws Exception;

	/*
	 * tag = 34
	 */
	BaseResult parserTotalUserSumInfo(String responseStr) throws Exception;

	/*
	 * tag = 35
	 */
//	BaseResult parserPushRegisterInfo(String responseStr) throws Exception;

	/*
	 * tag = 36
	 */
//	PushNewsListInfoModel parserNewsPushListInfo(String responseStr) throws Exception;

	/*
	 * tag = 100 logout
	 */
	BaseResult parserLogout(String responseStr) throws Exception;

	/**
	 * 37
	 */
	GoodsListInfoModel parserGoodsNewListInfo(String responseStr) throws Exception;

	/**
	 * 41
	 */
	BaseResult parserFeedbackInfo(String resData) throws Exception;

	/*
	 * tag = 42
	 */
	IncomeStatusListInfoModel parserCVStatusListInfo(String responseStr) throws Exception;

	/*
	 * tag = 43
	 */
//	ActivityPopInfoModel parserActiveFinalInfo(String responseStr) throws Exception;

	/*
	 * tag = 43
	 */
	ActivityFinalInfoModel parserActiveShareInfo(String responseStr) throws Exception;

	AdvertListInfoModel parserAdvertLatestInfo(String responseStr) throws Exception;

	/*
	 * tag = 125
	 */
	UserBankCardModel parserBindCardVerifyInfo(String responseStr) throws Exception;

	/*
	 * tag = 126
	 */
	BaseResult parserBindCardGetSMSCode(String responseStr) throws Exception;

	/*
	 * tag = 126
	 */
	BaseResult parserVerifySmsCodeResetLoginPwdResponse(String responseStr) throws Exception;

	/*
	 * tag = 126
	 */
	BaseResult parserCheckIdCardResetPwdResponse(String responseStr) throws Exception;

	/*
	 * tag = 126
	 */
	BaseResult parserCheckOldPwdResetPwdResponse(String responseStr) throws Exception;

	/*
	 * tag = 155 验证金额
	 */
	BindBankInfoModel parserBindCardVerifyMoney(String responseStr) throws Exception;

	/**
	 * tag = 156 重新发送金额
	 * 
	 * @param responseStr
	 * @return
	 * @throws Exception
	 */
	BindBankInfoModel parserBindCardGetMoney(String responseStr) throws Exception;
	
	/**
	 * tag = 156 新的换绑卡
	 * 
	 * @param responseStr
	 * @return
	 * @throws Exception
	 */
	UserBankCardModel parserBindCardNewGetMoney(String responseStr) throws Exception;
	
	/**
	 * tag = 156 绑卡三方验证
	 * 
	 * @param responseStr
	 * @return
	 * @throws Exception
	 */
	BindBankInfoModel parserBindCardStatus(String responseStr) throws Exception;
	
	/**
	 *  tag = 159 获取新的产品图
	 * @param responseStr
	 * @return
	 * @throws Exception
	 */
	ProductImage parserGetProductImage(String responseStr) throws Exception;
	/**
	 *  tag = 161 获取今日受益记录
	 * @param responseStr
	 * @return
	 * @throws Exception
	 */
	TodayBiddingRecord parserGetTodayBiddingRecord(String responseStr) throws Exception;
//	/**
//	 *  tag = 162 获取新手礼包
//	 * @param responseStr
//	 * @return
//	 * @throws Exception
//	 */
//	NovicePacksModel parserGetNovicePacks(String responseStr) throws Exception;

	BaseResult parserImageResource(String responseStr) throws Exception;

	/**
	 * 38
	 */
//	NewsCountModel parserNewsCountInfo(String responseStr) throws Exception;

	/**
	 * 38
	 */
	BaseResult parserPreOrderInfo(String responseStr) throws Exception;
	
	OwerListModel parserOwerListInfo(String responseStr) throws Exception;
	
	
	BaseResult parserGroupHomeInfo(String responseStr) throws Exception;
	
	BaseResult parserGroupFinanceInfo(String responseStr) throws Exception;
	
//	RateCompareModel parserRateCompareInfo(String responseStr) throws Exception;
	
	HelpCommonModel parserHelpCommonInfo(String responseStr) throws Exception;
	
	BaseResult parserGroupDiscoverInfo(String responseStr) throws  Exception;

	BaseResult parserGroupOwerInfo(String responseStr) throws  Exception;

	BaseResult parserDiscoverGrid(String responseStr) throws  Exception;

	BaseResult parserDiscoverList(String responseStr) throws  Exception;
	
	BaseResult parserCrashDataInfo(String responseStr) throws  Exception;
	
	NetProtocolHeader parserHeader(String responseStr) throws  Exception;


	BaseResult parserChangeBankPhoneInfo(String responseStr) throws Exception;

	BaseResult parserInvestBalance(String responseStr) throws Exception;

	BaseResult parserRegionInfo(String responseStr) throws Exception;

	BaseResult parserRegionSiteInfo(String responseStr) throws Exception;

	BaseResult parserAwardRate(String responseStr) throws Exception;

	BaseResult parserGetHtmlDoc(String responseStr) throws Exception;

	BaseResult parserGetIndexButtonMenu(String responseStr) throws Exception;

	BaseResult parserSetDotClick(String responseStr) throws Exception;

	
	//V6
	/*
	 * tag = 173
	 * 新版本标的列表
	 */
	MainIndexBiddingListModel parserMainIndexBiddingInfo(String responseStr) throws Exception;
	
	/**
	 * 理财页面列表数据
	 * @param responseStr
	 * @return
	 * @throws Exception
	 */
	FinanceIndexBiddingListModel parserFinanceIndexBiddingInfo(String responseStr) throws Exception;
	
	/**
	 * 解析永固投资提现配置信息
	 * @param responseStr
	 * @return
	 * @throws Exception
	 */
	UserConfigNewModel parserUserConfigNewInfo(String responseStr) throws Exception;
	/**
	 * V6解析获取消息列表
	 * @param responseStr
	 * @return
	 * @throws Exception
	 */
	BaseResult parserGetNewsListInfo(String responseStr) throws Exception;
	/**
	 * V6解析设置消息已读
	 * @param responseStr
	 * @return
	 * @throws Exception
	 */
	BaseResult parserSetNewsReadInfo(String responseStr) throws Exception;
	/**
	 * V6解析获取消息Tip
	 * @param resData
	 * @return
	 * @throws Exception
	 */
	BaseResult parserGetMessageTipInfo(String resData) throws Exception;
	/**
	 * V6解析获取下拉刷新Tip
	 * @param resData
	 * @return
	 * @throws Exception
	 */
	BaseResult parserGetRefreshTipInfo(String resData) throws Exception;
	
	/**
	 * 首页债权
	 * @param resData
	 * @return
	 * @throws Exception
	 */
	BaseResult parserGetNewCreditInfo(String resData) throws Exception;

	/**
	 * 债权列表
	 * @param responseStr
	 * @return
	 * @throws Exception
	 */
	BaseResult  parserGetCreditListInfo(String responseStr) throws Exception;

	RechargeOrderIdModel parserGetRechargeOrderIdInfo(String responseStr) throws Exception;

	RedeemItemInfoModel parserRechargeConfirmInfo(String responseStr) throws Exception;
	/**
	 * 债权列表
	 * @param responseStr
	 * @return
	 * @throws Exception
	 */
	BaseResult parserOpenBankAccount(String responseStr) throws Exception;
	/**
	 * 获取设置交易密码签名
	 *
	 */
	BaseResult parserGetSetPasswordSign(String resData) throws Exception;



	//V7 START

	/*
	 * tag = 25
	 */
	BaseResult parserRechargeOrderIdInfo(String responseStr) throws Exception;
	//V7  END


	BaseResult parserDiscoverMiddleMenu(String responseStr) throws  Exception;

	BaseResult parserDiscoverBottomMenu(String responseStr) throws  Exception;

	BaseResult parserSettingMenu(String responseStr) throws  Exception;

	BaseResult parserExclusiveList(String responseStr) throws Exception;

	BaseResult parserCouponsList(String responseStr) throws Exception;
	BaseResult  parserPopWindowUrl(String responseStr) throws Exception;
	BaseResult  parserHomeReadMe(String responseStr) throws Exception;

	BaseResult  parserDefaultPostDataAndUrl(String responseStr) throws Exception;

	BaseResult  parserFunNameJson(String responseStr, String funName, Class<DefaultPostDataUrlInfoModel> model) throws Exception;
	BaseResult  parserLoginSourceJson(String responseStr) throws Exception;

}
