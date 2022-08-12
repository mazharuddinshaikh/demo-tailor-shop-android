package com.example.demotailorshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotailorshop.databinding.ItemAppGuideListBinding;
import com.example.demotailorshop.databinding.ItemNoDressListBinding;
import com.example.demotailorshop.entity.AppGuide;
import com.example.demotailorshop.utils.DtsUtils;

import java.util.List;

public class AppGuideAdapter extends RecyclerView.Adapter<AppGuideAdapter.AppGuideViewHolder> {
    private List<AppGuide> appGuideList;
    private Context context;

    public void setAppGuideList(List<AppGuide> appGuideList) {
        this.appGuideList = appGuideList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AppGuideAdapter.AppGuideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppGuideListBinding appGuideListBinding = ItemAppGuideListBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new AppGuideAdapter.AppGuideViewHolder(appGuideListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppGuideAdapter.AppGuideViewHolder holder, int position) {
        ItemAppGuideListBinding binding = holder.getBinding();
        final AppGuide appGuide = appGuideList.get(position);
        binding.tvAppGuideTitle.setText(appGuide.getTitle());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        final AppGuideStepAdapter appGuideStepAdapter = new AppGuideStepAdapter();
        appGuideStepAdapter.setAppGuideStepList(appGuide.getStepList());
        appGuideStepAdapter.setContext(context);
        binding.rvAppGuideStep.setLayoutManager(layoutManager);
        binding.rvAppGuideStep.setAdapter(appGuideStepAdapter);
//        appGuideStepAdapter.notifyDataSetChanged();
        binding.tvAppGuideTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.rvAppGuideStep.getVisibility() == View.GONE) {
                    binding.rvAppGuideStep.setVisibility(View.VISIBLE);
                } else {
                    binding.rvAppGuideStep.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return !DtsUtils.isNullOrEmpty(appGuideList) ? appGuideList.size() : 0;
    }

    public static class AppGuideViewHolder extends RecyclerView.ViewHolder {
        private final ItemAppGuideListBinding binding;

        public AppGuideViewHolder(@NonNull ItemAppGuideListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemAppGuideListBinding getBinding() {
            return binding;
        }
    }
}
