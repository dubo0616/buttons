/**
 * 
 */
package com.jindan.p2p.json;

public class NetProtocolHeader {
	private int			mErrorCode;
	private String 	    mErrorDesc;
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
		mErrorCode = -1;
	}
	
	public int getErrorCode() {
		return mErrorCode;
	}
	public void setErrorCode(int errorCode) {
		this.mErrorCode = errorCode;
	}
	public String getErrorDesc() {
		return mErrorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.mErrorDesc = errorDesc;
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
				"mErrorCode=" + mErrorCode +
				", mErrorDesc='" + mErrorDesc + '\'' +
				", mTag=" + mTag +
				", info='" + info + '\'' +
				'}';
	}
}
