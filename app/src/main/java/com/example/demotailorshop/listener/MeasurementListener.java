package com.example.demotailorshop.listener;

import android.net.Uri;

import com.example.demotailorshop.entity.Customer;

import java.util.List;

public interface MeasurementListener {
    void onAddMeasurement(String type, int dressId);

    void showMeasurement(List<String> imageUrlList, List<Uri> uriList, int itemPosition);

    void customerCall(Customer customer);
}
