package com.example.searc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.searc.dto.RoutePlan;
import com.example.searc.dto.RouteRequest;
import com.example.searc.service.InnerRouteService;
import com.example.searc.dto.FacilitySearchRequest;
import com.example.searc.dto.FacilitySearchResult;

@RestController
@RequestMapping("/api")
public class InnerRouteController {

    @Autowired
    private InnerRouteService routeService;

    @PostMapping("/innerplan")
    public ResponseEntity<RoutePlan> createRoutePlan(@RequestBody RouteRequest request) {
        try {
            // 调用服务层的方法计算路径
            RoutePlan routePlan = routeService.calculateRoute(request);
            return ResponseEntity.ok(routePlan);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/search-facilities")
    public ResponseEntity<List<FacilitySearchResult>> searchFacilities(@RequestBody FacilitySearchRequest request) {
        try {
            List<FacilitySearchResult> results = routeService.findClosestFacilities(request);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
