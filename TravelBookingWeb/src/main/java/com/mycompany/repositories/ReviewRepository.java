
package com.mycompany.repositories;

import com.mycompany.pojo.Reviews;
import java.util.List;

public interface ReviewRepository {
    Reviews addReview(Reviews review);
    List<Reviews> getReviewsByProviderId(Long providerId);
    Double getAverageRatingByProviderId(Long providerId);
    Long countReviewsByProviderId(Long providerId);
}
