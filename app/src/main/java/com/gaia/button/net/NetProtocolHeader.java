/**
 * 
 */
package com.gaia.button.net;

public class NetProtocolHeader {
	private int code;
	private String msg;
	private int			mTag;
	
	private String info;
	
	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public NetProtocolHeader(){
		mTag = -1;
		code = -1;
	}
	
	public int getErrorCode() {
		return code;
	}
	public void setErrorCode(int errorCode) {
		this.code = errorCode;
	}
	public String getErrorDesc() {
		return msg;
	}
	public void setErrorDesc(String errorDesc) {
		this.msg = errorDesc;
	}
	public int getTag() {
		return mTag;
	}
	public void setTag(int tag) {
		this.mTag = tag;
	}

	@Override
	public String toString() {
		return "NetProtocolHeader{" +
				"mErrorCode=" + code +
				", mErrorDesc='" + msg + '\'' +
				", mTag=" + mTag +
				", info='" + info + '\'' +
				'}';
	}
}
