/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.repositories;

import com.mycompany.pojo.Locations;
import java.util.List;

/**
 *
 * @author nguyen
 */
public interface LocationRepository {
    List<Locations> getLocations();
    List<Locations> searchLocations(String keyword);
    Locations getLocationById(Long id);
}
