package com.main.traveltour.repository;

import com.main.traveltour.entity.TourDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
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

    @Query("SELECT td " +
            "FROM TourDetails td " +
            "JOIN td.toursByTourId t " +
            "JOIN t.tourTypesByTourTypeId ty " +
            "WHERE (:fromLocation IS NULL OR td.fromLocation LIKE :fromLocation) " +
            "AND (:departureDate IS NULL OR td.departureDate >= :arrivalDate) " +
            "AND (:arrivalDate IS NULL OR td.arrivalDate <= :arrivalDate) " +
            "AND (:price IS NULL OR td.unitPrice <= :price) " +
            "AND (coalesce(:tourTypesByTourTypeId) IS NULL OR ty.id IN (:tourTypesByTourTypeId)) ")
    Page<TourDetails> findTTourDetailWithFilter(
            @Param("fromLocation") String fromLocation,
            @Param("departureDate") Timestamp departureDate,
            @Param("arrivalDate") Timestamp arrivalDate,
            @Param("price") Integer price,
            @Param("tourTypesByTourTypeId") List<Integer> tourTypesByTourTypeId,
            Pageable pageable);
}