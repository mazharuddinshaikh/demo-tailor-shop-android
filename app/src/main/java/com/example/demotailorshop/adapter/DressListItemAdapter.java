package com.example.demotailorshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demotailorshop.R;
import com.example.demotailorshop.databinding.ItemDressListItemBinding;
import com.example.demotailorshop.entity.Dress;
import com.example.demotailorshop.listener.DressListClickListener;
import com.example.demotailorshop.utils.DtsUtils;

import java.util.List;

public class DressListItemAdapter extends RecyclerView.Adapter<DressListItemAdapter.DressListItemViewHolder> {
    private List<String> imageList;
    private Context context;
    private int displayWidth;
    private DressListClickListener listener;
    private Dress dress;

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public void setListener(DressListClickListener listener) {
        this.listener = listener;
    }

    public void setDress(Dress dress) {
        this.dress = dress;
    }

    @NonNull
    @Override
    public DressListItemAdapter.DressListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDressListItemBinding binding = ItemDressListItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new DressListItemAdapter.DressListItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DressListItemAdapter.DressListItemViewHolder holder, int position) {
        ItemDressListItemBinding binding = holder.getBinding();
        binding.getRoot().getLayoutParams().width = this.displayWidth / 2;
        binding.getRoot().getLayoutParams().height = this.displayWidth / 2;
        if (!DtsUtils.isNullOrEmpty(imageList)) {
            Glide.with(this.context).load(imageList.get(position))
                    .fallback(R.drawable.ic_no_photography_24)
                    .error(R.drawable.pant)
                    .into(binding.getRoot());
        }
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDressClicked(dress);
            }
        });

    }

    @Override
    public int getItemCount() {
        return DtsUtils.isNullOrEmpty(imageList) ? 0 : imageList.size();
    }

    public static class DressListItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemDressListItemBinding binding;

        public DressListItemViewHolder(@NonNull ItemDressListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemDressListItemBinding getBinding() {
            return binding;
        }
    }
}
