package com.pbl6.hotelbookingapp.controller;

import com.pbl6.hotelbookingapp.dto.*;
import com.pbl6.hotelbookingapp.entity.RoomAmenity;
import com.pbl6.hotelbookingapp.entity.View;
import com.pbl6.hotelbookingapp.service.RoomAmenityService;
import com.pbl6.hotelbookingapp.service.RoomTypeService;
import com.pbl6.hotelbookingapp.service.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/room-types")
@CrossOrigin("${allowed.origins}")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;
    private final RoomAmenityService roomAmenityService;

    private final ViewService viewService;

    public RoomTypeController(RoomTypeService roomTypeService, RoomAmenityService roomAmenityService, ViewService viewService) {
        this.roomTypeService = roomTypeService;
        this.roomAmenityService = roomAmenityService;
        this.viewService = viewService;
    }


    //    @Autowired
//    public RoomTypeController(RoomTypeService roomTypeService) {
//        this.roomTypeService = roomTypeService;
//    }

    @GetMapping(value = "/{roomTypeId}")
    public  RoomTypeDetailResponse getRoomTypeById(@RequestHeader("hotelId") Integer hotelId, @PathVariable Integer roomTypeId) {
        return roomTypeService.findRoomTypeById(hotelId, roomTypeId);
    }

    @GetMapping(value = "")
    public RoomTypesOfHotelResponse getRoomTypesOfHotelResponse(@RequestHeader("hotelId") Integer hotelId) {
        return roomTypeService.findRoomTypesByHotelId(hotelId);
    }

    @GetMapping("/amenities")
    public ResponseEntity<?> getAllRoomAmenities() {
        try {
            List<RoomAmenity> roomAmenities = roomAmenityService.getAllRoomAmenities();

            return ResponseEntity.ok().body(roomAmenities);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST,  consumes = {"multipart/form-data"})
    public AddRoomTypeResponse addRoomType(@RequestHeader("hotelId") Integer hotelId, @ModelAttribute RoomTypeDTO roomTypeDTO) throws IOException {
        return roomTypeService.addRoomType(hotelId, roomTypeDTO);
    }


    @RequestMapping (value = "/{roomTypeId}", method = RequestMethod.PUT, consumes = {"multipart/form-data"})
    public ResponseEntity<String> updateRoomType(@RequestHeader("hotelId") Integer hotelId, @PathVariable Integer roomTypeId, @ModelAttribute RoomTypeDTO roomTypeDTO) {
        try {
            roomTypeService.updateRoomType(hotelId, roomTypeId, roomTypeDTO);
            return new ResponseEntity<>("Room Type updated successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating Room Type", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/price/{roomTypeId}")
    public ResponseEntity<String> updatePrice(@RequestHeader("hotelId") Integer hotelId, @PathVariable Integer roomTypeId, @RequestBody UpdatePriceRoomTypeRequest request) {
        try {
            roomTypeService.updatePrice(hotelId, roomTypeId, request.getPrice());
            return new ResponseEntity<>("Price updated successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating price RoomType", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{roomTypeId}")
    public ResponseEntity<String> deleteRoomType(@RequestHeader("hotelId") Integer hotelId, @PathVariable Integer roomTypeId) {
        roomTypeService.deleteRoomTypeById(hotelId, roomTypeId);
        return ResponseEntity.ok("Room Type deleted successfully");
    }

    @PostMapping("/available-rooms")
    public List<RoomAvailableResponse> getAvailableRooms(@RequestHeader("hotelId") Integer hotelId, @RequestBody RoomAvailableRequest roomAvailableRequest) {
        return roomTypeService.getAvailableRooms(hotelId, roomAvailableRequest);
    }

    @GetMapping("/views")
    public ResponseEntity<List<View>> getAllViews() {
        List<View> views = viewService.getAllViews();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RoomTypeDetailResponse>> searchRoomTypes(
            @RequestHeader Integer hotelId,
            @RequestParam String name) {
        List<RoomTypeDetailResponse> roomTypes = roomTypeService.searchRoomTypesByName(hotelId, name);
        return new ResponseEntity<>(roomTypes, HttpStatus.OK);
    }

}
