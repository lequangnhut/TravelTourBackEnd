package com.main.traveltour.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_utilities", schema = "travel_tour")
public class RoomUtilities {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "room_utilities_name")
    private String roomUtilitiesName;

    @ManyToMany(mappedBy = "roomUtilities")
    private List<RoomTypes> roomTypes;
}
