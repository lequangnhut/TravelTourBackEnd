package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.TransportUtilities;
import com.main.traveltour.repository.TransportUtilitiesRepository;
import com.main.traveltour.service.admin.TransportUtilityServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportUtilityServiceImplAD implements TransportUtilityServiceAD {

    @Autowired
    private TransportUtilitiesRepository repo;

    @Override
    public TransportUtilities findTransUtilityADById(int transUtilityId) {
        return repo.findById(transUtilityId);
    }

    @Override
    public List<TransportUtilities> findAllTransUtilityAgent() {
        return repo.findAll();
    }

    @Override
    public Page<TransportUtilities> findAllTransUtilityAD(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public Page<TransportUtilities> findAllWithSearchTransUtilityAD(String searchTerm, Pageable pageable) {
        return repo.findByDescriptionContainingIgnoreCase(searchTerm, pageable);
    }

    @Override
    public TransportUtilities save(TransportUtilities transportUtilities) {
        return repo.save(transportUtilities);
    }

    @Override
    public TransportUtilities delete(TransportUtilities transportUtilities) {
        return null;
    }
}