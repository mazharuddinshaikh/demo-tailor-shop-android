package com.example.demotailorshop.api;

import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.entity.DressType;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserDressTypesApi {
    @GET(value = "api/userDressType/v1/userDressType")
    Call<ApiResponse<List<DressType>>> getUserDressType(@HeaderMap Map<String, String> headers, @Query(value = "userId") int userId);

    @POST("api/userDressType/v1/updateUserDressType")
    Call<ApiResponse<DressType>> updateDressType(@HeaderMap Map<String, String> headers, @Body DressType dressType);
}
