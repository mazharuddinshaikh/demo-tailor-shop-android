package com.example.demotailorshop.entity;

import android.net.Uri;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Map;

public class Measurement {

    private int measurementId;
    private List<String> measurementImageList;
    private List<String> rawDressImageList;
    private List<String> patternImageList;
    private List<String> seavedImageList;
    private String comment;
    private String createdAt;
    private String updatedAt;
    private MeasurementImage measurementImage;
    @JsonIgnore
    private Map<String, List<Uri>> uriMap;

    public Measurement() {
        super();
//        pattern = Pattern.compile("\\|\\|");
    }

    public int getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(int measurementId) {
        this.measurementId = measurementId;
    }

    public List<String> getMeasurementImageList() {
        return measurementImageList;
    }

    public void setMeasurementImageList(List<String> measurementImageList) {
        this.measurementImageList = measurementImageList;
    }

    public List<String> getRawDressImageList() {
        return rawDressImageList;
    }

    public void setRawDressImageList(List<String> rawDressImageList) {
        this.rawDressImageList = rawDressImageList;
    }

    public List<String> getPatternImageList() {
        return patternImageList;
    }

    public void setPatternImageList(List<String> patternImageList) {
        this.patternImageList = patternImageList;
    }

    public List<String> getSeavedImageList() {
        return seavedImageList;
    }

    public void setSeavedImageList(List<String> seavedImageList) {
        this.seavedImageList = seavedImageList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Map<String, List<Uri>> getUriMap() {
        return uriMap;
    }

    public void setUriMap(Map<String, List<Uri>> uriMap) {
        this.uriMap = uriMap;
    }

    public MeasurementImage getMeasurementImage() {
        return measurementImage;
    }

    public void setMeasurementImage(MeasurementImage measurementImage) {
        this.measurementImage = measurementImage;
    }

    public static class MeasurementImage {
        private List<String> measurementImageList;
        private List<String> rawImageList;
        private List<String> patternImageList;
        private List<String> seavedImageList;

        public MeasurementImage() {
        }

        public List<String> getMeasurementImageList() {
            return measurementImageList;
        }

        public void setMeasurementImageList(List<String> measurementImageList) {
            this.measurementImageList = measurementImageList;
        }

        public List<String> getRawImageList() {
            return rawImageList;
        }

        public void setRawImageList(List<String> rawImageList) {
            this.rawImageList = rawImageList;
        }

        public List<String> getPatternImageList() {
            return patternImageList;
        }

        public void setPatternImageList(List<String> patternImageList) {
            this.patternImageList = patternImageList;
        }

        public List<String> getSeavedImageList() {
            return seavedImageList;
        }

        public void setSeavedImageList(List<String> seavedImageList) {
            this.seavedImageList = seavedImageList;
        }
    }
}
