package com.example.demotailorshop.listener;

import android.net.Uri;

import com.example.demotailorshop.entity.Customer;

import java.util.List;

@FunctionalInterface
public interface MeasurementListener {
    void onAddMeasurement(String type, int dressId);

    default void showMeasurement(List<String> imageUrlList, List<Uri> uriList, int itemPosition) {

    }

    default void customerCall(Customer customer) {

    }

    default void onDeleteMeasurement(int dressId, String measurementType, Uri uri) {
    }
}
