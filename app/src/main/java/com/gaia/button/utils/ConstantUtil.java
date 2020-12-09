package com.jindan.p2p.utils;

import com.jindan.p2p.BuildConfig;
import com.jindan.p2p.net.NetConfig.SubmitDataType;

import static com.jindan.p2p.utils.SharePreferenceUtil.SPTOOL;

public class ConstantUtil {

	/**
	 * 输出log开关
	 */
	public static final boolean DEBUG = BuildConfig.DEBUG;

	/**
	 * 友盟上报统计开关
	 */
	public static volatile boolean UM_SWITCH = !BuildConfig.DEBUG;

	public static volatile boolean Is_Old_Version = false;
	/** 布尔型整数值(false) */
	public static final int FALSE = 0;
	/** 布尔型整数值(true) */
	public static final int TRUE = 1;

	/**
	 * 1:main 自有渠道 2:baidu_lianhe 百度联合首发(百度手机助手,91,android市场) 3:sougou 搜狗
	 * 4：baidu 5:91market 6:android_market 7:360 8:tencent 应用宝 9：wandoujia 豌豆荚
	 * 10：xiaomi 小米 11:moji 墨迹天气 12:wannianli 万年历 13:didi 滴滴打车
	 */
	public static final String APP_CHANNEL_NAME = AppUtils.getUMengChannelName();

	public static final String APP_VERSION_NAME = AppUtils.getAppVersionName();

	public static final String APP_API_VERSION = "2";

	public static final String SP_NAME_APP_DATA = "app_data";

	/** 字符串(-1)表示未初始化使用默认值 */
	public static final String SP_VALUE_DEFAULT = String.valueOf(-1);
	public static final String SP_KEY_APP_STATE_FIRST_START = "app_first_start";
	public static final String SP_KEY_GUIDE_TABLE_FINANCE_FIRST = "guide_table_finance";
	public static final String SP_KEY_GUIDE_OUR_CASH_FIRST = "guide_our_cash";
	/** 新手引导页面对应的VersionName(新手引导完成进行更新) */
	public static final String SP_KEY_APP_VERSION_GUIDE = "version_guide";
	/** 默认新手引导对应的VersionName */
	public static final String GUIDE_VERSION_NAME = AppUtils.getAppVersionName();
	public static String SP_NAME_RATE_JINDAN_CURRENT = "jindan_current_rate";
	public static String SP_NAME_RATE_BANK_CURRENT = "bank_current_rate";
	public static String SP_NAME_RATE_BANK_FIX = "bank_fix_rate";
	public static String SP_NAME_RATE_BAO = "bao_rate";
	public static String SP_NAME_RATE_OTHER = "other_current_rate";

	//help common
	public static String SP_NAME_HELP_COMMON_LOGIN = "help_common_login";
	public static String SP_NAME_HELP_COMMON_REGISTER = "help_common_register";
	public static String SP_NAME_HELP_COMMON_BIND = "help_common_bind";
	public static String SP_NAME_HELP_COMMON_INVEST = "help_common_invest";
	public static String SP_NAME_HELP_COMMON_REDEEM = "help_common_redeem";
	public static String SP_NAME_HELP_COMMON_RECHARGE = "help_common_recharge";
	public static String SP_NAME_HELP_COMMON_WITHDRAW = "help_common_withdraw";
	public static String SP_NAME_HELP_COMMON_WITHDRAW_PASSWORD = "help_common_withdraw_password";
	public static String SP_NAME_HELP_COMMON_PRODUCTLIST = "help_common_product_list";
	public static String SP_NAME_HELP_COMMON_MY = "help_common_my";
	public static String SP_NAME_HELP_COMMON_DISCOVERY = "help_common_discovery";

	// refresh
	public static String SP_NAME_REFRESH_TOP_DESC1 = "refresh_top_desc1";
	public static String SP_NAME_REFRESH_TOP_NAME1 = "refresh_top_name1";
	public static String SP_NAME_REFRESH_TOP_DESC2 = "refresh_top_desc2";
	public static String SP_NAME_REFRESH_TOP_NAME2 = "refresh_top_name2";
	public static String SP_NAME_REFRESH_TOP_DESC3 = "refresh_top_desc3";
	public static String SP_NAME_REFRESH_TOP_NAME3 = "refresh_top_name3";
	/** 大额提现: 银行卡号 */
	public static final String SP_NAME_SEARCH_BANK_CARDNO = "search_bank_cardno";
	/** 大额提现: 省份 */
	public static final String SP_NAME_SEARCH_BANK_PROVINCE = "search_bank_province";
	/** 大额提现: 城市 */
	public static final String SP_NAME_SEARCH_BANK_CITY = "search_bank_city";
	/** 大额提现: 支行 */
	public static final String SP_NAME_SEARCH_BANK_BRANCH = "search_bank_branch";
	/** 大额提现: 省份ID */
	public static final String SP_NAME_SEARCH_BANK_PROVINCE_ID = "search_bank_province_id";
	/** 大额提现: 城市ID */
	public static final String SP_NAME_SEARCH_BANK_CITY_ID = "search_bank_city_id";
	/** 大额提现: 支行ID */
	public static final String SP_NAME_SEARCH_BANK_BRANCH_ID = "search_bank_branch_id";
	/** 提现Config信息 */
	public static final String SP_NAME_WITHDRAW_CONFIG = "config_withdraw";

