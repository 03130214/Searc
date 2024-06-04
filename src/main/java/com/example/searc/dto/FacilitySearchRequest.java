package com.example.searc.dto;

public class FacilitySearchRequest {
    private String startLocationName;
    private String facilityType;

    // 构造函数
    public FacilitySearchRequest(String startLocationName, String facilityType) {
        this.startLocationName = startLocationName;
        this.facilityType = facilityType;
    }

    // Getters and Setters
    public String getStartLocationName() {
        return startLocationName;
    }

    public void setStartLocationName(String startLocationName) {
        this.startLocationName = startLocationName;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }
}
