/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.services;

import com.mycompany.pojo.Reviews;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyen
 */
public interface ReviewService {
    Reviews addReview(Reviews review);
    Reviews createReview(Map<String, String> params);
    List<Reviews> getReviewsByProviderId(Long providerId);
    Double getAverageRatingByProviderId(Long providerId);
    Long countReviewsByProviderId(Long providerId);
    Map<String, Object> getProviderReviewSummary(Long providerId);
}
