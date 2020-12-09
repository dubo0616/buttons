package com.jindan.p2p.user;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.jindan.p2p.database.DatabaseManager;
import com.jindan.p2p.json.model.CrashDomainModel;
import com.jindan.p2p.json.model.HelpCommonModel;
import com.jindan.p2p.json.model.PushNewsListInfoModel;
import com.jindan.p2p.json.model.RedeemItemInfoModel;
import com.jindan.p2p.json.model.UserModel;
import com.jindan.p2p.json.model.UserModel.UserInfo;
import com.jindan.p2p.net.BaseResult;
import com.jindan.p2p.net.INetListener;
import com.jindan.p2p.net.NetManager;
import com.jindan.p2p.utils.ConstantUtil;
import com.jindan.p2p.utils.SharePreferenceUtil;

import static com.jindan.p2p.utils.SharePreferenceUtil.SPTOOL;

@SuppressLint("UseSparseArrays")
public class UserImpl implements INetListener {

	private static final String TAG = "UserImpl";

	private UserModel.UserInfo mCurrentUser = null;

	private UserRequestProxy userProxy = null;

	public UserRequestProxy getUserRequestProxy() {
		return userProxy;
	}

	@SuppressLint("UseSparseArrays")
	public UserImpl() {
		userProxy = UserRequestProxy.getInstance();
		userProxy.setINetListener(this);
	}

