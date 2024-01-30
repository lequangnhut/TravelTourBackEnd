package com.main.traveltour.restcontroller.agent.transport;

import com.main.traveltour.component.ByteArrayMultipartFile;
import com.main.traveltour.dto.agent.ExportDataOrderTransportDto;
import com.main.traveltour.dto.agent.OrderTransportationsDto;
import com.main.traveltour.entity.OrderTransportations;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.service.agent.OrderTransportService;
import com.main.traveltour.service.agent.TransportationScheduleService;
import com.main.traveltour.service.utils.FileUpload;
import com.main.traveltour.service.utils.QRCodeService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateNextID;
import com.main.traveltour.utils.ReplaceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class OrderTransportAPI {

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private OrderTransportService orderTransportService;

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @GetMapping("/agent/order-transport/find-all-order-transport/{brandId}")
    private ResponseEntity<Page<OrderTransportations>> findAllTransportBrand(@RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size,
                                                                             @RequestParam(defaultValue = "id") String sortBy,
                                                                             @RequestParam(defaultValue = "asc") String sortDir,
                                                                             @RequestParam(required = false) String searchTerm,
                                                                             @PathVariable String brandId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<OrderTransportations> orderTransportations = searchTerm == null || searchTerm.isEmpty()
                ? orderTransportService.findAllOrderTransport(brandId, PageRequest.of(page, size, sort))
                : orderTransportService.findAllOrderTransportWithSearch(brandId, searchTerm, PageRequest.of(page, size, sort));
        return new ResponseEntity<>(orderTransportations, HttpStatus.OK);
    }

    @GetMapping("/agent/order-transport/find-by-orderTransId/{orderTransId}")
    private ResponseObject findByOrderTransId(@PathVariable String orderTransId) {
        Map<String, Object> response = new HashMap<>();
        OrderTransportations orderTransportations = orderTransportService.findById(orderTransId);
        ExportDataOrderTransportDto exportDataOrderTransportDto = EntityDtoUtils.convertToDto(orderTransportations, ExportDataOrderTransportDto.class);

        response.put("orderTransportations", orderTransportations);
        response.put("exportDataOrderTransportDto", exportDataOrderTransportDto);
        response.put("price", ReplaceUtils.formatPrice(orderTransportations.getOrderTotal()));
        return new ResponseObject("200", "Đã tìm thấy dữ liệu", response);
    }

    @GetMapping("/agent/order-transport/find-all-schedule-by-brandId/{brandId}")
    private ResponseObject findAllScheduleByBrandId(@PathVariable String brandId) {
        List<TransportationSchedules> transportationSchedules = transportationScheduleService.findAllScheduleByBrandId(brandId);

        if (transportationSchedules == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationSchedules);
        }
    }

    @GetMapping("/agent/order-transport/find-schedule-by-id/{scheduleId}")
    private ResponseObject findScheduleById(@PathVariable String scheduleId) {
        TransportationSchedules transportationSchedules = transportationScheduleService.findBySchedulesId(scheduleId);

        if (transportationSchedules == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", transportationSchedules);
        }
    }

    @PostMapping("/agent/order-transport/create-order-transport")
    private void createOrderTransport(@RequestBody OrderTransportationsDto orderTransportationsDto) {
        String orderTransportId = GenerateNextID.generateNextCode("OTR", orderTransportService.findMaxCode());
        String transportScheduleId = orderTransportationsDto.getTransportationScheduleId();

        OrderTransportations orderTransportations = EntityDtoUtils.convertToEntity(orderTransportationsDto, OrderTransportations.class);
        orderTransportations.setId(orderTransportId);
        orderTransportations.setOrderStatus(0); // 0 là đã tạo vé
        orderTransportations.setOrderTotal(ReplaceUtils.parseMoneyString(orderTransportationsDto.getPriceFormat()));
        orderTransportations.setOrderCode(generateQrCode(orderTransportId));
        orderTransportService.save(orderTransportations);

        TransportationSchedules schedules = transportationScheduleService.findBySchedulesId(transportScheduleId);
        schedules.setBookedSeat(schedules.getBookedSeat() + orderTransportationsDto.getAmountTicket());
        transportationScheduleService.save(schedules);
    }

    @PutMapping("/agent/order-transport/update-order-transport")
    private void updateOrderTransport(@RequestBody OrderTransportationsDto orderTransportationsDto) {
        String orderTransportId = orderTransportationsDto.getId();
        String transportScheduleId = orderTransportationsDto.getTransportationScheduleId();

        OrderTransportations orderTrans = orderTransportService.findById(orderTransportId);
        OrderTransportations orderTransportations = EntityDtoUtils.convertToEntity(orderTransportationsDto, OrderTransportations.class);
        orderTransportations.setId(orderTransportationsDto.getId());
        orderTransportations.setOrderCode(generateQrCode(orderTransportationsDto.getId()));
        if (orderTransportationsDto.getPriceFormat().contains(",")) {
            orderTransportations.setOrderTotal(ReplaceUtils.replacePrice(orderTransportationsDto.getPriceFormat()));
        } else {
            orderTransportations.setOrderTotal(ReplaceUtils.parseMoneyString(orderTransportationsDto.getPriceFormat()));
        }
        orderTransportService.save(orderTransportations);

        TransportationSchedules schedules = transportationScheduleService.findBySchedulesId(transportScheduleId);
        schedules.setBookedSeat(schedules.getBookedSeat() + orderTransportationsDto.getAmountTicket() - orderTrans.getAmountTicket());
        transportationScheduleService.save(schedules);
    }

    @GetMapping("/agent/order-transport/delete-order-transport/{orderTransportId}")
    private void deleteOrderTransport(@PathVariable String orderTransportId) {
        OrderTransportations orderTransportations = orderTransportService.findById(orderTransportId);
        orderTransportations.setOrderStatus(1); // đã hủy vé
        orderTransportService.save(orderTransportations);
    }

    private String generateQrCode(String orderTransportId) {
        String targetUrl = "http://localhost:3000/home/see-ticket-informatione/" + orderTransportId;
        BufferedImage qrCodeImage = qrCodeService.generateQRCodeImage(targetUrl);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ImageIO.write(qrCodeImage, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] imageBytes = baos.toByteArray();

        try {
            MultipartFile multipartFile = new ByteArrayMultipartFile(imageBytes, "qr-code.png");
            return fileUpload.uploadFile(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
