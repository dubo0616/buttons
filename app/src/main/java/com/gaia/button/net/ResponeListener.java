package com.gaia.button.net;
import com.xcheng.retrofit.Call;
import com.xcheng.retrofit.DefaultCallback;
import com.xcheng.retrofit.HttpError;

import retrofit2.Callback;
import retrofit2.Response;

public class ResponeListener<T> extends DefaultCallback<T> {


    @Override
    public void onStart(Call<T> call) {

    }

    @Override
    public void onError(Call<T> call, HttpError error) {

    }

    @Override
    public void onSuccess(Call<T> call, T t) {

    }
}
