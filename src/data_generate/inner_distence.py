import math
import random

# Function to calculate distance in meters using the Haversine formula
def haversine_distance_meters(lat1, lon1, lat2, lon2):
    # Radius of the Earth in kilometers
    R = 6371.0
    
    # Convert degrees to radians
    lat1_rad, lon1_rad = math.radians(lat1), math.radians(lon1)
    lat2_rad, lon2_rad = math.radians(lat2), math.radians(lon2)
    
    # Change in coordinates
    delta_lat = lat2_rad - lat1_rad
    delta_lon = lon2_rad - lon1_rad
    
    # Haversine formula
    a = math.sin(delta_lat / 2)**2 + math.cos(lat1_rad) * math.cos(lat2_rad) * math.sin(delta_lon / 2)**2
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
    distance_km = R * c
    distance_m = distance_km * 1000  # Convert km to meters
    return distance_m

# Example latitude and longitude data
coordinates =[(39.9635, 116.3499), (39.9631, 116.35), (39.9631, 116.3493), (39.9628, 116.35), (39.9628, 116.3493), (39.9625, 116.3492), (39.9624, 116.3492), (39.9624, 116.35), (39.9619, 116.3493), (39.9619, 116.3501), (39.9609, 116.3493), (39.9609, 116.3501), (39.9603, 116.3493), (39.9598, 116.3494), (39.9598, 116.349), (39.9593, 116.3494), (39.9593, 116.3511), (39.9603, 116.3511), (39.9609, 116.351), (39.9619, 116.351), (39.9614, 116.351), (39.9614, 116.3511), (39.9624, 116.351), (39.9628, 116.3509), (39.963, 116.3509), (39.9635, 116.3509), (39.963, 116.3517), (39.9635, 116.3517), (39.963, 116.3519), (39.9628, 116.3519), (39.963, 116.3524), (39.9631, 116.3524), (39.9633, 116.3524), (39.9632, 116.3532), (39.9634, 116.3531), (39.9632, 116.3535), (39.9632, 116.3541), (39.9634, 116.3546), (39.9636, 116.3546), (39.9632, 116.3546), (39.9628, 116.3546), (39.9628, 116.3541), (39.9624, 116.3541), (39.9624, 116.3546), (39.9621, 116.3541), (39.9621, 116.3546), (39.9623, 116.3535), (39.9616, 116.3536), (39.9616, 116.3542), (39.9617, 116.3547), (39.961, 116.3542), (39.961, 116.3547), (39.9616, 116.3525), (39.962, 116.3525), (39.961, 116.3525), (39.961, 116.3531), (39.9624, 116.3524), (39.9623, 116.3525), (39.9601, 116.3547), (39.9601, 116.3543), (39.9603, 116.3543), (39.9603, 116.3531), (39.9604, 116.3531), (39.9604, 116.3525), (39.9594, 116.3525), (39.9594, 116.3532), (39.9588, 116.3532), (39.9588, 116.3525), (39.9588, 116.3511), (39.9584, 116.3512), (39.9587, 116.3494), (39.9584, 116.3502), (39.9584, 116.3494), (39.9581, 116.3494), (39.9581, 116.3502), (39.9578, 116.3502), (39.9578, 116.3494), (39.9573, 116.3494), (39.957, 116.3494), (39.9573, 116.3503), (39.9571, 116.3503), (39.9571, 116.3512), (39.9575, 116.3512), (39.9571, 116.3518), (39.957, 116.3518), (39.9568, 116.3518), (39.957, 116.3527), (39.9571, 116.3527), (39.9571, 116.3525), (39.9571, 116.3531), (39.9575, 116.3525), (39.9576, 116.3536), (39.9573, 116.3531), (39.9573, 116.3537), (39.9571, 116.3537), (39.9579, 116.3525), (39.9579, 116.3533), (39.958, 116.3525), (39.958, 116.3512), (39.9584, 116.3525), (39.9584, 116.3534), (39.9581, 116.3534), (39.9584, 116.3548), (39.9581, 116.3546), (39.9573, 116.3546)]

