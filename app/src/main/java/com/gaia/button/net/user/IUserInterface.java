package com.jindan.p2p.user;

public interface IUserInterface {

	/**
	 * User regist with phone number It type is REQUEST_REGIST_WITH_PHONE
	 */
	void requestRegistWithPhoneNumber(IUserListener observer, String phonenumber, String msmcode);

	/**
	 * 获取产品活动图
	 * @param observer
	 */
	void requestSplashAdImage(IUserListener observer);

	void requestBindBankCardVerify(IUserListener observer, String msmcode);

	/**
	 * User login by sms
	 *
	 * @param observer
	 * @param phonenumber 登录账号
	 * @param msmcode  验证码
	 */
	void requestLoginBySms(IUserListener observer, String phonenumber, String msmcode);

	/**
	 * User login by password
	 * 
	 * @param observer
	 * @param phonenumber 登录账号
	 * @param pwd 密码
	 */
	void requestLoginByPwd(IUserListener observer, String phonenumber, String pwd);

	/**
	 * User set login pwd
	 * 
	 * @param observer
	 * @param newPwd 新密码
	 * @param oldPwd 旧密码
	 * @param ck cookie
	 * @param mobile 手机号
	 */
	void requestSetLoginPwd(IUserListener observer, String newPwd, String oldPwd, String ck, String mobile);

	/**
	 * 公用信息
	 */
	void requestTotalAmountInfo(IUserListener observer);

	/**
	 * 历史标的
	 */
	void requestBiaoHistoryListInfo(IUserListener observer, int pageNum, int pageSize);

	/**
	 * 银行列表
	 */
	void requestBankList(IUserListener observer, int pageNum, int pageSize);

	/**
	 * 银行列表
	 */
	void requestBankListNew(IUserListener observer, int pageNum, int pageSize);

	/**
	 * 绑卡之验卡
	 */
	void requestBindBankcard(IUserListener observer, String username, String idCard, String bankcard, String bankphone,
			String bankcardid);

	/**
	 * 新的绑卡
	 * 
	 * @param observer
	 * @param bankcard
	 * @param bankphone
	 * @param bankcardid
	 */
	void requestNewBindBankcard(IUserListener observer, String bankcard, String bankphone, String bankcardid);

	/**
	 * 获取用户最新数据
	 *
	 */
	void requestUserNewestInfo(IUserListener observer);

	/**
	 * **设置 和 修改支付密码**
	 * @param observer
	 * @param newpwd
	 * @param oldpwd
     * @param ck
	 *
     */
	void requestSetPayPwd(IUserListener observer, String newpwd, String oldpwd, String ck);

	/**
	 * 重置登录密码
	 */
	void requestRetrieveLoginPwd(IUserListener observer, String mobile, String ck);

	/**
	 * "mobile":手机号, "action_type":类型, //1(登录)、2(注册)、3(重置登录密码, mobile 给空串),
	 * "verify_type":验证码类型 //1(短信)、2(语音)
	 *
	 * @param observer
	 * @param phone
	 * @param action_type
	 * @param verifyType
     */
	void requestVerifyCode(IUserListener observer, String phone, int action_type, int verifyType);

	/**
	 * 重置登录密码--发送验证码
	 * 
	 * @param observer
	 * @param phonenumber
	 *            :手机号, //未登录必填，已登录可为空
	 * @param verifyType
	 */
	void requestSmsCodeResetPwd(IUserListener observer, String phonenumber, int verifyType);


	/**
	 * 请求更换银行卡绑定手机号
	 * @param observer
	 * @param mobile
	 * @param smsCode
     */
	void requestChangeBankPhone(IUserListener observer, String mobile, String smsCode);

	/**
	 * 重置登录密码--验证验证码
	 * 
	 * @param observer
	 * @param phonenumber
	 *            :手机号, //未登录必填，已登录可为空
	 * @param smsCode
	 */
	void requestSmsCodeVerifyResetPwd(IUserListener observer, String phonenumber, String smsCode);

	/**
	 * 重置登录密码--验证身份
	 * 
	 * @param observer
	 * @param phonenumber
	 *            :手机号, //未登录必填，已登录可为空
	 * @param idcard
	 */
	void requestCheckIdCardResetPwd(IUserListener observer, String phonenumber, String idcard);

