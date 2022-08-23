package com.example.demotailorshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demotailorshop.R;
import com.example.demotailorshop.databinding.ItemMeasurementViewBinding;
import com.example.demotailorshop.utils.DtsUtils;

import java.util.List;

public class MeasurementViewAdapter extends RecyclerView.Adapter<MeasurementViewAdapter.MeasurementViewHolder> {
    private List<String> imageUrlList;
    private List<Uri> imageUriList;
    private Context context;
    private int displayWidth;
    private int positionToOpen;
    private MeasurementClickListener measurementClickListener;

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public void setImageUriList(List<Uri> imageUriList) {
        this.imageUriList = imageUriList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setPositionToOpen(int positionToOpen) {
        this.positionToOpen = positionToOpen;
    }

    public void setMeasurementClickListener(MeasurementClickListener measurementClickListener) {
        this.measurementClickListener = measurementClickListener;
    }

    @NonNull
    @Override
    public MeasurementViewAdapter.MeasurementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMeasurementViewBinding binding = ItemMeasurementViewBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new MeasurementViewAdapter.MeasurementViewHolder(binding);
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    @Override
    public void onBindViewHolder(@NonNull MeasurementViewAdapter.MeasurementViewHolder holder, int position) {
        ItemMeasurementViewBinding binding = holder.getBinding();
        binding.getRoot().getLayoutParams().width = this.displayWidth / 3;
        binding.getRoot().getLayoutParams().height = this.displayWidth / 3;
        int adapterPosition = holder.getAdapterPosition();

        if (!DtsUtils.isNullOrEmpty(imageUrlList) && adapterPosition < imageUrlList.size() && DtsUtils.isNullOrEmpty(imageUriList)) {
            Glide.with(this.context).load(imageUrlList.get(adapterPosition))
                    .fallback(R.drawable.ic_no_image)
                    .error(R.drawable.ic_error_image)
                    .into(binding.cddlMeasurement);

        } else if (!DtsUtils.isNullOrEmpty(imageUriList) && adapterPosition < imageUriList.size() && DtsUtils.isNullOrEmpty(imageUrlList)) {
            Glide.with(this.context).load(imageUriList.get(adapterPosition))
                    .fallback(R.drawable.ic_no_image)
                    .error(R.drawable.ic_error_image)
                    .into(binding.cddlMeasurement);

        } else if (!DtsUtils.isNullOrEmpty(imageUrlList) && !DtsUtils.isNullOrEmpty(imageUriList)) {
            if (position < imageUrlList.size()) {
                Glide.with(this.context).load(imageUrlList.get(adapterPosition))
                        .fallback(R.drawable.ic_no_image)
                        .error(R.drawable.ic_error_image)
                        .into(binding.cddlMeasurement);

            } else {
                int uriPosition = adapterPosition - imageUrlList.size();
                Glide.with(this.context).load(imageUriList.get(uriPosition))
                        .fallback(R.drawable.ic_no_image)
                        .error(R.drawable.ic_error_image)
                        .into(binding.cddlMeasurement);

            }
        }

        binding.cddlMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DtsUtils.isNullOrEmpty(imageUrlList) && DtsUtils.isNullOrEmpty(imageUriList)) {
                    measurementClickListener.onMeasurementClick(imageUrlList, imageUriList, adapterPosition);
                }
                if (!DtsUtils.isNullOrEmpty(imageUriList) && DtsUtils.isNullOrEmpty(imageUrlList)) {
                    measurementClickListener.onMeasurementClick(imageUrlList, imageUriList, adapterPosition);
                }
                if (!DtsUtils.isNullOrEmpty(imageUrlList) && !DtsUtils.isNullOrEmpty(imageUriList)) {
                    measurementClickListener.onMeasurementClick(imageUrlList, imageUriList, adapterPosition);
                }

            }
        });
        if (adapterPosition == positionToOpen) {
            binding.cddlMeasurement.performClick();
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (!DtsUtils.isNullOrEmpty(imageUrlList)) {
            itemCount += imageUrlList.size();
        }
        if (!DtsUtils.isNullOrEmpty(imageUriList)) {
            itemCount += imageUriList.size();
        }
        return itemCount;
    }

    protected static class MeasurementViewHolder extends RecyclerView.ViewHolder {
        final ItemMeasurementViewBinding binding;

        public MeasurementViewHolder(@NonNull ItemMeasurementViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemMeasurementViewBinding getBinding() {
            return binding;
        }
    }

    public interface MeasurementClickListener {
        void onMeasurementClick(List<String> imageUrlList, List<Uri> imageUriList, int position);
    }
}
