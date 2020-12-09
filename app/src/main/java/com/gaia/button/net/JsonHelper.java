package com.jindan.p2p.json;

import com.jindan.p2p.net.BaseResult;
import com.jindan.p2p.utils.ConstantUtil;
import com.jindan.p2p.utils.LogUtil;

public final class JsonHelper {
	/**
	 * 应答结果解析
	 * 说明: 根据请求标签按照约定格式解析应答报文体
	 * 
	 */
	public BaseResult parserWithTag(int requestTag, String resData) throws Exception {
		BaseResult res = null;

		if (ConstantUtil.DEBUG) {
			LogUtil.e("return--strData=" + resData);
		}

		switch (requestTag) {
			case ConstantUtil.Net_Tag_RegistWithPhone:
				res = JsonManager.getJsonParser().parserUserRegistWithPhoneNumberResponse(resData);
				break;
			case ConstantUtil.Net_Tag_UserLogin_Sms:
				res = JsonManager.getJsonParser().parserLoginBySmsResponse(resData);
				break;
			case ConstantUtil.Net_Tag_UserLogin_Pwd:
				res = JsonManager.getJsonParser().parserLoginByPwdResponse(resData);
				break;
			case ConstantUtil.Net_Tag_VerifyCode:
				res = JsonManager.getJsonParser().parserSMSCode(resData);
				break;
			case ConstantUtil.Net_Tag_User_LoginPwd_Set:
				res = JsonManager.getJsonParser().parserSetLoginPwd(resData);
				break;
//			case ConstantUtil.Net_Tag_Banner_Main:
//				res = JsonManager.getJsonParser().parserBannerListInfo(resData);
//				break;
			case ConstantUtil.Net_Tag_Promote_Main:
				res = JsonManager.getJsonParser().parserPromoteListInfo(resData);
				break;
//			case ConstantUtil.Net_Tag_Announcement_Main:
//				res = JsonManager.getJsonParser().parserAnnouncementMainInfo(resData);
//				break;
			case ConstantUtil.Net_Tag_Total_Amount:
				res = JsonManager.getJsonParser().parserTotalAmountInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Main_Biao_List:
				res = JsonManager.getJsonParser().parserMainBiaoListInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Biao_Detail:
				res = JsonManager.getJsonParser().parserBiaoDetailInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Biao_History_List:
				res = JsonManager.getJsonParser().parserBiaoHistoryListInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Bank_List:
				res = JsonManager.getJsonParser().parserBankListInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Bank_List_New:
				res = JsonManager.getJsonParser().parserBankListNewInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Bind_BankCard:
				res = JsonManager.getJsonParser().parserBindBankCard(resData);
				break;
			case ConstantUtil.Net_Tag_Redeem_List:
				res = JsonManager.getJsonParser().parserRedeemListInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Invest_List:
				res = JsonManager.getJsonParser().parserInvestListInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Discovery_Html:
				res = JsonManager.getJsonParser().parserDiscoveryHtmlInfo(resData);
				break;
//			case ConstantUtil.Net_Tag_Safty_Html:
//				res = JsonManager.getJsonParser().parserSaftyHtmlInfo(resData);
//				break;
			case ConstantUtil.Net_Tag_SetPayPwd:
				res = JsonManager.getJsonParser().parserModifyPwdResponse(resData);
				break;
			case ConstantUtil.Net_Tag_Retrieve_Pwd:
				res = JsonManager.getJsonParser().parserResetPayPwdResponse(resData);
				break;
			case ConstantUtil.Net_Tag_Retrieve_LoginPwd:
				res = JsonManager.getJsonParser().parserResetLoginPwdResponse(resData);
				break;
			case ConstantUtil.Net_Tag_Get_Sms_Code_LoginPwd_Reset:
				res = JsonManager.getJsonParser().parserGetSmsCodeResetLoginPwdResponse(resData);
				break;
			case ConstantUtil.Net_Tag_Get_Sms_Code_Verify_LoginPwd_Reset:
				res = JsonManager.getJsonParser().parserVerifySmsCodeResetLoginPwdResponse(resData);
				break;
			case ConstantUtil.Net_Tag_Check_IdCard_LoginPwd_Reset:
				res = JsonManager.getJsonParser().parserCheckIdCardResetPwdResponse(resData);
				break;
			case ConstantUtil.Net_Tag_Check_Old_Pwd:
				res = JsonManager.getJsonParser().parserCheckOldPwdResetPwdResponse(resData);
				break;
			case ConstantUtil.Net_Tag_Request_Pre_Order:
				res = JsonManager.getJsonParser().parserPreOrderInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Ower_List:
				res = JsonManager.getJsonParser().parserOwerListInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Group_Home:
				res = JsonManager.getJsonParser().parserGroupHomeInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Group_Home_BD:
				res = JsonManager.getJsonParser().parserGroupHomeInfo(resData);
				break;
//			case ConstantUtil.Net_Tag_Rate_Compare:
//				res = JsonManager.getJsonParser().parserRateCompareInfo(resData);
//				break;
			case ConstantUtil.Net_Tag_Help_Common:
				res = JsonManager.getJsonParser().parserHelpCommonInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Group_Discover:
				res = JsonManager.getJsonParser().parserGroupDiscoverInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Group_Ower:
				res = JsonManager.getJsonParser().parserGroupOwerInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Discover_Grid:
				res = JsonManager.getJsonParser().parserDiscoverGrid(resData);
				break;
			case ConstantUtil.Net_Tag_Discover_List:
				res = JsonManager.getJsonParser().parserDiscoverList(resData);
				break;
			case ConstantUtil.Net_Tag_Crash_Info:
				res = JsonManager.getJsonParser().parserCrashDataInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Group_Finance_v6:
				res = JsonManager.getJsonParser().parserGroupFinanceInfo(resData);
				break;

			// OLD

			case ConstantUtil.Net_Tag_Get_UserInfo:
				res = JsonManager.getJsonParser().parserUserInfoResponse(resData);
				break;
			case ConstantUtil.Net_Tag_LogOut:
				// res = JsonManager.getJsonParser().parserHeader(responseString)
				break;
			case ConstantUtil.Net_Tag_Active_Invitation_Detail:
				res = JsonManager.getJsonParser().parserActiveDetail(resData);
				break;
			case ConstantUtil.Net_Tag_Activity_New_Info:
				res = JsonManager.getJsonParser().parserActivityNewInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Goods_Detail:
				res = JsonManager.getJsonParser().parserGoodsDetail(resData);
				break;
			case ConstantUtil.Net_Tag_Goods_New_Info:
				res = JsonManager.getJsonParser().parserGoodsInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Invest_OrderId:
				res = JsonManager.getJsonParser().parserInvestInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Invest_OrderId_V7:
				res = JsonManager.getJsonParser().parserRechargeOrderIdInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Invest_Commit:
				res = JsonManager.getJsonParser().parserInvestNewInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Goods_List:
				res = JsonManager.getJsonParser().parserGoodsListInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Invest_Status_List:
				res = JsonManager.getJsonParser().parserInvestStatusListInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Income_Status_List:
				res = JsonManager.getJsonParser().parserIncomeStatusListInfo(resData);
				break;
			case ConstantUtil.Net_Tag_CV_List:
				res = JsonManager.getJsonParser().parserCVStatusListInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Total_Money_Profit:
				res = JsonManager.getJsonParser().parserTotalMoneyProfitInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Reward_List:
				res = JsonManager.getJsonParser().parserRewardListInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Recharge_OrderId:
				res = JsonManager.getJsonParser().parserGetRechargeOrderIdInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Recharge_Commit:
				res = JsonManager.getJsonParser().parserRechargeConfirmInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Redeem_OrderId:
				res = JsonManager.getJsonParser().parserRedeemOrderIdInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Redeem_Commit:
				res = JsonManager.getJsonParser().parserRedeemNewInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Verify_Pwd:
				res = JsonManager.getJsonParser().parserVerifyPwdInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Credit_Detail:
				res = JsonManager.getJsonParser().parserCreditDetailInfo(resData);
				break;
			case ConstantUtil.Net_Tag_App_Update:
				res = JsonManager.getJsonParser().parserSoftUpdateInfo(resData);
				break;
			case ConstantUtil.Net_Tag_BindBandCard_Verify:
				res = JsonManager.getJsonParser().parserBindCardVerifyInfo(resData);
				break;
			case ConstantUtil.Net_Tag_BindBandCard_Sms_Code:
				res = JsonManager.getJsonParser().parserBindCardGetSMSCode(resData);
				break;
			case ConstantUtil.Net_Tag_Total_User_Sum:
				res = JsonManager.getJsonParser().parserTotalUserSumInfo(resData);
				break;
//			case ConstantUtil.Net_Tag_Push_Register:
//				res = JsonManager.getJsonParser().parserPushRegisterInfo(resData);
//				break;
//			case ConstantUtil.Net_Tag_Get_Push_News_List:
//				res = JsonManager.getJsonParser().parserNewsPushListInfo(resData);
//				break;
			case ConstantUtil.Net_Tag_Goods_New_List:
				res = JsonManager.getJsonParser().parserGoodsListInfo(resData);
				break;
			case ConstantUtil.Net_Tag_FeedBack:
				res = JsonManager.getJsonParser().parserFeedbackInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Share_Detail_Info:
				res = JsonManager.getJsonParser().parserActiveShareInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Advert_Latest_Info:
				res = JsonManager.getJsonParser().parserAdvertLatestInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Upload_Log:
				res = JsonManager.getJsonParser().parserUploadLogInfo(resData);
				break;
			/**
			 * 新的绑卡
			 */
			case ConstantUtil.Net_Tag_NEW_Bind_BankCard:
				res = JsonManager.getJsonParser().parserBindCardNewGetMoney(resData);
				break;
			/**
			 * 绑卡三方验证
			 */
			case ConstantUtil.Net_Tag_BankCard_Bind_Status:
				res = JsonManager.getJsonParser().parserBindCardStatus(resData);
				break;
			/**
			 * 获取新的产品图
			 */
			case ConstantUtil.Net_Product_Image:
				res = JsonManager.getJsonParser().parserGetProductImage(resData);
				break;
			/**
			 * 获取首页今日收益记录
			 */
			case ConstantUtil.Net_Tag_Today_Bidding_Record:
				res = JsonManager.getJsonParser().parserGetTodayBiddingRecord(resData);
				break;
//			/**
//			 * 获取首页新手礼包
//			 */
//			case ConstantUtil.Net_Tag_Novice_Packs:
//				res = JsonManager.getJsonParser().parserGetNovicePacks(resData);
//				break;
			case ConstantUtil.Net_Tag_Banner_Get_Image_Resource:
				res = JsonManager.getJsonParser().parserImageResource(resData);
				break;
			//V6
			case ConstantUtil.Net_Tag_Request_Home_Promote://新版本首页推荐位
				res = JsonManager.getJsonParser().parserPromoteListInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Request_Home_Indexbidding://新版本首页标的列表
				res = JsonManager.getJsonParser().parserMainIndexBiddingInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Request_Finance_ProductList://新版本理财list数据
				res = JsonManager.getJsonParser().parserFinanceIndexBiddingInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Request_User_Config://V6 用户投资提现配置信息
				res = JsonManager.getJsonParser().parserUserConfigNewInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Get_Push_News_List_V6:
				//V6获取消息列表
				res = JsonManager.getJsonParser().parserGetNewsListInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Set_News_Is_Read:
				//V6设置消息是否已读
				res = JsonManager.getJsonParser().parserSetNewsReadInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Get_News_Tip:
				//V6获取消息Tip
				res = JsonManager.getJsonParser().parserGetMessageTipInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Get_Refresh_Tip:
				//V6获取下拉刷新文案
				res = JsonManager.getJsonParser().parserGetRefreshTipInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Request_New_Credit://首页债权列表
				//首页债权
				res = JsonManager.getJsonParser().parserGetNewCreditInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Request_Today_Credit://今日债权列表
				res = JsonManager.getJsonParser().parserGetCreditListInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Request_Open_Bank_Account:
				//银行存管开户
				res = JsonManager.getJsonParser().parserOpenBankAccount(resData);
				break;
			case ConstantUtil.Net_Tag_Request_Get_Set_Password_Sign:
				//设置银行密码签名
				res = JsonManager.getJsonParser().parserGetSetPasswordSign(resData);
				break;
			case ConstantUtil.Net_Tag_Create_Withdraw_Order:
				res = JsonManager.getJsonParser().parserWithdrawOrderInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Sure_Withdraw_Order:
				res = JsonManager.getJsonParser().parserSureWithdrawOrderInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Sure_Redeem_Order:
				res = JsonManager.getJsonParser().parserSureRedeemOrderInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Redeem_Config:
				res = JsonManager.getJsonParser().parserGetRedeemConfig(resData);
				break;
			case ConstantUtil.Net_Tag_Withdraw_Config:
				res = JsonManager.getJsonParser().parserGetWithdrawConfig(resData);
				break;
			case ConstantUtil.Net_Tag_Request_Recharge_Config://充值配置信息
				res = JsonManager.getJsonParser().parserRechargeConfigInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Request_Invest_Config://投资配置信息
				res = JsonManager.getJsonParser().parserInvestConfigInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Request_Change_Bank://更换银行卡手机号
				res = JsonManager.getJsonParser().parserChangeBankPhoneInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Request_Invest_Balance:
				res = JsonManager.getJsonParser().parserInvestBalance(resData);
				break;
			case ConstantUtil.Net_Tag_Request_Get_Region:
				res = JsonManager.getJsonParser().parserRegionInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Request_Get_Bank_Site:
				res = JsonManager.getJsonParser().parserRegionSiteInfo(resData);
				break;
			case ConstantUtil.Net_Tag_Request_Get_Award_Rate:
				res = JsonManager.getJsonParser().parserAwardRate(resData);
				break;
			case ConstantUtil.Net_Tag_Request_Get_Html_Doc:
				res = JsonManager.getJsonParser().parserGetHtmlDoc(resData);
				break;
			case ConstantUtil.Net_Tag_Get_Index_Button_Menu:
				res = JsonManager.getJsonParser().parserGetIndexButtonMenu(resData);
				break;
			case ConstantUtil.Net_Tag_Set_Dot_Click:
				res = JsonManager.getJsonParser().parserSetDotClick(resData);
				break;
			case ConstantUtil.Net_Tag_Discover_Middle_Menu:
				res = JsonManager.getJsonParser().parserDiscoverMiddleMenu(resData);
				break;
			case ConstantUtil.Net_Tag_Discover_Bottom_Menu:
				res = JsonManager.getJsonParser().parserDiscoverBottomMenu(resData);
				break;
			case ConstantUtil.Net_Tag_Setting_Menu:
				res = JsonManager.getJsonParser().parserSettingMenu(resData);
				break;
			case ConstantUtil.Net_Tag_Request_Exclusive_List:
				// 解析尊享金蛋列表
				res = JsonManager.getJsonParser().parserExclusiveList(resData);
				break;

			case ConstantUtil.Net_Tag_Request_Coupons_List_Info:
				// 解析红包加息券信息
				res = JsonManager.getJsonParser().parserCouponsList(resData);
				break;
			case ConstantUtil.Net_Tag_Request_POpWindow_Url:
				res = JsonManager.getJsonParser().parserPopWindowUrl(resData);
				break;
			case ConstantUtil.Net_Tag_Get_Readme_Url:
					res = JsonManager.getJsonParser().parserHomeReadMe(resData);
				break;

			case ConstantUtil.Net_Tag_Get_PostData_Url:
				res = JsonManager.getJsonParser().parserDefaultPostDataAndUrl(resData);
				break;

			case ConstantUtil.Net_Tag_Get_LoginSource_Url:
				res = JsonManager.getJsonParser().parserLoginSourceJson(resData);
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
