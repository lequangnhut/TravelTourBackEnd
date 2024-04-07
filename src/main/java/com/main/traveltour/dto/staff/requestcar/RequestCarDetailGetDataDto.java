package com.main.traveltour.dto.staff.requestcar;

import com.main.traveltour.entity.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link RequestCarDetail}
 */
@Data
public class RequestCarDetailGetDataDto implements Serializable {

    Integer id;

    Integer requestCarId;

    String transportationScheduleId;

    Timestamp dateCreated;

    Boolean isAccepted;

    Transportations transportations;

    TransportationBrands transportationBrands;

    RequestCar requestCar;

    TransportationTypes transportationTypes;
}