package com.example.demotailorshop.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotailorshop.databinding.ItemFilterByDateBinding;
import com.example.demotailorshop.databinding.ItemFilterByDateRangeBinding;
import com.example.demotailorshop.databinding.ItemFilterByTypeMonthYearListBinding;
import com.example.demotailorshop.entity.DressType;
import com.example.demotailorshop.listener.FilterAndSortListener;
import com.example.demotailorshop.utils.DtsUtils;

import java.util.List;

public class DressFilterAdapter extends RecyclerView.Adapter {
    private List<DressType> filterDataList;
    private int filterBehaviour;
    private FilterAndSortListener filterAndSortListener;

    public DressFilterAdapter() {
    }

    public void setFilterDataList(List<DressType> filterDataList) {
        this.filterDataList = filterDataList;
    }

    public void setFilterBehaviour(int filterBehaviour) {
        this.filterBehaviour = filterBehaviour;
    }

    public void setFilterAndSortListener(FilterAndSortListener filterAndSortListener) {
        this.filterAndSortListener = filterAndSortListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (this.filterBehaviour) {
            case DtsUtils.VIEW_TYPE_CHECKBOX:
                ItemFilterByTypeMonthYearListBinding filterByTypeBinding = ItemFilterByTypeMonthYearListBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false);
                return new FilterByDressTypeViewHolder(filterByTypeBinding);
//            case DtsUtils.VIEW_TYPE_DATE:
//                ItemFilterByDateBinding filterByDateBinding = ItemFilterByDateBinding.inflate(LayoutInflater.from(parent.getContext()),
//                        parent, false);
//                return new FilterByDateViewHolder(filterByDateBinding);
//            case DtsUtils.VIEW_TYPE_DATE_RANGE:
//                ItemFilterByDateRangeBinding filterByDateRangeBinding = ItemFilterByDateRangeBinding.inflate(LayoutInflater.from(parent.getContext()),
//                        parent, false);
//                return new FilterByDateRangeViewHolder(filterByDateRangeBinding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (this.filterBehaviour) {
            case DtsUtils.VIEW_TYPE_CHECKBOX:
                final DressType dressType = filterDataList.get(position);
                FilterByDressTypeViewHolder dressTypeViewHolder = (FilterByDressTypeViewHolder) holder;
                ItemFilterByTypeMonthYearListBinding filterByDressTypeBinding = dressTypeViewHolder.getBinding();
                filterByDressTypeBinding.cbFilter.setText(dressType.getTypeName());
                break;
//            case DtsUtils.VIEW_TYPE_DATE:
//
//                break;
//            case DtsUtils.VIEW_TYPE_DATE_RANGE:
//
//                break;

        }
    }

    @Override
    public int getItemCount() {
//        if (this.filterBehaviour == DtsUtils.VIEW_TYPE_DATE || this.filterBehaviour == DtsUtils.VIEW_TYPE_DATE_RANGE) {
//            return 1;
//        }
        return DtsUtils.isNullOrEmpty(filterDataList) ? 0 : this.filterDataList.size();
    }


    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        switch (this.filterBehaviour) {
            case DtsUtils.VIEW_TYPE_CHECKBOX:
                viewType = DtsUtils.VIEW_TYPE_CHECKBOX;
                break;
//            case DtsUtils.VIEW_TYPE_DATE:
//                viewType = DtsUtils.VIEW_TYPE_DATE;
//                break;
//            case DtsUtils.VIEW_TYPE_DATE_RANGE:
//                viewType = DtsUtils.VIEW_TYPE_DATE_RANGE;
//                break;
        }
        return viewType;
    }

    class FilterByDressTypeViewHolder extends RecyclerView.ViewHolder {
        private ItemFilterByTypeMonthYearListBinding binding;

        public FilterByDressTypeViewHolder(ItemFilterByTypeMonthYearListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemFilterByTypeMonthYearListBinding getBinding() {
            return binding;
        }
    }

    //Implements later
    static class FilterByDateViewHolder extends RecyclerView.ViewHolder {
        ItemFilterByDateBinding binding;

        public FilterByDateViewHolder(ItemFilterByDateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemFilterByDateBinding getBinding() {
            return binding;
        }
    }

    //Implements later
    static class FilterByDateRangeViewHolder extends RecyclerView.ViewHolder {
        ItemFilterByDateRangeBinding binding;

        public FilterByDateRangeViewHolder(ItemFilterByDateRangeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemFilterByDateRangeBinding getBinding() {
            return binding;
        }
    }


}
