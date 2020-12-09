/**
 * 
 */
package com.jindan.p2p.http.builder;


public final class HttpSubmitDataManager {

	private static final Object mInstanceSync = new Object();

	private static HttpSubmitDataManager  mInstance = null;

	private SubmitDataBuilder 		mJsonBuilder = null;
	
	public static HttpSubmitDataBuilderInterface getBuilder(){
		return _getInstance()._getBuilder();
	}
	
	private static HttpSubmitDataManager _getInstance(){
		
		synchronized(mInstanceSync){
			
			if(mInstance == null){
				mInstance = new HttpSubmitDataManager();
			}
		}
		return mInstance;
	}
	
	private HttpSubmitDataBuilderInterface _getBuilder(){
		return mJsonBuilder;
	}
	
	private HttpSubmitDataManager(){
		mJsonBuilder = new SubmitDataBuilder();
	}
}
