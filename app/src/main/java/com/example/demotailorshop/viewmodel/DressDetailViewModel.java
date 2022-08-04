package com.example.demotailorshop.viewmodel;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demotailorshop.api.DressListApi;
import com.example.demotailorshop.api.DtsApiFactory;
import com.example.demotailorshop.entity.ApiResponse;
import com.example.demotailorshop.entity.Dress;
import com.example.demotailorshop.entity.DressDetail;
import com.example.demotailorshop.entity.Measurement;
import com.example.demotailorshop.entity.User;
import com.example.demotailorshop.utils.DtsSharedPreferenceUtil;
import com.example.demotailorshop.utils.DtsUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DressDetailViewModel extends ViewModel {
    private static final String TAG = "DressDetailViewModel";
    private MutableLiveData<DressDetail> dressDetailMutableLiveData;
    private MutableLiveData<ApiResponse<String>> apiErrorMutableLiveData;
    private int dressId;
    private String type;

    public MutableLiveData<DressDetail> getDressDetailMutableLiveData(User user, int customerId, boolean isNewCustomer) {
        if (dressDetailMutableLiveData == null) {
            dressDetailMutableLiveData = new MutableLiveData<>();
        }
        DressDetail dressDetail = dressDetailMutableLiveData.getValue();
        if (dressDetail == null || (!isNewCustomer && dressDetail.getCustomer().getCustomerId() != customerId)) {
            setDressDetails(user, customerId);
        }
        return dressDetailMutableLiveData;
    }

    public void setDressDetail(DressDetail dressDetail) {
        if (this.dressDetailMutableLiveData == null) {
            dressDetailMutableLiveData = new MutableLiveData<>();
        }
        this.dressDetailMutableLiveData.setValue(dressDetail);
    }

    public DressDetail getDressDetail() {
        if (this.dressDetailMutableLiveData != null) {
            return this.dressDetailMutableLiveData.getValue();
        }
        return null;
    }

    private void initializeErrorLiveData() {
        if (apiErrorMutableLiveData == null) {
            apiErrorMutableLiveData = new MutableLiveData<>();
        }
    }

    private void setDressDetails(User user, int customerId) {
        String authorizationHeader = user.getAuthenticationToken();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(DtsSharedPreferenceUtil.KEY_AUTHORIZATION, authorizationHeader);
        DressListApi dressListApi = DtsApiFactory.getRetrofitInstance().create(DressListApi.class);
        initializeErrorLiveData();

        dressListApi.getDressDetails(headersMap, user.getUserId(), customerId).enqueue(new Callback<ApiResponse<DressDetail>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<DressDetail>> call, @NonNull Response<ApiResponse<DressDetail>> response) {
                DressDetail dressDetail = null;
                ApiResponse<String> apiError = null;
                int httpStatus = response.code();
                String statusMessage = null;
                if (httpStatus == 200) {
                    ApiResponse<DressDetail> apiResponse = response.body();
                    if (apiResponse != null) {
                        statusMessage = apiResponse.getMessage();
                        dressDetail = apiResponse.getResult();
                    }
                    Log.v(TAG, statusMessage);
                } else if (httpStatus == 204) {
                    statusMessage = "No Dress details Found";
                    apiError = getErrorResponse(httpStatus, statusMessage);
                    Log.v(TAG, statusMessage);
                } else if (httpStatus == 401) {
                    apiError = getParsedObject(httpStatus, response);
                    statusMessage = apiError.getMessage();
                    Log.v(TAG, statusMessage);
                } else {
                    try {
                        if (response.errorBody() != null) {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            statusMessage = jObjError.getString("error");
                            httpStatus = jObjError.getInt("status");
                        }
                    } catch (IOException | JSONException e) {
                        statusMessage = "Something went wrong";
                        Log.e(TAG, "Exception while parsing json object");
                    }
                    apiError = getErrorResponse(httpStatus, statusMessage);
                    Log.v(TAG, statusMessage);
                }
                dressDetailMutableLiveData.setValue(dressDetail);
                apiErrorMutableLiveData.setValue(apiError);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<DressDetail>> call, @NonNull Throwable t) {
                Log.e(TAG, "Api-Error " + t.fillInStackTrace());
                ApiResponse<String> apiError = getErrorResponse(0, "Something went wrong");
                dressDetailMutableLiveData.setValue(null);
                apiErrorMutableLiveData.setValue(apiError);
            }
        });

    }


    private ApiResponse<String> getErrorResponse(int httpStatus, String statusMessage) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setHttpStatus(httpStatus);
        apiResponse.setMessage(statusMessage);
        return apiResponse;
    }

    private ApiResponse<String> getParsedObject(int httpStatus, Response<ApiResponse<DressDetail>> responseBody) {
        String statusMessage = null;
        ApiResponse<String> apiResponse = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (responseBody.errorBody() != null) {
                apiResponse = objectMapper.readValue(responseBody.errorBody().string(), new TypeReference<ApiResponse<String>>() {
                });
            }
        } catch (IOException e) {
            statusMessage = "Unauthorized Access";
            apiResponse = getErrorResponse(httpStatus, statusMessage);
            Log.e(TAG, "Exception while parsing json object");
        }
        return apiResponse;
    }


    public int getDressId() {
        return dressId;
    }

    public String getType() {
        return type;
    }

    public void addMeasurement(int dressId, String type) {
        this.dressId = dressId;
        this.type = type;
    }

    public void updateUriMap(List<Uri> uriList) {
        DressDetail dressDetail = dressDetailMutableLiveData.getValue();
        List<Dress> dressList = dressDetail.getDressList();
        Dress dress = getDress(dressDetail, this.dressId);
        Measurement measurement = dress.getMeasurement();
        int positionToDelete = 0;
        Map<String, List<Uri>> oldUriMap = measurement.getUriMap();
        if (oldUriMap == null) {
            oldUriMap = new HashMap<>();
        }
        List<Uri> oldUriList = oldUriMap.get(type);
        if (oldUriList == null) {
            oldUriMap.put(type, uriList);
        } else {
            List<Uri> newUriList = new ArrayList<>();
            newUriList.addAll(oldUriList);
            newUriList.addAll(uriList);
            oldUriMap.put(type, newUriList);
        }
        measurement.setUriMap(oldUriMap);
        dress.setMeasurement(measurement);

        for (int i = 0; i < dressList.size(); i++) {
            Dress d = dressList.get(i);
            if (d.getDressId() == dress.getDressId()) {
                positionToDelete = i;
                break;
            }
        }
        if (!DtsUtils.isNullOrEmpty(dressList)) {
            dressList.remove(positionToDelete);
        }
        dressList.add(positionToDelete, dress);
        dressDetail.setDressList(dressList);
        dressDetailMutableLiveData.setValue(dressDetail);

    }


    private Dress getDress(DressDetail dressDetail, int dressId) {
        Dress dress = new Dress();
        if (dressDetail != null) {
            if (!DtsUtils.isNullOrEmpty(dressDetail.getDressList())) {
                for (Dress d : dressDetail.getDressList()) {
                    if (dressId == d.getDressId()) {
                        dress = d;
                    }
                }
            }
        }
        return dress;
    }
}
