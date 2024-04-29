package com.main.traveltour.data;

import lombok.*;

import java.sql.Timestamp;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RoomOrder {
    private String orderIds;
    private String roomId;
    private Timestamp checkIn;
    private Timestamp checkOut;
}
