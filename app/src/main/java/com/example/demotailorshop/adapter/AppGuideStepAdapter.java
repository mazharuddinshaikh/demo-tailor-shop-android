package com.example.demotailorshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotailorshop.databinding.ItemAppGuideListBinding;
import com.example.demotailorshop.databinding.ItemAppGuideStepListBinding;
import com.example.demotailorshop.entity.AppGuideStep;
import com.example.demotailorshop.utils.DtsUtils;
import com.google.android.material.appbar.AppBarLayout;

import java.text.MessageFormat;
import java.util.ConcurrentModificationException;
import java.util.List;

public class AppGuideStepAdapter extends RecyclerView.Adapter<AppGuideStepAdapter.AppGuideStepViewHolder> {
    private List<AppGuideStep> appGuideStepList;
    private Context context;

    public void setAppGuideStepList(List<AppGuideStep> appGuideStepList) {
        this.appGuideStepList = appGuideStepList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AppGuideStepAdapter.AppGuideStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppGuideStepListBinding appGuideStepListBinding = ItemAppGuideStepListBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new AppGuideStepAdapter.AppGuideStepViewHolder(appGuideStepListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppGuideStepAdapter.AppGuideStepViewHolder holder, int position) {
        final ItemAppGuideStepListBinding binding = holder.getBinding();
        final AppGuideStep appGuideStep = appGuideStepList.get(position);
        binding.tvStepTitle.setText(MessageFormat.format("{0}{1}{2}", position + 1, ". ", appGuideStep.getTitle()));
        if (DtsUtils.isNullOrEmpty(appGuideStep.getDescription())) {
            binding.tvStepDescription.setVisibility(View.GONE);
        } else {
            binding.tvStepDescription.setText(appGuideStep.getDescription());
        }

        if (!DtsUtils.isNullOrEmpty(appGuideStep.getStepsImageList())) {
            final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            final AppGuideStepImageAdapter appGuideStepImageAdapter = new AppGuideStepImageAdapter();
            appGuideStepImageAdapter.setStepImageList(appGuideStep.getStepsImageList());
            appGuideStepImageAdapter.setContext(context);
            binding.rvStepImage.setLayoutManager(layoutManager);
            binding.rvStepImage.setAdapter(appGuideStepImageAdapter);

            appGuideStepImageAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public int getItemCount() {
        return !DtsUtils.isNullOrEmpty(appGuideStepList) ? appGuideStepList.size() : 0;
    }

    public static class AppGuideStepViewHolder extends RecyclerView.ViewHolder {
        private final ItemAppGuideStepListBinding binding;

        public AppGuideStepViewHolder(@NonNull ItemAppGuideStepListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemAppGuideStepListBinding getBinding() {
            return binding;
        }
    }
}
