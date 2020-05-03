package com.example.myapplication;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface RetrofitService {

    @Multipart
    @POST("/")
    Call<JsonObject> uploadFile(@PartMap()LinkedHashMap<String, RequestBody>
                                  partMap, @Part List<MultipartBody.Part> names);
}
