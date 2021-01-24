package com.gaia.button.net.user;

public interface IUserInterface {

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
		获取验证码
	 */
	void requestGetCode(IUserListener observer, String mobile);
	/**
	 找回密码
	 */
	void requestForgetPass(IUserListener observer, String mobile,String code,String newpass,String confirm);

	/**
	 找回密码
	 */
	void requestSetPass(IUserListener observer,String newpass,String confirm);
	void requestModPass(IUserListener observer,String oldpa,String newpass,String confirm);
	void requestgetDiscover(IUserListener observer,int page,String title);
	void requestCollect(IUserListener observer,String id);
	void requestProductList(IUserListener observer,int page,String title);
	void requestGetCollectIUserListener (IUserListener observer);
	void requestGetDevice (IUserListener observer);
	void requestLoginOut (IUserListener observer);
	void requestLoginWeixin (IUserListener observer,String openid,String access_token,String nickname,String avatar);
	void requestLoginBindPhone (IUserListener observer,String openid,String access_token,String nickname,String avatar,String phone,String code);
	void requestSetAutoPlay (IUserListener observer,int auto);
	void requestUpdate (IUserListener observer,String version);
	void requestAirUpdate(IUserListener observer,String name,String version);
	void requestGetUserInfo(IUserListener observer,String token);



}
