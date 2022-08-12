package com.example.demotailorshop.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demotailorshop.api.AppGuideApi;
import com.example.demotailorshop.api.DtsApiFactory;
import com.example.demotailorshop.entity.ApiError;
import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.entity.AppGuide;
import com.example.demotailorshop.utils.ApiUtils;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppGuideViewModel extends ViewModel {
    private static final String TAG = "AppGuideViewModel";
    private MutableLiveData<List<AppGuide>> appGuideMutableLiveData;

    public MutableLiveData<List<AppGuide>> getUserHelpListMutableLiveData() {
        if (appGuideMutableLiveData == null) {
            appGuideMutableLiveData = new MutableLiveData<>();
        }
        loadAppGuide();
        return appGuideMutableLiveData;
    }

    private void loadAppGuide() {
        AppGuideApi appGuideApi = DtsApiFactory.getRetrofitInstance().create(AppGuideApi.class);
        appGuideApi.getAppGuideList().enqueue(new Callback<ApiResponse<List<AppGuide>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<AppGuide>>> call, @NonNull Response<ApiResponse<List<AppGuide>>> response) {
                List<AppGuide> appGuideList = null;
                ApiError apiError = null;
                int httpStatus = response.code();
                String statusMessage = null;
                if (httpStatus == 200) {
                    ApiResponse<List<AppGuide>> apiResponse = response.body();
                    if (apiResponse != null) {
                        statusMessage = apiResponse.getMessage();
                        appGuideList = apiResponse.getResult();
                    }
                    Log.v(TAG, statusMessage);
                } else if (httpStatus == 204) {
                    statusMessage = "No app guide found";
                    apiError = ApiUtils.getDefaultErrorResponse(httpStatus, statusMessage);
                    Log.v(TAG, statusMessage);
                } else if (httpStatus == 401) {
                    apiError = ApiUtils.getApiErrorResponse(response);
                    statusMessage = apiError.getMessage();
                    Log.v(TAG, statusMessage);
                } else {
                    String responseString = null;
                    try {
                        responseString = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    apiError = ApiUtils.getDefaultErrorResponse(responseString);
                    statusMessage = apiError.getMessage();
                    Log.v(TAG, statusMessage);
                }
                appGuideMutableLiveData.setValue(appGuideList);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<AppGuide>>> call, @NonNull Throwable t) {
                Log.e(TAG, "Api-Error " + t.fillInStackTrace());
                ApiError apiError = ApiUtils.getDefaultErrorResponse(0, "Something went wrong");
                appGuideMutableLiveData.setValue(null);
            }
        });
    }

}