	/** 广告逻辑 **/
	public static final String SP_NAME_SPLASH_AD_START_TIME = "splash_ad_start_time";
	public static final String SP_NAME_SPLASH_AD_END_TIME = "splash_ad_end_time";
	public static final String SP_NAME_SPLASH_AD_BEGIN_LOCAL_TIME = "splash_ad_begin_local_time";
	public static final String SP_NAME_SPLASH_AD_IMAGE_URL = "splash_ad_image_url";
	public static final String SP_NAME_SPLASH_AD_IMAGE_OPENURL = "splash_ad_image_openurl";

	public static final SubmitDataType HTTP_DATA_TYPE = SubmitDataType.JSON;

	// public static final String NEW_BAPI_URL = "https://api.jindanlicai.com";
	
//	public static String NEW_API_NAME = "api";
	public static String NEW_M_NAME = "m";


	public static String NEW_BAPI_NAME_DEFAULT = "https://xwapi.jindanlicai.com/";

	public static String NEW_M_NAME_DEFAULT = "https://xwm.jindanlicai.com";


	public static String NEW_BAPI_NAME = NEW_BAPI_NAME_DEFAULT;

	public static String NEW_API_URL_SUFFIX = "v16/";

	public static String NEW_API_URL_LOG = "http://statistics.jindanlicai.com/v1";

	//默认访问BAPI域名
	public static String NEW_BAPI_URL = NEW_BAPI_NAME + NEW_API_URL_SUFFIX;


	public static String API_DOMAIN_M = "https://" + NEW_M_NAME+ ".jindanlicai.com/";

	public static String API_URL_H5 = API_DOMAIN_M + "apitwo/get_h5/?";

	public static final String NEW_WWW_URL = "http://www.jindanlicai.com/";

	public static final String WWW_URL_RULE = NEW_WWW_URL + "rule/";

	public static final String URL_PAYMENT = WWW_URL_RULE + "payment.html";
	
	public static final String URL_BANK_CARS_DES = WWW_URL_RULE + "bindDiscription.html";
	
	public static final String URL_USER_SERVE = API_DOMAIN_M + "wap/app/agreement?type=regist";

	public static final String URL_USER_SERVE_USERPRIVATE = API_DOMAIN_M +
			"wap/app/agreementDetail?htmlName=app/managemoney/agreement/agreement_privacy";
//	m.jindanlicai.com/wap/app/agreement?type=regist
//	public static final String URL_DHB_USER_SERVE = API_DOMAIN_M + "wap/fixed/agreement?na=app";
//	public static final String API_URL_PRIVATE_TATOL_MONEY = API_DOMAIN_M + "wap/bid/totalMoney?na=app";

	public static void setApiDomain(String domain) {

		ConstantUtil.NEW_BAPI_NAME = domain;
		//默认访问BAPI域名
		ConstantUtil.NEW_BAPI_URL =  ConstantUtil.NEW_BAPI_NAME + ConstantUtil.NEW_API_URL_SUFFIX;

		SPTOOL.putString("xapi", ConstantUtil.NEW_BAPI_NAME);
		reInit();
	}

	public static void setDefaultApiDomain() {

		NEW_BAPI_NAME = NEW_BAPI_NAME_DEFAULT;

		//默认访问BAPI域名
		NEW_BAPI_URL = NEW_BAPI_NAME + NEW_API_URL_SUFFIX;

		SPTOOL.putString("xapi", ConstantUtil.NEW_BAPI_NAME);

		reInit();
	}

	public static void reInit() {

		/**
		 * 上传持证照片
		 */
		SERVER_URL_TAG_42 = ConstantUtil.NEW_BAPI_URL + "user_card";
		/**
		 * 上传头像
		 */
		SERVER_URL_TAG_39 = ConstantUtil.NEW_BAPI_URL + "user_avatar";

	}


	// zsz new start

	public static final String SERVER_URL_NAME_LOGIN_SMS = "login_by_sms";

	public static final String SERVER_URL_NAME_LOGIN_PWD = "login_by_password";

	public static final String SERVER_URL_NAME_REGISTER = "register";

	public static final String SERVER_URL_NAME_USER_INFO = "getUserInfo";

	public static final String SERVER_URL_NAME_LOGINPWD_SET = "set_password";

	public static final String SERVER_URL_NAME_LOGINPWD_RESET = "reset_password";

	public static final String SERVER_URL_NAME_PAYPWD_RESET = "reset_pay_password";

	public static final String SERVER_URL_NAME_PAYPWD_SET = "set_pay_password";

	public static final String SERVER_URL_NAME_LOGOUT = "logout";

	public static final String SERVER_URL_NAME_SMS_CODE_LOGINPWD_RESET = "reset_password_send_code";

	public static final String SERVER_URL_NAME_CHECK_IDCARD_RESET = "check_id_card";

	public static final String SERVER_URL_NAME_CHECK_PAY_PWD = "check_pay_password";
	/** 用户页面列表数据 */
	public static final String SERVER_URL_OWER_LIST_INFO = "personal_menus";

	public static final String SERVER_URL_NAME_BIAO_DETAIL = "index_bidding_detail";

	public static final String SERVER_URL_NAME_BIAO_LIST_MAIN = "index_bidding";

	public static final String SERVER_URL_NAME_BIAO_HISTORY_LIST = "history_bidding";

	public static final String SERVER_URL_NAME_PRE_ORDER = "subscribe";

	public static final String SERVER_URL_NAME_TAG_8 = "sendVerifyCode";

	public static final String SERVER_URL_NAME_BANK_LIST = "get_bank_list";

	public static final String SERVER_URL_NAME_TOTAL_AMOUNT = "get_total_amount";

	public static final String SERVER_URL_NAME_CHECK_FOR_UPDATE = "check_for_update";

