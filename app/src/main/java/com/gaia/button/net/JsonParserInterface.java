/**
 * 
 */
package com.gaia.button.net;


public interface JsonParserInterface {



	NetProtocolHeader parserHeader(String responseStr) throws  Exception;

	BaseResult  parserLoginSms(String responseStr) throws Exception;
	BaseResult  parserLoginSendCode(String responseStr) throws Exception;


}