	/**
	 * 重置支付密码--验证旧密码
	 * 
	 * @param observer
	 * @param oldPwd
	 *            :手机号, //未登录必填，已登录可为空
	 */
	void requestCheckOldPwdResetPwd(IUserListener observer, String oldPwd);

	/**
	 * 预约
	 * 
	 * @param observer
	 * @param goodsId
	 */
	void requestPreOrdersInfo(IUserListener observer, String goodsId);

	// old

	/*
	 * User logout It type is REQUEST_USER_LOGOUT
	 */
	void requestLogout(IUserListener observer);

	/**
	 * 
	 * @param observer
	 * @param money
	 * @param goodsId
	 * @param type
	 * @param fromType
	 * @param bId
	 * @param refuseMatchBidding 是否同意购买下一标的  1拒绝 0同意 默认是同意
	 * @param regularAcceptDay
	 * @param couponId	红包/加息券ID
	 */
	void requestInvestOrderId(IUserListener observer,  String iUnlockConfig, String money, String goodsId, int type, int fromType, String bId, int refuseMatchBidding, String regularAcceptDay, String couponId);

	/**
	 * @param observer
	 * @param pay_pwd
	 * @param orderId
     */
	void requestInvestPay(IUserListener observer, String pay_pwd, String orderId, int fromType, String pid);

	/**
	 *
	 * @param observer
	 * @param money
     */
	void requestRechargeOrderId(IUserListener observer, String money);

	/**
	 *
	 * @param observer
	 * @param orderId
	 * @param authSeq
     */
	void requestRechargeConfirm(IUserListener observer, String orderId, String authSeq);

	/**
	 * 赎回
	 * @param observer
	 * @param money
	 * @param pId  产品id
	 * @param redeemType  赎回类型(1急速提 2 预约提)
	 * @param redeemAll  是否赎回全部（如果输入金额是最大金额传1其他传0）
	 * 
	 */
	void requestRedeemOrderId(IUserListener observer, String money, String pId, int redeemType, int redeemAll);

	/**
	 *
	 * @param observer
	 * @param money
	 * @param pId  产品id
	 * @param toType 提现至(1活期 2 银行卡)
	 * @param redeemType  提现类型(1急速提 2 预约提)
	 * @param redeemAll  是否提现全部（如果输入金额是最大金额传1其他传0）
	 *
	 */
	void requestRedeemOrderId(IUserListener observer, String money, String pId, int toType, int redeemType, int redeemAll);

	/**
	 *
	 * @param observer
	 * @param money
	 * @param goodsId
	 * @param type
	 * @param fromType
	 * @param bId
	 * @param refuseMatchBidding 是否同意购买下一标的  1拒绝 0同意 默认是同意
	 */
	void requestInvestOrderIdV7(IUserListener observer, String iUnlockConfig, String money, String goodsId, int type, int fromType, String bId, int refuseMatchBidding, String couponId);

	/**
	 * 确认赎回
	 * @param observer
	 * @param orderId
     */
	void requestSureRedeemOrder(IUserListener observer, String orderId);

	/**
	 * 提现
	 * @param observer
	 * @param money
     */
	void requestCreateWithdrawOrder(IUserListener observer, String money, String orderId, String bankSiteId);

	/**
	 *
	 * @param observer
	 * @param pay_pwd
	 * @param orderId
     * @param pId
     */
	void requestRedeemPay(IUserListener observer, String pay_pwd, String orderId, String pId);

	void requestBindBankGetVerifyCode(IUserListener observer);

	/**
	 * 获取消息列表
	 * 
	 * @param lastPublishTime： 最后一条消息的发布时间
	 */
	void requestNewsPushList(IUserListener observer, String lastPublishTime);
	/**
	 * 设置消息已读状态
	 * 
	 * @param messageId: 消息ID
	 */
	void requestNewsReadStatus(IUserListener observer, String messageId);

	/**
	 *
	 * @param observer
	 * @param activeId
	 *            活动ID,
	 * @param type
	 *            分享类型 //1,2,3微信，微博，QQ空间 4短信
	 */
	void requestShareDetailInfo(IUserListener observer, String activeId, int type);

	/**
	 *
	 * @param observer
	 * @param platform
	 * @param version
	 * @param build
     * @param isAuto
     */
	void requestCheckForUpdate(IUserListener observer, String platform, String version, int build, boolean isAuto);

