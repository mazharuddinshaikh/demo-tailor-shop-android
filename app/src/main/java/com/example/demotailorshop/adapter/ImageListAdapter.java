package com.example.demotailorshop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demotailorshop.R;
import com.example.demotailorshop.databinding.ItemImageListBinding;
import com.example.demotailorshop.listener.MeasurementListener;
import com.example.demotailorshop.utils.DtsUtils;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageListViewHolder> {
    private List<String> imageList;
    private int displayWidth;
    private Context context;
    private MeasurementListener measurementListener;
    private String measurementType;
    private int dressId;
    private List<Uri> uriList;

    public ImageListAdapter() {
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    //this method is used to identify measurement i.e. is it measurement or raw image or seaved image or pattern
    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public void setMeasurementListener(MeasurementListener measurementListener) {
        this.measurementListener = measurementListener;
    }

    public void setDressId(int dressId) {
        this.dressId = dressId;
    }

    public void setUriList(List<Uri> uriList) {
        this.uriList = uriList;
    }

    @NonNull
    @Override
    public ImageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImageListBinding binding = ItemImageListBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new ImageListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageListViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ItemImageListBinding binding = holder.getBinding();
        binding.getRoot().getLayoutParams().width = this.displayWidth / 2;
        binding.getRoot().getLayoutParams().height = this.displayWidth / 2;
        if ((DtsUtils.isNullOrEmpty(imageList) && DtsUtils.isNullOrEmpty(uriList) && position == 0)
                || (!DtsUtils.isNullOrEmpty(imageList) && position >= imageList.size() && DtsUtils.isNullOrEmpty(uriList))
                || (!DtsUtils.isNullOrEmpty(uriList) && position >= uriList.size() && DtsUtils.isNullOrEmpty(imageList))
                || (!DtsUtils.isNullOrEmpty(imageList) && !DtsUtils.isNullOrEmpty(uriList) && position >= imageList.size() + uriList.size())) {
            Glide.with(this.context).load(R.drawable.ic_add_24)
                    .fallback(R.drawable.ic_no_image)
                    .error(R.drawable.ic_error_image)
                    .into(binding.cddlDress);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    measurementListener.onAddMeasurement(measurementType, dressId);
                }
            });

        } else {
            if (!DtsUtils.isNullOrEmpty(imageList) && position < imageList.size() && DtsUtils.isNullOrEmpty(uriList)) {
                Glide.with(this.context).load(imageList.get(position))
                        .fallback(R.drawable.ic_no_image)
                        .error(R.drawable.ic_error_image)
                        .into(binding.cddlDress);
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        measurementListener.showMeasurementByImage(imageList, position);
                        measurementListener.showMeasurement(imageList, uriList, position);
                    }
                });
            } else if (!DtsUtils.isNullOrEmpty(uriList) && position < uriList.size() && DtsUtils.isNullOrEmpty(imageList)) {
                Glide.with(this.context).load(uriList.get(position))
                        .fallback(R.drawable.ic_no_image)
                        .error(R.drawable.ic_error_image)
                        .into(binding.cddlDress);
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        measurementListener.showMeasurementByUri(uriList, position);
                        measurementListener.showMeasurement(imageList, uriList, position);
                    }
                });
            } else if (!DtsUtils.isNullOrEmpty(imageList) && !DtsUtils.isNullOrEmpty(uriList)) {
                if (position < imageList.size()) {
                    Glide.with(this.context).load(imageList.get(position))
                            .fallback(R.drawable.ic_no_image)
                            .error(R.drawable.ic_error_image)
                            .into(binding.cddlDress);
                    binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            measurementListener.showMeasurementByImage(imageList, position);
                            measurementListener.showMeasurement(imageList, uriList, position);
                        }
                    });
                } else {
                    int uriPosition = position - imageList.size();
                    Glide.with(this.context).load(uriList.get(uriPosition))
                            .fallback(R.drawable.ic_no_image)
                            .error(R.drawable.ic_error_image)
                            .into(binding.cddlDress);
                    binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            measurementListener.showMeasurementByUri(uriList, position);
                            measurementListener.showMeasurement(imageList, uriList, position);
                        }
                    });
                }
            }
        }


    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (!DtsUtils.isNullOrEmpty(uriList)) {
            size = uriList.size();
        }
        return DtsUtils.isNullOrEmpty(imageList) ? size + 1 : size + imageList.size() + 1;
    }

    public static class ImageListViewHolder extends RecyclerView.ViewHolder {
        ItemImageListBinding binding;

        public ImageListViewHolder(ItemImageListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemImageListBinding getBinding() {
            return binding;
        }
    }


}
