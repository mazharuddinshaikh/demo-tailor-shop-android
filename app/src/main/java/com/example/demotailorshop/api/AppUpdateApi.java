package com.example.demotailorshop.api;

import com.example.demotailorshop.entity.AndroidAppUpdate;
import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.entity.Dress;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AppUpdateApi {

    @GET(value = "api/app/v1/appUpdate")
    Call<ApiResponse<AndroidAppUpdate>> getAppUpdate();
}
