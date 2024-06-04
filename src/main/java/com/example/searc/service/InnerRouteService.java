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
        // List<InnerLocation> shortestTimePath = findShortestTimePath(graph, startLocation, waypoints, routeRequest.getTransportMode());

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
            graph.addEdge(start, end, road.getDistance(), road.getCongestionFactor());
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

    private double calculateTotalDistance(InnerGraph graph, List<InnerLocation> path) {
        double totalDistance = 0.0;
        for (int i = 0; i < path.size() - 2; i++) {
            totalDistance += graph.getDistance(path.get(i), path.get(i + 1));
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

    // private List<InnerLocation> findShortestTimePath(InnerGraph graph, InnerLocation start, List<InnerLocation> waypoints, int transportMode) {
    //     // 如果你需要单独计算最短时间路径，需要实现该方法
    //     // 可以类似于 findShortestPath 方法，通过使用 A* 算法来考虑拥堵因素
    //     // 假设 graph.findShortestTimePath 是一个你已经实现的计算最短时间路径的方法
    //     // return graph.findShortestTimePath(start, waypoints, transportMode);

    //     // 这里提供一个简单的示例实现
    //     // Note: 这是一个示例，需要你根据具体需求进行修改和完善
    //     return graph.findShortestPath(graph, start, waypoints);
    // }
}