	/**
	 * 获取理财页面列表数据
	 * 
	 * @param observer
	 * @param functionNames
	 */
	void requestHomeData(IUserListener observer, String functionNames);

	/**
	 * 获取理财页面列表数据
	 *
	 * @param observer
	 * @param functionNames
	 */
	@Deprecated
	void requestHomeDataBD(IUserListener observer, String functionNames);
	
	/**
	 * 获取理财页面列表数据
	 * 
	 * @param observer
	 * @param functionNames
	 */
	void requestFinanceData(IUserListener observer, String functionNames);

	/**
	 * 获取发现页面列表数据
	 *
	 * @param observer
	 * @param functionNames
	 */
	void requestDiscoverData(IUserListener observer, String functionNames);
	
	/**
	 * 获取我的页面列表数据
	 *
	 * @param observer
	 * @param functionNames
	 */
	void requestOwerData(IUserListener observer, String functionNames);

	void requestOwerMenuList(IUserListener observer, String functionNames);

	/**
	 * 获取投资提现配置
	 * @param observer
	 * @param pId
	 * @param type
     * @param bId
     */
	void requestInvestAndRedeemConfig(IUserListener observer, String pId, String type, String bId);

	/**
	 * 获取赎回配置
	 * @param observer
	 * @param pId
	 * @param type
     */
	void requestRedeemConfig(IUserListener observer, String pId, String type);

	/**
	 * 获取赎回配置
	 * @param observer
     */
	void requestWithdrawConfig(IUserListener observer);

	/**
	 *
	 * @param observer
	 * @param pId
	 * @param type
     * @param bId
     */
	void requestInvestConfig(IUserListener observer,  String pId, String type, String bId);

	/**
	 *
	 * @param observer
	 */
	void requestRedpacketListInfo(IUserListener observer, String money);

	/**
	 * 投资页余额查询
	 * @param observer
     */
	void requestInvestBalance(IUserListener observer);

	/**
	 * 获取投资提现配置
	 * @param observer
	 */
	void requestRechargeConfig(IUserListener observer);

	/**
	 * 获取右上角的帮助信息
	 * 
	 * @param observer
	 */
	void requestHelpCommonInfo(IUserListener observer);

	/**
	 * 获取今日债权列表
	 * @param observer
	 * @param direct
	 * <ul> 方向   up or down（up是刷新，down是加载更多）
	 * @param offsetId
	 * <ul> 债权ID
	 */
	void requestGetTodayCreditList(IUserListener observer, String direct, int offsetId);

	/**
	 * 银行托管电子账户开户
	 * 说明: V8+
	 *
	 * @param observer: 应答监听者
	 * @param username: 用户名
	 * @param id_card: 身份证号
	 * @param bank_card: 银行卡号
	 * @param phonenum
	 * @param sms_ode
	 * @param bankCode
	 */
	void requestOpenBankAccount(IUserListener observer, String username, String id_card, String bank_card,
								String phonenum, String sms_ode, String bankCode);

	/**
	 * 获取设置交易密码签名信息接口
	 *
	 */
	void requestGetSetPasswordSign(IUserListener observer);

	/**
	 * 大额提现，获取省市
	 * @param observer
	 * @param regionId
	 */
	void requestGetRegionInfo(IUserListener observer, String regionId);

	/**
	 * 大额提现，获取网点
	 * @param observer
	 * @param regionId
	 * @param keyword
	 */
	void requestGetBankSiteInfo(IUserListener observer, String regionId, String keyword);


	/**
	 * 大额提现，获取网点
	 * @param observer
	 * @param name
	 */
	void requestGetHtmlDocInfo(IUserListener observer, String name);

	/**
	 * 首页标的推荐更多
	 * @param observer
	 * @param dotType
	 * @param dotId
     */
	void requestSetDotClick(IUserListener observer, String dotType, String dotId);


	void requestGetSettingMenu(IUserListener observer);

	/**
	 * 尊享金蛋
	 * @param observer
	 */
	void requestExclusiveData(IUserListener observer);

	/**
	 * 统一请求的方法
	 * 只有function name 没有参数
	 * 比如请求修改交易密码的数据，
	 * @param observer
	 * @param funName
	 */
	void requestDefaultPostDataAndUrl(IUserListener observer, String funName);

	/***
	 * 获取注册福利信息
	 * @param observer
	 */
	void requestLoginSource(IUserListener observer);

}
