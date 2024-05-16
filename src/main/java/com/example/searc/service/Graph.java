package com.example.searc.service;

// import java.util.*;

import com.example.searc.model.Location;

// /**
//  * 表示一个有向图，包含作为顶点的地点和作为边的道路。
//  */
// public class Graph {
//     private Map<Location, List<Edge>> adjacencyList;
//     private Map<String, Double> distanceMap;  // 存储两个地点间距离的映射

//     public Graph() {
//         this.adjacencyList = new HashMap<>();
//         this.distanceMap = new HashMap<>();
//     }

//     /**
//      * 向图中添加一个顶点。
//      * @param location 要添加为顶点的地点。
//      */
//     public void addVertex(Location location) {
//         adjacencyList.putIfAbsent(location, new ArrayList<>());
//     }

//     /**
//      * 向图中添加一条有向边。
//      * @param source 边的起始地点。
//      * @param destination 边的终点地点。
//      * @param distance 道路的距离。
//      * @param congestionFactor 影响行驶时间的拥堵系数。
//      */
//     public void addEdge(Location source, Location destination, double distance, double congestionFactor) {
//         adjacencyList.get(source).add(new Edge(destination, distance, congestionFactor));
//         // 存储地点间距离
//         distanceMap.put(source.getId() + "-" + destination.getId(), distance);
//     }

//     /**
//      * 查找从起始地点到途径点的最短路径。
//      * @param start 起始地点。
//      * @param waypoints 要访问的途径点列表。
//      * @return 表示最短路径的地点列表。
//      */
//     public List<Location> findShortestPath(Location start, List<Location> waypoints) {
//         Map<Location, Double> distances = new HashMap<>();
//         Map<Location, Location> previous = new HashMap<>();
//         PriorityQueue<Location> nodes = new PriorityQueue<>(Comparator.comparing(distances::get));

//         adjacencyList.keySet().forEach(vertex -> {
//             distances.put(vertex, vertex.equals(start) ? 0.0 : Double.MAX_VALUE);
//             nodes.add(vertex);
//         });

//         while (!nodes.isEmpty()) {
//             Location current = nodes.poll();
//             adjacencyList.get(current).forEach(edge -> {
//                 Location adjacent = edge.target;
//                 double newDist = distances.get(current) + edge.distance;
//                 if (newDist < distances.get(adjacent)) {
//                     distances.put(adjacent, newDist);
//                     previous.put(adjacent, current);
//                     nodes.add(adjacent);
//                 }
//             });
//         }

//         return reconstructPath(start, waypoints, previous);
//     }

//     /**
//      * 根据前驱节点地图重建路径。
//      * @param start 起始地点。
//      * @param waypoints 途径点列表。
//      * @param previous 前驱节点映射。
//      * @return 重建的路径列表。
//      */
//     private List<Location> reconstructPath(Location start, List<Location> waypoints, Map<Location, Location> previous) {
//         List<Location> path = new ArrayList<>();
//         Location end = waypoints.get(waypoints.size() - 1);
//         while (end != null && !end.equals(start)) {
//             path.add(end);
//             end = previous.get(end);
//         }
//         if (end != null) {
//             path.add(start);
//         }
//         Collections.reverse(path);
//         return path;
//     }

//     /**
//      * 根据地点ID获取两地之间的距离。
//      * @param location1 起始地点。
//      * @param location2 终点地点。
//      * @return 两地之间的距离。
//      */
//     public double getDistance(Location location1, Location location2) {
//         String key = location1.getId() + "-" + location2.getId();
//         if (distanceMap.containsKey(key)) {
//             return distanceMap.get(key);
//         }
//         throw new IllegalArgumentException("No distance found between " + location1.getName() + " and " + location2.getName());
//     }

//     public List<Location> findShortestTimePath(Location start, List<Location> waypoints, int transportMode) {
//         double speed = transportMode == 0 ? 2.0 : 6.0; // Assuming 2 m/s for walking, 6 m/s for electric scooters
//         Map<Location, Double> distances = new HashMap<>();
//         Map<Location, Location> previous = new HashMap<>();
//         PriorityQueue<Location> nodes = new PriorityQueue<>(Comparator.comparing(distances::get));
    
//         adjacencyList.keySet().forEach(vertex -> {
//             distances.put(vertex, vertex.equals(start) ? 0.0 : Double.MAX_VALUE);
//             nodes.add(vertex);
//         });
    
//         while (!nodes.isEmpty()) {
//             Location current = nodes.poll();
//             adjacencyList.get(current).forEach(edge -> {
//                 Location adjacent = edge.target;
//                 double time = distances.get(current) + edge.distance / (speed * (1 - edge.congestionFactor));
//                 if (time < distances.get(adjacent)) {
//                     distances.put(adjacent, time);
//                     previous.put(adjacent, current);
//                     nodes.add(adjacent);
//                 }
//             });
//         }
    
//         return reconstructPath(start, waypoints, previous);
//     }
    
//     private static class Edge {
//         Location target;
//         double distance;
//         double congestionFactor;

