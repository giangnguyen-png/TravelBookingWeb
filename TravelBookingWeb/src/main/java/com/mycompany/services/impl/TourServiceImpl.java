/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.services.impl;

import com.mycompany.pojo.Tours;
import com.mycompany.repositories.TourRepository;
import com.mycompany.services.CloudinaryService;
import com.mycompany.services.TourService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author nguyen
 */
@Service
public class TourServiceImpl implements TourService {

    @Autowired
    private TourRepository tourRepo;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public List<Tours> searchTours(Map<String, String> params) {
        return this.tourRepo.searchTours(params);
    }

    @Override
    public List<Tours> getToursByProviderId(Long providerId) {
        return this.tourRepo.getToursByProviderId(providerId);
    }

    @Override
    public List<Tours> getToursByIds(List<Long> ids) {
        return this.tourRepo.getToursByIds(ids);
    }

    @Override
    public Tours getTourById(Long id) {
        return this.tourRepo.getTourById(id);
    }

    @Override
    public Tours addOrUpdateTour(Tours tour) {
        if (tour.getCreatedAt() == null) {
            tour.setCreatedAt(new java.util.Date());
        }
        ServiceValidator.validateTour(tour);
        String thumbnail = this.cloudinaryService.upload(tour.getThumbnailFile(), "travel/tours");
        if (thumbnail != null) {
            tour.setThumbnail(thumbnail);
        }
        return this.tourRepo.addOrUpdateTour(tour);
    }

    @Override
    public void deleteTour(Long id) {
        this.tourRepo.deleteTour(id);
    }
}
