package com.example.demotailorshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotailorshop.databinding.ItemUserDressTypeBinding;
import com.example.demotailorshop.entity.DressType;
import com.example.demotailorshop.utils.DtsUtils;

import java.util.List;

public class UserDressTypeAdapter extends RecyclerView.Adapter<UserDressTypeAdapter.UserDressTypeViewHolder> {
    private List<DressType> dressTypeList;
    private OnDressTypeClickListener dressTypeClickListener;


    public void setDressTypeList(List<DressType> dressTypeList) {
        this.dressTypeList = dressTypeList;
    }

    public void setDressTypeClickListener(OnDressTypeClickListener dressTypeClickListener) {
        this.dressTypeClickListener = dressTypeClickListener;
    }

    @NonNull
    @Override
    public UserDressTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserDressTypeBinding binding = ItemUserDressTypeBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new UserDressTypeAdapter.UserDressTypeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDressTypeViewHolder holder, int position) {
        ItemUserDressTypeBinding binding = holder.getBinding();
        DressType dressType = dressTypeList.get(position);
        if (dressType != null) {
            binding.tvTypeName.setText(dressType.getTypeName());
            binding.tvTypeDescription.setText(dressType.getTypeDescription());
            binding.tvPrice.setText(String.valueOf(dressType.getPrice()));
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dressTypeClickListener.onDressTypeClick(dressType);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (!DtsUtils.isNullOrEmpty(dressTypeList)) {
            itemCount = dressTypeList.size();
        }
        return itemCount;
    }

    public static class UserDressTypeViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserDressTypeBinding binding;

        public UserDressTypeViewHolder(@NonNull ItemUserDressTypeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemUserDressTypeBinding getBinding() {
            return binding;
        }
    }

    public interface OnDressTypeClickListener {
        void onDressTypeClick(DressType dressType);
    }
}
