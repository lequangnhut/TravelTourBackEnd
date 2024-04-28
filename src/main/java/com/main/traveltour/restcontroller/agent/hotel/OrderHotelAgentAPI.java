package com.main.traveltour.restcontroller.agent.hotel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.traveltour.dto.agent.hotel.order.OrderHotelDto;
import com.main.traveltour.dto.customer.infomation.OrderHotelsDto;
import com.main.traveltour.entity.OrderHotelDetails;
import com.main.traveltour.entity.OrderHotels;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.service.staff.OrderHotelDetailService;
import com.main.traveltour.service.staff.OrderHotelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class OrderHotelAgentAPI {

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private OrderHotelsService orderHotelsService;

    @Autowired
    private OrderHotelDetailService orderHotelDetailService;


    /**
     * Phương thức tìm kiếm hóa đơn của khách sạn
     *
     * @param hotelId mã khách sạn
     * @return danh sách hóa đơn khách sạn
     */
    @GetMapping("/agent/order-hotel/findAllOrderHotel")
    public ResponseObject findAllOrderHotel(
            @RequestParam("hotelId") String hotelId,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "5", required = false) Integer size,
            @RequestParam(value = "sortField", defaultValue = "id", required = false) String sortField,
            @RequestParam(value = "sortDirection", required = false) Sort.Direction sortDirection,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "filter", required = false) Integer filter

    ) {
        List<String> roomType = roomTypeService.findAllByHotelId(hotelId).stream().map(RoomTypes::getId).toList();
        List<String> orderHotelDetails = orderHotelDetailService.findOrderHotelDetailsByRoomTypeIds(roomType).stream().map(OrderHotelDetails::getOrderHotelId).toList();

        // Xác định hướng sắp xếp mặc định nếu sortDirection là null
        Sort.Direction defaultSortDirection = Sort.Direction.DESC;

        // Xác định hướng sắp xếp cuối cùng
        Sort.Direction finalSortDirection = sortDirection != null ? sortDirection : defaultSortDirection;

        Sort sort = sortField != null ? Sort.by(finalSortDirection, sortField) : null;
        assert sort != null;
        System.out.println(size);
        LocalDateTime targetDateTime;

        switch (filter) {
            case 0:
                return new ResponseObject("200", "success", orderHotelsService.findOrderByIds(orderHotelDetails, PageRequest.of(page, size, sort)));
            case 1:
                targetDateTime = LocalDateTime.now().plusHours(12);
                break;
            case 2:
                targetDateTime = LocalDateTime.now().plusHours(24);
                break;
            case 3:
                targetDateTime = LocalDateTime.now().plusHours(36);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + filter);
        }

// Tạo Pageable sau khi có giá trị targetDateTime
        Pageable pageable = PageRequest.of(page, size);

// Gọi service method dựa trên filter
        return new ResponseObject("200", "success", orderHotelsService.findOrderHotelsAfter12Hours(orderHotelDetails, Timestamp.valueOf(targetDateTime), pageable));

    }

    @GetMapping("agent/order-hotel/findOrderHotelById")
    public ResponseEntity<OrderHotelDto> findOrderHotelById(@RequestParam("orderId") String orderId) {
        OrderHotelDto orderHotelsDto = orderHotelsService.findByOrderHotelId(orderId);
        return ResponseEntity.ok(orderHotelsDto);
    }

    @GetMapping("agent/order-hotel/confirmInvoiceByIdOrder")
    public ResponseEntity confirmInvoiceByIdOrder(@RequestParam("orderId") String orderId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        orderHotelsService.confirmInvoiceByIdOrder(orderId);
        return ResponseEntity.ok(objectMapper.writeValueAsString(Collections.singletonMap("message", "Xác nhận hóa đơn thành công, Vui lòng chuẩn bị phòng trước thời gian khách đến!")));
    }

    @GetMapping("agent/order-hotel/cancelInvoiceByIdOrder")
    public ResponseEntity cancelInvoiceByIdOrder(@RequestParam("orderId") String orderId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        orderHotelsService.cancelInvoiceByIdOrder(orderId);
        return ResponseEntity.ok(objectMapper.writeValueAsString(Collections.singletonMap("message", "Hủy hóa đơn hành công")));
    }
}
