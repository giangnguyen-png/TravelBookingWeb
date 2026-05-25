/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controllers;

import com.mycompany.pojo.HotelRooms;
import com.mycompany.services.HotelRoomService;
import com.mycompany.services.ProviderService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nguyen
 */
@RestController
@RequestMapping("/api/provider")
@CrossOrigin
public class ApiProviderServiceController {

    @Autowired
    private ProviderService providerService;
    @Autowired
    private HotelRoomService roomService;

    @GetMapping("/services")
    public ResponseEntity<?> list(@RequestParam(value = "providerId") Long providerId) {
        return new ResponseEntity<>(this.providerService.getProviderServices(providerId), HttpStatus.OK);
    }

    @PostMapping("/services")
    public ResponseEntity<?> create(@RequestBody Map<String, String> params) {
        return new ResponseEntity<>(this.providerService.addOrUpdateService(null, params), HttpStatus.CREATED);
    }

    @PutMapping("/services/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") Long id, @RequestBody Map<String, String> params) {
        return new ResponseEntity<>(this.providerService.addOrUpdateService(id, params), HttpStatus.OK);
    }

    @DeleteMapping("/services/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") Long id, @RequestParam(value = "providerId") Long providerId) {
        this.providerService.deleteService(id, providerId);
    }

    @GetMapping("/rooms")
    public ResponseEntity<?> rooms(@RequestParam(value = "hotelId") Long hotelId) {
        return new ResponseEntity<>(this.roomService.getRoomsByHotelId(hotelId), HttpStatus.OK);
    }

    @PostMapping("/rooms")
    public ResponseEntity<HotelRooms> createRoom(@RequestBody Map<String, String> params) {
        return new ResponseEntity<>(this.roomService.addOrUpdateRoom(null, params), HttpStatus.CREATED);
    }

    @PutMapping("/rooms/{id}")
    public ResponseEntity<HotelRooms> updateRoom(@PathVariable(value = "id") Long id, @RequestBody Map<String, String> params) {
        return new ResponseEntity<>(this.roomService.addOrUpdateRoom(id, params), HttpStatus.OK);
    }

    @DeleteMapping("/rooms/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable(value = "id") Long id) {
        this.roomService.deleteRoom(id);
    }
}
