package com.example.demotailorshop.entity;

import androidx.annotation.NonNull;

/**
 * @author Admin
 */
public class ApiResponse<T> {
    private int httpStatus;
    private String message;
    private T result;

    public ApiResponse() {
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @NonNull
    @Override
    public String toString() {
        return "ApiResponse{" +
                "httpStatus=" + httpStatus +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
