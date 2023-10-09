package com.example.demotailorshop.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

final public class DtsApiFactory {
    private static final String DEV_URL = "https://90ec-2401-4900-5612-fda8-fc0f-af9d-64a4-50ca.ngrok-free.app/digitailors/";
    private static final String TEST_URL = "http://192.168.121.119:8081/digitailors/";
    private static final String PROD_URL = "http://20.219.198.148:9000/digitailors/";
    private static final String BASE_URL = PROD_URL;
    private static Retrofit retrofit;

    //authTkoen "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXpoYXIiLCJpYXQiOjE2Mzk4MjM0OTgsImlzcyI6ImR0cy10YWlsb3Itc2hvcCJ9.hbRHvYk9EMi9UrYhFFXrMjV2yzQa4fj1kB0nRyMUXVI"
    private DtsApiFactory() {
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .readTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES)
                    .build();
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

}
