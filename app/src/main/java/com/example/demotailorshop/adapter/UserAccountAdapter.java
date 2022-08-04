package com.example.demotailorshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotailorshop.databinding.ItemUserAccountBinding;
import com.example.demotailorshop.listener.UserAccountSelectionListener;

import java.util.List;

public class UserAccountAdapter extends RecyclerView.Adapter<UserAccountAdapter.UserAccountViewHolder> {
    private List<String> optionList;
    UserAccountSelectionListener userAccountSelectionListener;

    public void setOptionList(List<String> optionList) {
        this.optionList = optionList;
    }

    public void setUserAccountSelectionListener(UserAccountSelectionListener userAccountSelectionListener) {
        this.userAccountSelectionListener = userAccountSelectionListener;
    }

    @NonNull
    @Override
    public UserAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserAccountBinding binding = ItemUserAccountBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new UserAccountAdapter.UserAccountViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAccountViewHolder holder, int position) {
        ItemUserAccountBinding binding = holder.getBinding();
        binding.tvOptionName.setText(optionList.get(position));
        binding.tvOptionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAccountSelectionListener.onOptionSelected(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    public static class UserAccountViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserAccountBinding binding;

        public UserAccountViewHolder(@NonNull ItemUserAccountBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemUserAccountBinding getBinding() {
            return binding;
        }
    }
}
