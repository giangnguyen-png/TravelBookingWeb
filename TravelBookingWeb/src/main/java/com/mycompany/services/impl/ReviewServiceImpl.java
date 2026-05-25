/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.services.impl;

import com.mycompany.pojo.Reviews;
import com.mycompany.repositories.ProviderRepository;
import com.mycompany.repositories.ReviewRepository;
import com.mycompany.repositories.UserRepository;
import com.mycompany.services.ReviewService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author nguyen
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ProviderRepository providerRepo;

    @Override
    public Reviews addReview(Reviews review) {
        return this.reviewRepo.addReview(review);
    }

    @Override
    public Reviews createReview(Map<String, String> params) {
        Reviews review = new Reviews();
        review.setCustomerId(this.userRepo.getUserById(Long.valueOf(params.get("customerId"))));
        review.setProviderId(this.providerRepo.getProviderById(Long.valueOf(params.get("providerId"))));
        review.setRating(Integer.parseInt(params.get("rating")));
        review.setComment(params.get("comment"));
        review.setCreatedAt(new Date());

        return this.reviewRepo.addReview(review);
    }

    @Override
    public List<Reviews> getReviewsByProviderId(Long providerId) {
        return this.reviewRepo.getReviewsByProviderId(providerId);
    }

    @Override
    public Double getAverageRatingByProviderId(Long providerId) {
        return this.reviewRepo.getAverageRatingByProviderId(providerId);
    }

    @Override
    public Long countReviewsByProviderId(Long providerId) {
        return this.reviewRepo.countReviewsByProviderId(providerId);
    }

    @Override
    public Map<String, Object> getProviderReviewSummary(Long providerId) {
        Map<String, Object> data = new HashMap<>();
        data.put("items", this.reviewRepo.getReviewsByProviderId(providerId));
        data.put("averageRating", this.reviewRepo.getAverageRatingByProviderId(providerId));
        data.put("totalReviews", this.reviewRepo.countReviewsByProviderId(providerId));
        return data;
    }
}
