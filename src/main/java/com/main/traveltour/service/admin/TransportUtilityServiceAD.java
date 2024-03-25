package com.main.traveltour.service.admin;

import com.main.traveltour.entity.TransportUtilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransportUtilityServiceAD {

    TransportUtilities findTransUtilityADById(int transUtilityId);

    List<TransportUtilities> findAllTransUtilityAgent();

    Page<TransportUtilities> findAllTransUtilityAD(Pageable pageable);

    Page<TransportUtilities> findAllWithSearchTransUtilityAD(String searchTerm, Pageable pageable);

    TransportUtilities save(TransportUtilities transportUtilities);

    TransportUtilities delete(TransportUtilities transportUtilities);
}