# Example connections between points
connections = [(1, 2), (1, 26), (2, 3), (2, 4), (4, 5), (4, 24), (4, 8), (6, 7), (7, 8), (7, 9), (8, 23), (8, 10), (9, 10), (9, 11), (10, 20), (10, 12), (11, 12), (11, 13), (12, 19), (13, 18), (13, 14), (14, 15), (14, 16), (16, 17), (16, 71), (17, 69), (17, 65), (18, 19), (18, 64), (19, 55), (19, 21), (20, 21), (20, 54), (21, 22), (23, 24), (23, 57), (24, 25), (25, 27), (25, 26), (27, 28), (27, 29), (29, 30), (29, 31), (31, 32), (31, 57), (32, 33), (32, 34), (34, 35), (34, 36), (35, 38), (36, 37), (36, 47), (37, 40), (37, 42), (38, 39), (38, 40), (40, 41), (41, 42), (41, 44), (42, 43), (43, 44), (43, 45), (44, 46), (45, 46), (45, 49), (46, 59), (47, 48), (47, 58), (48, 53), (48, 49), (49, 50), (49, 51), (50, 52), (51, 52), (51, 56), (52, 59), (53, 54), (53, 55), (54, 58), (55, 56), (55, 64), (56, 63), (57, 58), (59, 60), (60, 61), (61, 62), (62, 63), (62, 66), (63, 64), (64, 65), (65, 66), (65, 68), (66, 67), (67, 68), (68, 69), (69, 70), (69, 71), (70, 72), (70, 99), (70, 100), (72, 73), (72, 75), (73, 74), (74, 75), (74, 77), (75, 76), (76, 77), (76, 80), (77, 78), (78, 80), (78, 79), (79, 85), (80, 81), (81, 82), (82, 83), (82, 84), (83, 99), (83, 91), (84, 89), (84, 85), (85, 86), (85, 87), (87, 88), (88, 90), (88, 89), (89, 91), (90, 93), (90, 95), (91, 92), (91, 96), (93, 94), (94, 95), (96, 97), (96, 98), (98, 99), (98, 100), (100, 101), (101, 102), (101, 103), (102, 104), (104, 105), (2, 1), (26, 1), (3, 2), (4, 2), (5, 4), (24, 4), (8, 4), (7, 6), (8, 7), (9, 7), (23, 8), (10, 8), (10, 9), (11, 9), (20, 10), (12, 10), (12, 11), (13, 11), (19, 12), (18, 13), (14, 13), (15, 14), (16, 14), (17, 16), (71, 16), (69, 17), (65, 17), (19, 18), (64, 18), (55, 19), (21, 19), (21, 20), (54, 20), (22, 21), (24, 23), (57, 23), (25, 24), (27, 25), (26, 25), (28, 27), (29, 27), (30, 29), (31, 29), (32, 31), (57, 31), (33, 32), (34, 32), (35, 34), (36, 34), (38, 35), (37, 36), (47, 36), (40, 37), (42, 37), (39, 38), (40, 38), (41, 40), (42, 41), (44, 41), (43, 42), (44, 43), (45, 43), (46, 44), (46, 45), (49, 45), (59, 46), (48, 47), (58, 47), (53, 48), (49, 48), (50, 49), (51, 49), (52, 50), (52, 51), (56, 51), (59, 52), (54, 53), (55, 53), (58, 54), (56, 55), (64, 55), (63, 56), (58, 57), (60, 59), (61, 60), (62, 61), (63, 62), (66, 62), (64, 63), (65, 64), (66, 65), (68, 65), (67, 66), (68, 67), (69, 68), (70, 69), (71, 69), (72, 70), (99, 70), (100, 70), (73, 72), (75, 72), (74, 73), (75, 74), (77, 74), (76, 75), (77, 76), (80, 76), (78, 77), (80, 78), (79, 78), (85, 79), (81, 80), (82, 81), (83, 82), (84, 82), (99, 83), (91, 83), (89, 84), (85, 84), (86, 85), (87, 85), (88, 87), (90, 88), (89, 88), (91, 89), (93, 90), (95, 90), (92, 91), (96, 91), (94, 93), (95, 94), (97, 96), (98, 96), (99, 98), (100, 98), (101, 100), (102, 101), (103, 101), (104, 102), (105, 104)]


# Calculate distances in meters and generate random congestion factors
results = []
for start_id, end_id in connections:
    lat1, lon1 = coordinates[start_id - 1]
    lat2, lon2 = coordinates[end_id - 1]
    distance = haversine_distance_meters(lat1, lon1, lat2, lon2)
    congestion_factor = random.uniform(0.8, 1.0)
    results.append((start_id, end_id, round(distance, 2), round(congestion_factor, 2)))


# Print the results
print(results)
