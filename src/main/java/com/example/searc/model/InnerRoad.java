package com.example.searc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "innerroads")
public class InnerRoad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long startLocationId;  // 起始地点ID

    @Column(nullable = false)
    private Long endLocationId;  // 终点地点ID

    @Column(nullable = false)
    private double distance;  // 距离（单位可以是米或公里）

    @Column
    private double congestionFactor;  // 拥堵系数

    @Column
    private Long flag;  // 可否通行
    // 构造函数、getter和setter
    public InnerRoad() {}

    public InnerRoad(Long startLocationId, Long endLocationId, double distance, double congestionFactor,Long flag) {
        this.startLocationId = startLocationId;
        this.endLocationId = endLocationId;
        this.distance = distance;
        this.congestionFactor = congestionFactor;
        this.flag=flag;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStartLocationId() {
        return startLocationId;
    }

    public void setStartLocationId(Long startLocationId) {
        this.startLocationId = startLocationId;
    }

    public Long getEndLocationId() {
        return endLocationId;
    }

    public void setEndLocationId(Long endLocationId) {
        this.endLocationId = endLocationId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getCongestionFactor() {
        return congestionFactor;
    }

    public void setCongestionFactor(double congestionFactor) {
        this.congestionFactor = congestionFactor;
    }

    public Long getflag() {
        return flag;
    }

    public void setflag(Long flag) {
        this.flag = flag;
    }
}