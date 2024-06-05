package com.example.searc.service;

import com.example.searc.dto.FacilitySearchResult;
import com.example.searc.model.InnerLocation;
import java.util.*;

public class InnerGraph {
    private Map<InnerLocation, List<InnerEdge>> adjacencyList;
    private Map<String, Double> distanceMap; // 存储两个地点间距离的映射
    private Map<String, Double> distanceMap1;
    public InnerGraph() {
        this.adjacencyList = new HashMap<>();// 该地点连的边
        this.distanceMap = new HashMap<>();// 两点的权重距离
        this.distanceMap1 = new HashMap<>();// 两点的距离
    }

    public void addVertex(InnerLocation location) {
        adjacencyList.putIfAbsent(location, new ArrayList<>());
    }

    public void addEdge(InnerLocation source, InnerLocation destination, double distance, double weight, double congestionFactor) {
        adjacencyList.get(source).add(new InnerEdge(destination, distance, weight, congestionFactor));
        distanceMap.put(source.getId() + "-" + destination.getId(), weight);//权重
        distanceMap1.put(source.getId() + "-" + destination.getId(), distance);//距离
    }
    

    public List<InnerEdge> getNeighbors(InnerLocation location) {
        return adjacencyList.getOrDefault(location, new ArrayList<>());
    }

    public double getDistance(InnerLocation start, InnerLocation end) {
        return distanceMap.getOrDefault(start.getId() + "-" + end.getId(), Double.MAX_VALUE);
    }

    public double getDistance1(InnerLocation start, InnerLocation end) {
        return distanceMap1.getOrDefault(start.getId() + "-" + end.getId(), Double.MAX_VALUE);
    }

    public double getCongestionFactor(InnerLocation start, InnerLocation end) {
        return adjacencyList.get(start).stream()
                .filter(edge -> edge.getTarget().equals(end))
                .findFirst()
                .map(InnerEdge::getCongestionFactor)
                .orElse(1.0);
    }

    public List<FacilitySearchResult> bfsFindLocationsByType(InnerLocation start, String type, int maxResults) {
        Queue<InnerLocation> queue = new LinkedList<>();
        Map<InnerLocation, Double> distanceToStart = new HashMap<>(); // 保存到起始点的距离
        Set<InnerLocation> visited = new HashSet<>();
        List<FacilitySearchResult> foundLocations = new ArrayList<>();

        queue.add(start);
        visited.add(start);
        distanceToStart.put(start, 0.0); // 起始点的距离为0

        while (!queue.isEmpty() && foundLocations.size() < maxResults) {
            InnerLocation current = queue.poll();
            double currentDistance = distanceToStart.get(current);

            for (InnerEdge edge : getNeighbors(current)) {
                InnerLocation neighbor = edge.getTarget();
                double totalDistance = currentDistance + edge.getDistance(); // 更新到该邻居的距离

                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    distanceToStart.put(neighbor, totalDistance); // 更新距离

                    if (neighbor.getType().equals(type) && !neighbor.equals(start)) {
                        foundLocations.add(new FacilitySearchResult(neighbor.getName(), totalDistance));
                        if (foundLocations.size() == maxResults)
                            break;
                    }
                }
            }
        }

        // 按距离排序
        Collections.sort(foundLocations, Comparator.comparingDouble(FacilitySearchResult::getDistance));
        return foundLocations;
    }

    public List<InnerLocation> findShortestPath(InnerGraph graph, InnerLocation start, List<InnerLocation> waypoints) {
        List<InnerLocation> path = new ArrayList<>();
        InnerLocation current = start;
    
        for (InnerLocation waypoint : waypoints) {
            List<InnerLocation> segment = aStarSearch(current, waypoint);
            if (!path.isEmpty()) {
                segment.remove(0); // 移除重复的起点
            }
            path.addAll(segment);
            current = waypoint;
        }
    
        // 添加从最后一个途经点回到起点的路径
        List<InnerLocation> returnPath = aStarSearch(current, start);
        if (!path.isEmpty()) {
            returnPath.remove(0); // 移除重复的起点
        }
        path.addAll(returnPath);
    
        return path;
    }
    

    private List<InnerLocation> aStarSearch(InnerLocation start, InnerLocation goal) {
        PriorityQueue<InnerLocation> openSet = new PriorityQueue<>(Comparator.comparingDouble(InnerLocation::getFCost));//优先队列
        start.setGCost(0);
        start.setFCost(start.getGCost() + heuristic(start, goal));
        openSet.add(start);
        // 使用一个优先队列来存储所有待检查的节点（InnerLocation），
        // 这些节点根据它们的 fCost（即 gCost + heuristic）排序。
        // fCost 是从起点到该点的已知最低成本（gCost）加上从该点到目标点的启发式估计成本（heuristic）。
        // 这确保了每次从队列中取出的都是当前估计总成本最低的节点。
        Map<InnerLocation, InnerLocation> cameFrom = new HashMap<>();//路径重建映射
        // cameFrom 是一个映射，用于最后重建路径。它记录了到达每个节点的最优路径上的前一个节点。
        Map<InnerLocation, Double> gScore = new HashMap<>();//成本跟踪
        // gScore 是一个映射，记录从起点到图中每个节点的最短路径的成本。
        // 初始时，起点的 gScore 设为 0（因为从起点到自身的距离是 0），
        // 其他所有节点设为无穷大，表示开始时没有已知的路径到这些节点。
        adjacencyList.keySet().forEach(v -> gScore.put(v, Double.MAX_VALUE));
        gScore.put(start, 0.0);

        while (!openSet.isEmpty()) {
            InnerLocation current = openSet.poll();
            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }

            for (InnerEdge edge : getNeighbors(current)) {
                InnerLocation neighbor = edge.getTarget();
                double tentativeGScore = gScore.get(current) + edge.getWeight();
                if (tentativeGScore < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    neighbor.setGCost(tentativeGScore);
                    neighbor.setFCost(tentativeGScore + heuristic(neighbor, goal));
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    private double heuristic(InnerLocation a, InnerLocation b) {
        double dx = a.getLongitude() - b.getLongitude();
        double dy = a.getLatitude() - b.getLatitude();
        return Math.sqrt(dx * dx + dy * dy);
    }
    // 启发式函数是 A* 算法的核心，用于估计从当前节点到目标节点的最小成本。
    // 在这段代码中，启发式函数的实现细节没有显示，但通常使用如欧几里得距离或曼哈顿距离等。
    private List<InnerLocation> reconstructPath(Map<InnerLocation, InnerLocation> cameFrom, InnerLocation current) {
        List<InnerLocation> totalPath = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            totalPath.add(current);
            current = cameFrom.get(current);
        }
        totalPath.add(current); // 添加起点
        Collections.reverse(totalPath);
        return totalPath;
    }

    private static class InnerEdge {
        InnerLocation target;
        double distance;
        double congestionFactor;
        double weight; // 新增权重属性
    
        public InnerEdge(InnerLocation target, double distance, double weight, double congestionFactor) {
            this.target = target;
            this.distance = distance;
            this.weight = weight;
            this.congestionFactor = congestionFactor;
        }
    
        public InnerLocation getTarget() {
            return target;
        }
    
        public double getDistance() {
            return distance;
        }
    
        public double getWeight() {
            return weight;
        }
    
        public double getCongestionFactor() {
            return congestionFactor;
        }
    }
    
}
