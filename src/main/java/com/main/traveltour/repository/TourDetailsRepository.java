package com.main.traveltour.repository;

import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.entity.Tours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TourDetailsRepository extends JpaRepository<TourDetails, Integer> {

    Optional<TourDetails> findById(String id);

    TourDetails getById(String id);

    @Query("SELECT td FROM TourDetails td WHERE td.tourDetailStatus != 4 ORDER BY td.tourDetailStatus ASC")
    Page<TourDetails> findAllTourDetail(Pageable pageable);

    @Query("SELECT td FROM TourDetails td WHERE " +
            "UPPER(td.toursByTourId.tourName) LIKE %:searchTerm% OR " +
            "UPPER(td.fromLocation) LIKE %:searchTerm% OR " +
            "UPPER(td.toLocation) LIKE %:searchTerm%")
    Page<TourDetails> findTourDetailsByTourNameOrFromLocationOrToLocationContainingIgnoreCase(
            @Param("searchTerm") String searchTerm, Pageable pageable);


    @Query("SELECT COALESCE(MAX(td.id), 'TD0000') FROM TourDetails td")
    String getMaxCodeTourDetailId();

    List<TourDetails> findAllByDepartureDate(Date departureDate);

    List<TourDetails> findAllByArrivalDate(Date arrivalDate);
}