	// Net listener
	public void onNetResponse(int requestTag, BaseResult responseData,
			int requestId, int errorCode, String responseStr) {
		if (requestTag == ConstantUtil.Net_Tag_RegistWithPhone) {//注册
			// register with phone
			UserManager.getUserDataHandler().handleUserExit();
			try {
				UserInfo res = (UserInfo) responseData;
				if (res.getErrorCode() == 0) {
//					mCurrentUser = res;
					UserManager.getUserDataHandler().handleForePeriodUserLogin(res);
					//v8
////					try {
////						int role = mCurrentUser.getConfig().getRole();
////						ConstantUtil.Is_Old_Version = role == 1 ? false : true;
////						SPTOOL.putInt("ROLE",mCurrentUser.getConfig().getRole());
////					} catch (Exception e) {
////						ConstantUtil.Is_Old_Version = false;
////					}
//					try {
//						if (!TextUtils.isEmpty(mCurrentUser.getDomain())) {
//							ConstantUtil.setApiDomain(mCurrentUser.getDomain());
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					try {
//						DatabaseManager.getUserDbHandler().addUserInfo(mCurrentUser);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
					handleSuccessResult(requestId, ConstantUtil.Net_Tag_RegistWithPhone, (Object) res, responseStr);
				} else {
					handleFailedResult(requestId, requestTag, errorCode, "注册失败，请稍后重试", responseData);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handleFailedResult(requestId, requestTag, errorCode, "注册失败，请稍后重试", responseData);
			}

		} else if (requestTag == ConstantUtil.Net_Tag_UserLogin_Sms || requestTag == ConstantUtil.Net_Tag_UserLogin_Pwd) {
			// login
			// exit pre login user
			UserManager.getUserDataHandler().handleUserExit();
			try {
				UserInfo res = (UserInfo) responseData;
				if (res.getErrorCode() == 0) {
//					mCurrentUser = res;
					UserManager.getUserDataHandler().handleForePeriodUserLogin(res);
					handleSuccessResult(requestId, requestTag, (Object) res, responseStr);
				} else {
					handleFailedResult(requestId, requestTag, errorCode, "登录失败，请稍后重试", responseData);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handleFailedResult(requestId, requestTag, errorCode, "登录失败，请稍后重试", responseData);
			}

		} else if (requestTag == ConstantUtil.Net_Tag_User_LoginPwd_Set) {
			// update db
			mCurrentUser = UserManager.getUserDataHandler().getCurrentUserInfo();
			if (mCurrentUser != null) {
				mCurrentUser.getConfig().setHas_login_password(1);
				UserManager.getUserDataHandler().updateCurrentUserInfo(mCurrentUser);
			}
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_User_LoginPwd_Set, responseData, responseStr);

//		} else if (requestTag == ConstantUtil.Net_Tag_Banner_Main) {
//			// save db
//			saveDataToSP(requestTag, responseStr);
//			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Banner_Main, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Banner_Get_Image_Resource) {
			// update db
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Banner_Get_Image_Resource, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Banner_Goods) {
			// update db
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Banner_Goods, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Promote_Main) {
			// update db
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Promote_Main, responseData, responseStr);
//		} else if (requestTag == ConstantUtil.Net_Tag_Announcement_Main) {
//			// update db
//			saveDataToSP(requestTag, responseStr);
//			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Announcement_Main, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Total_Amount) {
			// update db
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Total_Amount, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Main_Biao_List) {
			// update db
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Main_Biao_List, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Biao_Detail) {
			// update db
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Biao_Detail, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Biao_History_List) {
			// update db
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Biao_History_List, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Retrieve_Pwd) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Retrieve_Pwd, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Retrieve_LoginPwd) {
			// update db
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Retrieve_LoginPwd, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_SetPayPwd) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_SetPayPwd, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_VerifyCode) {
			// get verify code
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_VerifyCode, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Bind_BankCard) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Bind_BankCard, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_BankCard_Bind_Status) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_BankCard_Bind_Status, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Get_UserInfo) {
			// request user newest info
			try {
				UserInfo res = (UserInfo) responseData;
				mCurrentUser = res;
				UserManager.getUserDataHandler().handleUserLogin(res);
				handleSuccessResult(requestId, ConstantUtil.Net_Tag_Get_UserInfo, responseData, responseStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (requestTag == ConstantUtil.Net_Tag_Active_Invitation_Detail) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Active_Invitation_Detail, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Activity_New_Info) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Activity_New_Info, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Goods_Detail) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Goods_Detail, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Goods_New_Info) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Goods_New_Info, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Invest) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Invest, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Redeem) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Redeem, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Redeem_List) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Redeem_List, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Invest_List) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Invest_List, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Goods_List) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Goods_List, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Bank_List) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Bank_List, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Invest_Status_List) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Invest_Status_List, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Income_Status_List) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Income_Status_List, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Total_Money_Profit) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Total_Money_Profit, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Reward_List) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Reward_List, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Invest_OrderId) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Invest_OrderId, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Invest_Commit) {
			try {
				RedeemItemInfoModel result = (RedeemItemInfoModel) responseData;
				if (result != null && result.getAccount() != null) {
					mCurrentUser = UserManager.getUserDataHandler().getCurrentUserInfo();
					mCurrentUser.setAccount(result.getAccount());
					mCurrentUser.getConfig().setHas_invested(1);
					UserManager.getUserDataHandler().updateCurrentUserInfo(mCurrentUser);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Invest_Commit, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Redeem_OrderId) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Redeem_OrderId, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Recharge_OrderId) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Recharge_OrderId, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Recharge_Commit) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Recharge_Commit, responseData, responseStr);
		}  if (requestTag == ConstantUtil.Net_Tag_Redeem_Commit) {
			RedeemItemInfoModel result = (RedeemItemInfoModel) responseData;
			if (result != null && result.getAccount() != null) {
				mCurrentUser = UserManager.getUserDataHandler().getCurrentUserInfo();
				try {
					mCurrentUser.setAccount(result.getAccount());
					UserManager.getUserDataHandler().updateCurrentUserInfo(mCurrentUser);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Redeem_Commit, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Verify_Pwd) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Verify_Pwd, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Credit_Detail) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Credit_Detail, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_App_Update) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_App_Update, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_BindBandCard_Verify) {
			// saveDataToSP(requestTag, responseStr);
			//4.1.3 需要修改
//			UserBankCardModel result = (UserBankCardModel) responseData;
//			if (result != null) {
//				mCurrentUser = getCurrentUserInfo();
//				mCurrentUser.setBank_cards(result);
//				DataManager.getUserDbHandler().updateUserInfo(mCurrentUser);
//			}
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_BindBandCard_Verify, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_BindBandCard_Sms_Code) {
			// saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_BindBandCard_Sms_Code, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Total_User_Sum) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Total_User_Sum, responseData, responseStr);
//		} else if (requestTag == ConstantUtil.Net_Tag_Get_Push_News_List) {
//			// save data
//			PushNewsListInfoModel result = (PushNewsListInfoModel) responseData;
//			if (result != null) {
//				if (UserRequestProxy.getInstance().getRequestPageNums().get(requestTag) != null
//						&& UserRequestProxy.getInstance().getRequestPageNums().get(requestTag) == 0) {
//					int firstId = DatabaseManager.getNewsDbHandler().getFirstNewsId();
//					if (firstId <= 0) {
//						DatabaseManager.getNewsDbHandler().addNewsListInfo(result);
//						saveDataToSP(requestTag, responseStr);
//					} else {
//						DatabaseManager.getNewsDbHandler().updateNewsListInfo(result);
//					}
//				}
//			}
//			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Get_Push_News_List, responseData, responseStr);
		} else if(requestTag == ConstantUtil.Net_Tag_Get_Push_News_List_V6) {
			//获取消息列表V6
			PushNewsListInfoModel result = (PushNewsListInfoModel) responseData;
			if (null != result) {
				// 结果缓存到数据库
				// 数据库增量更新
				DatabaseManager.getNewsDbHandler().incrementUpdateListInfo(result);
			}
			//结果通知
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Get_Push_News_List_V6, responseData, responseStr);
//		} else if (requestTag == ConstantUtil.Net_Tag_Push_Register) {
//			// saveDataToSP(requestTag, responseStr);
//			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Push_Register, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Goods_New_List) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Goods_New_List, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_FeedBack) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_FeedBack, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_CV_List) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_CV_List, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Share_Detail_Info) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Share_Detail_Info, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Advert_Latest_Info) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Advert_Latest_Info, responseData, responseStr);
//		} else if (requestTag == ConstantUtil.Net_Tag_Safty_Html) {
//			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Safty_Html, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Discovery_Html) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Discovery_Html, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Get_Sms_Code_LoginPwd_Reset) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Get_Sms_Code_LoginPwd_Reset, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Get_Sms_Code_Verify_LoginPwd_Reset) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Get_Sms_Code_Verify_LoginPwd_Reset, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Check_IdCard_LoginPwd_Reset) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Check_IdCard_LoginPwd_Reset, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Check_Old_Pwd) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Check_Old_Pwd, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Request_Pre_Order) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Request_Pre_Order, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_NEW_Bind_BankCard) {
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_NEW_Bind_BankCard, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Product_Image) {
			handleSuccessResult(requestId, ConstantUtil.Net_Product_Image, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Ower_List) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Ower_List, responseData, responseStr);
		} else if(requestTag == ConstantUtil.Net_Tag_Today_Bidding_Record) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Today_Bidding_Record, responseData, responseStr);
