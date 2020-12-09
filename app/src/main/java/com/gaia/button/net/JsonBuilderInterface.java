package com.gaia.button.net;

import java.util.HashMap;

public interface JsonBuilderInterface {

	/*
	 * tag 1 Default regist
	 */
	String buildDefaultString(String funName);

	String buildDefaultString(String funName, HashMap<String, String> params);

	Object buildRequestUploadLog(String content);

	Object buildGetGroupFinanceString(String funJsonString);

	Object buildGetGroupOwerString(String funJsonString);

}