	public static final String SERVER_URL_NAME_SMS_CODE_VERIFY_LOGINPWD_RESET = "check_verify_code";

	public static final String SERVER_URL_NAME_USER_BEHAVIOR_LOG = "user_behavior_log";
	/** 帮助信息 */
	public static final String SERVER_URL_HELP_COMMON = "help_center";

	public static final String SERVER_URL_NAME_DISCOVERY = "discovery_index";

	public static final String SERVER_URL_NAME_SHARE_INFO = "share_activity_info";

	public static final String SERVER_URL_NAME_LIST_HTML_DOM = "get_html_doc";
	/** 获取产品图 */
	public static final String GET_PRODUCT_IMAGE = "get_resource_info";
	/** 获取首页和详情页banner图 */
	public static final String SERVER_URL_NAME_GET_IMAGE_RESOURCE = "get_image_resource";
	/**获取首页弹窗*/
	public static final String SERVER_URL_NAME_GET_POPWINDOW_URL = "getPopWindowUrl";
	/***首页了解金蛋理财*/
	public static final String SERVER_URL_NAME_GET_README = "readme";

	/** 函数名称: 获取消息Tip */
	public static final String SERVER_URL_NAME_GET_MESSAGE_TIP = "getMessageTip";
	/** 函数名称: 获取消息列表 */
	public static final String SERVER_URL_GET_MESSAGE_LIST = "getMessageList";
	/** 函数名称: 设置消息已读 */
	public static final String SERVER_URL_SET_MESSAGE_READ = "setMessageRead";

	//	/** 新手礼包 */
//	public static final String SERVER_URL_NOVICE_PACKS = "novice_packs";
//	public static final String SERVER_URL_NAME_RATE_COMPARE = "rate_comparsion";
//	public static final String SERVER_URL_NAME_PROMOTE_MAIN = "get_promote";
// 	public static final String SERVER_URL_NAME_ANNOUNCEMENT_MAIN = "get_announcement";
//	public static final String SERVER_URL_NAME_BANNER_MAIN = "get_banner";
//	public static final String SERVER_URL_NAME_BANNER_GOODS = "get_goods_banner";
//	public static final String SERVER_URL_NAME_SAFTY = "security_index";
//	public static final String SERVER_URL_NAME_CALLBACK_POP_INFO = "has_activity_pop";
//	public static final String SERVER_URL_NAME_ACTIVITY_POP_INFO = "get_activity_pop";
//	public static final String SERVER_URL_NAME_NEWS_LIST = "get_notification_list";
//	public static final String SERVER_URL_NAME_NEWS_COUNT_UNREAD = "unread_count";
//	public static final String SERVER_URL_NAME_REGISTER_TOKEN = "registe_token";


	/**
	 * V6接口定义
	 */
	/** 首页推荐位新版本V6 */
	public static final String SERVER_URL_NAME_HOME_PROMOTE = "getIndexMenu";
	/** 新版本V6首页标的列表 */
	public static final String SERVER_URL_NAME_HOME_INDEXBIDDING = "indexBidding";
	/** 理财页面标的列表 */
	public static final String SERVER_URL_NAME_FINANCE_PRODUCTLIST = "productList";

	public static final String SERVER_URL_NAME_PAYINVEST = "payInvest";
	/** 获取下拉刷新文案 */
	public static final String SERVER_URL_NAME_GET_REFRESH_TIP = "getRefreshTip";
//	/** 获取发现页Grid数据 */
//	public static final String SERVER_URL_NAME_GET_DISCOVERY_GRID = "getDiscoveryGrids";
//	/** 获取发现页List数据 */
//	public static final String SERVER_URL_NAME_GET_DISCOVERY_LIST = "getDiscoveryList";


	/**
	 * V7接口定义
	 */
	/** 函数名称: 获取债权列表list v7 */
	public static final String SERVER_URL_NAME_GET_TODAY_CREDIT = "getTodayCredit";
	/** 函数名称: 获取首页债权列表 今日债权动态 */
	public static final String SERVER_URL_NAME_GET_NEW_CREDIT = "getNewCredit";

	public static final String SERVER_URL_TODAY_BIDDING_RECORD = "todayBiddingRecord";	// 今日标的记录


	/**
	 * V8接口定义
	 */
	public static final String SERVER_URL_GET_SET_PASSWORD_SIGN = "getSetPasswordSign";
	/** 函数名称: 银行托管开户 */
	public static final String SERVER_URL_NAME_OPEN_BANK_ACCOUNT = "openJxBankAccount";

	public static final String SERVER_URL_NAME_CONFIG_RECHARGE = "getRechargeConfig";

	public static final String SERVER_URL_NAME_MY_COUPONS = "getMyCoupons";

	public static final String SERVER_URL_NAME_CONFIG_INVEST = "getInvestConfig";
	/** 获取赎回配置 */
	public static final String SERVER_URL_NAME_GET_REDEEM_CONFIG = "getRedeemConfig";
	/** 获取提现配置 */
	public static final String SERVER_URL_NAME_GET_WITHDRAW_CONFIG = "getWithdrawConfig";
	/** 创建充值订单 */
	public static final String SERVER_URL_NAME_GET_RECHARGE_ORDERID = "createRechargeOrder";
	/** 充值确认 */
	public static final String SERVER_URL_NAME_GET_RECHARGE_CONFIRM = "sureRechargeOrder";
	/** 创建投资订单 */
	public static final String SERVER_URL_NAME_INVEST_CREATE = "createInvestOrder";
	/** 投资确认 */
	public static final String SERVER_URL_NAME_PAY_INVEST = "sureInvestOrder";
	/** 创建转出订单 */
	public static final String SERVER_URL_NAME_REDEEM_ORDERID = "createRedeemOrder";
	/** 转出确认 */
	public static final String SERVER_URL_NAME_SURE_REDEEM_ORDER = "sureRedeemOrder";
	/** 创建提现订单 */
	public static final String SERVER_URL_NAME_CREATE_WITHDRAW_ORDER = "createWithdrawOrder";

