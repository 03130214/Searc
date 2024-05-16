package com.example.searc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.searc.dto.RoutePlan;
import com.example.searc.dto.RouteRequest;
import com.example.searc.service.RouteService;

@RestController
@RequestMapping("/api/plan")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping
    public ResponseEntity<RoutePlan> createRoutePlan(@RequestBody RouteRequest request) {
        RoutePlan routePlan = routeService.calculateRoute(request);
        
        if (routePlan != null) {
            return ResponseEntity.ok(routePlan);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
