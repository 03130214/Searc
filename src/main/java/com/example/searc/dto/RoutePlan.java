package com.example.searc.dto;

import java.util.List;

public class RoutePlan {
    private List<String> steps;
    private double totalDistance;
    private double totalTime;

    // 构造器
    public RoutePlan() {}

    public RoutePlan(List<String> steps, double totalDistance, double totalTime) {
        this.steps = steps;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
    }

    // Getter 和 Setter
    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }
}