	public static final String SERVER_URL_NAME_CHANGE_BANK_MOBILE = "changeBankMobile";

	public static final String SERVER_URL_NAME_REGION = "getRegion";

	public static final String SERVER_URL_NAME_BANKSITE = "getBankSite";
	/** 投资页余额查询 */
	public static final String SERVER_URL_NAME_GET_INVEST_BALANCE = "getBalance";
	/** 首页获取奖励利率 */
	public static final String SERVER_URL_NAME_GET_AWARD_RATE = "getAwardRate";

	public static final String SERVER_URL_NAME_BANK_LIST_NEW = "getChannelBankList";

//	public static final String SERVER_URL_NAME_SURE_WITHDRAW_ORDER = "sureWithdrawOrder";


	/**
	 * V9接口定义
	 */
	/** 首页标的推荐更多 */
	public static final String SERVER_URL_GET_INDEX_BUTTON_MENU = "getIndexBottomMenu";

	public static final String SERVER_URL_SET_DOT_CLICK = "setDotClick";


	/**
	 * V10接口定义
	 */
	public static final String SERVER_URL_NAME_GET_DISCOVERY_GRID = "getDiscoveryTopMenu";

	public static final String SERVER_URL_NAME_GET_DISCOVERY_MIDDLE_MENU = "getDiscoveryMiddleMenu";

	public static final String SERVER_URL_NAME_GET_DISCOVERY_BOTTOM_MENU = "getDiscoveryBottomMenu";

	public static final String SERVER_URL_NAME_GET_DISCOVERY_LIST = "getNewsList";

	public static final String SERVER_URL_SETTING_MENU = "getSettingMenu";


	/**
	 * V11接口定义
	 */
	/** 尊享金蛋 */
	public static final String SERVER_URL_EXCLUSIVE_LIST = "zxBiddingList";


	// 以下是老版本独有的接口
	/**
	 * 以下是非存管版本独有接口
	 */

	public static final String SERVER_URL_NAME_PAY_REDEEM = "payRedeem";

	public static final String SERVER_URL_NAME_BANK_BIND = "bind_card_v2_1";

	public static final String SERVER_URL_NAME_BANK_BIND_STATUS = "bind_card_status";
	/**
	 * 新的绑卡接口
	 */
	public static final String SERVER_URL_New_NAME_BANK_BIND = "rebind_card";

	public static final String SERVER_URL_NAME_BANK_BIND_VERIFY = "bind_card_verify";
	/**
	 * 重新发送金额
	 */
	public static final String SERVER_URL_NAME_BIND_CARD_BY_MONEY = "bind_card_by_money";
	/**
	 * 金额验证
	 */
	public static final String SERVER_URL_NAME_BIND_CARD_VERIFY_BY_MONEY = "bind_card_verify_by_money";
	/**
	 * 获取投资提现配置
	 */
	public static final String SERVER_URL_NAME_GET_INVEST_AND_REDEEM_CONFIG = "getInvestAndRedeemConfig";

	public static final String SERVER_URL_NAME_BANK_BIND_GET_VERIFY_CODE = "bind_card_sms";
	/**
	 * 获取投资提现的配置信息
	 */
	public static final String SERVER_URL_NAME_USER_CONFIG = "getInvestAndRedeemConfig";

	public static final String SERVER_URL_GET_HTML_DOC = "get_html_doc";
	public static final String SERVER_URL_GET_LOGIN_SOURCE = "login_resource";

//	public static final String SERVER_URL_NAME_ERROR = "err";
//	public static final String SERVER_URL_CRASH_INFO = "crash_info";
//	public static final String SERVER_URL_NAME_REDEEM_LIST = "redeem_list";
//	public static final String SERVER_URL_NAME_INVEST_LIST = "invest_list";
//	public static final String SERVER_URL_NAME_INVEST_STATUS = "total_money";
//	public static final String SERVER_URL_NAME_INCOME_STATUS = "income_list";
//	public static final String SERVER_URL_NAME_BANK_BIND = "bind_card";


	/**
	 * 上传持证照片
	 */
	public static String SERVER_URL_TAG_42 = NEW_BAPI_URL + "user_card";
	/**
	 * 上传头像
	 */
	public static String SERVER_URL_TAG_39 = NEW_BAPI_URL + "user_avatar";


//	public static final int SMS_CODE_TYPE_DEFAULT = 0;
	/** 登录验证 */
	public static final int SMS_CODE_TYPE_LOGIN = 1;
	/** 注册验证 */
	public static final int SMS_CODE_TYPE_REGISTER = 2;
	/** 修改各种密码 */
	public static final int SMS_CODE_TYPE_RESET = 3;
	/** 绑卡验证 */
	public static final int SMS_CODE_TYPE_BIND_BANK = 4;
	/** 开户验证 */
	public static final int SMS_CODE_TYPE_ACCOUNT = 5;
	/** 支付密码验证 */
	public static final int SMS_CODE_TYPE_OLD_PAY_PASSWORD = 6;
	/** 交易密码验证(前置验证手机号，验证通过后调用第三方修改交易密码) */
//	public static final int SMS_CODE_TYPE_CHANGE_TRADE_PASSWORD = 6;
	public static final int SMS_CODE_TYPE_CHANGE_BANK_PHONE = 7;
	/** 验证码认证类型 */
	public enum VerifyActionType {
//		DEFAULT(SMS_CODE_TYPE_DEFAULT),
		LOGIN(SMS_CODE_TYPE_LOGIN),
		REGISTER(SMS_CODE_TYPE_REGISTER),
		OPEN_ACCOUNT(SMS_CODE_TYPE_ACCOUNT),

//		CHANGE_LOGIN_PASSWORD(SMS_CODE_TYPE_RESET),
		CHANGE_TRADE_PASSWORD(SMS_CODE_TYPE_RESET),

