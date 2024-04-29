package com.main.traveltour.service.staff;

import com.main.traveltour.entity.RequestCar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RequestCarService {

    Page<RequestCar> findAllRequestCarPage(Pageable pageable);

    Optional<RequestCar> findRequestCarById(Integer requestCarId);

    RequestCar findTransportBrandSubmitted(Integer requestCarId, String transportBrandId);

    RequestCar save(RequestCar requestCar);

    Page<RequestCar> findAllRequestCarsFilters(
            String fromLocation,
            String toLocation,
            Date dateOfDepartment,
            Date returnDay,
            List<Integer> mediaTypeList,
            List<String> listOfVehicleManufacturers,
            Pageable pageable);
}
