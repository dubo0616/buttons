/**
 * 
 */
package com.gaia.button.net;


import com.gaia.button.model.DiscoveryModel;

import java.util.List;

public interface JsonParserInterface {



	NetProtocolHeader parserHeader(String responseStr) throws  Exception;

	BaseResult  parserLoginSms(String responseStr) throws Exception;
	BaseResult  parserLoginPwd(String responseStr) throws Exception;
	BaseResult  parserForgetPwd(String responseStr) throws Exception;
	BaseResult  parserSetPwd(String responseStr) throws Exception;
	BaseResult  parserLoginSendCode(String responseStr) throws Exception;
	BaseResult parserArticleList(String responseStr) throws Exception;
	BaseResult parserArticleCollect(String responseStr) throws Exception;
	BaseResult parserProductList(String responseStr) throws Exception;
	BaseResult parserGetCollect(String responseStr) throws Exception;
	BaseResult parserGetDevice(String responseStr) throws Exception;
	BaseResult parserLoginout(String responseStr) throws Exception;
	BaseResult parserWeixinLogin(String responseStr) throws Exception;
	BaseResult parserWeixinLoginBindphone(String responseStr) throws Exception;
	BaseResult parserAutoPlay(String responseStr) throws Exception;
	BaseResult parserUpdate(String responseStr) throws Exception;
	BaseResult parserAirUpdate(String responseStr) throws Exception;
	BaseResult parserGetUserinfo(String responseStr) throws Exception;


}
