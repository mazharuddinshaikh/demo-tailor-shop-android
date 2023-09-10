package com.example.demotailorshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotailorshop.databinding.ItemAppGuideListBinding;
import com.example.demotailorshop.databinding.ItemAppUpdateBinding;
import com.example.demotailorshop.utils.DtsUtils;

import java.util.List;

public class AppUpdateAdapter extends RecyclerView.Adapter<AppUpdateAdapter.AppUpdateViewHolder> {
    private List<String> featureList;

    public void setFeatureList(List<String> featureList) {
        this.featureList = featureList;
    }

    @NonNull
    @Override
    public AppUpdateAdapter.AppUpdateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppUpdateBinding appUpdateBinding = ItemAppUpdateBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new AppUpdateAdapter.AppUpdateViewHolder(appUpdateBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppUpdateAdapter.AppUpdateViewHolder holder, int position) {
        ItemAppUpdateBinding appUpdateBinding = holder.getBinding();
        if (!DtsUtils.isNullOrEmpty(featureList)) {
            String appFeature = featureList.get(position);
            appUpdateBinding.tvAppFeature.setText(appFeature);
        }
    }

    @Override
    public int getItemCount() {
        return DtsUtils.isNullOrEmpty(featureList) ? 0 : featureList.size();
    }

    public static class AppUpdateViewHolder extends RecyclerView.ViewHolder {
        private final ItemAppUpdateBinding binding;

        public AppUpdateViewHolder(ItemAppUpdateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemAppUpdateBinding getBinding() {
            return binding;
        }
    }
}
