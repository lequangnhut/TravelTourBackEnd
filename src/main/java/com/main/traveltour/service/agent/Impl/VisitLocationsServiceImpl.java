package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.VisitLocations;
import com.main.traveltour.repository.VisitLocationsRepository;
import com.main.traveltour.service.agent.VisitLocationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitLocationsServiceImpl implements VisitLocationsService {

    @Autowired
    private VisitLocationsRepository visitLocationsRepository;

    @Override
    public String findMaxCode() {
        return visitLocationsRepository.findMaxCode();
    }

    @Override
    public VisitLocations findByAgencyId(int userId) {
        return visitLocationsRepository.findByAgenciesId(userId);
    }

    @Override
    public VisitLocations save(VisitLocations visitLocations) {
        return visitLocationsRepository.save(visitLocations);
    }
}