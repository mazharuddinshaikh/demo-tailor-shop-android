package com.example.demotailorshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demotailorshop.R;
import com.example.demotailorshop.databinding.ItemAppGuideStepImageBinding;
import com.example.demotailorshop.databinding.ItemAppGuideStepListBinding;
import com.example.demotailorshop.utils.DtsUtils;

import java.util.List;

public class AppGuideStepImageAdapter extends RecyclerView.Adapter<AppGuideStepImageAdapter.AppGuideStepImageViewHolder> {
    private List<String> stepImageList;
    private Context context;

    public void setStepImageList(List<String> stepImageList) {
        this.stepImageList = stepImageList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AppGuideStepImageAdapter.AppGuideStepImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppGuideStepImageBinding appGuideStepImageListBinding = ItemAppGuideStepImageBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new AppGuideStepImageAdapter.AppGuideStepImageViewHolder(appGuideStepImageListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppGuideStepImageAdapter.AppGuideStepImageViewHolder holder, int position) {
        final ItemAppGuideStepImageBinding binding = holder.getBinding();
        final String stepImage = stepImageList.get(position);
        if (!DtsUtils.isNullOrEmpty(stepImage)) {
            Glide.with(this.context).load(stepImage)
                    .fallback(R.drawable.ic_no_image)
                    .error(R.drawable.ic_error_image)
                    .into(binding.cdliStep);
        }
    }

    @Override
    public int getItemCount() {
        return !DtsUtils.isNullOrEmpty(stepImageList) ? stepImageList.size() : 0;
    }

    public static class AppGuideStepImageViewHolder extends RecyclerView.ViewHolder {
        private final ItemAppGuideStepImageBinding binding;

        public AppGuideStepImageViewHolder(@NonNull ItemAppGuideStepImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemAppGuideStepImageBinding getBinding() {
            return binding;
        }
    }
}
