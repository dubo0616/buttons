package com.gaia.button.net;


import com.gaia.button.utils.DcError;

public class BaseResult {
	protected int mErrorCode = DcError.DC_Error;
	protected String mErrorString;
	protected Object other;
	protected String info;
	private boolean isCache;
	private String serverErrorUrl;
	
	public String getServerErrorUrl() {
		return serverErrorUrl;
	}

	public void setServerErrorUrl(String serverErrorUrl) {
		this.serverErrorUrl = serverErrorUrl;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Object getOther() {
		return other;
	}

	public void setOther(Object other) {
		this.other = other;
	}

	public int getErrorCode() {
		return mErrorCode;
	}

	public void setErrorCode(int errorCode) {
		this.mErrorCode = errorCode;
	}

	public String getErrorString() {
		return mErrorString;
	}

	public void setErrorString(String errorString) {
		this.mErrorString = errorString;
	}

	public boolean isCache() {
		return isCache;
	}

	public void setCache(boolean isCache) {
		this.isCache = isCache;
	}

}