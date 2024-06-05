package com.example.searc.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.searc.dto.RoutePlan;
import com.example.searc.dto.RouteRequest;
import com.example.searc.model.InnerLocation;
import com.example.searc.model.InnerRoad;
import com.example.searc.repository.InnerLocationRepository;
import com.example.searc.repository.InnerRoadRepository;
import com.example.searc.dto.FacilitySearchRequest;
import com.example.searc.dto.FacilitySearchResult;

/**
 * 服务类，用于处理路线规划的请求。
 */
@Service
public class InnerRouteService {

    @Autowired
    private InnerLocationRepository locationRepository;
    @Autowired
    private InnerRoadRepository roadRepository;


    public List<FacilitySearchResult> findClosestFacilities(FacilitySearchRequest request) {
        InnerLocation startLocation = findLocationByName(request.getStartLocationName());
        InnerGraph graph = buildGraph(locationRepository.findAll(), roadRepository.findAll());
        return graph.bfsFindLocationsByType(startLocation, request.getFacilityType(), 4);
    }
    
    /**
     * 根据路线请求计算路线规划。
     * 
     * @param routeRequest 包含起始地点、途经点和交通方式的路线请求数据。
     * @return 路线规划结果，包含路径步骤、总距离和总时间。
     */
    public RoutePlan calculateRoute(RouteRequest routeRequest) {
        List<InnerLocation> locations = locationRepository.findAll();
        List<InnerRoad> roads = roadRepository.findAll();

        InnerGraph graph = buildGraph(locations, roads);

        InnerLocation startLocation = findLocationByName(routeRequest.getStartLocation());

        List<InnerLocation> waypoints = routeRequest.getWaypoints().stream()
            .map(this::findLocationByName)
            .collect(Collectors.toList());

        List<InnerLocation> shortestPath = graph.findShortestPath(graph, startLocation, waypoints);

        double totalDistance = calculateTotalDistance(graph, shortestPath);
        double totalTime = calculateTotalTime(graph, shortestPath, routeRequest.getTransportMode());

        return new RoutePlan(convertSteps(shortestPath), totalDistance, totalTime);
    }

    private InnerGraph buildGraph(List<InnerLocation> locations, List<InnerRoad> roads) {
        InnerGraph graph = new InnerGraph();
        locations.forEach(graph::addVertex);
        roads.forEach(road -> {
            InnerLocation start = findLocationById(road.getStartLocationId());
            InnerLocation end = findLocationById(road.getEndLocationId());
            double weight = road.getDistance();
            graph.addEdge(start, end, road.getDistance(), weight, road.getCongestionFactor());
        });
        return graph;
    }

    public RoutePlan calculateRoute1(RouteRequest routeRequest) {
        List<InnerLocation> locations = locationRepository.findAll();
        List<InnerRoad> roads = roadRepository.findAll();
    
        // 使用修改后的buildGraph方法构建图，以考虑速度和拥堵系数
        InnerGraph graph = buildGraphForShortestTime(locations, roads, routeRequest.getTransportMode());
    
        InnerLocation startLocation = findLocationByName(routeRequest.getStartLocation());
    
        List<InnerLocation> waypoints = routeRequest.getWaypoints().stream()
            .map(this::findLocationByName)
            .collect(Collectors.toList());
    
        List<InnerLocation> shortestTimePath = graph.findShortestPath(graph, startLocation, waypoints);
    
        double totalDistance = calculateTotalDistance1(graph, shortestTimePath);
        double totalTime = calculateTotalDistance(graph, shortestTimePath);
    
        return new RoutePlan(convertSteps(shortestTimePath), totalDistance, totalTime);
    }
    
    private InnerGraph buildGraphForShortestTime(List<InnerLocation> locations, List<InnerRoad> roads, int transportMode) {
        double speed = transportMode == 0 ? 2.0 : 6.0; // 假设 0 表示步行，1 表示开车
        InnerGraph graph = new InnerGraph();
        locations.forEach(graph::addVertex);
        roads.forEach(road -> {
            InnerLocation start = findLocationById(road.getStartLocationId());
            InnerLocation end = findLocationById(road.getEndLocationId());
            double weight = road.getDistance() / (speed * road.getCongestionFactor());
            graph.addEdge(start, end, road.getDistance(), weight, road.getCongestionFactor());
        });
        return graph;
    }

    private InnerLocation findLocationByName(String name) {
        return locationRepository.findByName(name)
            .orElseThrow(() -> new IllegalArgumentException("Location not found: " + name));
    }

    private InnerLocation findLocationById(Long id) {
        return locationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + id));
    }

    private double calculateTotalDistance(InnerGraph graph, List<InnerLocation> path) {//权重路径
        double totalDistance = 0.0;
        for (int i = 0; i < path.size() - 2; i++) {
            totalDistance += graph.getDistance(path.get(i), path.get(i + 1));
        }
        return totalDistance;
    }

    private double calculateTotalDistance1(InnerGraph graph, List<InnerLocation> path) {//最短时间的距离
        double totalDistance = 0.0;
        for (int i = 0; i < path.size() - 2; i++) {
            totalDistance += graph.getDistance1(path.get(i), path.get(i + 1));
        }
        return totalDistance;
    }

    private double calculateTotalTime(InnerGraph graph, List<InnerLocation> path, int transportMode) {
        double totalTime = 0.0;
        double speed = transportMode == 0 ? 2.0 : 6.0;  // 假设 0 表示步行，1 表示开车
        for (int i = 0; i < path.size() - 2; i++) {
            double distance = graph.getDistance(path.get(i), path.get(i + 1));
            double congestionFactor = graph.getCongestionFactor(path.get(i), path.get(i + 1));
            totalTime += distance / (speed * congestionFactor);
        }
        return totalTime;
    }

    private List<String> convertSteps(List<InnerLocation> path) {
        return path.stream()
            .map(InnerLocation::getName)
            .collect(Collectors.toList());
    }

}
