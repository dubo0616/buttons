//package com.gaia.button.net;
//
//import android.util.Log;
//
//import com.gaia.button.net.service.GaiaRequest;
//import com.gaia.button.net.service.LoginService;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.squareup.okhttp.OkHttpClient;
//
//import java.io.IOException;
//import java.lang.reflect.Type;
//import java.util.concurrent.TimeUnit;
//
//
//public class RetrofitHelper {
//    private static OkHttpClient okHttpClient;
//    private OkHttpClient mOkHttpClient;
//    private volatile static RetrofitHelper mInstance;
//    private Retrofit mRetrofit;
//
//    public static RetrofitHelper getInstance() {
//        if (mInstance == null) {
//            synchronized (RetrofitHelper.class) {
//                if (mInstance == null) {
//                    mInstance = new RetrofitHelper(GaiaRequest.HOST);
//                }
//            }
//        }
//        return mInstance;
//    }
//
//    private RetrofitHelper(String baseUrl) {
//        mOkHttpClient = initOkHttpClient();
//        mRetrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .client(mOkHttpClient)
//                .build();
//    }
//
//    public <T> T getService(Class<T> cls) {
//        return mRetrofit.create(cls);
//    }
//
//    public Retrofit getRetrofit() {
//        return mRetrofit;
//    }
//
//    private OkHttpClient initOkHttpClient() {
//        if (okHttpClient == null) {
//            okHttpClient = new OkHttpClient.Builder()
//                    .retryOnConnectionFailure(true)
//                    .connectTimeout(30, TimeUnit.SECONDS)
//                    .build();
//        }
//        return okHttpClient;
//    }
//
//    public void getBaseRequest(Call call, final ResponeListener listener) {
//        if (call == null) {
//            return;
//        }
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response != null && response.isSuccessful()) {
//                    if (listener != null) {
//                        try {
//                            listener.onSuccess(response.body().string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            if (listener != null) {
//                                listener.onFail(e.getCause());
//                            }
//
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("TTTT", "========" + t.getMessage());
//                if (listener != null) {
//                    listener.onFail(t);
//                }
//            }
////            @Override
////            public void onResponse(Call call, Response response) {
////                if(response != null &&  response.isSuccessful()){
////                    if(listener != null) {
////                        listener.onSuccess(response.body().toString());
////                    }
////                }
////            }
////
////            @Override
////            public void onFailure(Call call, Throwable t) {
////                if(listener != null) {
////                    listener.onFail(t);
////                }
////            }
//        });
//    }
//
//}