		CHANGE_TRADE_OLD_PASSWORD(SMS_CODE_TYPE_OLD_PAY_PASSWORD),

		BIND_CARD(SMS_CODE_TYPE_BIND_BANK),

		CHANGE_BANK_PHONE(SMS_CODE_TYPE_CHANGE_BANK_PHONE);
//		CHANGE_TRADE_PASSWORD(SMS_CODE_TYPE_CHANGE_TRADE_PASSWORD);

		private int value;
		private VerifyActionType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	/** 短信验证 */
	public static final int VERIFY_TYPE_SMS = 1;
	/** 语音验证 */
	public static final int VERIFY_TYPE_VOICE = 2;

	/**
	 * WebView返回按钮页面定义
	 * 说明: 根据来源确定返回页面的操作
	 *
	 */
	public enum BackType {
		//直接关闭当前页
		NONE(0),
		//首页
		HOME(1);

		private int value;
		private BackType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static BackType from(int value) {
			if(value == HOME.getValue()) {
				return HOME;
			}
			return NONE;
		}
	}

	/** NET Protocol Tag start **/
	public static final int Net_Invalid_Tag = -1;

	// public static final int Net_Tag_RegistWithName = 2;

	public static final int Net_Tag_UserLogin_Pwd = 110;

	public static final int Net_Tag_UserLogin_Sms = 111;

	public static final int Net_Tag_RegistWithPhone = 112;

	public static final int Net_Tag_User_LoginPwd_Set = 113;

//	public static final int Net_Tag_User_LoginPwd_Reset = 114;

//	public static final int Net_Tag_Banner_Main = 115;

	public static final int Net_Tag_Promote_Main = 116;

//	public static final int Net_Tag_Announcement_Main = 117;

	public static final int Net_Tag_Invest_OrderId = 118;

	public static final int Net_Tag_Total_Amount = 119;

	public static final int Net_Tag_Main_Biao_List = 120;

	public static final int Net_Tag_Biao_Detail = 121;

	public static final int Net_Tag_Biao_History_List = 122;

	public static final int Net_Tag_Bank_List = 123;

	public static final int Net_Tag_Bind_BankCard = 124;

	public static final int Net_Tag_BindBandCard_Verify = 125;

	public static final int Net_Tag_BindBandCard_Sms_Code = 126;

	public static final int Net_Tag_Invest_Commit = 127;

	public static final int Net_Tag_Redeem_Commit = 128;

	public static final int Net_Tag_Redeem_OrderId = 129;

	public static final int Net_Tag_Redeem_List = 130;

	public static final int Net_Tag_Invest_List = 131;

	public static final int Net_Tag_Invest_Status_List = 132;

	public static final int Net_Tag_Income_Status_List = 133;

	public static final int Net_Tag_Discovery_Html = 134;

//	public static final int Net_Tag_Safty_Html = 135;

	public static final int Net_Tag_Goods_List = 136;

//	public static final int Net_Tag_Get_Push_News_List = 137;

	public static final int Net_Tag_Get_UserInfo = 138;

	public static final int Net_Tag_About_Us = 139;

	public static final int Net_Tag_WebChat = 140;

	/**
	 * 设置支付密码
	 */
	public static final int Net_Tag_SetPayPwd = 141;

	/**
	 * 重置支付密码
	 */
	public static final int Net_Tag_Retrieve_Pwd = 142;

	/**
	 * 重置登录密码
	 */
	public static final int Net_Tag_Retrieve_LoginPwd = 143;

	/**
	 * 重置登录密码 -发送验证码
	 */
	public static final int Net_Tag_Get_Sms_Code_LoginPwd_Reset = 144;

	/**
	 * 重置登录密码 -验证验证码
	 */
	public static final int Net_Tag_Get_Sms_Code_Verify_LoginPwd_Reset = 145;

	/**
	 * 重置登录密码 -验证验证码
	 */
	public static final int Net_Tag_Check_IdCard_LoginPwd_Reset = 146;

	/**
	 * 重置登录密码 -验证验证码
	 */
	public static final int Net_Tag_Check_Old_Pwd = 147;

	/**
	 * 预售投资
	 */
	public static final int Net_Tag_Request_Pre_Order = 149;

	/**
	 * 检查更新
	 */
	public static final int Net_Tag_App_Update = 150;

	/**
     * 
     */
	public static final int Net_Tag_Share_Detail_Info = 151;

//	public static final int Net_Tag_Active_Final = 152;

	public static final int Net_Tag_Active_Pop_Callback = 153;

	public static final int Net_Tag_Upload_Log = 154;
//	/**
//	 * 验证金额
//	 */
//	public static final int Net_Tag_BindBandCard_Money_Verify = 155;

	/**
	 * 重新发送金额
	 */
//	public static final int Net_Tag_BindBandCard_Get_Money = 156;
	/**
	 * 新的绑卡
	 */
	public static final int Net_Tag_NEW_Bind_BankCard = 157;

