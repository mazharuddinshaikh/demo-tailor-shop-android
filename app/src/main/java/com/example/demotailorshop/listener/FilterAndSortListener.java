package com.example.demotailorshop.listener;

import com.example.demotailorshop.entity.DressFilter;

public interface FilterAndSortListener {

    void onFilterChecked(DressFilter filter, boolean isChecked);
}
