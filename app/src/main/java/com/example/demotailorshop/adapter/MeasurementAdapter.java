package com.example.demotailorshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotailorshop.databinding.ItemMeasurementListBinding;
import com.example.demotailorshop.entity.Measurement;
import com.example.demotailorshop.listener.MeasurementListener;
import com.example.demotailorshop.utils.DtsConstants;

import java.util.List;
import java.util.Map;

public class MeasurementAdapter extends RecyclerView.Adapter<MeasurementAdapter.MeasurementViewHolder> {
    private Measurement measurement;
    private int displayWidth;
    private Context context;
    private MeasurementListener measurementListener;
    private int dressId;


    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setMeasurementListener(MeasurementListener measurementListener) {
        this.measurementListener = measurementListener;
    }

    public void setDressId(int dressId) {
        this.dressId = dressId;
    }

    @NonNull
    @Override
    public MeasurementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMeasurementListBinding binding = ItemMeasurementListBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new MeasurementViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MeasurementViewHolder holder, int position) {
        String measurementType = null;
        ItemMeasurementListBinding binding = holder.getBinding();
        ImageListAdapter adapter = new ImageListAdapter();
        List<String> imageList = null;
        Map<String, List<Uri>> measurementUriMap = null;
        measurementUriMap = measurement.getUriMap();
        switch (position) {
//            raw
            case 0:
                binding.tvHeading.setText("Raw Dress Image");
                imageList = measurement.getRawDressImageList();
                measurementType = DtsConstants.RAW;
                List<Uri> rawUriList = null;
                if (measurementUriMap != null) {
                    rawUriList = measurementUriMap.get(DtsConstants.RAW);
                }
                adapter.setUriList(rawUriList);
                adapter.setMeasurementType(measurementType);
                break;
//                measurement
            case 1:
                binding.tvHeading.setText("Measurement Image");
                imageList = measurement.getMeasurementImageList();
                measurementType = DtsConstants.MEASUREMENT;
                List<Uri> measurementUriList = null;
                if (measurementUriMap != null) {
                    measurementUriList = measurementUriMap.get(DtsConstants.MEASUREMENT);
                }
                adapter.setUriList(measurementUriList);
                adapter.setMeasurementType(measurementType);
                break;
//                pattern
            case 2:
                binding.tvHeading.setText("Pattern Image");
                imageList = measurement.getPatternImageList();

                measurementType = DtsConstants.PATTERN;
                List<Uri> patternUriList = null;
                if (measurementUriMap != null) {
                    patternUriList = measurementUriMap.get(DtsConstants.PATTERN);
                }
                adapter.setUriList(patternUriList);
                adapter.setMeasurementType(measurementType);
                break;
//                seaved
            case 3:
                binding.tvHeading.setText("Seaved Image");
                imageList = measurement.getSeavedImageList();
                measurementType = DtsConstants.SEAVED;
                List<Uri> seavedUriList = null;
                if (measurementUriMap != null) {
                    seavedUriList = measurementUriMap.get(DtsConstants.SEAVED);
                }
                adapter.setUriList(seavedUriList);
                adapter.setMeasurementType(measurementType);
                break;
        }

        adapter.setImageList(imageList);
        adapter.setDisplayWidth(this.displayWidth);
        adapter.setContext(context);
        adapter.setDressId(dressId);
        adapter.setMeasurementListener(measurementListener);

        GridLayoutManager layoutManager = new GridLayoutManager(context, 1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvImageList.setAdapter(adapter);
        binding.rvImageList.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class MeasurementViewHolder extends RecyclerView.ViewHolder {
        private final ItemMeasurementListBinding binding;

        public MeasurementViewHolder(@NonNull ItemMeasurementListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemMeasurementListBinding getBinding() {
            return binding;
        }
    }
}
