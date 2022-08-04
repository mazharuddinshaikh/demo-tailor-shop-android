package com.example.demotailorshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotailorshop.databinding.ItemFilterDressBinding;
import com.example.demotailorshop.entity.DressFilter;
import com.example.demotailorshop.entity.DressType;
import com.example.demotailorshop.utils.DtsUtils;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {
    private Context context;
    private List<DressFilter> filterList;

    public FilterAdapter() {
    }

    public void setFilterList(List<DressFilter> filterList) {
        this.filterList = filterList;
    }


    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFilterDressBinding binding = ItemFilterDressBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new FilterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        ItemFilterDressBinding binding = holder.getBinding();
        DressFilter dressFilter = filterList.get(position);
        String filterName = dressFilter.getFilterMessage();
        List<DressType> typeList = dressFilter.getDressTypeList();
        binding.tvHeading.setText(filterName);
        DressFilterAdapter adapter = new DressFilterAdapter();
        adapter.setFilterDataList(typeList);
        adapter.setFilterBehaviour(dressFilter.getFilterBehaviour());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvFilter.setAdapter(adapter);
        binding.rvFilter.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return DtsUtils.isNullOrEmpty(filterList) ? 0 : filterList.size();
    }

    public static class FilterViewHolder extends RecyclerView.ViewHolder {
        private final ItemFilterDressBinding binding;

        public FilterViewHolder(ItemFilterDressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemFilterDressBinding getBinding() {
            return binding;
        }
    }

}
