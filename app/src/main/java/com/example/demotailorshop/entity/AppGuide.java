package com.example.demotailorshop.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AppGuide {
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("helpStepsList")
    private List<AppGuideStep> helpStepsList;

    public AppGuide() {
        super();
    }

    public AppGuide(String title, String description) {
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

    public List<AppGuideStep> getStepList() {
        return helpStepsList;
    }

    public void setStepList(List<AppGuideStep> stepList) {
        this.helpStepsList = stepList;
    }
}

