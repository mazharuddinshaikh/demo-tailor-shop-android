package com.example.demotailorshop.entity;


import java.util.List;

public class DressFilter {
    private String filterType;
    private String filterMessage;
    private List<DressType> dressTypeList;
    private int filterBehaviour;

    public DressFilter() {
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getFilterMessage() {
        return filterMessage;
    }

    public void setFilterMessage(String filterMessage) {
        this.filterMessage = filterMessage;
    }

    public List<DressType> getDressTypeList() {
        return dressTypeList;
    }

    public void setDressTypeList(List<DressType> dressTypeList) {
        this.dressTypeList = dressTypeList;
    }

    public int getFilterBehaviour() {
        return filterBehaviour;
    }

    public void setFilterBehaviour(int filterBehaviour) {
        this.filterBehaviour = filterBehaviour;
    }
}
