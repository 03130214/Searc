package com.example.searc.service;

import com.example.searc.model.InnerLocation;
import com.example.searc.repository.InnerLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InnerLocationService {

    @Autowired
    private InnerLocationRepository repository;

    public List<InnerLocation> getAllLocations() {
        return repository.findAll();
    }
}
