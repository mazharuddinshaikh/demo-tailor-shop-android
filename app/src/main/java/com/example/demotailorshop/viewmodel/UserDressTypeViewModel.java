package com.example.demotailorshop.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demotailorshop.api.DtsApiFactory;
import com.example.demotailorshop.api.UserDressTypesApi;
import com.example.demotailorshop.entity.ApiError;
import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.entity.DressType;
import com.example.demotailorshop.entity.User;
import com.example.demotailorshop.utils.ApiUtils;
import com.example.demotailorshop.utils.DtsSharedPreferenceUtil;
import com.example.demotailorshop.utils.DtsUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDressTypeViewModel extends ViewModel {
    private static final String TAG = "UserDressTypeViewModel";
    private MutableLiveData<List<DressType>> dressTypeMutableLiveData;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MutableLiveData<List<DressType>> getDressTypeMutableLiveData() {

        if (dressTypeMutableLiveData == null) {
            dressTypeMutableLiveData = new MutableLiveData<>();
        }
        setDressType(user);
        return dressTypeMutableLiveData;
    }

    private void setDressType(User user) {
        {
            Map<String, String> headersMap = new HashMap<>();
            headersMap.put(DtsSharedPreferenceUtil.KEY_AUTHORIZATION, user.getAuthenticationToken());
            UserDressTypesApi userDressTypesApi = DtsApiFactory.getRetrofitInstance().create(UserDressTypesApi.class);
            userDressTypesApi.getUserDressType(headersMap, user.getUserId()).enqueue(new Callback<ApiResponse<List<DressType>>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<List<DressType>>> call, @NonNull Response<ApiResponse<List<DressType>>> response) {
                    List<DressType> dressTypeList = null;
                    ApiError apiError = null;
                    int httpStatus = response.code();
                    String statusMessage = null;
                    if (httpStatus == 200) {
                        ApiResponse<List<DressType>> apiResponse = response.body();
                        if (apiResponse != null) {
                            statusMessage = apiResponse.getMessage();

                            dressTypeList = apiResponse.getResult();
                            if (!DtsUtils.isNullOrEmpty(dressTypeList)) {
                                dressTypeMutableLiveData.setValue(dressTypeList);
                            }

                        }
                        Log.v(TAG, statusMessage);
                    } else if (httpStatus == 204) {
                        statusMessage = "No Dress Found";
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

                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<List<DressType>>> call, @NonNull Throwable t) {
                    Log.e(TAG, "Api-Error " + t.fillInStackTrace());
                }
            });
        }
    }

//    private ApiResponse<String> getErrorResponse(int httpStatus, String statusMessage) {
//        ApiResponse<String> apiResponse = new ApiResponse<>();
//        apiResponse.setHttpStatus(httpStatus);
//        apiResponse.setMessage(statusMessage);
//        return apiResponse;
//    }

//    private ApiResponse<String> getParsedObject(int httpStatus, Response<ApiResponse<List<DressType>>> responseBody) {
//        String statusMessage = null;
//        ApiResponse<String> apiResponse = null;
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            if (responseBody.errorBody() != null) {
//                apiResponse = objectMapper.readValue(responseBody.errorBody().string(), new TypeReference<ApiResponse<String>>() {
//                });
//            }
//        } catch (IOException e) {
//            statusMessage = "Unauthorized Access";
//            apiResponse = getErrorResponse(httpStatus, statusMessage);
//            Log.e(TAG, "Exception while parsing json object");
//        }
//        return apiResponse;
//    }

}
