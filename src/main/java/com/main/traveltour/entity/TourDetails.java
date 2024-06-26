package com.main.traveltour.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tour_details", schema = "travel_tour")
public class TourDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "tour_id")
    private Integer tourId;

    @Basic
    @Column(name = "guide_id")
    private Integer guideId;

    @Basic
    @Column(name = "departure_date")
    private Date departureDate;

    @Basic
    @Column(name = "arrival_date")
    private Date arrivalDate;

    @Basic
    @Column(name = "number_of_guests")
    private Integer numberOfGuests;

    @Basic
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Basic
    @Column(name = "tour_detail_notes")
    private String tourDetailNotes;

    @Basic
    @Column(name = "tour_detail_status")
    private Integer tourDetailStatus;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;
}
