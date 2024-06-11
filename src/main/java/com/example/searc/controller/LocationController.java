package com.example.searc.controller;

import com.example.searc.model.Dishes;
import com.example.searc.model.Location;
import com.example.searc.repository.LocationRepository;
import com.example.searc.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("/api")
public class LocationController {

    @Autowired
    private final LocationService locationService;
    private final LocationRepository locationRepository;

    public LocationController(LocationService locationService, LocationRepository locationRepository) {
        this.locationService = locationService;
        this.locationRepository = locationRepository;
    }
//    @GetMapping("/locations/distance")
//    public double getDistanceBetweenLocations(
//            @RequestParam String name1,
//            @RequestParam String name2) {
//        return locationService.calculateDistance(name1, name2);
//    }
    @GetMapping("/locations")
    public List<Location> getLocations(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category) {
        List<Location> filteredDishes = locationService.getAllLocations();
        if (name != null && !name.isEmpty()) {
            return locationService.findLocationsByName(name,category);
        } else if (type != null && !type.isEmpty()) {
            return locationService.findLocationsByKind(type,category);
        } else {
            return locationService.getTop10HotLocation(filteredDishes);
        }
    }

    @GetMapping("/map")
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/select")
    public List<Location> getTopLocations(@RequestParam(required = false) String type) {
        List<Location> filteredDishes = locationService.getAllLocations();
        if (type != null && type.equals("score"))
            return locationService.getTop10ScoreLocations(filteredDishes);
        else
            return locationService.getTop10HotLocation(filteredDishes);
    }
}

