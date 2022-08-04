package com.example.demotailorshop.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demotailorshop.entity.DressFilter;
import com.example.demotailorshop.entity.DressType;
import com.example.demotailorshop.utils.DtsUtils;

import java.util.ArrayList;
import java.util.List;

public class DressFilterViewModel extends ViewModel {
    private MutableLiveData<List<DressFilter>> dressFilterListLiveData;

    public LiveData<List<DressFilter>> getDressFilterListLiveData() {
        if (dressFilterListLiveData == null) {
            dressFilterListLiveData = new MutableLiveData<>();
            dressFilterListLiveData.setValue(getDressFilterList());
        }
        return dressFilterListLiveData;
    }

    private List<DressFilter> getDressFilterList() {
        List<DressFilter> filterList = new ArrayList<>();
        filterList.add(getDressTypeFilter());
        filterList.add(getDressStatusFilter());
        filterList.add(getDressMonthFilter());
        filterList.add(getDressYearFilter());
        return filterList;
    }

    private DressFilter getDressTypeFilter() {
        DressFilter filter = new DressFilter();
        filter.setFilterMessage("Filter By Dress type");
        filter.setFilterBehaviour(DtsUtils.VIEW_TYPE_CHECKBOX);
        filter.setFilterType("DT");
        List<DressType> dressTypeList = new ArrayList<>();
        DressType dressType = new DressType();
        dressType.setUserDressTypeId(1);
        dressType.setTypeName("Dress");
        dressType.setTypeDescription("(Shirt Pant)");
        dressTypeList.add(dressType);
        dressType = new DressType();
        dressType.setUserDressTypeId(2);
        dressType.setTypeName("Kurta");
        dressType.setTypeDescription("(Kurta)");
        dressTypeList.add(dressType);
        dressType = new DressType();
        dressType.setUserDressTypeId(3);
        dressType.setTypeName("Pathani");
        dressType.setTypeDescription("(Kurta Paijama)");
        dressTypeList.add(dressType);
        dressType = new DressType();
        dressType.setUserDressTypeId(4);
        dressType.setTypeName("Safari");
        dressType.setTypeDescription("(Safari shirt and Pant)");
        dressTypeList.add(dressType);
        dressType = new DressType();
        dressType.setUserDressTypeId(5);
        dressType.setTypeName("Blazer");
        dressType.setTypeDescription("Blazer");
        dressTypeList.add(dressType);
        dressType = new DressType();
        dressType.setUserDressTypeId(6);
        dressType.setTypeName("Pant");
        dressType.setTypeDescription("Pant");
        dressTypeList.add(dressType);
        dressType.setUserDressTypeId(7);
        dressType.setTypeName("Shirt");
        dressType.setTypeDescription("Shirt");
        dressTypeList.add(dressType);
        filter.setDressTypeList(dressTypeList);
        return filter;
    }

    private DressFilter getDressStatusFilter() {
        DressFilter filter = new DressFilter();
        filter.setFilterMessage("Filter By Status");
        filter.setFilterBehaviour(DtsUtils.VIEW_TYPE_CHECKBOX);
        filter.setFilterType("DS");
        List<DressType> dressTypeList = new ArrayList<>();
        DressType dressType = new DressType();
        dressType.setUserDressTypeId(1);
        dressType.setTypeName("DELIVERED");
        dressTypeList.add(dressType);
        dressType = new DressType();
        dressType.setUserDressTypeId(2);
        dressType.setTypeName("PAYED");
        dressTypeList.add(dressType);
        dressType = new DressType();
        dressType.setUserDressTypeId(3);
        dressType.setTypeName("UNDELIVERED");
        dressTypeList.add(dressType);
        dressType = new DressType();
        dressType.setUserDressTypeId(4);
        dressType.setTypeName("MEASURED");
        dressTypeList.add(dressType);
        filter.setDressTypeList(dressTypeList);
        return filter;
    }

    private DressFilter getDressMonthFilter() {
        DressFilter filter = new DressFilter();
        filter.setFilterMessage("Filter By Month");
        filter.setFilterBehaviour(DtsUtils.VIEW_TYPE_CHECKBOX);
        filter.setFilterType("DM");
        List<DressType> dressTypeList = new ArrayList<>();
        DressType dressType = new DressType();
        dressType.setUserDressTypeId(1);
        dressType.setTypeName("JAN");
        dressTypeList.add(dressType);
        dressType = new DressType();
        dressType.setUserDressTypeId(2);
        dressType.setTypeName("FEB");
        dressTypeList.add(dressType);
        dressType = new DressType();
        dressType.setUserDressTypeId(3);
        dressType.setTypeName("MAR");
        dressTypeList.add(dressType);
        dressType = new DressType();
        dressType.setUserDressTypeId(4);
        dressType.setTypeName("APR");
        dressTypeList.add(dressType);
        dressType = new DressType();
        dressType.setUserDressTypeId(5);
        dressType.setTypeName("MAY");
        dressTypeList.add(dressType);
        filter.setDressTypeList(dressTypeList);
        return filter;
    }

    private DressFilter getDressYearFilter() {
        DressFilter filter = new DressFilter();
        filter.setFilterMessage("Filter By Year");
        filter.setFilterBehaviour(DtsUtils.VIEW_TYPE_CHECKBOX);
        filter.setFilterType("DY");
        List<DressType> dressTypeList = new ArrayList<>();
        DressType dressType = new DressType();
        dressType.setUserDressTypeId(1);
        dressType.setTypeName("2018");
        dressTypeList.add(dressType);
        dressType = new DressType();
        dressType.setUserDressTypeId(2);
        dressType.setTypeName("2019");
        dressTypeList.add(dressType);
        filter.setDressTypeList(dressTypeList);
        return filter;
    }

    private DressFilter getDressDateFilter() {
        DressFilter filter = new DressFilter();
        filter.setFilterMessage("Filter By Date");
        filter.setFilterBehaviour(DtsUtils.VIEW_TYPE_DATE);
        return filter;
    }

    private DressFilter getDressDateRangeFilter() {
        DressFilter filter = new DressFilter();
        filter.setFilterMessage("Filter By Date Range");
        filter.setFilterBehaviour(DtsUtils.VIEW_TYPE_DATE_RANGE);
        return filter;
    }
}
