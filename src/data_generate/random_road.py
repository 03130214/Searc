import random

def generate_connected_graph(num_nodes, max_edges):
    if max_edges < num_nodes - 1:
        raise ValueError("Number of edges is too few to create a connected graph with the given number of nodes.")
    
    # 创建最小生成树以保证图的基本连通性
    edges = set()
    for i in range(1, num_nodes):
        start = i
        end = i + 1 if i < num_nodes else 1
        distance = random.randint(500, 5000)
        congestion = round(random.uniform(0.01, 1.00), 2)
        edges.add((start, end, distance, congestion))  # 只添加一次，保证连通性

    # 添加额外的随机道路直到达到所需的最大数
    while len(edges) < max_edges:
        start = random.randint(1, num_nodes)
        end = random.randint(1, num_nodes)
        while start == end:
            end = random.randint(1, num_nodes)

        distance = random.randint(500, 5000)
        congestion = round(random.uniform(0.01, 1.00), 2)
        road = (start, end, distance, congestion)
        
        # 确保不添加重复的边
        if road not in edges and (end, start, distance, congestion) not in edges:
            edges.add(road)

    return list(edges)

# 生成具有440个节点和不超过800条边的连通图
road_data = generate_connected_graph(440, 800)

# 打印生成的道路数据，每项以逗号结尾
for road in road_data:
    print(f"({road[0]}, {road[1]}, {road[2]}, {road[3]}),")
