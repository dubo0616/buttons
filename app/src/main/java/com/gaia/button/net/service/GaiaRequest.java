package com.gaia.button.net.service;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GaiaRequest {
    String HOST = "https://www.xxx.com/app_v5/";
    @POST("?service=sser.getList")
    Call<GaiaResponse<String> >getList(@Query("id") String id);

}