//		} else if(requestTag == ConstantUtil.Net_Tag_Novice_Packs) {
//			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Novice_Packs, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Group_Home) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Group_Home, responseData, responseStr);
		}  else if (requestTag == ConstantUtil.Net_Tag_Group_Home_BD) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Group_Home_BD, responseData, responseStr);
//		}  else if (requestTag == ConstantUtil.Net_Tag_Rate_Compare) {
//			saveDataToSP(requestTag, responseStr);
//			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Rate_Compare, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Help_Common) {
			saveDataToSP(requestTag, responseStr);
			saveHelpInfo(responseData);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Help_Common, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Group_Discover) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Group_Discover, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Group_Ower) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Group_Ower, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Discover_Grid) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Discover_Grid, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Discover_List) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Discover_List, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Crash_Info) {
			saveCrashBackInfo(responseData);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Crash_Info, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Group_Finance_v6) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Group_Finance_v6, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Request_User_Config) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, ConstantUtil.Net_Tag_Request_User_Config, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Get_News_Tip) {
			//获取首页消息Tip
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, requestTag, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Get_Refresh_Tip) {
			//获取首页下拉刷新Tip
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, requestTag, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Request_Today_Credit) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, requestTag, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Request_Invest_Config) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, requestTag, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Request_Recharge_Config) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, requestTag, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Bank_List_New) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, requestTag, responseData, responseStr);
        } else if (requestTag == ConstantUtil.Net_Tag_Set_Dot_Click) {
			handleSuccessResult(requestId, requestTag, responseData, responseStr);
		} else if (requestTag == ConstantUtil.Net_Tag_Setting_Menu) {
			saveDataToSP(requestTag, responseStr);
			handleSuccessResult(requestId, requestTag, responseData, responseStr);
		} else {
			//通用消息处理
			handleSuccessResult(requestId, requestTag, responseData, responseStr);
		} 
	}

	private void saveHelpInfo(final BaseResult data) {
		// save help info
		try {
			new Thread() {
				@Override
				public void run() {
					super.run();
					HelpCommonModel res = (HelpCommonModel) data;
					SharePreferenceUtil utils = SharePreferenceUtil.getInstance();
					utils.putObject(ConstantUtil.SP_NAME_HELP_COMMON_LOGIN, res.getLogin());
					utils.putObject(ConstantUtil.SP_NAME_HELP_COMMON_REGISTER, res.getRegister());
					utils.putObject(ConstantUtil.SP_NAME_HELP_COMMON_BIND, res.getBind_card());
					utils.putObject(ConstantUtil.SP_NAME_HELP_COMMON_INVEST, res.getInvest());
					utils.putObject(ConstantUtil.SP_NAME_HELP_COMMON_REDEEM, res.getRedeem());
					utils.putObject(ConstantUtil.SP_NAME_HELP_COMMON_RECHARGE, res.getRecharge());
					utils.putObject(ConstantUtil.SP_NAME_HELP_COMMON_WITHDRAW, res.getWithdraw());
					utils.putObject(ConstantUtil.SP_NAME_HELP_COMMON_WITHDRAW_PASSWORD, res.getWithdrawPassword());
					utils.putObject(ConstantUtil.SP_NAME_HELP_COMMON_PRODUCTLIST, res.getProductList());
					utils.putObject(ConstantUtil.SP_NAME_HELP_COMMON_MY, res.getMy());
					utils.putObject(ConstantUtil.SP_NAME_HELP_COMMON_DISCOVERY, res.getDiscovery());
//						utils.putObject(ConstantUtil.SP_NAME_HELP_BUTTON_INVEST, res.getP2pInvest());
				}
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveCrashBackInfo(BaseResult responseData) {
		try {
			CrashDomainModel model = (CrashDomainModel) responseData;
			if (model != null && model.getList() != null && model.getList().size() > 0) {
				SPTOOL.putObject(ConstantUtil.SP_NAME_CRASH_KEY, model);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onDownLoadStatus(DownLoadStatus status, int requestId) {

	}

	public void onDownLoadProgressCurSize(long curSize, long totalSize, int requestId) {

	}

	public void onNetResponseErr(int requestTag, int requestId, int errorCode,
			String msg, Object responseData) {
		// handleFailedResult(requestType, errorCode, msg);
		if(errorCode == 1000) { // 用户状态失效
			UserManager.getUserDataHandler().handleUserExit();
		}
		if (requestTag == ConstantUtil.Net_Tag_Banner_Get_Image_Resource) {
			saveDataToSP(requestTag, msg + "error");
			handleFailedResult(requestId, ConstantUtil.Net_Tag_Banner_Get_Image_Resource, errorCode, msg, responseData);
		} else {
			handleFailedResult(requestId, requestTag, errorCode, msg, responseData);
		}
	}

	private void handleSuccessResult(int requestId, final int requestType,
			final Object data, String responseStr) {

		userProxy.handleSuccessResult(requestId, requestType, data, responseStr);
	}

	private void handleFailedResult(int requestId, final int requestType,
			final int errorCode, final String errorMsg, Object responseData) {

		userProxy.handleFailedResult(requestId, requestType, errorCode, errorMsg, responseData);
	}

	private void cancelSpecialIdRequest(int requestId) {
		NetManager.getHttpConnect().cancelRequestById(requestId);
	}

	private void saveDataToSP(int requestTag, String responseStr) {
		UserManager.getUserDataHandler().saveDataToSP(requestTag, responseStr);
	}

	private void clearCurrentUserAllCache() {
		UserManager.getUserDataHandler().clearCurrentUserAllCache();
	}

}

