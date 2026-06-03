
package com.mycompany.services;

import com.mycompany.pojo.Locations;
import java.util.List;

public interface LocationService {
    List<Locations> getLocations();
    List<Locations> searchLocations(String keyword);
    Locations getLocationById(Long id);
}
