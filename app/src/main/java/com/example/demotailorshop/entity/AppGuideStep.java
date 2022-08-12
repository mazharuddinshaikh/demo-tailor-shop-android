package com.example.demotailorshop.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AppGuideStep {
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("stepsImageList")
    private List<String> stepsImageList;

    public AppGuideStep() {
    }

    public AppGuideStep(String title, String description) {
        super();
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getStepsImageList() {
        return stepsImageList;
    }

    public void setStepsImageList(List<String> stepsImageList) {
        this.stepsImageList = stepsImageList;
    }

}