	/**
	 * 标的详情页Banner
	 */
	public static final int Net_Tag_Banner_Goods = 158;
	/**
	 * 获取产品图
	 */
	public static final int Net_Product_Image = 159;
	/**
	 * 今日标的记录
	 */
	public static final int Net_Tag_Today_Bidding_Record = 160;
	
	public static final int Net_Tag_Ower_List = 161;

//	public static final int Net_Tag_Novice_Packs = 162;
	
	//组合接口
	public static final int Net_Tag_Group_Home = 163;
	//组合接口
	public static final int Net_Tag_Group_Home_BD = 1163;
	
	public static final int Net_Tag_Group_Ower = 164;
	
	/**
	 * 首页和详情页的Banner图
	 */
	public static final int Net_Tag_Banner_Get_Image_Resource = 165;
	
//	public static final int Net_Tag_Rate_Compare = 166;
	
	
	public static final int Net_Tag_Help_Common = 167;

	//发现页组合接口
	public static final int Net_Tag_Group_Discover = 168;

	public static final int Net_Tag_Discover_Grid = 169;

	public static final int Net_Tag_Discover_List = 170;
	
	public static final int Net_Tag_BankCard_Bind_Status = 171;
	/**
	 * 新版本首页推荐为V6
	 */
	public static final int Net_Tag_Request_Home_Promote = 172;

	/**
	 * 首页标的列表V6
	 */
	public static final int Net_Tag_Request_Home_Indexbidding = 173;
	
	/**
	 * 理财标的列表V6
	 */
	public static final int Net_Tag_Request_Finance_ProductList = 174;

	//理财页组合接口
	public static final int Net_Tag_Group_Finance_v6 = 175;
	
	/**
	 * 用户投资提现配置信息
	 */
	public static final int Net_Tag_Request_User_Config = 176;
	/** 获取消息中心消息列表 */
	public static final int Net_Tag_Get_Push_News_List_V6 = 177;
	/** 设置消息是否已读 */
	public static final int Net_Tag_Set_News_Is_Read = 178;
	/** 获取消息Tip */
	public static final int Net_Tag_Get_News_Tip = 179;
	/** 获取下拉刷新文案 */
	public static final int Net_Tag_Get_Refresh_Tip = 180;	
	
	
	/**
	 * 首页债权
	 */
	public static final int Net_Tag_Request_New_Credit = 181;
	
	/**
	 * 债权列表
	 */
	public static final int Net_Tag_Request_Today_Credit = 182;
	/**
	 * 银行托管开户
	 */
	public static final int Net_Tag_Request_Open_Bank_Account = 183;
	/*
	 * 确认赎回
	 */
	public static final int Net_Tag_Sure_Redeem_Order = 184;
	/**
	 * 充值订单
	 */
	public static final int Net_Tag_Recharge_OrderId = 185;
	/** 确认充值 */
	public static final int Net_Tag_Recharge_Commit = 186;

	/**
	 * 提现
	 */
	public static final int Net_Tag_Create_Withdraw_Order = 187;

	/**
	 * 确认提现
	 */
	public static final int Net_Tag_Sure_Withdraw_Order = 188;
	/** 获取设置交易密码签名接口 */
	public static final int Net_Tag_Request_Get_Set_Password_Sign = 189;

	/**
	 * 用户赎回配置信息
	 */
	public static final int Net_Tag_Redeem_Config = 190;

	/**
	 * 用户充值配置信息
	 */
	public static final int Net_Tag_Request_Recharge_Config = 191;

	/**
	 * 用户提现配置信息
	 */
	public static final int Net_Tag_Withdraw_Config = 192;

	//用户投资配置信息
	public static final int Net_Tag_Request_Invest_Config = 193;

	/**
	 *
	 */
	public static final int Net_Tag_Request_Change_Bank = 194;

	/**
	 * 投资页余额查询
	 */
	public static final int Net_Tag_Request_Invest_Balance = 195;

	/**
	 * 大额提现，获取省市
	 */
	public static final int Net_Tag_Request_Get_Region = 196;

	/**
	 * 大额提现，获取网点
	 */
	public static final int Net_Tag_Request_Get_Bank_Site = 197;

	/**
	 * 首页奖励利率
	 */
	public static final int Net_Tag_Request_Get_Award_Rate = 198;

	/**
	 * 首页奖励利率
	 */
	public static final int Net_Tag_Request_Get_Html_Doc = 199;


	public static final int Net_Tag_Bank_List_New = 200;

	public static final int Net_Tag_Get_Index_Button_Menu = 201;

	public static final int Net_Tag_Set_Dot_Click = 202;


	public static final int Net_Tag_Discover_Middle_Menu = 203;

	public static final int Net_Tag_Discover_Bottom_Menu = 204;

//	public static final int Net_Tag_Discover_Get_Image_Resource = 205;

	public static final int Net_Tag_Setting_Menu = 206;

	public static final int Net_Tag_Request_Exclusive_List = 207;

	//用户红包加息券配置信息
	public static final int Net_Tag_Request_Coupons_List_Info= 208;

	public static final int Net_Tag_Request_POpWindow_Url= 209;


	//统一获取postdata 和url 的请求
	public static final int Net_Tag_Get_PostData_Url = 210;

	/**了解金蛋理财**/
	public static final int Net_Tag_Get_Readme_Url = 211;


	/**了解金蛋理财**/
	public static final int Net_Tag_Get_LoginSource_Url = 212;


	//v7 start
	public static final int Net_Tag_Invest_OrderId_V7 = 1118;

	//v7  end


	//灾备相关
	public static final int Net_Tag_Crash_Info = 999;
	public static final String SP_NAME_CRASH_KEY = "crash_back_key";
	

