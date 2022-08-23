package com.example.demotailorshop.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

final public class DtsApiFactory {
    private static final String TEST_URL = "http://192.168.227.119:8081/digitailors/";
    private static final String PROD_URL = "http://http://65.0.224.253:8081/digitailors/";
    private static final String BASE_URL = TEST_URL;
    private static Retrofit retrofit;

    //authTkoen "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXpoYXIiLCJpYXQiOjE2Mzk4MjM0OTgsImlzcyI6ImR0cy10YWlsb3Itc2hvcCJ9.hbRHvYk9EMi9UrYhFFXrMjV2yzQa4fj1kB0nRyMUXVI"
    private DtsApiFactory() {
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

}
