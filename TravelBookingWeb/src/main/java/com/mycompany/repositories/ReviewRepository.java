/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.repositories;

import com.mycompany.pojo.Reviews;
import java.util.List;

/**
 *
 * @author nguyen
 */
public interface ReviewRepository {
    Reviews addReview(Reviews review);
    List<Reviews> getReviewsByProviderId(Long providerId);
    Double getAverageRatingByProviderId(Long providerId);
    Long countReviewsByProviderId(Long providerId);
}
