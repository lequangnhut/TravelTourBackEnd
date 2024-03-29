package com.main.traveltour.repository;

import com.main.traveltour.dto.customer.visit.VisitLocationTrendDTO;
import com.main.traveltour.entity.Hotels;
import com.main.traveltour.entity.VisitLocations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

public interface VisitLocationsRepository extends JpaRepository<VisitLocations, Integer> {

    @Query(value = "SELECT MAX(pl.id) FROM VisitLocations pl")
    String findMaxCode();

    VisitLocations findByAgenciesId(int userId);

    VisitLocations findById(String visitLocationsId);

    List<VisitLocations> findAllByAgenciesIdAndIsActiveTrue(int userId);

    List<VisitLocations> findAllByVisitLocationTypeId(int id);

    List<VisitLocations> findAllById(String id);

    VisitLocations findByIdAndIsActiveIsTrue(String id);

    @Query("SELECT vl FROM VisitLocations vl " +
            "JOIN vl.visitLocationTicketsById vlt " +
            "WHERE vl.isActive = TRUE AND vl.isAccepted = TRUE " +
            "GROUP BY vl.id")
    Page<VisitLocations> getAllByIsActiveIsTrueAndIsAcceptedIsTrue(Pageable pageable);

    @Query("SELECT vl FROM VisitLocations vl " +
            "JOIN vl.visitLocationTicketsById vlt " +
            "WHERE UPPER(vl.visitLocationName) LIKE %:searchTerm% OR " +
            "UPPER(vl.phone) LIKE %:searchTerm% OR " +
            "UPPER(vl.province) LIKE %:searchTerm% OR " +
            "UPPER(vl.district) LIKE %:searchTerm% OR " +
            "UPPER(vl.ward) LIKE %:searchTerm% AND " +
            "UPPER(vlt.ticketTypeName) LIKE %:searchTerm% AND " +
            "UPPER(CAST(vlt.unitPrice AS string)) LIKE %:searchTerm% AND " +
            "vl.isActive = TRUE AND vl.isAccepted = TRUE " +
            "GROUP BY vl.id")
    Page<VisitLocations> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT vl FROM VisitLocations vl " +
            "JOIN vl.visitLocationTicketsById vlt " +
            "WHERE (:searchTerm IS NULL OR " +
            "(UPPER(vl.visitLocationName) LIKE %:searchTerm% OR " +
            "UPPER(vl.phone) LIKE %:searchTerm% OR " +
            "UPPER(vl.province) LIKE %:searchTerm% OR " +
            "UPPER(vl.district) LIKE %:searchTerm% OR " +
            "UPPER(vl.ward) LIKE %:searchTerm% AND " +
            "UPPER(vlt.ticketTypeName) LIKE %:searchTerm% AND " +
            "UPPER(vl.agenciesByAgenciesId.nameAgency) LIKE %:searchTerm%)) AND " +
            "(vl.isActive = TRUE AND vl.isAccepted = TRUE) AND " +
            "(vlt.unitPrice <= :price) AND " +
            "(:tickerTypeList IS NULL OR vlt.ticketTypeName IN (:tickerTypeList)) AND " +
            "(:locationTypeList IS NULL OR vl.visitLocationTypesByVisitLocationTypeId.id IN (:locationTypeList))" +
            "GROUP BY vl.id")
    Page<VisitLocations> findByFilters(
            @Param("searchTerm") String searchTerm,
            @Param("price") BigDecimal price,
            @Param("tickerTypeList") List<String> tickerTypeList,
            @Param("locationTypeList") List<Integer> locationTypeList,
            Pageable pageable);


    @Query("SELECT vl FROM VisitLocations vl " +
            "JOIN vl.visitLocationTicketsById vlt " +
            "WHERE (:location IS NULL OR UPPER(vl.province) LIKE CONCAT('%', UPPER(:location), '%'))AND " +
            "vl.isActive = TRUE AND vl.isAccepted = TRUE " +
            "GROUP BY vl.id")
    Page<VisitLocations> findVisitLocationsByProvince(
            @Param("location") String location,
            Pageable pageable);


    @Query("SELECT new com.main.traveltour.dto.customer.visit.VisitLocationTrendDTO(vl.id, vl.visitLocationName, vl.visitLocationImage, COUNT(ov)   ) " +
            "FROM VisitLocations vl " +
            "JOIN vl.orderVisitsById ov " +
            "WHERE vl.isActive = TRUE AND vl.isAccepted = TRUE " +
            "GROUP BY vl.id, vl.visitLocationName, vl.visitLocationImage " +
            "ORDER BY COUNT(ov) DESC LIMIT 5")
    List<VisitLocationTrendDTO> findVisitLocationsTrend();

    //fill tham quan theo tour

    @Query("SELECT vl FROM VisitLocations vl " +
            "JOIN vl.orderVisitsById ov " +
            "JOIN ov.orderVisitDetailsById ovd " +
            "JOIN ov.tourDetails td " +
            "WHERE td.id = :tourDetailId AND " +
            "ov.orderStatus = :orderVisitStatus AND " +
            "(:searchTerm IS NULL OR (UPPER(vl.visitLocationName) LIKE CONCAT('%', UPPER(:searchTerm), '%') OR " +
            "UPPER(vl.province) LIKE CONCAT('%', UPPER(:searchTerm), '%') OR " +
            "UPPER(vl.district) LIKE CONCAT('%', UPPER(:searchTerm), '%') OR " +
            "UPPER(vl.ward) LIKE CONCAT('%', UPPER(:searchTerm), '%') OR " +
            "UPPER(vl.address) LIKE CONCAT('%', UPPER(:searchTerm), '%') OR " +
            "UPPER(vl.phone) LIKE CONCAT('%', UPPER(:searchTerm), '%'))) " +
            "GROUP BY vl.id")
    Page<VisitLocations> findVisitByTourDetailId(@Param("tourDetailId") String tourDetailId,
                                                 @Param("orderVisitStatus") Integer orderVisitStatus,
                                                 @Param("searchTerm") String searchTerm,
                                                 Pageable pageable);


    @Query("SELECT v FROM VisitLocations v join v.agenciesByAgenciesId ag" +
            " WHERE (v.isAccepted = :isAccepted) and (v.isActive = true) and " +
            "(ag.isActive = true) and (ag.isAccepted) = 2")
    Page<VisitLocations> findAllVisitPostByAcceptedAndTrueActive(@Param("isAccepted") Boolean isAccepted, Pageable pageable);

    @Query("SELECT v FROM VisitLocations v  join v.agenciesByAgenciesId ag " +
            "WHERE (v.isAccepted = :isAccepted) and (v.isActive = true) " +
            "and LOWER(v.visitLocationName) " +
            "LIKE LOWER(CONCAT('%', :searchTerm, '%')) and (ag.isActive) = true and (ag.isAccepted) = 2")
    Page<VisitLocations> findAllVisitPostByAcceptedAndTrueActiveByName(@Param("isAccepted") Boolean isAccepted, Pageable pageable, String searchTerm);

    @Query("SELECT COUNT(vs) FROM VisitLocations vs")
    Long countVisitLocations ();
}