	public static final int Net_Tag_ModifyPhone = 7;

	public static final int Net_Tag_VerifyCode = 8;

	public static final int Net_Tag_Active_Invitation_Detail = 11;

	public static final int Net_Tag_Activity_New_Info = 12;
	public static final int Net_Tag_Goods_New_Info = 13;
	public static final int Net_Tag_Goods_Detail = 14;

	public static final int Net_Tag_Invest = 15;

	public static final int Net_Tag_Redeem = 16;

	public static final int Net_Tag_Total_Money_Profit = 23;

	public static final int Net_Tag_Reward_List = 24;

	public static final int Net_Tag_Verify_Pwd = 29;

	public static final int Net_Tag_Credit_Detail = 30;

	public static final int Net_Tag_Total_User_Sum = 34;

//	public static final int Net_Tag_Push_Register = 35;

	public static final int Net_Tag_Goods_New_List = 37;

	public static final int Net_Tag_Upload_Avatar = 39;

	// 首页活动弹窗，接口已废弃
//	public static final int Net_Tag_Request_Main_Pop = 40;
	/**
	 * 复投列表
	 */
	public static final int Net_Tag_CV_List = 43;

	public static final int Net_Tag_Advert_Latest_Info = 46;

	public static final int Net_Tag_LogOut = 100;

	public static final int Net_Tag_FeedBack = 101;

	/** user constant state */
	public static final int USER_STATE_VISITOR = 1;

	public static final int USER_STATE_LOGIN = 2;

	public static final int USER_STATE_LOGOUT = 3;

	// DAIZHUCE USER FROM 1.3.0
	public static final int USER_TYPE_VISITOR = 5;

	public static final int USER_LOGINTYPE_DUOKU = 1;

	public static final int USER_LOGINTYPE_BAIDU = 2;

	public static final int USER_TYPE_COMMON = 1;

	public static final int USER_TYPE_VIP = 2;

	public static final int USER_TYPE_CONSUMER = 3;

	public static final int USER_TYPE_NO_DAY = 4;

	public static boolean isVipUser = false;

	// push
	public static final String SP_PUSH_INFO = "setting_push.ini";

	public static final String SP_PUSH_INFO_STATE = "setting_push_state";

	public static final String SP_PUSH_INFO_INIT = "setting_push_init";

	public static final String SP_PUSH_INFO_MSG = "setting_push_msg";

	public static final String SP_PUSH_INFO_MAIN_STATE = "setting_push_main_state";

	public static final String SP_PUSH_INFO_MSG_APPID = "appid";

	public static final String SP_PUSH_INFO_MSG_CHANNELID = "channel_id";

	public static final String SP_PUSH_INFO_MSG_USERID = "user_id";

	public static final String BAIDU_PUSH_API_KEY = "GeOoegK2kHSIV0ycp5M7WmSI";

	public static final String BAIDU_PUSH_SCERET_KEY = "UhseG9FTAwfYkU9lfneTvuNhggoSZ2i2";

	// filename
	public static final String FILE_NAME_CAMERA_TEMP = "cameraTemp.jpg";

	public static final String FILE_NAME_IMAGECUT_TEMP = "imageCutTemp.jpg";

	public static final String SP_SOFTUPDATE_NEW_BUILD = "softupdate_new_build";

	public static final String SP_SOFTUPDATE_URL = "softupdate_url";

	public static final String SP_SOFTUPDATE_CONTENT = "softupdate_content";

	public static final String SP_SOFTUPDATE_IS_FORCE = "softupdate_is_force";

	public static final String SP_SOFTUPDATE_NEW_VISION = "softupdate_new_vision";

	public static final String SP_ACTIVE_PIC_LOCAL_PATH_SAVEFILE = "active_pic_local_path_savefile";

	public static final String SP_ACTIVE_IS_SHOW = "active_is_show";

	public static final String SP_ACTIVE_TIME = "active_time";

	public static final String SP_ACTIVE_POP_ID = "active_pop_id";

	public static final String SP_ACTIVE_POP_URL = "active_pop_url";
	
	public static final String SP_LOGIN_ACCOUNT_CACHE = "login_account_cache";
	
	public static final String SP_REGISTER_ACCOUNT_CACHE = "register_account_cache";

	
	// active
	/**
	 * 组成：id_imgurl
	 */
	public static final String SP_ACTIVE_BANNER_ACTIVE_ID = "active_banner_active_id";
	public static final String SP_ACTIVE_BANNER_PIC_LOCAL_PATH_SAVEFILE = "active_banner_pic_local_path_savefile";

	public static final String SP_ACTIVE_BANNER_IMG_URL = "active_banner_img_url";

	public static final String SP_ACTIVE_BANNER_TITLE = "active_banner_title";
	/** 首页消息Tip */
	public static final String SP_HOME_MESSAGE_TIPS = "home_message_tips";
	/** 锁屏冷却时间 */
	public static final String SP_LOCK_COOL_DOWN_TIME = "lock_cool_down_time";
	
	/**
	 * 活动跳转地址
	 */
	public static final String SP_ACTIVE_BANNER_ULR = "active_banner_url";

	public static final String SP_OVERBAR_START_LOCAL_TIME = "overbar_start_local_time";

	public static final String SP_OVERBAR_SERVER_TIME = "overbar_server_time";

	public static final int LIST_PAGE_SIZE = 20;

	public static final String KEY_HTML_USER_ID = "u_id";
	public static final String KEY_HTML_TOKEN = "token";
	public static final String KEY_HTML_DEVICE_ID = "device_id";
	public static final String KEY_HTML_VERSION = "version";

