package com.gaia.button.net.user;

import android.annotation.SuppressLint;

import com.gaia.button.data.PreferenceManager;
import com.gaia.button.model.AccountInfo;
import com.gaia.button.net.BaseResult;
import com.gaia.button.net.INetListener;
import com.gaia.button.net.NetManager;
import com.gaia.button.utils.ConstantUtil;


@SuppressLint("UseSparseArrays")
public class UserImpl implements INetListener {

	private static final String TAG = "UserImpl";

	private AccountInfo mCurrentUser = null;

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
			PreferenceManager.getInstance().setLoginOut();
			try {
				AccountInfo res = (AccountInfo) responseData;
				if (res.getErrorCode() == 0) {
//					mCurrentUser = res;
					PreferenceManager.getInstance().save(res);
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
			PreferenceManager.getInstance().setLoginOut();
			try {
				AccountInfo res = (AccountInfo) responseData;
				if (res.getErrorCode() == 0) {
//					mCurrentUser = res;
					PreferenceManager.getInstance().save(res);
					handleSuccessResult(requestId, requestTag, (Object) res, responseStr);
				} else {
					handleFailedResult(requestId, requestTag, errorCode, "登录失败，请稍后重试", responseData);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handleFailedResult(requestId, requestTag, errorCode, "登录失败，请稍后重试", responseData);
			}

		}  else {
			//通用消息处理
			handleSuccessResult(requestId, requestTag, responseData, responseStr);
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
			PreferenceManager.getInstance().setLoginOut();
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

}

