
package com.mycompany.repositories;

import com.mycompany.pojo.Tours;
import java.util.List;
import java.util.Map;

public interface TourRepository {
    List<Tours> searchTours(Map<String, String> params);
    List<Tours> getToursByProviderId(Long providerId);
    List<Tours> getToursByIds(List<Long> ids);
    Tours getTourById(Long id);
    Tours addOrUpdateTour(Tours tour);
    void deleteTour(Long id);
}
