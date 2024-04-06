package com.main.traveltour.dto.admin.post;

import com.main.traveltour.entity.TransportationImage;
import com.main.traveltour.entity.TransportationTypes;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * DTO for {@link com.main.traveltour.entity.Transportations}
 */
@Data
public class TransportationsDto implements Serializable {
    String id;

    String transportationBrandId;

    Integer transportationTypeId;

    String transportationImg;

    String licensePlate;

    Integer amountSeat;

    Timestamp dateCreated;

    Boolean isActive;

    Boolean isTransportBed;

    Integer columnSeat;

    TransportationTypes transportationTypes;

    Collection<TransportationImage> transportationImagesById;
}