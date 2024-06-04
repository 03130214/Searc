package com.example.searc.dto;

public class FacilitySearchResult {
    private String locationName;
    private double distance;

    // 构造函数
    public FacilitySearchResult(String locationName, double distance) {
        this.locationName = locationName;
        this.distance = distance;
    }

    // Getters and Setters
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
