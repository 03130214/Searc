package com.example.searc.controller;

import com.example.searc.model.InnerLocation;
import com.example.searc.service.InnerLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InnerLocationController {

    @Autowired
    private InnerLocationService locationService;

    @GetMapping("/innerlocations")
    public ResponseEntity<List<InnerLocation>> getAllLocations() {
        List<InnerLocation> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }
}