//         public Edge(Location target, double distance, double congestionFactor) {
//             this.target = target;
//             this.distance = distance;
//             this.congestionFactor = congestionFactor;
//         }
//     }
// }








import java.util.*;

public class Graph {
    private Map<Location, List<Edge>> adjacencyList;
    private Map<String, Double> distanceMap;  // 存储两个地点间距离的映射

    public Graph() {
        this.adjacencyList = new HashMap<>();
        this.distanceMap = new HashMap<>();
    }

    /**
     * 向图中添加一个顶点。
     * @param location 要添加为顶点的地点。
     */
    public void addVertex(Location location) {
        adjacencyList.putIfAbsent(location, new ArrayList<>());
    }

    /**
     * 向图中添加一条有向边。
     * @param source 边的起始地点。
     * @param destination 边的终点地点。
     * @param distance 道路的距离。
     * @param congestionFactor 影响行驶时间的拥堵系数。
     */
    public void addEdge(Location source, Location destination, double distance, double congestionFactor) {
        adjacencyList.get(source).add(new Edge(destination, distance, congestionFactor));
        // 存储地点间距离
        distanceMap.put(source.getId() + "-" + destination.getId(), distance);
    }

    /**
     * 获取一个地点的所有邻居节点及其边信息。
     * @param location 要获取邻居节点的地点。
     * @return 该地点的所有邻居节点及其边信息列表。
     */
    public List<Edge> getNeighbors(Location location) {
        return adjacencyList.getOrDefault(location, new ArrayList<>());
    }

    /**
     * A*搜索算法，用于查找从起始地点到目标地点的最短路径。
     * @param graph 图对象。
     * @param start 起始地点。
     * @param goal 目标地点。
     * @return 最短路径上的地点列表。
     */
    private List<Location> aStarSearch(Graph graph, Location start, Location goal) {
        PriorityQueue<Location> openSet = new PriorityQueue<>(Comparator.comparingDouble(Location::getFCost));
        start.setGCost(0);
        start.setFCost(start.getGCost() + heuristic(start, goal));
        openSet.add(start);
    
        Map<Location, Location> cameFrom = new HashMap<>();
        Map<Location, Double> gScore = new HashMap<>();
        graph.adjacencyList.keySet().forEach(v -> gScore.put(v, Double.MAX_VALUE));
        gScore.put(start, 0.0);
    
        while (!openSet.isEmpty()) {
            Location current = openSet.poll();
            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }
    
            for (Edge edge : graph.getNeighbors(current)) {
                Location neighbor = edge.getTarget();
                double tentativeGScore = gScore.get(current) + edge.getDistance();
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

    /**
     * 查找从起始地点经过所有中间点并返回起始地点的最短路径。
     * @param graph 图对象。
     * @param start 起始地点。
     * @param waypoints 要经过的中间点列表。
     * @return 最短路径上的地点列表。
     */
    public List<Location> findShortestPath(Graph graph, Location start, List<Location> waypoints) {
        List<Location> path = aStarSearch(graph, start, waypoints.get(waypoints.size() - 1));
        for (int i = waypoints.size() - 2; i >= 0; i--) {
            List<Location> partialPath = aStarSearch(graph, waypoints.get(i + 1), waypoints.get(i));
            partialPath.remove(0);  // 移除重复的起点
            path.addAll(partialPath);
        }
        path.add(start);  // 添加起点到路径末尾，形成闭环
        return path;
    }

    /**
     * 启发式函数，计算两个地点间的欧几里得距离。
     * @param a 起始地点。
     * @param b 目标地点。
     * @return 欧几里得距离。
     */
    private double heuristic(Location a, Location b) {
        double dx = a.getLongitude() - b.getLongitude();
        double dy = a.getLatitude() - b.getLatitude();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 根据cameFrom映射重建从起点到当前节点的路径。
     * @param cameFrom 路径映射。
     * @param current 当前节点。
     * @return 从起点到当前节点的路径。
     */
    private List<Location> reconstructPath(Map<Location, Location> cameFrom, Location current) {
        List<Location> totalPath = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            totalPath.add(current);
            current = cameFrom.get(current);
        }
        totalPath.add(current);  // 添加起点
        Collections.reverse(totalPath);
        return totalPath;
    }


    public double getDistance(Location start, Location end) {
        return distanceMap.getOrDefault(start.getId() + "-" + end.getId(), Double.MAX_VALUE);
    }

    public double getCongestionFactor(Location start, Location end) {
        return adjacencyList.get(start).stream()
                .filter(edge -> edge.getTarget().equals(end))
                .findFirst()
                .map(Edge::getCongestionFactor)
                .orElse(1.0);
    }

    /**
     * 内部类，表示图中的一条边。
     */
    private static class Edge {
        Location target;  // 目标节点
        double distance;  // 边的距离
        double congestionFactor;  // 拥堵系数

        public Edge(Location target, double distance, double congestionFactor) {
            this.target = target;
            this.distance = distance;
            this.congestionFactor = congestionFactor;
        }

        public Location getTarget() {
            return target;
        }

        public double getDistance() {
            return distance;
        }

        public double getCongestionFactor() {
            return congestionFactor;
        }
    }
}
