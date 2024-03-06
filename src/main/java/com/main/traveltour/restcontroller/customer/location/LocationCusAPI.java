package com.main.traveltour.restcontroller.customer.location;

import com.main.traveltour.dto.staff.VisitLocationsDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.VisitLocationTypes;
import com.main.traveltour.entity.VisitLocations;
import com.main.traveltour.service.agent.VisitLocationTypeService;
import com.main.traveltour.service.staff.VisitLocationService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/")
public class LocationCusAPI {

    @Autowired
    private VisitLocationService visitLocationService;

    @Autowired
    private VisitLocationTypeService visitLocationTypeService;

    @GetMapping("customer/location/find-all-location")
    private ResponseObject findAllLocation(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(required = false) String searchTerm) {
        Page<VisitLocations> visitLocations = searchTerm != null && !searchTerm.isEmpty()
                ? visitLocationService.findBySearchTerm(searchTerm, PageRequest.of(page, size))
                : visitLocationService.getAllByIsActiveIsTrueAndIsAcceptedIsTrue(PageRequest.of(page, size));

        Page<VisitLocationsDto> visitLocationsDto = visitLocations.map(visitLocation -> EntityDtoUtils.convertToDto(visitLocation, VisitLocationsDto.class));

        if (visitLocationsDto.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocationsDto);
        }
    }

    @GetMapping("customer/location/find-all-location-type")
    private ResponseObject findAllLocationType() {
        List<VisitLocationTypes> visitLocationTypes = visitLocationTypeService.findAllForRegisterAgency();

        if (visitLocationTypes.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", visitLocationTypes);
        }
    }
}
