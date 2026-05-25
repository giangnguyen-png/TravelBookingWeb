/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controllers;

import com.mycompany.pojo.Reviews;
import com.mycompany.services.ReviewService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nguyen
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<Reviews> create(@RequestBody Map<String, String> params) {
        return new ResponseEntity<>(this.reviewService.createReview(params), HttpStatus.CREATED);
    }

    @GetMapping("/providers/{id}/reviews")
    public ResponseEntity<?> list(@PathVariable(value = "id") Long providerId) {
        return new ResponseEntity<>(this.reviewService.getProviderReviewSummary(providerId), HttpStatus.OK);
    }
}
