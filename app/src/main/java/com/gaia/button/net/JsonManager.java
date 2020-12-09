/**
 * 
 */
package com.gaia.button.net;


public final class JsonManager {

	private static final Object mInstanceSync = new Object();

	private static JsonManager mInstance = null;

	private JSONBuilder mJsonBuilder = null;

	private JSONParser mJsonParser = null;
	
	public static JsonBuilderInterface getJsonBuilder(){
		return _getInstance()._getJsonBuilder();
	}
	
	public static JsonParserInterface getJsonParser(){
		return _getInstance()._getJsonParser();
	}
	
	private static JsonManager _getInstance(){
		
		synchronized(mInstanceSync){
			
			if(mInstance == null){
				mInstance = new JsonManager();
			}
		}
		return mInstance;
	}
	
	private JsonBuilderInterface _getJsonBuilder(){
		return mJsonBuilder;
	}
	
	private JsonParserInterface _getJsonParser(){
		return mJsonParser;
	}
	
	private JsonManager(){
		mJsonBuilder = new JSONBuilder();
		mJsonParser = new JSONParser();
	}
}
