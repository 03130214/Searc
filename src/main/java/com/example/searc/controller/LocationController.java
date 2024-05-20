package com.example.searc.controller;

import com.example.searc.model.Location;
import com.example.searc.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/map")
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/select")
    public List<Location> getTopLocations(@RequestParam(required = false) String type) {
        return locationService.getTopLocations(type);
    }
}

