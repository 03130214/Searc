package com.example.searc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.searc.model.Location;
import com.example.searc.repository.LocationRepository;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
}
