package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transportation_schedules", schema = "travel_tour")
public class TransportationSchedules {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "transportation_id")
    private Integer transportationId;

    @Basic
    @Column(name = "from_location")
    private String fromLocation;

    @Basic
    @Column(name = "to_location")
    private String toLocation;

    @Basic
    @Column(name = "departure_time")
    private Timestamp departureTime;

    @Basic
    @Column(name = "arrival_time")
    private Timestamp arrivalTime;

    @Basic
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Basic
    @Column(name = "is_active")
    private Boolean isActive;

    @Basic
    @Column(name = "booked_seat")
    private Integer bookedSeat;

    @Basic
    @Column(name = "trip_type")
    private Boolean tripType;

    @Basic
    @Column(name = "is_status")
    private Integer isStatus;

    @OneToMany(mappedBy = "transportationSchedulesByTransportationScheduleId")
    @JsonManagedReference
    private Collection<OrderTransportations> orderTransportationsById;

    @ManyToOne
    @JoinColumn(name = "transportation_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Transportations transportationsByTransportationId;
}
