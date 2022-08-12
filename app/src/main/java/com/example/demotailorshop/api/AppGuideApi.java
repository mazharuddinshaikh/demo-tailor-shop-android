package com.example.demotailorshop.api;

import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.entity.AppGuide;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AppGuideApi {

    @GET(value = "api/userHelp/v1/appGuide")
    Call<ApiResponse<List<AppGuide>>> getAppGuideList();
}
