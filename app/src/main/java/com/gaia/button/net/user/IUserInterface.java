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


}
