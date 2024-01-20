package com.main.traveltour.service.impl.staff;

import com.main.traveltour.entity.Tours;
import com.main.traveltour.repository.ToursRepository;
import com.main.traveltour.service.staff.ToursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToursServiceImpl implements ToursService {

    @Autowired
    private ToursRepository toursRepository;

    @Override
    public List<Tours> findAll() {
        return toursRepository.findAll();
    }

    @Override
    public Page<Tours> findAll(Pageable pageable) {
        return toursRepository.findAll(pageable);
    }

    @Override
    public Page<Tours> findAllWithSearch(String searchTerm, Pageable pageable) {
        return toursRepository.findByTourNameContainingIgnoreCase(searchTerm, pageable);
    }

    @Override
    public Optional<Tours> findById(int tourId) {
        return toursRepository.findById(tourId);
    }

    @Override
    public Tours save(Tours tours) {
        return toursRepository.save(tours);
    }
}
