/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.services;

import com.mycompany.enums.VerificationStatus;
import com.mycompany.pojo.ProviderProfiles;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyen
 */
public interface ProviderService {
    ProviderProfiles getProviderById(Long id);
    ProviderProfiles getProviderByUserId(Long userId);
    List<ProviderProfiles> getProvidersByStatus(VerificationStatus status);
    ProviderProfiles addOrUpdateProvider(ProviderProfiles provider);
    void updateVerificationStatus(Long providerId, VerificationStatus status);
    Object getProviderServices(Long providerId);
    Object addOrUpdateService(Long serviceId, Map<String, String> params);
    void deleteService(Long serviceId, Long providerId);
}
