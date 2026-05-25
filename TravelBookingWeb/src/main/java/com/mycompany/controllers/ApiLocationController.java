/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controllers;

import com.mycompany.pojo.Locations;
import com.mycompany.services.LocationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nguyen
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiLocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/locations")
    public ResponseEntity<List<Locations>> list(@RequestParam(value = "kw", required = false) String kw) {
        if (kw != null && !kw.isBlank()) {
            return new ResponseEntity<>(this.locationService.searchLocations(kw), HttpStatus.OK);
        }

        return new ResponseEntity<>(this.locationService.getLocations(), HttpStatus.OK);
    }
}
