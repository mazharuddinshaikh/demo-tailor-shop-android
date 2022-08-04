package com.example.demotailorshop.api;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

final public class DtsApiFactory {
    private static final String BASE_URL = "http://192.168.227.119:8081/";
    private static Retrofit retrofit;
//authTkoen "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXpoYXIiLCJpYXQiOjE2Mzk4MjM0OTgsImlzcyI6ImR0cy10YWlsb3Itc2hvcCJ9.hbRHvYk9EMi9UrYhFFXrMjV2yzQa4fj1kB0nRyMUXVI"
    private DtsApiFactory() {
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
