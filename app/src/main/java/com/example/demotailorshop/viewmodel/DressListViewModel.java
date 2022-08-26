package com.example.demotailorshop.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demotailorshop.api.DressListApi;
import com.example.demotailorshop.api.DtsApiFactory;
import com.example.demotailorshop.entity.ApiError;
import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.entity.Dress;
import com.example.demotailorshop.entity.User;
import com.example.demotailorshop.utils.ApiUtils;
import com.example.demotailorshop.utils.DtsSharedPreferenceUtil;
import com.example.demotailorshop.utils.DtsUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DressListViewModel extends ViewModel {
    private static final String TAG = "DressListViewModel";
    private MutableLiveData<List<Dress>> dressListMutableLiveData;
    private MutableLiveData<ApiError> apiErrorMutableLiveData;
    private int limit;
    private int offset;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public MutableLiveData<List<Dress>> getDressListLiveData(User user, List<String> dressTypeList) {
        if (dressListMutableLiveData == null) {
            dressListMutableLiveData = new MutableLiveData<>();
        }
//        List<Dress> dressList = dressListMutableLiveData.getValue();
//        if (DtsUtils.isNullOrEmpty(dressList)) {
            setDressList(user, dressTypeList);
//        }

        return dressListMutableLiveData;
    }

    public MutableLiveData<ApiError> getApiErrorMutableLiveData() {
        if (apiErrorMutableLiveData == null) {
            apiErrorMutableLiveData = new MutableLiveData<>();
        }
        return apiErrorMutableLiveData;
    }

    private void initializeErrorLiveData() {
        if (apiErrorMutableLiveData == null) {
            apiErrorMutableLiveData = new MutableLiveData<>();
        }
    }

    public void setDressList(User user, List<String> dressTypeList) {
        initializeErrorLiveData();
        List<Dress> initialDressList = new ArrayList<>();
        initialDressList.add(null);
        dressListMutableLiveData.setValue(initialDressList);
        apiErrorMutableLiveData.setValue(null);
        addDressList(user, dressTypeList);
    }

    public void addDressList(User user, List<String> dressTypeList) {
        apiErrorMutableLiveData.setValue(null);
        String authenticationToken = user.getAuthenticationToken();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(DtsSharedPreferenceUtil.KEY_AUTHORIZATION, authenticationToken);
        DressListApi dressListApi = DtsApiFactory.getRetrofitInstance().create(DressListApi.class);
        dressListApi.getAllDress(headersMap, user.getUserId(), offset, limit, dressTypeList).enqueue(new Callback<ApiResponse<List<Dress>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Dress>>> call, @NonNull Response<ApiResponse<List<Dress>>> response) {
                List<Dress> dressList = dressListMutableLiveData.getValue();
                ApiError apiError = null;
                int httpStatus = response.code();
                String statusMessage = null;
                if (httpStatus == 200) {
                    ApiResponse<List<Dress>> apiResponse = response.body();
                    if (apiResponse != null) {
                        statusMessage = apiResponse.getMessage();
                        if (!DtsUtils.isNullOrEmpty(dressList)) {
                            dressList.addAll(apiResponse.getResult());
                        }
                    }
                    Log.v(TAG, statusMessage);
                } else if (httpStatus == 204) {
                    decreaseOffset();
                    statusMessage = "No Dress Found";
                    apiError = ApiUtils.getDefaultErrorResponse(httpStatus, statusMessage);
                    Log.v(TAG, statusMessage);
                } else if (httpStatus == 401) {
                    decreaseOffset();
                    apiError = ApiUtils.getApiErrorResponse(response);
                    statusMessage = apiError.getMessage();
                    Log.v(TAG, statusMessage);
                } else {
                    decreaseOffset();
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
                if (!DtsUtils.isNullOrEmpty(dressList)) {
                    dressList.remove(null);

                }
                dressListMutableLiveData.setValue(dressList);
                apiErrorMutableLiveData.setValue(apiError);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Dress>>> call, @NonNull Throwable t) {
                decreaseOffset();
                Log.e(TAG, "Api-Error " + t.fillInStackTrace());
                ApiError apiError = ApiUtils.getDefaultErrorResponse(0, "Something went wrong");
                List<Dress> dressList = dressListMutableLiveData.getValue();
                if (DtsUtils.isNullOrEmpty(dressList) || (!DtsUtils.isNullOrEmpty(dressList) && dressList.size() == 1 && dressList.get(0) == null)) {
                    dressListMutableLiveData.setValue(null);
                    apiErrorMutableLiveData.setValue(apiError);
                } else {
                    dressList.remove(null);
                    dressList.add(null);
                    dressListMutableLiveData.setValue(dressList);
                    apiErrorMutableLiveData.setValue(apiError);

                }
            }
        });
    }

    public void addDress(Dress dress) {
        List<Dress> dressList = dressListMutableLiveData.getValue();
        if (DtsUtils.isNullOrEmpty(dressList)) {
            dressList = new ArrayList<>();
        }
        dressList.remove(null);
        dressList.add(dress);
        dressListMutableLiveData.setValue(dressList);
    }


    public void removeDress(Dress dress) {
        List<Dress> dressList = dressListMutableLiveData.getValue();
        if (!DtsUtils.isNullOrEmpty(dressList)) {
            dressList.remove(dress);
            dressListMutableLiveData.setValue(dressList);
        }
    }

    private void decreaseOffset() {
        if (this.offset != 0) {
            this.offset--;
        }
    }
}
