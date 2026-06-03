
package com.mycompany.repositories;

import com.mycompany.pojo.Locations;
import java.util.List;

public interface LocationRepository {
    List<Locations> getLocations();
    List<Locations> searchLocations(String keyword);
    Locations getLocationById(Long id);
}
