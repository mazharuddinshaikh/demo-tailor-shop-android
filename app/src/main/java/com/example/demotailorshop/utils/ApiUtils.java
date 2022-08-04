package com.example.demotailorshop.utils;

import android.util.Log;

import com.example.demotailorshop.entity.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;

public class ApiUtils {
    private static final String TAG = "ApiUtils";
    public static final int SUCCESS = 200;
    public static final int UN_AUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int BAD_REQUEST = 400;

    public static <E> ApiError getApiErrorResponse(Response<E> response) {
        int httpStatus = response.code();
        String statusMessage = null;
        ApiError apiResponse = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (response.errorBody() != null) {
                apiResponse = objectMapper.readValue(response.errorBody().string(), ApiError.class);
            }
        } catch (final IOException e) {
            statusMessage = "Unauthorized Access";
            apiResponse = getDefaultErrorResponse(httpStatus, statusMessage);
            Log.e(TAG, "Exception while parsing json object");
        }
        return apiResponse;
    }

    public static ApiError getDefaultErrorResponse(int httpStatus, String statusMessage) {
        ApiError apiError = new ApiError();
        apiError.setHttpStatus(httpStatus);
        apiError.setMessage(statusMessage);
        return apiError;
    }

    public static <E> ApiError getDefaultErrorResponse(String response) {
        int httpStatus = 0;
        String statusMessage = null;

        ApiError apiError = new ApiError();
        if (!DtsUtils.isNullOrEmpty(response)) {
            try {
                JSONObject jObjError = new JSONObject(response);
                statusMessage = jObjError.getString("error");
                httpStatus = jObjError.getInt("status");
            } catch (JSONException e) {
                statusMessage = "Something went wrong";
                Log.e(TAG, "Exception while parsing json object");
            }
        }
        apiError.setHttpStatus(httpStatus);
        apiError.setMessage(statusMessage);
        return apiError;
    }


}
