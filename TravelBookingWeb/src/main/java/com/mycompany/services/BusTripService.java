
package com.mycompany.services;

import com.mycompany.pojo.BusTrips;
import java.util.List;
import java.util.Map;

public interface BusTripService {
    List<BusTrips> searchBusTrips(Map<String, String> params);
    List<BusTrips> getBusTripsByProviderId(Long providerId);
    List<BusTrips> getBusTripsByIds(List<Long> ids);
    BusTrips getBusTripById(Long id);
    BusTrips addOrUpdateBusTrip(BusTrips busTrip);
    void deleteBusTrip(Long id);
}
