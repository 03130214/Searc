package com.example.searc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.searc.model.Location;
import com.example.searc.repository.LocationRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public List<Location> getTopLocations(String type) {
        List<Location> allLocations = locationRepository.findAll();
        if (type != null && !type.isEmpty()) {
            allLocations = allLocations.stream()
                    .filter(loc -> loc.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }
        return allLocations.stream()
                .sorted(Comparator.comparing(Location::getPopularity).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
    // 来实现按热度降序排序（即热度最高的在前）。
    // 接着使用 .limit(10) 方法限制结果只返回前10个元素。
    // 最终通过 .collect(Collectors.toList()) 方法将排序和限制后的流收集成一个新的列表并返回。
}
