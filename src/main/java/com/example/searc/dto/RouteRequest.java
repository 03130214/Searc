package com.example.searc.dto;

import java.util.List;

public class RouteRequest {
    private String startLocation;
    private List<String> waypoints;
    private int transportMode;

    // 构造器
    public RouteRequest() {}

    public RouteRequest(String startLocation, List<String> waypoints, int transportMode) {
        this.startLocation = startLocation;
        this.waypoints = waypoints;
        this.transportMode = transportMode;
    }

    // Getter 和 Setter
    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public List<String> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<String> waypoints) {
        this.waypoints = waypoints;
    }

    public int getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(int transportMode) {
        this.transportMode = transportMode;
    }
}
