package com.gaia.button.net;

import com.gaia.button.net.service.GaiaRequest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private static OkHttpClient okHttpClient;
    private OkHttpClient mOkHttpClient;
    private volatile static RetrofitHelper mInstance;
    private  Retrofit mRetrofit;
    public static RetrofitHelper getInstance(){
        if(mInstance == null){
            synchronized(RetrofitHelper.class){
                if(mInstance == null){
                    mInstance = new RetrofitHelper(GaiaRequest.HOST);
                }
            }
        }
        return mInstance;
    }
    private RetrofitHelper(String baseUrl){
        mOkHttpClient = initOkHttpClient();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public Retrofit getRetrofit(){
        return mRetrofit;
    }
    private OkHttpClient initOkHttpClient() {
        if(okHttpClient == null){
            okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }
    public <T> T getBaseRequest(Class<T> service){
       return mRetrofit.create(service);
    }

}