	public static final String SERVICE_NAME_LOG_UPDATE = "com.jindan.p2p.service.logupdate";

	public static String COUNT_MESSAGE_USER = "count_message_user";

	public static String COUNT_ACTIVE_CUSTOM = "count_active_custom";
	
	public static String DISCOVER_COUNT_ACTIVE_CUSTOM = "count_active_custom_discover";

	public static String COUNT_MESSAGE_NOTICE = "count_message_notice";
	
	public static String OWER_LIST_ITEM_NOTICE = "ower_list_item_notice";
	
	/** 关键字: 开户对话框MESSAGE */
	public static final String KEY_DIALOG_MESSAGE = "dialog_message";
	/** 关键字: 开户对话框HINT */
	public static final String KEY_DIALOG_HINT = "dialog_hint";
	/** 关键字: 开户对话框开户按钮TEXT */
	public static final String KEY_DIALOG_BUTTON_TEXT = "dialog_button_text";
	/** 关键字: 开户对话框开户TIP TEXT */
	public static final String KEY_DIALOG_TIP_TEXT = "dialog_tip_text";
	/** 关键字: 开户对话框开户按钮跳转URL */
	public static final String KEY_DIALOG_BUTTON_URL = "dialog_button_url";
	/** 关键字: 开户对话框数据信息 */
	public static final String KEY_DIALOG_DATA = "dialog_data";
	/** 关键字: 二级WEB页URL */
	public static final String KEY_URL = "url";
	/** 关键字: 返回按钮跳转的页面 */
	public static final String KEY_BACK_TYPE = "back_type";
	/** 关键字: 二级WEB页标题 */
	public static final String KEY_TITLE = "title";
	/** 关键字: ActiveWebActivity标题栏右边按钮 */
	public static final String KEY_TITLEBAR_RIGHT_TEXT = "titlebar_right_text";
	/** 关键字: 按钮动作类型 */
	public static final String KEY_TITLEBAR_RIGHT_ACTION_TYPE = "titlebar_right_action_type";
	/** 关键字: 按钮动作跳转URL */
	public static final String KEY_TITLEBAR_RIGHT_ACTION_URL = "titlebar_right_action_url";
	/** 跳转OPEN URL */
	public static final String KEY_OPEN_URL = "open_url";
	/** 返回动作BACK URL */
	public static final String KEY_BACK_URL = "back_url";
	/** 是否初次打开 */
	public static final String KEY_FIRST_OPEN = "first_open";
	/** POST参数 */
	public static final String KEY_POST_PARAM = "post_param";
	/** webviewactivity 参数 :function name 需要请求的接口名字 */
	public static final String KEY_FUN_NAME_WEBVIEW = "fun_name_webview";
	/** 发送验证码动作类型 */
	public static final String KEY_VERIFY_ACTION_TYPE = "verifyType";
	/** 对话框动作类型 */
	public static final String KEY_DIALOG_ACTION_TYPE = "dialog_action_url";
	/** 手机号 */
	public static final String KEY_PHONE = "phone";
	/** 旧手机号 */
	public static final String KEY_OLD_PHONE = "oldMobile";
	/** 余额 */
	public static final String KEY_BALANCE = "balance";
	/** 首页标的推荐更多已读type */
	public static final String KEY_INDEX_BUTTOM_MENU = "indexBottomMenu";

	public static String SP_NAME_HELP_COMMON_TOTAL_INCOME = "help_common_total_income";
	public static String SP_NAME_HELP_COMMON_MY_DEPOSIT = "help_common_my_deposit";
	/** 债权动态数据缓存时间 */
	public static String SP_NAME_CREDITOR_UPDATE_TIME = "creditor_update_time";
	
	public static String URL_HELP_COMMON_INDEX = "http://jindanlicai.com/help_background";//默认帮助地址
	
	public static String APPID_WEIXIN_OPENAPI = "wx72607e32ec65d0eb";
	
	public static String APPSECRET_WEIXIN_OPENAPI = "6d78ad48ce2e1e904ff85c0532049e28";
	/** QQ AppId */
	public static String APPID_QQ_OPENAPI = "1104065421";

	/**
	 * 活期 0活期 1新手标 2加息卡 3定活宝
	 */
	public static final int GoodsTypeCurrent = 0;
	/**
	 * 新手标
	 */
	public static final int GoodsTypeNew = 1;
	/**
	 * 加息卡
	 */
	public static final int GoodsTypeRateCard = 2;
	/**
	 * 定期
	 */
	public static final int GoodsTypeDingqi = 3;
	
	/** 消息ID: 显示进度页面 */
	public static final int MSG_SHOW_LOADING = 500;
	/** 数据加载完成 */
	public static final int MSG_GET_DATA_SUCCESS = 501;
	/** 加载更多数据完成 */
	public static final int MSG_GET_MORE_DATA_SUCCESS = 502;
	/** 数据加载失败 */
	public static final int MSG_GET_DATA_FAILED = 503;
	/** Item刷新 */
	public static final int MSG_REFRESH_LISTITEM = 504;

	public static String BBS_BASE_URL = "https://bbs.jindanlicai.com/forum.php?mod=guide&view=hot&mobile=2";

	//登录密码长度最少位数限制
	public static final int LOGIN_PASSWORD_LESSLENGTH = 8;
	//登录密码长度最长位数限制
	public static final int LOGIN_PASSWORD_MORELENGTH = 20;

	public static final String  HOME_ACTIVE_POPTIME = "home_active_time";//首页弹窗记录值

	public static final String FUN_NAME_SET_PWD_XW_SIGN = "getSetPasswordSign";




}
