package com.example.myapplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitImg {
    private  static RetrofitImg ourInstance = new RetrofitImg();
    public  static  RetrofitImg getInstance(){
        return ourInstance;
    }
    private  RetrofitImg(){

    }
    OkHttpClient defaultHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor(){
        @Override
        public Response intercept(Chain chain) throws IOException{
            Request request = chain.request().newBuilder()
                    .addHeader("Content-Type", "image/jpeg")
                    .build();
            return  chain.proceed(request);
        }
    }).build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://35.234.9.15:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(defaultHttpClient)
            .build();

    RetrofitService service = retrofit.create(RetrofitService.class);

    public RetrofitService getService() {
        return service;
    }
}
