package com.example.searc.service;

import com.example.searc.model.Dishes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.searc.model.Location;
import com.example.searc.repository.LocationRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;
    public Location findByName(String name) {
        return locationRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public List<Location> findLocationsByName(String name, String category) {
        return locationRepository.findByNameContainingIgnoreCase(name).stream()
                .sorted(getComparator(category))
                .collect(Collectors.toList());
    }

    public List<Location> findLocationsByKind(String type, String category) {
        return locationRepository.findByTypeContainingIgnoreCase(type).stream()
                .sorted(getComparator(category))
                .collect(Collectors.toList());
    }
    private Comparator<Location> getComparator(String category) {
        if ("popularity".equalsIgnoreCase(category)) {
            return Comparator.comparingLong(Location::getPopularity).reversed();
        } else if ("score".equalsIgnoreCase(category)) {
            return Comparator.comparingDouble(Location::getScore).reversed();
        } else {
            return Comparator.comparingLong(Location::getPopularity).reversed();
        }
    }
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

public List<Location> getTop10HotLocation(List<Location> allLocation) {
    List<Location> top10Location = new ArrayList<>();
    if (allLocation.size() <= 10) {
        return allLocation;
    }

    int k = 10;
    quickSelect(allLocation, 0, allLocation.size() - 1, k);

    for (int i = 0; i < k; i++) {
        top10Location.add(allLocation.get(i));
    }

    return top10Location;
}

    private void quickSelect(List<Location> location, int left, int right, int k) {//for hot
        int pivotIndex = partition(location, left, right);
        if (pivotIndex == k - 1) {
            return;
        } else if (pivotIndex > k - 1) {
            quickSelect(location, left, pivotIndex - 1, k);
        } else {
            quickSelect(location, pivotIndex + 1, right, k);
        }
    }
    private void quickSelect_score(List<Location> location, int left, int right, int k) {//for score
        int pivotIndex = partition_score(location, left, right);
        if (pivotIndex == k - 1) {
            return;
        } else if (pivotIndex > k - 1) {
            quickSelect_score(location, left, pivotIndex - 1, k);
        } else {
            quickSelect_score(location, pivotIndex + 1, right, k);
        }
    }

    private int partition(List<Location> location, int left, int right) {
        Location pivot = location.get(right);
        int i = left;
        for (int j = left; j < right; j++) {
            if (location.get(j).getPopularity() >= pivot.getPopularity()) {
                Collections.swap(location, i, j);
                i++;
            }
        }
        Collections.swap(location, i, right);
        return i;
    }
    private int partition_score(List<Location> location, int left, int right) {//for score
        Location pivot = location.get(right);
        int i = left;
        for (int j = left; j < right; j++) {
            if (location.get(j).getScore() >= pivot.getScore()) {
                Collections.swap(location, i, j);
                i++;
            }
        }
        Collections.swap(location, i, right);
        return i;
    }

    public List<Location> getTop10ScoreLocations(List<Location> allLocation) {
        List<Location> top10Location = new ArrayList<>();
        if (allLocation.size() <= 10) {
            return allLocation;
        }

        int k = 10;
        quickSelect_score(allLocation, 0, allLocation.size() - 1, k);

        for (int i = 0; i < k; i++) {
            top10Location.add(allLocation.get(i));
        }

        return top10Location;
    }
}
