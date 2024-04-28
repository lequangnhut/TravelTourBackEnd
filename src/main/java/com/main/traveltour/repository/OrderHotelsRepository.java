package com.main.traveltour.repository;

import com.main.traveltour.dto.agent.hotel.StatisticalBookingHotelDto;
import com.main.traveltour.entity.OrderHotels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderHotelsRepository extends JpaRepository<OrderHotels, Integer> {
    @Query("SELECT COALESCE(MAX(oh.id), 'OH0000') FROM OrderHotels oh")
    String maxCodeTourId();

    @Query("SELECT oh FROM Hotels h " +
            "JOIN h.roomTypesById rt " +
            "JOIN rt.orderHotelDetailsById ohd " +
            "JOIN ohd.orderHotelsByOrderHotelId oh " +
            "JOIN oh.tourDetails td " +
            "WHERE (td.id = :tourDetailId) AND " +
            "(h.id = :hotelId) " +
            "GROUP BY oh.id")
    List<OrderHotels> findOrderHotelByTourDetailIdAndHotelId(@Param("tourDetailId") String tourDetailId,
                                                             @Param("hotelId") String hotelId);

    @Query("SELECT oh FROM OrderHotels oh WHERE (:orderStatus IS NULL OR oh.orderStatus = :orderStatus) AND oh.customerEmail = :email ORDER BY oh.dateCreated DESC")
    Page<OrderHotels> findAllBookingHotelsByUserId(@Param("orderStatus") Integer orderStatus, @Param("email") String email, Pageable pageable);

    OrderHotels findById(String id);

    Page<OrderHotels> findByIdIn(List<String> orderIds, Pageable pageable);

    @Query(value = "SELECT YEAR(o.date_created)                                                                                       AS year,\n" +
            "       MONTH(o.date_created)                                                                                      AS month,\n" +
            "       o.order_status                                                                                             AS orderStatus,\n" +
            "       COUNT(*)                                                                                                   AS orderCount,\n" +
            "       ROUND((COUNT(*) / SUM(COUNT(*)) OVER (PARTITION BY YEAR(o.date_created), MONTH(o.date_created))) * 100,\n" +
            "             1)                                                                                                   AS orderCountPercentage\n" +
            "FROM order_hotels o\n" +
            "         INNER JOIN order_hotel_details od ON o.id = od.order_hotel_id\n" +
            "         INNER JOIN room_types rt ON od.room_type_id = rt.id\n" +
            "WHERE YEAR(o.date_created) = :year\n" +
            "  AND rt.hotel_id = :hotelId\n" +
            "GROUP BY YEAR(o.date_created), MONTH(o.date_created), o.order_status\n" +
            "ORDER BY year, month;", nativeQuery = true)
    List<Object[]> findStatisticalBookingHotel(@Param("year") Integer year, @Param("hotelId") String hotelId);

    @Query(value = "SELECT rt.id                 as id,\n" +
            "       rt.room_type_name     as RoomTypeName,\n" +
            "       YEAR(o.date_created)  AS year,\n" +
            "       MONTH(o.date_created) AS month,\n" +
            "       order_status          AS orderStatus,\n" +
            "       count(*)              as countRoomType\n" +
            "FROM order_hotels o\n" +
            "         INNER JOIN order_hotel_details ON o.id = order_hotel_details.order_hotel_id\n" +
            "         INNER JOIN room_types rt ON order_hotel_details.room_type_id = rt.id\n" +
            "WHERE YEAR(o.date_created) = :year\n" +
            "  AND rt.hotel_id = :hotelId\n" +
            "  AND order_status = 3\n" +
            "GROUP BY YEAR(o.date_created), MONTH(o.date_created), rt.id", nativeQuery = true)
    List<Object[]> statisticalRoomTypeHotel(@Param("year") Integer year, @Param("hotelId") String hotelId);

    @Query(value = "SELECT YEAR(o.date_created)  AS year,\n" +
            "       MONTH(o.date_created) AS month,\n" +
            "       sum(o.order_total)              as countRoomType\n" +
            "FROM order_hotels o\n" +
            "         INNER JOIN order_hotel_details ON o.id = order_hotel_details.order_hotel_id\n" +
            "         INNER JOIN room_types rt ON order_hotel_details.room_type_id = rt.id\n" +
            "WHERE YEAR(o.date_created) = :year\n" +
            "  AND rt.hotel_id = :hotelId\n" +
            "  AND order_status = 3\n" +
            "GROUP BY YEAR(o.date_created), MONTH(o.date_created), rt.id", nativeQuery = true)
    List<Object[]> findHotelRevenueStatistics(@Param("year") Integer year, @Param("hotelId") String hotelId);

    @Query("SELECT DISTINCT YEAR(oh.dateCreated) FROM OrderHotels oh ORDER BY YEAR(oh.dateCreated) DESC")
    List<Integer> getAllOrderHotelYear();

}