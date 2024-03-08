package com.main.traveltour.restcontroller.staff.tour;

import com.main.traveltour.dto.staff.tour.TourDetailsGetDataDto;
import com.main.traveltour.dto.staff.tour.TourTripGetDataDto;
import com.main.traveltour.dto.staff.tour.TourTripsDto;
import com.main.traveltour.dto.staff.tour.TourTypesDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TourTrips;
import com.main.traveltour.service.staff.TourTripsService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/tour-trips/")
public class TourTripsAPI {

    @Autowired
    private TourTripsService tourTripsService;

    @Autowired
    private FileUpload fileUpload;

    @GetMapping("find-all-tourTrips/{tourId}")
    private ResponseObject findAllTourTrips(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "null") String tourId) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<TourTrips> tourTripPage = tourTripsService.findTourTripsByTourId(tourId, PageRequest.of(page, size, sort));
        Page<TourTripGetDataDto> tourTripGetDataDto = tourTripPage.map(tourTrips -> EntityDtoUtils.convertToDto(tourTrips, TourTripGetDataDto.class));

        if (tourTripGetDataDto.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourTripGetDataDto);
        }
    }

    @GetMapping("find-tourTrips-by-tourId/{tourId}")
    private ResponseObject findTourTripsByTourId(@RequestParam(defaultValue = "null") String tourId) {
        List<TourTrips> items = tourTripsService.findTourTripsByTourId(tourId);

        if (items.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", items);
        }
    }

    @GetMapping("find-by-id/{id}")
    public ResponseObject findById(@PathVariable int id) {
        Optional<TourTrips> tourTrips = tourTripsService.findById(id);

        if (tourTrips.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", tourTrips);
        }
    }

    @PostMapping("create-tourTrips")
    public ResponseObject createTourTrips(@RequestPart TourTripsDto tourTripsDto, @RequestPart MultipartFile placeImage, @RequestPart String timeGo) {
        try {
            String imagesPath = fileUpload.uploadFile(placeImage);
            Time placeTimeGo = TimeUtils.parseTime(timeGo);
            int dayInTripIsMax = tourTripsService.getDayInTripIsMax(tourTripsDto.getTourDetailId());

            TourTrips tourTrips = EntityDtoUtils.convertToEntity(tourTripsDto, TourTrips.class);
            tourTrips.setPlaceImage(imagesPath);
            tourTrips.setTimeGo(placeTimeGo);
            tourTrips.setDayInTrip(dayInTripIsMax + 1);
            tourTripsService.save(tourTrips);
            return new ResponseObject("200", "Thêm mới thành công", "thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject("500", "Thêm mới thất bại", "thất bại");
        }
    }

    @PutMapping("update-tourTrips/{id}")
    public ResponseObject updateTourTrips(@PathVariable int id,
                                          @RequestPart TourTripsDto tourTripsDto,
                                          @RequestPart(required = false) MultipartFile placeImage,
                                          @RequestParam("timeGo") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeGo) {
        try {
            Optional<TourTrips> tripsOptional = tourTripsService.findById(id);

            tourTripsDto.setId(id);
            TourTrips tourTrips = EntityDtoUtils.convertToEntity(tourTripsDto, TourTrips.class);
            tourTrips.setTimeGo(Time.valueOf(timeGo));
            if (placeImage != null) {
                String imagesPath = fileUpload.uploadFile(placeImage);
                tourTrips.setPlaceImage(imagesPath);
            } else {
                tourTrips.setPlaceImage(tripsOptional.get().getPlaceImage());
            }
            TourTrips updatedTourDetail = tourTripsService.save(tourTrips);
            return new ResponseObject("200", "Cập nhật thành công", updatedTourDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject("500", "Cập nhật thất bại", null);
        }
    }

    @DeleteMapping("delete-tourTrips/{id}")
    public ResponseObject deleteTourTrips(@PathVariable int id) {
        try {
            TourTrips TourTrips = tourTripsService.getById(id);
            tourTripsService.delete(TourTrips);
            return new ResponseObject("204", "Xóa thành công", null);
        } catch (Exception e) {
            return new ResponseObject("500", "Xó    a thất bại", null);
        }
    }
}
