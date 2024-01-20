package com.main.traveltour.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_types", schema = "travel_tour")
public class RoomTypes {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "room_type_name")
    private String roomTypeName;

    @Basic
    @Column(name = "hotel_id")
    private Integer hotelId;

    @Basic
    @Column(name = "capacity_adults")
    private Integer capacityAdults;

    @Basic
    @Column(name = "capacity_children")
    private Integer capacityChildren;

    @Basic
    @Column(name = "amount_room")
    private Integer amountRoom;

    @Basic
    @Column(name = "price")
    private BigDecimal price;

    @Basic
    @Column(name = "is_active")
    private Boolean isActive;

    @Basic
    @Column(name = "room_type_description")
    private String roomTypeDescription;

    @OneToMany(mappedBy = "roomTypesByRoomTypeId")
    @JsonManagedReference
    private Collection<OrderHotelDetails> orderHotelDetailsById;

    @OneToMany(mappedBy = "roomTypesByRoomTypeId")
    @JsonManagedReference
    private Collection<RoomBeds> roomBedsById;

    @OneToMany(mappedBy = "roomTypesByRoomTypeId")
    @JsonManagedReference
    private Collection<RoomImages> roomImagesById;

    @ManyToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Hotels hotelsByHotelId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "room_type_utilities", joinColumns = {@JoinColumn(name = "room_type_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "room_utilities_id", referencedColumnName = "id")})
    private List<RoomUtilities> roomUtilities = new ArrayList<>();
}
