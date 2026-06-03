
package com.mycompany.services;

import com.mycompany.pojo.Reviews;
import java.util.List;
import java.util.Map;

public interface ReviewService {
    Reviews addReview(Reviews review);
    Reviews createReview(Map<String, String> params);
    List<Reviews> getReviewsByProviderId(Long providerId);
    Double getAverageRatingByProviderId(Long providerId);
    Long countReviewsByProviderId(Long providerId);
    Map<String, Object> getProviderReviewSummary(Long providerId);
}
