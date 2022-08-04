package com.example.demotailorshop.api;

import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserApi {

    @FormUrlEncoded
    @POST("api/user/v1/signin")
    Call<User> signin(@Field("userName") String userName, @Field("password") String password);

    @FormUrlEncoded
    @POST("/api/user/v1/forgotPassword")
    Call<ApiResponse<String>> forgotPassword(@Field("userName") String userName,
                                             @Field("email") String email);

    @POST("/api/user/v1/signup")
    Call<ApiResponse<User>> signup(@Body User user);

    @FormUrlEncoded
    @POST("/api/user/v1/updatePassword")
    Call<ApiResponse<String>> updatePassword(@Field("userName") String userName,
                                             @Field("oldPassword") String oldPassword,
                                              @Field("newPassword") String newPassword);

    @POST("/api/user/v1/updateUser")
    Call<ApiResponse<User>> updateUser(@Body User user);

    @POST("/api/user/v1/updateShop")
    Call<ApiResponse<User>> updateShop(@Body User user);
}
