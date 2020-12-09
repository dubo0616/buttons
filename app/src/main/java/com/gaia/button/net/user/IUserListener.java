package com.gaia.button.net.user;

public interface IUserListener {

	void onRequestSuccess(int requestTag, Object data);
	void onRequestError(int requestTag, int errorCode, String errorMsg, Object data);
	void startProgressDialog(int requestTag);
	void endProgressDialog(int requestTag);

}