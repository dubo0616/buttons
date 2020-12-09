package com.gaia.button.net;

public interface IHttpInterface {
	
	int sendRequest(String url, int requestTag, Object bodydata, INetListener listener);
	int sendRequestNetCache(String url, int requestTag, Object bodydata, INetListener listener, int offset);
	int	sendDownLoadRequest(String url, String filepath, INetListener listener);
	void cancelRequestById(int requestId);
	void cancelAllRequest();
}
